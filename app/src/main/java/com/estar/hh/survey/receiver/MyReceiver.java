package com.estar.hh.survey.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.estar.hh.survey.utils.LogUtils;
import com.google.gson.Gson;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送消息接收者
 * @author wuhan
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "wl";
	private Gson gson=new Gson();
//	private NotifyMessage notify=new NotifyMessage();
	private Bundle bundle;		
	private SharedPreferences prefer,nofityShare;
	private Intent inten;

	@Override
	public void onReceive(final Context context, Intent intent) {
		nofityShare=context.getSharedPreferences("username", Context.MODE_PRIVATE);
		String phoneNumber=nofityShare.getString("telephone", "");
		
		bundle=intent.getExtras();
		String notifyStr = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String title = bundle.getString(JPushInterface.EXTRA_TITLE);//标题
		String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);//json报文
		LogUtils.i("JPushExample:", "notifyStr------------------------:" + notifyStr);
		LogUtils.i("JPushExample:", "title----------------------------:" + title);
		LogUtils.i("JPushExample:", "jsonStr--------------------------:" + jsonStr);
		if (TextUtils.isEmpty(notifyStr)) {
			return;
		}


		try {
//			notify=gson.fromJson(notifyStr, NotifyMessage.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			LogUtils.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			//send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			LogUtils.i(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			prefer=context.getSharedPreferences("bundle", Context.MODE_PRIVATE);
			Editor editor=prefer.edit();
			editor.putString("notify", bundle.getString(JPushInterface.EXTRA_MESSAGE));
			editor.commit();

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			LogUtils.i(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			LogUtils.i(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
			 
			nofityShare=context.getSharedPreferences(phoneNumber, Context.MODE_PRIVATE);
			int i=nofityShare.getInt("number", 0);
			LogUtils.i("nf", "接收器前推送消息数目---->"+i);
			i++;
			LogUtils.i("nf", "接收器中推送消息数目---->"+i);
			Editor edit=nofityShare.edit();
			edit.putInt("number", i);
			edit.commit();
			LogUtils.i("nf", "接收器后推送消息数目---->"+i);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {

		} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {

		} else {

		}
	}


}
