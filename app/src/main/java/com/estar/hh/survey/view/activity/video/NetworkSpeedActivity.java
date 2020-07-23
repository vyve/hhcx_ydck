package com.estar.hh.survey.view.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.VideoCreateRequest;
import com.estar.hh.survey.entity.response.VideoCreateResponse;
import com.estar.hh.survey.entity.vo.VideoResult;
import com.estar.hh.survey.entity.vo.video.ResVideoLineVO;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.utils.NetWorkSpeedInfo;
import com.estar.hh.survey.utils.ReadFile;
import com.estar.hh.survey.view.activity.HuangheBaseActivity;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

//import com.tendcloud.tenddata.TCAgent;


/**
 * 测试网速
 * Created by ding on 2015/11/2.
 */
public class NetworkSpeedActivity extends HuangheBaseActivity {

    private RelativeLayout rlall;
    private TextView now_speed;
    private TextView average_speed;
    /**
     * 丢包显示
     */
    protected TextView spd;
//    NetSpeed speed;
    byte[] imageData = null;
    NetWorkSpeedInfo netWorkSpeedInfo = null;
    private final int UPDATE_SPEED = 1;// 进行中
    private final int UPDATE_DNOE = 0;// 完成下载
    private ImageView imageView;
    private int big = 0;
    private long small = 0;
    private List<Long> list = new ArrayList<Long>();
    private Thread thread1, thread2,thread3;
    private boolean change=true;
    private boolean onRun=true;
    /** Had finished bytes */
    public long hadFinishedBytes = 0;
    /** Total bytes of a file, default is 1024 bytes,1K */
    public long totalBytes = 1024;
//    private ResVideoLineVO resVideoLineVO=new ResVideoLineVO();
    private List<ResVideoLineVO> meetinglist=null;

    private VideoResult videoResult = null;
    private Mission mission = null;
    private UserSharePrefrence userShare = null;
    private User user = null;

    private Handler handler = new Handler() {
        private long tem = 0;
        private long falg = 0;
        private long numberTotal = 0;
        private long over=0;
        private long OKKB=30;//平均网速达标值

        private List<Long> list = new ArrayList<Long>();

        @Override
        public void handleMessage(Message msg) {
            int value = msg.what;
            switch (value) {
                case UPDATE_SPEED:
                    tem = netWorkSpeedInfo.speed / 1024;
                    list.add(tem);
//                    Log.i("a", "tem****" + tem);
                    for (Long numberLong : list) {
                        numberTotal += numberLong;
                    }
                    falg = numberTotal / list.size();
                    numberTotal = 0;
                    now_speed.setText("当前速度:"+tem + "kb/s");
                    average_speed.setText("平均速度:"+falg + "kb/s");
                    if(falg>over){
                        over=falg;
                    }
                    break;
                case UPDATE_DNOE:
                    change=false;
                    onRun=false;
                    thread1=null;
                    thread2=null;
                    thread3=null;
                        if (over>OKKB||over<1){//平均网速达标值或者下载地址异常
                            rlall.setVisibility(View.GONE);
                            resVideoLine("1");//发送视频请求 //视频类型 0-初始化值(即没有申请视频)； 1-视频模式； 2-图片模式； 3-后台拒绝； 4-后台无闲置人员

//                            Intent intent2 = new Intent(NetworkSpeedActivity.this, VideoActivity.class);
//                            intent2.putExtra("taskInfo", taskInfo);
//                            intent2.putExtra("resVideoLineVO", resVideoLineVO);
//                            startActivity(intent2);
//                            NetworkSpeedActivity.this.finish();
                        }else {
                            showShortToast("平均网速不达标(需有一超过"+OKKB+"KB)，请重试！");
                            NetworkSpeedActivity.this.finish();
                            resVideoLine("1");//发送视频请求 //视频类型 0-初始化值(即没有申请视频)； 1-视频模式； 2-图片模式； 3-后台拒绝； 4-后台无闲置人员
                        }

                    break;

                case 5:
//                    String speedMsg = "上行速度：" + msg.arg1 + "K/s；下行速度：" + msg.arg2 + "K/s！";
//                    speedMsg = msg.obj.toString();
//                    spd.setText(speedMsg);
//                    LogUtil.w("speedMsg =" + speedMsg);
//                    fileUtils.writeLog(speedMsg);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_speed);

        rlall = findViewById(R.id.rlall);
        now_speed = findViewById(R.id.now_speed);
        average_speed= findViewById(R.id.average_speed);
        spd = findViewById(R.id.spd);
        initWidget();

        initData();

    }


    public void initData() {
        userShare = new UserSharePrefrence(this);
        user = userShare.getUserEntity();
        mission = (Mission)getIntent().getSerializableExtra("mission");
    }

    /**
     * 手机端发起视频连线
     */
    public void resVideoLine(String videoMark) {

        final MyProgressDialog progressDialog = new MyProgressDialog(this, "发起视频连线，请稍候...");

        VideoCreateRequest request = new VideoCreateRequest();
        request.getData().setPmechanism("");
        request.getData().setReportType("");
        request.getData().setReportNo(mission.getRptNo());
        request.getData().setTaskType("1");
        request.getData().setTaskNo(mission.getTaskId());
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
                        Intent intent2 = new Intent(NetworkSpeedActivity.this, VideoActivity.class);
                        intent2.putExtra("resVideoLineVO", videoResult);
                        startActivity(intent2);
                        NetworkSpeedActivity.this.finish();
                    } else {
                        showShortToast("连接失败");
                        finish();
                    }
                } else {
                    showShortToast(response.getMsg());
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

//    private void seach(){
//        /**
//         * LitePal数据库  查询数据库
//         */
//        try {
//            meetinglist= DataSupport.where("taskNo=? ", taskInfo.getTaskNo()).find(ResVideoLineVO.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(meetinglist.size()>0){
//
//            netWorkSpeedInfo = new NetWorkSpeedInfo();
//            list.clear();
//            thread1=new Thread1();
//            thread1.start();
//            thread2=new Thread2();
//            thread2.start();
//            thread3=new Thread3();
//            thread3.start();
//            resVideoLineVO=meetinglist.get(0);
//            return;
//        }else {
//            finish();
//        }
//    }

    protected void initWidget() {
//        try {
//            speed = NetSpeed.getInstant(this, handler);
//            speed.startCalculateNetSpeed();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        rlall.setVisibility(View.VISIBLE);
        netWorkSpeedInfo = new NetWorkSpeedInfo();
        list.clear();
        thread1=new Thread1();
        thread1.start();
        thread2=new Thread2();
        thread2.start();
        thread3=new Thread3();
        thread3.start();
    }

    public	class Thread1 extends Thread {

        public void run(){
            if(onRun){
                try {
                    imageData = ReadFile.getFileFromUrl(Constants.NetworkSpeedActivity_Url, netWorkSpeedInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public	class Thread2 extends Thread {

        public void run(){
            while (change) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(UPDATE_SPEED);
            }
            if (change) {
                handler.sendEmptyMessage(UPDATE_SPEED);

                netWorkSpeedInfo.hadFinishedBytes = 0;
            }
        }
    }
    public	class Thread3 extends Thread {

        public void run(){
                try {
                    sleep(5500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    handler.sendEmptyMessage(UPDATE_DNOE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
