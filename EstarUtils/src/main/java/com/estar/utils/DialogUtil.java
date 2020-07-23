package com.estar.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estarutils.R;
import com.estarview.SVCircleProgressBar;


public class DialogUtil {

	/**
	 * 得到自定义的progressDialog
	 * @return
	 */
	public static Dialog show(Context context,String dialogTitle) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
		LinearLayout layout = v.findViewById(R.id.dialog_view);// 加载布局
		ImageView ivBigLoading = v.findViewById(R.id.ivBigLoading);
		ivBigLoading.setVisibility(View.VISIBLE);
		ImageView ivSmallLoading = v.findViewById(R.id.ivSmallLoading);
		TextView tvMsg = v.findViewById(R.id.tvMsg);
		tvMsg.setText(dialogTitle);
		SVCircleProgressBar pb = v.findViewById(R.id.circleProgressBar);
		Dialog loadingDialog = new Dialog(context, R.style.dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

		RotateAnimation mRotateAnimation = new RotateAnimation(0f, 359f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setDuration(1000L);
		mRotateAnimation.setInterpolator(new LinearInterpolator());
		mRotateAnimation.setRepeatCount(-1);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
		//开启旋转动画
		ivBigLoading.startAnimation(mRotateAnimation);
		loadingDialog.show();
		return loadingDialog;

	}


}
