package com.estar.hh.survey.utils;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.estar.utils.MessageDialog;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 拍照、录像
 */
public class VideoModual implements PreviewCallback, ErrorCallback,
		AutoFocusCallback, PictureCallback, Callback, ShutterCallback {

	// 静态变量(单例模式)
	private static VideoModual mVideoModual = null;
	/**前后摄像头切换 **/
	private int CHANGE_CAMERA=0;
	// 图片格式
	public  final int PICTURE_FORMAT_JPEG = 1;
	// 拍照回调对象
	private VideoCallBack mVideoCallBack = null;
	// Camera实例
	private Camera mCamera = null;
	// SurfaceHolder实例
	private SurfaceHolder mSurfaceHolder = null;
	// 表示是否正在预览
	private boolean mPreview = false;
	private Context context;
	private boolean photograph=false;//拍照为true, 聚焦为false

	// 私有构造函数
	private VideoModual(Context context2) {
		this.context=context2.getApplicationContext();//防止内存泄漏  Applicaton的Context
	}

	public static VideoModual getInstance(Context context) {
		LogUtils.w("getInstance(Context context)");
		if (null==mVideoModual) {
			mVideoModual = new VideoModual(context);
		}
		return mVideoModual;
	}

	/**
	 * @fn setCallBack
	 * @brief 设置回调函数
	 * @param videoCallBack
	 *            - 视频回调对象(OUT)
	 * @return 无
	 */
	public void setCallBack(VideoCallBack videoCallBack) {
		LogUtils.w("setCallBack(VideoCallBack videoCallBack)");
		mVideoCallBack = videoCallBack;
	}

	/**
	 * @fn startPreview
	 * @brief 开始预览
	 * @param isModel
	 *            true视频预览 false 图片预览
	 * @param surfaceView
	 *            - Surface View(IN)
	 * @return true - 成功 false - 失败
	 */
	public boolean startPreview(SurfaceView surfaceView, boolean isModel) {
		LogUtils.w("startPreview(SurfaceView surfaceView, boolean isModel)");
		if (mPreview) {
			LogUtils.w( "previewing");
			return true;
		}

		if (surfaceView == null) {
			LogUtils.w( "surfaceView eques null");
			mSurfaceHolder=null;
			return false;
		}

		// 获取surface holder
		mSurfaceHolder = surfaceView.getHolder();
		if (null==mSurfaceHolder ) {
			LogUtils.w("surfaceHolder eques null");
			return false;
		}
		//设置改Surface不需要自己维护缓冲区
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.addCallback(this);


		return true;
	}


	/**
	 * @fn stopPreview
	 * @brief 停止预览
	 * @param
	 * @return 无
	 */
	public void stopPreview() {
		LogUtils.w("stopPreview()");
		try {
			mCamera.stopPreview();
			mCamera.setPreviewCallbackWithBuffer(null);
			mCamera.setErrorCallback(null);
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;

			mPreview = false;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.w("停止预览出错");
		}
	}

//	FLASH_MODE_RED_EYE防红眼模式，减小或阻止图片上的人物像的红眼出现。
//	FLASH_MODE_TORCH填充模式，在正常光照下会减弱闪光强度。
//	FLASH_MODE_AUTO自动模式，有需要的时候会自动闪光。
//	FLASH_MODE_OFF 闪光模式将不会被关闭
//	FLASH_MODE_ON  快照时闪光模式将永远被关闭
	public void openLamp(String value) {
		LogUtils.w("openLamp(String value)");
		try {
			Parameters par = mCamera.getParameters();
			par.setFlashMode(value);// 开启闪光灯

			mCamera.setParameters(par);
			if (Parameters.FLASH_MODE_OFF.equals(value)){//关闭闪光灯
//			mCamera.release();
				LogUtils.w("关闭闪光灯");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.w("设置闪光灯出错");
		}

	}


	/**
	 * @fn takenPicture
	 * @brief 拍照
	 * @param
	 * @return true - 成功 false - 失败
	 */
	public boolean takenPicture(boolean isAutoFocus) {
		LogUtils.w("takenPicture(boolean isAutoFocus)");
		try {
			if (!mPreview || mCamera == null) {
				return false;
			}
			photograph=true;
			mCamera.autoFocus(mAutoFocusCallBack);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.w("出错");
		}
		return true;
	}

		private AutoFocusCallback mAutoFocusCallBack = new AutoFocusCallback() {
	
			public void onAutoFocus(boolean success, Camera camera) {
				if (photograph){
					try {
						mCamera.takePicture(VideoModual.this, null, VideoModual.this);
					} catch (Exception e) {
						e.printStackTrace();
						LogUtils.w("生成照片出错");
					}
				}

			}
		};


	/**
	 * @fn aotoFocus
	 * @brief 自动对焦
	 * @param
	 * @return 无
	 */
	public void aotoFocus() {
		LogUtils.w("aotoFocus()");
		try{
			if (mPreview == true && mCamera != null) {
//				mCamera.autoFocus(this);
				photograph=false;
				mCamera.autoFocus(mAutoFocusCallBack);

			}
		}catch(Exception e){
			e.printStackTrace();
			LogUtils.w("自动对焦出错");
		}
	}






	/**
	 * @fn setParamToApplication
	 * @brief 将视频参数和图像参数写入配置文件中
	 * @param
	 * @return true - 写入成功 false - 写入失败
	 */
	private boolean setParamToApplication() {
		return true;
	}

	public Camera getCamera(){
		return mCamera;
	}

	/**前后摄像头切换 **/
	public void setChangeCamera(int CHANGE_CAMERA){
		this.CHANGE_CAMERA=CHANGE_CAMERA;
		// 相机出现异常之后 重新开始
		stopPreview();
		preview(Parameters.FLASH_MODE_OFF);

	}

	private void preview(String value) {
		LogUtils.w("preview(String value)");
		try {
			try {

				// 开启摄像头
				mCamera = Camera.open(CHANGE_CAMERA);
				if (mCamera == null) {
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			try {
				mCamera.setPreviewDisplay(mSurfaceHolder);
			} catch (IOException e) {
				mCamera.release();
				e.printStackTrace();
				return;
			}

			// 设置视频数据回调函数
			mCamera.setPreviewCallback(this);

			// 设置出错回调函数
			mCamera.setErrorCallback(this);

			// mCamera.setDisplayOrientation(90);
			// mCamera.autoFocus(this);
			// 获取摄像头参数
			Parameters param = mCamera.getParameters();
			//
			//	if (this.context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			param.set("orientation", "portrait"); //
			param.set("rotation", 0); // 镜头角度转90度（默认摄像头是横拍）
			mCamera.setDisplayOrientation(0); // 在2.2以上可以使用
			param.setRotation(0);

			//			} else// 如果是横屏
			//			{
			//				param.set("orientation", "landscape"); //
			//				param.set("rotation", 0); // 镜头角度转90度（默认摄像头是横拍）
			//				mCamera.setDisplayOrientation(0); // 在2.2以上可以使用
			//				param.setRotation(0);
			//			}
			// param = mCamera.getParameters();

			// 对list从低到高排序
			Comparator comp = new Comparator() {
				public int compare(Object o1, Object o2) {
					Size p1 = (Size) o1;
					Size p2 = (Size) o2;
					if (p1.width < p2.width)
						return -1;
					else if (p1.width == p2.width)
						return 0;
					else if (p1.width > p2.width)
						return 1;
					return 0;
				}
			};
			// 获取摄像头支持的预览分辨率
			List<Size> preSizes = param.getSupportedPreviewSizes();
			// 获取摄像头支持的图片分辨率
			List<Size> picSizes = param.getSupportedPictureSizes();
			Collections.sort(picSizes, comp);
			Collections.sort(preSizes, comp);


			int picterWidth = 1920;
			int picterheight = 1080;



			param.setPreviewSize(preSizes.get(preSizes.size()-1).width,
					preSizes.get(preSizes.size()-1).height);
			boolean isBoolean=false;
			// 获取最合适的图片分辨率
			for (int i = 0, j = picSizes.size(); i < j; i++) {

//				LogUtil.w(i+"  项宽="+picSizes.get(i).width+"  项高="+picSizes.get(i).height);
				if(picSizes.get(i).width==picterWidth){
					isBoolean=true;
					break;
				}
			}
			if(!isBoolean){
				picterWidth=picSizes.get(picSizes.size()-6).width;
				picterheight=picSizes.get(picSizes.size()-6).height;
			}


			param.setPictureSize(picterWidth, picterheight);

			//	}

			// Size preSize = param.getPreviewSize();
			// preSize.height);
			param.setFlashMode(value);// 关闭闪光灯
			// 将参数设置到配置文件中
			setParamToApplication();
			// 设置摄像头参数
			mCamera.setParameters(param);

			// 开始预览
			mCamera.startPreview();
			this.aotoFocus();
			mPreview = true;
		} catch (Exception e) {
//			Toast.makeText(context, "相机打开失败，请重启手机!", Toast.LENGTH_SHORT).show();
//			Toast.makeText(context, "相机打开失败!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			LogUtils.w("打开相机出错");


		}
	}

	public void onPreviewFrame(byte[] data, Camera camera) {
//		LogUtil.w("onPreviewFrame(byte[] data, Camera camera)");
		if (mVideoCallBack != null) {


		}
	}

	public void onPictureTaken(byte[] data, Camera camera) {
		LogUtils.w("onPictureTaken(byte[] data, Camera camera)");
		// 加上这个判断,避免mPictureData内存频繁分配
//		int pictrueSize = data.length;
//		if (mPictureData == null) {
//			mPictureData = new byte[pictrueSize];
//		} else if (pictrueSize != mPictureSize) {
//			mPictureData = null;
//			mPictureData = new byte[pictrueSize];
//			mPictureSize = pictrueSize;
//		}
//
//		// 拷贝JPEG数据
//		System.arraycopy(data, 0, mPictureData, 0, pictrueSize);

//		// 发送消息
//		if (mHandler != null) {
//			Message msg = new Message();
//			msg.what = MSG_TAKEN_PICTURE;
//			mHandler.sendMessage(msg);
//		}

		if (null!=mVideoCallBack && null !=mVideoModual) {
			mVideoCallBack.onPictureData(data,
					PICTURE_FORMAT_JPEG);
		}

		// 重新开始预览
		camera.startPreview();
	}

	/**
	 * 变焦
	 */
	public void onAutoFocus(boolean arg0, Camera arg1) {
		LogUtils.w("onAutoFocus(boolean arg0, Camera arg1)");

	}

	public void onError(int error, Camera camera) {
		LogUtils.w("onError(int error, Camera camera)");
		// 相机出现异常之后 重新开始
		stopPreview();
		preview(Parameters.FLASH_MODE_OFF);

		if (mVideoCallBack != null) {
			mVideoCallBack.onMsg(error, "");
		}
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		LogUtils.w("surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)");
		// 开始预览
		if (mCamera != null){
			try {
				mCamera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();

				new MessageDialog(context, new MessageDialog.SubmitOnClick() {
					@Override
					public void onSubmitOnClickSure() {
					}

					@Override
					public void onSubmitOnClickCancel() {
					}
				}, "重要提示", "打开相机失败,请检查软件是否具有拍照权限？有,请重启手机;没有,请开通权限", "确定",
						"取消",false);


			}
		}

	}

	public void surfaceCreated(SurfaceHolder holder) {
		LogUtils.w("surfaceCreated(SurfaceHolder holder)");
		preview(Parameters.FLASH_MODE_OFF);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		LogUtils.w("surfaceDestroyed(SurfaceHolder holder)");
		try {
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.setPreviewCallbackWithBuffer(null);
				mCamera.setErrorCallback(null);
				mCamera.setPreviewCallback(null);
				mCamera.release();
			}
			mCamera = null;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.w("出错");
		}

	}
	/**
	 * 调节焦距
	 * 
	 * @param type
	 */
	public void zoom(int type) {
		LogUtils.w("zoom(int type)");
		//if (isSupportZoom()) {
		try {
			Parameters params = mCamera.getParameters();
			final int MAX = params.getMaxZoom();
			LogUtils.e("===", MAX+"<<<<<<<");
			if (MAX == 0)
				return;

			int zoomValue = params.getZoom();
			//1拉开
			if(type==1){
				zoomValue += 2;
			}else{
				zoomValue -= 2;
			}
			if(zoomValue>MAX)zoomValue=MAX;

			if(zoomValue<0)zoomValue=0;
			params.setZoom(zoomValue);
			mCamera.setParameters(params);

		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.w("调节焦距出错");
		}
	}


	/**
	 * 快门声
	 */
	public void onShutter() {
		LogUtils.w("onShutter()");
	}


	private  boolean checkCameraFacing(final int facing) {
		if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
			return false;
		}
		final int cameraCount = Camera.getNumberOfCameras();
		Camera.CameraInfo info = new Camera.CameraInfo();
		for (int i = 0; i < cameraCount; i++) {
			Camera.getCameraInfo(i, info);
			if (facing == info.facing) {
				return true;
			}
		}
		return false;
	}



	/**
	 * 判断是否有前置摄像头 为1
	 * @return
	 */
	public  boolean hasFrontFacingCamera() {
		final int CAMERA_FACING_BACK = 1;
		return checkCameraFacing(CAMERA_FACING_BACK);
	}

	public  int getSdkVersion() {
		return Build.VERSION.SDK_INT;
	}


}
