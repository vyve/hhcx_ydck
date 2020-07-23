package com.estar.hh.survey.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 跟网络相关的工具类
 * 
 * @author zhy
 * 
 */
public class NetUtils
{
	private NetUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context)
	{

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null != connectivity)
		{

			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected())
			{
                return info.getState() == NetworkInfo.State.CONNECTED;
			}
		}
		return false;
	}

	/**
	 * 判断是否是wifi连接
	 */
	public static boolean isWifi(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm == null)
			return false;
		return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity)
	{
		Intent intent=null;
		//判断手机系统的版本  即API大于10 就是3.0或以上版本
		if(android.os.Build.VERSION.SDK_INT>10){
			intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		}else{
			intent = new Intent();
			ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}
		activity.startActivity(intent);
	}
	
	/**
     * 检测当前网络状态
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
		<uses-permission android:name="android.permission.INTERNET"/>	
     * @param context
     * @return
     */
    public void checkNetworkState(Context context){
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            //1.判断是否有网络连接 
            boolean networkAvailable = networkInfo.isAvailable();
            //2.获取当前网络连接的类型信息
            int networkType = networkInfo.getType();
            if(ConnectivityManager.TYPE_WIFI == networkType){
                //当前为wifi网络
            }else if(ConnectivityManager.TYPE_MOBILE == networkType){
                //当前为mobile网络
            }
        }
    }

}
