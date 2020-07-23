package com.estar.hh.survey.view.activity;

import android.app.Activity;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.entity.vo.ImageVO;
import com.estarimage.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.estar.hh.survey.R;
import com.estar.hh.survey.service.LocationService;
import com.estar.hh.survey.utils.BaseSpinner;
import com.estar.hh.survey.utils.FileUtils;
import com.estar.hh.survey.utils.ImageCanvas;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.utils.VideoCallBack;
import com.estar.hh.survey.utils.VideoModual;
import com.estar.utils.DateUtil;
import com.estar.utils.ImageCircleUtil;
import com.estar.utils.ShowImagePopWindow;
import com.estar.utils.ToastUtils;
import com.rey.material.widget.Spinner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 相机
 */
public class CameraActivity extends Activity implements OnGestureListener {
    /**
     * 图片数据
     */
    protected List<String> imagePathList = new ArrayList<String>();
    /**
     * 控制聚焦动画的开关
     */
    private boolean bt = true;
    /**
     * 获取点击事件
     **/
    private FrameLayout FrameLayout01;
    /**
     * 自定义聚焦
     **/
    private com.estarview.MoveImageView MoveImageView;
    /**
     * 前后摄像头切换
     **/
    private ImageView change_iv;
    /**
     * 闪光灯切换
     **/
    private ImageView im_deng;
    /**
     * 拍照
     **/
    private ImageView im_camera;
    /**
     * 相册
     **/
    private ImageView im_photo;
    /**
     * 焦距远
     */
    private ImageView im_add;
    /**
     * 焦距近
     */
    private ImageView im_reduce;

    /**
     * 前后摄像头切换
     **/
    private int CHANGE_CAMERA = 0;
    /**
     * 相机预览
     **/
    private SurfaceView surfaceView;
    /**
     * 大类
     */
    private Spinner sp_bigClass;
    /**
     * 小类
     */
    private Spinner sp_smallClass;
    /**
     * 相机模块
     **/
    protected VideoModual mVideoModual = null;

    protected String lossitemserialno = "";//损失序列号
    protected String reportNo = "";
    protected String taskno = "";//任务Id
    private String type = ""; //任务类型 121现在查勘 ，122：人伤查勘，131：车辆定损，132：财产定损
    private String picCategory = ""; //1 其他 2单证
    /** 闪光灯按钮
     * **/
    /**
     * 闪光灯控制
     * FLASH_MODE_RED_EYE防红眼模式，减小或阻止图片上的人物像的红眼出现。
     * FLASH_MODE_TORCH填充模式，在正常光照下会减弱闪光强度。
     * 0为  	FLASH_MODE_AUTO自动模式，有需要的时候会自动闪光。
     * 1为		FLASH_MODE_OFF 闪光模式将不会被关闭
     * 2为	    FLASH_MODE_ON  快照时闪光模式将永远被关闭
     */
    protected int openDeng = 0;
    /**
     * 相机拍照
     **/
    protected boolean carmBoolean = false;


    protected Animation myAnimationScale, myAnimationTranslate1;


    /**
     * 照片当前地址
     **/
    protected String address = "网络问题,没有进行百度定位";
    protected boolean isAddressRun = true;


    //	百度定位
    private LocationService locationService;
    private String img_path;
    private String nowTime;//网络时间
    private boolean isExit = false;
    private boolean isLocationServiceStop = false;

    private BaseSpinner bs;
    private FileUtils fileUtils;


    private UserSharePrefrence userSharePrefrence = null;


