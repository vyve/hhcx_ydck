package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.common.ConfigSharePrefrence;
import com.estar.hh.survey.utils.ActivityManagerUtils;
import com.estar.hh.survey.utils.CacheCleanUtils;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.estarview.ToggleButtonView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.estar.hh.survey.common.ConfigSharePrefrence.LOGUPLOADSWITCH;

/**
 * Created by Administrator on 2017/10/12 0012.
 * 个人设置
 */

public class SettingActivity extends HuangheBaseActivity implements View.OnClickListener{

    @ViewInject(R.id.setting_back)
    private LinearLayout back;

    @ViewInject(R.id.setting_mydata)
    private LinearLayout ownData;

    @ViewInject(R.id.setting_changepass)
    private LinearLayout changePwd;

    @ViewInject(R.id.setting_changetel)
    private LinearLayout changeTel;

    @ViewInject(R.id.setting_clear)
    private LinearLayout clear;

    @ViewInject(R.id.setting_log)
    private LinearLayout log;

    @ViewInject(R.id.setting_log_switch)
    private ToggleButtonView logSwitch;

    @ViewInject(R.id.setting_cache)
    private TextView cacheSize;

    @ViewInject(R.id.setting_about)
    private LinearLayout about;

    @ViewInject(R.id.setting_logout)
    private Button logout;

    private MyProgressDialog dialog;
    private ConfigSharePrefrence configSharePrefrence = null;
    private boolean loguploadswitch = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        x.view().inject(this);

        initData();
        initView();

    }

    private void initData(){
        configSharePrefrence = new ConfigSharePrefrence(this);
        loguploadswitch = configSharePrefrence.getBoolean(LOGUPLOADSWITCH);
    }

    private void initView(){

        String caches = null;
        try {
            caches = CacheCleanUtils.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(caches)){
            cacheSize.setText("0MB");
        }else {
            cacheSize.setText(caches);
        }

        if (loguploadswitch){
            logSwitch.setToggleOn();
        }else {
            logSwitch.setToggleOff();
        }

        /**
         * 日志上传控制开关
         */
        logSwitch.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                configSharePrefrence.putBoolean(LOGUPLOADSWITCH, on);
            }
        });

        back.setOnClickListener(this);
        ownData.setOnClickListener(this);
        changePwd.setOnClickListener(this);
        changeTel.setOnClickListener(this);
        clear.setOnClickListener(this);
        about.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_back:{
                finish();
            }break;
            case R.id.setting_mydata:{//个人资料
                Intent intent = new Intent(SettingActivity.this, MyDataAcitivty.class);
                startActivity(intent);
            }break;
            case R.id.setting_changepass:{//修改密码
                Intent intent = new Intent(SettingActivity.this, ModifyPasswordActivity.class);
                startActivity(intent);
            }break;
            case R.id.setting_changetel:{//改绑手机号
                Intent intent = new Intent(SettingActivity.this, ChangePhoneActivity.class);
                startActivity(intent);
            }break;
            case R.id.setting_clear:{
                new CacheClearTask().execute();//执行缓存清楚操作
            }break;
            case R.id.setting_about:{//关于
                Intent intent = new Intent(SettingActivity.this, VersionAboutActivity.class);
                startActivity(intent);
            }break;
            case R.id.setting_logout:{
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                finish();//关闭当前页
                ActivityManagerUtils.getInstance().finishActivityclass(HomeActivity.class);//关闭主页
            }break;
        }
    }

    /**
     * 缓存清理异步任务
     */
    private class CacheClearTask extends AsyncTask<Object, String, String> {

        @Override
        protected void onPreExecute() {//onPreExecute方法用于在执行后台任务前做一些UI操作
            super.onPreExecute();
            dialog = new MyProgressDialog(SettingActivity.this, "正在清除缓存...");
        }

        @Override
        protected String doInBackground(Object... objects) {//doInBackground方法内部执行后台任务,不可在此方法内修改UI
            try {
                Thread.sleep(1000);
                CacheCleanUtils.clearAllCache(SettingActivity.this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {//onProgressUpdate方法用于更新进度信息
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {//onPostExecute方法用于在执行完后台任务后更新UI,显示结果
            super.onPostExecute(s);
            dialog.stopDialog();
            String caches = null;
            try {
                caches = CacheCleanUtils.getTotalCacheSize(SettingActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isEmpty(caches)){
                cacheSize.setText("0KB");
            }else {
                cacheSize.setText(caches);
            }
            ToastUtils.showShort(SettingActivity.this, "清除完毕");
        }

        @Override
        protected void onCancelled() {//onCancelled方法用于在取消执行中的任务时更改UI
            super.onCancelled();
        }
    }

}
