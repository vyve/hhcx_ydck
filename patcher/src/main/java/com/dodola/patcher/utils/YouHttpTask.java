package com.dodola.patcher.utils;


import android.content.Context;
import android.os.AsyncTask;


/**
 * 加强版的开始线程的操作（网络判断）
 * @author Administrator
 *
 * @param <Q> 调用execute(Q)方法出入的参数类型
 * @param <T> 得出的结果result的类型
 */
public abstract class YouHttpTask<Q, T> extends AsyncTask<Q, Void, T> {
	
	private Context context;
	
	/**
	 * 加强版的开始线程的操作（网络判断）
	 * @author Administrator
	 *
	 * @param <Q> 调用execute(Q)方法出入的参数类型
	 * @param <T> 得出的结果result的类型
	 */
	public final AsyncTask<Q, Void, T> executeProxy(Q... params) {
		// 判断网络的状态
		if (NetUtils.checkNetWork(context)) {
			return super.execute(params);
		} else {
			PromptManager.showNoNetWork(context);
			return null;
		}
	}
	
	/**
	 * 必须先调用此方法设置context.
	 */
	public void setContext(Context context){
		this.context=context;
	}
	protected void onPostExecute(T result) {
		
	}
	
}