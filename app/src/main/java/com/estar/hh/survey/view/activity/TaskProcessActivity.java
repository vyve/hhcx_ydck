package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.CarLossInfoDto;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.GoodInfoDto;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.SubmitSurvMainInfoDto;
import com.estar.hh.survey.entity.entity.SurvBaseInfoDto;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.CopyInfoRequest;
import com.estar.hh.survey.entity.request.SurvSaveRequest;
import com.estar.hh.survey.entity.request.SurvSubmitRequest;
import com.estar.hh.survey.entity.response.CopyInfoResponse;
import com.estar.hh.survey.entity.response.SurvSaveResponse;
import com.estar.hh.survey.entity.response.SurvSubmitResponse;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.LogTools;
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

import static com.estar.hh.survey.entity.entity.CarLossInfoDto.LOSSTYPECAR;
import static com.estar.hh.survey.entity.entity.CarLossInfoDto.LOSSTYPEOTHERCAR;
import static com.estar.hh.survey.view.activity.TaskCarProcessActivity.inputLog;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 现场查勘任务流程
 */

public class TaskProcessActivity extends HuangheBaseActivity implements View.OnClickListener {

    @ViewInject(R.id.task_process_back)
    private LinearLayout back;

    @ViewInject(R.id.task_process_caseno)
    private TextView caseNo;

    @ViewInject(R.id.task_process_taskno)
    private TextView taskNo;

    @ViewInject(R.id.task_process_read)
    public TextView read;

    @ViewInject(R.id.task_process_contact)
    public TextView contact;

    @ViewInject(R.id.task_process_remind)
    private TextView remind;

    @ViewInject(R.id.task_process_casesurvey)
    public TextView caseSurvey;

    @ViewInject(R.id.task_process_othercar)
    public TextView otherCar;

    @ViewInject(R.id.task_process_thing)
    public TextView thing;

    @ViewInject(R.id.task_process_people)
    public TextView people;

    @ViewInject(R.id.task_process_extend)
    public TextView extend;

    @ViewInject(R.id.task_process_feeno)
    public TextView feeNo;

    @ViewInject(R.id.task_process_operate)
    private LinearLayout operate;

    @ViewInject(R.id.task_process_video)
    private LinearLayout video;

    @ViewInject(R.id.task_process_change)
    private LinearLayout change;

    @ViewInject(R.id.task_process_save)
    private LinearLayout save;

    @ViewInject(R.id.task_process_submit)
    private LinearLayout submit;

    private Mission mission = null;
    public CopyMainInfoDto copyMainInfo = null;//抄单总信息
    public SubmitSurvMainInfoDto survMainInfoDto = null;//查勘提交vo

    private UserSharePrefrence userSharePrefrence = null;
    private User user = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_process_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView() {
        back.setOnClickListener(this);
        read.setOnClickListener(this);
        contact.setOnClickListener(this);
        remind.setOnClickListener(this);
        caseSurvey.setOnClickListener(this);
        otherCar.setOnClickListener(this);
        thing.setOnClickListener(this);
        people.setOnClickListener(this);
        extend.setOnClickListener(this);
        feeNo.setOnClickListener(this);
        video.setOnClickListener(this);
        change.setOnClickListener(this);
        save.setOnClickListener(this);
        submit.setOnClickListener(this);
    }


    private void initData() {

        userSharePrefrence = new UserSharePrefrence(this);
        user = userSharePrefrence.getUserEntity();

        mission = (Mission) getIntent().getSerializableExtra("mission");
        if (mission != null) {
            caseNo.setText(mission.getRptNo());
            taskNo.setText(mission.getTaskId());
            if (!StringUtils.isEmpty(mission.getRptNo())) {
                getCopyInfo(mission.getRptNo());
            }

            /**
             * 查找数据库中保存的任务提交实体
             */
            survMainInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                    .order("createTime desc").findFirst(SubmitSurvMainInfoDto.class);

            if (survMainInfoDto == null) {
                survMainInfoDto = new SubmitSurvMainInfoDto();
                survMainInfoDto.setTaskNo(mission.getTaskId());
                survMainInfoDto.setReportNo(mission.getRptNo());
                survMainInfoDto.setUserName(user.getEmpCde());
            }

            initProcess(survMainInfoDto);

            //任务状态为G 表示案件已成功提交至理赔平台 隐藏操作
            if (!StringUtils.isEmpty(mission.getTaskStatus()) && mission.getTaskStatus().compareTo("G") >= 0) {
                operate.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
            }

        }

    }


