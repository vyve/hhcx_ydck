package com.flurgle.camerakit;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.flurgle.camerakit.CameraKit.Constants.FLASH_OFF;
import static com.flurgle.camerakit.CameraKit.Constants.FOCUS_CONTINUOUS;
import static com.flurgle.camerakit.CameraKit.Constants.FOCUS_OFF;
import static com.flurgle.camerakit.CameraKit.Constants.FOCUS_TAP;
import static com.flurgle.camerakit.CameraKit.Constants.METHOD_STANDARD;
import static com.flurgle.camerakit.CameraKit.Constants.METHOD_STILL;

@SuppressWarnings("deprecation")
public class Camera1 extends CameraImpl {

    private int mCameraId;
    private Camera mCamera;
    private Camera.Parameters mCameraParameters;
    private Camera.CameraInfo mCameraInfo;
    private Size mPreviewSize;
    private Size mCaptureSize;
    private MediaRecorder mMediaRecorder;
    private File mVideoFile;

    private int mDisplayOrientation;

    @Facing
    private int mFacing;

    @Flash
    private int mFlash;

    @Focus
    private int mFocus;

    @Method
    private int mMethod;

    @Zoom
    private int mZoom;

    private Context context;
    Camera1(CameraListener callback, PreviewImpl preview,Context context) {
        super(callback, preview);
        preview.setCallback(new PreviewImpl.Callback() {
            @Override
            public void onSurfaceChanged() {
                if (mCamera != null) {
                    setupPreview();
                    adjustCameraParameters();
                }
            }
        });

        mCameraInfo = new Camera.CameraInfo();
        this.context = context;

    }

    // CameraImpl:

