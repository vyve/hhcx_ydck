package com.estarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RadioToggleButtonView extends RadioGroup implements
		CompoundButton.OnCheckedChangeListener {

	public static abstract class RadioToggleButtonAdapter {

		public abstract String[] getTitle();

		public int getCount() {
			return getTitle().length;
		}

	}

	private RadioToggleButtonAdapter adapter;
	private int textSize = 18;
	private int textColor = Color.parseColor("#B73304");
	private int padding;
	private boolean fristChecked = true;// 第一个是否默认选中，默认情况下选中

	public RadioToggleButtonView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public RadioToggleButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
		init();
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
		init();
	}

	@SuppressLint("ResourceAsColor")
	public void init() {
		this.setOrientation(LinearLayout.HORIZONTAL);
		padding = dipToPixels(getContext(), 5);
		this.removeAllViews();
		RadioButton radioBtn = null;
		for (int i = 0; adapter != null && i < adapter.getCount(); i++) {
			radioBtn = new RadioButton(getContext());
			/**
			 * 修改背景样式
			 * radio_left_bg 改为 radio_left_bg_re
			 * radio_right_bg 改为 radio_right_bg_re
			 * radio_center_bg 改为 radio_center_bg_re
			 */
			if (i == 0) {
				radioBtn.setBackgroundResource(R.drawable.radio_left_bg_re);
			} else if (i == adapter.getCount() - 1) {
				radioBtn.setBackgroundResource(R.drawable.radio_right_bg_re);
			} else {
				radioBtn.setBackgroundResource(R.drawable.radio_center_bg_re);
			}
			radioBtn.setButtonDrawable(android.R.color.transparent);
			radioBtn.setText(adapter.getTitle()[i]);
			radioBtn.setGravity(Gravity.CENTER);
			radioBtn.setTextSize(textSize);
			radioBtn.setTextColor(textColor);
			radioBtn.setId(i);
			radioBtn.setPadding(0, padding, 0, padding);
			radioBtn.setOnCheckedChangeListener(this);
			addView(radioBtn, getParams());
		}

		if (fristChecked && getChildCount() > 0) {
			setSelectPosition(0);
		}

	}

	public void setAdapter(RadioToggleButtonAdapter adapter) {

		this.adapter = adapter;
		init();
	}

	public void setAdapter(final String[] titles) {
		this.adapter = new RadioToggleButtonAdapter() {

			@Override
			public String[] getTitle() {
				// TODO Auto-generated method stub
				return titles;
			}
		};
		init();
	}

	public LayoutParams getParams() {
		LayoutParams params = new LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER;
		params.leftMargin = 0;
		params.rightMargin = 0;
//		params.leftMargin = dipToPixels(getContext() , 10);
//		params.rightMargin = dipToPixels(getContext() , 10);
		params.topMargin = dipToPixels(getContext() , 10);
		params.bottomMargin = dipToPixels(getContext() , 10);
		return params;
	}


	/**
	 * 获取当前选中按钮的下标位置
	 * 
	 * @return
	 */
	public int getSelectPosition() {
		return getCheckedRadioButtonId();
	}

	/**
	 * 设置当前选中的按钮
	 * 
	 * @param position
	 */
	public void setSelectPosition(int position) throws IndexOutOfBoundsException{
		if (position < getChildCount()) {
			((RadioButton) getChildAt(position)).setChecked(true);
		} else {
//			throw new IndexOutOfBoundsException("ToggleButton count = "
//					+ getChildCount() + "  position = " + position);
		}
	}

	/**
	 * 设置第一个按钮是否选中状态
	 * 
	 * @param isChecked
	 */
	public void setFristChecked(boolean isChecked) {
		fristChecked = isChecked;
		init();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			buttonView.setTextColor(Color.WHITE);
		}else{
//			buttonView.setTextColor(Color.parseColor("#C7000A"));
			buttonView.setTextColor(textColor);
		}
	}
	public  static int dipToPixels(Context context,int dip) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return (int) px;
	}
}
