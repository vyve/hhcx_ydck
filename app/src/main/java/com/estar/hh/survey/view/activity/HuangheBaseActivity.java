package com.estar.hh.survey.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.estar.hh.survey.utils.ActivityManagerUtils;


/**
 * Created by Administrator on 2017/9/26 0026.
 * 黄河移动查勘基础类
 */

public class HuangheBaseActivity extends Activity{

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManagerUtils.getInstance().addActivity(this);
        ActivityManagerUtils.setCurrentActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManagerUtils.getInstance().finishActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityManagerUtils.setCurrentActivity(this);
    }

    public void showLongToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
