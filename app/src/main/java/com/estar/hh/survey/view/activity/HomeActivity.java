package com.estar.hh.survey.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estar.hh.survey.BuildConfig;
import com.estar.hh.survey.MyApplication;
import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.HomeMissionAdapter;
import com.estar.hh.survey.adapter.MissionAdapter;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.MissionListRequest;
import com.estar.hh.survey.entity.response.MissionListResponse;
import com.estar.hh.survey.entity.response.VersionVO;
import com.estar.hh.survey.utils.ActivityManagerUtils;
import com.estar.hh.survey.utils.Download;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.ListUtils;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.DateUtil;
import com.google.gson.Gson;
//import com.pgyersdk.crash.PgyCrashManager;
//import com.pgyersdk.javabean.AppBean;
//import com.pgyersdk.update.PgyUpdateManager;
//import com.pgyersdk.update.UpdateManagerListener;
import com.yongchun.library.view.ImageSelectorActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/27 0027.'
 * 主页
 */

public class HomeActivity extends HuangheBaseActivity implements View.OnClickListener {

    @ViewInject(R.id.home_list)
    private ListView homeList;

    private ImageView setting;//设置按钮
    private ImageView img;//首页头像
    private TextView name;//姓名
    private TextView score;//评分
    private TextView orgname;//机构名
    private RadioGroup switchs;
    private RadioButton switchAll;//总量
    private RadioButton switchMonth;//月量
    private TextView detailInfo;//详细信息
    private LinearLayout notifyLayout;//通知栏
    private TextView notifyText;//通知栏提示
    private LinearLayout sign;//签到
    private LinearLayout mission;//我的任务
    private LinearLayout caseFlow;//案件跟踪
    private LinearLayout noteBook;//记事本
    private LinearLayout knowLege;//查勘常识
    private LinearLayout watingLayout;//待办任务栏
    private ImageView missionRefresh;//待办任务栏刷新

    private ListView missionList;//任务列表
    private HomeMissionAdapter adapter;//代办任务适配器
    private MissionAdapter missionAdapter;//代办任务以新任务替换适配器

    private LinearLayout homeContent;

    private List<Object> homeMissions = new ArrayList<>();
    private List<Mission> missions = new ArrayList<>();

    private User user;

    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        x.view().inject(this);

        ActivityManagerUtils.getInstance().addActivity(HomeActivity.this);//将MainActivity添加到activity管理工具中方便进行管理

        initView();
        initData();
        initVersion();
//        pwdChange();
    }

