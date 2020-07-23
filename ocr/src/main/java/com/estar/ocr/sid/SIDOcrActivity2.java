package com.estar.ocr.sid;


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

		import com.estar.ocr.BaseActivitity;
		import com.estar.ocr.R;
		import com.etop.DL.DLCardAPI;
		import com.etop.SIDCard.SIDCardAPI;

		import android.media.ToneGenerator;
		import android.os.Build;
		import android.os.Bundle;
		import android.os.Environment;
		import android.os.Vibrator;
		import android.annotation.TargetApi;
		import android.app.Activity;
		import android.app.AlertDialog;
		import android.app.AlertDialog.Builder;
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
		import android.util.Log;
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
		import android.widget.RelativeLayout;
		import android.widget.Toast;

public class SIDOcrActivity2 extends BaseActivitity implements SurfaceHolder.Callback, Camera.PreviewCallback{

	//private Button photo_button = null;
	private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
	private Camera camera;
	private SurfaceView surfaceView;
	private RelativeLayout re_c;
	private SurfaceHolder surfaceHolder;
	private SIDCardAPI   api=null;
	private Bitmap bitmap;
	private int preWidth = 0;
	private int preHeight = 0;
	private int width;
	private int height;
	private TimerTask timer;
	private Timer timer2;
	private ToneGenerator tone;
	private byte[] tackData;
	private SIDViewfinderView myView;
	private long recogTime;
	private boolean isFatty = false;
	private boolean  bInitKernal=false;
	AlertDialog alertDialog = null;
	Toast toast;
	String FilePath="";
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
					api= new SIDCardAPI();
				}
				FilePath =Environment.getExternalStorageDirectory().toString()+"/9AF68E014442E5AA58EF.lic";
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				int nRet = api.SIDCardKernalInit("",FilePath,"9AF68E014442E5AA58EF",0x02,0x02,telephonyManager,this);
				if(nRet!=0)
				{
					Toast.makeText(getApplicationContext(), "激活失败"+nRet, Toast.LENGTH_SHORT).show();
					System.out.print("nRet="+nRet);
					bInitKernal =false;
				}
				else
				{
					System.out.print("nRet="+nRet);
					bInitKernal=true;
					api.SIDCardSetRecogType(0);
				}
			}
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		// // 屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.activity_sidocr2);
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
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(SIDOcrActivity2.this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
	public void selectResultPicPath(String picPath) {
		super.selectResultPicPath(picPath);
		Intent intent =new Intent(this,SIDPhotoResultActivity.class);
		intent.putExtra("data",picPath);
		startActivity(intent);
	}

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
		if(!bInitKernal)
		{
			FilePath =Environment.getExternalStorageDirectory().toString()+"/9AF68E014442E5AA58EF.lic";
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			if(api==null)
			{
				api= new SIDCardAPI();
			}
			int nRet = api.SIDCardKernalInit("",FilePath,"etop",0x02,0x02,telephonyManager,this);
			if(nRet!=0)
			{
				Toast.makeText(getApplicationContext(), "激活失败"+nRet, Toast.LENGTH_SHORT).show();
				System.out.print("nRet="+nRet);
				bInitKernal =false;
			}
			else
			{
				System.out.print("nRet="+nRet);
				bInitKernal=true;
			}
			//bInitKernal=true;
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
		if(bInitKernal){
			api.SIDCardKernalUnInit();
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
				if (camera != null) {
					camera.setPreviewCallback(null);
					camera.stopPreview();
					camera.release();
					camera = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(bInitKernal)
			{
				api.SIDCardKernalUnInit();
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
		if (isFatty)
			myView = new SIDViewfinderView(this, width, height, isFatty);
		else
			myView = new SIDViewfinderView(this, width, height);
		try {
			re_c.addView(myView);
		}catch (Exception e) {
			e.printStackTrace();
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
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.startPreview();

		//camera.cancelAutoFocus();
	}

	public String savePicture(Bitmap bitmap, String tag) {
		String strCaptureFilePath = PATH + tag + "_SIDCard_" + pictureName() + ".jpg";
		String strCaptureFileHeadPath = PATH + tag + "_SIDCard_Head_" + pictureName() + ".jpg";
		int nRet =api.SIDCardSaveHeadImage(strCaptureFileHeadPath);
		if(nRet ==0){
			System.out.print("1111");
		}
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
			int r = api.SIDCardRecognizeNV21(data, parameters.getPreviewSize().width, parameters.getPreviewSize().height,recogval,buffl);
			Long timeEnd= System.currentTimeMillis();
			if (r == 0) {
				//camera.stopPreview();
				//api.WTUnInitCardKernal();

				// 震动
				recogTime = (timeEnd-timeStart);
				//mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				//	mVibrator.vibrate(50);
				// 删除正常识别保存图片功能
				int[] datas = convertYUV420_NV21toARGB8888(tackData, parameters.getPreviewSize().width,
						parameters.getPreviewSize().height);

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				bitmap = Bitmap.createBitmap(datas, parameters.getPreviewSize().width,
						parameters.getPreviewSize().height, android.graphics.Bitmap.Config.ARGB_8888);
				savePicture(bitmap,"M");
				String str = "";
				int nRecog=api.SIDCardGetRecogType();
				if(nRecog==1){
					for(int i =0;i<6;i++){
						str += api.SIDCardGetResult(i);
						str += "\r\n";
					}
				}
				else if(nRecog ==2)
				{
					for(int i =6;i<8;i++){
						str += api.SIDCardGetResult(i);
						str += "\r\n";
					}
				}
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
			}
		}
	}

	@Override
	public void show(byte[] data) {
		super.show(data);
		if(api==null)
		{
			if(api==null)
			{
				api= new SIDCardAPI();
			}
			FilePath =Environment.getExternalStorageDirectory().toString()+"/9AF68E014442E5AA58EF.lic";
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			int nRet = api.SIDCardKernalInit("",FilePath,"9AF68E014442E5AA58EF",0x02,0x02,telephonyManager,this);
			if(nRet!=0)
			{
				Toast.makeText(getApplicationContext(), "激活失败"+nRet, Toast.LENGTH_SHORT).show();
				System.out.print("nRet="+nRet);
				bInitKernal =false;
			}
			else
			{
				System.out.print("nRet="+nRet);
				bInitKernal=true;
				api.SIDCardSetRecogType(0);
			}
		}
		if(alertDialog==null){
			alertDialog = new Builder(this).create();
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		BitmapFactory.decodeByteArray(data,0,data.length,options);
		Long timeStart= System.currentTimeMillis();
		int buffl = 256;
		char[] recogval = new char[buffl];
		if(!alertDialog.isShowing()){
			int r = api.SIDCardRecognizeNV21(data,800, 640,recogval,buffl);
			Long timeEnd= System.currentTimeMillis();
			if (r == 0) {
				//camera.stopPreview();
				//api.WTUnInitCardKernal();

				// 震动
				recogTime = (timeEnd-timeStart);
				//mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
				//	mVibrator.vibrate(50);
				// 删除正常识别保存图片功能
				int[] datas = convertYUV420_NV21toARGB8888(tackData, options.outWidth,
						options.outHeight);

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				bitmap = Bitmap.createBitmap(datas, options.outWidth,
						options.outHeight, android.graphics.Bitmap.Config.ARGB_8888);
				savePicture(bitmap,"M");
				String str = "";
				int nRecog=api.SIDCardGetRecogType();
				if(nRecog==1){
					for(int i =0;i<6;i++){
						str += api.SIDCardGetResult(i);
						str += "\r\n";
					}
				}
				else if(nRecog ==2)
				{
					for(int i =6;i<8;i++){
						str += api.SIDCardGetResult(i);
						str += "\r\n";
					}
				}
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
			}else{
				Toast.makeText(this,"识别失败",0).show();
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
