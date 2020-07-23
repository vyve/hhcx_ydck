package com.estar.utils;


import android.content.Context;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FlipEnter.FlipHorizontalSwingEnter;
import com.flyco.animation.FlipExit.FlipHorizontalExit;
import com.flyco.dialog.listener.OnBtnLeftClickL;
import com.flyco.dialog.listener.OnBtnRightClickL;
import com.flyco.dialog.widget.NormalDialog;

public class MessageDialog {

	private NormalDialog dialog;
	public	interface SubmitOnClick{
		void onSubmitOnClickSure();
		void onSubmitOnClickCancel();
	}
	/**
	 *
	 * @param context 上下文
	 * @param submitOnClick 提交按钮事件
	 * @param titleStr  标题
	 * @param messageBodyStr  标题内容
	 * @param submitStr 提交按钮事件
	 * @param exitStr 取消按钮事件
	 * @param dismissAnim  true  为取消退出动画
	 */
	public MessageDialog(Context context, final SubmitOnClick submitOnClick, String titleStr, String messageBodyStr,
						 String submitStr, String exitStr,boolean dismissAnim) {
		BaseAnimatorSet bas_in = new FlipHorizontalSwingEnter();//
		BaseAnimatorSet bas_out = new FlipHorizontalExit();//
		dialog = new NormalDialog(context);
		if(!dismissAnim){//true  为取消退出动画
			dialog.dismissAnim(bas_out);//
		}
		dialog.style(NormalDialog.STYLE_TWO)
				.titleTextSize(23)//
				.content(messageBodyStr)//
				.title(titleStr)
				.btnText(exitStr,submitStr)//
				.showAnim(bas_in)//
				.show();

		dialog.setCanceledOnTouchOutside(false);//获取所有焦点
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
