package com.estar.hh.survey;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;
import com.estar.hh.survey.service.CrashHandler;
import com.estar.hh.survey.utils.FileUtils;
import com.estar.hh.survey.utils.LogUtils;
//import com.pgyersdk.crash.PgyCrashManager;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class MyApplication extends LitePalApplication {

    private FileUtils fileUtils;
    private boolean isDubug = true;
    private static boolean isAesString= false;

    public static boolean isAesString() {
        return isAesString;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.isOpen = isDubug;
        SDKInitializer.initialize(getApplicationContext());

        /**
         * xutils3工具初始化
         */
        x.Ext.init(this);//初始化xutils工具
        x.Ext.setDebug(true); // 是否输出debug日志

        /**
         * litePal工具初始化
         */
        LitePal.initialize(this);//litePal框架初始化

        /**
         * 蒲公英异常上报初始化
         */
        //PgyCrashManager.register(this);

        /**
         * 初始化崩溃日志记录
         */
        CrashHandler.getInstance().init(this);

        /**
         * 初始化文件路径
         */
        initFilePath();

        /**
         * 初始化证件识别证书
         */
        try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化缓存文件路径
     */
    private void initFilePath() {
        fileUtils = FileUtils.getInstance(this);
        fileUtils.getCacheMD();
        File file = new File(fileUtils.getImagePath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建本地lic文件
     *
     * @throws IOException
     */
    public void copyDataBase() throws IOException {
        //  Common common = new Common();
//        String dst = Environment.getExternalStorageDirectory().toString() + "/60586FE35F4988B86C79.lic";
        String dst = Environment.getExternalStorageDirectory().toString() + "/60586FE35F4988B86C79.lic";

        File file = new File(dst);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
        }

        try {
            InputStream myInput = getAssets().open("60586FE35F4988B86C79.lic");
            OutputStream myOutput = new FileOutputStream(dst);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
        }
    }

    public static String getVersionName(Context context) {

        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }


}
