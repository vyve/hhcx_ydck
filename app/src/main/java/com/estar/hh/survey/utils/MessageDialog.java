package com.estar.hh.survey.utils;


import android.content.Context;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnLeftClickL;
import com.flyco.dialog.listener.OnBtnRightClickL;
import com.flyco.dialog.widget.MaterialDialog;

public class MessageDialog {
	private BaseAnimatorSet bas_in;
	private BaseAnimatorSet bas_out;
	private MaterialDialog dialog ;

	public	interface SubmitOnClick{
		void onSubmitOnClickSure();
		void onSubmitOnClickCancel();
	}
	/**
	 * 
	 * @param context 上下文 	
	 * @param theme 主题
	 * @param submitOnClick 提交按钮事件
	 * @param titleStr  标题
	 * @param messageBodyStr  标题内容
	 * @param submitStr 提交按钮事件
	 * @param exitStr 取消按钮事件
	 */
	public MessageDialog(Context context, int theme, final SubmitOnClick submitOnClick, String titleStr, String messageBodyStr, String submitStr, String exitStr) {
		bas_in= new FlipVerticalSwingEnter();
		bas_out= new FadeExit();
		dialog = new MaterialDialog(context);
		dialog.content(messageBodyStr)//
		.title(titleStr)
		.btnText(exitStr, submitStr)//
		.showAnim(bas_in)//
//		.dismissAnim(bas_out)
		.show();

		//        dialog.setCanceledOnTouchOutside(false);//获取所有焦点
		dialog.setOnBtnLeftClickL(new OnBtnLeftClickL() {
			@Override
			public void onBtnLeftClick() {
				if(dialog!=null)dialog.dismiss();
				if(submitOnClick!=null)submitOnClick.onSubmitOnClickCancel();
			}
		});

		dialog.setOnBtnRightClickL(new OnBtnRightClickL() {
			@Override
			public void onBtnRightClick() {
				if(dialog!=null)dialog.dismiss();
				if(submitOnClick!=null)submitOnClick.onSubmitOnClickSure();
			}
		});

	}



}
