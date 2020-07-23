package com.estar.ocr.vin.vincode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.estar.ocr.MainActivity;
import com.estar.ocr.R;
import com.estar.ocr.backcard.utils.ActivityList;

public class VinShowResult extends Activity {
	private static final int resultBitmapOfW = 400;
	private static final int resultBitmapOfH = 80;

	private ImageButton back;
	private ImageButton ok_show;
	private ImageView word_show;
	private ImageView showbitmap;
	private EditText num1_show;
	private EditText num2_show;
	private EditText num3_show;
	private EditText num4_show;
	private EditText num5_show;
	//private TextView surplusTimes;
	private LinearLayout lin_edit;

	private String placeActivity;
	private int VINAPP;
	private int numTimes;
	private String CountStrs;
	private int editTextSize;
	private String disResult = "";
	private int[] resultBitmapArray;
	private String bitmapPath;
	private int[] bitmapCut = new int[4];
	private Bitmap ResultBitmap;
	private int success=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 竖屏
		setContentView(R.layout.activity_show_vin_result);

		Intent intent = getIntent();
		resultBitmapArray = intent.getIntArrayExtra("PicR");
		char[] results = intent.getCharArrayExtra("StringR");
		success = intent.getIntExtra("Success", 0);
		VINAPP = intent.getIntExtra("VINAPP", -1);
		placeActivity = intent.getStringExtra("Action");
		CountStrs = intent.getStringExtra("CountStrs");
		findView(success);

		if (success == 2) {
			if (results != null) {
				String resultS = String.valueOf(results);
				String[] temp = null;
                temp = resultS.split(" ");
				HiddenView(temp.length, temp);
			}
			if (resultBitmapArray != null) {
				ResultBitmap = Bitmap.createBitmap(resultBitmapArray, resultBitmapOfW, resultBitmapOfH,
						Config.ARGB_8888);
				showbitmap.setImageBitmap(ResultBitmap);
			}
		}
		if	(success ==3){
			bitmapPath = intent.getStringExtra("Path");
			bitmapCut[0]=intent.getIntExtra("l", 0);
			bitmapCut[1]=intent.getIntExtra("t", 0);
			bitmapCut[2]=intent.getIntExtra("w", 0);
			bitmapCut[3]=intent.getIntExtra("h", 0);
			String[] temp = null;
			HiddenView(0, temp);
			Bitmap TmpBitmap = BitmapFactory.decodeFile(bitmapPath);
//			ResultBitmap = Bitmap.createBitmap(TmpBitmap,bitmapCut[0],bitmapCut[1],bitmapCut[2],bitmapCut[3]);
			showbitmap.setImageBitmap(TmpBitmap);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (ResultBitmap != null) {
			ResultBitmap.recycle();
			ResultBitmap = null;
		}
	}


