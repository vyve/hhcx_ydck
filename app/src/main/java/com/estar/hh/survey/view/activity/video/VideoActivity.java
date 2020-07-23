package com.estar.hh.survey.view.activity.video;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.video.MainPage2GridViewAdapter;
import com.estar.hh.survey.adapter.video.MsgListAdapter;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.ImageUploadRequest;
import com.estar.hh.survey.entity.request.VideoCreateRequest;
import com.estar.hh.survey.entity.response.ImageUploadResponse;
import com.estar.hh.survey.entity.response.LoginResponse;
import com.estar.hh.survey.entity.response.VideoCreateResponse;
import com.estar.hh.survey.entity.vo.ImageVO;
import com.estar.hh.survey.entity.vo.MsgVO;
import com.estar.hh.survey.entity.vo.VideoResult;
import com.estar.hh.survey.entity.vo.video.ReponseResVideoLineVO;
import com.estar.hh.survey.entity.vo.video.RequestReqVideoLineVO;
import com.estar.hh.survey.entity.vo.video.ResVideoLineVO;
import com.estar.hh.survey.entity.vo.video.VideoUserInfoVO;
import com.estar.hh.survey.service.MyIntentService;
import com.estar.hh.survey.utils.DialogUtil;
import com.estar.hh.survey.utils.FileUtils;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.HttpUtils;
import com.estar.hh.survey.utils.ImageCanvas;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.utils.MessageDialog;
import com.estar.hh.survey.utils.NetSpeed;
import com.estar.hh.survey.utils.PictureModule;
import com.estar.hh.survey.view.activity.HomeActivity;
import com.estar.hh.survey.view.activity.HuangheBaseActivity;
import com.estar.hh.survey.view.activity.LoginActivity;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.hh.survey.view.component.video.FloatingActionsMenu;
import com.estar.hh.survey.view.component.video.MyTrafficStatistics;
import com.estar.hh.survey.view.component.video.SegmentedRadioGroup;
import com.estar.hh.survey.view.component.video.ShowImagePopWindow;
import com.estar.utils.DateUtil;
import com.estar.utils.SDCardUtils;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.estarimage.EstarImage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import info.emm.meeting.MeetingSession;
import info.emm.meeting.MeetingUser;
import info.emm.utils.NotificationCenter;

import static com.estar.hh.survey.service.MyIntentService.ConvertBase64HH;
import static com.estar.hh.survey.service.MyIntentService.getMD5Checksum;

/**
 * 视频模块
 * Created by ding on 2015/10/23.
 */
