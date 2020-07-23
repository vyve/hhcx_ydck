package com.estar.hh.survey.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.estar.hh.survey.common.ConfigSharePrefrence;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.LogUploadRequest;
import com.estar.hh.survey.entity.request.LoginRequest;
import com.estar.hh.survey.entity.response.LogUploadResponse;
import com.estar.hh.survey.entity.response.LoginResponse;
import com.estar.hh.survey.utils.FileUtils;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.HttpUtils;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.utils.base64.Base64;
import com.estar.hh.survey.view.activity.HomeActivity;
import com.estar.hh.survey.view.activity.LoginActivity;
import com.estar.utils.DateUtil;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.estar.hh.survey.common.ConfigSharePrefrence.LOGUPLOADSWITCH;

/**
 * Created by Administrator on 2017/10/16 0016.
 * 系统崩溃日志
 */

@SuppressLint("SimpleDateFormat")
public class CrashHandler implements Thread.UncaughtExceptionHandler {


    public static String TAG = "MyCrash";
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private static CrashHandler instance = new CrashHandler();
    private Context mContext;

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        autoClear(5);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            SystemClock.sleep(3000);
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null)
            return false;

        try {
            // 使用Toast来显示异常信息
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "很抱歉,程序出现异常,即将重启.",
                            Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
            // 收集设备参数信息
            collectDeviceInfo(mContext);
            // 保存日志文件
            String fileName = saveCrashInfoFile(ex);

            /**
             * 根据用户设置选择日志是否上传
             */
            ConfigSharePrefrence configSharePrefrence = new ConfigSharePrefrence(mContext);
            boolean uploadLog = configSharePrefrence.getBoolean(LOGUPLOADSWITCH);
            if (uploadLog) {
                // 上传日志到服务器
                uploadLog(fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e( "an error occured when collect package info", e.toString());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                LogUtils.e( "an error occured when collect crash info", e.toString());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     * @throws Exception
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            String date = DateUtil.getTime_YMDMS(new Date());
            sb.append("\r\n" + date + "\n");
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result);

            String fileName = writeFile(sb.toString());
            return fileName;
        } catch (Exception e) {
            LogUtils.e( "an error occured while writing file...", e.toString());
            sb.append("an error occured while writing file...\r\n");
            writeFile(sb.toString());
        }
        return null;
    }

    private String writeFile(String sb) throws Exception {
        String time = DateUtil.getTime_YMDMS(new Date());
        String fileName = "crash-" + time + ".log";
        if (FileUtils.getInstance().hasSDCard()) {
            String path = getGlobalpath();
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            FileOutputStream fos = new FileOutputStream(path + fileName, true);
            fos.write(sb.getBytes());
            fos.flush();
            fos.close();
        }
        return fileName;
    }

    public static String getGlobalpath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +FileUtils.SYS_TEMMD + File.separator + "crash" + File.separator;
    }


    /**
     * 日志上传方法
     */
    private void uploadLog(String fileName){

        // 获取日志文件
        String fileAbsolutePath = getGlobalpath() + fileName;
        String fileString = null;
        File file = new File(fileAbsolutePath);
        if (!file.exists()){
            return;
        }

        fileString = ConvertBase64HH(fileAbsolutePath);
        if (StringUtils.isEmpty(fileString)){
            return;
        }else {
            fileString = fileString.replace("+", "%2B");
        }

        UserSharePrefrence userSharePrefrence = new UserSharePrefrence(mContext);
        User user = userSharePrefrence.getUserEntity();
        if (user == null){
            return;
        }

        LogUploadRequest request = new LogUploadRequest();
        request.getData().setSurveyCode(user.getEmpCde());
        request.getData().setLogData(fileString);

        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.LOGUPLOAD, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.LOGUPLOAD, reqMsg);

        LogUtils.i("日志上传请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("日志上传返回参数为:", "--------------------------------------------\n" + result);
                LogUploadResponse response = gson.fromJson(result, LogUploadResponse.class);
                if (response.getSuccess()){
                    // 退出程序
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                    System.exit(1);
                }else{

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("日志上传请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                ToastUtils.showShort(mContext, ex.getLocalizedMessage());
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("日志上传请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                ToastUtils.showShort(mContext, cex.getLocalizedMessage());
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    // 将图片转为Base64编码(黄河移动销售)
    public String ConvertBase64HH(String filePath) {

        String text = null;
        try{
            FileInputStream fis = new FileInputStream(filePath);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            text = com.estar.hh.survey.utils.base64.Base64.encodeBase64String(buffer);
            System.gc();

        }catch (Exception e){
            e.printStackTrace();
        }

        return text;

    }

    /**
     * 获取文件二进制流
     * @param file
     * @return
     */
    private byte[] readFile(File file) {
        // 需要读取的文件，参数是文件的路径名加文件名
        if (file.isFile()) {
            // 以字节流方法读取文件
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                // 设置一个，每次 装载信息的容器
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                // 开始读取数据
                int len = 0;// 每次读取到的数据的长度
                while ((len = fis.read(buffer)) != -1) {// len值为-1时，表示没有数据了
                    // append方法往sb对象里面添加数据
                    outputStream.write(buffer, 0, len);
                }
                // 输出字符串
                return outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
        return null;
    }


    public static void setTag(String tag) {
        TAG = tag;
    }

    /**
     * 文件定期清理
     * 文件删除
     * @param autoClearDay 文件保存天数
     */
    public void autoClear(final int autoClearDay) {
//        FileUtil.delete(getGlobalpath(), new FilenameFilter() {
//
//            @Override
//            public boolean accept(File file, String filename) {
//                String s = FileUtil.getFileNameWithoutExtension(filename);
//                int day = autoClearDay < 0 ? autoClearDay : -1 * autoClearDay;
//                String date = "crash-" + DateUtil.getOtherDay(day);
//                return date.compareTo(s) >= 0;
//            }
//        });
    }

}
