package com.estarview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * 自定义组合
 */
public class MyCheckBox extends LinearLayout implements OnClickListener{
	private int mSpacing;
	private int mTextSize;
	private int mTextColor;
	private String mText;
	private boolean mChecked;
	
	private CheckBoxView mCheckBox;
	private TextView mTextView;
	
	private WeakReference<OnMyCheckBoxClickListener> mListener;
	
	public MyCheckBox(Context context) {
		super(context);
	}

	public MyCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.mycheckbox);  
		mChecked = a.getBoolean(R.styleable.mycheckbox_checked, false);
		mTextSize = a.getInt(R.styleable.mycheckbox_ctext_size, 14);
		mTextColor = a.getColor(R.styleable.mycheckbox_ctext_color, 0XFF333333); 
		mText = a.getString(R.styleable.mycheckbox_ctext);
		mSpacing = a.getInt(R.styleable.mycheckbox_spacing, 24);
		a.recycle();
		
		LayoutInflater.from(context).inflate(R.layout.widget_my_checkbox, this, true);
		mCheckBox = this.findViewById(R.id.my_checkbox);
//		mCheckBox.setSelected(mChecked);
		mCheckBox.setChecked(mChecked);
		
		mTextView = this.findViewById(R.id.my_checkbox_text);
		if(null != mText){
	        mTextView.setText(mText);
//	        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//	        		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
//	        		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//	        lp.setMargins(mSpacing, 0, 0, 0);
//	        mTextView.setLayoutParams(lp);
//		mTextView.setTextSize(dpToPx(mTextSize,getResources()));
	        mTextView.setTextColor(mTextColor);
	        mTextView.setVisibility(View.VISIBLE);
		}
		
		View v = this.findViewById(R.id.my_checkbox_linearLayout);
//		v.setOnClickListener(this);
		mTextView.setOnClickListener(this);
		
		mCheckBox.setOncheckListener(new CheckBoxView.OnCheckListener() {
            @Override
            public void onCheck(CheckBoxView view, boolean check) {
                if(null != mListener && null != mListener.get()){
					mListener.get().onMyCheckBoxClick(check);
					mChecked = check;
				}
            }
        });
//		mTextView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mCheckBox.setChecked(!mChecked);
//			}
//		});
	}

	@Override
	public void onClick(View v) {
//		mCheckBox.setSelected(!mChecked);
		mCheckBox.setChecked(!mChecked);
		
		mChecked = !mChecked;
		if(null != mListener && null != mListener.get()){
			mListener.get().onMyCheckBoxClick(mChecked);
		}
	}
	
	public void setOnMyCheckBoxClickListener(OnMyCheckBoxClickListener listener) {
		mListener = new WeakReference<OnMyCheckBoxClickListener>(listener);
	}
	
	public interface OnMyCheckBoxClickListener {
        void onMyCheckBoxClick(boolean v);
    }

	public void setChecked(boolean isAllCheck) {
//		mCheckBox.setSelected(isAllCheck);
		mCheckBox.setChecked(isAllCheck);
		this.mChecked=isAllCheck;
	}

	public boolean isChecked() {
//		mChecked = mCheckBox.isSelected();
		mChecked = mCheckBox.isCheck();
		return mChecked;
	}

	public String getmText() {
		return mText;
	}
	/**
	 * Convert Dp to Pixel
	 */
	public static int dpToPx(float dp, Resources resources){
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
		return (int) px;
	}

	public void setText(String str){
		mText=str;
		mTextView.setText(mText);
	}
}
