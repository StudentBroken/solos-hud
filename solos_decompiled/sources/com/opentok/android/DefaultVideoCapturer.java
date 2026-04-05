package com.opentok.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;
import com.opentok.android.BaseVideoCapturer;
import com.opentok.android.OtLog;
import com.opentok.android.Publisher;
import com.opentok.android.VideoUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes15.dex */
class DefaultVideoCapturer extends BaseVideoCapturer implements Camera.PreviewCallback, BaseVideoCapturer.CaptureSwitch {
    private static final int PIXEL_FORMAT = 17;
    private static final OtLog.LogToken log = new OtLog.LogToken("[camera]", false);
    private Camera camera;
    private int cameraIndex;
    private int[] captureFpsRange;
    private Display currentDisplay;
    int[] frame;
    private Publisher.CameraCaptureFrameRate preferredFramerate;
    private Publisher.CameraCaptureResolution preferredResolution;
    private Publisher publisher;
    private SurfaceTexture surfaceTexture;
    private Camera.CameraInfo currentDeviceInfo = null;
    public ReentrantLock previewBufferLock = new ReentrantLock();
    PixelFormat pixelFormat = new PixelFormat();
    private boolean isCaptureStarted = false;
    private boolean isCaptureRunning = false;
    private final int numCaptureBuffers = 3;
    private int expectedFrameSize = 0;
    private int captureWidth = -1;
    private int captureHeight = -1;
    private boolean blackFrames = false;
    private boolean isCapturePaused = false;
    int fps = 1;
    int width = 0;
    int height = 0;
    Handler handler = new Handler();
    Runnable newFrame = new Runnable() { // from class: com.opentok.android.DefaultVideoCapturer.1
        @Override // java.lang.Runnable
        public void run() {
            if (DefaultVideoCapturer.this.isCaptureRunning) {
                if (DefaultVideoCapturer.this.frame == null) {
                    new VideoUtils.Size();
                    VideoUtils.Size resolution = DefaultVideoCapturer.this.getPreferredResolution();
                    DefaultVideoCapturer.this.fps = DefaultVideoCapturer.this.getPreferredFramerate();
                    DefaultVideoCapturer.this.width = resolution.width;
                    DefaultVideoCapturer.this.height = resolution.height;
                    DefaultVideoCapturer.this.frame = new int[DefaultVideoCapturer.this.width * DefaultVideoCapturer.this.height];
                }
                DefaultVideoCapturer.this.provideIntArrayFrame(DefaultVideoCapturer.this.frame, 2, DefaultVideoCapturer.this.width, DefaultVideoCapturer.this.height, 0, false);
                DefaultVideoCapturer.this.handler.postDelayed(DefaultVideoCapturer.this.newFrame, 1000 / DefaultVideoCapturer.this.fps);
            }
        }
    };

