package com.etop.VL;

import android.content.Context;
import android.telephony.TelephonyManager;

public class VLCardAPI {
	static {
		System.loadLibrary("AndroidVLCard");
	}
	
	
	public native int VLKernalInit(String szSysPath,String FilePath,String CommpanyName,int nProductType,int nAultType,TelephonyManager telephonyManager,Context context);
	public native void VLKernalUnInit();
	public native int VLRecognizeNV21(byte[] ImageStreamNV21, int Width, int Height, char[] Buffer, int BufferLen);
	public native int VLRecognizePhoto(byte[] ImageStreamNV21, int nLen);
	public native int VLRecognizeImageFileW(String filepath);
    public native String VLGetResult(int nIndex);
	public native int VLDetectLine(byte[] streamnv21, int cols, int raws, int []LineX,int[]LineY);
	
}
