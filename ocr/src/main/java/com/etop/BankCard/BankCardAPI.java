package com.etop.BankCard;

import android.content.Context;
import android.telephony.TelephonyManager;


/**
 * 	
 *  
 *	
 */
public class BankCardAPI {
	static {
		System.loadLibrary("AndroidCard");
	}
	
	
	public native int ScanStart(String szSysPath,String FilePath,String CommpanyName,int nProductType,int nAultType,TelephonyManager telephonyManager,Context context);
	public native void ScanEnd();
	public native void SetRegion(int left,int top,int right,int bottom);
	public native int ScanStreamNV21(byte[] streamnv21, int cols, int raws, int []Line,char[] cardno, int []wrapdata);
	public native int ScanImage(String imgpath,char[]cardno);
}
