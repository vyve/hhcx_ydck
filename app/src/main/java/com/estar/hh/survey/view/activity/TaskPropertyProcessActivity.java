package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.GoodLossInfoDto;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.PropLossBaseInfoDto;
import com.estar.hh.survey.entity.entity.SubmitGoodLossMainInfoDto;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.CopyInfoRequest;
import com.estar.hh.survey.entity.request.GoodLossSaveRequest;
import com.estar.hh.survey.entity.request.GoodLossSubmitRequest;
import com.estar.hh.survey.entity.response.CopyInfoResponse;
import com.estar.hh.survey.entity.response.GoodLossSaveResponse;
import com.estar.hh.survey.entity.response.GoodLossSubmitResponse;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.activity.video.VideoActivity;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.DateUtil;
import com.estar.utils.StringUtils;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 财产定损任务流程
 */

public class TaskPropertyProcessActivity extends HuangheBaseActivity implements View.OnClickListener{

    @ViewInject(R.id.task_property_process_back)
    private LinearLayout back;

    @ViewInject(R.id.task_property_process_caseno)
    private TextView caseNo;

    @ViewInject(R.id.task_property_process_taskno)
    private TextView taskNo;

    @ViewInject(R.id.task_property_process_read)
    public TextView read;

    @ViewInject(R.id.task_property_process_contact)
    public TextView contact;

    @ViewInject(R.id.task_property_process_remind)
    private TextView remind;

    @ViewInject(R.id.task_property_process_baseinfo)
    public TextView baseInfo;

    @ViewInject(R.id.task_property_process_detail)
    public TextView detail;

    @ViewInject(R.id.task_property_process_operate)
    private LinearLayout operate;

    @ViewInject(R.id.task_property_process_video)
    private LinearLayout video;

    @ViewInject(R.id.task_property_process_change)
    private LinearLayout change;

    @ViewInject(R.id.task_property_process_save)
    private LinearLayout save;

    @ViewInject(R.id.task_property_process_submit)
    private LinearLayout submit;

    private Mission mission = null;
    public CopyMainInfoDto copyMainInfo = null;//抄单总信息
    public SubmitGoodLossMainInfoDto goodLossMainInfoDto = null;//财损提交vo

