package com.estar.hh.survey.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 测试上下行速度、丢包率
 */
public class NetSpeed {

	/** 达标次数*/
	private long NETSPEED_NUMBER;
	/** 达标网速*/
	private long NETSPEED_UPOK;
	/** 计算时间段*/
	private long NETSPEED_ONE_TIME;
	/** 每次发送不符合网速信息的最短时间间隔*/
	private long NETSPEED_SEND_TIME;

	private long NUMBER=0;//记录次数
	private long UPOK=0;//达标网速
	private long OKORNO=0;//初始为0  1为达标  2为不达标
	private long TIME=0;//时间每次一秒

	private boolean START=true;


	FileUtils fileUtils =  new FileUtils();
	private final static String TAG = "NetSpeed";
	private long[] iniNetData = new long[16],preNetData= new long[16];
	private Timer mTimer = null;
	private static NetSpeed mNetSpeed;
	private Handler mHandler;

	private long preRxBytes = 0;
	private long preTxBytes = 0;
	private Context mContext;

	private CalculateNetSpeed calculateNetSpeed;
	/**
	 * 是否继续统计
	 */
	private boolean isBoolean = false;
	/**
	 * 默认1秒一次调用
	 */
	private int intervalTime = 1000;


	/**
	 *
	 * @param mContext
	 * @param mHandler
	 * @param NUMBER  ONE_TIME时间段内 达标次数
	 * @param UPOK    达标网速(KB)
	 * @param ONE_TIME  计算时间段（秒）
	 * @param SEND_TIME  每次发送不符合网速信息的最短时间间隔（秒）
	 */
	public NetSpeed(Context mContext, Handler mHandler, long NUMBER, long UPOK, long ONE_TIME, long SEND_TIME) {
		this.mContext = mContext;
		this.mHandler = mHandler;
		this.NETSPEED_NUMBER=NUMBER;
		this.NETSPEED_UPOK=UPOK;
		this.NETSPEED_ONE_TIME=ONE_TIME;
		this.NETSPEED_SEND_TIME=SEND_TIME;
		this.TIME=SEND_TIME;

	}

//	public static NetSpeed getInstant(Context mContext, Handler mHandler,long NUMBER,long UPOK,long TIME) {
//		if (mNetSpeed == null) {
//			mNetSpeed = new NetSpeed(mContext, mHandler);
//		}
//		return mNetSpeed;
//	}

	/**
	 * 开始统计
	 * @param intervalTime
	 */
	public void start(int intervalTime) {
		if(isBoolean){//防止重复开启
			return;
		}
		this.intervalTime = intervalTime;
		isBoolean = true;
		new CalculateNetSpeed().start();
	}
	/**
	 * 结束统计
	 */
	public void stop() {
		isBoolean = false;
	}

