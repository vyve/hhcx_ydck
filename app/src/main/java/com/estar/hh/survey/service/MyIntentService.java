package com.estar.hh.survey.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.request.ImageUploadRequest;
import com.estar.hh.survey.entity.response.ImageUploadResponse;
import com.estar.hh.survey.entity.vo.ImageVO;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.HttpUtils;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.utils.ToastUtils;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *图片上传服务
 * Created by ding on 2017/1/13.
 */
public class MyIntentService extends IntentService {
    AlarmManager am = null; // 提醒管理类
    private int pertime = 180; // 定位间隔 单位：秒
    PendingIntent pendingIntent;
    private String userCode = ""; // 工号
    /**
     * 发送的URL
     **/
    private String resultjson = "";
//    private RequestUploadImageDTO sendImageVO = new RequestUploadImageDTO();
    private UserSharePrefrence userSharePrefrence = null;
    private ImageUploadRequest request = new ImageUploadRequest();
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private static final String ACTION_FOO = "com.estar.common.service.action.FOO";
    private static final String ACTION_BAZ = "com.estar.common.service.action.BAZ";

    private static final String EXTRA_PARAM1 = "com.estar.common.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.estar.common.service.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userSharePrefrence = new UserSharePrefrence(this);
        userCode = userSharePrefrence.getUserEntity().getEmpCde();
        notifyService(); // 开启闹铃服务
        LogUtils.w("MyIntentService  onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtils.w("MyIntentService  onHandleIntent");
        List<ImageVO> imageList = null;
        if (intent != null) {
            try {
                Map<String, List<ImageVO>> map = (Map<String, List<ImageVO>>) intent.getExtras().get("map");
                imageList = map.get("imageList");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (null == imageList) {
            try {
                imageList = DataSupport.where("usercode=? and uploadtimes<? and upcliamflag=? ", userCode, "3", "1").find(ImageVO.class);
                LogUtils.w("usercode=" + userCode);
                LogUtils.w("imageList.size()=" + imageList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogUtils.w("imageList.size()=" + imageList.size());

        for (final ImageVO imageVO : imageList) {
            // 文件存在
            if (new File(imageVO.getImagePath()).exists()) {
                try {
                    String imgHashCode = getMD5Checksum(imageVO.getImagePath());
                    String imageBase64 = ConvertBase64HH(imageVO.getImagePath());
                    imageBase64 = imageBase64.replace("+", "%2B");
                    LogUtils.i("图片上传hash码为:", "-------------------------------------------\n" + imgHashCode + "-------------名称为:" + imageVO.getImagename());
                    if (null != imageBase64 && !imageBase64.equals("")) {// 如果图片64位码为空

                        request.getData().setTaskno(imageVO.getTaskno());
                        LogUtils.i("Taskno:", "--------" + imageVO.getTaskno());
                        request.getData().setReportno(imageVO.getReportno());
                        LogUtils.i("Reportno:", "--------" + imageVO.getReportno());
                        request.getData().setUsercode(imageVO.getUsercode());
                        request.getData().setUploadType(imageVO.getUploadtype());//移动查勘图片
                        request.getData().setImgHashCode(imgHashCode);
                        request.getData().setImageData(imageBase64);

                        request.getData().setFileName(imageVO.getImagename());
                        request.getData().setPicDtl(imageVO.getPicDtl());
                        request.getData().setPicCls(imageVO.getPicCls());

                        final Gson gson = new Gson();
                        String reqMsg = gson.toJson(request);
                        int tryNum = 0;

//                        RequestParams params = HttpUtils.buildRequestParam(Constants.IMAGEUPLOAD, reqMsg);
                        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.IMAGEUPLOAD, reqMsg);

                        params.setConnectTimeout(30*1000);//设置请求超时

                        LogUtils.i("图片上传请求参数为:", "-------------------------------------------\n" + reqMsg);

                        x.http().post(params, new Callback.CacheCallback<String>() {
                            @Override
                            public boolean onCache(String result) {
                                return false;
                            }

                            @Override
                            public void onSuccess(String result) {
                                LogUtils.i("图片上传返回参数为:", "--------------------------------------------\n" + result);
                                ImageUploadResponse response = gson.fromJson(result, ImageUploadResponse.class);
                                if (response.getSuccess()){
                                    ImageVO updataImageVO = new ImageVO();
                                    updataImageVO.setUpcliamflag(2);// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败
                                    updataImageVO.setIsFirstUpload("1");//手机是否首次上传 0首次 1补传
                                    imageVO.setIsFirstUpload("1");//手机是否首次上传 0首次 1补传
                                    updataImageVO.updateAll("imagename= ? ", imageVO.getImagename());
                                }else{
                                    ImageVO updataImageVO = new ImageVO();
                                    updataImageVO.setIsFirstUpload("1");//手机是否首次上传 0首次 1补传
                                    updataImageVO.setUpcliamflag(3);// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败
                                    updataImageVO.setMsg(response.getMsg());//更新错误信息
                                    LogUtils.w("上传失败 更新数据库=" + imageVO.getImagename());
                                    updataImageVO.updateAll("imagename = ? ", imageVO.getImagename());
                                    LogUtils.w("更新完成");
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                LogUtils.i("图片上传请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                                ToastUtils.showShort(MyIntentService.this, ex.getLocalizedMessage());
                                /**更新数据库*/
                                ImageVO updataImageVO = new ImageVO();
                                imageVO.setIsFirstUpload("1");//手机是否首次上传 0首次 1补传
                                updataImageVO.setIsFirstUpload("1");//手机是否首次上传 0首次 1补传
                                updataImageVO.setUpcliamflag(3);// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败
//                                updataImageVO.setMsg("请求超时");//更新错误信息
                                updataImageVO.setMsg(ex.getMessage());//更新错误信息
                                LogUtils.w("上传失败 更新数据库=" + imageVO.getImagename());
                                updataImageVO.updateAll("imagename = ? ", imageVO.getImagename());
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {
                                LogUtils.i("图片上传请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                                ToastUtils.showShort(MyIntentService.this, cex.getLocalizedMessage());
                            }

                            @Override
                            public void onFinished() {
                                Intent intent2 = new Intent();
                                intent2.setAction("action.updateUI");
                                sendBroadcast(intent2);
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 每次发送后停歇3秒
             */
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        if (null != imageList && imageList.size() > 0) {//当有需要上传的图片时，更新图片列表
            Intent intent2 = new Intent();
            intent2.setAction("action.updateUI");
            sendBroadcast(intent2);
        }

    }

    /**
     * 通过闹铃服务来设定定位间隔
     */
    private synchronized void notifyService() {
        Calendar calendar;// 获取时间类
        calendar = Calendar.getInstance(); // 赋值
        calendar.setTimeInMillis(System.currentTimeMillis()); // 设置系统时间
        int mHour = calendar.get(Calendar.HOUR_OF_DAY); // 获取时
        int mMinute = calendar.get(Calendar.MINUTE); // 分
        calendar.setTimeInMillis(System.currentTimeMillis()); // 毫秒数
        calendar.set(Calendar.HOUR_OF_DAY, mHour); // 时
        calendar.set(Calendar.MINUTE, mMinute); // 分
        calendar.set(Calendar.SECOND, 0); // 秒
        calendar.set(Calendar.MILLISECOND, 0); // 毫秒
        // 建立Intent和PendingIntent，来调用目标组件
        Intent intent = new Intent(this, MyIntentService.class);
        pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        // pendingIntent = PendingIntent.getBroadcast(this, 0,new Intent() , 0);
        if (am != null) // 提醒服务类不为空，先取消
        {
            am.cancel(pendingIntent);
        }

        // 获取闹钟管理的实例
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 设置闹钟
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
        // 设置周期闹
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (pertime * 1000), (pertime * 1000), pendingIntent); // 提醒服务生效
    }

    // 将图片转为Base64编码(黄河移动销售)
    public static String ConvertBase64HH(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        String text = com.estar.hh.survey.utils.base64.Base64.encodeBase64String(buffer);
        System.gc();
        return text;
    }

    // 将图片转为Base64编码
    public String ConvertBase64(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
//        CRLF 这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
//        DEFAULT 这个参数是默认，使用默认的方法来加密
//        NO_PADDING 这个参数是略去加密字符串最后的”=”
//        NO_WRAP 这个参数意思是略去所有的换行符（设置后CRLF就没用了）
//        URL_SAFE 这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/
        String text = Base64.encodeToString(buffer, Base64.DEFAULT);
//        System.gc();
        return text;
    }

    //获取图片hash值
    public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);// 加0x100是因为有的b[i]的十六进制只有1位
        }
        return result;

    }

    public static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5"); // 如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
        int numRead;
        do {
            numRead = fis.read(buffer); // 从文件读到buffer，最多装满buffer
            if (numRead > 0) {
                complete.update(buffer, 0, numRead); // 用读到的字节进行MD5的计算，第二个参数是偏移量
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