    public DefaultVideoCapturer(Context context, Publisher.CameraCaptureResolution resolution, Publisher.CameraCaptureFrameRate fps) {
        this.cameraIndex = 0;
        this.preferredResolution = Publisher.CameraCaptureResolution.defaultResolution();
        this.preferredFramerate = Publisher.CameraCaptureFrameRate.defaultFrameRate();
        this.cameraIndex = getCameraIndexUsingFront(true);
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        this.currentDisplay = windowManager.getDefaultDisplay();
        this.preferredFramerate = fps;
        this.preferredResolution = resolution;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void init() {
        log.w("init() enetered", new Object[0]);
        try {
            this.camera = Camera.open(this.cameraIndex);
        } catch (RuntimeException exp) {
            log.e(exp, "The camera is in use by another app", new Object[0]);
            this.publisher.onCameraFailed();
        }
        this.currentDeviceInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(this.cameraIndex, this.currentDeviceInfo);
        log.w("init() exit", new Object[0]);
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized int startCapture() {
        int i = -1;
        synchronized (this) {
            log.w("started() entered", new Object[0]);
            if (!this.isCaptureStarted) {
                if (this.camera != null) {
                    VideoUtils.Size resolution = getPreferredResolution();
                    if (configureCaptureSize(resolution.width, resolution.height) != null) {
                        Camera.Parameters parameters = this.camera.getParameters();
                        parameters.setPreviewSize(this.captureWidth, this.captureHeight);
                        parameters.setPreviewFormat(17);
                        parameters.setPreviewFpsRange(this.captureFpsRange[0], this.captureFpsRange[1]);
                        try {
                            this.camera.setParameters(parameters);
                            PixelFormat.getPixelFormatInfo(17, this.pixelFormat);
                            int bufSize = ((this.captureWidth * this.captureHeight) * this.pixelFormat.bitsPerPixel) / 8;
                            for (int i2 = 0; i2 < 3; i2++) {
                                byte[] buffer = new byte[bufSize];
                                this.camera.addCallbackBuffer(buffer);
                            }
                            try {
                                this.surfaceTexture = new SurfaceTexture(42);
                                this.camera.setPreviewTexture(this.surfaceTexture);
                                this.camera.setPreviewCallbackWithBuffer(this);
                                this.camera.startPreview();
                                this.previewBufferLock.lock();
                                this.expectedFrameSize = bufSize;
                                this.previewBufferLock.unlock();
                            } catch (Exception e) {
                                this.publisher.onCameraFailed();
                                e.printStackTrace();
                            }
                        } catch (RuntimeException exp) {
                            log.e(exp, "Camera.setParameters(parameters) - failed", new Object[0]);
                            this.publisher.onCameraFailed();
                        }
                    }
                } else {
                    this.blackFrames = true;
                    this.handler.postDelayed(this.newFrame, 1000 / this.fps);
                }
                this.isCaptureRunning = true;
                this.isCaptureStarted = true;
                log.w("started() exit", new Object[0]);
                i = 0;
            }
        }
        return i;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0038 A[Catch: all -> 0x0053, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0002, B:6:0x0006, B:7:0x000b, B:9:0x000f, B:10:0x002c, B:17:0x0042, B:11:0x0031, B:13:0x0038), top: B:22:0x0002, inners: #1 }] */
    @Override // com.opentok.android.BaseVideoCapturer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized int stopCapture() {
        /*
            r5 = this;
            r1 = 0
            monitor-enter(r5)
            android.hardware.Camera r2 = r5.camera     // Catch: java.lang.Throwable -> L53
            if (r2 == 0) goto L31
            java.util.concurrent.locks.ReentrantLock r2 = r5.previewBufferLock     // Catch: java.lang.Throwable -> L53
            r2.lock()     // Catch: java.lang.Throwable -> L53
            boolean r2 = r5.isCaptureRunning     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
            if (r2 == 0) goto L2c
            r2 = 0
            r5.isCaptureRunning = r2     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
            android.hardware.Camera r2 = r5.camera     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
            r2.stopPreview()     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
            android.hardware.Camera r2 = r5.camera     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
            r3 = 0
            r2.setPreviewCallbackWithBuffer(r3)     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
            android.hardware.Camera r2 = r5.camera     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
            r2.release()     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
            com.opentok.android.OtLog$LogToken r2 = com.opentok.android.DefaultVideoCapturer.log     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
            java.lang.String r3 = "Camera capture is stopped"
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
            r2.d(r3, r4)     // Catch: java.lang.RuntimeException -> L41 java.lang.Throwable -> L53
        L2c:
            java.util.concurrent.locks.ReentrantLock r2 = r5.previewBufferLock     // Catch: java.lang.Throwable -> L53
            r2.unlock()     // Catch: java.lang.Throwable -> L53
        L31:
            r2 = 0
            r5.isCaptureStarted = r2     // Catch: java.lang.Throwable -> L53
            boolean r2 = r5.blackFrames     // Catch: java.lang.Throwable -> L53
            if (r2 == 0) goto L3f
            android.os.Handler r2 = r5.handler     // Catch: java.lang.Throwable -> L53
            java.lang.Runnable r3 = r5.newFrame     // Catch: java.lang.Throwable -> L53
            r2.removeCallbacks(r3)     // Catch: java.lang.Throwable -> L53
        L3f:
            monitor-exit(r5)
            return r1
        L41:
            r0 = move-exception
            com.opentok.android.OtLog$LogToken r1 = com.opentok.android.DefaultVideoCapturer.log     // Catch: java.lang.Throwable -> L53
            java.lang.String r2 = "Camera.stopPreview() - failed "
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L53
            r1.e(r0, r2, r3)     // Catch: java.lang.Throwable -> L53
            com.opentok.android.Publisher r1 = r5.publisher     // Catch: java.lang.Throwable -> L53
            r1.onCameraFailed()     // Catch: java.lang.Throwable -> L53
            r1 = -1
            goto L3f
        L53:
            r1 = move-exception
            monitor-exit(r5)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.DefaultVideoCapturer.stopCapture():int");
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public void destroy() {
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public boolean isCaptureStarted() {
        return this.isCaptureStarted;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public BaseVideoCapturer.CaptureSettings getCaptureSettings() {
        BaseVideoCapturer.CaptureSettings settings = new BaseVideoCapturer.CaptureSettings();
        VideoUtils.Size resolution = getPreferredResolution();
        int framerate = getPreferredFramerate();
        if (this.camera != null && configureCaptureSize(resolution.width, resolution.height) != null) {
            settings.fps = framerate;
            settings.width = resolution.width;
            settings.height = resolution.height;
            settings.format = 1;
            settings.expectedDelay = 0;
        } else {
            settings.fps = framerate;
            settings.width = resolution.width;
            settings.height = resolution.height;
            settings.format = 2;
        }
        return settings;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void onPause() {
        if (this.isCaptureStarted) {
            this.isCapturePaused = true;
            stopCapture();
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public void onResume() {
        if (this.isCapturePaused) {
            init();
            startCapture();
            this.isCapturePaused = false;
        }
    }

    private int getNaturalCameraOrientation() {
        if (this.currentDeviceInfo != null) {
            return this.currentDeviceInfo.orientation;
        }
        return 0;
    }

    public boolean isFrontCamera() {
        return this.currentDeviceInfo != null && this.currentDeviceInfo.facing == 1;
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public int getCameraIndex() {
        return this.cameraIndex;
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public synchronized void cycleCamera() {
        swapCamera((getCameraIndex() + 1) % Camera.getNumberOfCameras());
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    @SuppressLint({"DefaultLocale"})
    public synchronized void swapCamera(int index) {
        boolean wasStarted = this.isCaptureStarted;
        if (this.camera != null) {
            stopCapture();
            this.camera.release();
            this.camera = null;
        }
        this.cameraIndex = index;
        if (wasStarted) {
            if (-1 != Build.MODEL.toLowerCase().indexOf("samsung")) {
                forceCameraRelease(index);
            }
            this.camera = Camera.open(index);
            this.currentDeviceInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(index, this.currentDeviceInfo);
            startCapture();
        }
    }

    private int compensateCameraRotation(int uiRotation) {
        int currentDeviceOrientation = 0;
        switch (uiRotation) {
            case 0:
                currentDeviceOrientation = 0;
                break;
            case 1:
                currentDeviceOrientation = 270;
                break;
            case 2:
                currentDeviceOrientation = 180;
                break;
            case 3:
                currentDeviceOrientation = 90;
                break;
        }
        int cameraOrientation = getNaturalCameraOrientation();
        int cameraRotation = roundRotation(currentDeviceOrientation);
        boolean usingFrontCamera = isFrontCamera();
        if (usingFrontCamera) {
            int inverseCameraRotation = (360 - cameraRotation) % 360;
            int totalCameraRotation = (inverseCameraRotation + cameraOrientation) % 360;
            return totalCameraRotation;
        }
        int totalCameraRotation2 = (cameraRotation + cameraOrientation) % 360;
        return totalCameraRotation2;
    }

    private static int roundRotation(int rotation) {
        return ((int) (Math.round(((double) rotation) / 90.0d) * 90)) % 360;
    }

    private static int getCameraIndexUsingFront(boolean front) {
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (!front || info.facing != 1) {
                if (!front && info.facing == 0) {
                    return i;
                }
            } else {
                return i;
            }
        }
        return 0;
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] data, Camera camera) {
        this.previewBufferLock.lock();
        if (this.isCaptureRunning && data.length == this.expectedFrameSize) {
            int currentRotation = compensateCameraRotation(this.currentDisplay.getRotation());
            provideByteArrayFrame(data, 1, this.captureWidth, this.captureHeight, currentRotation, isFrontCamera());
            camera.addCallbackBuffer(data);
        }
        this.previewBufferLock.unlock();
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    private boolean forceCameraRelease(int cameraIndex) {
        Camera camera = null;
        try {
            Camera camera2 = Camera.open(cameraIndex);
            if (camera2 != null) {
                camera2.release();
            }
            return false;
        } catch (RuntimeException e) {
            if (0 == 0) {
                return true;
            }
            camera.release();
            return true;
        } catch (Throwable th) {
            if (0 != 0) {
                camera.release();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public VideoUtils.Size getPreferredResolution() {
        VideoUtils.Size resolution = new VideoUtils.Size();
        switch (this.preferredResolution) {
            case LOW:
                resolution.width = 352;
                resolution.height = 288;
                return resolution;
            case MEDIUM:
                resolution.width = 640;
                resolution.height = 480;
                return resolution;
            case HIGH:
                resolution.width = 1280;
                resolution.height = 720;
                return resolution;
            default:
                return resolution;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPreferredFramerate() {
        switch (this.preferredFramerate) {
            case FPS_30:
                return 30;
            case FPS_15:
                return 15;
            case FPS_7:
                return 7;
            case FPS_1:
                return 1;
            default:
                return 0;
        }
    }

    private VideoUtils.Size configureCaptureSize(int preferredWidth, int preferredHeight) {
        int preferredFramerate = getPreferredFramerate();
        try {
            try {
                Camera.Parameters parameters = this.camera.getParameters();
                List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
                this.captureFpsRange = findClosestEnclosingFpsRange(preferredFramerate * 1000, parameters.getSupportedPreviewFpsRange());
                int maxw = 0;
                int maxh = 0;
                for (int i = 0; i < sizes.size(); i++) {
                    Camera.Size size = sizes.get(i);
                    if (size.width >= maxw && size.height >= maxh && size.width <= preferredWidth && size.height <= preferredHeight) {
                        maxw = size.width;
                        maxh = size.height;
                    }
                }
                if (maxw == 0 || maxh == 0) {
                    Camera.Size size2 = sizes.get(0);
                    int minw = size2.width;
                    int minh = size2.height;
                    for (int i2 = 1; i2 < sizes.size(); i2++) {
                        Camera.Size size3 = sizes.get(i2);
                        if (size3.width <= minw && size3.height <= minh) {
                            minw = size3.width;
                            minh = size3.height;
                        }
                    }
                    maxw = minw;
                    maxh = minh;
                }
                this.captureWidth = maxw;
                this.captureHeight = maxh;
                VideoUtils.Size retSize = new VideoUtils.Size(maxw, maxh);
                return retSize;
            } catch (RuntimeException exp) {
                log.e(exp, "Error configuring capture size", new Object[0]);
                this.publisher.onCameraFailed();
                return null;
            }
        } catch (Throwable th) {
            return null;
        }
    }

    private int[] findClosestEnclosingFpsRange(int preferredFps, List<int[]> supportedFpsRanges) {
        if (supportedFpsRanges == null || supportedFpsRanges.size() == 0) {
            return new int[]{0, 0};
        }
        if (isFrontCamera() && "samsung-sm-g900a".equals(Build.MODEL.toLowerCase()) && 30000 == preferredFps) {
            preferredFps = 24000;
        }
        final int fps = preferredFps;
        int[] closestRange = (int[]) Collections.min(supportedFpsRanges, new Comparator<int[]>() { // from class: com.opentok.android.DefaultVideoCapturer.2
            private static final int MAX_FPS_DIFF_THRESHOLD = 5000;
            private static final int MAX_FPS_HIGH_DIFF_WEIGHT = 3;
            private static final int MAX_FPS_LOW_DIFF_WEIGHT = 1;
            private static final int MIN_FPS_HIGH_VALUE_WEIGHT = 4;
            private static final int MIN_FPS_LOW_VALUE_WEIGHT = 1;
            private static final int MIN_FPS_THRESHOLD = 8000;

            private int progressivePenalty(int value, int threshold, int lowWeight, int highWeight) {
                return value < threshold ? value * lowWeight : (threshold * lowWeight) + ((value - threshold) * highWeight);
            }

            private int diff(int[] range) {
                int minFpsError = progressivePenalty(range[0], 8000, 1, 4);
                int maxFpsError = progressivePenalty(Math.abs((fps * 1000) - range[1]), MAX_FPS_DIFF_THRESHOLD, 1, 3);
                return minFpsError + maxFpsError;
            }

            @Override // java.util.Comparator
            public int compare(int[] lhs, int[] rhs) {
                return diff(lhs) - diff(rhs);
            }
        });
        checkRangeWithWarning(preferredFps, closestRange);
        return closestRange;
    }

    private void checkRangeWithWarning(int preferredFps, int[] range) {
        if (preferredFps < range[0] || preferredFps > range[1]) {
            log.w("Closest fps range found [%d-%d] for desired fps: %d", Integer.valueOf(range[0] / 1000), Integer.valueOf(range[1] / 1000), Integer.valueOf(preferredFps / 1000));
        }
    }
}
