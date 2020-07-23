package com.estar.ocr.common.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.estar.ocr.R;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback,
		Camera.PictureCallback {

	public static final String TAG = CameraFragment.class.getSimpleName();
	public static final String CAMERA_ID_KEY = "camera_id";
	public static final String CAMERA_FLASH_KEY = "flash_mode";
	public static final String PREVIEW_HEIGHT_KEY = "preview_height";

	public static final int CAMERA_NO_FRONT = -111; // 表示该设备没有前置摄像头
	private static final int PICTURE_SIZE_MAX_WIDTH = 1200;
	private static final int PREVIEW_SIZE_MAX_WIDTH = 1200;

	private int mCameraID;
	private String mFlashMode;
	private Camera mCamera;
	private SquareCameraPreview mPreviewView;
	private SurfaceHolder mSurfaceHolder;

	private int mDisplayOrientation;
	private int mLayoutOrientation;

	public int mCoverHeight;
	private int mPreviewHeight;

	private CameraOrientationListener mOrientationListener;

	public PicCallback picCallBack;
	private Timer timer = null;

	public static Fragment newInstance() {
		return new CameraFragment();
	}

	public CameraFragment() {
	}

	public interface PicCallback {
		void onPictureTaken(byte[] data, int rotation);
	}

	public void setOnPicCallBack(PicCallback picCallBack) {
		this.picCallBack = picCallBack;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mOrientationListener = new CameraOrientationListener(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_camera, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (savedInstanceState == null) {
			mCameraID = getBackCameraID();
			mFlashMode = Camera.Parameters.FLASH_MODE_OFF;
		} else {
			mCameraID = savedInstanceState.getInt(CAMERA_ID_KEY);
			mFlashMode = savedInstanceState.getString(CAMERA_FLASH_KEY);
			mPreviewHeight = savedInstanceState.getInt(PREVIEW_HEIGHT_KEY);
		}

		mOrientationListener.enable();

		mPreviewView = view
				.findViewById(R.id.camera_preview_view);
		mPreviewView.getHolder().addCallback(CameraFragment.this);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(CAMERA_ID_KEY, mCameraID);
		outState.putString(CAMERA_FLASH_KEY, mFlashMode);
		outState.putInt(PREVIEW_HEIGHT_KEY, mPreviewHeight);
		super.onSaveInstanceState(outState);
	}

	@SuppressLint("NewApi")
	private boolean getCamera(int cameraID) {
		if (cameraID == CAMERA_NO_FRONT) {
			cameraID = getBackCameraID();
		}
		try {
			mCamera = Camera.open(cameraID);
			mPreviewView.setCamera(mCamera);
		} catch (Exception e) {
			Toast.makeText(getActivity(), "相机打开失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 得到当前的camera
	 * @return
	 */
	public Camera getCamera(){
		return mCamera;
	}
	
	/**
	 * 获取预览SurfaceView
	 * @return
	 */
	public SurfaceView getSurfaceView(){
		return mPreviewView;
	}
	/**
	 * Start the camera preview
	 */
	private void startCameraPreview() {
		determineDisplayOrientation();
		setupCamera();

		try {
			mCamera.setPreviewDisplay(mSurfaceHolder);
			mCamera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stop the camera preview
	 */
	private void stopCameraPreview() {
		// Nulls out callbacks, stops face detection
		if (mCamera != null) {
			try {
				try {
					mCamera.setPreviewCallback(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mCamera.stopPreview();
				mPreviewView.setCamera(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Determine the current display orientation and rotate the camera preview
	 * accordingly
	 */
	@SuppressLint("NewApi")
	private void determineDisplayOrientation() {
		CameraInfo cameraInfo = new CameraInfo();
		Camera.getCameraInfo(mCameraID, cameraInfo);

		int rotation = getActivity().getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0: {
			degrees = 0;
			break;
		}
		case Surface.ROTATION_90: {
			degrees = 90;
			break;
		}
		case Surface.ROTATION_180: {
			degrees = 180;
			break;
		}
		case Surface.ROTATION_270: {
			degrees = 270;
			break;
		}
		}

		int displayOrientation;

		// Camera direction
		if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
			// Orientation is angle of rotation when facing the camera for
			// the camera image to match the natural orientation of the device
			displayOrientation = (cameraInfo.orientation + degrees) % 360;
			displayOrientation = (360 - displayOrientation) % 360;
		} else {
			displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
		}

		mDisplayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
		mLayoutOrientation = degrees;
		mCamera.setDisplayOrientation(displayOrientation);
	}

	public Size getPictureSize() {
		Camera.Parameters parameters = mCamera.getParameters();

		Size bestPictureSize = determineBestPictureSize(parameters);
		return bestPictureSize;
	}

	/**
	 * Setup the camera parameters
	 */
	private void setupCamera() {
		// Never keep a global parameters
		Camera.Parameters parameters = mCamera.getParameters();

		Size bestPreviewSize = determineBestPreviewSize(parameters);
		Size bestPictureSize = determineBestPictureSize(parameters);

		parameters
				.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
		parameters
				.setPictureSize(bestPictureSize.width, bestPictureSize.height);
		parameters.setFlashMode(mFlashMode);
		parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
		parameters.setJpegQuality(95);
		// Set continuous picture focus, if it's supported
		if (parameters.getSupportedFocusModes().contains(
				Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
			parameters
					.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		}
		// parameters.setAutoWhiteBalanceLock(true);
		/**
		 * 设置3秒定时焦距一次
		 */
		// timer = new Timer();
		// timer.schedule(new TimerTask() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// mCamera.autoFocus(new AutoFocusCallback() {
		//
		// @Override
		// public void onAutoFocus(boolean success, Camera camera) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// }
		// }, 5000);

		// Lock in the changes
		mCamera.setParameters(parameters);
	}

	private Size determineBestPreviewSize(Camera.Parameters parameters) {
		return determineBestSize(parameters.getSupportedPreviewSizes(),
				PREVIEW_SIZE_MAX_WIDTH);
	}

	private Size determineBestPictureSize(Camera.Parameters parameters) {
		List<Size> sizes = parameters.getSupportedPictureSizes();
		Size size = null;
		for (Size s : sizes) {
			if (size == null) {
				size = s;
			}
			int mS = Math.min(s.width, s.height);
			if (s.width > size.width&&mS<=PICTURE_SIZE_MAX_WIDTH) {
//				if(s.width==PICTURE_SIZE_MAX_WIDTH){
//					size = s;
//				}else{
					size = s;
//				}
			}

		}
		return size;
		// return determineBestSize(parameters.getSupportedPictureSizes(),
		// PICTURE_SIZE_MAX_WIDTH);
	}

	private Size determineBestSize(List<Size> sizes, int widthThreshold) {
		Size bestSize = null;
		Size size;
		int numOfSizes = sizes.size();
		int temWidht = 0;
		for (int i = 0; i < numOfSizes; i++) {
			size = sizes.get(i);
			int size11 = Math.min(size.width, size.height);
			boolean isDesireRatio = (size.width / 4) == (size.height / 3);
			boolean isBetterSize = (bestSize == null)
					|| size.width > bestSize.width;

			if (isDesireRatio && isBetterSize
					&& widthThreshold >= PICTURE_SIZE_MAX_WIDTH) {
				bestSize = size;
			}
//			if (size11 < widthThreshold && size11 > temWidht
//					&& widthThreshold == PICTURE_SIZE_MAX_WIDTH) {
//				bestSize = size;
//				temWidht = size11;
//			}   
		}

		if (bestSize == null) {
			return sizes.get(sizes.size() - 1);
		}

		return bestSize;
	}

	private void restartPreview() {
		stopCameraPreview();
		mCamera.release();
		mCamera = null;
		if (getCamera(mCameraID)) {
			startCameraPreview();
		}
	}

	private int getFrontCameraID() {
		PackageManager pm = getActivity().getPackageManager();
		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			return CameraInfo.CAMERA_FACING_FRONT;
		}

		return CAMERA_NO_FRONT;
	}

	private int getBackCameraID() {
		return CameraInfo.CAMERA_FACING_BACK;
	}

	public int getCurrentCameraId() {
		return mCameraID;
	}

	/**
	 * 切换摄像头
	 */
	public int switchCamera() {
		if (mCameraID == CameraInfo.CAMERA_FACING_FRONT) {
			mCameraID = getBackCameraID();
		} else {
			mCameraID = getFrontCameraID();
		}
		restartPreview();
		return mCameraID;
	}

	/**
	 * 设置聚焦模式
	 */
	public void setFucosMode() {
		if (mFlashMode.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_AUTO)) {
			mFlashMode = Camera.Parameters.FLASH_MODE_ON;
		} else if (mFlashMode.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_ON)) {
			mFlashMode = Camera.Parameters.FLASH_MODE_OFF;
		} else if (mFlashMode
				.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_OFF)) {
			mFlashMode = Camera.Parameters.FLASH_MODE_AUTO;
		}

		setupCamera();
	}

	public void setFucosMode(String focosMode) {
		mFlashMode = focosMode;
		setupCamera();

	}

	/**
	 * 得到聚焦模式
	 */
	public String getFucosMode(){
		return mFlashMode;
	}
	/**
	 * 设置聚焦图片
	 */
	public void setFocusView(View focusView) {
		mPreviewView.setFocusView(focusView);
	}

	/**
	 * Take a picture
	 */
	public synchronized void takePicture() {
		mOrientationListener.rememberOrientation();

		// Shutter callback occurs after the image is captured. This can
		// be used to trigger a sound to let the user know that image is taken
		final Camera.ShutterCallback shutterCallback = new ShutterCallback() {

			@Override
			public void onShutter() {
				// TODO Auto-generated method stub
				ToneGenerator tone = new ToneGenerator(
						AudioManager.STREAM_DTMF, ToneGenerator.MIN_VOLUME);
				tone.startTone(ToneGenerator.TONE_PROP_BEEP);

			}
		};
		int rotation = (mDisplayOrientation
				+ mOrientationListener.getRememberedNormalOrientation() + mLayoutOrientation) % 360;
		try {
			Camera.Parameters parameters = mCamera.getParameters();
			if(mCameraID != CameraInfo.CAMERA_FACING_FRONT){
				parameters.setRotation(rotation);
				parameters.set("rotation", rotation);
			}

			mCamera.setParameters(parameters);
			// Raw callback occurs when the raw image data is available
			Camera.PictureCallback raw = null;
			
			// postView callback occurs when a scaled, fully processed
			// postView image is available.
			Camera.PictureCallback postView = null;

			// jpeg callback occurs when the compressed image is available
			// mCamera.autoFocus(new AutoFocusCallback() {
			//
			// @Override
			// public void onAutoFocus(boolean success, Camera camera) {
			mCamera.takePicture(shutterCallback, null, null,
					CameraFragment.this);

			// }
			// });
		} catch (RuntimeException e) {
			// Toast.makeText(getActivity(), "相机拍照失败，请重试",
			// Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			restartPreview();
			takePicture();
		}
	}

	@Override
	public void onStop() {

		// stop the preview
		stopCameraPreview();
		if (mCamera != null) {
			mCamera.release();
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		super.onStop();
	}

	@Override
	public void onDestroy() {
		mOrientationListener.disable();
		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mSurfaceHolder = holder;

		if (getCamera(mCameraID)) {
			startCameraPreview();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// The surface is destroyed with the visibility of the SurfaceView is
		// set to View.Invisible
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;

		switch (requestCode) {
		case 1:
			// Uri imageUri = data.getData();
			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * A picture has been taken
	 * 
	 * @param data
	 * @param camera
	 */
	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		int rotation = (mDisplayOrientation
				+ mOrientationListener.getRememberedNormalOrientation() + mLayoutOrientation) % 360;
		camera.startPreview();
		// Bitmap bitmap = ImageUtility.rotatePicture(getActivity(), rotation,
		// data);
		// Uri uri = ImageUtility.savePicture(getActivity(), bitmap);
		if (picCallBack != null) {
			// 前置摄像头拍照颠倒
			if (mCameraID == CameraInfo.CAMERA_FACING_FRONT) {
				if (rotation == 180)
					rotation = 0;
				else if (rotation == 0)
					rotation = 180;

			}
			picCallBack.onPictureTaken(data, rotation);
		}
	}

	/**
	 * When orientation changes, onOrientationChanged(int) of the listener will
	 * be called
	 */
	private static class CameraOrientationListener extends
            OrientationEventListener {

		private int mCurrentNormalizedOrientation;
		private int mRememberedNormalOrientation;

		public CameraOrientationListener(Context context) {
			super(context, SensorManager.SENSOR_DELAY_GAME);
		}

		@Override
		public void onOrientationChanged(int orientation) {
			if (orientation != ORIENTATION_UNKNOWN) {
				mCurrentNormalizedOrientation = normalize(orientation);
			}
		}

		private int normalize(int degrees) {
			if (degrees > 315 || degrees <= 45) {
				return 0;
			}

			if (degrees > 45 && degrees <= 135) {
				return 90;
			}

			if (degrees > 135 && degrees <= 225) {
				return 180;
			}

			if (degrees > 225 && degrees <= 315) {
				return 270;
			}

			throw new RuntimeException(
					"The physics as we know them are no more. Watch out for anomalies.");
		}

		public void rememberOrientation() {

			mRememberedNormalOrientation = mCurrentNormalizedOrientation;
		}

		public int getRememberedNormalOrientation() {
			return mRememberedNormalOrientation;
		}
	}
}