    private UserSharePrefrence userSharePrefrence = null;
    private User user = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_property_process_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){
        back.setOnClickListener(this);
        read.setOnClickListener(this);
        contact.setOnClickListener(this);
        remind.setOnClickListener(this);
        baseInfo.setOnClickListener(this);
        detail.setOnClickListener(this);
        change.setOnClickListener(this);
        save.setOnClickListener(this);
        submit.setOnClickListener(this);
        video.setOnClickListener(this);
    }

    private void initData(){

        userSharePrefrence = new UserSharePrefrence(this);
        user = userSharePrefrence.getUserEntity();

        mission = (Mission) getIntent().getSerializableExtra("mission");
        if (mission != null){
            caseNo.setText(mission.getRptNo());
            taskNo.setText(mission.getTaskId());
            if (!StringUtils.isEmpty(mission.getRptNo())){
                getCopyInfo(mission.getRptNo());
            }

            //任务状态为G 表示案件已成功提交至理赔平台
            if (!StringUtils.isEmpty(mission.getTaskStatus()) && mission.getTaskStatus().compareTo("G") >= 0){
                operate.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
            }
        }

        /**
         * 查找数据库中保存的任务提交实体
         */
        goodLossMainInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").findFirst(SubmitGoodLossMainInfoDto.class);

        if (goodLossMainInfoDto == null){
            goodLossMainInfoDto = new SubmitGoodLossMainInfoDto();
            goodLossMainInfoDto.setTaskNo(mission.getTaskId());
            goodLossMainInfoDto.setReportNo(mission.getRptNo());
            goodLossMainInfoDto.setUserName(user.getEmpCde());
            goodLossMainInfoDto.setUserIdNo(user.getUserIdNo());
        }

        initProcess(goodLossMainInfoDto);

    }

    /**
     * 流程图状态初始化
     *
     * @param goodLossMainInfoDto
     */
    private void initProcess(SubmitGoodLossMainInfoDto goodLossMainInfoDto) {

        if (goodLossMainInfoDto.getReadFlag() == 1) {
            changeProcessStyle(read);
        }

        if (goodLossMainInfoDto.getContactFlag() == 1) {
            changeProcessStyle(contact);
        }

        if (goodLossMainInfoDto.getBaseInfoFlag() == 1) {
            changeProcessStyle(baseInfo);
        }

        if (goodLossMainInfoDto.getGoodlistFlag() == 1) {
            changeProcessStyle(detail);
        }

    }


    /**
     * 获取抄单信息
     * @param rptNo
     */
    private void getCopyInfo(String rptNo){

        final MyProgressDialog dialog = new MyProgressDialog(TaskPropertyProcessActivity.this, "正在获取抄单信息...");

        CopyInfoRequest request = new CopyInfoRequest();
        request.getData().setReportNo(rptNo);
        /**
         * 安全整改
         * add by zhengyg 2018年12月6日16:02:56
         */
        request.getData().setSrvyfaceCde(user.getEmpCde());

        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.COPYINFO, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.COPYINFO, reqMsg);

        LogUtils.i("抄单请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("抄单返回参数为:", "--------------------------------------------\n" + result);
                CopyInfoResponse response = gson.fromJson(result, CopyInfoResponse.class);
                if (response.getSuccess()){
                    copyMainInfo = response.getObj();
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("抄单请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("抄单请求取消为:", "--------------------------------------------\n" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.task_property_process_back:{
//                saveSubmitMainVO();
                finish();
            }break;
            case R.id.task_property_process_read:{

                if (copyMainInfo == null){
                    showShortToast("抄单信息获取失败，请重新获取");
                    return;
                }

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent = new Intent(TaskPropertyProcessActivity.this, TaskReadActivity.class);
                intent.putExtra("copyMainInfo", copyMainInfo);
                intent.putExtra("mission", mission);
                intent.putExtra("taskType", Constants.GOODLOSS_TYPE);
                startActivity(intent);

            }break;
            case R.id.task_property_process_contact:{

                if (copyMainInfo == null){
                    showShortToast("抄单信息获取失败，请重新获取");
                    return;
                }

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent = new Intent(TaskPropertyProcessActivity.this, ContactCustomerActivity.class);
                intent.putExtra("copyMainInfo", copyMainInfo);
                intent.putExtra("mission", mission);
                intent.putExtra("taskType", Constants.GOODLOSS_TYPE);
                startActivity(intent);

            }break;
            case R.id.task_property_process_remind:{

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent = new Intent(this, LeaveMessageActivity.class);
                intent.putExtra("mission", mission);
                startActivity(intent);

            }break;
            case R.id.task_property_process_baseinfo:{

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                if (goodLossMainInfoDto.getReadFlag() == 0) {
                    showShortToast("请先阅读任务");
                    return;
                }

                Intent intent = new Intent(TaskPropertyProcessActivity.this, PropertyBaseInfoActivity.class);
                intent.putExtra("copyMainInfo", copyMainInfo);
                intent.putExtra("mission", mission);
                startActivity(intent);

            }break;
            case R.id.task_property_process_detail:{

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                if (goodLossMainInfoDto.getReadFlag() == 0) {
                    showShortToast("请先阅读任务");
                    return;
                }

                Intent intent = new Intent(TaskPropertyProcessActivity.this, PropertyListActivity.class);
                intent.putExtra("copyMainInfo", copyMainInfo);
                intent.putExtra("mission", mission);
                startActivity(intent);

            }break;
            case R.id.task_property_process_change:{

            }break;
            case R.id.task_property_process_save:{

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                //任务状态为E表示案件已暂存
                if (!StringUtils.isEmpty(mission.getTaskStatus()) && mission.getTaskStatus().compareTo("E") >= 0){
                    showShortToast("该案件已暂存");
                    return;
                }

                if (goodLossMainInfoDto == null){
                    showShortToast("任务信息为空, 请先完成任务");
                    return;
                }

                if (!setSubmitMainVO()){
                    return;
                }

                goodLossSave();
            }break;
            case R.id.task_property_process_submit:{

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                //任务状态为F表示案件已提交
                if (!StringUtils.isEmpty(mission.getTaskStatus()) && mission.getTaskStatus().compareTo("G") >= 0){
                    showShortToast("该案件已提交");
                    return;
                }

                if (goodLossMainInfoDto == null){
                    showShortToast("任务信息为空, 请先完成任务");
                    return;
                }

                if (!setSubmitMainVO()){
                    return;
                }

                goodLossSubmit();
            }break;
            case R.id.task_property_process_video:{

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent2 = new Intent(TaskPropertyProcessActivity.this, VideoActivity.class);
                intent2.putExtra("mission", mission);
                startActivity(intent2);

            }break;
        }
    }

    /**
     * 财损暂存
     */
    private void goodLossSave(){

        final MyProgressDialog dialog = new MyProgressDialog(this, "任务暂存中...");

        GoodLossSaveRequest request = new GoodLossSaveRequest();
        request.setData(goodLossMainInfoDto);
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.SYNCHPROPLOSS, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.SYNCHPROPLOSS, reqMsg);

        LogUtils.i("任务暂存参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("任务暂存返回参数为:", "--------------------------------------------\n" + result);
                GoodLossSaveResponse response = gson.fromJson(result, GoodLossSaveResponse.class);
                if (response.getSuccess()){
                    showShortToast(response.getMsg());
                    goodLossMainInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
                    goodLossMainInfoDto.save();
                    finish();
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("任务暂存请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("任务暂存请求取消为:", "--------------------------------------------\n" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }

    /**
     * 财损提交
     */
    private void goodLossSubmit(){

        final MyProgressDialog dialog = new MyProgressDialog(this, "任务提交中...");

        GoodLossSubmitRequest request = new GoodLossSubmitRequest();
        request.setData(goodLossMainInfoDto);
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.SUBMITPROPLOSS, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.SUBMITPROPLOSS, reqMsg);

        LogUtils.i("任务提交请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("任务提交返回参数为:", "--------------------------------------------\n" + result);
                GoodLossSubmitResponse response = gson.fromJson(result, GoodLossSubmitResponse.class);
                if (response.getSuccess()){
                    showShortToast(response.getMsg());
                    goodLossMainInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
                    goodLossMainInfoDto.save();
                    finish();
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("任务提交请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("任务提交请求取消为:", "--------------------------------------------\n" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }


    /**
     * 封装提交总vo
     * @return
     */
    private boolean setSubmitMainVO(){

        if (goodLossMainInfoDto.getReadFlag() == 0){
            showShortToast("阅读任务未完成");
            return false;
        }

        if (goodLossMainInfoDto.getBaseInfoFlag() == 0){
            showShortToast("基本信息任务未完成");
            return false;
        }

        if (goodLossMainInfoDto.getGoodlistFlag() == 0){
            showShortToast("物品明细任务未完成");
            return false;
        }

        PropLossBaseInfoDto propLossBaseInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").findFirst(PropLossBaseInfoDto.class);

        if (propLossBaseInfoDto == null){
            showShortToast("请录入基本信息");
            return false;
        }

        List<GoodLossInfoDto> goodLossInfoDtos =DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").find(GoodLossInfoDto.class);

        if (goodLossInfoDtos == null || goodLossInfoDtos.size() == 0){
            showShortToast("请录入物损信息");
            return false;
        }

        goodLossMainInfoDto.setPropLossBaseInfoDto(propLossBaseInfoDto);
        goodLossMainInfoDto.setListGoodLossInfoDto(goodLossInfoDtos);

        return true;

    }

    /**
     * 保存提交总vo
     * @return
     */
    private void saveSubmitMainVO(){

        PropLossBaseInfoDto propLossBaseInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").findFirst(PropLossBaseInfoDto.class);

        if (propLossBaseInfoDto != null){
            goodLossMainInfoDto.setPropLossBaseInfoDto(propLossBaseInfoDto);
        }

        List<GoodLossInfoDto> goodLossInfoDtos =DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").find(GoodLossInfoDto.class);

        if (goodLossInfoDtos != null){
            goodLossMainInfoDto.setListGoodLossInfoDto(goodLossInfoDtos);
        }

        goodLossMainInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
        goodLossMainInfoDto.save();

    }


    /**
     * 改变流程节点样式
     * @param textView
     */
    public void changeProcessStyle(TextView textView){
        textView.setBackgroundResource(R.drawable.lancardview);
        textView.setTextColor(0xffffffff);
    }

    /**
     * 改变流程节点样式(变白还原)
     * @param textView
     */
    public void returnProcessStyle(TextView textView){
        textView.setBackgroundResource(R.drawable.cardview);
        textView.setTextColor(0xff919191);
    }


//    @Override
//    protected void onDestroy() {
//
//        //保存信息
//        saveSubmitMainVO();
//
//        super.onDestroy();
//    }

    @Override
    protected void onStop() {

        //保存信息
        saveSubmitMainVO();

        super.onStop();
    }
}
