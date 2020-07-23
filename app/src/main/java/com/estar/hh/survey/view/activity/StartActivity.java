package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estar.hh.survey.R;
import com.estar.hh.survey.common.ConfigSharePrefrence;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.LoginRequest;
import com.estar.hh.survey.entity.response.LoginResponse;
import com.estar.hh.survey.utils.HttpClientUtil2;
import com.estar.hh.survey.utils.HttpUtils;
import com.estar.hh.survey.utils.MD5Util;
import com.estar.hh.survey.utils.MightypleUtil;
import com.estar.hh.survey.utils.SystemUtils;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.ocr.Config;
import com.estar.utils.StringUtils;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class StartActivity extends HuangheBaseActivity{

    @ViewInject(R.id.welcomeImg)
    private ImageView welcomeImg;

    private UserSharePrefrence userSharePrefrence;
    private ConfigSharePrefrence configSharePrefrence;

    private String registerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        x.view().inject(this);

        initView();
        initData();

        autoLogin();

    }

    private void initView(){
        Glide.with(this)
                .load(R.drawable.start_page)
                .centerCrop()
                .into(welcomeImg);
    }

    private void initData(){

        registerId = MightypleUtil.getDeviceId(this);

        userSharePrefrence = new UserSharePrefrence(this);
        configSharePrefrence = new ConfigSharePrefrence(this);
    }

    /**
     * 自动登录
     */
    private void autoLogin(){
        User user = userSharePrefrence.getUserEntity();
         if (!StringUtils.isEmpty(user.getUserName()) && !StringUtils.isEmpty(user.getPassWord()) && configSharePrefrence.getBoolean("autoLogin")){
            login(user.getUserName(), user.getPassWord());
        }else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void login(final String userName, final String password){

        LoginRequest request = new LoginRequest();
        request.getData().setUserName(userName);
        request.getData().setPassWord(MD5Util.MD5Encode(password));
        if (!StringUtils.isEmpty(registerId)) {
            request.getData().setRegisterId(registerId);
        }else {
            request.getData().setRegisterId("0000" + SystemUtils.getSystemModel());
        }
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.LOGIN, reqMsg);
        RequestParams params = HttpClientUtil2.getHttpRequestParam(Constants.LOGIN, reqMsg);

        Log.i("login请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                Log.i("login返回参数为:", "--------------------------------------------\n" + result);
                LoginResponse response = gson.fromJson(result, LoginResponse.class);
                if (response.getSuccess()){
                    if (response.getObj() != null) {
                        User user = response.getObj();
//                        DataSupport.deleteAll(User.class, "empCde = ?", user.getEmpCde());
                        user.save();
                        userSharePrefrence.putString("userName", userName);
                        userSharePrefrence.putString("passWord", password);
                        userSharePrefrence.setUserEntity(response.getObj());
                    }
                    startActivity(new Intent(StartActivity.this, HomeActivity.class));
                    finish();
                }else{
                    showShortToast(response.getMsg());
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("login请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i("login请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onFinished() {

            }
        });
    }

}
