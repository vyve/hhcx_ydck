package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.request.ModifyPassRequest;
import com.estar.hh.survey.entity.response.ModifyPassResponse;
import com.estar.hh.survey.utils.ActivityManagerUtils;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.utils.MD5Util;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForNumberAndChar;

/**
 * Created by Administrator on 2017/8/9.
 * 修改密码
 */

public class ModifyPasswordActivity extends HuangheBaseActivity{

    @ViewInject(R.id.modify_password_back)
    private LinearLayout back;

    @ViewInject(R.id.modify_password_old)
    private EditText old_pass;

    @ViewInject(R.id.modify_password_new)
    private EditText new_pass;

    @ViewInject(R.id.modify_password_confirm)
    private EditText confirm_pass;

    @ViewInject(R.id.modify_password_submit)
    private Button submit_pass;

    private String telNo;
    private UserSharePrefrence userSharePrefrence;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_password_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){

        setEditTextOnlyForNumberAndChar(old_pass);
        setEditTextOnlyForNumberAndChar(new_pass);
        setEditTextOnlyForNumberAndChar(confirm_pass);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldPass = old_pass.getText().toString();
                String newPass = new_pass.getText().toString();
                String confirmPass = confirm_pass.getText().toString();

                if (StringUtils.isEmpty(oldPass)){
                    ToastUtils.showShort(ModifyPasswordActivity.this, "请输入原密码");
                    return;
                }

                if (StringUtils.isEmpty(newPass)){
                    ToastUtils.showShort(ModifyPasswordActivity.this, "请输入新密码");
                    return;
                }

                if (StringUtils.isEmpty(confirmPass)){
                    ToastUtils.showShort(ModifyPasswordActivity.this, "请确认新密码");
                    return;
                }

                if (confirmPass.length() < 6){
                    ToastUtils.showShort(ModifyPasswordActivity.this, "密码设置不能小于6位");
                    return;
                }

                if (!newPass.equals(confirmPass)){
                    ToastUtils.showShort(ModifyPasswordActivity.this, "两次输入密码不一致");
                    return;
                }

                modify(oldPass, newPass);

            }
        });
    }

    private void initData(){

        userSharePrefrence = new UserSharePrefrence(this);
        telNo = userSharePrefrence.getUserEntity().getEmpCde();

    }


    private MyProgressDialog dialog;

    /**
     * 修改密码
     * @param oldPass
     * @param newPass
     */
    private void modify(String oldPass, String newPass){

        dialog = new MyProgressDialog(ModifyPasswordActivity.this, "密码修改中...");

        ModifyPassRequest request = new ModifyPassRequest();
        request.getData().setOldPwd(MD5Util.MD5Encode(oldPass));
        request.getData().setNewPwd(MD5Util.MD5Encode(newPass));
        request.getData().setTelNo(telNo);
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.MODIFYPASS, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.MODIFYPASS, reqMsg);

        LogUtils.i("密码修改请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("密码修改返回参数为:", "--------------------------------------------\n" + result);
                ModifyPassResponse response = gson.fromJson(result, ModifyPassResponse.class);
                if (response.getSuccess()){
                    userSharePrefrence.putString("passWord", "");
                    startActivity(new Intent(ModifyPasswordActivity.this, LoginActivity.class));
                    ActivityManagerUtils.getInstance().finishActivityclass(HomeActivity.class);//关闭主页
                    finish();//关闭当前页
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("密码修改请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("密码修改请求取消为:", "--------------------------------------------\n" + cex.getMessage());
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
