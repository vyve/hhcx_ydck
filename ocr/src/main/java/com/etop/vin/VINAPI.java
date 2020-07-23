package com.etop.vin;

import android.content.Context;
import android.telephony.TelephonyManager;

public class VINAPI {
	static {
		System.loadLibrary("AndroidVinKernal");
	}
	
	public native int VinKernalInit(String szSysPath,String FilePath,String CommpanyName,int nProductType,int nAultType,TelephonyManager telephonyManager,Context context);
	
	public native void VinKernalUnInit();

	public native void VinSetROI(int[] borders, int imgWidth, int imgHeight);
	
	
	
	public native int VinRecognizeMemory(int[] pImageBuffer, int nWidth, int nHeight, int nBitCount, char[] szBuffer, int BufferLen);
	
	public native int VinRecognizeNV21(byte[] ImageStreamNV21, int Width, int Height, char[] Buffer, int BufferLen);
	public native int VinRecognizeNV21Ex(byte[] ImageStreamNV21, int Width, int Height, char[] Buffer, int BufferLen,int []pLine);

    public native int VinSaveImage(String imagePath);
    public native String VinGetResult();
}
