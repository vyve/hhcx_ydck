package com.etop.DL;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DLCardAPI {
	static {
		System.loadLibrary("AndroidDLCard");
	}
	
	
	public native int DLKernalInit(String szSysPath,String FilePath,String CommpanyName,int nProductType,int nAultType,TelephonyManager telephonyManager,Context context);
	public native void DLKernalUnInit();
	public native int DLRecognizeNV21(byte[] ImageStreamNV21, int Width, int Height, char[] Buffer, int BufferLen);
	public native int DLRecognizePhoto(byte[] ImageStreamNV21, int nLen);
	public native int DLRecognizeImageFileW(String filepath);
	public native String DLGetResult(int nIndex);
	public native int DLDetectLine(byte[] streamnv21, int cols, int raws, int []LineX,int[]LineY);
}