public class VideoActivity extends HuangheBaseActivity implements NotificationCenter.NotificationCenterDelegate,
        OnCheckedChangeListener {
    /**
     * 数据库图片个数  +1
     */
    private int image_id = 0;
    /**
     * 闪光灯按钮
     **/
    protected boolean openDeng = true;
    // Camera实例
    private Camera mCamera = null;
    static public int WEIYI_VIDEO_OUT_SLOW = 1;       //视频发送速度慢
    static public int WEIYI_VIDEO_OUT_DISCONNECT = 2; //视频发送连接断开重连
    static public int WEIYI_VIDEO_IN_SLOW = 3;        //视频接收速度慢
    static public int WEIYI_VIDEO_IN_DISCONNECT = 4;  //视频接收连接断开重连
    static public int WEIYI_AUDIO_DISCONNECT = 10;    //音频连接断开重连
    static public int WEIYI_AUDIO_DISABLED = 11;      //未能取得打开麦克风权限
    private String warningString;//众望连接状态
    protected ViewPager tabpager;
    protected SegmentedRadioGroup btns;
    protected ImageButton userinfo;
    protected ImageButton takePicture;
    protected ListView msgListView;
    /**
     * 消息输入框
     */
    protected EditText msgText;
    /**
     * 发送按钮
     */
    protected Button send_msg;
    protected RadioButton button_three;
    /**
     * 播放预览
     */
    protected SurfaceView surfaceView1;

    protected Button taskinfo;


    /**
     * 上传 显示
     */
    protected TextView upDataTV;

    /**
     * 下载 显示
     */
    protected TextView downDataTV;
    /**
     * 丢包显示
     */
    protected TextView spd;

    /**
     * 设置摄像头 前置和后置
     */
    boolean usefront = true;
    /**
     * 会议用户列表
     */
    protected List<VideoUserInfoVO> userList = new ArrayList<VideoUserInfoVO>();
    /**
     * 消息列表
     */
    protected List<MsgVO> msgList = new ArrayList<MsgVO>();
    /**
     * 消息适配器
     */
    protected MsgListAdapter msgListAdapter = null;
    /**
     * 用户面板
     */
    protected UserPopWindow userPopWindow;
    /**
     * 摄像头前置后置
     */
    boolean hasfront;
    /**
     * 打开视频和关闭视频
     */
    protected boolean isVideo = false;
    /**
     * 打开扬声器 关闭扬声器
     */
    protected boolean isfrontBoolean = false;

    /**
     * 申请发言，取消发言
     */
    protected boolean sparkBoolean = true;
    /**
     * 消息闪烁标志
     */
    protected boolean msgFlashingBoolean = false;


    /**
     * 是否进行混音
     */
    static public boolean _serverMix = false;
    /**
     * 初始化第一，第二第三页面数据
     */
    protected String userName = "", setLossName = "";
    /**
     * 用户类型 1为现场人员 2未非现场人员
     */
    protected int userType = 1;
    /**
     * 会议ID
     */
    protected int myPid = 0;
    private String upData_tv, downData_tv;
    //    private ImageLoader imageLoader;
    public boolean showSpeed = true;
    NetSpeed speed = null;
    FileUtils fileUtils = new FileUtils();
    private long upKb = 0, upPackets = 0, upErrs = 0, upDrop = 0, downKb = 0, downPackets = 0, downErrs = 0, downDrop = 0, OKORNO = 0;
    private int takingPicture = 0;//0 空闲，1 启动中, 2 正在对焦, 3 正在拍照中或正在停止中，默认值是0
    private boolean isCameraOK = true;
    private boolean _bUsingFront = true;
    private int _apiVersion = Build.VERSION.SDK_INT;
    private String nowTime = DateUtil.getSysDate();//网络时间
    private String uploadtype = "3";// 上传类型(1,PC端截屏图片 2,移动查勘图片 3移动视频图片，)

    /**
     * 更新未读消息状态
     */
    final Handler vhandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {

                    if (null != pictureModule) pictureModule.dismissPicture();
                } catch (Exception e) {
                }
                threadIsShow = true;
            } else if (msg.what == 2) {
//                isExit = false;
            } else if (msg.what == 3) {
//                fini();
            } else if (msg.what == 4) {
//                if (showSpeed){
//                upDataTV.setText(upData_tv + "KB");
//                downDataTV.setText(downData_tv + "KB");
//                }
            } else if (msg.what == 5) {

                upKb = msg.getData().getLong("upKb");//上行速度
                upPackets = msg.getData().getLong("upPackets");//上行正确包
                upErrs = msg.getData().getLong("upErrs");//上行错误包
                upDrop = msg.getData().getLong("upDrop");//上行丢弃包
                downKb = msg.getData().getLong("downKb");//下行速度
                downPackets = msg.getData().getLong("downPackets");//下行正确包
                downErrs = msg.getData().getLong("downErrs");//下行错误包
                downDrop = msg.getData().getLong("downDrop");//下行丢弃包
                OKORNO = msg.getData().getLong("OKORNO");////初始为0  1为达标  2为不达标
                if (OKORNO == 2) {
                    showShortToast("网络速度达不到视频要求，将影响音视频效果");
                    sendMessage2("网络速度达不到视频要求，将影响音视频效果");
                    OKORNO = 0;
                }

                upDataTV.setVisibility(View.VISIBLE);
                upDataTV.setText(upKb + "KB");
                downDataTV.setVisibility(View.VISIBLE);
                downDataTV.setText(downKb + "KB");

                String netDataMsg = "上行数据：\n" + "上行速度：" + upKb + "K/s；上行正确包：" + upPackets + "；上行错误包：" + upErrs + "；上行丢弃包：" + upDrop;
                netDataMsg = netDataMsg + "\n\n下行数据：\n" + "下行速度：" + downKb + "K/s；下行正确包：" + downPackets + "；下行错误包：" + downErrs + "；下行丢弃包：" + downDrop;
//                spd.setText(netDataMsg );
                upDataTV.setText("上行速度：" + upKb + "K/s\n    上行丢包率：" + upDrop + "%");
                downDataTV.setText("下行速度：" + downKb + "K/s\n    下行丢包率：" + downDrop + "%");
                fileUtils.writeLog(netDataMsg + "\n\n+众望提示：" + warningString);


            }

        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //要做的事情
            setTime();
            vhandler.postDelayed(this, 2000);
        }
    };


    /**
     * 获取网络时间
     *
     * @param
     */
    public void setTime() {
        new Thread() {
            public void run() {
                URL url;
                try {
                    url = new URL("http://www.baidu.com");//取得资源对象  http://www.bjtime.cn
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    long ld = uc.getDate(); //取得网站日期时间
                    Date date = new Date(ld); //转换为标准时间对象
                    DateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String UserTime = sdformat.format(date);
//						String UserTime = "1970-01-27 13:41:37";
                    if (null != UserTime && !"".equals(UserTime) &&
                            !UserTime.contains("1970")) {//判断网络时间不为空且不包含1970
                        nowTime = "北京时间:" + UserTime;
                    } else {
                        nowTime = "手机时间:" + DateUtil.getSysDate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    nowTime = "手机时间:" + DateUtil.getSysDate();
                }

            }
        }.start();
    }

    /**
     * 是否需要上传图片
     */
    protected boolean isUpdateImage = true;


    /**
     * 图片适配器
     */
    protected MainPage2GridViewAdapter mainPage2GridViewAdapter;
    /**
     * 图片控件
     */
    protected GridView gridView;

    /**
     * 图片数据
     */
    protected List<String> imagePathList = new ArrayList<String>();

    /**
     * 图片临时文件
     */
    protected String path = Environment.getExternalStorageDirectory()
            .toString() + "/temp.jpg";

    /**
     * 会议室ID
     */
    protected String meetingId = "";

    /**
     * 任务号
     */
    protected String taskNo = "";

    /**
     * 每个多少秒检测未上传照片
     */
    protected int checkImageTime = 5000;

    /**
     * 登录dialog
     */
    Dialog dialog = null;

//    protected FloatingActionsMenu floatingActionsMenu;
    /**
     * 菜单
     */
    protected MenuPopWindow menuPopWindow;

    protected TaskInfoPopWindow taskInfoPopWindow;


    /**
     * 是否获取地址
     */
    protected Boolean isAddressRun = true;
    /**
     * 图片展示类
     */
    protected PictureModule pictureModule;
    /**
     * 流量统计
     */
    protected MyTrafficStatistics myTrafficStatistics;


    /**
     * 最大发言数
     */
    protected int maxSpeakerCount = 9;
    /**
     * 重力感应
     */
    protected SensorManager sm = null;
    protected float y = 0;
    protected Sensor sensor = null;
    /**
     * 拍照 true、聚焦 false
     */
    protected boolean carmBoolean = true;

    protected boolean threadIsShow = true;
    private String reportNo;
    //	百度
    private LocationClient mLocationClient;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private TextView latitude, longitude, location_tv;
    private String tempcoor = "gcj02";
    private String address = "";
    private int _watchingPeerID = 0;
    private String imagePath = "";//文件夹
    private boolean sharpness = true;
    private ImageView cameraDeng_iv;

    private VideoResult videoResult = new VideoResult();
    private Mission mission = null;
    private UserSharePrefrence userSharePrefrence = null;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        findViewById();

        initData();

    }


    public void setContentView() {
        //屏幕禁止休眠和锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.video);
    }


    public void findViewById() {

        taskinfo = findViewById(R.id.taskinfo);


        taskinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskinfo();
            }
        });


    }

    /**
     * 测试网速，大于10K进界面，大于30K可以视频，10-30K语音、拍照
     * <p/>
     * <p/>
     * 初始化数据
     */
    public void initData() {

        mission = (Mission) getIntent().getSerializableExtra("mission");
        videoResult = (VideoResult) getIntent().getSerializableExtra("videoResult");
        userSharePrefrence = new UserSharePrefrence(this);
        user = userSharePrefrence.getUserEntity();

        mLocationClient = new LocationClient(this);
        latitude = new TextView(this);//纬度
        longitude = new TextView(this);//经度
        longitude = new TextView(this);//经度
        location_tv = new TextView(this);
        InitLocation();//定位
        //启动计时器
        vhandler.postDelayed(runnable, 2000);//每5秒执行一次runnable.
        setTime();

        try {

//            String ss = sharedPreferencesUtil.getString("sharpness", "true");
//            if (!"true".equals(ss)) {
//                sharpness = false;
//            }
            sharpness = true;
            LogUtil.w("sharpness=" + sharpness);

            //工号_姓名_分公司名称
            userName = user.getEmpCde() + "_" + user.getRealName() + "_" + user.getOrgCode();
            setLossName = userName;
            reportNo = mission.getRptNo();
            taskNo = mission.getTaskId();
            imagePath = fileUtils.getImageMd(reportNo);
            /**
             *
             * @param mContext
             * @param mHandler
             * @param NUMBER  ONE_TIME时间段内 达标次数
             * @param UPOK    达标网速(KB)
             * @param ONE_TIME  计算时间段（秒）
             * @param SEND_TIME  每次发送不符合网速信息的最短时间间隔（秒）
             */
            speed = new NetSpeed(this, vhandler, 7, 25, 10, 60);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(this, "数据缺失！initData");
            fileUtils.writeException(e);
            finish();
        }
        btns = findViewById(R.id.btns);
        btns.setOnCheckedChangeListener(this);
        initImage();
        initViews();

        // 如果是观看人员则隐藏播放视频按钮
        if (userType != 1) {
            takePicture.setVisibility(View.GONE);
        }

        userPopWindow = new UserPopWindow(this, userList, userType, setLossName);


        pictureModule = PictureModule.getInstance();
        pictureModule.initialize(this, "");
        sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sm != null && sel != null)
            sm.registerListener(sel, sensor, SensorManager.SENSOR_DELAY_GAME);

        /**
         * 若视频请求集为空则发起请求
         * 若视频请求集不为空则直接连接视频
         */
        if (videoResult == null) {
            resVideoLine("1");//发送视频请求 //视频类型 0-初始化值(即没有申请视频)； 1-视频模式； 2-图片模式； 3-后台拒绝； 4-后台无闲置人员
        } else {
            meetingId = videoResult.getMeetingId();
            initVideo();//连接视频
        }

    }

    // 添加重力感应侦听，并实现其方法，
    SensorEventListener sel = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            // float x=se.values[SensorManager.DATA_X];
            y = se.values[SensorManager.DATA_Y];
            // float z=se.values[SensorManager.DATA_Z];
            //HandlerSensorChanged(se);
            //reSetRotation();
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {

        }
    };


    /**
     * 查询数据库图片
     */
    private void initImage() {
        List<ImageVO> list = null;
        try {
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                /**
                 * LitePal数据库  查询数据库
                 */
                list = DataSupport.where("taskno=? and uploadtype=? ", taskNo, "3").find(ImageVO.class);
                for (ImageVO imageVO : list) {
                    // 文件存在
                    if (new File(imagePath + "/" + imageVO.getImagename()).exists()) {
                        imagePathList.add(imagePath + "/" + imageVO.getImagename());
                    } else {
                        DataSupport.deleteAll(ImageVO.class, "imagename = ? ", imageVO.getImagename());
                    }
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            showShortToast("查询数据库图片 错误");
            fileUtils.writeException(e);
        }
    }

    /**
     * 手机端发起视频连线
     */
    public void resVideoLine(String videoMark) {

        final MyProgressDialog progressDialog = new MyProgressDialog(this, "发起视频连线，请稍候...");

        VideoCreateRequest request = new VideoCreateRequest();
        request.getData().setPmechanism("");
        request.getData().setReportType("");
        request.getData().setReportNo(reportNo);
        request.getData().setTaskType("1");
        request.getData().setTaskNo(taskNo);
        request.getData().setUserCode(user.getEmpCde());
        request.getData().setUserName(user.getRealName());
        request.getData().setDptCode(user.getOrgCode());
        request.getData().setDptName(user.getOrgName());
        request.getData().setVideoMark(videoMark);

        String gson = new Gson().toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.VIDEOCREATE, gson);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.VIDEOCREATE, gson);

        LogUtils.i("视频发起请求参数为:", "-------------------------------------------\n" + gson);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("视频发起返回参数为:", "--------------------------------------------\n" + result);
                VideoCreateResponse response = new Gson().fromJson(result, VideoCreateResponse.class);
                if (response.getSuccess()) {
                    videoResult = response.getObj();
                    if (videoResult != null) {
                        meetingId = videoResult.getMeetingId();
                        initVideo();//连接视频
                    } else {
                        showShortToast("连接失败");
//                        exit();
                        finish();
                    }
                } else {
                    showShortToast(response.getMsg());
//                    exit();
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("视频发起请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("视频发起请求取消为:", "--------------------------------------------\n" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                progressDialog.stopDialog();
            }
        });

    }


    protected void initViews() {
        LogUtil.w("initViews()");
        tabpager = findViewById(R.id.tabpager);
        btns = findViewById(R.id.btns);
        button_three = findViewById(R.id.button_three);
        taskinfo = findViewById(R.id.taskinfo);
//        floatingActionsMenu = ((FloatingActionsMenu) findViewById(R.id.floatingActionsMenu));
        // 设置禁止滑动
        tabpager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        // 将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.video_page1, null);
        View view2 = mLi.inflate(R.layout.video_page2, null);
        View view3 = mLi.inflate(R.layout.video_page3, null);
        userinfo = view1.findViewById(R.id.userinfo);
        userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startCameraActivity();
                userPopWindow.show(userinfo);
            }
        });
        cameraDeng_iv = view1.findViewById(R.id.cameraDeng_iv);//闪光灯
        cameraDeng_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (openDeng) {
                        // 打开
                        openLamp(Camera.Parameters.FLASH_MODE_ON);
                        cameraDeng_iv.setImageResource(R.drawable.deng_on);
                    } else {
                        // 关闭
                        openLamp(Camera.Parameters.FLASH_MODE_OFF);
                        cameraDeng_iv.setImageResource(R.drawable.deng_off);

                    }
                    openDeng = !openDeng;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ImageButton add = view1.findViewById(R.id.add);//近焦距
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom(1);
            }
        });
        ImageButton reduceib = view1.findViewById(R.id.reduce);//远焦距
        reduceib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom(2);
            }
        });
        takePicture = view1.findViewById(R.id.takePicture);//拍照
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.w("点击了拍照");
                uploadtype = "3";// 上传类型(1,PC端截屏图片 2,移动查勘图片 3移动视频图片，)
                carmBoolean = true;
                takePicture();//触发拍照事件
            }
        });
        surfaceView1 = view1.findViewById(R.id.surfaceView1);
        surfaceView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carmBoolean = false;//聚焦
                takePicture();//触发拍照事件
            }
        });


        upDataTV = view1.findViewById(R.id.upData);
        spd = view1.findViewById(R.id.spd);
        downDataTV = view1.findViewById(R.id.downData);
        msgListView = view3.findViewById(R.id.msgListView);
        msgText = view3.findViewById(R.id.msgText);
        send_msg = view3.findViewById(R.id.send_msg);
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgText.getText().toString().trim().length() == 0) {
                    ToastUtils.showShort(VideoActivity.this, "请输入消息内容");
                    return;
                }
                sendMessage2(msgText.getText().toString().trim());
            }
        });
        gridView = view2.findViewById(R.id.pull_refresh_grid);

        //获取快捷消息按钮，并设置监听
        LinearLayout msgLL = view3.findViewById(R.id.msgLL);
        for (int i = 0; i < msgLL.getChildCount(); i++) {
            LinearLayout msgBodyItemLL = (LinearLayout) msgLL.getChildAt(i);
            for (int j = 0; j < msgBodyItemLL.getChildCount(); j++) {
                final TextView textView = (TextView) msgBodyItemLL.getChildAt(j);
                textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        sendMessage2(textView.getText().toString().trim());
                    }
                });
            }
        }

        // 每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        // 填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            public int getCount() {
                return views.size();
            }

            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }

            public void finishUpdate(View paramView) {
            }

            public void restoreState(Parcelable paramParcelable,
                                     ClassLoader paramClassLoader) {
            }

            public Parcelable saveState() {
                return null;
            }

            public void startUpdate(View paramView) {
            }
        };

        // 初始化ViewPager和消息ListView 区域
        tabpager.setAdapter(mPagerAdapter);
        msgListAdapter = new MsgListAdapter(this, msgList);
        msgListView.setAdapter(msgListAdapter);

        // 初始化GridView区域
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        imageLoader = ImageLoader.getInstance(); // Get singleton instance

        mainPage2GridViewAdapter = new MainPage2GridViewAdapter(this,
                imagePathList, dm.widthPixels);
        gridView.setAdapter(mainPage2GridViewAdapter);
        gridViewItemClick();
        // 初始化菜单
        menuPopWindow = new MenuPopWindow(this, userType);
        // 设置菜单监听器 关闭和点击展开
        menuPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