    /**
     * 初始化三者车和物损列表
     */
    private void initSurveyList() {

        if (copyMainInfo == null) {
            showShortToast("抄单获取失败");
            return;
        }

        /**
         * 新任务才执行此项操作
         */
        if (!mission.getTaskStatus().equals("A")) {
            return;
        }

        List<CarLossInfoDto> otherCars = DataSupport.where("reportNo = ? and taskNo = ? and lossItemType = ?"
                , mission.getRptNo(), mission.getTaskId(), LOSSTYPEOTHERCAR)
                .order("createTime desc").find(CarLossInfoDto.class);

        if (otherCars == null || otherCars.size() == 0) {
            List<CarLossInfoDto> lossCars = copyMainInfo.getSurvyListCarLossInfoDto();
            if (lossCars != null && lossCars.size() > 0) {
                for (CarLossInfoDto carLossInfoDto : lossCars) {
                    carLossInfoDto.setReportNo(mission.getRptNo());
                    carLossInfoDto.setTaskNo(mission.getTaskId());
                    carLossInfoDto.setLossItemType(LOSSTYPEOTHERCAR);
                    carLossInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
                    /**
                     * 默認附0
                     */
                    if (StringUtils.isEmpty(carLossInfoDto.getLicenseNoType())) {//默認號牌種類賦值
                        carLossInfoDto.setLicenseNoType("02");
                    }
                    if (StringUtils.isEmpty(carLossInfoDto.getIsHaveDuty())) {//默認是否有責賦值
                        carLossInfoDto.setIsHaveDuty("0");
                    }
                    if (StringUtils.isEmpty(carLossInfoDto.getDamageFlag())) {//默認是否有損失賦值
                        carLossInfoDto.setDamageFlag("1");
                    }
                    if (StringUtils.isEmpty(carLossInfoDto.getRepairFlag())) {//默認是否維修賦值
                        carLossInfoDto.setRepairFlag("0");
                    }
                    if (StringUtils.isEmpty(carLossInfoDto.getReserveFlag())) {//默認重大賠案標誌賦值
                        carLossInfoDto.setReserveFlag("0");
                    }
                    if (StringUtils.isEmpty(carLossInfoDto.getIsFoundOther())) {//默認是否無法找到第三方賦值
                        carLossInfoDto.setIsFoundOther("0");
                    }
                    if (StringUtils.isEmpty(carLossInfoDto.getIdentifyType())) {//默認證件類型賦值
                        carLossInfoDto.setIdentifyType("01");
                    }
                    if (StringUtils.isEmpty(carLossInfoDto.getExceptSumLossFee())) {//默認估損金額賦值
                        carLossInfoDto.setExceptSumLossFee("0");
                    }
                    carLossInfoDto.save();
                }
            }
        }


        List<GoodInfoDto> things = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").find(GoodInfoDto.class);

        if (things == null || things.size() == 0) {
            List<GoodInfoDto> lossGoods = copyMainInfo.getSurvyListGoodInfoDto();
            if (lossGoods != null && lossGoods.size() > 0) {
                for (GoodInfoDto goodInfoDto : lossGoods) {
                    goodInfoDto.setReportNo(mission.getRptNo());
                    goodInfoDto.setTaskNo(mission.getTaskId());
                    goodInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
                    if (StringUtils.isEmpty(goodInfoDto.getSumRescueFee())) {
                        goodInfoDto.setSumRescueFee("0");
                    }
                    if (StringUtils.isEmpty(goodInfoDto.getSumLossFee())) {
                        goodInfoDto.setSumLossFee("0");
                    }
                    goodInfoDto.save();
                }
            }
        }

    }


