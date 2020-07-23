package com.estar.hh.survey.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.ChangePhoneRequest;
import com.estar.hh.survey.entity.response.ChangePhoneResponse;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.estar.hh.survey.utils.TextUtils.isChinaPhoneLegal;
import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForNumber;

/**
 * Created by Administrator on 2017/8/9.
 * 改绑手机号界面
 */

public class ChangePhoneActivity extends HuangheBaseActivity{

    @ViewInject(R.id.change_phone_back)
    private LinearLayout back;

    @ViewInject(R.id.change_phone_old_phone)
    private EditText oldPhone;

    @ViewInject(R.id.change_phone_new_phone)
    private EditText newPhone;

    @ViewInject(R.id.change_phone_check_code)
    private EditText checkCode;

    @ViewInject(R.id.change_phone_get_check)
    private Button getCheck;

    @ViewInject(R.id.change_phone_submit)
    private Button submit;

    /**
     * 安全整改
     * add by zhengyg 2018年12月6日16:02:56
     */
    private UserSharePrefrence userSharePrefrence = null;
    private User user = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){

        setEditTextOnlyForNumber(oldPhone);
        setEditTextOnlyForNumber(newPhone);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPhoneNum = oldPhone.getText().toString();

                if (StringUtils.isEmpty(oldPhoneNum) || oldPhoneNum.length() < 11){
                    ToastUtils.showShort(ChangePhoneActivity.this , "原手机号输入有误");
                    return;
                }

                getCheckCode();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPhoneNum = oldPhone.getText().toString();
                String newPhoneNum = newPhone.getText().toString();
                String checkCodeNum = checkCode.getText().toString();

                if (StringUtils.isEmpty(oldPhoneNum) || oldPhoneNum.length() < 11){
                    ToastUtils.showShort(ChangePhoneActivity.this , "原手机号输入有误");
                    return;
                }

                if (!isChinaPhoneLegal(oldPhoneNum)){
                    ToastUtils.showShort(ChangePhoneActivity.this , "原手机号输入有误");
                    return;
                }

                if (StringUtils.isEmpty(newPhoneNum) || newPhoneNum.length() < 11){
                    ToastUtils.showShort(ChangePhoneActivity.this , "新手机号输入有误");
                    return;
                }

                if (!isChinaPhoneLegal(newPhoneNum)){
                    ToastUtils.showShort(ChangePhoneActivity.this , "新手机号输入有误");
                    return;
                }

//                if (StringUtils.isEmpty(checkCodeNum)){
//                    ToastUtils.showShort(ChangePhoneActivity.this , "验证码输入有误");
//                    return;
//                }

                changePhone(oldPhoneNum, newPhoneNum);

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

    }


    /**
     * 获取短信验证码
     */
    private void getCheckCode(){

    }


    /**
     * 改绑手机号
     * @param oldPhone
     * @param newPhone
     */
    private void changePhone(String oldPhone, String newPhone){

        final MyProgressDialog dialog = new MyProgressDialog(ChangePhoneActivity.this, "手机号改绑中...");

        ChangePhoneRequest request = new ChangePhoneRequest();
        request.getData().setNewTelNo(newPhone);
        request.getData().setOldTelNo(oldPhone);
        /**
         * 安全整改
         * add by zhengyg 2018年12月6日16:02:56
         */
        request.getData().setTelNo(user.getEmpCde());
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.CHANGGEPHONE, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.CHANGGEPHONE, reqMsg);

        LogUtils.i("改绑手机号请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("改绑手机号返回参数为:", "--------------------------------------------\n" + result);
                ChangePhoneResponse response = gson.fromJson(result, ChangePhoneResponse.class);
                if (response.getSuccess()){
                    showShortToast("手机号改绑成功");
                    finish();
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("改绑手机号请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("改绑手机号请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                showShortToast(cex.getLocalizedMessage());
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });
    }

}
