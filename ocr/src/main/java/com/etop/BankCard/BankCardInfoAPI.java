package com.etop.BankCard;

import android.content.Context;
import android.telephony.TelephonyManager;


/**
 * 	
 *  
 *	
 */
public class BankCardInfoAPI {
	static {
		System.loadLibrary("AndroidCardInfo");
	}
	
	
	public native int InitCardInfo();
	public native int UninitCardInfo();
	public native int GetCardInfo(char[] cardno,String[] cardinfo);
}