//    private void pwdChange() {
//        if (user.getPwdState().equals("-1")) {
//            new AlertDialog.Builder(HomeActivity.this, AlertDialog.THEME_HOLO_LIGHT)
//                    .setTitle("提示信息")
//                    .setMessage(user.getPwdTip()+"")
//                    .setCancelable(false)
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            startActivity(new Intent(HomeActivity.this, ModifyPasswordActivity.class));
//                            finish();
//                        }
//                    }).show();;
//        }else if (user.getPwdState().equals("1")) {
//            new AlertDialog.Builder(HomeActivity.this, AlertDialog.THEME_HOLO_LIGHT)
//                    .setTitle("提示信息")
//                    .setMessage(user.getPwdTip()+"")
//                    .setCancelable(false)
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }).show();;
//        }
//    }

    private void initView() {
        UserSharePrefrence userSharePrefrence = new UserSharePrefrence(this);
        userSharePrefrence.getUserEntity().getRealName();
        homeContent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.home_activity_content, null);
        setting = homeContent.findViewById(R.id.home_setting);
        img = homeContent.findViewById(R.id.home_head_img);
        name = homeContent.findViewById(R.id.home_name);
        score = homeContent.findViewById(R.id.home_score);
        orgname = homeContent.findViewById(R.id.home_orgname);
        switchs = homeContent.findViewById(R.id.home_switch);
        switchAll = homeContent.findViewById(R.id.home_switch_all);
        switchMonth = homeContent.findViewById(R.id.home_switch_month);
        detailInfo = homeContent.findViewById(R.id.home_detail_info);
        notifyLayout = homeContent.findViewById(R.id.home_notify_layout);
        notifyText = homeContent.findViewById(R.id.home_notify_text);
        sign = homeContent.findViewById(R.id.home_sign);
        mission = homeContent.findViewById(R.id.home_mission);
        caseFlow = homeContent.findViewById(R.id.home_caseflow);
        knowLege = homeContent.findViewById(R.id.home_knowlege);
        noteBook = homeContent.findViewById(R.id.home_notebook);
        watingLayout = homeContent.findViewById(R.id.home_waiting_layout);
        missionRefresh = homeContent.findViewById(R.id.home_waiting_layout_refresh);
        missionList = homeContent.findViewById(R.id.home_mission_list);

        setting.setOnClickListener(this);
        img.setOnClickListener(this);
        switchAll.setOnClickListener(this);
        switchMonth.setOnClickListener(this);
        notifyLayout.setOnClickListener(this);
        sign.setOnClickListener(this);
        mission.setOnClickListener(this);
        caseFlow.setOnClickListener(this);
        knowLege.setOnClickListener(this);
        noteBook.setOnClickListener(this);
        watingLayout.setOnClickListener(this);
        missionRefresh.setOnClickListener(this);

        homeList.addHeaderView(homeContent);
        homeList.setAdapter(null);

    }

    private void initData() {

        switchAll.performClick();

        UserSharePrefrence userSharePrefrence = new UserSharePrefrence(this);
        user = userSharePrefrence.getUserEntity();
        if (user != null) {
            Glide.with(this)
                    .load(Constants.BASE_IP + user.getHeadUrl())
                    .error(R.drawable.logo)
                    .centerCrop()
                    .into(img);
            name.setText(user.getRealName());
            score.setText(user.getScore());
            orgname.setText(user.getOrgName());

            //业务单数
            detailInfo.setText("查勘" + user.getSumSurvyTaskCount() + "单  定损" + user.getSumLossTaskCount() + "单");
            switchs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group,  int checkedId) {
                    if (checkedId == R.id.home_switch_all) {
                        detailInfo.setText("查勘" + user.getSumSurvyTaskCount() + "单  定损" + user.getSumLossTaskCount() + "单");
                    } else if (checkedId == R.id.home_switch_month) {
                        detailInfo.setText("查勘" + user.getMonthSurvyTaskCount() + "单  定损" + user.getMonthLossTaskCount() + "单");
                    }
                }
            });

        }

        homeMissions.add(new Object());
        homeMissions.add(new Object());
        homeMissions.add(new Object());
        homeMissions.add(new Object());
        homeMissions.add(new Object());
        adapter = new HomeMissionAdapter(this, homeMissions);
        missionAdapter = new MissionAdapter(this, missions, 1);
        missionList.setAdapter(missionAdapter);
        ListUtils.setListViewHeightBasedOnChildren(missionList);

    }

    @Override
    protected void onResume() {
        super.onResume();
        missionSearch();

        {

            UserSharePrefrence userSharePrefrence = new UserSharePrefrence(this);
            user = userSharePrefrence.getUserEntity();

            Glide.with(this)
                    .load(Constants.BASE_IP + user.getHeadUrl())
                    .error(R.drawable.logo)
                    .centerCrop()
                    .into(img);
            name.setText(user.getRealName());
            score.setText(user.getScore());
            orgname.setText(user.getOrgName());

    }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_setting: {//设置
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
            }
            break;
            case R.id.home_head_img: {//个人头像
//                /**
//                 * @param context
//                 * @param maxselectNum 最大照片选择数
//                 * @param mode 模式 1-多重模式  2-单个模式
//                 * @param isShow 是否展示
//                 * @param enablePreview 是否支持预览图
//                 * @param enableCrop 是否支持裁剪
//                 */
//                ImageSelectorActivity.start(HomeActivity.this, 1, 2, true, true, true);
            }
            break;
            case R.id.home_switch_all: {//总单量
                switchAll.setTextColor(0xffffffff);
                switchMonth.setTextColor(0xff494949);
            }
            break;
            case R.id.home_switch_month: {//月单量
                switchAll.setTextColor(0xff494949);
                switchMonth.setTextColor(0xffffffff);
            }
            break;
            case R.id.home_notify_layout: {//通知界面
                startActivity(new Intent(HomeActivity.this, MessageNotifyActivity.class));
            }
            break;
            case R.id.home_sign: {//上班签到
                startActivity(new Intent(HomeActivity.this, SignActivity.class));
            }
            break;
            case R.id.home_mission: {//我的任务
                startActivity(new Intent(HomeActivity.this, MissionListActivity.class));
            }
            break;
            case R.id.home_caseflow: {//案件跟踪
                startActivity(new Intent(HomeActivity.this, MissionFollowListActivity.class));
            }
            break;
            case R.id.home_knowlege: {//查勘常识
                startActivity(new Intent(HomeActivity.this, KnowLegeActivity.class));
            }
            break;
            case R.id.home_notebook: {//记事本
                startActivity(new Intent(HomeActivity.this, NoteBookActivity.class));
            }
            break;
            case R.id.home_waiting_layout: {//待办任务
//                startActivity(new Intent(HomeActivity.this, MissionListActivity.class));
            }
            break;
            case R.id.home_waiting_layout_refresh: {//待办任务刷新
                missionRefresh();
            }
            break;
            default:
                break;
        }
    }

    private void missionSearch() {

//        final MyProgressDialog dialog = new MyProgressDialog(this, "正在获取任务...");

        MissionListRequest request = new MissionListRequest();
        request.getData().setDisEndTime(DateUtil.getTime(new Date()));
        request.getData().setDisStartTime(DateUtil.getTime(new Date()));
        request.getData().setSrvyfaceCde(user.getEmpCde());
        request.getData().setSearchType("0");//全部新任务
        request.getData().setSearchStatus("1");//新任务

        final Gson gson = new Gson();
        final String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.MISSIONLIST, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.MISSIONLIST, reqMsg);

        LogUtils.i("任务列表请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("任务列表返回参数为:", "--------------------------------------------\n" + result);
                MissionListResponse response = gson.fromJson(result, MissionListResponse.class);
                if (response.getSuccess()) {
                    if (response.getObj() != null && response.getObj().size() > 0) {
                        missions.clear();
                        missions.addAll(response.getObj());
                        missionAdapter.notifyDataSetChanged();
                        ListUtils.setListViewHeightBasedOnChildren(missionList);
                    } else {
                        missions.clear();
                        missionAdapter.notifyDataSetChanged();
                        ListUtils.setListViewHeightBasedOnChildren(missionList);
                    }
                } else {
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("任务列表请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                ex.printStackTrace();
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("任务列表请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                cex.printStackTrace();
                showShortToast(cex.getLocalizedMessage());
            }

            @Override
            public void onFinished() {
//                dialog.stopDialog();
            }
        });

    }

    private void missionRefresh() {

        final MyProgressDialog dialog = new MyProgressDialog(this, "正在刷新任务...");

        MissionListRequest request = new MissionListRequest();
        request.getData().setDisEndTime(DateUtil.getTime(new Date()));
        request.getData().setDisStartTime(DateUtil.getTime(new Date()));
        request.getData().setSrvyfaceCde(user.getEmpCde());
        request.getData().setSearchType("0");//全部新任务
        request.getData().setSearchStatus("1");//新任务

        final Gson gson = new Gson();
        final String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.MISSIONLIST, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.MISSIONLIST, reqMsg);

        LogUtils.i("任务列表请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("任务列表返回参数为:", "--------------------------------------------\n" + result);
                MissionListResponse response = gson.fromJson(result, MissionListResponse.class);
                if (response.getSuccess()) {
                    if (response.getObj() != null && response.getObj().size() > 0) {
                        missions.clear();
                        missions.addAll(response.getObj());
                        missionAdapter.notifyDataSetChanged();
                        ListUtils.setListViewHeightBasedOnChildren(missionList);
                    } else {
                        missions.clear();
                        missionAdapter.notifyDataSetChanged();
                        ListUtils.setListViewHeightBasedOnChildren(missionList);
                    }
                } else {
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("任务列表请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                ex.printStackTrace();
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("任务列表请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                cex.printStackTrace();
                showShortToast(cex.getLocalizedMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }

    /**
     * 版本更新检测
     */
    private MyProgressDialog dialog;
    private void initVersion() {
//        PgyUpdateManager.register(HomeActivity.this, "com.estar.hh.survey", new UpdateManagerListener() {
//
//            @Override
//            public void onNoUpdateAvailable() {
//
//            }
//
//            @Override
//            public void onUpdateAvailable(String s) {
//                // 将新版本信息封装到AppBean中
//                final AppBean appBean = getAppBeanFromString(s);
//                new AlertDialog.Builder(HomeActivity.this, AlertDialog.THEME_HOLO_LIGHT)
//                        .setTitle("更新提示")
//                        .setMessage("发现新版本" + appBean.getVersionName() + " 是否更新？")
//                        .setCancelable(false)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                startDownloadTask(
//                                        HomeActivity.this,
//                                        appBean.getDownloadURL());
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        }).show();
//            }
//        });

        dialog = new MyProgressDialog(this, "版本更新...");
        Map<String, String> params2 = new HashMap<>();
        params2.put("appName", "黄河查勘");
        params2.put("systemType", "1");
        final String request = new Gson().toJson(params2);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.VERSION_UPDATE, request);
        LogUtils.i("版本更新请求参数为:", "-------------------------------------------\n" + request);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("版本更新返回参数为:", "--------------------------------------------\n" + result);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                    boolean code = obj.optBoolean("success");
                    if (code) {
                        VersionVO codeVO = new Gson().fromJson(result, VersionVO.class);
                        String newVersionCode =  codeVO.getObj().getCurrVersion();
                        String  currentVersion = MyApplication.getVersionName(HomeActivity.this);
                        LogUtils.i("============", newVersionCode + "");
                        LogUtils.i("============", currentVersion + "");
                        if (!newVersionCode.equals(currentVersion)) {
                            showDialogUpdate(codeVO.getObj().getDownloadSource());
                        }
                    } else {
                        showShortToast(obj.optString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("版本更新错误参数为:", "--------------------------------------------\n" + ex.getMessage() + "\n" + ex.getCause());
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("版本更新取消参数为:", "--------------------------------------------\n" + cex.getMessage() + "\n" + cex.getCause());

            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }

    private void showDialogUpdate(final String introductions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("版本升级").
        setMessage("发现新版本！请及时更新").
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showload(introductions);
                    }
                }).
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                    }
                });

        // 生产对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);//点击返回不消失
        // 显示对话框
        alertDialog.show();
    }
    private ProgressBar progressbar1;
    private int pregress_cont;
    public void showload(final String filePath) {
        //开始下载文件
        new Thread() {
            public void run() {
                Download.loadFile(filePath, mHandler);
//                Download.loadFile("http://1.190.246.161:8091/unzip/cxcb/app-cxcb.apk", mHandler);
            }
        }.start();
        LinearLayout layout = (LinearLayout) LayoutInflater.from(HomeActivity.this).inflate(R.layout.loaddialog, null);
        progressbar1 = layout.findViewById(R.id.progressbar1);
        AlertDialog.Builder my_builder = new AlertDialog.Builder(HomeActivity.this,AlertDialog.THEME_HOLO_LIGHT);
        my_builder.setMessage("下载进度");
        AlertDialog dialog2 = my_builder.create();
        dialog2.setCancelable(false);
        dialog2.setView(layout);
        dialog2.show();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog2.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
        p.width = d.getWidth() * 1;    //宽度设置为屏幕的0.5
        dialog2.getWindow().setAttributes(p);     //设置生效
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                case 11:
                    // 正在下载   设置进度条位置
                    pregress_cont = msg.arg1;
                    progressbar1.setProgress(pregress_cont);
                    break;
                case 200:
//              通知安装下载的apk
                    File file = new File(Environment.getExternalStorageDirectory(),
                            "hhcx.apk");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        Uri contentUri = FileProvider.getUriForFile(MainActitivty.this, FileUtils.getApplicationId(MainActitivty.this) + ".provider", file);
                        Uri contentUri = FileProvider.getUriForFile(HomeActivity.this, BuildConfig.APPLICATION_ID+".provider",file);

                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                        //兼容8.0
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
//                            if (!hasInstallPermission) {
//                                startInstallPermissionSettingActivity();
//                                return;
//                            }
//                        }

                    } else {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    }
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//处理图片选择返回
            //拿到返回的图片地址清单
            List<String> images = data.getStringArrayListExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            if (images != null && images.size() > 0) {
                Glide.with(this).load(images.get(0)).into(img);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler2.sendEmptyMessageDelayed(0, 3000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            ActivityManagerUtils.getInstance().exit();
        }
    }

    Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //PgyCrashManager.unregister();//蒲公英解除注册
    }
}