	private void findView(final int success) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;
		boolean isFatty = false;
		if (height * 3 == width * 4) {
			isFatty = true;
		}
		back = findViewById(R.id.back_showR);
		word_show = findViewById(R.id.word_show);
		showbitmap = findViewById(R.id.showbitmap);
		num1_show = findViewById(R.id.num1_show);
		num2_show = findViewById(R.id.num2_show);
		num3_show = findViewById(R.id.num3_show);
		num4_show = findViewById(R.id.num4_show);
		num5_show = findViewById(R.id.num5_show);
		lin_edit = findViewById(R.id.lin_edit);
		ok_show = findViewById(R.id.OK_show);
		int back_w = (int) (height * 0.066796875);
		int back_h = back_w * 1;
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layoutParams.topMargin = (int) (height * 0.07734375);
		layoutParams.leftMargin = (int) (height * 0.054296875);
		back.setLayoutParams(layoutParams);
		editTextSize = back_h*10 / 22;
		int word_show_w = (int) (height * 0.479609375);
		int word_show_h = (int) (word_show_w * 0.08213820078226857887874837027379);
		layoutParams = new RelativeLayout.LayoutParams(word_show_w, word_show_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layoutParams.topMargin = (int) (height * 0.1921875);
		layoutParams.leftMargin = (int) (height * 0.047265625);
		word_show.setLayoutParams(layoutParams);
		layoutParams = new RelativeLayout.LayoutParams(word_show_w, word_show_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layoutParams.topMargin = (int) (height * 0.1921875);
		layoutParams.leftMargin = (int) (height * 0.047265625);
		word_show.setLayoutParams(layoutParams);
		num1_show.setBackgroundResource(R.drawable.edit_bg);
		num1_show.setTextColor(Color.BLACK);

		int showbitmap_w = width * 1;
		int showbitmap_h = (int) (showbitmap_w * 0.2);
		layoutParams = new RelativeLayout.LayoutParams(showbitmap_w, showbitmap_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layoutParams.topMargin = (int) (height * 0.23828125);
		showbitmap.setLayoutParams(layoutParams);
		int lin_edit_w = width * 1;
		layoutParams = new RelativeLayout.LayoutParams(lin_edit_w, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		if (isFatty)
			layoutParams.topMargin = (int) (height * 0.4875);
		else
			layoutParams.topMargin = (int) (height * 0.3875);
		lin_edit.setLayoutParams(layoutParams);

		int ok_show_w = (int) (width * 0.81041666666666666666666666666667);
		int ok_show_h = (int) (ok_show_w * 0.1533847472150814053127677806341);
		layoutParams = new RelativeLayout.LayoutParams(ok_show_w, ok_show_h);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		layoutParams.topMargin = (int) (height * 0.63375);
		ok_show.setLayoutParams(layoutParams);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentBack = new Intent("etop.vin.scan_camera");//Intent(VinShowResult.this, VinScanCamera.class);
				intentBack.putExtra("VINAPP", VINAPP);
				intentBack.putExtra("Action", placeActivity);
				startActivity(intentBack);
				finish();
			}
		});
		ok_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (VINAPP == 10011) {
					Intent intent = new Intent(VinShowResult.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					if (placeActivity != null && !placeActivity.equals("")) {
						if (success == 2 && !CountStrs.equals("isFree")) {
						}
					}
					Intent intent = new Intent(placeActivity);
					intent.putExtra("BankCardNumber", disResult); // 识别结果
					if (success == 2) {
						intent.putExtra("RecogSuccess", true);
						intent.putExtra("ResultBitmapArray", resultBitmapArray);
						intent.putExtra("ResultBitmapOfW", resultBitmapOfW);
						intent.putExtra("ResultBitmapOfH", resultBitmapOfH);
					}
					startActivity(intent);
					ActivityList.closeActivity();
					finish();
				}
			}
		});
	}

	private void HiddenView(int num, String[] s) {
		if(success== 2)
		{
			String $s = s[0];
			char[] ch = $s.toCharArray();
			char n = ' ';
			char[] $ch = { ch[0], ch[1], ch[2], ch[3], n, ch[4], ch[5], ch[6], ch[7], n, ch[8], ch[9], ch[10], ch[11],
					n, ch[12], ch[13], ch[14], ch[15], n, ch[16], ch[17], ch[18] };
			String resultS = String.valueOf($ch);
			String[] temp = null;
			temp = resultS.split(" ");
			num1_show.setText(temp[0]);
			num2_show.setText(temp[1]);
			num3_show.setText(temp[2]);
			num4_show.setText(temp[3]);
			num5_show.setText(temp[4]);
			num1_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
			num2_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
			num3_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
			num4_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
			num5_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
			num1_show.setTypeface(Typeface.DEFAULT_BOLD);
			num2_show.setTypeface(Typeface.DEFAULT_BOLD);
			num3_show.setTypeface(Typeface.DEFAULT_BOLD);
			num4_show.setTypeface(Typeface.DEFAULT_BOLD);
			num5_show.setTypeface(Typeface.DEFAULT_BOLD);
			num1_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(temp[0].length()) });
			num2_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(temp[1].length()) });
			num3_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(temp[2].length()) });
			num4_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(temp[3].length()) });
			num5_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(temp[4].length()) });
			num1_show.setTextColor(Color.BLACK);
			num2_show.setTextColor(Color.BLACK);
			num3_show.setTextColor(Color.BLACK);
			num4_show.setTextColor(Color.BLACK);
			num5_show.setTextColor(Color.BLACK);
			num1_show.setBackgroundResource(R.drawable.edit_bg);
			num2_show.setBackgroundResource(R.drawable.edit_bg);
			num3_show.setBackgroundResource(R.drawable.edit_bg);
			num4_show.setBackgroundResource(R.drawable.edit_bg);
			num5_show.setBackgroundResource(R.drawable.edit_bg);
			num1_show.setPadding(20, 0, 20, 0);
			num2_show.setPadding(20, 0, 20, 0);
			num3_show.setPadding(20, 0, 20, 0);
			num4_show.setPadding(20, 0, 20, 0);
			num5_show.setPadding(20, 0, 20, 0);
			for (int i = 0; i < temp.length; i++) {
				disResult += temp[i];
			}
		}
		if(success ==3){
			num1_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
			num2_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
			num3_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
			num4_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
			num5_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
			num1_show.setTypeface(Typeface.DEFAULT_BOLD);
			num2_show.setTypeface(Typeface.DEFAULT_BOLD);
			num3_show.setTypeface(Typeface.DEFAULT_BOLD);
			num4_show.setTypeface(Typeface.DEFAULT_BOLD);
			num5_show.setTypeface(Typeface.DEFAULT_BOLD);
			num1_show.setTextColor(Color.BLACK);
			num2_show.setTextColor(Color.BLACK);
			num3_show.setTextColor(Color.BLACK);
			num4_show.setTextColor(Color.BLACK);
			num5_show.setTextColor(Color.BLACK);
			num1_show.setBackgroundResource(R.drawable.edit_bg);
			num2_show.setBackgroundResource(R.drawable.edit_bg);
			num3_show.setBackgroundResource(R.drawable.edit_bg);
			num4_show.setBackgroundResource(R.drawable.edit_bg);
			num5_show.setBackgroundResource(R.drawable.edit_bg);
			num1_show.setPadding(20, 0, 20, 0);
			num2_show.setPadding(20, 0, 20, 0);
			num3_show.setPadding(20, 0, 20, 0);
			num4_show.setPadding(20, 0, 20, 0);
			num5_show.setPadding(20, 0, 20, 0);
		}

	}

	TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (num2_show.getVisibility() == View.INVISIBLE && num3_show.getVisibility() == View.INVISIBLE
					&& num4_show.getVisibility() == View.INVISIBLE && num5_show.getVisibility() == View.INVISIBLE) {
			}
			{
				if (start >= 18) {
					String string = s.toString();
					String str = string.replaceAll("\\s*", "");
//					setBankInfo(str);
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (num2_show.getVisibility() == View.VISIBLE) {
				String cardNum = num1_show.getText().toString() + num2_show.getText().toString();
//				setBankInfo(cardNum);
			} else if (num3_show.getVisibility() == View.VISIBLE) {
				String cardNum = num1_show.getText().toString() + num2_show.getText().toString()
						+ num3_show.getText().toString();
//				setBankInfo(cardNum);
			} else if (num4_show.getVisibility() == View.VISIBLE) {
				String cardNum = num1_show.getText().toString() + num2_show.getText().toString()
						+ num3_show.getText().toString() + num4_show.getText().toString();
//				setBankInfo(cardNum);
			} else if (num5_show.getVisibility() == View.VISIBLE) {
				String cardNum = num1_show.getText().toString() + num2_show.getText().toString()
						+ num3_show.getText().toString() + num4_show.getText().toString()
						+ num5_show.getText().toString();
//				setBankInfo(cardNum);
			}
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(VinShowResult.this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		return super.dispatchKeyEvent(event);
	}

}
