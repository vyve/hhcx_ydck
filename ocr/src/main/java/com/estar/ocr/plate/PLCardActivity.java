package com.estar.ocr.plate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.estar.ocr.R;
import com.estar.ocr.common.ValueForKey;
import com.estar.ocr.common.camera.OcrVO;
import com.estar.ocr.dl.NewDLOcrActivity;
import com.etop.plate.PlateAPI;

import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.ShutterCallback;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PLCardActivity extends Activity   implements SurfaceHolder.Callback, Camera.PreviewCallback{

	//private Button photo_button = null;
	private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
	private Camera camera;
	private SurfaceView surfaceView;
	private RelativeLayout re_c;
	private SurfaceHolder surfaceHolder;
	private PlateAPI   api =null;
	private Bitmap bitmap;
	private int preWidth = 0;
	private int preHeight = 0;
	private int width;
	private int height;
	private TimerTask timer;
	private Timer timer2;
	private Vibrator mVibrator;
	private ToneGenerator tone;
	private byte[] tackData;
	private PLViewfinderView myView;
	private long recogTime;
	private boolean isFatty = false;
	private boolean  bInitKernal=false;
	AlertDialog alertDialog = null;
	private int[] m_ROI={0,0,0,0};
	private boolean isROI=false;
	private int rotation =-1;
	Toast toast;
	String FilePath="";
	private ImageButton back;
	private ImageButton flash;
	private boolean baddView =false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		try {
			copyDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int uiRot = getWindowManager().getDefaultDisplay().getRotation();// 获取屏幕旋转的角度
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int metricwidth = metric.widthPixels; // 屏幕宽度（像素）
		int metricheight = metric.heightPixels; // 屏幕高度（像素）

		if(metricwidth>=metricheight){
			//设备横屏状态时 屏幕旋转角度为1或者3时执行以下代码
			if(uiRot==1||uiRot==3){
				switch (uiRot)
				{
					case 1:
						rotation = 0;

						break;
					case 3:
						rotation = 180;

						break;
				}
			}else{
				////设备横屏状态时 屏幕旋转角度为2或者0时执行以下代码
				switch (uiRot)
				{
					case 0:
						rotation = 0;

						break;

					case 2:
						rotation = 180;

						break;

				}
			}
		}else if(metricheight>=metricwidth){
			//设备竖屏状态时 屏幕旋转角度为0或者2时执行以下代码
			if(uiRot==0||uiRot==2){

				switch (uiRot)
				{
					case 0:
						rotation = 90;

						break;

					case 2:
						rotation = 270;

						break;

				}
			}else{
				//设备竖屏状态时 屏幕旋转角度为1或者3时执行以下代码
				switch (uiRot)
				{
					case 1:
						rotation = 270;

						break;
					case 3:
						rotation = 90;

						break;
				}
			}
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		Configuration cf= this.getResources().getConfiguration(); //获取设置的配置信息
		int noriention=cf.orientation;
		if(alertDialog==null){
			alertDialog = new AlertDialog.Builder(this).create();
		}
		if(noriention== Configuration.ORIENTATION_PORTRAIT)
		{
			if(api==null)
			{
				api= new PlateAPI();
//				FilePath =Environment.getExternalStorageDirectory().toString()+"/9AF68E014442E5AA58EF.lic";
				FilePath =Environment.getExternalStorageDirectory().toString()+"/44EDFED05E5D2449FDD9.lic";
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				int nRet = api.ETInitPlateKernal("",FilePath,"44EDFED05E5D2449FDD9",0x06,0x02,telephonyManager,this);
				if(nRet!=0)
				{
					Toast.makeText(getApplicationContext(), "激活失败", Toast.LENGTH_SHORT).show();
					System.out.print("nRet="+nRet);
					bInitKernal =false;
				}
				else
				{
					System.out.print("nRet="+nRet);
					bInitKernal=true;
				}
			}
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		// // 屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.activity_car_card);
		findView();
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

	protected void onRestart() {
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
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels; // 屏幕宽度（像素）
		height = metric.heightPixels; // 屏幕高度（像素）
		if (width * 3 == height * 4) {
			isFatty = true;
		}
		back = findViewById(R.id.back);
		flash = findViewById(R.id.flash);

		int back_w = (int) (height * 0.066796875);
		int back_h = back_w * 1;
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layoutParams.leftMargin = (int) ((width *0.10));
		layoutParams.topMargin = (int) (height * 0.0486111111111111111111111111111);
		back.setLayoutParams(layoutParams);

		int flash_w = (int) (height * 0.066796875);
		int flash_h = flash_w * 1;
		layoutParams = new RelativeLayout.LayoutParams(flash_w, flash_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layoutParams.leftMargin = (int) (((width - width *0.150))-flash_w);
		layoutParams.topMargin = (int) (height * 0.0486111111111111111111111111111);
		flash.setLayoutParams(layoutParams);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (camera != null) {
						camera.setPreviewCallback(null);
						camera.stopPreview();
						camera.release();
						camera = null;
					}
				} catch (Exception e) {
				}
				if(toast!=null){
					toast.cancel();
					toast = null;
				}
				if(timer2!=null){
					timer2.cancel();
					timer2=null;
				}
				if(alertDialog!=null)
				{
					alertDialog.dismiss();
					alertDialog.cancel();
					alertDialog=null;
				}
				if(api!=null)
				{
					api.ETUnInitPlateKernal();
					api=null;
				}
				finish();
			}
		});
		flash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
					String mess = "当前设备不支持闪光灯";
					Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
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
							String mess = "当前设备不支持闪光灯";
							Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
						}
						camera.startPreview();
					}
				}
			}
		});

		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(PLCardActivity.this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
		if(api==null)
		{
			api= new PlateAPI();
			FilePath =Environment.getExternalStorageDirectory().toString()+"/44EDFED05E5D2449FDD9.lic";
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			int nRet = api.ETInitPlateKernal("",FilePath,"44EDFED05E5D2449FDD9",0x06,0x02,telephonyManager,this);
			if(nRet!=0)
			{
				Toast.makeText(getApplicationContext(), "激活失败", Toast.LENGTH_SHORT).show();
				System.out.print("nRet="+nRet);
				bInitKernal =false;
			}
			else
			{
				System.out.print("nRet="+nRet);
				bInitKernal=true;
			}
		}
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
						//isSuccess=false;
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

		if(alertDialog==null){
			alertDialog = new AlertDialog.Builder(this).create();
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
		try {
			if (camera != null) {
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		} catch (Exception e) {
		}
		if(toast!=null){
			toast.cancel();
			toast = null;
		}
		if(timer2!=null){
			timer2.cancel();
			timer2=null;
		}
		if(alertDialog!=null)
		{
			alertDialog.dismiss();
			alertDialog.cancel();
			alertDialog=null;
		}
		if(api!=null)
		{
			api.ETUnInitPlateKernal();
			api=null;
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
			if(api!=null)
			{
				api.ETUnInitPlateKernal();
				bInitKernal=false;
				api = null;
			}
			finish();
			if(toast!=null){
				toast.cancel();
				toast=null;
			}
			if(timer2!=null){
				timer2.cancel();
				timer2=null;
			}
			if(alertDialog!=null)
			{
				alertDialog.cancel();
				alertDialog=null;
			}
			// android.os.Process.killProcess(android.os.Process.myPid());
			// System.exit(0);
		}
		return super.onKeyDown(keyCode, event);
	}

	@TargetApi(14)
	private void initCamera(SurfaceHolder holder) {
		Camera.Parameters parameters = camera.getParameters();
		List<Camera.Size> list = parameters.getSupportedPreviewSizes();
		Camera.Size size;
		int length = list.size();
		int previewWidth = list.get(0).width;
		int previewheight = list.get(0).height;
		int second_previewWidth = 0;
		int second_previewheight = 0;
		int nlast = -1;
		int nThird =-1;
		int Third_previewWidth = 0;
		int Third_previewheight = 0;
		if (length == 1) {
			size = list.get(0);
			previewWidth = size.width;
			previewheight = size.height;
		} else {

			for (int i = 0; i < length; i++) {
				size = list.get(i);
				second_previewWidth = size.width;
				second_previewheight = size.height;
				if(rotation==90)
				{
					if((size.height==width||size.width==height) && nThird==-1)
					{
						if(size.height ==width &&size.width >=height)
						{
							Third_previewWidth  = size.width;
							Third_previewheight = size.height;
							nThird =i;
						}
						else if(size.width ==height &&size.height >=width)
						{
							Third_previewWidth  = size.width;
							Third_previewheight = size.height;
							nThird =i;
						}
					}
					if (second_previewWidth * width == second_previewheight * height) {

						if(second_previewWidth >800){
							if(second_previewWidth== width && nlast == -1){
								previewWidth = second_previewWidth;
								previewheight = second_previewheight;
								nlast = i;
							}
							else if(second_previewWidth< width){
								previewWidth = second_previewWidth;
								previewheight = second_previewheight;
								nlast = i;
							}

						}
					}
				}
				else{
					if((size.height==height||size.width==width) && nThird==-1)
					{
						Third_previewWidth  = size.width;
						Third_previewheight = size.height;
						nThird =i;
					}
					if (second_previewWidth * height == second_previewheight * width) {

						if(second_previewWidth >800){
							if(second_previewWidth== width && nlast == -1){
								previewWidth = second_previewWidth;
								previewheight = second_previewheight;
								nlast = i;
							}
							else if(second_previewWidth< width){
								previewWidth = second_previewWidth;
								previewheight = second_previewheight;
								nlast = i;
							}

						}
					}
				}

//				}
			}
			if(nlast==-1 && nThird!=-1){
				preWidth = Third_previewWidth;
				preHeight = Third_previewheight;
			}
			else{
				preWidth = previewWidth;
				preHeight = previewheight;
			}
		}
		if (!isROI) {
			int t;
			int b;
			int l;
			int r;
			l = height/5;
			r = height*3/5;
			t =  4;
			b = width -4;
			double proportion = (double)height/(double)  preWidth;
			double hproportion=(double) width / (double) preHeight;
			l = (int) (l /proportion);
			t = (int) (t /hproportion);
			r = (int) (r /proportion);
			b = (int) (b / hproportion);
			int[] borders = { l, t, r, b };
			m_ROI[0]=l;
			m_ROI[1]=t;
			m_ROI[2]=r;
			m_ROI[3]=b;
			api.ETSetPlateROI(borders, preWidth, preHeight);
			isROI = true;
		}
		if(!baddView)
		{
			if (isFatty)
				myView = new PLViewfinderView(this, width, height, isFatty);
			else
				myView = new PLViewfinderView(this, width, height);
			re_c.addView(myView);
			baddView = true;
		}

		parameters.setPictureFormat(PixelFormat.JPEG);

		parameters.setPreviewSize(preWidth,preHeight);
		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		}
		if(parameters.isZoomSupported()){
			parameters.setZoom(2);
		}

		camera.setPreviewCallback(this);
		camera.setParameters(parameters);
		camera.setDisplayOrientation(90);
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.startPreview();
		//camera.cancelAutoFocus();
	}

	public String savePicture(Bitmap bitmap, String tag) {
		String strCaptureFilePath = PATH + tag + "_Plate_" + pictureName() + ".jpg";
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


	private int counter = 0;
	private int counterCut = 0;

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {

		tackData = data;
		Camera.Parameters parameters = camera.getParameters();
		int buffl = 256;
		char[] recogval = new char[buffl];
		Date dt= new Date();
		Long timeStart= System.currentTimeMillis();
		if(!alertDialog.isShowing()){
			int[] pLineWarp = new int[800*45];
			int nv21Width =  parameters.getPreviewSize().width;
			int nv21Height = parameters.getPreviewSize().height;
			int r = api.RecognizePlateNV21(data,1,nv21Width, nv21Height,recogval, buffl,pLineWarp);
			Long timeEnd= System.currentTimeMillis();
			if (r == 0) {
				//camera.stopPreview();
				//api.WTUnInitCardKernal();

				// 震动
				recogTime = (timeEnd-timeStart);
				mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				mVibrator.vibrate(50);
				// 删除正常识别保存图片功能
				int[] datas = convertYUV420_NV21toARGB8888(tackData, parameters.getPreviewSize().width,
						parameters.getPreviewSize().height);


				String carNo = null;
				String str = String.valueOf(recogval);
				carNo = str;
				str += "\r\n";
				str +="\r\n识别时间:"+recogTime;
				//alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.setMessage(str);
				Window window = alertDialog.getWindow();
				WindowManager.LayoutParams lp = window.getAttributes();
				// 设置透明度为0.3
				lp.alpha = 0.8f;
				lp.width= width*2/3;
				//lp.flags= 0x00000020;
				window.setAttributes(lp);
				window.setGravity(Gravity.LEFT |Gravity.BOTTOM);
				alertDialog.show();
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				bitmap = Bitmap.createBitmap(datas, parameters.getPreviewSize().width,
						parameters.getPreviewSize().height, android.graphics.Bitmap.Config.ARGB_8888);
				Bitmap tmpbitmap = Bitmap.createBitmap(bitmap, m_ROI[0], m_ROI[1], m_ROI[2]-m_ROI[0], m_ROI[3]-m_ROI[1]);
				savePicture(tmpbitmap, "P");
				//savePicture(bitmap,"FP");

				/**
				 * 向H5调用方法返回值
				 */
				List<ValueForKey> list = new ArrayList<>();
				ValueForKey valueForKey = new ValueForKey();
				valueForKey.setKey("carNo");
				valueForKey.setValue(carNo);
				list.add(valueForKey);
				Intent intent = new Intent();
				OcrVO ocr = new OcrVO();
				ocr.setList(list);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("orcData",ocr);
				intent.putExtras(mBundle);
				setResult(14, intent);
				finish();
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
		if(timer2!=null){
			timer2.cancel();
			timer2=null;
		}
		if(alertDialog!=null)
		{
			alertDialog.cancel();
			alertDialog.dismiss();
		}
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		try {
			if (camera != null) {
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		} catch (Exception e) {
		}
		if(api!=null)
		{
			api.ETUnInitPlateKernal();
			api=null;
		}
	}

	@TargetApi(14)
	private void NewApis(Camera.Parameters parameters) {
		if (Build.VERSION.SDK_INT >= 14) {
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		}
	}
	public  int[] convertYUV420_NV21toARGB8888(byte[] data, int width, int height) {
		int size = width * height;
		int offset = size;
		int[] pixels = new int[size];
		int u, v, y1, y2, y3, y4;

		// i along Y and the final pixels
		// k along pixels U and V
		for (int i = 0, k = 0; i < size; i += 2, k += 2) {
			y1 = data[i] & 0xff;
			y2 = data[i + 1] & 0xff;
			y3 = data[width + i] & 0xff;
			y4 = data[width + i + 1] & 0xff;

			u = data[offset + k] & 0xff;
			v = data[offset + k + 1] & 0xff;
			u = u - 128;
			v = v - 128;

			pixels[i] = convertYUVtoARGB(y1, u, v);
			pixels[i + 1] = convertYUVtoARGB(y2, u, v);
			pixels[width + i] = convertYUVtoARGB(y3, u, v);
			pixels[width + i + 1] = convertYUVtoARGB(y4, u, v);

			if (i != 0 && (i + 2) % width == 0)
				i += width;
		}

		return pixels;
	}
	private   int convertYUVtoARGB(int y, int u, int v) {
		int r, g, b;

		r = y + (int) 1.402f * u;
		g = y - (int) (0.344f * v + 0.714f * u);
		b = y + (int) 1.772f * v;
		r = r > 255 ? 255 : r < 0 ? 0 : r;
		g = g > 255 ? 255 : g < 0 ? 0 : g;
		b = b > 255 ? 255 : b < 0 ? 0 : b;
		return 0xff000000 | (r << 16) | (g << 8) | b;
	}

}
