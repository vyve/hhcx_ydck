package com.flurgle.camerakit;

import android.hardware.Camera;

abstract class CameraImpl {

    protected final CameraListener mCameraListener;
    protected final PreviewImpl mPreview;

    CameraImpl(CameraListener callback, PreviewImpl preview) {
        mCameraListener = callback;
        mPreview = preview;
    }

    abstract void start();
    abstract void stop();

    abstract void setDisplayOrientation(int displayOrientation);

    abstract void setFacing(@Facing int facing);
    abstract void setFlash(@Flash int flash);
    abstract String getFlash();
    abstract void setFocus(@Focus int focus);
    abstract void setMethod(@Method int method);
    abstract void setZoom(@Zoom int zoom);

    abstract void captureImage();
    abstract void startVideo();
    abstract void endVideo();

    abstract Size getCaptureResolution();
    abstract Size getPreviewResolution();
    abstract boolean isCameraOpened();

    abstract void autoFocus(Camera.AutoFocusCallback callback);
}
