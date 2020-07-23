package com.estar.hh.survey.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.CarLossBaseInfoDto;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.CopyRiskDto;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.SubmitCarLossMainInfoDto;
import com.estar.hh.survey.entity.entity.SurveyAlreadySubmitDto;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.CarLossSaveRequest;
import com.estar.hh.survey.entity.request.CarLossSubmitRequest;
import com.estar.hh.survey.entity.request.CopyInfoRequest;
import com.estar.hh.survey.entity.request.JYLossnoRequest;
import com.estar.hh.survey.entity.response.CarLossSaveResponse;
import com.estar.hh.survey.entity.response.CarLossSubmitResponse;
import com.estar.hh.survey.entity.response.CopyInfoResponse;
import com.estar.hh.survey.entity.response.JYLossnoResponse;
import com.estar.hh.survey.entity.vo.CarLossInitJyReqVO;
import com.estar.hh.survey.entity.vo.LossRiskVO;
import com.estar.hh.survey.utils.FileUtils;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.activity.video.VideoActivity;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.DateUtil;
import com.estar.utils.MessageDialog;
import com.estar.utils.StringUtils;
import com.estar.utils.SystemTool;
import com.estar.utils.ToastUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jy.common.client.response.EvalResponse;
import com.jy.common.model.EvalLossInfo;
import com.jy.common.model.InsuranceItem;

import org.litepal.crud.DataSupport;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 车辆定损任务流程
 */

public class TaskCarProcessActivity extends HuangheBaseActivity implements View.OnClickListener{

    @ViewInject(R.id.task_car_process_back)
    private LinearLayout back;

    @ViewInject(R.id.task_car_process_caseno)
    private TextView caseNo;

    @ViewInject(R.id.task_car_process_taskno)
    private TextView taskNo;

    @ViewInject(R.id.task_car_process_read)
    public TextView read;

    @ViewInject(R.id.task_car_process_contact)
    public TextView contact;

    @ViewInject(R.id.task_car_process_remind)
    private TextView remind;

    @ViewInject(R.id.task_car_process_baseinfo)
    public TextView baseInfo;

    @ViewInject(R.id.task_car_process_carloss)
    public TextView carLoss;

    @ViewInject(R.id.task_car_process_operate)
    private LinearLayout operate;

    @ViewInject(R.id.task_car_process_video)
    private LinearLayout video;

    @ViewInject(R.id.task_car_process_change)
    private LinearLayout change;

    @ViewInject(R.id.task_car_process_save)
    private LinearLayout save;

    @ViewInject(R.id.task_car_process_submit)
    private LinearLayout submit;

    private Mission mission = null;
    private String reportNo;
    private String taskId;
    private CopyMainInfoDto copyMainInfo = null;//抄单总信息
    private SurveyAlreadySubmitDto surveyAlreadySubmitDto = null;//查勘已提交信息
    public SubmitCarLossMainInfoDto carLossMainInfoDto = null;//车损提交总vo

