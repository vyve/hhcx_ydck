package com.estar.hh.survey.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.CopyInfoRequest;
import com.estar.hh.survey.entity.request.TaskReadRequest;
import com.estar.hh.survey.entity.response.CopyInfoResponse;
import com.estar.hh.survey.entity.response.TaskReadResponse;
import com.estar.hh.survey.utils.ActivityManagerUtils;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.HttpUtils;
import com.estar.hh.survey.utils.LogTools;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.hh.survey.view.fragment.MissionFragment;
import com.estar.hh.survey.view.fragment.TaskCaseFragment;
import com.estar.hh.survey.view.fragment.TaskHistoryFragment;
import com.estar.hh.survey.view.fragment.TaskPlyFragment;
import com.estar.hh.survey.view.fragment.TaskRiskkindFragment;
import com.estar.utils.DateUtil;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.google.gson.Gson;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 阅读任务
 */

public class TaskReadActivity extends FragmentActivity {

    @ViewInject(R.id.task_read_back)
    private LinearLayout back;

    @ViewInject(R.id.task_read_indicator)
    private TabPageIndicator indicator;

    @ViewInject(R.id.task_read_indicator_underline)
    private UnderlinePageIndicator underline;

    @ViewInject(R.id.task_read_pager)
    private ViewPager pager;

    /**
     * Tab标题
     */
    private static final String[] INDICATORTITLE = new String[] { "报案信息", "保单信息", "险别信息" ,"历史赔案"};

    private CopyMainInfoDto copyMainInfo = null;
    private Mission mission = null;

    /**
     * 安全整改
     * add by zhengyg 2018年12月6日16:02:56
     */
    private UserSharePrefrence userSharePrefrence = null;
    private User user = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_read_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ViewPager的adapter
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        underline.setViewPager(pager);
        underline.setFades(false);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(underline);


        //如果我们要对ViewPager设置监听，用indicator设置就行了
        underline.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initData(){

        /**
         * 安全整改
         * add by zhengyg 2018年12月6日16:02:56
         */
        userSharePrefrence = new UserSharePrefrence(this);
        user = userSharePrefrence.getUserEntity();

        copyMainInfo = (CopyMainInfoDto) getIntent().getSerializableExtra("copyMainInfo");
        mission = (Mission) getIntent().getSerializableExtra("mission");

        if (mission == null){
            ToastUtils.showShort(TaskReadActivity.this, "未找到报案号，阅读任务请求失败");
            return;
        }

        taskRead(mission.getRptNo(), mission.getTaskId());

    }


    /**
     * 阅读任务接口
     * @param rptNo
     * @param taskNo
     */
    private void taskRead(String rptNo, String taskNo){

        TaskReadRequest request = new TaskReadRequest();
        request.getData().setReportNo(rptNo);
        request.getData().setTaskNo(taskNo);
        request.getData().setReadingTaskTime(DateUtil.getTime_YMDMS(new Date()));
        /**
        * 安全整改
        * add by zhengyg 2018年12月6日16:02:56
        */
        request.getData().setSrvyfaceCde(user.getEmpCde());
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.TASKREAD, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.TASKREAD, reqMsg);

        Log.i("阅读任务请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                Log.i("阅读任务返回参数为:", "--------------------------------------------\n" + result);
                TaskReadResponse response = gson.fromJson(result, TaskReadResponse.class);
                ToastUtils.showShort(TaskReadActivity.this, response.getMsg());
                changeProcessColor();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("阅读任务请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                ToastUtils.showShort(TaskReadActivity.this, ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i("阅读任务请求取消为:", "--------------------------------------------\n" + cex.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });

    }


    /**
     * ViewPager适配器
     * @author len
     *
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {

        private Fragment caseInfoFragment;
        private Fragment plyInfoFragment;
        private Fragment riskInfoFragment;
        private Fragment historyFragment;

        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
            caseInfoFragment = new TaskCaseFragment();
            plyInfoFragment = new TaskPlyFragment();
            riskInfoFragment = new TaskRiskkindFragment();
            historyFragment = new TaskHistoryFragment();
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            switch (position){
                case 0:{
                    fragment = caseInfoFragment;
                }break;
                case 1:{
                    fragment = plyInfoFragment;
                }break;
                case 2:{
                    fragment = riskInfoFragment;
                }break;
                case 3:{
                    fragment = historyFragment;
                }break;
                default:break;
            }

            Bundle args = new Bundle();
            args.putSerializable("copyMainInfo", copyMainInfo);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return INDICATORTITLE[position % INDICATORTITLE.length];
        }

        @Override
        public int getCount() {
            return INDICATORTITLE.length;
        }
    }

    /**
     * 更改流程图颜色
     */
    private void changeProcessColor(){

        String taskType = getIntent().getStringExtra("taskType");
        if (StringUtils.isEmpty(taskType)){
            ToastUtils.showShort(this, "未知的任务类型");
            return;
        }

        switch (taskType){
            case Constants.SURVEY_TYPE:{
                Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
                ((TaskProcessActivity) activity).changeProcessStyle(((TaskProcessActivity)activity).read);
                ((TaskProcessActivity) activity).survMainInfoDto.setReadFlag(1);
                ((TaskProcessActivity) activity).survMainInfoDto.save();
                LogTools.writeText("现场查勘修改流程图颜色以及状态："+((TaskProcessActivity) activity).survMainInfoDto.toString());
            }break;
            case Constants.CARLOSS_TYPE:{
                Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskCarProcessActivity.class);
                ((TaskCarProcessActivity) activity).changeProcessStyle(((TaskCarProcessActivity)activity).read);
                ((TaskCarProcessActivity) activity).carLossMainInfoDto.setReadFlag(1);
                ((TaskCarProcessActivity) activity).carLossMainInfoDto.save();
            }break;
            case Constants.GOODLOSS_TYPE:{
                Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskPropertyProcessActivity.class);
                ((TaskPropertyProcessActivity) activity).changeProcessStyle(((TaskPropertyProcessActivity)activity).read);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.setReadFlag(1);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.save();
            }break;
            default:break;
        }
    }

}
