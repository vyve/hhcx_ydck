package com.estarview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * 右边带眼睛的输入框
 */
public class EyeEditText extends EditText {
	private Drawable dRight;
	private Rect rBounds;
	private Drawable eye_nomal,eye_checked;

	public EyeEditText(Context paramContext) {
		super(paramContext);
		setTag("hide");//隐藏密码
		initEditText();
	}

	public EyeEditText(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		setTag("hide");//隐藏密码
		initEditText();
	}

	public EyeEditText(Context paramContext, AttributeSet paramAttributeSet,
					   int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		setTag("hide");//隐藏密码
		initEditText();
	}

	/**
	 * 初始化 
	 * 添加addTextChangedListener 可以添加多个addTextChangedListener
	 */
	private void initEditText() {
		setEditTextDrawable();
		addTextChangedListener(new TextWatcher() { // 对文本内容改变进行监听
			@Override
			public void afterTextChanged(Editable paramEditable) {
			}

			@Override
			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
			}

			@Override
			public void onTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
				setEditTextDrawable();
			}
		});
	}

	/**
	 * 监听，如果没有数据了则清除图片，否则添加上图片
	 */
	public void setEditTextDrawable() {
		if (getText().toString().length() == 0) {
			setCompoundDrawables(null, null, null, null);
		} else {
			setCompoundDrawables(null, null, this.dRight, null);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.dRight = null;
		this.rBounds = null;

	}

	/**
	 * 添加触摸事件 点击之后 出现 清空editText的效果
	 */
	@Override
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
//		if ((this.dRight != null) && (paramMotionEvent.getAction() == 1)) {
//			this.rBounds = this.dRight.getBounds();
//			int i = (int) paramMotionEvent.getRawX();
//			if (i > getRight() - 3 * this.rBounds.width()) {
////				setText("");
//
//
//				if("hide".equals(getTag().toString())){
////					setCompoundDrawables(Drawable left,Drawable top,Drawable right,Drawable bottom)
////					eye_checked=getResources().getDrawable(R.mipmap.eye_checked);
////					setCompoundDrawables(null,null,eye_checked,null);
//					setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//					setTag("show");//显示密码
//				}else{
//					setTag("hide");//隐藏密码
////					 eye_nomal=getResources().getDrawable(com.estarview.R.mipmap.eye_nomal);
////					setCompoundDrawables(null,null,eye_nomal,null);
//					setTransformationMethod(PasswordTransformationMethod.getInstance());
//				}
//
//				paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
//			}
//		}

		if (dRight != null && paramMotionEvent.getAction() == MotionEvent.ACTION_UP) {
			int eventX = (int) paramMotionEvent.getRawX();
			int eventY = (int) paramMotionEvent.getRawY();
			Rect rect = new Rect();
			getGlobalVisibleRect(rect);
			rect.left = rect.right - 50;
			if(rect.contains(eventX, eventY))
				if("hide".equals(getTag().toString())){
//					setCompoundDrawables(Drawable left,Drawable top,Drawable right,Drawable bottom)
//					eye_checked=getResources().getDrawable(R.mipmap.eye_checked);
//					setCompoundDrawables(null,null,eye_checked,null);
//					if (null!=eye_checked){
//						dRight=eye_checked;
//						setEditTextDrawable();
////						setCompoundDrawables(null, null, this.eye_checked, null);
//					}
					setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					setTag("show");//显示密码
				}else{
//					if (null!=eye_nomal){
//						dRight=eye_nomal;
//						setEditTextDrawable();
////						setCompoundDrawables(null, null, this.eye_nomal, null);
//					}
					setTag("hide");//隐藏密码
//					 eye_nomal=getResources().getDrawable(com.estarview.R.mipmap.eye_nomal);
//					setCompoundDrawables(null,null,eye_nomal,null);
					setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
		}
		return super.onTouchEvent(paramMotionEvent);
	}

	/**
	 * 显示右侧清楚图片
	 * 左上右下
	 */
	@Override
	public void setCompoundDrawables(Drawable paramDrawable1,
			Drawable paramDrawable2, Drawable paramDrawable3,
			Drawable paramDrawable4) {
		if (paramDrawable3 != null)
			this.dRight = paramDrawable3;
		super.setCompoundDrawables(paramDrawable1, paramDrawable2,
				paramDrawable3, paramDrawable4);
	}
}