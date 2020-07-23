package com.estar.hh.survey.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.CopyReportInfoDto;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.CusContactRequest;
import com.estar.hh.survey.entity.response.CusContactResponse;
import com.estar.hh.survey.utils.ActivityManagerUtils;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.utils.DateUtil;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Calendar;
import java.util.Date;

/**
 * 联系客户
 */
public class ContactCustomerActivity extends Activity {

    @ViewInject(R.id.contact_name)
    private TextView name;

    @ViewInject(R.id.contact_phone)
    private TextView phone;

    @ViewInject(R.id.contact_phone_call)
    private ImageView phoneCall;

    @ViewInject(R.id.contact_time)
    private TextView contactTime;

    @ViewInject(R.id.contact_arrive_time)
    private EditText arriveTime;

    @ViewInject(R.id.contact_cancel)
    private TextView cancel;

    @ViewInject(R.id.contact_comfirm)
    private TextView comfirm;

    private CopyMainInfoDto copyMainInfo = null;
    private Mission mission = null;
    private String callPhoneTime = null;//通话时间

    /**
     * 安全整改
     * add by zhengyg 2018年12月6日16:02:56
     */
    private UserSharePrefrence userSharePrefrence = null;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_customer_activity);
        x.view().inject(this);

        initView();
        iniData();

    }

    private void initView(){
        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String telNo = phone.getText().toString();
                    if (!StringUtils.isEmpty(telNo)) {
                        // 监听电话状态
                        TelephonyManager mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                        mTelephonyMgr.listen(new TeleListener(), PhoneStateListener.LISTEN_CALL_STATE);

                        // 生成呼叫意图
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNo));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (ActivityCompat.checkSelfPermission(ContactCustomerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        callPhoneTime = DateUtil.getTime_YMDMS(new Date());
                        startActivityForResult(intent,Activity.DEFAULT_KEYS_DIALER);
                    }else {
                        ToastUtils.showShort(ContactCustomerActivity.this, "未知手机号");
                    }
                }catch (Exception e) {
                    Toast.makeText(ContactCustomerActivity.this, "拨打电话出现异常,请重试", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StringUtils.isEmpty(contactTime.getText().toString())){
                    ToastUtils.showShort(ContactCustomerActivity.this, "请先联系客户");
                    return;
                }

                if (StringUtils.isEmpty(arriveTime.getText().toString())){
                    ToastUtils.showShort(ContactCustomerActivity.this, "请填写预计到达时间");
                    return;
                }

                cusContact(mission.getRptNo(), mission.getTaskId());
            }
        });
    }

    private void iniData(){

        /**
         * 安全整改
         * add by zhengyg 2018年12月6日16:02:56
         */
        userSharePrefrence = new UserSharePrefrence(this);
        user = userSharePrefrence.getUserEntity();

        copyMainInfo = (CopyMainInfoDto) getIntent().getSerializableExtra("copyMainInfo");
        mission = (Mission) getIntent().getSerializableExtra("mission");

        CopyReportInfoDto reportInfo = copyMainInfo.getCopyReportInfo();
        if (reportInfo == null){
            ToastUtils.showShort(ContactCustomerActivity.this, "报案人未找到");
        }else {
            name.setText(reportInfo.getReporter());
            phone.setText(reportInfo.getReportPhoneNo());
        }

    }


    /**
     * 联系客户接口
     * @param rptNo
     * @param taskNo
     */
    private void cusContact(String rptNo, String taskNo){

        Date nowDate = new Date();

        String arriveTimeValue = "";
        if (!StringUtils.isEmpty(arriveTime.getText().toString())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowDate);
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + Integer.valueOf(arriveTime.getText().toString()));
            arriveTimeValue = DateUtil.getTime_YMDMS(calendar.getTime());
        }

        CusContactRequest request = new CusContactRequest();
        request.getData().setReportNo(rptNo);
        request.getData().setTaskNo(taskNo);
        request.getData().setTalkTime(contactTime.getText().toString());
        request.getData().setCallPhoneTime(DateUtil.getTime_YMDMS(nowDate));
        request.getData().setArrivedTime(arriveTimeValue);
        request.getData().settFromLinkToArri(arriveTime.getText().toString());

        /**
         * 安全整改
         * add by zhengyg 2018年12月6日16:02:56
         */
        request.getData().setSrvyfaceCde(user.getEmpCde());

        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.TASKREAD, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.TASKREAD, reqMsg);

        LogUtils.i("联系客户请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("联系客户返回参数为:", "--------------------------------------------\n" + result);
                CusContactResponse response = gson.fromJson(result, CusContactResponse.class);
                ToastUtils.showShort(ContactCustomerActivity.this, response.getMsg());
                if (response.getSuccess()){
                    changeProcessColor();
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("联系客户请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                ToastUtils.showShort(ContactCustomerActivity.this, ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("联系客户请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                ToastUtils.showShort(ContactCustomerActivity.this, cex.getLocalizedMessage());
            }

            @Override
            public void onFinished() {

            }
        });

    }

    // 电话状态监听
    class TeleListener extends PhoneStateListener {
        private boolean callFalg = false;
        private boolean callInFalg = true;//防止在此时有电话呼入导致计时

        Date date1;

        public void onCallStateChanged(int state, String incomingNumber) {
            long time = 0;
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: {// 待机
                    LogUtils.w("待机");
                    callInFalg = true;

                    if (callFalg) {
                        Cursor cursor = null;
                        try {
                            if (ActivityCompat.checkSelfPermission(ContactCustomerActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//                                return;
                            }
                            cursor = getContentResolver().query(
                                    CallLog.Calls.CONTENT_URI,
                                    null,
                                    "type=2   and number="
                                            + phone.getText().toString(), null,
                                    CallLog.Calls.DEFAULT_SORT_ORDER);
                            int duration = 0;
                            if (cursor != null && cursor.getCount() > 0) {// 数据库中存在记录
                                cursor.moveToFirst();
                                int repor = cursor.getColumnIndex("duration");
                                String reportnos = cursor.getString(repor);
                                duration = Integer.parseInt(reportnos);

                                if (duration != 0) {
                                    if(duration>3)contactTime.setText("" + duration+"");
                                } else {
                                    Date date2 = new Date();
                                    time = (date2.getTime() - date1.getTime()) / 1000;
                                    contactTime.setText("" + time+"");
                                }
                            } else {
                                Date date2 = new Date();
                                time = (date2.getTime() - date1.getTime()) / 1000;
                                contactTime.setText("" + time+"");
                            }

                        } catch (Exception e) {
                            Date date2 = new Date();
                            time = (date2.getTime() - date1.getTime()) / 1000;
                            contactTime.setText("" + time+"");

                        }  finally {
                            if (cursor != null&&!cursor.isClosed())
                                cursor.close();
                        }
                        callFalg = false;
                    }
                    break;
                }
                case TelephonyManager.CALL_STATE_OFFHOOK: {// 摘机
                    LogUtils.w("摘机");
                    if (callInFalg) {
                        callFalg = true;
                        date1 = new Date();
                    }
                    break;
                }
                case TelephonyManager.CALL_STATE_RINGING: {// 响铃
                    LogUtils.w("响铃");
                    callInFalg = false;// 防止此时有电话进来而导至计时
                    break;
                }

                default:
                    break;
            }
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
                ((TaskProcessActivity) activity).changeProcessStyle(((TaskProcessActivity)activity).contact);
                ((TaskProcessActivity) activity).survMainInfoDto.setContactFlag(1);
                ((TaskProcessActivity) activity).survMainInfoDto.save();
            }break;
            case Constants.CARLOSS_TYPE:{
                Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskCarProcessActivity.class);
                ((TaskCarProcessActivity) activity).changeProcessStyle(((TaskCarProcessActivity)activity).contact);
                ((TaskCarProcessActivity) activity).carLossMainInfoDto.setContactFlag(1);
                ((TaskCarProcessActivity) activity).carLossMainInfoDto.save();
            }break;
            case Constants.GOODLOSS_TYPE:{
                Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskPropertyProcessActivity.class);
                ((TaskPropertyProcessActivity) activity).changeProcessStyle(((TaskPropertyProcessActivity)activity).contact);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.setContactFlag(1);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.save();
            }break;
            default:break;
        }

    }

}
