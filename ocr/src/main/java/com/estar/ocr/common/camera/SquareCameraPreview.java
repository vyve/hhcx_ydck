package com.estar.ocr.common.camera;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class SquareCameraPreview extends SurfaceView {

	public static final String TAG = SquareCameraPreview.class.getSimpleName();
	private static final int INVALID_POINTER_ID = -1;

	private static final int ZOOM_OUT = 0;
	private static final int ZOOM_IN = 1;
	private static final int ZOOM_DELTA = 1;

	private static final int FOCUS_SQR_SIZE = 100;
	private static final int FOCUS_MAX_BOUND = 1000;
	private static final int FOCUS_MIN_BOUND = -FOCUS_MAX_BOUND;

	private static final double ASPECT_RATIO = 3.0 / 4.0;
	private Camera mCamera;

	private float mLastTouchX;
	private float mLastTouchY;

	// For scaling
	private int mMaxZoom;
	private boolean mIsZoomSupported;
	public int mActivePointerId = INVALID_POINTER_ID;
	private int mScaleFactor = 1;
	private ScaleGestureDetector mScaleDetector;

	// For focus
	private boolean mIsFocus;
	private Camera.Area mFocusArea;
	private ArrayList<Camera.Area> mFocusAreas;
	private View focusView;//
	public SquareCameraPreview(Context context) {
		super(context);
		init(context);
	}

	public SquareCameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SquareCameraPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@SuppressLint("NewApi")
	private void init(Context context) {
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		mFocusArea = new Camera.Area(new Rect(), 1000);
		mFocusAreas = new ArrayList<Camera.Area>();
		mFocusAreas.add(mFocusArea);
	}

	/**
	 * Measure the view and its content to determine the measured width and the
	 * measured height
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		if (isScreenLandScape()) {
			if (height < width * ASPECT_RATIO) {
				height = (int) (width * ASPECT_RATIO + 0.5);
			} else {
				width = (int) (height / ASPECT_RATIO + 0.5);
			}
//			height = (int) (width*ASPECT_RATIO+0.5);
		} else {
			if (width > height * ASPECT_RATIO) {
				width = (int) (height * ASPECT_RATIO + 0.5);
			} else {
				height = (int) (width / ASPECT_RATIO + 0.5);
			}
		}

		setMeasuredDimension(width, height);
	}

	public boolean isScreenLandScape() {

		Configuration mConfiguration = this.getResources().getConfiguration(); // 获取设置的配置信息
		int ori = mConfiguration.orientation; // 获取屏幕方向

		if (ori == Configuration.ORIENTATION_LANDSCAPE) {

			// 横屏
			return true;
		} else if (ori == Configuration.ORIENTATION_PORTRAIT) {

			// 竖屏
			return false;
		}
		return false;
	}

	public int getViewWidth() {
		return getWidth();
	}

	public int getViewHeight() {
		return getHeight();
	}

	public void setCamera(Camera camera) {
		mCamera = camera;

		if (camera != null) {
			Camera.Parameters params = camera.getParameters();
			mIsZoomSupported = params.isZoomSupported();
			if (mIsZoomSupported) {
				mMaxZoom = params.getMaxZoom();
			}
		}
	}

	public void setFocusView(View view){
		this.focusView = view;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mScaleDetector.onTouchEvent(event);
	
		final int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			mIsFocus = true;
			
			mLastTouchX = event.getX();
			mLastTouchY = event.getY();

			mActivePointerId = event.getPointerId(0);
			break;
		}
		case MotionEvent.ACTION_UP: {
			if (mIsFocus&&mCamera!=null) {
				handleFocus(mCamera.getParameters());
			}
			if(event.getPointerCount()==1){
				handleStartFocus(event);
			}
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}
		case MotionEvent.ACTION_POINTER_DOWN: {
			mCamera.cancelAutoFocus();
			mIsFocus = false;
			break;
		}
		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}
		}

		return true;
	}

	private void handleZoom(Camera.Parameters params) {
		int zoom = params.getZoom();
		if (mScaleFactor == ZOOM_IN) {
			if (zoom < mMaxZoom)
				zoom += ZOOM_DELTA;
		} else if (mScaleFactor == ZOOM_OUT) {
			if (zoom > 0)
				zoom -= ZOOM_DELTA;
		}
		params.setZoom(zoom);
		mCamera.setParameters(params);
	}
	private void handleStartFocus(MotionEvent event){

	}
	
	private void handleFocus(Camera.Parameters params) {
		float x = mLastTouchX;
		float y = mLastTouchY;

		if (!setFocusBound(x, y))
			return;

		List<String> supportedFocusModes = params.getSupportedFocusModes();
		if (supportedFocusModes != null
				&& supportedFocusModes
						.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
			params.setFocusAreas(mFocusAreas);
			params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			mCamera.setParameters(params);
			mCamera.autoFocus(new Camera.AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					// Callback when the auto focus completes
//					if (focusView != null) {
//						focusView.setBackgroundResource(R.drawable.autofocus_ok);
//						Handler timeHandler = new Handler();
//						timeHandler.post(new Runnable() {
//							public void run() {
//								try {
//									Thread.sleep(500);
//									focusView.setVisibility(View.GONE);
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//							}
//						});
//					}
				}
			});
		}
	}

	private boolean setFocusBound(float x, float y) {
		int left = (int) (x - FOCUS_SQR_SIZE / 2);
		int right = (int) (x + FOCUS_SQR_SIZE / 2);
		int top = (int) (y - FOCUS_SQR_SIZE / 2);
		int bottom = (int) (y + FOCUS_SQR_SIZE / 2);

		if (FOCUS_MIN_BOUND > left || left > FOCUS_MAX_BOUND)
			return false;
		if (FOCUS_MIN_BOUND > right || right > FOCUS_MAX_BOUND)
			return false;
		if (FOCUS_MIN_BOUND > top || top > FOCUS_MAX_BOUND)
			return false;
		if (FOCUS_MIN_BOUND > bottom || bottom > FOCUS_MAX_BOUND)
			return false;

		mFocusArea.rect.set(left, top, right, bottom);

		return true;
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor = (int) detector.getScaleFactor();
			handleZoom(mCamera.getParameters());
			return true;
		}
	}
}