    /**
     * 登录返回的数据
     */
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
            } else if (msg.what == 2) {
                isExit = false;
            } else if (msg.what == 3) {
                fini();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        fileUtils = FileUtils.getInstance(this);
        /**获取点击事件**/
        FrameLayout01 = findViewById(R.id.FrameLayout01);
        /**自定义聚焦**/
        MoveImageView = findViewById(R.id.MoveImageView);
        /**前后摄像头切换**/
        change_iv = findViewById(R.id.change_iv);
        /**闪光灯切换**/
        im_deng = findViewById(R.id.im_deng);
        /**拍照**/
        im_camera = findViewById(R.id.im_camera);
        /**相册**/
        im_photo = findViewById(R.id.im_photo);
        /**焦距远*/
        im_add = findViewById(R.id.im_add);
        /**焦距近*/
        im_reduce = findViewById(R.id.im_reduce);
        /**相机预览**/
        surfaceView = findViewById(R.id.surfaceView);
        /**大类*/
        sp_bigClass = findViewById(R.id.sp_bigClass);
        /**小类*/
        sp_smallClass = findViewById(R.id.sp_smallClass);

        initData();
        initWidget();
        widgetClick();
    }


    /**
     * 初始化数据
     */

    protected void initData() {

        userSharePrefrence = new UserSharePrefrence(this);


// -----------location config ------------
        locationService = new LocationService(getApplicationContext());//百度定位初始化定位sdk
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 开始定位

        bs = new BaseSpinner(this);
        Bundle b = getIntent().getExtras();
        reportNo=b.getString("reportNo");
        taskno=b.getString("taskno");
//        type=b.getString("type");//任务类型 121现在查勘 ，122：人伤查勘，131：车辆定损，132：财产定损
        lossitemserialno = "lossitemserialno";
        type = "1";//任务类型 121现在查勘 ，122：人伤查勘，131：车辆定损，132：财产定损

        myAnimationScale = AnimationUtils.loadAnimation(this, R.anim.autocamera_start);
        myAnimationTranslate1 = AnimationUtils.loadAnimation(this, R.anim.autocamera_end);
        myAnimationScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (bt) {
                    MoveImageView.setBackgroundResource(R.drawable.autofocus);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animation.hashCode() == myAnimationScale.hashCode()) {
                    //
                    MoveImageView.startAnimation(myAnimationTranslate1);
                }
            }
        });

        myAnimationTranslate1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (bt) {
                    MoveImageView.setBackgroundResource(R.drawable.autofocus_ok);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (bt) {
                    MoveImageView.setBackgroundResource(R.drawable.autofocus);
                    MoveImageView.setBackgroundResource(R.drawable.autofocus_1);
                    carmBoolean = false;//聚焦动画播放完毕，可以拍照
                }
            }
        });
