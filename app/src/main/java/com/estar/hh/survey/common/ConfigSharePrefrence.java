package com.estar.hh.survey.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/11/1 0001.
 * 系统属性数据
 */

public class ConfigSharePrefrence {

    private SharedPreferences config;
    private SharedPreferences.Editor configEditor;

    private static final String SPNAME = "config";

    public static final String SIGNIN = "signin";//签到状态标志
    public static final String LOGUPLOADSWITCH = "logUpload";//日志上传标志

    public ConfigSharePrefrence(Context context){
        config = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        configEditor = config.edit();
    }

    public String getString(String key){
        return config.getString(key, "");
    }

    public int getInt(String key){
        return config.getInt(key, -1);
    }

    public boolean getBoolean(String key){
        return config.getBoolean(key, false);
    }

    public void putString(String key, String value){
        configEditor.putString(key, value);
        configEditor.commit();
    }

    public void putInt(String key, int value){
        configEditor.putInt(key, value);
        configEditor.commit();
    }

    public void putBoolean(String key, boolean value){
        configEditor.putBoolean(key, value);
        configEditor.commit();
    }

    public void commitData(){
        configEditor.commit();
    }


}