    UserSharePrefrence userSharePrefrence = null;
    User user = null;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_car_process_activity);
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
        carLoss.setOnClickListener(this);
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

            reportNo = mission.getRptNo();
            taskId = mission.getTaskId();

            /**
             * 查找数据库中保存的任务提交实体
             */
            carLossMainInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", reportNo, taskId)
                    .order("createTime desc").findFirst(SubmitCarLossMainInfoDto.class);

            if (carLossMainInfoDto == null){
                carLossMainInfoDto = new SubmitCarLossMainInfoDto();
                carLossMainInfoDto.setTaskNo(mission.getTaskId());
                carLossMainInfoDto.setReportNo(mission.getRptNo());
                carLossMainInfoDto.setUserName(user.getEmpCde());
                carLossMainInfoDto.setUserIdNo(user.getUserIdNo());
            }

            initProcess(carLossMainInfoDto);

            //任务状态为G 表示案件已成功提交至理赔平台
            if (!StringUtils.isEmpty(mission.getTaskStatus()) && mission.getTaskStatus().compareTo("G") >= 0){
                operate.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 流程图状态初始化
     *
     * @param carLossMainInfoDto
     */
    private void initProcess(SubmitCarLossMainInfoDto carLossMainInfoDto) {

        if (carLossMainInfoDto.getReadFlag() == 1) {
            changeProcessStyle(read);
        }

        if (carLossMainInfoDto.getContactFlag() == 1) {
            changeProcessStyle(contact);
        }

        if (carLossMainInfoDto.getBaseInfoFlag() == 1) {
            changeProcessStyle(baseInfo);
        }

        if (carLossMainInfoDto.getLosscheckFlag() == 1) {
            changeProcessStyle(carLoss);
        }

    }

    /**
     * 获取抄单信息
     * @param rptNo
     */
    private void getCopyInfo(String rptNo){

        final MyProgressDialog dialog = new MyProgressDialog(TaskCarProcessActivity.this, "正在获取抄单信息...");

        CopyInfoRequest request = new CopyInfoRequest();
        request.getData().setReportNo(rptNo);

        /**
         * 安全整改
         * add by zhengyg 2018年12月6日16:02:56
         */
        request.getData().setSrvyfaceCde(user.getEmpCde());

        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

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
                inputLog("XChrist",result);
                CopyInfoResponse response = gson.fromJson(result, CopyInfoResponse.class);
                if (response.getSuccess()){
                    copyMainInfo = response.getObj();
                    surveyAlreadySubmitDto = copyMainInfo.getSurveyAlreadySubmit();
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
            case R.id.task_car_process_back:{
//                saveSubmitMainVO();
                finish();
            }break;
            case R.id.task_car_process_read:{

                if (copyMainInfo == null){
                    showShortToast("抄单信息获取失败，请重新获取");
                    return;
                }

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent = new Intent(this, TaskReadActivity.class);
                intent.putExtra("copyMainInfo", copyMainInfo);
                intent.putExtra("mission", mission);
                intent.putExtra("taskType", Constants.CARLOSS_TYPE);
                startActivity(intent);

            }break;
            case R.id.task_car_process_contact:{

                if (copyMainInfo == null){
                    showShortToast("抄单信息获取失败，请重新获取");
                    return;
                }

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent = new Intent(this, ContactCustomerActivity.class);
                intent.putExtra("copyMainInfo", copyMainInfo);
                intent.putExtra("mission", mission);
                intent.putExtra("taskType", Constants.CARLOSS_TYPE);
                startActivity(intent);

            }break;
            case R.id.task_car_process_remind:{

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent = new Intent(this, LeaveMessageActivity.class);
                intent.putExtra("mission", mission);
                startActivity(intent);

            }break;
            case R.id.task_car_process_baseinfo:{

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                if (copyMainInfo == null){
                    showShortToast("抄单信息获取失败，请尝试刷新");
                    return;
                }

                if (carLossMainInfoDto.getReadFlag() == 0) {
                    showShortToast("请先阅读任务");
                    return;
                }

                Intent intent = new Intent(this, CarSurveyBaseInfoActivity.class);
                intent.putExtra("mission", mission);
                intent.putExtra("copyMainInfo", copyMainInfo);
                intent.putExtra("surveyAlreadySubmitDto", surveyAlreadySubmitDto);
                startActivity(intent);

            }break;
            case R.id.task_car_process_carloss:{

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                if (copyMainInfo == null){
                    showShortToast("未找到抄单信息");
                    return;
                }

                if (carLossMainInfoDto.getReadFlag() == 0) {
                    showShortToast("请先阅读任务");
                    return;
                }

                getJYLossNo();
//                changeProcessStyle(carLoss);
//                carLossMainInfoDto.setLosscheckFlag(1);
                carLossMainInfoDto.save();

            }break;
            case R.id.task_car_process_change:{

            }break;
            case R.id.task_car_process_save:{

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                //任务状态为E表示案件已暂存
                if (!StringUtils.isEmpty(mission.getTaskStatus()) && mission.getTaskStatus().compareTo("E") >= 0){
                    showShortToast("该案件已暂存");
                    return;
                }

                if (carLossMainInfoDto == null){
                    showShortToast("任务信息为空, 请先完成任务");
                    return;
                }

                if (!setSubmitMainVO()){
                    return;
                }

                carLossSave();
            }break;
            case R.id.task_car_process_submit:{

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                //任务状态为F表示案件已提交
                if (!StringUtils.isEmpty(mission.getTaskStatus()) && mission.getTaskStatus().compareTo("G") >= 0){
                    showShortToast("该案件已提交");
                    return;
                }

                if (carLossMainInfoDto == null){
                    showShortToast("任务信息为空, 请先完成任务");
                    return;
                }

                if (!setSubmitMainVO()){
                    return;
                }

                carLossSubmit();
            }break;
            case R.id.task_car_process_video:{

                if (mission == null){
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent2 = new Intent(TaskCarProcessActivity.this, VideoActivity.class);
                intent2.putExtra("mission", mission);
                startActivity(intent2);

            }break;
        }
    }

    /**
     * 车损暂存
     */
    private void carLossSave(){

        final MyProgressDialog dialog = new MyProgressDialog(TaskCarProcessActivity.this, "任务暂存中...");

        CarLossSaveRequest request = new CarLossSaveRequest();
        request.setData(carLossMainInfoDto);
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.SYNCHCARLOSS, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.SYNCHCARLOSS, reqMsg);

        Log.i("任务暂存参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("任务暂存返回参数为:", "--------------------------------------------\n" + result);
                CarLossSaveResponse response = gson.fromJson(result, CarLossSaveResponse.class);
                if (response.getSuccess()){
                    showShortToast(response.getMsg());
                    carLossMainInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
                    carLossMainInfoDto.save();
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
     * 车损提交
     */
    private void carLossSubmit(){

        final MyProgressDialog dialog = new MyProgressDialog(TaskCarProcessActivity.this, "任务提交中...");

        CarLossSubmitRequest request = new CarLossSubmitRequest();
        request.setData(carLossMainInfoDto);
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.SUBMITCARLOSS, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.SUBMITCARLOSS, reqMsg);

        LogUtils.i("任务提交请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("任务提交返回参数为:", "--------------------------------------------\n" + result);
                CarLossSubmitResponse response = gson.fromJson(result, CarLossSubmitResponse.class);
                if (response.getSuccess()){
                    showShortToast(response.getMsg());
                    carLossMainInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
                    carLossMainInfoDto.save();
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

        if (carLossMainInfoDto.getReadFlag() == 0){
            showShortToast("阅读任务未完成");
            return false;
        }

        if (carLossMainInfoDto.getBaseInfoFlag() == 0){
            showShortToast("基本信息任务未完成");
            return false;
        }

        if (carLossMainInfoDto.getLosscheckFlag() == 0){
            showShortToast("车辆定损任务未完成");
            return false;
        }

        CarLossBaseInfoDto carLossBaseInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").findFirst(CarLossBaseInfoDto.class);

        if (carLossBaseInfoDto == null){
            showShortToast("请完成基本信息填写");
            return false;
        }

        /**
         * 添加基本信息完成检测
         */
        if (checkBaseInfo(carLossBaseInfoDto)){
            carLossMainInfoDto.setCarLossBaseInfoDto(carLossBaseInfoDto);
        }else {
            return false;
        }


        return true;

    }

    /**
     * 保存提交总vo
     * @return
     */
    private void saveSubmitMainVO(){

        CarLossBaseInfoDto carLossBaseInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").findFirst(CarLossBaseInfoDto.class);

        if (carLossBaseInfoDto != null){
            carLossMainInfoDto.setCarLossBaseInfoDto(carLossBaseInfoDto);
        }

        carLossMainInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
        carLossMainInfoDto.save();

    }


    /**
     * 获取精友定损单号
     */
    private void getJYLossNo(){

        final MyProgressDialog dialog = new MyProgressDialog(TaskCarProcessActivity.this, "精友定损单号获取中...");

        JYLossnoRequest request = new JYLossnoRequest();
        request.getData().setReportNo(reportNo);
        request.getData().setTaskNo(taskId);
        /**
         * 安全整改
         * add by zhengyg 2018年12月6日16:02:56
         */
        request.getData().setSrvyfaceCde(user.getEmpCde());
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.JYLOSSNO, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.JYLOSSNO, reqMsg);

        LogUtils.i("精友定损单号请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("精友定损单号返回参数为:", "--------------------------------------------\n" + result);
                JYLossnoResponse response = gson.fromJson(result, JYLossnoResponse.class);
                if (response.getSuccess()){
                    //定损单号获取成功，则进入精友系统
                    jy(response.getObj().getLossItemNumber());
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("精友定损单号请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("精友定损单号请求取消为:", "--------------------------------------------\n" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }


    /**
     * 改变流程节点样式(变蓝)
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


    /**
     * 精友系统调用前检测基本信息是否完成录入
     * @return
     */
    private boolean checkBaseInfo(CarLossBaseInfoDto carLossBaseInfoDto){

        if (carLossBaseInfoDto == null){
            showShortToast("请完成基本信息填写");
            return false;
        }

        if (StringUtils.isEmpty(carLossBaseInfoDto.getLicsnseNo())){
            showShortToast("请填写基本信息-车牌号");
            return false;
        }
        if (StringUtils.isEmpty(carLossBaseInfoDto.getLicsnseNoType())){
            showShortToast("请选择基本信息-号牌种类");
            return false;
        }
        if (StringUtils.isEmpty(carLossBaseInfoDto.getDriverName())){
            showShortToast("请填写基本信息-驾驶人姓名");
            return false;
        }
        if (StringUtils.isEmpty(carLossBaseInfoDto.getIdentifyNumber())){
            showShortToast("请填写基本信息-驾驶人身份证号");
            return false;
        }
        if (StringUtils.isEmpty(carLossBaseInfoDto.getDriverTelNo())){
            showShortToast("请填写基本信息-驾驶人手机号");
            return false;
        }
        if (StringUtils.isEmpty(carLossBaseInfoDto.getDefSite())){
            showShortToast("请填写基本信息-定损地点");
            return false;
        }
        if (StringUtils.isEmpty(carLossBaseInfoDto.getCetainLossType())){
            showShortToast("请填写基本信息-定损方式");
            return false;
        }

        return true;

    }

    /**
     * 调用精友报价系统
     */
    private void jy(String lossItemNumber) {

        try {

            /**
             * 检测定损基本信息是否完成录入
             */
            CarLossBaseInfoDto carLossBaseInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                    .order("createTime desc").findFirst(CarLossBaseInfoDto.class);

            if (!checkBaseInfo(carLossBaseInfoDto)){
                return;
            }

            //保存基本信息定损单号
            carLossBaseInfoDto.setLossItemNo(lossItemNumber);
            carLossBaseInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
            carLossBaseInfoDto.save();

            LogUtils.w("调用精友");
            Intent mIntent = new Intent();
            mIntent.setAction("com.jy.lioneye.huanghe.EVAL");
            mIntent.addCategory("android.intent.category.DEFAULT");
            Bundle bundle = new Bundle();
            EvalLossInfo evalLossInfo = new EvalLossInfo();
            evalLossInfo.setUserCode("jy");
            evalLossInfo.setPassword("jy");
            CarLossInitJyReqVO reqJy = getCarLossInitJyReqVO(carLossBaseInfoDto, lossItemNumber);

            if(reqJy==null){
                ToastUtils.showShort(TaskCarProcessActivity.this, "获取请求精友数据为空！");
                return;
            }


//            if(myApplication.getTaskStatu().equals("已提交理赔")){//判断任务状态
            if("已提交理赔".equals("已提交理赔")){//判断任务状态
                evalLossInfo.setRequestType("001"); //001  定损请求 002  定损退回 003 查看
            }else {
                evalLossInfo.setRequestType(reqJy.getRequestType()); //001  定损请求 002  定损退回  003 查看
            }

            evalLossInfo.setLossNo(reqJy.getLossNo());//损失项目编号 LossNo+CaseNo确定唯一定损单  任务号
            evalLossInfo.setReportCode(reportNo);//报案号
            evalLossInfo.setInsureVehicleCode(reqJy.getInsureVehicleCode());//承保车型编码
            evalLossInfo.setInsureVehicleName(reqJy.getInsureVehicleName());
            evalLossInfo.setComCode(reqJy.getComCode());//定损员所属机构代码
            evalLossInfo.setCompany(reqJy.getCompany());//所属分公司名称
            evalLossInfo.setBranchComCode(reqJy.getBranchComCode());//所属中支代码 如果没有则传分公司代码
            evalLossInfo.setBranchComName(reqJy.getBranchComName());//所属中支名称如果没有则传分公司名称
            evalLossInfo.setHandlerCode(reqJy.getHandlerCode());//定损员代码
            evalLossInfo.setHandlerName(reqJy.getHandlerName());//定损员姓名
            evalLossInfo.setIsSubjectVehicle(reqJy.getIsSubjectVehicle());//是否标的车 1 是0 否
            evalLossInfo.setPlateNo(reqJy.getPlateNo());//车牌号码
            evalLossInfo.setRequestSourceCode("HHBX");// 请求来源代码
            evalLossInfo.setRequestSourceName("黄河财产保险股份有限公司");//请求来源名称
            evalLossInfo.setOperatingTime(DateUtil.getTime_YMDMS(new Date()));//操作时间
            evalLossInfo.setVinNo(reqJy.getVinNo());//VIN码

            evalLossInfo.setDriverName(reqJy.getDriverName());//驾驶员
            evalLossInfo.setMarkColor(reqJy.getMarkColor());//牌照颜色
            evalLossInfo.setEvalTypeCode(reqJy.getEvalTypeCode());//定损方式 01修复定损 02一次性协议定损 03推定全损 04	法院判决
            evalLossInfo.setEnrolDate(DateUtil.getTime(new Date()));//初登日期  2014-09-03
            evalLossInfo.setEngineNo(reqJy.getEngineNo());//发动机号
            evalLossInfo.setRefreshURL(mission.getRefreshURL());
            evalLossInfo.setReturnURL(mission.getReturnURL());
            //险别 列表
            List<InsuranceItem> insuranceItemList = new ArrayList<>();
            List<LossRiskVO> riskList=  reqJy.getRiskList();
            if(riskList!=null&&riskList.size()!=0){
                for (int i=0;i<riskList.size();i++){
                    LossRiskVO lossRiskVO=riskList.get(i);
                    InsuranceItem insuranceItem = new InsuranceItem();
                    insuranceItem.setLossNo(lossRiskVO.getLossNo());
                    insuranceItem.setInsureTermCode(lossRiskVO.getCode());
                    insuranceItem.setInsureTerm(lossRiskVO.getName());
                    insuranceItemList.add(insuranceItem);
                }
            }

            evalLossInfo.setInsuranceItemList(insuranceItemList);//险别信息
            GsonBuilder builder = new GsonBuilder();
            builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
            Gson gson = builder.create();
            String requestData = gson.toJson(evalLossInfo);
            bundle.putString("REQUEST_DATA", requestData);
            LogUtils.w("精友请求参数=" + requestData);

            //测试精友请求数据
            //            FileUtils.getInstance().saveFile(requestData,
            //                    FileUtils.getInstance().carateSubmitJYInfoPath(
            //                            myApplication.getTaskVO().getTaskNo()));


            mIntent.putExtras(bundle);
            startActivityForResult(mIntent, 1);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(TaskCarProcessActivity.this, "调用精友报价系统出错");
            new MessageDialog(TaskCarProcessActivity.this, new MessageDialog.SubmitOnClick() {

                @Override
                public void onSubmitOnClickSure() {
                    final ProgressDialog progressDialog = new ProgressDialog(TaskCarProcessActivity.this);
                    String path = Environment.getExternalStorageDirectory() + "/download/JYversion"; //配置下载路径
                    RequestParams requestParams = new RequestParams(Constants.JY_DOWNLOAD);
                    requestParams.setSaveFilePath(FileUtils.getInstance().getCachePath());
                    x.http().get(requestParams, new Callback.ProgressCallback<File>() {
                        @Override
                        public void onWaiting() {

                        }

                        @Override
                        public void onStarted() {

                        }

                        @Override
                        public void onLoading(long total, long current, boolean isDownloading) {
                            /**
                             * 创建下载进度条
                             */
                            progressDialog.setCancelable(false);//下载提示不可取消
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setMessage("精友下载中...");
                            progressDialog.show();
                            progressDialog.setMax((int) total);
                            progressDialog.setProgress((int) current);
                        }

                        @Override
                        public void onSuccess(final File result) {
                            /**
                             * 下载完成提示安装
                             */
                            new MessageDialog(TaskCarProcessActivity.this, new MessageDialog.SubmitOnClick() {
                                @Override
                                public void onSubmitOnClickSure() {
                                    SystemTool.installApk(TaskCarProcessActivity.this, result);
                                }
                                @Override
                                public void onSubmitOnClickCancel() {
                                }
                            }, "版本安装提示", "版本下载成功，是否安装！", "确定",
                                    "取消", false);
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            ToastUtils.showShort(TaskCarProcessActivity.this, "下载出错:" + ex.getMessage());
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                            ToastUtils.showShort(TaskCarProcessActivity.this, "下载取消:" + cex.getMessage());
                        }

                        @Override
                        public void onFinished() {
                            progressDialog.dismiss();
                        }
                    });

                }

                @Override
                public void onSubmitOnClickCancel() {

                }
            }, "调用精友豹眼系统失败", "是否已安装精友豹眼系统", "现在安装", "取消", false);

        }
    }

    /**
     * 封装精友数据vo
     * @param carLossBaseInfoDto
     * @return
     */
    private CarLossInitJyReqVO getCarLossInitJyReqVO(CarLossBaseInfoDto carLossBaseInfoDto, String lossItemNumber){
        CarLossInitJyReqVO req = new CarLossInitJyReqVO();
        req.setInsureVehicleCode(copyMainInfo.getCopyCarInfo().getCarTypeCode());//承保车型编码
        req.setInsureVehicleName(copyMainInfo.getCopyCarInfo().getBrandModels());//承保车型名称
        req.setBranchComCode(user.getBranchComCode());//定损员所属中支代码
        req.setBranchComName(user.getBranchComName());//定损员所属中支名称
        req.setComCode(user.getOrgCode());//定损员所属分公司代码
        req.setCompany(user.getOrgName());//定损员所属分公司名称
//        req.setComCode("016200");//定损员所属分公司代码
//        req.setCompany("兰州-甘肃分公司");//定损员所属分公司名称
        req.setHandlerCode(user.getEmpCde());//定损员代码
        req.setHandlerName(mission.getSrvyfactNme());//定损员姓名
        if (!StringUtils.isEmpty(mission.getTaskType()) && mission.getTaskType().equals("1")){
            req.setIsSubjectVehicle("1");//是否标的车 1是 0否
        }else if (!StringUtils.isEmpty(mission.getTaskType()) && mission.getTaskType().equals("2")){
            req.setIsSubjectVehicle("0");//是否标的车 1是 0否
        }
        req.setPlateNo(carLossBaseInfoDto.getLicsnseNo());//车牌号码
        req.setVinNo(carLossBaseInfoDto.getVinNo());//车架号
        req.setDriverName(carLossBaseInfoDto.getDriverName());//驾驶员
        req.setEnrolDate(copyMainInfo.getCopyCarInfo().getjQCFstRegYm());//初登日期--取交强险
        req.setMarkColor("白色");//牌照颜色
        req.setEvalTypeCode("01");//定损方式 01	修复定损 02一次性协议定损 03推定全损 04法院判决
        req.setLossNo(lossItemNumber);//定损单号
        req.setEngineNo(carLossBaseInfoDto.getEngineNo());//发动机号
        req.setRequestType("001");//001：进入定损    002：核损退回    003:查看定损
        List<LossRiskVO> riskList = new ArrayList<LossRiskVO>();
        //获取抄单险别信息列表
        List<CopyRiskDto> risks = copyMainInfo.getListRisk();
        if (risks != null && risks.size() > 0){
            for (CopyRiskDto risk : risks){
                //剔除 无法找到第三方特约险
                if(!risk.getInsuranceCode().equals("28")){
                    LossRiskVO lossRisk = new LossRiskVO();
                    lossRisk.setLossNo(lossItemNumber);//46902239
                    lossRisk.setCode(risk.getInsuranceCode());//险别代码
                    lossRisk.setName(risk.getInsuranceName());//险别
                    riskList.add(lossRisk);
                }
            }
        }
        req.setRiskList(riskList);
        return req;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            GsonBuilder builder = new GsonBuilder();
            builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
            Gson gson = builder.create();
            //获取定损信息
            String claimEvalData = data.getStringExtra("CLAIMEVAL_DATA");
            EvalResponse evalResponse = gson.fromJson(claimEvalData, EvalResponse.class);

            String responseCode = evalResponse.getResponseCode() ;
//            String errorMessage = evalResponse.getErrorMessage() ;
            if("0".equals(responseCode) || "2".equals(responseCode)){
                returnProcessStyle(carLoss);//点击返回、或没提交的情况下，刷白。--补丁。。
            }else if("1".equals(responseCode)){
                carLossMainInfoDto.setLosscheckFlag(1);
                changeProcessStyle(carLoss);//成功交互并有实体信息返回，则刷蓝色
            }else{
               //容错
            }
        }
    }


    @Override
    protected void onStop() {

        //保存信息
        saveSubmitMainVO();

        super.onStop();
    }


    public static void inputLog(String tag, String msg) { //信息太长,分段打印
        int max_str_length = 2001 - tag.length();
        while (msg.length() > max_str_length) {
            LogUtils.e(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        LogUtils.e(tag, msg);
    }
}
