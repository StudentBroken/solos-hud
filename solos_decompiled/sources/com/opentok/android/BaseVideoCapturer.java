package com.opentok.android;

import com.opentok.android.OpentokError;
import com.opentok.impl.OpentokErrorImpl;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes15.dex */
public abstract class BaseVideoCapturer {
    public static final int ARGB = 2;
    public static final int NV21 = 1;
    private long nativeInstace = 0;
    private PublisherKit publisherKit;

    public static class CaptureSettings {
        public int expectedDelay;
        public int format;
        public int fps;
        public int height;
        public int width;
    }

    public interface CaptureSwitch {
        void cycleCamera();

        int getCameraIndex();

        void swapCamera(int i);
    }

    private native void finalizeNative();

    public abstract void destroy();

    public abstract CaptureSettings getCaptureSettings();

    public abstract void init();

    public abstract boolean isCaptureStarted();

    public abstract void onPause();

    public abstract void onResume();

    public native void provideBufferFrame(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, boolean z);

    public native void provideBufferFramePlanar(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, boolean z);

    public native void provideByteArrayFrame(byte[] bArr, int i, int i2, int i3, int i4, boolean z);

    public native void provideIntArrayFrame(int[] iArr, int i, int i2, int i3, int i4, boolean z);

    public abstract int startCapture();

    public abstract int stopCapture();

    static {
        System.loadLibrary("opentok");
    }

    protected void finalize() throws Throwable {
        finalizeNative();
        super.finalize();
    }

    private void setNativeInstanceId(long instance) {
        this.nativeInstace = instance;
    }

    private long getNativeInstanceId() {
        return this.nativeInstace;
    }

    void onCaptureError() {
        OtLog.i("Error on video capturer", new Object[0]);
        OpentokError.ErrorCode error = OpentokError.ErrorCode.VideoCaptureFailed;
        OpentokError videoCapturerError = new OpentokErrorImpl(OpentokError.Domain.PublisherErrorDomain, error.getErrorCode());
        if (this.publisherKit != null) {
            this.publisherKit.throwError(videoCapturerError);
        }
    }

    protected void setPublisherKit(PublisherKit publisher) {
        this.publisherKit = publisher;
    }

    private int initTrap() {
        try {
            init();
            return 0;
        } catch (RuntimeException e) {
            error();
            return -1;
        }
    }

    private int startCaptureTrap() {
        try {
            return startCapture();
        } catch (RuntimeException e) {
            error();
            return -1;
        }
    }

    private int stopCaptureTrap() {
        try {
            return stopCapture();
        } catch (RuntimeException e) {
            error();
            return -1;
        }
    }

    private void destroyTrap() {
        try {
            destroy();
        } catch (RuntimeException e) {
            error();
        }
    }

    private void error() {
        if (this.publisherKit instanceof Publisher) {
            ((Publisher) this.publisherKit).onCameraFailed();
        } else {
            onCaptureError();
        }
    }
}
