package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.ConfigSharePrefrence;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.LoginRequest;
import com.estar.hh.survey.entity.request.SignRequest;
import com.estar.hh.survey.entity.response.LoginResponse;
import com.estar.hh.survey.entity.response.SignResponse;
import com.estar.hh.survey.service.LocationInfoUploadService;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.HttpUtils;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.ToastUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/10/13 0013.
 * 签到界面
 */

public class SignActivity extends HuangheBaseActivity{

    @ViewInject(R.id.sign_back)
    private LinearLayout back;

    @ViewInject(R.id.sign_in)
    private TextView signIn;

    @ViewInject(R.id.sign_out)
    private TextView signOut;

    private User user;
    private ConfigSharePrefrence configSharePrefrence;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_activity);
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

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void initData(){
        configSharePrefrence = new ConfigSharePrefrence(this);
        UserSharePrefrence userSharePrefrence = new UserSharePrefrence(this);
        user = userSharePrefrence.getUserEntity();
    }

    private void signIn(){

        if (user != null){
            sign("1");
        }else {
            showShortToast("用户信息验证失败，请重新登录");
        }

    }

    private void signOut(){

        if (user != null){
            sign("2");
        }else {
            showShortToast("用户信息验证失败，请重新登录");
        }

    }

    private void sign(final String signFlag){

        String signMsg = null;
        if (signFlag.equals("1")){//上班签到
            signMsg = "签到中...";
        }else if (signFlag.equals("2")){//下班签退
            signMsg = "签退中...";
        }

        final MyProgressDialog dialog = new MyProgressDialog(SignActivity.this, signMsg);

        SignRequest request = new SignRequest();
        request.getData().setSignType(signFlag);
        request.getData().setEmpCde(user.getEmpCde());
        request.getData().setDptCde(user.getOrgCode());
        request.getData().setDtpNme(user.getOrgName());
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.SIGN, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.SIGN, reqMsg);

        LogUtils.i("考勤请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("考勤返回参数为:", "--------------------------------------------\n" + result);
                SignResponse response = gson.fromJson(result, SignResponse.class);
                if (response.getSuccess()){
                    if (signFlag.equals("1")){
                        showShortToast("签到成功");
                        //若签到请求成功
                        configSharePrefrence.putBoolean(ConfigSharePrefrence.SIGNIN, true);//true:签到状态  false：签退状态
                        startService(new Intent(SignActivity.this, LocationInfoUploadService.class));
                    }else if (signFlag.equals("2")){
                        showShortToast("签退成功");
                        //若签到请求成功
                        configSharePrefrence.putBoolean(ConfigSharePrefrence.SIGNIN, false);//true:签到状态  false：签退状态
                        stopService(new Intent(SignActivity.this, LocationInfoUploadService.class));
                    }
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("考勤请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("考勤请求取消为:", "--------------------------------------------\n" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }

}