	class CalculateNetSpeed extends Thread {
		public void run() {
			iniNetData = getNetData();//启动时的网络数据
			System.arraycopy(iniNetData, 0, preNetData, 0, 10);
//			int tempIntervalTime = intervalTime / 1000;
			while (isBoolean) {
				try {
//					iniNetData = getNetData();//启动时的网络数据
					Thread.sleep(intervalTime);//暂停
					//根据上一次的网络数据进行计算
				} catch (Exception e) {
					e.printStackTrace();
					fileUtils.writeException(e);
				}
				long[] curNetData = getNetData();//当前的网络数据
				int downKb = (int) Math.floor((curNetData[0] - preNetData[0]) / 1024 + 0.5);//下行速度
				if (downKb < 0)
					downKb = 0;
				long downPackets = curNetData[1] - iniNetData[1];//下行正确的包量
				if (downPackets < 0)
					downPackets = 0;
				long downErrs = curNetData[2] - iniNetData[2];//下行错误的包量
				if (downErrs < 0)
					downErrs = 0;
				long downDrop = curNetData[3] - iniNetData[3];//下行丢弃的包量
				if (downDrop < 0)
					downDrop = 0;

				int upKb = (int) Math.floor((curNetData[8] - preNetData[8]) / 1024 + 0.5);//上行速度
				if (upKb < 0)
					upKb = 0;
				long upPackets = curNetData[9] - iniNetData[9];//上行正确的包量
				if (upPackets < 0)
					upPackets = 0;
				long upErrs = curNetData[10] - iniNetData[10];//上行错误的包量
				if (upErrs < 0)
					upErrs = 0;
				long upDrop = curNetData[11] - iniNetData[11];//上行丢弃的包量
				if (upDrop < 0)
					upDrop = 0;

				System.arraycopy(curNetData, 0, preNetData, 0, curNetData.length);

//					//另一种方式计算上下行网速
//					long	curRxNetSpeed = getRxNetSpeed();
//					long	curTxNetSpeed = getTxNetSpeed();
//

				String netDataMsg = "上行数据：\n" + "上行速度：" + upKb + "K/s；上行正确包：" + upPackets + "；上行错误包：" + upErrs + "；上行丢弃包：" + upDrop;
				netDataMsg = netDataMsg + "\n\n下行数据：\n" + "下行速度：" + downKb + "K/s；下行正确包：" + downPackets + "；下行错误包：" + downErrs + "；下行丢弃包：" + downDrop;


				++NUMBER;// 记录消息发送次数
				++TIME;//记录时间  每秒/次
				if (upKb > NETSPEED_UPOK) { //判断网速大于达标网速
					++UPOK;
				}

				while (NUMBER == NETSPEED_ONE_TIME) {
					if (UPOK < NETSPEED_NUMBER) {//判断达标网速小于达标网速的次数
						if (TIME > NETSPEED_SEND_TIME) {//与上次不达标网速发送时间间隔（第一次除外）
							OKORNO = 2;//初始为0  1为达标  2为不达标
							TIME = 0;//清零
						}
					} else {
						OKORNO = 1;//初始为0  1为达标  2为不达标
					}
					UPOK = 0;//清零
					NUMBER = 0;//清零
				}
				Message msg = new Message();
				msg.what = 5;
				Bundle bundle = new Bundle();
				bundle.putLong("upKb", upKb);//上行速度
				bundle.putLong("upPackets", upPackets);//上行正确包
				bundle.putLong("upErrs", upErrs);//上行错误包
//					bundle.putLong("upDrop",upDrop);//上行丢弃包
				bundle.putLong("upDrop", division((double) upDrop, (double) (upPackets + upErrs + upDrop)));//上行丢弃包率


				bundle.putLong("downKb", downKb);//下行速度
				bundle.putLong("downPackets", downPackets);//下行正确包
				bundle.putLong("downErrs", downErrs);//下行错误包
				bundle.putLong("downDrop", downDrop);//下行丢弃包
//					bundle.putLong("downDrop",downDrop);//下行丢弃包

				bundle.putLong("downDrop", division((double) downDrop, (double) (downPackets + downErrs + downDrop)));//下行丢弃包率

				bundle.putLong("OKORNO", OKORNO);//初始为0  1为达标  2为不达标
				msg.setData(bundle);//mes利用Bundle传递数据
				mHandler.sendMessage(msg);
				OKORNO = 0;//初始为0  1为达标  2为不达标
			}
		}
	}

