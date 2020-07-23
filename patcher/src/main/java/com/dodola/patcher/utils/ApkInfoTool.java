package com.dodola.patcher.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 获取apk信息
 * @author zhangzhp
 * 
 */
public class ApkInfoTool {
	private static final String tag = null;

	/**
	 * 
	 * @param context
	 *            Context上下文
	 * @param apkPath
	 *            apk在SD中的路径
	 * @return
	 */
	public static Drawable getApkIcon(Context context, String apkPath) {

		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.sourceDir = apkPath;
			return appInfo.loadIcon(pm);
		}
		return null;
	}

	/**
	 * 获取apk的VersionName
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {

		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(),
					0);
			return packinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取apk的VersionCode
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {

		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(),
					0);
			return packinfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 备份data/app目录下本程序的apk安装文件
	 * @param packageName
	 * @param mActivity
	 * @throws IOException
	 */
	public static void backupApp(String packageName, Activity mActivity)
			throws IOException {
		// 存放位置
		String newFile = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
		String oldFile = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			// 原始位置
			oldFile = mActivity.getPackageManager().getApplicationInfo(
					packageName, 0).sourceDir;


		File in = new File(oldFile);
		File out = new File(newFile + packageName + ".apk");
		if (!out.exists()) {
			out.createNewFile();
		}

		 fis = new FileInputStream(in);
		 fos = new FileOutputStream(out);

		int count;
		// 文件太大的话，我觉得需要修改
		byte[] buffer = new byte[256 * 1024];
		while ((count = fis.read(buffer)) > 0) {
			fos.write(buffer, 0, count);
		}

		fis.close();
		fos.flush();
		fos.close();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}finally {
			if (null!=fis){
			fis.close();
			}
			if (null!=fos){
				fos.flush();
				fos.close();
			}

		}
	}

}
