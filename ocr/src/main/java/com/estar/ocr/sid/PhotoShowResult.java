package com.estar.ocr.sid;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estar.ocr.R;

public class PhotoShowResult extends Activity {
	private ImageButton back;
	private ImageButton ok_show;
	private TextView  resultEdit;
	private String strResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // ����
		setContentView(R.layout.activity_sid_show_result);

		Intent intent = getIntent();
		strResult = intent.getStringExtra("StringR");
		findView();
 
	}

	@Override
	protected void onStop() {
		super.onStop();
	}


	private void findView() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;
		back = findViewById(R.id.back_showR);
		ok_show = findViewById(R.id.OK_show);
		resultEdit = findViewById(R.id.Result);
		resultEdit.setText(strResult);
		int back_w = (int) (height * 0.106796875);
		int back_h = back_w * 1;
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layoutParams.topMargin = (int) (height * 0.90734375);
		layoutParams.leftMargin = (int) (height * 0.054296875);
		back.setLayoutParams(layoutParams);
		int ok_show_h = (int) (height * 0.106796875);
		int ok_show_w = ok_show_h * 2;
		
		layoutParams = new RelativeLayout.LayoutParams(ok_show_w, ok_show_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		layoutParams.topMargin = (int) (height * 0.90734375);
		ok_show.setLayoutParams(layoutParams);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentBack = new Intent(PhotoShowResult.this, SIDOcrActivity2.class);
				startActivity(intentBack);
				finish();
			}
		});
		ok_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intentBack = new Intent(PhotoShowResult.this, SIDOcrActivity2.class);
				startActivity(intentBack);
				finish();
					}
		});
	}

}
