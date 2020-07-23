package com.estar.hh.survey.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lc on 18-11-14.
 */

public class SharedUtils {

    private static final String FILE_NAME = "shared_hh";

    /**
     * 存储基本数据类型
     *
     * @param context
     * @param key     key
     * @param data    value
     */
    public static void saveData(Context context, String key, Object data) {
        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context
                .MODE_PRIVATE);//读写基本数据类型都是在一个特定的文件，以软件的包名为文件名
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if ("Integer".equals(type)) {//注意这里是integer对象，同下
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }
        editor.commit();
    }

    /**
     * 读取基本数据类型
     *
     * @param context
     * @param key      key
     * @param defValue 当取不到值时返回默认值
     * @return
     */
    public static Object getData(Context context, String key, Object defValue) {
        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context
                .MODE_PRIVATE);  //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }
        return null;
    }

    public static void clearData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context
                .MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
