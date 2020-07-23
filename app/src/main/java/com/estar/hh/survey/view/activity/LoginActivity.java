package com.estar.hh.survey.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.ConfigSharePrefrence;
import com.estar.hh.survey.common.MyStringCallback;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.LoginRequest;
import com.estar.hh.survey.entity.response.LoginResponse;
import com.estar.hh.survey.utils.ActivityManagerUtils;
import com.estar.hh.survey.utils.HttpClientUtil2;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.utils.MD5Util;
import com.estar.hh.survey.utils.MightypleUtil;
import com.estar.hh.survey.utils.SystemUtils;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.StringUtils;
import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForNumberAndChar;


/**
 * Created by Administrator on 2017/8/1.
 * 登录页
 */

public class LoginActivity extends HuangheBaseActivity {

    @ViewInject(R.id.login_userName)
    private EditText loginUserName;

    @ViewInject(R.id.login_passwd)
    private EditText loginPassword;

    @ViewInject(R.id.login_userName_clear)
    private ImageView userNameClear;

    @ViewInject(R.id.login_passwd_clear)
    private ImageView passwordClear;

    @ViewInject(R.id.login_autologin)
    private CheckBox autoLogin;

    @ViewInject(R.id.login_submit)
    private Button loginSubmit;

    @ViewInject(R.id.login_regist)
    private Button loginRegist;

    @ViewInject(R.id.login_version)
    private TextView versionCode;

    private UserSharePrefrence userSharePrefrence;
    private ConfigSharePrefrence configSharePrefrence;

    private boolean isExit;

    private MyProgressDialog dialog;

    private String registerId;
    @ViewInject(R.id.ck_login)
    private CheckBox ck_login;
    private boolean agree;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        x.view().inject(this); //注解注释View的绑定
        initView();
        iniData();
    }

    /**
     * 初始化控件相关
     */
    private void initView() {

        setEditTextOnlyForNumberAndChar(loginUserName);
//        setEditTextOnlyForNumberAndChar(loginPassword);

        loginUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (StringUtils.isEmpty(loginUserName.getText().toString())) {
                    userNameClear.setVisibility(View.INVISIBLE);
                } else {
                    userNameClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (StringUtils.isEmpty(loginPassword.getText().toString())) {
                    passwordClear.setVisibility(View.INVISIBLE);
                } else {
                    passwordClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userNameClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserName.setText("");
            }
        });

        passwordClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPassword.setText("");
            }
        });

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configSharePrefrence.putBoolean("autoLogin", isChecked);
            }
        });

        /**
         * 登录执行
         */
        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userName = loginUserName.getText().toString();
                String password = loginPassword.getText().toString();

                if (StringUtils.isEmpty(userName)) {
                    showShortToast("请填写用户名");
                    return;
                }

                if (StringUtils.isEmpty(password)) {
                    showLongToast("请输入正确的密码");
                    return;
                }

                if (StringUtils.isEmpty(registerId)) {
//                    registerId = MightypleUtil.getDeviceId(LoginActivity.this);
//                    showShortToast("极光号初始化中，请稍候");
//                    return;
                    registerId = "0000" + SystemUtils.getSystemModel();

                }
                SharedPreferences login = getSharedPreferences("login_agree", Context.MODE_PRIVATE);
                boolean login_agree = login.getBoolean("login_agree", false);
                if(!login_agree) {
                    if (!agree) {
                        showLongToast("请同意隐私协议");
                        return;
                    }
                }
                SharedPreferences.Editor edit = login.edit();
                edit.putBoolean("login_agree",true);
                edit.commit();
                login(userName, password);

            }
        });

        loginRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });
        ck_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                agree = isChecked;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniData();
    }

    private void iniData() {
        SharedPreferences login = getSharedPreferences("login_agree", Context.MODE_PRIVATE);
        boolean login_agree = login.getBoolean("login_agree", false);
        if(login_agree){
            ck_login.setVisibility(View.GONE);
        }else {
            ck_login.setVisibility(View.VISIBLE);
        }
        userSharePrefrence = new UserSharePrefrence(this);
        configSharePrefrence = new ConfigSharePrefrence(this);
        registerId = MightypleUtil.getDeviceId(this);

        User userEntity = userSharePrefrence.getUserEntity();
        String telNo = userEntity.getTelNo();
        String userName = userEntity.getUserName();
        String passWord = userEntity.getPassWord();
        loginUserName.setText(userName);
        loginPassword.setText(passWord);
        if (!StringUtils.isEmpty(userName)) {
            userNameClear.setVisibility(View.VISIBLE);
        }
        if (!StringUtils.isEmpty(passWord)) {
            passwordClear.setVisibility(View.VISIBLE);
        }

        boolean isAutoLogin = configSharePrefrence.getBoolean("autoLogin");
        autoLogin.setChecked(isAutoLogin);

        String version = null;
        try {
            version = SystemUtils.getVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!StringUtils.isEmpty(version)) {
            versionCode.setText(Constants.VERSION_DIF + version);
        } else {
            versionCode.setText(Constants.VERSION_DIF + "1.0.0");
        }
        Spanned tip = Html.fromHtml("登录即代表您已阅读并同意" + "<font color=\"#007AFF\">《黄河财险app软件服务及隐私条款》</font>" +"");
        Spanned tip2 = Html.fromHtml("我已阅读并同意" + "<font color=\"#007AFF\">《黄河财险app软件服务及隐私条款》</font>" +"");
        if(login_agree){
            versionCode.setText(tip);
        }else {
            versionCode.setText(tip2);
        }
        versionCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,WebViewShowActivity.class);
                intent.putExtra("url", Constants.SERVICE_ANDPROVISION);
                startActivity(intent);
            }
        });
    }

    private void login(final String userName, final String password) {


        dialog = new MyProgressDialog(LoginActivity.this, "正在登录...");

        LoginRequest request = new LoginRequest();
        request.getData().setUserName(userName);
        request.getData().setPassWord(MD5Util.MD5Encode(password));
        request.getData().setRegisterId(registerId);
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

        RequestParams params = HttpClientUtil2.getHttpRequestParam(Constants.LOGIN, reqMsg);


        x.http().post(params, new MyStringCallback() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onMySuccess(String result) {
                LogUtils.i("login返回参数为:", "--------------------------------------------\n" + result);
                LoginResponse response = gson.fromJson(result, LoginResponse.class);
                if (response.getSuccess()) {
                    if (response.getObj() != null) {
                        User user = response.getObj();
//                        DataSupport.deleteAll(User.class, "empCde = ?", user.getEmpCde());
                        user.save();
                        userSharePrefrence.putString("userName", userName);
                        userSharePrefrence.putString("passWord", password);
                        userSharePrefrence.setUserEntity(response.getObj());
                    }
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("login请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("login请求取消为:", "--------------------------------------------\n" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 3000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            ActivityManagerUtils.getInstance().exit();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }
    };
}
