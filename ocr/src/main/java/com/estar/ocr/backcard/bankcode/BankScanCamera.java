package com.estar.ocr.backcard.bankcode;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

//import com.wintone.bankcard.BankCardAPI;
import com.estar.ocr.BaseActivitity;
import com.estar.ocr.R;
import com.estar.ocr.backcard.utils.Utils;
import com.estar.ocr.backcard.view.BankViewfinderView;
import com.etop.BankCard.BankCardAPI;
//import com.etop.db.SqliteHelperUtils;
//import com.wintone.lisence.Common;

import android.R.bool;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.DisplayMetrics;
//import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BankScanCamera extends BaseActivitity implements SurfaceHolder.Callback, Camera.PreviewCallback {
	private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
	private Camera camera;
	private SurfaceView surfaceView;
	private RelativeLayout re_c;
	private SurfaceHolder surfaceHolder;
	private ImageButton back;
	private ImageButton flash;
	//private ImageButton eject_btn;
	//private ImageButton tackPic;
	private ImageView help_word;
	//private ImageView wintone_logo;
	private BankViewfinderView myView;

	private BankCardAPI   api;
	private Bitmap bitmap;
	private int preWidth = 0;
	private int preHeight = 0;
	private boolean isROI = false;
	private int width;
	private int height;
	private TimerTask timer;
	private Vibrator mVibrator;
	private ToneGenerator tone;
	private byte[] tackData;
	private TelephonyManager telephonyManager;
	private int BankAPP;
	private String placeActivity;
	private String countStrs;
	private boolean isFatty = false;
	private int[] m_ROI={0,0,0,0};
	private boolean  bInitKernal=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			copyDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
		Configuration cf= this.getResources().getConfiguration(); //获取设置的配置信息
		int noriention=cf.orientation;
		if(noriention== Configuration.ORIENTATION_LANDSCAPE)
		{
			if(!bInitKernal)
			{
				api =new BankCardAPI();
				String FilePath =Environment.getExternalStorageDirectory().toString()+"/9AF68E014442E5AA58EF.lic";
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				int nRet = api.ScanStart("",FilePath,"9AF68E014442E5AA58EF",0x04,0x02,telephonyManager,this);
				if(nRet!=0)
				{
					Toast.makeText(getApplicationContext(), "激活失败"+nRet, Toast.LENGTH_SHORT).show();
					System.out.print("nRet="+nRet);
				}
				else
				{
					//Toast.makeText(getApplicationContext(), "初始化成功", Toast.LENGTH_SHORT).show();
					bInitKernal=true;
				}
			}
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		// // 屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_scan_camera);

		Intent intent = getIntent();
		BankAPP = intent.getIntExtra("BankAPP", -1);
		placeActivity = intent.getStringExtra("Action");
		findView();

	}

	@Override
	protected void onRestart() {
//		if (tackPic != null && tackPic.getVisibility() != View.GONE) {
//			tackPic.setVisibility(View.GONE);
//			//eject_btn.setVisibility(View.VISIBLE);
//		}
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		super.onRestart();
	}

	@Override
	protected void onResume() {

		super.onResume();


	}

	private void findView() {

		surfaceView = findViewById(R.id.surfaceViwe);
		re_c = findViewById(R.id.re_c);
		back = findViewById(R.id.back_camera);
		flash = findViewById(R.id.flash_camera);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels; // 屏幕宽度（像素）
		height = metric.heightPixels; // 屏幕高度（像素）
		if (width * 3 == height * 4) {
			isFatty = true;
		}

		int back_w = (int) (width * 0.066796875);
		int back_h = back_w * 1;
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		int Fheight = height;
		if (isFatty)
			Fheight = (int) (height * 0.75);
		layoutParams.leftMargin = (int) (((width - Fheight * 0.06 * 1.585)  - back_h));
		layoutParams.bottomMargin = (int) (height * 0.10486111111111111111111111111111);
		back.setLayoutParams(layoutParams);

		int flash_w = (int) (width * 0.066796875);
		int flash_h = flash_w * 1;
		layoutParams = new RelativeLayout.LayoutParams(flash_w, flash_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		if (isFatty)
			Fheight = (int) (height * 0.75);
		layoutParams.leftMargin = (int) (((width - Fheight * 0.06 * 1.585)  - back_h));
		layoutParams.topMargin = (int) (height * 0.10486111111111111111111111111111);
		flash.setLayoutParams(layoutParams);


		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(BankScanCamera.this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				api.ScanEnd();
				finish();
			}
		});
		flash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
					String mess = getResources().getString(R.string.toast_flash);
					Toast.makeText(BankScanCamera.this, mess, Toast.LENGTH_LONG).show();
				} else {
					if (camera != null) {
						Camera.Parameters parameters = camera.getParameters();
						String flashMode = parameters.getFlashMode();
						if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
							parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
							parameters.setExposureCompensation(0);
						} else {
							parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 闪光灯常亮
							parameters.setExposureCompensation(-1);
						}
						try {
							camera.setParameters(parameters);
						} catch (Exception e) {
							String mess = getResources().getString(R.string.toast_flash);
							Toast.makeText(BankScanCamera.this, mess, Toast.LENGTH_LONG).show();
						}
						camera.startPreview();
					}
				}
			}
		});
		findViewById(R.id.select).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				openSelectPic();
			}
		});
	}

	private ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			try {
				if (tone == null) {
					tone = new ToneGenerator(1, ToneGenerator.MIN_VOLUME);
				}
				tone.startTone(ToneGenerator.TONE_PROP_BEEP);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};


	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		if (camera == null) {
			try {
				camera = Camera.open();
			} catch (Exception e) {
				e.printStackTrace();
				String mess = getResources().getString(R.string.toast_camera);
				Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_LONG).show();
				return;
			}
		}
		try {
			/**
			 * 禁止打开相机时在此崩溃,TODO
			 */
			camera.setPreviewDisplay(holder);
			initCamera(holder);
			Timer time = new Timer();
			if (timer == null) {
				timer = new TimerTask() {
					public void run() {
						// isSuccess=false;
						if (camera != null) {
							try {
								camera.autoFocus(new AutoFocusCallback() {
									public void onAutoFocus(boolean success, Camera camera) {
										// isSuccess=success;
									}
								});
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
			}
			time.schedule(timer, 500, 2500);

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {
		if (camera != null) {
			camera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					if (success) {
						synchronized (camera) {
							new Thread() {
								public void run() {
									initCamera(holder);
									super.run();
								}
							}.start();
						}
						// camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
					}
				}
			});
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
//		try {
//			if (camera != null) {
//				camera.setPreviewCallback(null);
//				camera.stopPreview();
//				camera.release();
//				camera = null;
//			}
//		} catch (Exception e) {
//		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
				try {
			if (camera != null) {
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				if (camera != null) {
					camera.setPreviewCallback(null);
					camera.stopPreview();
					camera.release();
					camera = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			api.ScanEnd();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@TargetApi(14)
	private void initCamera(SurfaceHolder holder) {
		Camera.Parameters parameters = camera.getParameters();
		List<Camera.Size> list = parameters.getSupportedPreviewSizes();
		Camera.Size size;
		int length = list.size();
		int previewWidth = 640;
		int previewheight = 480;
		int second_previewWidth = 0;
		int second_previewheight = 0;
		if (length == 1) {
			size = list.get(0);
			previewWidth = size.width;
			previewheight = size.height;
		} else {
			for (int i = 0; i < length; i++) {
				size = list.get(i);
				if (isFatty) {
					if (size.height <= 960 || size.width <= 1280) {
						second_previewWidth = size.width;
						second_previewheight = size.height;

						if (previewWidth <= second_previewWidth && second_previewWidth * 3 == second_previewheight * 4) {
							previewWidth = second_previewWidth;
							previewheight = second_previewheight;
						}
					}
				} else {
					if (size.height <= 960 || size.width <= 1280) {
						second_previewWidth = size.width;
						second_previewheight = size.height;
						if (previewWidth <= second_previewWidth&&second_previewWidth <= width && second_previewheight <= height) {
							previewWidth = second_previewWidth;
							previewheight = second_previewheight;
						}
					}
				}
			}
		}
		preWidth = previewWidth;
		preHeight = previewheight;
		if (!isROI) {
			int $t = height / 10;
			int t = $t;
			int b = height - t;
			int $l = (int) ((b - t) * 1.58577);
			int l = (width - $l) / 2;
			int r = width - l;
			if (isFatty) {
				$t = height / 5;
				t = $t;
				b = height - t;
				$l = (int) ((b - t) * 1.58577);
				l = (width - $l) / 2;
				r = width - l;
			}
			double proportion = (double) width / (double) preWidth;
			double hproportion=(double)height/(double)  preHeight;
			l = (int) (l /proportion);
			t = (int) (t /hproportion);
			r = (int) (r /proportion);
			b = (int) (b / hproportion);
			m_ROI[0]=l;
			m_ROI[1]=t;
			m_ROI[2]=r;
			m_ROI[3]=b;
			api.SetRegion(l, t, r, b);
			isROI = true;
			if (isFatty)
				myView = new BankViewfinderView(this, width, height, isFatty);
			else
				myView = new BankViewfinderView(this, width, height);
			re_c.addView(myView);
		}
		parameters.setPictureFormat(PixelFormat.JPEG);

		parameters.setPreviewSize(preWidth,preHeight);
		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		}
		camera.setPreviewCallback(this);
		camera.setParameters(parameters);
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.startPreview();
	}

	public String savePicture(Bitmap bitmap, String tag) {
		String strCaptureFilePath = PATH + tag + "_Bank_" + pictureName() + ".jpg";
		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(strCaptureFilePath);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();

		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "图片存储失败,请检查SD卡", Toast.LENGTH_SHORT).show();
		}
		return strCaptureFilePath;
	}

	public String pictureName() {
		String str = "";
		Time t = new Time();
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month + 1;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;
		if (month < 10)
			str = year + "0" + month;
		else {
			str = String.valueOf(year) + month;
		}
		if (date < 10)
			str = str + "0" + date + "_";
		else {
			str = str + date + "_";
		}
		if (hour < 10)
			str = str + "0" + hour;
		else {
			str = str + hour;
		}
		if (minute < 10)
			str = str + "0" + minute;
		else {
			str = str + minute;
		}
		if (second < 10)
			str = str + "0" + second;
		else {
			str = str + second;
		}
		return str;
	}

	private PictureCallback picturecallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if (camera != null) {
				camera.setPreviewCallback(null);
			}
			Camera.Parameters parameters = camera.getParameters();
			int pW = parameters.getPreviewSize().width;
			int pH = parameters.getPreviewSize().height;
			if (tackData != null) {
				if (camera != null) {
					camera.setPreviewCallback(null);
				}
				int[] datas = Utils.convertYUV420_NV21toARGB8888(tackData, pW, pH);

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				bitmap = Bitmap.createBitmap(datas, pW, pH, android.graphics.Bitmap.Config.ARGB_8888);
			}

			if (bitmap != null) {
				int $t = pH / 10;
				int ntmp = pH*3 / 10;
				int t = ntmp;
				int b = pH - ntmp;
				int $l = (int) ((pH-$t-$t) * 1.585);
				int l = (pW - $l) / 2;
				int r = pW - l;
				l = l + 30;
				t = t + 19;
				r = r - 30;
				b = b - 19;
				if (isFatty) {
					$t = pH / 5;
					ntmp = pH*2 / 5;
					t = ntmp;
					b = pH - t;
					$l = (int) ((pH - $t-$t) * 1.585);
					l = (pW - $l) / 2;
					r = pW - l;
				}
				double proportion = (double) pW / (double) preWidth;
				double hproportion=(double)pH/(double)  preHeight;
				l = (int) (l /proportion);
				t = (int) (t /hproportion);
				r = (int) (r /proportion);
				b = (int) (b / hproportion);
				Bitmap tmpbitmap = Bitmap.createBitmap(bitmap, l, t, r-l, b-t);
				String path = savePicture(tmpbitmap, "E");
				tackData = null;
				String flashMode = parameters.getFlashMode();
				if (flashMode != null && flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
					camera.startPreview();
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					camera.setParameters(parameters);
					camera.stopPreview();
				}

				Intent intent = new Intent(BankScanCamera.this, bankShowResult.class);
				intent.putExtra("Success", 3);
				intent.putExtra("BankAPP", BankAPP);
				intent.putExtra("Action", placeActivity);
				intent.putExtra("CountStrs", countStrs);
				intent.putExtra("Path", path);
				intent.putExtra("l", l);
				intent.putExtra("t", t);
				intent.putExtra("w", r - l);
				intent.putExtra("h", b - t);
				startActivity(intent);
				finish();
				// 只有打开预览时设置闪光灯才管用
				if (!bitmap.isRecycled()) {
					bitmap.recycle();
				}

			}
		}
	};

	@Override
	public void show(byte[] data) {

		super.show(data);
		tackData = data;
		Camera.Parameters parameters = camera.getParameters();
		counter=2;
		if (counter == 2) {
			counter = 0;
			int buffl = 30;
			char[] recogval = new char[buffl];
			int[] line =new int[4];
			line[0]=0;
			line[1]=0;
			line[2]=0;
			line[3]=0;
			int[] pLineWarp = new int[32000];
			int r = api.ScanStreamNV21(data, parameters.getPreviewSize().width, parameters.getPreviewSize().height,line,recogval,pLineWarp);
			if (line[0] == 1) {
				if (myView != null) {
					myView.setLeftLine(1);
				}
			} else {
				if (myView != null) {
					myView.setLeftLine(0);
				}
			}
			if (line[1] == 1) {
				if (myView != null) {
					myView.setTopLine(1);
				}
			} else {
				if (myView != null) {
					myView.setTopLine(0);
				}
			}
			if (line[2] == 1) {
				if (myView != null) {
					myView.setRightLine(1);
				}
			} else {
				if (myView != null) {
					myView.setRightLine(0);
				}
			}
			if (line[3] == 1) {
				if (myView != null) {
					myView.setBottomLine(1);
				}
			} else {
				if (myView != null) {
					myView.setBottomLine(0);
				}
			}
			if (r == 0) {
				camera.stopPreview();
				//api.WTUnInitCardKernal();

				// 震动
				mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				mVibrator.vibrate(100);
				// 删除正常识别保存图片功能
				int[] datas = Utils.convertYUV420_NV21toARGB8888(tackData, parameters.getPreviewSize().width,
						parameters.getPreviewSize().height);

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				bitmap = Bitmap.createBitmap(datas, parameters.getPreviewSize().width,
						parameters.getPreviewSize().height, android.graphics.Bitmap.Config.ARGB_8888);
				Bitmap tmpbitmap = Bitmap.createBitmap(bitmap, m_ROI[0], m_ROI[1], m_ROI[2]-m_ROI[0], m_ROI[3]-m_ROI[1]);
				savePicture(bitmap,"M");
				savePicture(tmpbitmap, "V");
				api.ScanEnd();
				Intent intent = new Intent(BankScanCamera.this, bankShowResult.class);
				intent.putExtra("PicR", pLineWarp);
				intent.putExtra("StringR", recogval);
				intent.putExtra("Success", 2);
				intent.putExtra("BankAPP", BankAPP);
				intent.putExtra("Action", placeActivity);
				intent.putExtra("CountStrs", countStrs);
				startActivity(intent);
				finish();
				camera.setPreviewCallback(null);
				camera = null;
			}
			if (r == 1) {
				Toast.makeText(this,"识别失败"+r,Toast.LENGTH_SHORT).show();
				counterCut++;
				if (counterCut == 5) {
					counterCut = 0;
				}

			}
		}
	}

	private int counter = 0;
	private int counterCut = 0;

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		tackData = data;
		Camera.Parameters parameters = camera.getParameters();
		counter++;
		if (counter == 2) {
			counter = 0;
			int buffl = 30;
			char[] recogval = new char[buffl];
			int[] line =new int[4];
			line[0]=0;
			line[1]=0;
			line[2]=0;
			line[3]=0;
			int[] pLineWarp = new int[32000];
			int r = api.ScanStreamNV21(data, parameters.getPreviewSize().width, parameters.getPreviewSize().height,line,recogval,pLineWarp);
			if (line[0] == 1) {
				if (myView != null) {
					myView.setLeftLine(1);
				}
			} else {
				if (myView != null) {
					myView.setLeftLine(0);
				}
			}
			if (line[1] == 1) {
				if (myView != null) {
					myView.setTopLine(1);
				}
			} else {
				if (myView != null) {
					myView.setTopLine(0);
				}
			}
			if (line[2] == 1) {
				if (myView != null) {
					myView.setRightLine(1);
				}
			} else {
				if (myView != null) {
					myView.setRightLine(0);
				}
			}
			if (line[3] == 1) {
				if (myView != null) {
					myView.setBottomLine(1);
				}
			} else {
				if (myView != null) {
					myView.setBottomLine(0);
				}
			}
			if (r == 0) {
				camera.stopPreview();
				//api.WTUnInitCardKernal();

				// 震动
				mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				mVibrator.vibrate(100);
				// 删除正常识别保存图片功能
				int[] datas = Utils.convertYUV420_NV21toARGB8888(tackData, parameters.getPreviewSize().width,
						parameters.getPreviewSize().height);

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				bitmap = Bitmap.createBitmap(datas, parameters.getPreviewSize().width,
						parameters.getPreviewSize().height, android.graphics.Bitmap.Config.ARGB_8888);
				Bitmap tmpbitmap = Bitmap.createBitmap(bitmap, m_ROI[0], m_ROI[1], m_ROI[2]-m_ROI[0], m_ROI[3]-m_ROI[1]);
				savePicture(bitmap,"M");
				savePicture(tmpbitmap, "V");
				api.ScanEnd();
				Intent intent = new Intent(BankScanCamera.this, bankShowResult.class);
				intent.putExtra("PicR", pLineWarp);
				intent.putExtra("StringR", recogval);
				intent.putExtra("Success", 2);
				intent.putExtra("BankAPP", BankAPP);
				intent.putExtra("Action", placeActivity);
				intent.putExtra("CountStrs", countStrs);
				startActivity(intent);
				finish();
				camera.setPreviewCallback(null);
				camera = null;
			}
			if (r == 1) {
				counterCut++;
				if (counterCut == 5) {
					counterCut = 0;
				}

			}
		}
	}

	@Override
	protected void onStop() {

		super.onStop();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
//		try {
//			if (camera != null) {
//				camera.setPreviewCallback(null);
//				camera.stopPreview();
//				camera.release();
//				camera = null;
//			}
//		} catch (Exception e) {
//		}
	}

	private void isFocusTakePicture(Camera camera) {
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
			camera.stopPreview();
			camera.startPreview();
			camera.takePicture(shutterCallback, null, picturecallback);
		} else {
			camera.autoFocus(new AutoFocusCallback() {
				public void onAutoFocus(boolean success, Camera camera) {
					if (success)
						camera.stopPreview();
					camera.startPreview();
					camera.takePicture(shutterCallback, null, picturecallback);
					// camera.unlock();
				}
			});
		}
	}

	private boolean isZh() {
		Locale locale = getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		return language.endsWith("zh");
	}

	@TargetApi(14)
	private void NewApis(Camera.Parameters parameters) {
		if (Build.VERSION.SDK_INT >= 14) {
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		}
	}

	public void copyDataBase() throws IOException {
		//  Common common = new Common();
		String dst = Environment.getExternalStorageDirectory().toString() + "/9AF68E014442E5AA58EF.lic";

		File file = new File(dst);
		if (!file.exists()) {
			// file.createNewFile();
		} else {
			file.delete();
		}

		try {
			InputStream myInput = getAssets().open("9AF68E014442E5AA58EF.lic");
			OutputStream myOutput = new FileOutputStream(dst);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {
		}
	}

}
