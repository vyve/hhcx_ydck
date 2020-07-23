package com.estar.hh.survey.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.MyApplication;
import com.estar.hh.survey.R;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.response.VersionVO;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.SystemUtils;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.google.gson.Gson;
//import com.pgyersdk.javabean.AppBean;
//import com.pgyersdk.update.PgyUpdateManager;
//import com.pgyersdk.update.UpdateManagerListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21.
 * 关于版本界面
 */

public class VersionAboutActivity extends Activity {

    @ViewInject(R.id.version_back)
    private LinearLayout back;

    @ViewInject(R.id.version_code)
    private TextView versionCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version_about_activity);
        x.view().inject(this);

        initView();

    }

    private void initView(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String version = null;
        try {
            version = SystemUtils.getVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!StringUtils.isEmpty(version)){
            versionCode.setText(Constants.VERSION_DIF + version);
        }else {
            versionCode.setText(Constants.VERSION_DIF + "1.0.0");
        }

        versionCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkVersion();
            }
        });
    }

    private void checkVersion(){
//        PgyUpdateManager.register(VersionAboutActivity.this, "com.estar.hh.survey", new UpdateManagerListener(){
//
//            @Override
//            public void onNoUpdateAvailable() {
//                ToastUtils.showShort(VersionAboutActivity.this, "暂无版本更新");
//            }
//
//            @Override
//            public void onUpdateAvailable(String s) {
//                // 将新版本信息封装到AppBean中
//                final AppBean appBean = getAppBeanFromString(s);
//                new AlertDialog.Builder(VersionAboutActivity.this,AlertDialog.THEME_HOLO_LIGHT)
//                        .setTitle("更新提示")
//                        .setMessage("发现新版本" + appBean.getVersionName() + " 是否更新？")
//                        .setCancelable(false)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                startDownloadTask(
//                                        VersionAboutActivity.this,
//                                        appBean.getDownloadURL());
//            }
//                        })
//                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
//            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        }).show();
//            }
//        });
    }

}
