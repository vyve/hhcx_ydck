package com.estar.hh.survey.view.component.video;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;

import java.text.DecimalFormat;

/**
 * 统计当前这一秒的上传，下载流量
 * 
 * @author mabo
 * @time 2015-05-13
 * 
 */
public class MyTrafficStatistics {
	/**
	 * 默认3秒一次调用
	 */
	private int intervalTime = 3;
	/**
	 * 是否继续统计
	 */
	private boolean isBoolean = false;
	private ITrafficStatistics iTrafficStatistics;
	/**
	 * 临时上传流量
	 */
	double tempUpData = 0;
	/**
	 * 临时下载流量
	 */
	double tempDownData = 0;

	/**
	 * 当前APP uID
	 */
	private int uID;
	/**
	 * 第一次不要显示
	 */
	private boolean showBoolean =false;
	
	public MyTrafficStatistics(Activity activity) {
	
		try {
			PackageManager pm = activity.getPackageManager();
			ApplicationInfo ai = pm.getApplicationInfo(
					activity.getPackageName(), PackageManager.GET_ACTIVITIES);
			uID = ai.uid;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void setITrafficStatistics(ITrafficStatistics iTrafficStatistics){
		this.iTrafficStatistics = iTrafficStatistics;
	}
	public interface ITrafficStatistics {
		void result(String upData, String downData);
	}

	/**
	 * 开始统计
	 * @param intervalTime
	 */
	public void start(int intervalTime) {
		this.intervalTime = intervalTime;
		isBoolean = true;
		new MyTrafficStatisticsThread().start();
	}

	/**
	 * 结束统计
	 */
	public void stop() {
		isBoolean = false;
	}

	class MyTrafficStatisticsThread extends Thread {
		public void run() {
			DecimalFormat df = new DecimalFormat("0.00");
			int tempIntervalTime=intervalTime/1000;
			while (isBoolean) {
				if(TrafficStats.getUidRxBytes(uID)==-1){
					if (null != iTrafficStatistics)
						iTrafficStatistics.result("-1","-1");
					isBoolean=false;
					return;
				}
				double downData = TrafficStats.getUidRxBytes(uID) / 1024.00;
				double upData = TrafficStats.getUidTxBytes(uID) / 1024.00;
				try {
					//计算即时流量 回调界面
					if (showBoolean&&null != iTrafficStatistics)
						iTrafficStatistics.result(df.format((upData - tempUpData)/tempIntervalTime), df.format((downData
								- tempDownData)/tempIntervalTime));
					Thread.sleep(intervalTime);
					tempUpData = upData;
					tempDownData = downData;
					showBoolean=true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