//                floatingActionsMenu.collapse();
            }

        });
//        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
//                    public void onMenuExpanded() {
//
//                        if (!menuPopWindow.isShowing()) {
//                            menuPopWindow.showAtLocation(
//                                    findViewById(R.id.body), Gravity.BOTTOM, 0,
//                                    0);
//                        }
//                    }
//
//                    public void onMenuCollapsed() {
//                        menuPopWindow.dismiss();
//                    }
//
//                });
        // 设置菜单监听器
        menuPopWindow.setOnMenuOnClickListener(new MenuPopWindow.OnMenuOnClickListener() {
            /**
             * 打开和关闭视频
             */
            public void videoOption(TextView tv) {
                if (!isVideo) {
                    // 打开
                    playVideo();
                    startRecording();
                    tv.setText("关闭视频");
                } else {
                    tv.setText("打开视频");
                    MeetingSession.getInstance().setWatchMeWish(false);
                    stopRecording();
                }
                isVideo = !isVideo;
            }

            /**
             * 打开和关闭语音
             */
            public void spark(TextView tv) {
                if (!sparkBoolean) {
                    // 打开
                    MeetingSession.getInstance().StartSpeaking();
                    tv.setText("取消发言");
                } else {
                    MeetingSession.getInstance().StopSpeaking();
                    tv.setText("申请发言");
                }
                sparkBoolean = !sparkBoolean;

            }


            /**
             * 听筒和外音
             */
            public void front(TextView tv) {
                if (!isfrontBoolean) {
                    // 打开
                    tv.setText("听筒");
                } else {
                    tv.setText("外放");
                }
                isfrontBoolean = !isfrontBoolean;
                MeetingSession.getInstance().setLoudSpeaker(isfrontBoolean);

            }

            /**
             * 退出
             */
            public void exit(TextView tv) {
                exitMeeting(1);
            }

            /**
             * 邀请用户
             */
            public void sendUser(TextView tv) {
//				Intent intent = new Intent(BaseVideoActivity.this,AddUserActivity_.class);
//				intent.putExtra("TaskInfoVO",taskInfoVO);
//				intent.putExtra("type",2);
//				//startActivity(intent);
//				startActivityForResult(intent, 100);
            }

            @Override
            public void cancel(TextView tv) {

            }


            /**
             * 完成任务
             */
            public void complete(TextView tv) {

                new MessageDialog(VideoActivity.this, R.style.dialog, new MessageDialog.SubmitOnClick() {

                    @Override
                    public void onSubmitOnClickSure() {
                    }

                    @Override
                    public void onSubmitOnClickCancel() {

                    }
                }, "友情提示", "您确定完成视频定损?", "确定", "取消");
            }

        });
    }

    /**
     * 闪光灯
     *
     * @param value
     */
    public void openLamp(String value) {
        try {
            Camera.Parameters par = mCamera.getParameters();
            par.setFlashMode(value);// 开启闪光灯

            mCamera.setParameters(par);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 调节焦距
     *
     * @param type
     */
    public void zoom(int type) {

        try {
            Camera.Parameters params = mCamera.getParameters();
            final int MAX = params.getMaxZoom();
            LogUtils.e("===", MAX + "<<<<<<<");
            if (MAX == 0)
                return;

            int zoomValue = params.getZoom();
            //1拉开
            if (type == 1) {
                zoomValue += 2;
            } else {
                zoomValue -= 2;
            }
            if (zoomValue > MAX) zoomValue = MAX;

            if (zoomValue < 0) zoomValue = 0;
            params.setZoom(zoomValue);
            mCamera.setParameters(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * PC控制听筒和外音
     */
    public void front() {
        isfrontBoolean = !isfrontBoolean;
        MeetingSession.getInstance().setLoudSpeaker(isfrontBoolean);

    }


    /**
     * PC控制退出
     */
    public void exitMeet(String userCode) {
        new MessageDialog(VideoActivity.this, R.style.dialog, new MessageDialog.SubmitOnClick() {

            @Override
            public void onSubmitOnClickSure() {
            }

            @Override
            public void onSubmitOnClickCancel() {

            }
        }, "完成定损", "后台定损人员【" + userCode + "】温馨提示您，现在可以关闭视频，完成定损任务。", "确定", "取消");


    }

    /**
     * 图片item点击事件
     */
    protected void gridViewItemClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                ShowImagePopWindow show = new ShowImagePopWindow(VideoActivity.this, imagePathList, arg2);
                //设置PopupWindow全屏
                show.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                show.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                show.showAtLocation(findViewById(R.id.body), Gravity.CENTER, 0, 0);
            }

        });
    }


    /**
     * 初始化视频和连接服务器
     */
    protected void initVideo() {
        try {
            /**
             * 众望视频初始化
             * 最后一位参数控制boolean  是否输出日志
             */
            usefront = hasfront = MeetingSession.getInstance().Init(this, "demo", "", true);
            // 注册消息
            NotificationCenter.getInstance().addObserver(this, MeetingSession.NET_CONNECT_ING);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.NET_CONNECT_SUCCESS);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.NET_CONNECT_FAILED);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.NET_CONNECT_BREAK);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.NET_CONNECT_ENABLE_PRESENCE);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.NET_CONNECT_USER_IN);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.NET_CONNECT_USER_OUT);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.UI_NOTIFY_USER_AUDIO_CHANGE);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.UI_NOTIFY_USER_WATCH_VIDEO);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.UI_NOTIFY_SELF_VEDIO_WISH_CHANGE);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.UI_NOTIFY_USER_VEDIO_CHANGE);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.UI_NOTIFY_CHAT_RECEIVE_TEXT);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.UI_NOTIFY_USER_UNREAD_MSG);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.UI_NOTIFY_USER_WHITE_PAD_DOC_CHANGE);
