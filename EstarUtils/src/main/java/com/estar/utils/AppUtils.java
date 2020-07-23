package com.estar.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 跟App相关的辅助类
 * 
 * @author zhy
 * 
 */
public class AppUtils
{

	private AppUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}
	
	/**
	 * 判断该APK是否正在运行
	 * 
	 * @param apkPackageName
	 *            想要判断的应用包名
	 * @return true 正在运行 false 未运行
	 * 
	 * */
	public static boolean appIsRun(Context context, String apkPackageName) {

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(apkPackageName) && info.baseActivity.getPackageName().equals(apkPackageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取机器安装软件信息（包名、版本号、版本code）
	 * 
	 * @return ArrayList<AppInfo>
	 */
	public static ArrayList<AppInfo> getPackagesInfo(Context context) {
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据
		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			// 非系统应用才会添加至appList
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				AppInfo tmpInfo = new AppInfo();
				tmpInfo = getPackageInfo(context, packageInfo.packageName);
				appList.add(tmpInfo);
			}
		}
		return appList;
	}
	/**
	 * 获取安装应用的详细信息
	 * 
	 * @param packageName
	 *            安装应用的包名
	 * @return AppInfo
	 */
	public static AppInfo getPackageInfo(Context context, String packageName) {
		AppInfo packages = new AppInfo();
		PackageInfo packageInfo = new PackageInfo();
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
			return null;
		}
		if (packageInfo != null) {
			packages.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
			packages.setPackageName(packageInfo.packageName);
			packages.setVersionName(packageInfo.versionName);
			packages.setVersionCode(packageInfo.versionCode);
			packages.setAppIcon(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
		} else {
			packages = null;
		}
		return packages;
	}
	/**
     * 检测某程序是否安装
     */
    public static boolean isInstalledApp(Context context, String packageName)
    {

        try
        {
            PackageManager pm = context.getPackageManager();
            List< PackageInfo> pkgs = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (PackageInfo pkg : pkgs)
            {
                // 当找到了名字和该包名相同的时候，返回
                if ((pkg.packageName).equals(packageName))
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        return false;
    }
    
    /**
     * 安装.apk文件
     *
     * @param context
     */
    public void install(Context context, String fileName)
    {
        if (TextUtils.isEmpty(fileName) || context == null)
        {
            return;
        }
        try
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
 
    /**
     * 安装.apk文件
     *
     * @param context
     */
    public void install(Context context, File file)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取手机及SIM卡相关信息
     * @param context
     * @return
     */
    public static Map<String,String> getPhoneInfo(Context context) {
    	Map<String,String> map = new HashMap<String,String>();
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
		/**
		 * 返回电话状态
		 *
		 * CALL_STATE_IDLE 无任何状态时
		 * CALL_STATE_OFFHOOK 接起电话时
		 * CALL_STATE_RINGING 电话进来时
		 */
		int sate=tm.getCallState();
		/**
		 * 返回当前移动终端的唯一标识
		 *
		 * 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
		 */
        String imei = tm.getDeviceId();
		//返回用户唯一标识，比如GSM网络的IMSI编号
        String imsi = tm.getSubscriberId();
        String phoneMode = android.os.Build.MODEL;
        String phoneSDk = android.os.Build.VERSION.RELEASE;
        map.put("imei", imei);
        map.put("imsi", imsi);
        map.put("phoneMode", phoneMode+"##"+phoneSDk);
        map.put("model", phoneMode);
        map.put("sdk", phoneSDk);
        return map;
    }
    
	/**
	 * app详细信息，包括应用名称，包名，版本号，图标等
	 * */

	static class AppInfo {

		private String appName = "";// 应用名称
		private String packageName = "";// 应用包名
		private String versionName = "";// 版本名称
		private int versionCode = 0;// 版本ID
		private Drawable appIcon = null;// 应用图标

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public Drawable getAppIcon() {
			return appIcon;
		}

		public void setAppIcon(Drawable appIcon) {
			this.appIcon = appIcon;
		}

	}
}