    @Override
    void start() {
        setFacing(mFacing);
        openCamera();
        if (mPreview.isReady()) setupPreview();
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] bytes, Camera camera) {
//                Toast.makeText(context,bytes.length+"-----------",Toast.LENGTH_SHORT).show();
                mCameraListener.onPreviewCallback(bytes);
//                camera.startPreview();
            }
        });
        mCamera.startPreview();
    }

    @Override
    void stop() {
        if (mCamera != null) mCamera.stopPreview();
        releaseCamera();
    }

    @Override
    void setDisplayOrientation(int displayOrientation) {
        this.mDisplayOrientation = displayOrientation;
    }

    @Override
    void setFacing(@Facing int facing) {
        int internalFacing = new ConstantMapper.Facing(facing).map();
        if (internalFacing == -1) {
            return;
        }

        for (int i = 0, count = Camera.getNumberOfCameras(); i < count; i++) {
            Camera.getCameraInfo(i, mCameraInfo);
            if (mCameraInfo.facing == internalFacing) {
                mCameraId = i;
                mFacing = facing;
                break;
            }
        }

        if (mFacing == facing && isCameraOpened()) {
            stop();
            start();
        }
    }

    @Override
    void setFlash(@Flash int flash) {
        if (mCameraParameters != null) {
            List<String> flashes = mCameraParameters.getSupportedFlashModes();
            String internalFlash = new ConstantMapper.Flash(flash).map();
            if (flashes != null && flashes.contains(internalFlash)) {
                mCameraParameters.setFlashMode(internalFlash);
                mFlash = flash;
            } else {
                String currentFlash = new ConstantMapper.Flash(mFlash).map();
                if (flashes == null || !flashes.contains(currentFlash)) {
                    mCameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mFlash = FLASH_OFF;
                }
            }

            mCamera.setParameters(mCameraParameters);
        } else {
            mFlash = flash;
        }
    }

    @Override
    String getFlash() {
        if(mCameraParameters!=null){
            return mCameraParameters.getFlashMode();
        }
        return "";
    }

    @Override
    void setFocus(@Focus int focus) {
        this.mFocus = focus;
        switch (focus) {
            case FOCUS_CONTINUOUS:
                if (mCameraParameters != null) {
                    final List<String> modes = mCameraParameters.getSupportedFocusModes();
                    if (modes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                        mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    } else {
                        setFocus(FOCUS_OFF);
                    }
                }
                break;

            case FOCUS_TAP:
                setFocus(FOCUS_CONTINUOUS);
                break;

            case FOCUS_OFF:
                if (mCameraParameters != null) {
                    final List<String> modes = mCameraParameters.getSupportedFocusModes();
                    if (modes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
                        mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
                    } else if (modes.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
                        mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
                    } else {
                        mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    }
                }
                break;
        }
    }

    @Override
    void setMethod(@Method int method) {
        this.mMethod = method;
    }

    @Override
    void setZoom(@Zoom int zoom) {
        this.mZoom = zoom;
    }

    @Override
    void captureImage() {
        switch (mMethod) {
            case METHOD_STANDARD:
                mCamera.takePicture(null, null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        mCameraListener.onPictureTaken(data);
                        camera.startPreview();
                    }
                });
                break;

            case METHOD_STILL:
                mCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        new Thread(new ProcessStillTask(data, camera, mCameraInfo, new ProcessStillTask.OnStillProcessedListener() {
                            @Override
                            public void onStillProcessed(final YuvImage yuv) {
                                mCameraListener.onPictureTaken(yuv);
                            }
                        })).start();
                    }
                });
                break;
        }
    }

    @Override
    void startVideo() {
        initMediaRecorder();
        prepareMediaRecorder();
        mMediaRecorder.start();
    }

    @Override
    void endVideo() {
        mMediaRecorder.stop();
        mMediaRecorder = null;
        mCameraListener.onVideoTaken(mVideoFile);
    }

    @Override
    Size getCaptureResolution() {
        if (mCaptureSize == null && mCameraParameters != null) {
            TreeSet<Size> sizes = new TreeSet<>();
            for (Camera.Size size : mCameraParameters.getSupportedPictureSizes()) {
                sizes.add(new Size(size.width, size.height));
            }

            TreeSet<AspectRatio> aspectRatios = findCommonAspectRatios(
                    mCameraParameters.getSupportedPreviewSizes(),
                    mCameraParameters.getSupportedPictureSizes()
            );
            AspectRatio targetRatio = aspectRatios.size() > 0 ? aspectRatios.last() : null;

            Iterator<Size> descendingSizes = sizes.descendingIterator();
            Size size;
            while (descendingSizes.hasNext() && mCaptureSize == null) {
                size = descendingSizes.next();
                if (targetRatio == null || targetRatio.matches(size)) {
                    mCaptureSize = size;
                    break;
                }
            }
        }

        return mCaptureSize;
    }

    @Override
    Size getPreviewResolution() {
        if (mPreviewSize == null && mCameraParameters != null) {
//            TreeSet<Size> sizes = new TreeSet<>();
//            for (Camera.Size size : mCameraParameters.getSupportedPreviewSizes()) {
//                sizes.add(new Size(size.width, size.height));
//            }
//
//            TreeSet<AspectRatio> aspectRatios = findCommonAspectRatios(
//                    mCameraParameters.getSupportedPreviewSizes(),
//                    mCameraParameters.getSupportedPictureSizes()
//            );
//            AspectRatio targetRatio = aspectRatios.size() > 0 ? aspectRatios.last() : null;
//
//            Iterator<Size> descendingSizes = sizes.descendingIterator();
//            Size size;
//            while (descendingSizes.hasNext() && mPreviewSize == null) {
//                size = descendingSizes.next();
//                if (targetRatio == null || targetRatio.matches(size)) {
//                    mPreviewSize = size;
//                    break;
//                }
//            }

            List<Camera.Size> list = mCameraParameters.getSupportedPreviewSizes();
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

           int height =  ScreenUtils.getScreenHeight(context);
            int width = ScreenUtils.getScreenWidth(context);
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
//                preWidth = Third_previewWidth;
//                preHeight = Third_previewheight;
                mPreviewSize = new Size(Third_previewWidth,Third_previewheight);
            }
            else{
//                preWidth = previewWidth;
//                preHeight = previewheight;
                mPreviewSize = new Size(previewWidth,previewheight);

            }
        }
        }
        return mPreviewSize;
    }

    @Override
    boolean isCameraOpened() {
        return mCamera != null;
    }

    @Override
    void autoFocus(Camera.AutoFocusCallback callback) {
        if (mCamera!=null) {
            mCamera.autoFocus(callback);
        }
    }

    // Internal:

    private void openCamera() {
        if (mCamera != null) {
            releaseCamera();
        }

        mCamera = Camera.open(mCameraId);
        mCameraParameters = mCamera.getParameters();

        adjustCameraParameters();


        mCamera.setDisplayOrientation(
                calculateCameraRotation(mDisplayOrientation)
        );

        mCameraListener.onCameraOpened();
    }

    private void setupPreview() {
        try {
            if (mPreview.getOutputClass() == SurfaceHolder.class) {
                mCamera.setPreviewDisplay(mPreview.getSurfaceHolder());
            } else {
                mCamera.setPreviewTexture(mPreview.getSurfaceTexture());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
            mCameraParameters = null;
            mPreviewSize = null;
            mCaptureSize = null;
            mCameraListener.onCameraClosed();
        }
    }

    private int calculateCameraRotation(int rotation) {
        if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return (360 - (mCameraInfo.orientation + rotation) % 360) % 360;
        } else {
            return (mCameraInfo.orientation - rotation + 360) % 360;
        }
    }
    public boolean isScreenLandScape() {

        Configuration mConfiguration = context.getResources().getConfiguration(); // 获取设置的配置信息
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

    private void adjustCameraParameters() {

        if (isScreenLandScape()) {
            mPreview.setTruePreviewSize(
                    getPreviewResolution().getHeight(),
                    getPreviewResolution().getWidth()
            );
        }else {
            mPreview.setTruePreviewSize(
                    getPreviewResolution().getWidth(),
                    getPreviewResolution().getHeight()
            );
        }

        mCameraParameters.setPreviewSize(
                getPreviewResolution().getWidth(),
                getPreviewResolution().getHeight()
        );

        mCameraParameters.setPictureSize(
                getCaptureResolution().getWidth(),
                getCaptureResolution().getHeight()
        );

        mCameraParameters.setRotation(
                calculateCameraRotation(mDisplayOrientation)
                        + (mFacing == CameraKit.Constants.FACING_FRONT ? 180 : 0)
        );
        setFocus(mFocus);
        setFlash(mFlash);
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        mCameraParameters.setPictureFormat(PixelFormat.JPEG);
        mCamera.setParameters(mCameraParameters);
    }

    private TreeSet<AspectRatio> findCommonAspectRatios(List<Camera.Size> previewSizes, List<Camera.Size> captureSizes) {
        Set<AspectRatio> previewAspectRatios = new HashSet<>();
        for (Camera.Size size : previewSizes) {
            if (size.width >= CameraKit.Internal.screenHeight && size.height >= CameraKit.Internal.screenWidth) {
                previewAspectRatios.add(AspectRatio.of(size.width, size.height));
            }
        }

        Set<AspectRatio> captureAspectRatios = new HashSet<>();
        for (Camera.Size size : captureSizes) {
            captureAspectRatios.add(AspectRatio.of(size.width, size.height));
        }

        TreeSet<AspectRatio> output = new TreeSet<>();
        for (AspectRatio aspectRatio : previewAspectRatios) {
            if (captureAspectRatios.contains(aspectRatio)) {
                output.add(aspectRatio);
            }
        }

        return output;
    }

    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();

        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        mVideoFile = new File(mPreview.getView().getContext().getExternalFilesDir(null), "video.mp4");
        mMediaRecorder.setOutputFile(mVideoFile.getAbsolutePath());

        mMediaRecorder.setMaxDuration(20000);
        mMediaRecorder.setMaxFileSize(5000000);
        mMediaRecorder.setOrientationHint(mCameraInfo.orientation);
    }

    private void prepareMediaRecorder() {
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
