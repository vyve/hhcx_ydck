package com.estar.ocr.backcard.bankcode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Locale;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.estar.ocr.MainActivity;
import com.estar.ocr.R;
import com.estar.ocr.backcard.utils.ActivityList;
import com.estar.ocr.backcard.view.BankCardEditTextWatcher;

public class bankShowResult extends Activity {
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
	private TextView surplusTimes;
	private LinearLayout lin_edit;

	private String placeActivity;
	private int BankAPP;
	private int numTimes;
//	private String CountStrs;
	private int editTextSize;
	private String disResult = "";
	private int[] resultBitmapArray;
	private String bitmapPath;
	private int[] bitmapCut = new int[4];
	private Bitmap ResultBitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 竖屏
		setContentView(R.layout.activity_show_result);

		Intent intent = getIntent();
		resultBitmapArray = intent.getIntArrayExtra("PicR");
		char[] results = intent.getCharArrayExtra("StringR");
		int success = intent.getIntExtra("Success", 0);
		BankAPP = intent.getIntExtra("BankAPP", -1);
		placeActivity = intent.getStringExtra("Action");
//		CountStrs = intent.getStringExtra("CountStrs");
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
		} else if (success == 3) {
			num1_show.addTextChangedListener(textWatcher);
			bitmapPath = intent.getStringExtra("Path");
			int l = intent.getIntExtra("l", -1);
			int t = intent.getIntExtra("t", -1);
			int w = intent.getIntExtra("w", -1);
			int h = intent.getIntExtra("h", -1);
			bitmapCut[0] = l;
			bitmapCut[1] = t;
			bitmapCut[2] = w;
			bitmapCut[3] = h;
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeFile(bitmapPath);
				bitmap = Bitmap.createBitmap(bitmap, l, t, w, h);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "图片存储失败,请检查SD卡", Toast.LENGTH_SHORT).show();
			}
			if (bitmap != null) {
				showbitmap.setImageBitmap(bitmap);
			}
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
		if (success == 3) {
			BankCardEditTextWatcher myNum1_show = new BankCardEditTextWatcher(num1_show);
			num1_show.addTextChangedListener(myNum1_show);
		}
		num2_show = findViewById(R.id.num2_show);
		num3_show = findViewById(R.id.num3_show);
		num4_show = findViewById(R.id.num4_show);
		num5_show = findViewById(R.id.num5_show);
		lin_edit = findViewById(R.id.lin_edit);
		surplusTimes = findViewById(R.id.surplusTimes);
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

		if (success == 2) {
			int word_show_w = (int) (height * 0.479609375);
			int word_show_h = (int) (word_show_w * 0.08213820078226857887874837027379);
			layoutParams = new RelativeLayout.LayoutParams(word_show_w, word_show_h);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			layoutParams.topMargin = (int) (height * 0.1921875);
			layoutParams.leftMargin = (int) (height * 0.047265625);
			word_show.setLayoutParams(layoutParams);
		} else if (success == 3) {
			//		word_show.setBackgroundResource(R.drawable.please_word);

			int word_show_w = (int) (height * 0.479609375);
			int word_show_h = (int) (word_show_w * 0.08213820078226857887874837027379);
			if (!isZh()) {
				word_show_w = (int) (height * 0.49609375);
				word_show_h = (int) (word_show_w * 0.05887850467289719626168224299065);
			}
			layoutParams = new RelativeLayout.LayoutParams(word_show_w, word_show_h);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			layoutParams.topMargin = (int) (height * 0.1921875);
			layoutParams.leftMargin = (int) (height * 0.047265625);
			word_show.setLayoutParams(layoutParams);
			num1_show.setBackgroundResource(R.drawable.edit_bg);
			num1_show.setTextColor(Color.BLACK);
		}
		if (success == 2) {

			int showbitmap_w = width * 1;
			int showbitmap_h = (int) (showbitmap_w * 0.2);
			layoutParams = new RelativeLayout.LayoutParams(showbitmap_w, showbitmap_h);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			layoutParams.topMargin = (int) (height * 0.23828125);
			showbitmap.setLayoutParams(layoutParams);
		} else if (success == 3) {

			int showbitmap_w = (int) (width * 0.83888888888888888888888888888889);
			int showbitmap_h = (int) (showbitmap_w / 1.58577);
			layoutParams = new RelativeLayout.LayoutParams(showbitmap_w, showbitmap_h);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			layoutParams.topMargin = (int) (height * 0.23828125);
			showbitmap.setLayoutParams(layoutParams);
		}
		if (success == 2) {
			int lin_edit_w = width * 1;
			layoutParams = new RelativeLayout.LayoutParams(lin_edit_w, RelativeLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			if (isFatty)
				layoutParams.topMargin = (int) (height * 0.4875);
			else
				layoutParams.topMargin = (int) (height * 0.3875);
			lin_edit.setLayoutParams(layoutParams);

		} else if (success == 3) {
			int lin_edit_w = (int) (width * 0.83888888888888888888888888888889);
			layoutParams = new RelativeLayout.LayoutParams(lin_edit_w, RelativeLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			layoutParams.addRule(RelativeLayout.BELOW, R.id.showbitmap);
			layoutParams.topMargin = (int) (height * 0.0234375);
			lin_edit.setLayoutParams(layoutParams);

			num2_show.setVisibility(View.GONE);
			num3_show.setVisibility(View.GONE);
			num4_show.setVisibility(View.GONE);
			num5_show.setVisibility(View.GONE);
			int num1_show_w = (int) (width * 0.83888888888888888888888888888889);
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(num1_show_w,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			num1_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(23) });
			num1_show.setGravity(Gravity.CENTER);
			num1_show.setKeyListener(new DigitsKeyListener(false, true));
			String mess = getResources().getString(R.string.hint_editText);
			num1_show.setHint(mess);
			num1_show.setLayoutParams(layoutParam);
		}


		if (success == 2) {
			int ok_show_w = (int) (width * 0.81041666666666666666666666666667);
			int ok_show_h = (int) (ok_show_w * 0.1533847472150814053127677806341);
			layoutParams = new RelativeLayout.LayoutParams(ok_show_w, ok_show_h);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			layoutParams.topMargin = (int) (height * 0.63375);
			ok_show.setLayoutParams(layoutParams);
		} else if (success == 3) {
			int ok_show_w = (int) (width * 0.81041666666666666666666666666667);
			int ok_show_h = (int) (ok_show_w * 0.1533847472150814053127677806341);
			layoutParams = new RelativeLayout.LayoutParams(ok_show_w, ok_show_h);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			layoutParams.topMargin = (int) (height * 0.656375);
			ok_show.setLayoutParams(layoutParams);
		}
		layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.BELOW, R.id.OK_show);
		layoutParams.topMargin = editTextSize / 3;
		surplusTimes.setLayoutParams(layoutParams);
		surplusTimes.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize * 2 / 3);

