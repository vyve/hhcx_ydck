package com.estar.ocr.vl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.estar.ocr.R;
import com.estar.ocr.vl.VLViewfinderView;
import com.etop.VL.VLCardAPI;

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
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VLOcrActivity extends Activity   implements SurfaceHolder.Callback, Camera.PreviewCallback{

	//private Button photo_button = null;
	private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
	private Camera mycamera;
	private SurfaceView surfaceView;
	private RelativeLayout re_c;
	private SurfaceHolder surfaceHolder;
	private VLCardAPI   api=null;
	private Bitmap bitmap;
	private int preWidth = 0;
	private int preHeight = 0;
	private int photoWidth =0;
	private int photoHeight = 0;
	private int width;
	private int height;
	private TimerTask timer;
	private Timer timer2;
	private ToneGenerator tone;
	private byte[] tackData;
	private VLViewfinderView myView;
	private long recogTime;
	private boolean isFatty = false;
	private boolean  bInitKernal=false;
	AlertDialog alertDialog = null;
	Toast toast;
	String FilePath="";
	private ImageButton back;
	private ImageButton tack_pic;
	private ImageButton change;
	private ImageButton flash;
	private boolean bPhoto = false;
	private boolean bP=false;
	private boolean bAddrec=false;
	private Vibrator  mVibrator;
	private TextView mtext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题

		//photo_button = (Button)findViewById(R.id.recog);
		//photo_button.setOnClickListener(this);
		try {
			copyDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
		Configuration cf= this.getResources().getConfiguration(); //获取设置的配置信息
		int noriention=cf.orientation;
		if(alertDialog==null){
			alertDialog = new AlertDialog.Builder(this).create();
		}
		if(noriention== Configuration.ORIENTATION_LANDSCAPE)
		{
			if(!bInitKernal)
			{
				if(api==null)
				{
					api= new VLCardAPI();
				}
				FilePath =Environment.getExternalStorageDirectory().toString()+"/9AF68E014442E5AA58EF.lic";
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				int nRet = api.VLKernalInit("",FilePath,"9AF68E014442E5AA58EF",0x05,0x02,telephonyManager,this);
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

		setContentView(R.layout.activity_vlocr);
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
		tack_pic = findViewById(R.id.take_pic);
		change = findViewById(R.id.change);
		flash = findViewById(R.id.flash);
		mtext= findViewById(R.id.mytext);

		mtext.setText("当前为视频流识别模式,可点击切换按钮切换识别模式");
		mtext.setTextColor(Color.GREEN);
//		mtext.setTextSize(TypedValue.COMPLEX_UNIT_PX,height/18);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT );
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		layoutParams.bottomMargin = (int) (height * 0.1);
		mtext.setLayoutParams(layoutParams);

		int back_w = (int) (width * 0.066796875);
		int back_h = back_w * 1;
		layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		int Fheight = height;
		layoutParams.leftMargin = back_h/3;
		layoutParams.bottomMargin = (int) (height * 0.15);
		back.setLayoutParams(layoutParams);

		layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		layoutParams.leftMargin = back_h/3;
		//layoutParams.topMargin = (int) (height * 0.5);
		change.setLayoutParams(layoutParams);

		layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layoutParams.leftMargin = back_h/3;
		layoutParams.topMargin = (int) (height * 0.15);
		flash.setLayoutParams(layoutParams);

		int takepic_w = (int) (width * 0.106796875);
		int takepic_h = takepic_w * 1;
		layoutParams = new RelativeLayout.LayoutParams(takepic_w, takepic_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		layoutParams.rightMargin = (width-(14*height/15)*88/58)/2+back_h/4;
		tack_pic.setLayoutParams(layoutParams);
		tack_pic.setVisibility(View.INVISIBLE);


		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(VLOcrActivity.this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				api.VLKernalUnInit();
				finish();

				//back
			}
		});
		change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!bPhoto)
				{
					//change.setText("拍照模式");
					mtext.setText("当前为拍照识别模式,可点击切换按钮切换识别模式");
					mtext.setTextColor(Color.GREEN);
					mtext.setTextSize(TypedValue.COMPLEX_UNIT_PX,height/18);
					tack_pic.setVisibility(View.VISIBLE);
					bPhoto =true;
					if(mycamera!=null)
					{
						mycamera.setPreviewCallback(null);
						Parameters parameters =mycamera.getParameters();
						if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
						}
						mycamera.setParameters(parameters);
					}
				}
				else
				{
					//change.setText("视频流模式");
					mtext.setText("当前为视频流识别模式,可点击切换按钮切换识别模式");
					mtext.setTextColor(Color.GREEN);
					mtext.setTextSize(TypedValue.COMPLEX_UNIT_PX,height/18);
					tack_pic.setVisibility(View.INVISIBLE);
					if(mycamera!=null)
					{
						mycamera.setPreviewCallback(VLOcrActivity.this);
						Parameters parameters =mycamera.getParameters();
						if (parameters.getSupportedFocusModes().contains(
								Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
							if (timer2 != null) {
								timer2.cancel();
								timer2 = null;
							}
							if (timer != null) {
								timer.cancel();
								timer = null;
							}
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
						}
						else if (parameters.getSupportedFocusModes().contains(
								Parameters.FOCUS_MODE_AUTO))
						{
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);// 1连续对焦
						}
						mycamera.setParameters(parameters);

					}

					bPhoto =false;
				}
			}
		});
		tack_pic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!alertDialog.isShowing()){
					if (mycamera != null) {
						try {
							bP =false;
							isFocusTakePicture();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		flash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
					String mess = "当前设备不支持闪光灯";
					Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
				} else {
					if (mycamera != null) {
						Camera.Parameters parameters = mycamera.getParameters();
						String flashMode = parameters.getFlashMode();
						if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
							parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
							parameters.setExposureCompensation(0);
						} else {
							parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 闪光灯常亮
							parameters.setExposureCompensation(-1);
						}
						try {
							mycamera.setParameters(parameters);
						} catch (Exception e) {
							String mess = "当前设备不支持闪光灯";
							Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
						}
						mycamera.startPreview();
					}
				}
			}
		});

	}
	private void isFocusTakePicture() {

		mycamera.autoFocus(new AutoFocusCallback() {
			public void onAutoFocus(boolean success, Camera camera) {
				if (success&&!bP)
				{
					try{
						mycamera.takePicture(null, null, picturecallback);
					} catch (RuntimeException e ) {
						e.printStackTrace();
						return ;
					}
				}
			}
		});
	}

	private PictureCallback picturecallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if (mycamera == null) {
				return ;
			}
			tackData = data;
			int nlen = data.length;
			Camera.Parameters parameters = mycamera.getParameters();
			int pW = parameters.getPreviewSize().width;
			int pH = parameters.getPreviewSize().height;
			try
			{
				data2file(data,"VLP");
			}
			catch (Exception e) {
			}
			if (tackData != null) {
				//if (camera != null) {
				//	camera.setPreviewCallback(null);
				//}
				Long timeStart= System.currentTimeMillis();

				int nRet = api.VLRecognizePhoto(tackData,nlen);
				Long timeEnd= System.currentTimeMillis();
				recogTime = timeEnd-timeStart;
				mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				mVibrator.vibrate(50);
				String strResult ="";
				if	(nRet==0){
					strResult += "号牌号码:"+api.VLGetResult(0);
					strResult += "\r\n";
					strResult += "车辆类型:"+api.VLGetResult(1);
					strResult += "\r\n";
					strResult += "所  有  人:"+api.VLGetResult(2);
					strResult += "\r\n";
					strResult += "住         址:"+api.VLGetResult(3);
					strResult += "\r\n";
					strResult += "使用性质:"+api.VLGetResult(4);
					strResult += "\r\n";
					strResult += "品牌型号:"+api.VLGetResult(5);
					strResult += "\r\n";
					strResult += "车架号码:"+api.VLGetResult(6);
					strResult += "\r\n";
					strResult += "发动机号:"+api.VLGetResult(7);
					strResult += "\r\n";
					strResult += "注册日期:"+api.VLGetResult(8);
					strResult += "\r\n";
					strResult += "发证日期:"+api.VLGetResult(9);
//					for(int i =0;i<10;i++){
//						strResult += api.VLGetResult(i);
//						strResult += "\r\n";
//					}
//					strResult  +="\r\n识别时间:"+recogTime;
				}
				else
				{
					strResult +="识别失败!\r\n";
					strResult +="请重新拍照!";
				}
				alertDialog.setMessage(strResult);
				Window window = alertDialog.getWindow();
				WindowManager.LayoutParams lp = window.getAttributes();
				// 设置透明度为0.3
				lp.alpha = 0.8f;
				lp.width= width*2/3;
				//lp.flags= 0x00000020;
//				 window.setAttributes(lp);
				window.setGravity(Gravity.CENTER_HORIZONTAL |Gravity.BOTTOM);
				alertDialog.show();

			}
			bP=true;
			camera.startPreview();
		}
	};
	private void data2file(byte[] w, String tag) throws Exception {
		//将二进制数据转换为文件的函数
		String strCaptureFilePath = PATH + tag + "_VL_" + pictureName() + ".jpg";
		FileOutputStream out =null;
		try {
			out =new FileOutputStream(strCaptureFilePath);
			out.write(w);
			out.close();
		} catch (Exception e) {
			if (out !=null)
				out.close();
			throw e;
		}
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
			api= new VLCardAPI();
			FilePath =Environment.getExternalStorageDirectory().toString()+"/9AF68E014442E5AA58EF.lic";
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			int nRet = api.VLKernalInit("",FilePath,"9AF68E014442E5AA58EF",0x05,0x02,telephonyManager,this);
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

		if (mycamera == null) {
			try {
				mycamera = Camera.open();
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
			mycamera.setPreviewDisplay(holder);

			timer2 = new Timer();
			if (timer == null) {
				timer = new TimerTask() {
					public void run() {
						//isSuccess=false;
						if (mycamera != null) {
							try {
								mycamera.autoFocus(new AutoFocusCallback() {
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
			timer2.schedule(timer, 500, 2500);
			initCamera(holder,0);
		} catch (IOException e) {
			e.printStackTrace();

		}
		if(alertDialog==null){
			alertDialog = new AlertDialog.Builder(this).create();
		}
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {
//		if (camera != null) {
//			camera.autoFocus(new AutoFocusCallback() {
//				@Override
//				public void onAutoFocus(boolean success, Camera camera) {
//					if (success) {
////						synchronized (camera) {
////							new Thread() {
////								public void run() {
////
////									super.run();
////								}
////							}.start();
////						}
//						initCamera(holder,0);
//						camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
//					}
//				}
//			});
//		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			if (mycamera != null) {
				mycamera.setPreviewCallback(null);
				mycamera.stopPreview();
				mycamera.release();
				mycamera = null;
			}
		} catch (Exception e) {
		}
		if(bInitKernal){
			api.VLKernalUnInit();
			bInitKernal=false;
			api = null;
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
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				if (mycamera != null) {
					mycamera.setPreviewCallback(null);
					mycamera.stopPreview();
					mycamera.release();
					mycamera = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(bInitKernal)
			{
				api.VLKernalUnInit();
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
	private void initCamera(SurfaceHolder holder,int ntype) {

		Camera.Parameters parameters = mycamera.getParameters();
		List<Camera.Size> list = parameters.getSupportedPreviewSizes();
		List<Camera.Size> listP =  parameters.getSupportedPictureSizes();
		Camera.Size size;
		int length = list.size();
		Size tmpsize = getOptimalPreviewSize(list,width,height);
		int previewWidth = list.get(0).width;
		int previewheight = list.get(0).height;
		int second_previewWidth = 0;
		int second_previewheight = 0;
		int nlast = -1;
		int nThird =-1;
		previewWidth = tmpsize.width;
		previewheight = tmpsize.height;
//			int Third_previewWidth = 0;
//			int Third_previewheight = 0;
//			if (length == 1) {
//				size = list.get(0);
//				previewWidth = size.width;
//				previewheight = size.height;
//			} else {
//
//				for (int i = 0; i < length; i++) {
//					size = list.get(i);
//					if (size.width * height == size.height * width) {
//
//						if(size.width >800){
//		                     if(size.width== width ){
//		                    	 previewWidth = size.width;
//								 previewheight = size.height;
//								 nlast =i;
//		                     }
//		                     else if(size.width!= width && size.width>previewWidth &&nlast==-1){
//		                    	 previewWidth = size.width;;
//								 previewheight = size.height;
//								 nlast =i;
//		                     }
//
//						}
//					}
//					else if(size.width==width&&nlast==-1)
//					{
//						if(size.height >=height)
//						{
//							previewWidth  = size.width;
//							previewheight = size.height;
//							nThird =i;
//						}
//					}
//					else if(size.height ==height &&nlast==-1&&nThird ==-1)
//					{
//						if(size.width >=width)
//						{
//							previewWidth  = size.width;
//							previewheight = size.height;
//							//nThird =i;
//						}
//					}
//				}
//			}
		if (length == 1) {
			preWidth = previewWidth;
			preHeight = previewheight;
		}
		else
		{
			second_previewWidth=previewWidth;
			second_previewheight = previewheight;
			for (int i = 0; i < length; i++) {
				size = list.get(i);
				if(size.height>700)
				{
					if(size.width * previewheight == size.height * previewWidth && size.height<second_previewheight)
					{
						second_previewWidth =size.width;
						second_previewheight= size.height;
					}
				}
			}
			preWidth = second_previewWidth;
			preHeight = second_previewheight;
		}
		length = listP.size();
		int pwidth = listP.get(0).width;
		int pheight = listP.get(0).height;
		second_previewWidth = 0;
		second_previewheight = 0;
		nlast = -1;
		nThird =-1;
		//	 Third_previewWidth = 0;
		//	 Third_previewheight = 0;
		if (length == 1) {
			size = listP.get(0);
			pwidth = size.width;
			pheight = size.height;
		} else {

			for (int i = 0; i < length; i++) {
				size = listP.get(i);
				if (size.width * height == size.height * width) {

					if(size.width >800){
						if(size.width== width ){
							pwidth = size.width;
							pheight = size.height;
							nlast =i;
						}
						else if(size.width!= width && size.width>pwidth &&nlast==-1){
							pwidth = size.width;
							pheight = size.height;
							nlast =i;
						}

					}
				}
				else if(size.width==width&&nlast==-1)
				{
					if(size.height >=height)
					{
						pwidth  = size.width;
						pheight = size.height;
						nThird =i;
					}
				}
				else if(size.height ==height &&nlast==-1&&nThird ==-1)
				{
					if(size.width >=width)
					{
						pwidth  = size.width;
						pheight = size.height;
						//nThird =i;
					}
				}
			}
		}
		photoWidth=pwidth;
		photoHeight=pheight;
		if	(!bAddrec)
		{
			if (isFatty)
				myView = new VLViewfinderView(this, width, height, isFatty);
			else
				myView = new VLViewfinderView(this, width, height);
			re_c.addView(myView);
			bAddrec=true;

		}
		parameters.setPictureFormat(PixelFormat.JPEG);
		parameters.setJpegQuality(100);
		parameters.setPictureSize(photoWidth, photoHeight);
		parameters.setPreviewSize(preWidth,preHeight);
		if(ntype==0)
		{
			if (parameters.getSupportedFocusModes().contains(
					Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
				if (timer2 != null) {
					timer2.cancel();
					timer2 = null;
				}
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			}
			else if (parameters.getSupportedFocusModes().contains(
					Parameters.FOCUS_MODE_AUTO))
			{
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);// 1连续对焦
			}

		}else{
			if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			}
		}
		if(parameters.isZoomSupported()){
			parameters.setZoom(2);
		}
		mycamera.setPreviewCallback(this);
		mycamera.setParameters(parameters);
		try {
			mycamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mycamera.startPreview();
		//camera.cancelAutoFocus();
	}

	public String savePicture(Bitmap bitmap, String tag) {
		String strCaptureFilePath = PATH + tag + "_VL_" + pictureName() + ".jpg";
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
		if(!alertDialog.isShowing()&& !bPhoto){
			int r = api.VLRecognizeNV21(data, parameters.getPreviewSize().width, parameters.getPreviewSize().height,recogval,buffl);
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

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				bitmap = Bitmap.createBitmap(datas, parameters.getPreviewSize().width,
						parameters.getPreviewSize().height, android.graphics.Bitmap.Config.ARGB_8888);
				//savePicture(bitmap,"M");
				String strResult = "";
//					for(int i =0;i<10;i++){

//						str += api.VLGetResult(i);
//						str += "\r\n";
//					}
//					str +="\r\n识别时间:"+recogTime;
				strResult += "号牌号码:"+api.VLGetResult(0);
				strResult += "\r\n";
				strResult += "车辆类型:"+api.VLGetResult(1);
				strResult += "\r\n";
				strResult += "所  有  人:"+api.VLGetResult(2);
				strResult += "\r\n";
				strResult += "住         址:"+api.VLGetResult(3);
				strResult += "\r\n";
				strResult += "使用性质:"+api.VLGetResult(4);
				strResult += "\r\n";
				strResult += "品牌型号:"+api.VLGetResult(5);
				strResult += "\r\n";
				strResult += "车架号码:"+api.VLGetResult(6);
				strResult += "\r\n";
				strResult += "发动机号:"+api.VLGetResult(7);
				strResult += "\r\n";
				strResult += "注册日期:"+api.VLGetResult(8);
				strResult += "\r\n";
				strResult += "发证日期:"+api.VLGetResult(9);
				//alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.setMessage(strResult);
				Window window = alertDialog.getWindow();
				WindowManager.LayoutParams lp = window.getAttributes();
				// 设置透明度为0.3
				lp.alpha = 0.8f;
				lp.width= height-80;
				//lp.flags= 0x00000020;
//							 window.setAttributes(lp);
				window.setGravity(Gravity.CENTER_HORIZONTAL |Gravity.BOTTOM);
				alertDialog.show();
			}
		}
	}
	@Override
	protected void onStop() {

		super.onStop();
//		if (timer != null) {
//			timer.cancel();
//			timer = null;
//		}
//		if(timer2!=null){
//			timer2.cancel();
//			timer2=null;
//		}
//		if(alertDialog!=null)
//		{
//			alertDialog.cancel();
//			alertDialog.dismiss();
//		}
//		if (bitmap != null) {
//			bitmap.recycle();
//			bitmap = null;
//		}
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

	@TargetApi(14)
	private void NewApis(Camera.Parameters parameters) {
		if (Build.VERSION.SDK_INT >= 14) {
			parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
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
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null) return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}


}