//            NotificationCenter.getInstance().addObserver(this, MeetingSession.UI_NOTIFY_USER_PICTURE_TAKEN);

            NotificationCenter.getInstance().addObserver(this, MeetingSession.NET_CONNECT_WARNING);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.VIDEO_NOTIFY_CAMERA_DID_OPEN);
            NotificationCenter.getInstance().addObserver(this, MeetingSession.VIDEO_NOTIFY_CAMERA_WILL_CLOSE);

            //	MeetingSession.UI_NOTIFY_USER_SERVER_RECORDING  录制视频回调
            // 设置最大发言数，在请求会议室调用，调式用一般不要打开默认9个
            MeetingSession.getInstance().setMaxSpeakerCount(maxSpeakerCount);
            usefront = true;
            startMeeting();
        } catch (Exception ex) {
            ToastUtils.showShort(this, "初始化视频和连接服务器 错误");
            ex.printStackTrace();
            fileUtils.writeException(ex);
        }

    }

    /**
     * 进入会议
     */
    protected void startMeeting() {
        onConnect(_serverMix, 0);
    }

    /**
     * 设置视频方向
     */
    public void reSetRotation() {
        WindowManager wm = this.getWindowManager();
        int rotation = wm.getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                MeetingSession.getInstance().setRotate(1);
                break;
            case Surface.ROTATION_90:
                MeetingSession.getInstance().setRotate(4);
                break;
            case Surface.ROTATION_180:
                MeetingSession.getInstance().setRotate(2);
                break;
            case Surface.ROTATION_270:
                MeetingSession.getInstance().setRotate(3);
                break;
        }
        LogUtils.d("ROT", "" + rotation + "--");

    }

    /**
     * 连接服务器结果，在enterMeeting后回调 status :0成功，非0失败，失败通常代表网络不可用或服务器不可达
     */
    public void onConnect(boolean serverMix, int state) {

        try {

            if (dialog == null)
                dialog = DialogUtil.show(this, "", "正在登录服务器，请稍候...");
            int uid = (int) (Math.random() * 100000);
            LogUtils.i("login", "登录服务器==================");
            // 进入会议
            // ip :媒体服务器地址，可以是域名或IP
            // port :媒体服务器端口，取决于服务器的配置，默认是443
            // nickname :与会者的昵称
            // meeting_id:会议号，字母和数字的组合，用户指定，需要保证全局唯一
            // user_id :调用者系统中的与会者id，用于将会议中的与会者和调用者系统中的与会者一一对应
            // serverMix :是否使用服务器进行混音
//            MeetingSession.getInstance().enterMeeting(Url.VIDEO_IP, Url.VIDEO_PORT,
//                    userName, meetingId, uid, serverMix);
            //            MeetingSession.getInstance().enterMeeting(ip, port, "user"+uid, mid, uid, serverMix, 0);
            MeetingSession.getInstance().enterMeeting(Constants.VIDEO_IP, Constants.VIDEO_PORT, userName, meetingId, uid, serverMix, 0);
        } catch (Exception ex) {
            showShortToast("连接服务器结果 错误");
            ex.printStackTrace();
            fileUtils.writeException(ex);
        }


    }

    /**
     * 用户列表新增一个用户
     *
     * @param name
     * @param id
     * @param redioBoolean
     * @param videoBoolean
     */
    private void insertUserInfo(String name, int id, boolean redioBoolean,
                                boolean videoBoolean) {
        VideoUserInfoVO us = new VideoUserInfoVO();
        us.setName(name);
        us.setMeetBuddyID(id);
        us.setRedio(redioBoolean);
        us.setVideo(videoBoolean);
        userList.add(us);

    }

    /**
     * 所有消息指令的回调函数
     */
    public void didReceivedNotification(int type, Object... arg1) {
//        ToastUtils.showShort(this, "notify " + type + " " + arg1);
        dialog.dismiss();
        switch (type) {
            case MeetingSession.NET_CONNECT_ING:
                LogUtil.w("====================================正在连接 NET_CONNECT_ING");
                if (null != speed)
                    speed.stop();
                // 正在连接
                break;
            case MeetingSession.NET_CONNECT_FAILED:
                if (null != speed)
                    speed.stop();
                LogUtil.w("=========================" +
                        "===========连接失败 NET_CONNECT_FAILED");
                // 连接失败
                _watchingPeerID = 0;
                ToastUtils.showShort(this, "网络连接失败");
                break;
            case MeetingSession.NET_CONNECT_BREAK:
                if (null != speed)
                    speed.stop();
                LogUtil.w("====================================连接中断 NET_CONNECT_FAILED");
                ToastUtils.showShort(this, "连接中断");
                // 连接中断
                ToastUtils.showShort(this, "视频连接中断");
                break;
            case MeetingSession.NET_CONNECT_SUCCESS:
                if (null != speed)
                    speed.start(1000);
                LogUtil.w("====================================连接成功 NET_CONNECT_FAILED");
                ToastUtils.showShort(this, "连接成功");
                // 连接成功
                break;
            case MeetingSession.NET_CONNECT_ENABLE_PRESENCE:
                if (null != speed)
                    speed.start(1000);
                LogUtil.w("====================================成功出席会议 NET_CONNECT_FAILED");
                // 成功出席会议
                if (null != dialog)
                    dialog.dismiss();
                ToastUtils.showShort(this, "连接成功");
                // //设置摄像头 前置和 true后置
                MeetingSession.getInstance().switchCamera(false);
                // //设置清晰度
                // // highquality :true表示高分辨率（但占用带宽和CPU较大，慎用），false表示中等分辨率
                // // setCameraQuality除自己外的所有人，-1:包括自己的所有人，>0:指定某个与会者的peerID
//                MeetingSession.getInstance().setCameraQuality(false);//码流低
                MeetingSession.getInstance().setCameraQuality(sharpness);
//                --------高分辨率640*480(标清：清晰度可以达到客户要求)
//                MeetingSession.getInstance().setCameraQuality(false);
//                --------低分辨率320*240(普清：这个相对将会模糊)
                // //是否使用外音
                MeetingSession.getInstance().setLoudSpeaker(isfrontBoolean);
                MeetingSession.getInstance().StartSpeaking();

                // myPid = MeetingUserMgr.getInstance().getSelfUser().getPeerID();
                myPid = MeetingSession.getInstance().getUserMgr().getSelfUser().getPeerID();
                reSetRotation();


                // 添加自己上用户列表中
                insertUserInfo(
                        // MeetingUserMgr.getInstance().getSelfUser().getName(),
                        // MeetingUserMgr.getInstance().getSelfUser().getPeerID(),
                        MeetingSession.getInstance().getUserMgr().getSelfUser().getName(),
                        MeetingSession.getInstance().getUserMgr().getSelfUser().getPeerID(),
                        false, false);

                LogUtil.w("====================================userList" + userList.size());
                userPopWindow.notifyDataSetChanged(userList);

                // 如果自己是现场查勘员，则播放下自己的视频
                if (userType == 1 && setLossName.equals(userName)) {

                    isVideo = true;
                    this.playVideo();
                    //开始录制自己的视频
                    //开始录制：
                    startRecording();

                } else {
                    // 设置后台人员不上传视频，即没有摄像头
                    MeetingSession.getInstance().setWatchMeWish(false);
                }
                break;
            case MeetingSession.NET_CONNECT_USER_IN:
                if (mCamera != null) {//聚焦
                    carmBoolean = false;//聚焦
                    takePicture();//触发拍照事件
                }
                if (null != speed)
                    speed.start(1000);
                LogUtil.w("====================================用户进入会议 NET_CONNECT_FAILED");
                // 用户进入会议（p1 int 用户peerID）（p2 boolean 是否比你先进入）
                // MeetingUser mu = MeetingUserMgr.getInstance().getUser(
                // (Integer) arg1[0]);


                MeetingUser mu = MeetingSession.getInstance().getUserMgr().getUser((Integer) arg1[0]);

                if (!(Boolean) arg1[1]) {
                    // 比例先来的用户不用提示，只提示比你后进的用户
                    if (mu != null) {
                        ToastUtils.showShort(this, mu.getName() + "进入了群组");
                    }
                }
                try {
                    insertUserInfo(mu.getName(), mu.getPeerID(), false, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userPopWindow.notifyDataSetChanged(userList);
                // 录制现场查勘员视频
                for (VideoUserInfoVO vuser : userList) {
                    if (userType != 1 && vuser.getName().equals(setLossName)) {
                        // 播放
                        MeetingSession.getInstance().PlayVideo(vuser.getMeetBuddyID(), true,
                                surfaceView1, 0, 0, 1, 1, 0, false, 0);


                        return;
                    }
                }
                break;
            case MeetingSession.NET_CONNECT_USER_OUT:
                if (null != speed)
                    speed.stop();
                LogUtil.w("====================================有用户离开 NET_CONNECT_FAILED");
                // 有用户离开
                for (int i = 0, j = userList.size(); i < j; i++) {
                    if (userList.get(i).getMeetBuddyID() == (Integer) arg1[0]) {
                        ToastUtils.showShort(this, arg1[1] + "离开了群组");
                        userList.remove(i);
                        userPopWindow.notifyDataSetChanged(userList);
                        return;
                    }
                }
                break;
            case MeetingSession.UI_NOTIFY_USER_AUDIO_CHANGE:
                LogUtil.w("====================================有用户音频状态有变化 NET_CONNECT_FAILED");
                // 有用户音频状态有变化（p1 int 用户peerID）（p2 int 用户音频状态《RequestSpeak_Disable
                // ，RequestSpeak_Allow，RequestSpeak_Pending》）
                if ((Integer) arg1[0] == myPid
                        && MeetingSession.RequestSpeak_Pending == (Integer) arg1[1]) {
                    ToastUtils.showShort(this, "由于超过最发发言人数，请等待！");
                }
                for (int i = 0, j = userList.size(); i < j; i++) {
                    if (userList.get(i).getMeetBuddyID() == (Integer) arg1[0]) {
                        // 设置用户语音状态 RequestSpeak_Disable 没有进行发言
                        // RequestSpeak_Allow 正在进行发言 RequestSpeak_Pending//正在排队
                        // userList.get(i).setRedio(true);
                        userList.get(i).setRedioType((Integer) arg1[1]);
                        userPopWindow.notifyDataSetChanged(userList);
                        return;
                    }
                }
                break;
            case MeetingSession.UI_NOTIFY_CHAT_RECEIVE_TEXT:
                LogUtil.w("====================================接收到手机截图指令 NET_CONNECT_FAILED");
                if ("screenshot_00_11_command".equals(arg1[1])) {//抓拍指令
//                    uploadtype = "1";// 上传类型(1,PC端截屏图片 2,移动查勘图片 3移动视频图片，)
                    uploadtype = "3";// 上传类型(1,PC端截屏图片 2,移动查勘图片 3移动视频图片，)
                    carmBoolean = true;
                    takePicture();//触发拍照事件
                    return;//此处不显示聊天记录，直接返回
                } else if ("setLoudSpeaker_00_11_command".equals(arg1[1])) {
                    front();//外放听筒切换
                    return;
                } else if ("pull_nearly_command".equals(arg1[1])) {//聚距拉近
                    zoom(1);
                    return;
                } else if ("pull_away_command".equals(arg1[1])) {//聚距拉远
                    zoom(2);
                    return;
                } else if ("flash_light_command".equals(arg1[1])) {//开启闪光灯
                    try {
                        if (openDeng) {
                            // 打开
                            openLamp(Camera.Parameters.FLASH_MODE_ON);
                            cameraDeng_iv.setImageResource(R.drawable.deng_on);
                        } else {
                            // 关闭
                            openLamp(Camera.Parameters.FLASH_MODE_OFF);
                            cameraDeng_iv.setImageResource(R.drawable.deng_off);

                        }
                        openDeng = !openDeng;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                } else if ("focus_command".equals(arg1[1])) {//聚焦
                    carmBoolean = false;//聚焦
                    takePicture();//触发拍照事件
                    return;
                } else if ("give_up_task_command".equals(arg1[1])) {//放弃任务
                    showDialog("后端人员放弃任务");
                    return;
                } else if ("finish_task_command".equals(arg1[1])) {//完成定损
                    showDialog("后端人员完成定损");
                    return;
                } else if ("refuse_task_command".equals(arg1[1])) {//拒绝任务
                    showDialog("后端人员拒绝任务");
                    return;
                } else if ("exitmeet_00_11_command".equals(((String) arg1[1]).split("#")[0])) {
                    String userCode = ((String) arg1[1]).split("#")[1];
                    exitMeet(userCode);//退出会议
                    stopRecording();//停止录制
                    return;
                } else if ("finish_video".equals(arg1[1])){//视频结束指令
                    finishMeeting(1);
                }
                // 有消息聊天时，回调
                addMsgVO((String) arg1[2], (String) arg1[1]);
                msgListAdapter.notifyDataSetChanged(msgList);
                // 如果不在消息页面，则提示
                if (tabpager.getCurrentItem() != 2) {
                    vhandler.removeCallbacks(msgRrunnable);
                    vhandler.postDelayed(msgRrunnable, 1000);
                }
                break;
            case MeetingSession.UI_NOTIFY_USER_UNREAD_MSG:
                LogUtil.w("====================================有未读 ，该方法暂时无效 NET_CONNECT_FAILED");
                // 有未读 ，该方法暂时无效
                addMsgVO((String) arg1[2], (String) arg1[1]);
                msgListAdapter.notifyDataSetChanged(msgList);
                break;
            case MeetingSession.UI_NOTIFY_USER_WATCH_VIDEO:
                LogUtil.w("====================================他人查看或关闭别人的视频  NET_CONNECT_FAILED");
                // 他人查看或关闭别人的视频（p1 int 用户peerID）（p2 boolean 查看还是关闭）
                if ((Boolean) arg1[1]) {
                    MeetingSession.getInstance().PlayVideo((Integer) arg1[0], true,
                            surfaceView1, 0, 0, 1, 1, 0, false, 0);
                } else {
                    MeetingSession.getInstance().PlayVideo((Integer) arg1[0], false,
                            null, 0, 0, 1, 1, 0, false, 0);

                }
                break;
            case MeetingSession.UI_NOTIFY_USER_VEDIO_CHANGE:
                LogUtil.w("====================================录制人（现场查勘员） 打开或关闭视频  NET_CONNECT_FAILED");
                break;
            case MeetingSession.UI_NOTIFY_USER_WHITE_PAD_DOC_CHANGE:
                LogUtil.w("====================================  有图片上传会调用  NET_CONNECT_FAILED");
                // 有图片上传会调用
                JSONObject js = (JSONObject) arg1[0];
                boolean isdel;
                try {
                    isdel = js.getBoolean("isdel");
                    if (!isdel) {
                        mainPage2GridViewAdapter.notifyDataSetChanged(imagePathList);
                    }

                } catch (JSONException e) {
                    showShortToast("有图片上传会调用 错误");
                    e.printStackTrace();
                    fileUtils.writeException(e);
                }

                break;

            case MeetingSession.VIDEO_NOTIFY_CAMERA_DID_OPEN: {
                LogUtil.w("====================================打开相机回调");
                mCamera = (Camera) arg1[0];
//                Boolean isFront = (Boolean) args[1];
//                log((isFront ? "Front" : "Back" ) + " camera open");
                if (null == mCamera) {
//                    showToast("没有打开摄像头权限,请添加权限！");
//                    LogUtil.w("没有打开摄像头权限,请添加权限！");
                    // 初始化提示框
                    final AlertDialog alertDialog = new AlertDialog.Builder(VideoActivity.this)
                            .create();
                    alertDialog.show();
                    alertDialog.getWindow().setContentView(R.layout.scenesurvey_casesurvey_mydialog);
                    TextView dialogContent = alertDialog.getWindow().findViewById(R.id.tip);
                    dialogContent.setText("打开相机失败,请检查软件是否具有拍照权限？有,请重启手机;没有,请开通权限");
                    alertDialog.getWindow().findViewById(R.id.okBtn)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    alertDialog.dismiss();
                                }
                            });
                    return;
                }
                _bUsingFront = (Boolean) arg1[1];
            }
            break;
            case MeetingSession.VIDEO_NOTIFY_CAMERA_WILL_CLOSE: {
            }
            break;
            case MeetingSession.NET_CONNECT_WARNING: {//网络链接状态

                int warning = (Integer) arg1[0];
                if (11 == warning) {//当warning=11时，说明没有打开麦克风的权限

                }

                if (warning == WEIYI_VIDEO_OUT_SLOW)
                    warningString = "视频发送速度慢";
                else if (warning == WEIYI_VIDEO_OUT_DISCONNECT)
                    warningString = "视频发送连接断开重连";
                else if (warning == WEIYI_VIDEO_IN_SLOW)
                    warningString = "视频接收速度慢";
                else if (warning == WEIYI_VIDEO_IN_DISCONNECT)
                    warningString = "视频接收连接断开重连";
                else if (warning == WEIYI_AUDIO_DISCONNECT)
                    warningString = "音频连接断开重连";
                else if (warning == WEIYI_AUDIO_DISABLED) {
                    warningString = "未能取得打开麦克风权限";

                    // 初始化提示框
                    final AlertDialog alertDialog = new AlertDialog.Builder(VideoActivity.this)
                            .create();
                    alertDialog.show();
                    alertDialog.getWindow().setContentView(
                            R.layout.scenesurvey_casesurvey_mydialog);
                    TextView dialogContent = alertDialog.getWindow().findViewById(R.id.tip);
                    dialogContent.setText("打开麦克风失败,请检查软件是否具有录音权限？有,请重启手机;没有,请开通权限");
                    alertDialog.getWindow().findViewById(R.id.okBtn)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    alertDialog.dismiss();
                                }
                            });
                } else
                    warningString = "未知警告";
            }
            break;

        }

    }

    private void showDialog(String str) {
        // 初始化提示框
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .create();
        alertDialog.show();
        alertDialog.getWindow().setContentView(
                R.layout.scenesurvey_casesurvey_mydialog);
        TextView tv1 = alertDialog.getWindow().findViewById(R.id.tip);
        tv1.setText("提示:" + str + "！");
        alertDialog.getWindow().findViewById(R.id.okBtn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        exit();
                        alertDialog.dismiss();
                    }
                });
    }


    /**
     * 拍照
     */
    void takePicture() {
        LogUtil.w("进入takePicture()");
        if (0 != takingPicture) {
            showShortToast("正在操作相机,请稍后再试！");

            new Handler().postDelayed(new Runnable() {//延迟2秒，如果还是不可以拍照就修改状态
                @Override
                public void run() {
                    if (0 != takingPicture) {
                        takingPicture = 0;
                    }
                }
            }, 2000);
            return;
        }


//        carmBoolean = true;
        //这里需要对高度或宽度进行排序下
//        List<Camera.Size> list = MeetingSession.getInstance().getCurrentCameraResolutions();
        List<Camera.Size> list = getCurrentCameraResolutions();
        if (list != null) {
            LogUtil.w("list != null");
            int width = 1024;
            int height = 768;
            boolean isBoolean = false;
            for (int i = 0; i < list.size(); i++) {
                if (width == list.get(i).width) {
                    height = list.get(i).height;
                    width = list.get(i).width;
                    isBoolean = true;
                    LogUtil.w("支持该拍照尺寸");
                    break;
                }
            }
            if (!isBoolean) {
                height = list.get(list.size() - 1).height;
                width = list.get(list.size() - 1).width;
            }

            try {
                takePhoto(width, height);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            LogUtil.w("list = null");
            int width = 1024;
            int height = 768;
            try {
                takePhoto(width, height);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // 1 获取支持的拍照尺寸
    public List<Camera.Size> getCurrentCameraResolutions() {
        if (mCamera == null)
            return null;

        List<Camera.Size> l = null;
        try {
            Camera.Parameters p = mCamera.getParameters();
            l = p.getSupportedPictureSizes();
        } catch (Exception e) {
            return null;
        }

        ComparatorSize comparator = new ComparatorSize();
        Collections.sort(l, comparator);
        return l;
    }


    // 2 拍照
    public void takePhoto(int width, int height) throws Exception{
        if (mCamera == null) {
            LogUtil.w("mCamera=null");
            return;
        }

        if (takingPicture > 0) {
            LogUtil.w("takingPicture=" + takingPicture);
            return;
        }
        takingPicture = 1;
        Camera.Parameters p = mCamera.getParameters();
        List<Camera.Size> l = p.getSupportedPictureSizes();
        Camera.Size sz = p.getPictureSize();

        if (sz.width != width || sz.height != height) {
            LogUtil.w("相机没有该尺寸");
            sz.width = width;
            sz.height = height;
            if (l.contains(sz)) {

                p.setPictureSize(width, height);
                try {
                    mCamera.setParameters(p);
                    LogUtil.w("Picture size = " + p.getPictureSize().width + " " + p.getPictureSize().height);
                } catch (Exception e) {
                    takingPicture = 0;
                    LogUtil.w("Error setSize: " + e.getMessage());
                    return;
                }
            } else {
                LogUtil.w("相机列表没有该尺寸");
                takingPicture = 0;
                return;
            }
        }

        p = mCamera.getParameters();

        try {
            mCamera.setParameters(p);
        } catch (Exception e) {
            LogUtil.w("Error focus mode: " + e.getMessage());
        }
        takingPicture = 2;
        p = mCamera.getParameters();
        LogUtil.w("Focus Mode = " + p.getFocusMode());
        if ((Build.MANUFACTURER.equalsIgnoreCase("xiaomi") && _bUsingFront)
                || (!p.getFocusMode().equals(Camera.Parameters.FOCUS_MODE_AUTO)
                && !p.getFocusMode().equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))) {
            LogUtil.w("跳过聚焦");
            time2TakePicture();
        } else {
            try {
                LogUtil.w("准备聚焦");

                mCamera.cancelAutoFocus();
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        LogUtil.w("聚焦= " + success);
                        time2TakePicture();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.w("聚焦失败");
                time2TakePicture();
            }


        }
    }

    public void time2TakePicture() {
        LogUtil.w("time2TakePicture(): " + takingPicture);
        if (takingPicture != 2)
            return;

        takingPicture = 3;

        boolean failed = false;
        if (mCamera == null || !isCameraOK) {
            failed = true;
            LogUtil.w("mCamera == null");
        } else {
            try {
                mCamera.takePicture(null, null, pictureCallback);
                LogUtil.w("获取图片回调 成功");
            } catch (Exception e) {
                LogUtil.w("获取图片回调  失败");
                failed = true;
            }
        }

        if (failed) {
            if (mCamera != null) {
                mCamera.cancelAutoFocus();
                try {
                    Camera.Parameters p = mCamera.getParameters();
                    p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    LogUtil.w("Focus Mode = " + p.getFocusMode());
                    mCamera.setParameters(p);
                } catch (Exception e) {
                    LogUtil.w("Error focus mode: " + e.getMessage());
                }
            }

            takingPicture = 0;
        }
    }

    // 3 拍照完毕的回调，注意这里一定要startPreview（否则视频会暂停），并且要设置focuseMode为continuous_video（否则视频中不再自动变焦）
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        // 该方法用于处理拍摄后的照片数据
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            LogUtil.w("Photo taken " + (data != null ? data.length : ""));

            if (camera != null) {
                camera.cancelAutoFocus();
            }

            if (camera == mCamera && isCameraOK) {
                try {
                    Camera.Parameters p = mCamera.getParameters();
                    p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    LogUtil.w("Focus Mode = " + p.getFocusMode());
                    mCamera.setParameters(p);
                } catch (Exception e) {
                    LogUtil.w("Error focus mode: " + e.getMessage());
                }
                mCamera.startPreview();
            }

            takingPicture = 0;

            if (carmBoolean) {//拍照时
                saveCameraImage(true, data);
            } else {
                carmBoolean = true;
            }
        }
    };


    /**
     * 拍照后保存图片
     *
     * @param isBoolean
     * @param picterByte
     */
    private void saveCameraImage(boolean isBoolean, byte[] picterByte) {
        LogUtil.w("saveCameraImage方法   isBoolean=" + isBoolean);
        if (isBoolean) {
            try {
                if (SDCardUtils.isSDCardEnable()) {
                    FileOutputStream out = null;
                    File file = null;
                /* 保存图片到sd卡中 */
                    try {
                        file = new File(path);
                        // 保存图片
                        out = new FileOutputStream(file);
                        out.write(picterByte);
                    } catch (Exception e) {
                        showShortToast("拍照后保存图片 错误");
                        e.printStackTrace();
                        fileUtils.writeException(e);
                    } finally {
                        if (out != null)
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                fileUtils.writeException(e);
                            }
                    }
                    // 压缩程序
                    EstarImage estar = new EstarImage();
                    if (null != file && file.exists()) {
                        LogUtil.w("file.exists()");
                        String strPath = path;
                        // 开始压缩
                        String jpg = taskNo + "_" + DateUtil.getLongData() + ".jpg";// 图片名称
                        String img_path = fileUtils.getImageMd(reportNo) + "/" + jpg;

                        //开始压缩
                        int resultCode = 0;
                        resultCode = estar.CompressPictureByResolution2(strPath, img_path, 1920, 1080, 100, 500);
                        if (!(resultCode == 2 || (resultCode >= 100 && resultCode <= 100 + 100))) {
                            resultCode = estar.CompressPictureByResolution2(strPath, img_path, 1024, 768, 90, 500);
                        }
                        if (!(resultCode == 2 || (resultCode >= 100 && resultCode <= 100 + 100))) {
                            ToastUtils.showShort(this, "压缩照片失败,请重新拍摄！");
                            return;
                        }

                        LogUtil.w("resultCode=" + resultCode);
                        if (resultCode == 2
                                || (resultCode >= 90 && resultCode <= 90 + 100)) {
                            // 添加水印
                            ImageCanvas canvas = new ImageCanvas(VideoActivity.this);

                            if ("1".equals(uploadtype)) {//1,PC端截屏图片 2,移动查勘图片 3移动视频图片
                                ImageVO imageVO = new ImageVO();
                                imageVO.setTaskno(taskNo);// 任务号
                                if ("".equals(jpg)) {
                                    jpg = taskNo + "_" + DateUtil.getLongData() + ".jpg";// 图片名称
                                }
                                imageVO.setImagePath(img_path);
                                imageVO.setImagename(jpg);// 图片名称
                                imageVO.setUsercode(user.getEmpCde());// 用户代码
                                imageVO.setUpcliamflag(1);// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败  每张图片最多上传3次
                                imageVO.setReportno(reportNo);// 报案号
                                imageVO.setUploadtype(uploadtype);// 上传类型(1,PC端截屏图片 2,移动查勘图片 3移动视频图片，)
                                imageVO.setUploadtimes(0);// 上传次数 3次以上不上传
                                imageVO.setMark("视频图片");// 标记(备注，中文名字)
                                imageVO.setIsFirstUpload("0");//手机是否首次上传 0首次 1补传

                                sendImage(imageVO);

                                return;
                            }
                            address = location_tv.getText().toString();
//                            boolean addCanvas = canvas.watermarkBitmap(img_path, address, 18, myApplication.getUserName(), myApplication.getTaskVO().getReportNo(), nowTime);
                            boolean addCanvas = canvas.watermarkBitmap(img_path, address, 18, user.getEmpCde(), mission.getRptNo(), nowTime);
                            if (!addCanvas) {
                                showShortToast("添加水印失败,请重新拍摄！");
                                return;
                            }
                            ImageVO imageVO = new ImageVO();
                            imageVO.setTaskno(taskNo);// 任务号
                            if ("".equals(jpg)) {
                                jpg = taskNo + "_" + DateUtil.getLongData() + ".jpg";// 图片名称
                            }
                            imageVO.setImagePath(img_path);
                            imageVO.setImagename(jpg);// 图片名称
                            imageVO.setUsercode(user.getEmpCde());// 用户代码
//                            imageVO.setPiccls("2");// 大类        a    1、基础类  2、现场类  3、损失类
                            imageVO.setUpcliamflag(1);// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败  每张图片最多上传3次
                            imageVO.setReportno(reportNo);// 报案号
                            imageVO.setUploadtype(uploadtype);// 上传类型(1,PC端截屏图片 2,移动查勘图片 3移动视频图片，)
                            imageVO.setUploadtimes(0);// 上传次数 3次以上不上传
                            imageVO.setMark("视频图片");// 标记(备注，中文名字)
                            imageVO.setIsFirstUpload("0");//手机是否首次上传 0首次 1补传

                            //显示
                            pictureModule.showPicture(this.findViewById(R.id.taskinfo), img_path, true);

                            sendImage(imageVO);


                            new IsShowImage().start();
                        } else {
                            if ("3".equals(uploadtype)) {//1,PC端截屏图片 2,移动查勘图片 3移动视频图片
                                ToastUtils.showShort(this, "拍摄照片失败，请重新拍摄！");
                                LogUtil.w("2   image_id=" + image_id);
                                --image_id;
                                LogUtil.w("2   image_id=" + image_id);
                            }
                        }
                        // end 压缩结束
                    }
                } else {
                    if ("3".equals(uploadtype)) {//1,PC端截屏图片 2,移动查勘图片 3移动视频图片
                        ToastUtils.showShort(this, "拍照失败，请重试!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if ("3".equals(uploadtype)) {//1,PC端截屏图片 2,移动查勘图片 3移动视频图片
                    showShortToast("生成照片失败");
                }
            }
        } else {
            if ("3".equals(uploadtype)) {//1,PC端截屏图片 2,移动查勘图片 3移动视频图片
                ToastUtils.showShort(this, "失败，请重试!");
            }
        }
    }


    /**
     * 上传图片
     *
     * @param
     */
    private void sendImage(final ImageVO imageVO) {

        try {
            String imgHashCode = getMD5Checksum(imageVO.getImagePath());
            String imageBase64 = ConvertBase64HH(imageVO.getImagePath());
            imageBase64 = imageBase64.replace("+", "%2B");
            LogUtils.i("图片上传hash码为:", "-------------------------------------------\n" + imgHashCode + "-------------名称为:" + imageVO.getImagename());
            if (!StringUtils.isEmpty(imageBase64)) {// 如果图片64位码为空

                final MyProgressDialog progressDialog = new MyProgressDialog(VideoActivity.this, "图片上传中...");

                ImageUploadRequest request = new ImageUploadRequest();
                request.getData().setTaskno(imageVO.getTaskno());
                request.getData().setReportno(imageVO.getReportno());
                request.getData().setUsercode(imageVO.getUsercode());
                request.getData().setUploadType(imageVO.getUploadtype());//移动查勘图片
                request.getData().setImgHashCode(imgHashCode);
                request.getData().setImageData(imageBase64);

                request.getData().setFileName(imageVO.getImagename());
                request.getData().setPicDtl(imageVO.getPicDtl());
                request.getData().setPicCls(imageVO.getPicCls());

                final Gson gson = new Gson();
                String reqMsg = gson.toJson(request);

//                RequestParams params = HttpUtils.buildRequestParam(Constants.IMAGEUPLOAD, reqMsg);
                RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.IMAGEUPLOAD, reqMsg);

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
                        if (response.getSuccess()) {
                            if ("3".equals(uploadtype)){
                                showShortToast("图片上传成功");
                                imageVO.setUpcliamflag(2);//将图片设置为上传成功
                            }
                        } else {
                            if ("3".equals(uploadtype)) {//1,PC端截屏图片 2,移动查勘图片 3移动视频图片
                                showShortToast("图片上传失败:" + response.getMsg());
                                imageVO.setUpcliamflag(3);// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败  每张图片最多上传3次                            }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        LogUtils.i("图片上传请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                        showShortToast(ex.getLocalizedMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        LogUtils.i("图片上传请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                        showShortToast(cex.getLocalizedMessage());
                    }

                    @Override
                    public void onFinished() {
                        if ("3".equals(uploadtype)){//1,PC端截屏图片 2,移动查勘图片 3移动视频图片
                            imageVO.save();//插入数据库
                            imagePathList.add(imagePath + "/" + imageVO.getImagename());
                            mainPage2GridViewAdapter.notifyDataSetChanged(imagePathList);
                        }else {
                            fileUtils.deletePath(imagePath + "/" + imageVO.getImagename());
                        }
                        progressDialog.stopDialog();
                    }
                });
            } else {
                showShortToast("图片转码失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class IsShowImage extends Thread {

        public void run() {
            try {
                threadIsShow = false;
                Thread.sleep(3000);
                Message mesg = new Message();
                mesg.what = 1;
                vhandler.sendMessage(mesg);


            } catch (Exception e) {
                e.printStackTrace();
                fileUtils.writeException(e);
            }
        }
    }

    /**
     * 添加一个消息进集合中
     *
     * @param name
     * @param text
     */

    protected void addMsgVO(String name, String text) {
        MsgVO msg = new MsgVO();
        msg.setName(name);
        msg.setMsg(text);
        if (name.equals(userName)) {
            msg.setOptionType("2");
        } else {
            msg.setOptionType("1");
        }
        msgList.add(msg);
    }


    /**
     * 发送消息
     *
     * @param msg
     */
    protected void sendMessage2(String msg) {
        JSONObject params = new JSONObject();
        try {
            params.put("senderID", myPid);
            params.put("toID", 0);
            params.put("name", userName);
        } catch (JSONException e) {
            showShortToast("发送消息 错误");
            e.printStackTrace();
            fileUtils.writeException(e);
        }
        MeetingSession.getInstance().sendTextMessage(0, msg, params);
        addMsgVO(userName, msg);
        msgListAdapter.notifyDataSetChanged(msgList);
        msgText.setText("");
        msgListView.setSelection(msgList.size() - 1);
    }

    /**
     * 设置消息图标
     *
     * @param drawableID
     */
    protected void setMsgLeftDrawable(int drawableID) {
        Drawable drawable = getResources().getDrawable(drawableID);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        button_three.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 更新消息图标
     */
    void startMsgFlashing() {
        if (msgFlashingBoolean) {
            setMsgLeftDrawable(R.mipmap.bar_msg);
        } else {
            setMsgLeftDrawable(R.mipmap.jqa);
        }
        msgFlashingBoolean = !msgFlashingBoolean;
    }

    /**
     * 更新消息图标闪烁状态
     */
    protected Runnable msgRrunnable = new Runnable() {
        public void run() {
            startMsgFlashing();
            vhandler.postDelayed(this, 800);
        }

    };

    /**
     * 切换 视频、图片、消息
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.button_one) {
            tabpager.setCurrentItem(0);
            showSpeed = true;
        } else if (checkedId == R.id.button_two) {
            tabpager.setCurrentItem(1);
            showSpeed = false;
        } else if (checkedId == R.id.button_three) {
            vhandler.removeCallbacks(msgRrunnable);
            setMsgLeftDrawable(R.mipmap.bar_msg);
            tabpager.setCurrentItem(2);
            showSpeed = false;
        }
    }


    /**
     * 播放自己的视频
     */
    protected void playVideo() {
        // 后置摄像头false
        MeetingSession.getInstance().switchCamera(false);

        MeetingSession.getInstance().PlayVideo(0, true,
                surfaceView1, 0, 0, 1, 1, 0, false, 0);

//        // 后置摄像头false
//        MeetingSession.getInstance().switchCamera(false);

        // 开始上传视频指令
        MeetingSession.getInstance().setWatchMeWish(true);

    }


    /**
     * 开始录制视频
     */
    protected void startRecording() {
        //开始录制：
        MeetingSession.getInstance().setFocusUser(myPid);//将某人选为焦点，录制时将录制该人的视频
        MeetingSession.getInstance().serverRecording(true);//开始服务器录制
    }


    /**
     * 结束录制视频
     */
    protected void stopRecording() {
        MeetingSession.getInstance().serverRecording(false);//停止服务器录制
    }


    private void exit() {
        //停止计时器
        vhandler.removeCallbacks(runnable);
        NotificationCenter.getInstance().removeObserver(this);
        stopRecording();
        // 退出会议
        MeetingSession.getInstance().exitMeeting();
        finish();
    }

    /**
     * 退出
     */
    public void exitMeeting(int type) {
        if (type == 0) {
            exit();
            return;
        }
        new MessageDialog(VideoActivity.this, R.style.dialog, new MessageDialog.SubmitOnClick() {

            @Override
            public void onSubmitOnClickSure() {
                exit();
            }

            @Override
            public void onSubmitOnClickCancel() {

            }
        }, "提示", "退出定损", "确定", "取消");
    }

    /**
     * 视频结束退出
     */
    public void finishMeeting(int type) {
        if (type == 0) {
            exit();
            return;
        }
        new MessageDialog(VideoActivity.this, R.style.dialog, new MessageDialog.SubmitOnClick() {

            @Override
            public void onSubmitOnClickSure() {
                exit();
            }

            @Override
            public void onSubmitOnClickCancel() {

            }
        }, "提示", "后端定损已完成，是否关闭", "确定", "取消");
    }


    /**
     * 返回
     */

    void backClose() {
        exitMeeting(1);
    }

    /**
     * 任务信息
     */
    void taskinfo() {
        if (null == taskInfoPopWindow) {
            taskInfoPopWindow = new TaskInfoPopWindow(VideoActivity.this, videoResult);
        }
        if (!taskInfoPopWindow.isShowing()) {
            taskInfoPopWindow.show(taskinfo);
        }
    }

    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//设置定位模式
        option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
        int span = 1000;
        try {
            span = Integer.valueOf(10000);//定位间隔时间ms
        } catch (Exception e) {
            e.printStackTrace();
        }
        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }


    /**
     * 显示即时流量
     */
    void getUidHttpTrafficData() {
        if (myTrafficStatistics == null) {
            myTrafficStatistics = new MyTrafficStatistics(this);
        }
        myTrafficStatistics.setITrafficStatistics(new MyTrafficStatistics.ITrafficStatistics() {
            public void result(String upData, String downData) {
                //该方法在线程中必须UI更新
//                updateTrafficDataTV(upData, downData);
                if (upData.equals("-1")) {
                    //手机不支持该获取流量函数
                    upDataTV.setVisibility(View.GONE);
                    downDataTV.setVisibility(View.GONE);
                    return;
                } else {
                    upData_tv = upData;
                    downData_tv = downData;
                    if (showSpeed) {
                        //更新UI方法  1
                        Message message = new Message();
                        message.what = 4;
                        vhandler.sendMessage(message);
                    }

                }
            }

        });
        myTrafficStatistics.start(3 * 1000);
    }

    /**
     * 网速低于15KB
     */
    private void slowSpeed() {
        resVideoLine("2");//视频类型 0-初始化值(即没有申请视频)； 1-视频模式； 2-图片模式； 3-后台拒绝； 4-后台无闲置人员
        MeetingSession.getInstance().setWatchMeWish(false);
    }

    /**
     * 更新流量TextView
     *
     * @param upData
     * @param downData
     */
    void updateTrafficDataTV(String upData, String downData) {
        if (upData.equals("-1")) {
            //手机不支持该获取流量函数
            upDataTV.setVisibility(View.GONE);
            downDataTV.setVisibility(View.GONE);
            return;
        }
        upDataTV.setText(upData + "KB");
        downDataTV.setText(downData + "KB");
    }

    /**
     * 捕获横竖屏切换的事件
     */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            //更改为LANDSCAPE
            case (Configuration.ORIENTATION_LANDSCAPE):
                //如果转换为横向屏时，有要做的事，请写在这里
                reSetRotation();
                break;
            //更改为PORTRAIT
            case (Configuration.ORIENTATION_PORTRAIT):
                //如果转换为竖向屏时，有要做的事，请写在这里
                reSetRotation();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == 88) {
            playVideo();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        image_id = 0;
        if (isVideo) {
            MeetingSession.getInstance().resumeLocalVideo();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();//停止定位
        if (isVideo) {
            MeetingSession.getInstance().pauseLocalVideo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (null != speed)
//                speed.stopCalculateNetSpeed();
                speed.stop();
            isUpdateImage = false;
            isAddressRun = false;
            if (null != myTrafficStatistics)
                myTrafficStatistics.stop();

            if (mCamera != null) {
                mCamera.setPreviewCallback(null) ;
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileUtils.writeException(e);
        }

    }

    // 拦截硬按键返回事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exitMeeting(1);
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