//		if (BankAPP == 10011 || "isFree".equals(CountStrs)) {
//			numTimes = 50;
//		} else {
//			numTimes = 50 - Integer.valueOf(CountStrs);
//		}

		if (isZh()) {
			String only = getResources().getString(R.string.only);
			String time = getResources().getString(R.string.time);
			surplusTimes.setText(only + numTimes + time);
		} else {
			String only = getResources().getString(R.string.only);
			String time = getResources().getString(R.string.time);
			String times = getResources().getString(R.string.times);
			String free = getResources().getString(R.string.free);
//			if (numTimes == 1 || numTimes == 0)
//				surplusTimes.setText(only + " " + numTimes + " " + time + " " + free);
//			else
//				surplusTimes.setText(only + " " + numTimes + " " + times + " " + free);

		}
		surplusTimes.setTextColor(Color.rgb(173, 173, 173));
		if (numTimes <= 9 && numTimes >= 0) {
			surplusTimes.setVisibility(View.VISIBLE);
			if (numTimes == 0) {
				ok_show.setVisibility(View.INVISIBLE);
			}
		} else {
			surplusTimes.setVisibility(View.INVISIBLE);

		}
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentBack = new Intent(bankShowResult.this, BankScanCamera.class);
				intentBack.putExtra("BankAPP", BankAPP);
				intentBack.putExtra("Action", placeActivity);
				startActivity(intentBack);
				finish();
			}
		});
		ok_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BankAPP == 10011) {
					Intent intent = new Intent(bankShowResult.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					if (placeActivity != null && !placeActivity.equals("")) {
//						if (success == 2 && !CountStrs.equals("isFree")) {
////							if (!CreateAuthFile(50 - numTimes + 1)) {
////								Toast.makeText(ShowResult.this, "校验授权文件失败,请检查SD卡", Toast.LENGTH_LONG).show();
////								return;
////							}
//						} else if (success == 3) {
//							String s = num1_show.getText().toString();
//							if (s != null && !s.equals("")) {
//								disResult = s;
//							}
//						}
						Intent intent = new Intent(placeActivity);
						intent.putExtra("BankCardNumber", disResult); // 识别结果
						if (success == 2) {
							intent.putExtra("RecogSuccess", true);
							intent.putExtra("ResultBitmapArray", resultBitmapArray);
							intent.putExtra("ResultBitmapOfW", resultBitmapOfW);
							intent.putExtra("ResultBitmapOfH", resultBitmapOfH);
						} else if (success == 3) {
							intent.putExtra("RecogSuccess", false);
							intent.putExtra("BitmapPath", bitmapPath);
							intent.putExtra("BitmapCut", bitmapCut);
						}
						startActivity(intent);
						ActivityList.closeActivity();
						finish();
					}
				}
			}
		});
	}

	private void HiddenView(int num, String[] s) {
		switch (num) {
			case 1:
				setBankInfo(s[0]);
				String $s = s[0];
				char[] ch = $s.toCharArray();
				char n = ' ';
				char[] $ch = { ch[0], ch[1], ch[2], ch[3], n, ch[4], ch[5], ch[6], ch[7], n, ch[8], ch[9], ch[10], ch[11],
						n, ch[12], ch[13], ch[14], ch[15], n, ch[16], ch[17], ch[18] };
				String resultS = String.valueOf($ch);
				String[] temp = null;
				temp = resultS.split(" ");
				HiddenView(temp.length, temp);
				break;
			case 2:
				setBankInfo(s[0] + s[1]);
				num1_show.setText(s[0]);
				num2_show.setText(s[1]);
				num1_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
				num2_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
				num1_show.setTypeface(Typeface.DEFAULT_BOLD);
				num2_show.setTypeface(Typeface.DEFAULT_BOLD);
				num1_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[0].length()) });
				num2_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[1].length()) });
				num1_show.setTextColor(Color.BLACK);
				num2_show.setTextColor(Color.BLACK);
				num1_show.setBackgroundResource(R.drawable.edit_bg);
				num1_show.setPadding(20, 0, 20, 0);
				num2_show.setBackgroundResource(R.drawable.edit_bg);
				num2_show.setPadding(20, 0, 20, 0);
				num3_show.setVisibility(View.GONE);
				num4_show.setVisibility(View.GONE);
				num5_show.setVisibility(View.GONE);
				break;
			case 3:
				setBankInfo(s[0] + s[1] + s[2]);
				num1_show.setText(s[0]);
				num2_show.setText(s[1]);
				num3_show.setText(s[2]);
				num1_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
				num2_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
				num3_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
				num1_show.setTypeface(Typeface.DEFAULT_BOLD);
				num2_show.setTypeface(Typeface.DEFAULT_BOLD);
				num3_show.setTypeface(Typeface.DEFAULT_BOLD);
				num1_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[0].length()) });
				num2_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[1].length()) });
				num3_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[2].length()) });
				num1_show.setTextColor(Color.BLACK);
				num2_show.setTextColor(Color.BLACK);
				num3_show.setTextColor(Color.BLACK);
				num1_show.setBackgroundResource(R.drawable.edit_bg);
				num2_show.setBackgroundResource(R.drawable.edit_bg);
				num3_show.setBackgroundResource(R.drawable.edit_bg);
				num1_show.setPadding(20, 0, 20, 0);
				num2_show.setPadding(20, 0, 20, 0);
				num3_show.setPadding(20, 0, 20, 0);
				num4_show.setVisibility(View.GONE);
				num5_show.setVisibility(View.GONE);
				break;
			case 4:
				setBankInfo(s[0] + s[1] + s[2] + s[3]);
				num1_show.setText(s[0]);
				num2_show.setText(s[1]);
				num3_show.setText(s[2]);
				num4_show.setText(s[3]);
				num1_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
				num2_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
				num3_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
				num4_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
				num1_show.setTypeface(Typeface.DEFAULT_BOLD);
				num2_show.setTypeface(Typeface.DEFAULT_BOLD);
				num3_show.setTypeface(Typeface.DEFAULT_BOLD);
				num4_show.setTypeface(Typeface.DEFAULT_BOLD);
				num1_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[0].length()) });
				num2_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[1].length()) });
				num3_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[2].length()) });
				num4_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[3].length()) });
				num1_show.setTextColor(Color.BLACK);
				num2_show.setTextColor(Color.BLACK);
				num3_show.setTextColor(Color.BLACK);
				num4_show.setTextColor(Color.BLACK);
				num1_show.setBackgroundResource(R.drawable.edit_bg);
				num2_show.setBackgroundResource(R.drawable.edit_bg);
				num3_show.setBackgroundResource(R.drawable.edit_bg);
				num4_show.setBackgroundResource(R.drawable.edit_bg);
				num1_show.setPadding(20, 0, 20, 0);
				num2_show.setPadding(20, 0, 20, 0);
				num3_show.setPadding(20, 0, 20, 0);
				num4_show.setPadding(20, 0, 20, 0);
				num5_show.setVisibility(View.GONE);
				break;
			default:
				setBankInfo(s[0] + s[1] + s[2] + s[3] + s[4]);
				num1_show.setText(s[0]);
				num2_show.setText(s[1]);
				num3_show.setText(s[2]);
				num4_show.setText(s[3]);
				num5_show.setText(s[4]);
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
				num1_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[0].length()) });
				num2_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[1].length()) });
				num3_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[2].length()) });
				num4_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[3].length()) });
				num5_show.setFilters(new InputFilter[] { new InputFilter.LengthFilter(s[4].length()) });
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
				break;
		}
		for (int i = 0; i < s.length; i++) {
			disResult += s[i];
		}
	}

	// 根据传入的参数来创建wintone.lsc文件；返回true为创建授权文件成功；返回false为创建授权文件失败
	/*public Boolean CreateAuthFile(Object count) {
		String PATH = Environment.getExternalStorageDirectory().toString();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		String query_sql = "select * from wtbank_lsc where _id=1";
		String insert_sql = "insert into wtbank_lsc(_id,wt_content) values(?,?)";
		String update_sql = "update wtbank_lsc set wt_content=? where _id=?";
		// 黄震20141029 修改360删除授权文件的bug start

//		Common common = new Common();
		String SysCertVersion = "wtversion5_5";
		String resultString;
		FileOutputStream fos = null;
		String filePathString = PATH + "/AndroidWT/bankcard.lsc";
		File file = new File(filePathString);
		try {
			String s = telephonyManager.getDeviceId();
			resultString = common.getDesPassword(String.valueOf(count).toString() + "==" + s, SysCertVersion);
			fos = new FileOutputStream(file);
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append(resultString);
			fos.write(sBuffer.toString().getBytes());
			fos.close();
			// 黄震20141029 修改360删除授权文件的bug start
			// 查询数据库
			// System.out.println("查询数据库");
			SqliteHelperUtils sqliteHelperUtils = new SqliteHelperUtils(this, "wt_wisdom_bankcard.db", 2);
			Cursor cursor = sqliteHelperUtils.queryData(query_sql, null);
			if (cursor.getCount() > 0) {
				// 数据库中有数据就更新数据
				// System.out.println("更新数据库");
				Object[] update_param = { resultString, 1 };
				sqliteHelperUtils.executeData(update_sql, update_param);
			} else if (cursor.getCount() == 0) {
				// 如果数据库中没有数据，就插入数据
				// System.out.println("插入数据库");
				Object[] insert_param = { 1, resultString };
				sqliteHelperUtils.executeData(insert_sql, insert_param);
			}
			cursor.close();
			// 黄震20141029 修改360删除授权文件的bug end
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
*/
	private boolean isZh() {
		Locale locale = getResources().getConfiguration().locale;
		String language = locale.getLanguage();
        return language.endsWith("zh");
	}

	private void setBankInfo(String bankNum) {
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
					setBankInfo(str);
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
				setBankInfo(cardNum);
			} else if (num3_show.getVisibility() == View.VISIBLE) {
				String cardNum = num1_show.getText().toString() + num2_show.getText().toString()
						+ num3_show.getText().toString();
				setBankInfo(cardNum);
			} else if (num4_show.getVisibility() == View.VISIBLE) {
				String cardNum = num1_show.getText().toString() + num2_show.getText().toString()
						+ num3_show.getText().toString() + num4_show.getText().toString();
				setBankInfo(cardNum);
			} else if (num5_show.getVisibility() == View.VISIBLE) {
				String cardNum = num1_show.getText().toString() + num2_show.getText().toString()
						+ num3_show.getText().toString() + num4_show.getText().toString()
						+ num5_show.getText().toString();
				setBankInfo(cardNum);
			}
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(bankShowResult.this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		return super.dispatchKeyEvent(event);
	}

}