//        /**小类*/
//        bs.initSprinnerByKey(sp_bigClass, R.array.documentsA_Value, R.array.documentsA_Code, null);//默认小类
//        bs.initSprinnerByKey(sp_smallClass, R.array.documentsB_Value6, R.array.documentsB_Code6, null);//默认小类
//        sp_bigClass.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(Spinner parent, View view, int position, long id) {
//
//                if(bs.getSpinnerKey(sp_bigClass).equals("11000")){
//                    bs.initSprinnerByKey(sp_smallClass, R.array.documentsB_Value1, R.array.documentsB_Code1, null);//默认小类
//                }else if(bs.getSpinnerKey(sp_bigClass).equals("51000")){
//                    bs.initSprinnerByKey(sp_smallClass, R.array.documentsB_Value3, R.array.documentsB_Code3, null);//默认小类
//                }else if(bs.getSpinnerKey(sp_bigClass).equals("101000")){
//                    bs.initSprinnerByKey(sp_smallClass, R.array.documentsB_Value4, R.array.documentsB_Code5, null);//默认小类
//                }else if(bs.getSpinnerKey(sp_bigClass).equals("41000")){
//                    bs.initSprinnerByKey(sp_smallClass, R.array.documentsB_Value5, R.array.documentsB_Code5, null);//默认小类
//                }else if(bs.getSpinnerKey(sp_bigClass).equals("61000")){
//                    bs.initSprinnerByKey(sp_smallClass, R.array.documentsB_Value6, R.array.documentsB_Code6, null);//默认小类
//                }
//
//
//            }
//        });
        /**小类（选类重写）*/
        bs.initSprinnerByKey(sp_bigClass, R.array.documents_Dls_Value, R.array.documents_Dls_Code, null);//默认大类
        bs.initSprinnerByKey(sp_smallClass, R.array.documents_cls1_Value, R.array.documents_cls1_Code, null);//默认小类
        sp_bigClass.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {

                if(bs.getSpinnerKey(sp_bigClass).equals("2")){
                    bs.initSprinnerByKey(sp_smallClass, R.array.documents_cls1_Value, R.array.documents_cls1_Code, null);//默认小类
                }else if(bs.getSpinnerKey(sp_bigClass).equals("3")){
                    bs.initSprinnerByKey(sp_smallClass, R.array.documents_cls2_Value, R.array.documents_cls2_Code, null);//默认小类
                }

            }
        });



    }

    protected void initWidget() {

        FrameLayout01.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                bt = true;
                MoveImageView.autoMouse(event);
                //开始动画
                MoveImageView.startAnimation(myAnimationScale);

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_UP:
                        if (carmBoolean) {//防止拍照的时候点击聚焦
                            return true;
                        } else {
                            carmBoolean = true;
                        }
                        if (mVideoModual != null)//自动对焦
                            mVideoModual.aotoFocus();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 监听点击事件
     */
    public void widgetClick() {
        /**自定义聚焦**/
        MoveImageView = findViewById(R.id.MoveImageView);
        /**前后摄像头切换**/
        change_iv = findViewById(R.id.change_iv);
        /**闪光灯切换**/
        im_deng = findViewById(R.id.im_deng);
        /**拍照**/
        im_camera = findViewById(R.id.im_camera);
        /**相册**/
        im_photo = findViewById(R.id.im_photo);
        /**焦距远*/
        im_add = findViewById(R.id.im_add);
        /**焦距近*/
        im_reduce = findViewById(R.id.im_reduce);
        /**相机预览**/
        surfaceView = findViewById(R.id.surfaceView);
        /**大类*/
        sp_bigClass = findViewById(R.id.sp_bigClass);
        /**小类*/
        sp_smallClass = findViewById(R.id.sp_smallClass);

        change_iv.setOnClickListener(new View.OnClickListener() {//切换摄像头
            @Override
            public void onClick(View v) {
                if (CHANGE_CAMERA == 0) {//0 为后置摄像头  1 为前置摄像头
                    ToastUtils.showShort(CameraActivity.this, "切换成前置摄像头");
                    CHANGE_CAMERA = 1;
                } else if (CHANGE_CAMERA == 1) {
                    ToastUtils.showShort(CameraActivity.this, "切换成后置摄像头");
                    CHANGE_CAMERA = 0;
                }
                mVideoModual.setChangeCamera(CHANGE_CAMERA);//切换摄像头
            }
        });
        im_deng.setOnClickListener(new View.OnClickListener() {//闪光灯切换
            @Override
            public void onClick(View v) {
                try {
//					0为  	FLASH_MODE_AUTO 自动模式，有需要的时候会自动闪光。
//					1为		FLASH_MODE_OFF 闪光模式将不会被关闭
//					2为	    FLASH_MODE_ON  快照时闪光模式将永远被关闭
                    if (openDeng != 1) {
                        openDeng = 1;
                        // 关闭
                        mVideoModual.openLamp(Parameters.FLASH_MODE_OFF);
                        im_deng.setImageResource(R.drawable.deng_off);
                    } else if (openDeng == 1) {
                        openDeng = 2;
                        // 打开
                        mVideoModual.openLamp(Parameters.FLASH_MODE_ON);
                        im_deng.setImageResource(R.drawable.deng_on);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        im_camera.setOnClickListener(new View.OnClickListener() {//拍照
            @Override
            public void onClick(View v) {
                if (!carmBoolean) {
                    carmBoolean = true;
                    crame();
                }
            }
        });
        im_photo.setOnClickListener(new View.OnClickListener() {//相册
            @Override
            public void onClick(View v) {
                if (imagePathList.size() > 0) {
                    ShowImagePopWindow show = new ShowImagePopWindow(CameraActivity.this, imagePathList, imagePathList.size() - 1);
                    //设置PopupWindow全屏
                    show.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                    show.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                    show.showAtLocation(findViewById(R.id.surfaceView), Gravity.CENTER, 0, 0);
                }
            }
        });
        im_add.setOnClickListener(new View.OnClickListener() {//焦距远
            @Override
            public void onClick(View v) {
                if (null != mVideoModual)
                    mVideoModual.zoom(1);
            }
        });
        im_reduce.setOnClickListener(new View.OnClickListener() {//焦距近
            @Override
            public void onClick(View v) {
                if (null != mVideoModual)
                    mVideoModual.zoom(2);
            }
        });

    }

    public void onPictureData2(byte[] data, int dataType) {
        FileOutputStream out = null;
        File file = null;
            /* 保存图片到sd卡中 */
        try {
            file = new File(fileUtils.getImagePath());
            // 保存图片
            out = new FileOutputStream(file);
            out.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        // 读取刚刚拍的照片,调用c代码压缩图片
        if (null != file && file.exists()) {
            String jpg = taskno + "_" + DateUtil.getLongData() + ".jpg";// 图片名称
            img_path = fileUtils.getImageMd(reportNo) + "/" + jpg;
            try {
                // 读取刚刚拍的照片,调用c代码压缩图片
                EstarImage estar = new EstarImage();
                //开始压缩
                int resultCode = 0;
                resultCode=estar.CompressPictureByResolution2(fileUtils.getImagePath(), img_path, 1920, 1080, 90, 0);
                if (!(resultCode == 2 || (resultCode >= 100 && resultCode <= 100 + 100))) {
                    resultCode = estar.CompressPictureByResolution2(fileUtils.getImagePath(), img_path, 1024, 768, 90, 0);
                }
                if (!(resultCode == 2 || (resultCode >= 100 && resultCode <= 100 + 100))) {
                    ToastUtils.showShort(this, "压缩照片失败,请重新拍摄！");
                    return;
                }
                if (null == nowTime || "".equals(nowTime) ||
                        nowTime.contains("1970")) {//判断网络时间不为空且不包含1970
                    nowTime = "手机时间:" + DateUtil.getYMDHMS();
                }
                // 添加水印
                ImageCanvas canvas = new ImageCanvas(CameraActivity.this);
                boolean addCanvas = canvas.watermarkBitmap(img_path, address, 18, "", "", nowTime);
                if (!addCanvas) {
                    ToastUtils.showShort(this, "添加水印失败,请重新拍摄！");
                    carmBoolean = false;
                    return;
                }
                ImageVO imageVO = new ImageVO();
//                imageVO.setImgdate(DateUtil.getYMD());//
//                imageVO.setTaskid(taskno);// 任务id
//                imageVO.setImagename(jpg);// 图片名称
//                imageVO.setPath(img_path);//路径
//                imageVO.setUsercode("");// 用户代码
//                imageVO.setPicCls(bs.getSpinnerKey(sp_bigClass));// 大类
//                imageVO.setPicDtl(bs.getSpinnerKey(sp_smallClass));// 小类
//                imageVO.setReportno(reportNo);// 报案号
//                imageVO.setImageTaskType(type);//任务类型
//                imageVO.setPicClsName(bs.getSpinnerValue(sp_bigClass));//图片大类名称
//                imageVO.setPicDtlName(bs.getSpinnerValue(sp_smallClass));//图片小类名称
//                imageVO.setPicCategory(picCategory);// 1其他 2单证收集
//                imageVO.setTaskType("");//任务类型

                imageVO.setTaskno(taskno);
                imageVO.setTaskid(taskno);
                imageVO.setReportno(reportNo);
                imageVO.setImagename(jpg);// 图片名称
                imageVO.setImagePath(img_path);
                imageVO.setPicCls(bs.getSpinnerKey(sp_bigClass));// 大类
                imageVO.setPicDtl(bs.getSpinnerKey(sp_smallClass));// 小类
                imageVO.setUsercode(userSharePrefrence.getUserEntity().getEmpCde());

                /** LitePal数据库  插入数据库*/
                boolean ifSaveSuccess = imageVO.saveFast();//插入数据库
                if (ifSaveSuccess) {
                    im_photo.setImageBitmap(ImageCircleUtil.toRoundBitmap(this, img_path));
                    imagePathList.add(img_path);
                } else {
                    ToastUtils.showShort(this, "保存数据库失败，请重新拍摄！");
                }

                return;
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort(this, "压缩失败");
                return;
            } finally {
                if (carmBoolean) {
                    carmBoolean = false;
                }
            }
        } else {
            ToastUtils.showShort(this, "拍摄照片失败，请重新拍摄！");
        }

        if (carmBoolean) {
            carmBoolean = false;
        }

    }


    /**
     * // 相机出现异常之后 重新开始
     *
     * @param msg     - 消息类型(OUT)
     * @param msgText - 消息文字(OUT)
     */
    public void onMsg2(int msg, String msgText) {
        carmBoolean = false;//允许拍照
        ToastUtils.showShort(this, "相机出错正在重试");

    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mVideoModual != null){
                mVideoModual.stopPreview();
                mVideoModual.startPreview(null, false);
                mVideoModual.setCallBack(null);
                mVideoModual = null;
            }
            handler.removeCallbacks(runnable);//移除线程防止 handler内存泄漏
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
            isLocationServiceStop = true;
            LogUtils.w("定位成功关闭定位");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        isAddressRun = false;
        System.gc();
    }


    /**
     * 拦截硬按键返回事件
     **/
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        exit();
        return true;

    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtils.showShort(this, "再按一次退出相机");
            Message mesg = new Message();
            mesg.what = 2;
            handler.sendMessageDelayed(mesg, 2000);//延迟两秒
        } else {
            Message mesg = new Message();
            mesg.what = 3;
            handler.sendMessage(mesg);
        }
    }

    private void fini() {
        if (mVideoModual != null){
            mVideoModual.stopPreview();
            mVideoModual.startPreview(null, false);
            mVideoModual.setCallBack(null);
            mVideoModual = null;
        }
        handler.removeCallbacks(runnable);//移除线程防止 handler内存泄漏
//		System.gc();
        finish();

    }


    protected void onResume() {

        super.onResume();
        start();
        if (isLocationServiceStop) {
            locationService.start();// 开始定位
            isLocationServiceStop = false;
            LogUtils.w("开启定位");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mVideoModual != null){
                mVideoModual.stopPreview();
                mVideoModual.startPreview(null, false);
                mVideoModual.setCallBack(null);
                mVideoModual = null;
            }
            locationService.stop(); //停止定位服务
            isLocationServiceStop = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启相机
     */
    private void start() {
        mVideoModual = VideoModual.getInstance(this);
        if (mVideoModual.hasFrontFacingCamera()) {//判断是否有前置摄像头
            change_iv.setVisibility(View.VISIBLE);
        }
        mVideoModual.setCallBack(new VideoCallBack() {//设置回调
            @Override
            public void onPictureData(byte[] data, int dataType) {
                onPictureData2(data,dataType);
            }

            @Override
            public void onMsg(int msg, String msgText) {
                onMsg2(msg, msgText);
            }
        });
        mVideoModual.startPreview(surfaceView, false);
    }


    private void crame() {
        bt = false;
        if (mVideoModual != null) {

            if (openDeng == 0) {
                //自动
                mVideoModual.openLamp(Parameters.FLASH_MODE_AUTO);
            } else if (openDeng == 1) {
                // 关闭
                mVideoModual.openLamp(Parameters.FLASH_MODE_OFF);
            } else if (openDeng == 2) {
                // 打开
                mVideoModual.openLamp(Parameters.FLASH_MODE_ON);
            }
            //拍照
            mVideoModual.takenPicture(true);
        } else {
            ToastUtils.showShort(this, "mVideoModual = null");
        }

    }

    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float agr,
                           float arg3) {


        if (e1.getY() - e2.getY() > 120) {

            //android.util.Log.e("向上", "111111111");
            return true;
        } else if (e1.getY() - e2.getY() < -120) {
            //android.util.Log.e("向下", "111111111");
            return true;
        }
        return true;

    }

    public void onLongPress(MotionEvent arg0) {


    }

    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
                            float arg3) {

        return false;
    }

    public void onShowPress(MotionEvent arg0) {


    }

    public boolean onSingleTapUp(MotionEvent arg0) {

        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (mVideoModual != null){
                mVideoModual.stopPreview();
                mVideoModual.startPreview(null, false);
                mVideoModual.setCallBack(null);
                mVideoModual = null;
            }
            handler.removeCallbacks(runnable);//移除线程防止 handler内存泄漏
            locationService.stop(); //停止定位服务
            isLocationServiceStop = true;
            LogUtils.w("定位成功关闭定位");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (location.getLocType() == BDLocation.TypeServerError) {
                    address = "服务端网络定位失败";//详细地址
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    address = "网络不同导致定位失败,请检查网络是否通畅";//详细地址
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    address = "无法获取有效定位,一般是由于手机的原因";//详细地址
                } else {
                    address = location.getAddrStr();//详细地址
                    nowTime = location.getTime();
                    LogUtils.w("address=" + address + "\nnowTime" + nowTime + "\n手机时间:" + DateUtil.getYMDHMS());
                }
                try {
                    locationService.stop(); //停止定位服务
                    handler.postDelayed(runnable, 20000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                address = "定位失败";//详细地址
                nowTime = "手机时间:" + DateUtil.getYMDHMS();
                LogUtils.w("定位失败");
            }

        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isLocationServiceStop) {
                locationService.start();// 开始定位
            }
        }
    };


}