    /**
     * 流程图状态初始化
     *
     * @param survMainInfoDto
     */
    private void initProcess(SubmitSurvMainInfoDto survMainInfoDto) {

        if (survMainInfoDto.getReadFlag() == 1) {
            changeProcessStyle(read);
        }

        if (survMainInfoDto.getContactFlag() == 1) {
            changeProcessStyle(contact);
        }

        if (survMainInfoDto.getBaseInfoFlag() == 1) {
            changeProcessStyle(caseSurvey);
        }

        if (survMainInfoDto.getCarInfoFlag() == 1) {
            changeProcessStyle(otherCar);
        }

        if (survMainInfoDto.getGoodInfoFlag() == 1) {
            changeProcessStyle(thing);
        }

    }


    /**
     * 获取抄单信息
     *
     * @param rptNo
     */
    private void getCopyInfo(String rptNo) {

//        final MyProgressDialog dialog = new MyProgressDialog(TaskProcessActivity.this, "正在获取抄单信息...");

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
                inputLog("XChrist",result);
                CopyInfoResponse response = gson.fromJson(result, CopyInfoResponse.class);
                if (response.getSuccess()) {
                    copyMainInfo = response.getObj();
                    initSurveyList();
                } else {
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("抄单请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("抄单请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
//                dialog.stopDialog();
            }
        });

    }


    /**
     * 查勘暂存
     */
    private void survSave() {

        final MyProgressDialog dialog = new MyProgressDialog(TaskProcessActivity.this, "任务暂存中...");

        SurvSaveRequest request = new SurvSaveRequest();
        request.setData(survMainInfoDto);
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.SYNCHSURVEY, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.SYNCHSURVEY, reqMsg);

        LogUtils.i("任务暂存参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("任务暂存返回参数为:", "--------------------------------------------\n" + result);
                SurvSaveResponse response = gson.fromJson(result, SurvSaveResponse.class);
                if (response.getSuccess()) {
                    showShortToast(response.getMsg());
//                    survMainInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
//                    survMainInfoDto.save();
                    finish();
                } else {
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
     * 查勘提交
     */
    private void survSubmit() {

        final MyProgressDialog dialog = new MyProgressDialog(TaskProcessActivity.this, "任务提交中...");

        SurvSubmitRequest request = new SurvSubmitRequest();
        request.setData(survMainInfoDto);
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.SUBMITSURVEY, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.SUBMITSURVEY, reqMsg);

        LogUtils.i("任务提交请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public boolean onCache(String result) {
//                return false;
//            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("任务提交返回参数为:", "--------------------------------------------\n" + result);
                SurvSubmitResponse response = gson.fromJson(result, SurvSubmitResponse.class);
                if (response.getSuccess()) {
                    showShortToast(response.getMsg());
//                    survMainInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
//                    survMainInfoDto.save();
                    finish();
                } else {
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("任务提交请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("任务提交请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_process_back: {
                finish();
            }
            break;
            case R.id.task_process_read: {

                if (copyMainInfo == null) {
                    showShortToast("抄单信息获取失败，请重新获取");
                    return;
                }

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent = new Intent(this, TaskReadActivity.class);
                intent.putExtra("copyMainInfo", copyMainInfo);
                intent.putExtra("mission", mission);
                intent.putExtra("taskType", Constants.SURVEY_TYPE);
                startActivity(intent);

            }
            break;
            case R.id.task_process_contact: {

                if (copyMainInfo == null) {
                    showShortToast("抄单信息获取失败，请重新获取");
                    return;
                }

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }

                if (survMainInfoDto.getReadFlag() == 0) {
                    showShortToast("请先阅读任务");
                    return;
                }

                Intent intent = new Intent(this, ContactCustomerActivity.class);
                intent.putExtra("copyMainInfo", copyMainInfo);
                intent.putExtra("mission", mission);
                intent.putExtra("taskType", Constants.SURVEY_TYPE);
                startActivity(intent);

            }
            break;
            case R.id.task_process_remind: {

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent = new Intent(this, LeaveMessageActivity.class);
                intent.putExtra("mission", mission);
                startActivity(intent);
            }
            break;
            case R.id.task_process_casesurvey: {

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }
                LogTools.writeText("点击事故查勘："+survMainInfoDto.toString());
                if (survMainInfoDto.getReadFlag() == 0) {
                    showShortToast("请先阅读任务");
                    return;
                }

                if (copyMainInfo == null) {
                    showShortToast("抄单信息获取失败，请重新获取");
                    return;
                }

                Intent intent = new Intent(this, CaseSurveyActivity.class);
                intent.putExtra("mission", mission);
                intent.putExtra("copyMainInfo", copyMainInfo);
                startActivity(intent);
                survMainInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                        .order("createTime desc").findFirst(SubmitSurvMainInfoDto.class);
                LogTools.writeText("重新获取数据库查询的："+survMainInfoDto.toString());
            }
            break;
            case R.id.task_process_othercar: {

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }

                if (survMainInfoDto.getReadFlag() == 0) {
                    showShortToast("请先阅读任务");
                    return;
                }

                Intent intent = new Intent(this, OtherCarActivity.class);
                intent.putExtra("mission", mission);
                startActivity(intent);

            }
            break;
            case R.id.task_process_thing: {

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }

                if (survMainInfoDto.getReadFlag() == 0) {
                    showShortToast("请先阅读任务");
                    return;
                }

                Intent intent = new Intent(this, ThingLossActivity.class);
                intent.putExtra("mission", mission);
                startActivity(intent);

            }
            break;
            case R.id.task_process_people: {
//                Intent intent = new Intent(this, ManDamegeActivity.class);
//                startActivity(intent);
//                changeProcessStyle(people);
            }
            break;
            case R.id.task_process_extend: {
//                changeProcessStyle(extend);
            }
            break;
            case R.id.task_process_feeno: {
//                Intent intent = new Intent(this, InCountActivity.class);
//                startActivity(intent);
//                changeProcessStyle(feeNo);
            }
            break;
            case R.id.task_process_change: {

            }
            break;
            case R.id.task_process_save: {

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }

                //任务状态为E表示案件已暂存
                if (!StringUtils.isEmpty(mission.getTaskStatus()) && mission.getTaskStatus().compareTo("E") >= 0) {
                    showShortToast("该案件已暂存");
                    return;
                }

                if (survMainInfoDto == null) {
                    showShortToast("任务信息为空, 请先完成任务");
                    return;
                }

                if (!setSubmitMainVO()) {
                    return;
                }

                survSave();
            }
            break;
            case R.id.task_process_submit: {

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }

                //任务状态为F表示案件已提交
                if (!StringUtils.isEmpty(mission.getTaskStatus()) && mission.getTaskStatus().compareTo("G") >= 0) {
                    showShortToast("该案件已提交");
                    return;
                }

                if (survMainInfoDto == null) {
                    showShortToast("任务信息为空, 请先完成任务");
                    return;
                }

                if (!setSubmitMainVO()) {
                    return;
                }

                survSubmit();
            }
            break;
            case R.id.task_process_video: {

                if (mission == null) {
                    showShortToast("未找到任务号");
                    return;
                }

                Intent intent2 = new Intent(TaskProcessActivity.this, VideoActivity.class);
                intent2.putExtra("mission", mission);
                startActivity(intent2);

            }
            break;
        }
    }

    /**
     * 封装提交总vo
     *
     * @return
     */
    private boolean setSubmitMainVO() {

        if (survMainInfoDto.getReadFlag() == 0) {
            showShortToast("阅读任务未完成");
            return false;
        }

        if (survMainInfoDto.getBaseInfoFlag() == 0) {
            showShortToast("事故查勘任务未完成");
            return false;
        }

        SurvBaseInfoDto survBaseInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").findFirst(SurvBaseInfoDto.class);

        if (survBaseInfoDto == null) {
            showShortToast("请完成查勘信息填写");
            return false;
        }

        CarLossInfoDto carLossInfoDto = DataSupport.where("reportNo = ? and taskNo = ? and lossItemType = ?", mission.getRptNo(), mission.getTaskId(), LOSSTYPECAR)
                .order("createTime desc").findFirst(CarLossInfoDto.class);

        if (carLossInfoDto == null) {
            showShortToast("请完成标的信息填写");
            return false;
        }

        List<CarLossInfoDto> otherCars = DataSupport.where("reportNo = ? and taskNo = ? and lossItemType = ?", mission.getRptNo(), mission.getTaskId(), LOSSTYPEOTHERCAR)
                .order("createTime desc").find(CarLossInfoDto.class);

        List<GoodInfoDto> things = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").find(GoodInfoDto.class);

        survMainInfoDto.setSurvBaseInfoDto(survBaseInfoDto);
        survMainInfoDto.setCarLossInfoDto(carLossInfoDto);
        survMainInfoDto.setListCarLossInfoDto(otherCars);
        survMainInfoDto.setListGoodInfoDto(things);

        return true;

    }

    /**
     * 保存提交总vo
     *
     * @return
     */
    private void saveSubmitMainVO() {

        LogUtils.i("保存提交信息", "");

        MyProgressDialog dialog = new MyProgressDialog(this, "正在保存数据");

        SurvBaseInfoDto survBaseInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").findFirst(SurvBaseInfoDto.class);

        CarLossInfoDto carLossInfoDto = DataSupport.where("reportNo = ? and taskNo = ? and lossItemType = ?", mission.getRptNo(), mission.getTaskId(), "1")
                .order("createTime desc").findFirst(CarLossInfoDto.class);

        List<CarLossInfoDto> otherCars = DataSupport.where("reportNo = ? and taskNo = ? and lossItemType = ?", mission.getRptNo(), mission.getTaskId(), "2")
                .order("createTime desc").find(CarLossInfoDto.class);

        List<GoodInfoDto> things = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").find(GoodInfoDto.class);

        if (survBaseInfoDto != null) survMainInfoDto.setSurvBaseInfoDto(survBaseInfoDto);
        if (carLossInfoDto != null) survMainInfoDto.setCarLossInfoDto(carLossInfoDto);
        if (otherCars != null) survMainInfoDto.setListCarLossInfoDto(otherCars);
        if (things != null) survMainInfoDto.setListGoodInfoDto(things);
        survMainInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
        survMainInfoDto.save();

        dialog.stopDialog();

    }

    /**
     * 改变流程节点样式
     *
     * @param textView
     */
    public void changeProcessStyle(TextView textView) {
        textView.setBackgroundResource(R.drawable.lancardview);
        textView.setTextColor(0xffffffff);
    }

    /**
     * 改变流程节点样式(变白还原)
     *
     * @param textView
     */
    public void returnProcessStyle(TextView textView) {
        textView.setBackgroundResource(R.drawable.cardview);
        textView.setTextColor(0xff919191);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    protected void onDestroy() {
//
//        //退出时保存数据
//        saveSubmitMainVO();
//
//        super.onDestroy();
//    }

    /**
     * 极端情况下，系统会直接杀死我们的app进程，并不执行activity的onDestroy()
     * 回调方法, 因此我们需要使用onStop()来释放资源，从而避免内存泄漏。或者执行
     * 那些CPU intensive的shut-down操作，例如往数据库写信息。
     */
    @Override
    protected void onStop() {

        //退出时保存数据
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
