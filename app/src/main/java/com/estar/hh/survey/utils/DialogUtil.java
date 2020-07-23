package com.estar.hh.survey.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;


public class DialogUtil {


	/**
	 * 得到自定义的progressDialog
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog show(Context context, String title, String msg) {

		
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog_video, null);// 得到加载view
		LinearLayout layout = v.findViewById(R.id.dialog_view);// 加载布局
		v.findViewById(R.id.progressBarCircularIndetermininate).setBackgroundColor(Color.parseColor("#6699CC"));
		// main.xml中的ImageView
		TextView tipTextView = v.findViewById(R.id.tipTextView);// 提示文字
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context,R.style.dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		loadingDialog.show();
		return loadingDialog;

	}
	
	
}