	/**
	 * 开始计算网络速度相差数据
	 */
	public void startCalculateNetSpeed() {


		iniNetData = getNetData();//启动时的网络数据

//		for (int i =0 ;i<16;i++){
//			Log.d(TAG, "iniNetData[" +i + "]" + iniNetData[i]);
//		}
		System.arraycopy(iniNetData,0,preNetData,0,10);

//		preRxBytes = getNetworkRxBytes();
//		preTxBytes = getNetworkTxBytes();

		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimer == null) {
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					//根据上一次的网络数据进行计算
					long[] curNetData = getNetData();//当前的网络数据
					int downKb = (int) Math.floor((curNetData[0]-preNetData[0]) / 1024 + 0.5);//下行速度
					if (downKb < 0)
						downKb = 0;
					long downPackets = curNetData[1] - iniNetData[1];//下行正确的包量
					if (downPackets < 0)
						downPackets = 0;
					long downErrs =  curNetData[2] - iniNetData[2];//下行错误的包量
					if (downErrs < 0)
						downErrs = 0;
					long downDrop =  curNetData[3] - iniNetData[3];//下行丢弃的包量
					if (downDrop < 0)
						downDrop = 0;

					int upKb =  (int) Math.floor((curNetData[8]-preNetData[8]) / 1024 + 0.5);//上行速度
					if (upKb < 0)
						upKb = 0;
					long upPackets = curNetData[9] - iniNetData[9];//上行正确的包量
					if (upPackets < 0)
						upPackets = 0;
					long upErrs =  curNetData[10] - iniNetData[10];//上行错误的包量
					if (upErrs < 0)
						upErrs = 0;
					long upDrop =  curNetData[11] - iniNetData[11];//上行丢弃的包量
					if (upDrop < 0)
						upDrop = 0;

					System.arraycopy(curNetData,0,preNetData,0,curNetData.length);

//					//另一种方式计算上下行网速
//					long	curRxNetSpeed = getRxNetSpeed();
//					long	curTxNetSpeed = getTxNetSpeed();
//

					String netDataMsg = "上行数据：\n" + "上行速度：" + upKb  +"K/s；上行正确包：" + upPackets + "；上行错误包：" + upErrs + "；上行丢弃包：" + upDrop ;
					netDataMsg = netDataMsg +  "\n\n下行数据：\n" + "下行速度：" + downKb   +"K/s；下行正确包：" + downPackets + "；下行错误包：" + downErrs + "；下行丢弃包：" +downDrop ;




					++NUMBER;// 记录消息发送次数
					++TIME;//记录时间  每秒/次
					if(upKb>NETSPEED_UPOK){ //判断网速大于达标网速
						++UPOK;
					}

					while(NUMBER==NETSPEED_ONE_TIME){
						if(UPOK<NETSPEED_NUMBER){//判断达标网速小于达标网速的次数
							if(TIME>NETSPEED_SEND_TIME){//与上次不达标网速发送时间间隔（第一次除外）
								OKORNO=2;//初始为0  1为达标  2为不达标
								TIME=0;//清零
							}
						}else {
							OKORNO=1;//初始为0  1为达标  2为不达标
						}
						UPOK=0;//清零
						NUMBER=0;//清零
					}

					Message msg = new Message();
					msg.what = 5;
					Bundle bundle = new Bundle();
					bundle.putLong("upKb", upKb);//上行速度
					bundle.putLong("upPackets", upPackets);//上行正确包
					bundle.putLong("upErrs",upErrs );//上行错误包
//					bundle.putLong("upDrop",upDrop);//上行丢弃包
					bundle.putLong("upDrop", division((double) upDrop, (double) (upPackets + upErrs + upDrop)));//上行丢弃包率


					bundle.putLong("downKb", downKb);//下行速度
					bundle.putLong("downPackets", downPackets);//下行正确包
					bundle.putLong("downErrs",downErrs );//下行错误包
					bundle.putLong("downDrop",downDrop);//下行丢弃包
//					bundle.putLong("downDrop",downDrop);//下行丢弃包

					bundle.putLong("downDrop", division((double) downDrop, (double) (downPackets + downErrs + downDrop)));//下行丢弃包率

					bundle.putLong("OKORNO", OKORNO);//初始为0  1为达标  2为不达标
					msg.setData(bundle);//mes利用Bundle传递数据
					mHandler.sendMessage(msg);
					OKORNO=0;//初始为0  1为达标  2为不达标
				}
			}, 1000, 1000);
		}
	}

	private Long division(double a, double b){
		double resultDiv=0;
		long rst=0;
		BigDecimal b2= null;//四舍五入  保留两位小数
		try {
			if(b!=0){
				resultDiv=a/b;
			}else {
				resultDiv=0;
			}
			b2 = new BigDecimal(resultDiv);
//			rst=(long)b2.setScale(BigDecimal.ROUND_HALF_UP).doubleValue()*100;
			rst=(long)b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100;
			LogUtils.d(TAG, "a=" + a + "  b=" + b+"   resultDiv="+resultDiv+"    rst="+rst);
		} catch (Exception e) {
			e.printStackTrace();
			fileUtils.writeException(e);
		}


		return rst;
	}

	/**
	 * 停止计算
	 */
	public void stopCalculateNetSpeed() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	/**
	 * 获取网络数据
	 *  /proc/net/dev
	 *  Inter-| Receive | Transmitface |bytes packets errs drop fifo frame compressed multicast|bytes packets errs drop fifo colls carrier compressed
	 *  rmnet0: 1386 33 0 33 0 0 0 0 17664 120 0 0 0 0 0 0
	 *  最左边的表示接口的名字，Receive表示收包，Transmit表示发包；bytes表示收发的字节数；
	 *  packets表示收发正确的包量；errs表示收发错误的包量；drop表示收发丢弃的包量；
	 */
	private long[] getNetData(){
		String line;
		FileReader fr = null;
		BufferedReader in = null;
		long[] netData = new long[16];

		try {
			//初始化网络数据
			for (int i =0 ;i<16;i++){
				netData[i] = 0;
			}

			//读取文件
			fr = new FileReader("/proc/net/dev");
			in = new BufferedReader(fr, 500);

			//解释数据
			while ((line = in.readLine()) != null) {
				line = line.trim();
				int netDataIndex = -1;
				if (line.startsWith("rmnet") || line.startsWith("eth") || line.startsWith("wlan")) {
					int colonPos = line.indexOf(":");//冒号所处的位置
					String newLine = line.substring(colonPos+1).trim();//冒号后面的字符串
					String[] newLineArray = newLine.split(" ");
					for (int j =0;j<newLineArray.length;j++){
						if (!"".endsWith(newLineArray[j].trim())){
							netDataIndex ++;
							if (netDataIndex  < 16){
								netData[netDataIndex]  = netData[netDataIndex]  +  Long.parseLong(newLineArray[j]);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			fileUtils.writeException(e);
		}finally{
			try {
				if (in != null)
					in.close();
				if (fr != null)
					fr.close();
			} catch (IOException e2) {
				e2.printStackTrace();
				fileUtils.writeException(e2);
			}
		}

		return netData;
	}


	/***************----------以下为另外一种方法获取上下行速度----------***************/
//	private int getUid() {
//		try {
//			PackageManager pm = mContext.getPackageManager();
//			ApplicationInfo ai = pm.getApplicationInfo(
//					mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
//			return ai.uid;
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		return -1;
//	}
//
//	/**
//	 * 获取下行网速
//	 * @return
//	 */
//	private long getNetworkRxBytes() {
//		int currentUid = getUid();
//		//Log.d(TAG, "currentUid =" + currentUid);
//		if (currentUid < 0) {
//			return 0;
//		}
//		long rxBytes = TrafficStats.getUidRxBytes(currentUid);
//		/* 下句中if里的一般都为真，只能得到全部的网速 */
//		if (rxBytes == TrafficStats.UNSUPPORTED) {
//			//Log.d(TAG, "getUidRxBytes fail !!!");/* 本函数可以只用下面一句即可 */
//			rxBytes = TrafficStats.getTotalRxBytes();
//		}
//		return rxBytes;
//	}
//
//	/**
//	 * 获取上行网速
//	 * @return
//	 */
//	private long getNetworkTxBytes() {
//		int currentUid = getUid();
//		//Log.d(TAG, "currentUid =" + currentUid);
//		if (currentUid < 0) {
//			return 0;
//		}
//		long txBytes = TrafficStats.getUidTxBytes(currentUid);
//		/* 下句中if里的一般都为真，只能得到全部的网速 */
//		if (txBytes == TrafficStats.UNSUPPORTED) {
//			//Log.d(TAG, "getUidRxBytes fail !!!");/* 本函数可以只用下面一句即可 */
//			txBytes = TrafficStats.getTotalTxBytes();
//		}
//		return txBytes;
//	}
//	
//	public int getRxNetSpeed() {
//		long curRxBytes = getNetworkRxBytes();
//		long bytes = curRxBytes - preRxBytes;
//		preRxBytes = curRxBytes;
//		int kb = (int) Math.floor(bytes / 1024 + 0.5);
//		return kb;
//	}
//	
//	public int getTxNetSpeed() {
//		long curTxBytes = getNetworkTxBytes();
//		long bytes = curTxBytes - preTxBytes;
//		preTxBytes = curTxBytes;
//		int kb = (int) Math.floor(bytes / 1024 + 0.5);
//		return kb;
//	}

}
