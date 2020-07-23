package com.estar.hh.survey.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.request.RegistRequest;
import com.estar.hh.survey.entity.response.ModifyPassResponse;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.HttpUtils;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.utils.MD5Util;
import com.estar.hh.survey.utils.MightypleUtil;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.StringUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.estar.hh.survey.utils.TextUtils.isChinaPhoneLegal;
import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForNumber;
import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForNumberAndChar;

/**
 * Created by Administrator on 2017/10/26 0026.
 * 注册页面
 */

public class RegistActivity extends HuangheBaseActivity {

    @ViewInject(R.id.regist_back)
    private LinearLayout back;

    @ViewInject(R.id.regist_workNumber)
    private EditText workNumber;

    @ViewInject(R.id.regist_process_password)
    private EditText proPassword;

    @ViewInject(R.id.regist_phoneNumber)
    private EditText phoneNumber;

    @ViewInject(R.id.regist_password)
    private EditText password;

    @ViewInject(R.id.regist_submit)
    private Button submit;

    private String registerId;//极光号
    @ViewInject(R.id.tip)
    private TextView tip;
    @ViewInject(R.id.ck_regist)
    private CheckBox ck_regist;
    @ViewInject(R.id.ll_xieyi)
    private LinearLayout ll_xieyi;
    private boolean agree;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registe_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){
//        SharedPreferences login = getSharedPreferences("regist_agree", Context.MODE_PRIVATE);
//        boolean regist_agree = login.getBoolean("regist_agree", false);
//        if(regist_agree){
//            ll_xieyi.setVisibility(View.GONE);
//        }else {
//            ll_xieyi.setVisibility(View.VISIBLE);
//        }
        ck_regist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                agree = isChecked;
            }
        });
        Spanned tipStr = Html.fromHtml("绑定账号即代表您已阅读并同意" + "<font color=\"#007AFF\">《黄河财险app软件服务及隐私条款》</font>" +"");
        Spanned tip2 = Html.fromHtml("我已阅读并同意" + "<font color=\"#007AFF\">《黄河财险app软件服务及隐私条款》</font>" +"");
        tip.setText(tip2);
        tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistActivity.this,WebViewShowActivity.class);
                intent.putExtra("url", Constants.SERVICE_ANDPROVISION);
                intent.putExtra("title", "软件服务及隐私条款");
                startActivity(intent);
            }
        });
        setEditTextOnlyForNumberAndChar(workNumber);
//        setEditTextOnlyForNumberAndChar(proPassword);
        setEditTextOnlyForNumber(phoneNumber);
        setEditTextOnlyForNumberAndChar(password);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String workNumberValue = workNumber.getText().toString();
                String proPasswordValue = proPassword.getText().toString();
                String phoneNumberValue = phoneNumber.getText().toString();
                String passwordValue = password.getText().toString();

                if (StringUtils.isEmpty(workNumberValue)){
                    showShortToast("请填写工号");
                    return;
                }

                if (StringUtils.isEmpty(proPasswordValue)){
                    showShortToast("请填写理赔密码");
                    return;
                }

                if (proPasswordValue.length() < 6){
                    showShortToast("理赔密码长度至少6位");
                    return;
                }

                if (StringUtils.isEmpty(phoneNumberValue)){
                    showShortToast("请填写手机号");
                    return;
                }

//                if (!isChinaPhoneLegal(phoneNumberValue)){
//                    showShortToast("请填写正确的手机号");
//                    return;
//                }

                if (StringUtils.isEmpty(passwordValue)){
                    showShortToast("请填写密码");
                    return;
                }

                if (proPasswordValue.length() < 6){
                    showShortToast("密码长度至少6位");
                    return;
                }

                if (StringUtils.isEmpty(registerId)){
                    showShortToast("极光号初始化中，请稍候");
                    registerId = MightypleUtil.getDeviceId(RegistActivity.this);
                    return;
                }
                if (!agree) {
                    showShortToast("请同意隐私协议");
                    return;
                }
                SharedPreferences login = getSharedPreferences("regist_agree", Context.MODE_PRIVATE);
                //boolean regist_agree = login.getBoolean("regist_agree", false);
                SharedPreferences.Editor edit = login.edit();
                edit.putBoolean("regist_agree",true);
                edit.commit();
                regist(workNumberValue, proPasswordValue, phoneNumberValue, passwordValue);
            }
        });
    }

    private void initData(){

        registerId = MightypleUtil.getDeviceId(RegistActivity.this);

    }

    /**
     * 用户绑定
     * @param workNumberValue
     * @param proPasswordValue
     * @param phoneNumberValue
     * @param passwordValue
     */
    private void regist(String workNumberValue, String proPasswordValue,
                        String phoneNumberValue, String passwordValue){

        final MyProgressDialog dialog = new MyProgressDialog(this, "请稍候...");

        RegistRequest request = new RegistRequest();
        request.getData().setTelNo(phoneNumberValue);
        request.getData().setUserName(workNumberValue);
        request.getData().setClaimPwd(proPasswordValue);
        request.getData().setPwd(MD5Util.MD5Encode(passwordValue));
        request.getData().setRegisterId(registerId);

        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

        RequestParams params = HttpUtils.buildRequestParam(Constants.REGIST, reqMsg);
       // RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.REGIST, reqMsg);

        LogUtils.i("用户绑定请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("用户绑定返回参数为:", "--------------------------------------------\n" + result);
                ModifyPassResponse response = gson.fromJson(result, ModifyPassResponse.class);
                if (response.getSuccess()){
                    showShortToast("绑定成功");
                    finish();
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("用户绑定请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("用户绑定请求取消为:", "--------------------------------------------\n" + cex.getMessage());
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
