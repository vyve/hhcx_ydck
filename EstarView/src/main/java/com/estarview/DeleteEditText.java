package com.estarview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * 右边带删除的输入框
 */
public class DeleteEditText extends EditText {
	private Drawable dRight,dLeft;
	private Rect rBounds;

	public DeleteEditText(Context paramContext) {
		super(paramContext);
		initEditText();
	}

	public DeleteEditText(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		initEditText();
	}

	public DeleteEditText(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
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
			setCompoundDrawables(this.dLeft, null, null, null);
		} else {
			setCompoundDrawables(this.dLeft, null, this.dRight, null);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.dRight = null;
		this.dLeft = null;
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
//				setText("");
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
				setText("");
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
		if (paramDrawable1 != null)
			this.dLeft = paramDrawable1;
		super.setCompoundDrawables(paramDrawable1, paramDrawable2,
				paramDrawable3, paramDrawable4);
	}



}