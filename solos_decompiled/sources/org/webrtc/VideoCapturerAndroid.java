package org.webrtc;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.SystemClock;
import android.view.WindowManager;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.webrtc.CameraEnumerationAndroid;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.Metrics;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;

/* JADX INFO: loaded from: classes57.dex */
@Deprecated
public class VideoCapturerAndroid implements CameraVideoCapturer, Camera.PreviewCallback, SurfaceTextureHelper.OnTextureFrameAvailableListener {
    private static final int CAMERA_STOP_TIMEOUT_MS = 7000;
    private static final int MAX_OPEN_CAMERA_ATTEMPTS = 3;
    private static final int NUMBER_OF_CAPTURE_BUFFERS = 3;
    private static final int OPEN_CAMERA_DELAY_MS = 500;
    private static final String TAG = "VideoCapturerAndroid";
    private Context applicationContext;
    private Camera camera;
    private CameraVideoCapturer.CameraStatistics cameraStatistics;
    private volatile Handler cameraThreadHandler;
    private CameraEnumerationAndroid.CaptureFormat captureFormat;
    private final CameraVideoCapturer.CameraEventsHandler eventsHandler;
    private boolean firstFrameReported;
    private int id;
    private Camera.CameraInfo info;
    private final boolean isCapturingToTexture;
    private int openCameraAttempts;
    private volatile boolean pendingCameraSwitch;
    private int requestedFramerate;
    private int requestedHeight;
    private int requestedWidth;
    private long startStartTimeNs;
    private SurfaceTextureHelper surfaceHelper;
    private static final Metrics.Histogram videoCapturerAndroidStartTimeMsHistogram = Metrics.Histogram.createCounts("WebRTC.Android.VideoCapturerAndroid.StartTimeMs", 1, AbstractSpiCall.DEFAULT_TIMEOUT, 50);
    private static final Metrics.Histogram videoCapturerAndroidStopTimeMsHistogram = Metrics.Histogram.createCounts("WebRTC.Android.VideoCapturerAndroid.StopTimeMs", 1, AbstractSpiCall.DEFAULT_TIMEOUT, 50);
    private static final Metrics.Histogram videoCapturerAndroidResolutionHistogram = Metrics.Histogram.createEnumeration("WebRTC.Android.VideoCapturerAndroid.Resolution", CameraEnumerationAndroid.COMMON_RESOLUTIONS.size());
    private final AtomicBoolean isCameraRunning = new AtomicBoolean();
    private final Object cameraIdLock = new Object();
    private final Object pendingCameraSwitchLock = new Object();
    private VideoCapturer.CapturerObserver frameObserver = null;
    private final Set<byte[]> queuedBuffers = new HashSet();
    private final Camera.ErrorCallback cameraErrorCallback = new Camera.ErrorCallback() { // from class: org.webrtc.VideoCapturerAndroid.1
        @Override // android.hardware.Camera.ErrorCallback
        public void onError(int error, Camera camera) {
            String errorMessage;
            if (error == 100) {
                errorMessage = "Camera server died!";
            } else {
                errorMessage = "Camera error: " + error;
            }
            Logging.e(VideoCapturerAndroid.TAG, errorMessage);
            if (VideoCapturerAndroid.this.eventsHandler != null) {
                if (error == 2) {
                    VideoCapturerAndroid.this.eventsHandler.onCameraDisconnected();
                } else {
                    VideoCapturerAndroid.this.eventsHandler.onCameraError(errorMessage);
                }
            }
        }
    };

    public static VideoCapturerAndroid create(String name, CameraVideoCapturer.CameraEventsHandler eventsHandler) {
        return create(name, eventsHandler, false);
    }

    @Deprecated
    public static VideoCapturerAndroid create(String name, CameraVideoCapturer.CameraEventsHandler eventsHandler, boolean captureToTexture) {
        try {
            return new VideoCapturerAndroid(name, eventsHandler, captureToTexture);
        } catch (RuntimeException e) {
            Logging.e(TAG, "Couldn't create camera.", e);
            return null;
        }
    }

    public void printStackTrace() {
        Thread cameraThread = null;
        if (this.cameraThreadHandler != null) {
            cameraThread = this.cameraThreadHandler.getLooper().getThread();
        }
        if (cameraThread != null) {
            StackTraceElement[] cameraStackTraces = cameraThread.getStackTrace();
            if (cameraStackTraces.length > 0) {
                Logging.d(TAG, "VideoCapturerAndroid stacks trace:");
                for (StackTraceElement stackTrace : cameraStackTraces) {
                    Logging.d(TAG, stackTrace.toString());
                }
            }
        }
    }

    @Override // org.webrtc.CameraVideoCapturer
    public void switchCamera(final CameraVideoCapturer.CameraSwitchHandler switchEventsHandler) {
        if (Camera.getNumberOfCameras() < 2) {
            if (switchEventsHandler != null) {
                switchEventsHandler.onCameraSwitchError("No camera to switch to.");
                return;
            }
            return;
        }
        synchronized (this.pendingCameraSwitchLock) {
            if (this.pendingCameraSwitch) {
                Logging.w(TAG, "Ignoring camera switch request.");
                if (switchEventsHandler != null) {
                    switchEventsHandler.onCameraSwitchError("Pending camera switch already in progress.");
                }
            } else {
                this.pendingCameraSwitch = true;
                boolean didPost = maybePostOnCameraThread(new Runnable() { // from class: org.webrtc.VideoCapturerAndroid.2
                    @Override // java.lang.Runnable
                    public void run() {
                        VideoCapturerAndroid.this.switchCameraOnCameraThread();
                        synchronized (VideoCapturerAndroid.this.pendingCameraSwitchLock) {
                            VideoCapturerAndroid.this.pendingCameraSwitch = false;
                        }
                        if (switchEventsHandler != null) {
                            switchEventsHandler.onCameraSwitchDone(VideoCapturerAndroid.this.info.facing == 1);
                        }
                    }
                });
                if (!didPost && switchEventsHandler != null) {
                    switchEventsHandler.onCameraSwitchError("Camera is stopped.");
                }
            }
        }
    }

    @Override // org.webrtc.VideoCapturer
    public void changeCaptureFormat(final int width, final int height, final int framerate) {
        maybePostOnCameraThread(new Runnable() { // from class: org.webrtc.VideoCapturerAndroid.3
            @Override // java.lang.Runnable
            public void run() {
                VideoCapturerAndroid.this.startPreviewOnCameraThread(width, height, framerate);
            }
        });
    }

    private int getCurrentCameraId() {
        int i;
        synchronized (this.cameraIdLock) {
            i = this.id;
        }
        return i;
    }

    public boolean isCapturingToTexture() {
        return this.isCapturingToTexture;
    }

    public VideoCapturerAndroid(String cameraName, CameraVideoCapturer.CameraEventsHandler eventsHandler, boolean captureToTexture) {
        if (Camera.getNumberOfCameras() == 0) {
            throw new RuntimeException("No cameras available");
        }
        if (cameraName == null || cameraName.equals("")) {
            this.id = 0;
        } else {
            this.id = Camera1Enumerator.getCameraIndex(cameraName);
        }
        this.eventsHandler = eventsHandler;
        this.isCapturingToTexture = captureToTexture;
        Logging.d(TAG, "VideoCapturerAndroid isCapturingToTexture : " + this.isCapturingToTexture);
    }

    private void checkIsOnCameraThread() {
        if (this.cameraThreadHandler == null) {
            Logging.e(TAG, "Camera is not initialized - can't check thread.");
        } else if (Thread.currentThread() != this.cameraThreadHandler.getLooper().getThread()) {
            throw new IllegalStateException("Wrong thread");
        }
    }

    private boolean maybePostOnCameraThread(Runnable runnable) {
        return maybePostDelayedOnCameraThread(0, runnable);
    }

    private boolean maybePostDelayedOnCameraThread(int delayMs, Runnable runnable) {
        return this.cameraThreadHandler != null && this.isCameraRunning.get() && this.cameraThreadHandler.postAtTime(runnable, this, SystemClock.uptimeMillis() + ((long) delayMs));
    }

    @Override // org.webrtc.VideoCapturer
    public void dispose() {
        Logging.d(TAG, "dispose");
    }

    private boolean isInitialized() {
        return (this.applicationContext == null || this.frameObserver == null) ? false : true;
    }

    @Override // org.webrtc.VideoCapturer
    public void initialize(SurfaceTextureHelper surfaceTextureHelper, Context applicationContext, VideoCapturer.CapturerObserver frameObserver) {
        Logging.d(TAG, "initialize");
        if (applicationContext == null) {
            throw new IllegalArgumentException("applicationContext not set.");
        }
        if (frameObserver == null) {
            throw new IllegalArgumentException("frameObserver not set.");
        }
        if (isInitialized()) {
            throw new IllegalStateException("Already initialized");
        }
        this.applicationContext = applicationContext;
        this.frameObserver = frameObserver;
        this.surfaceHelper = surfaceTextureHelper;
        this.cameraThreadHandler = surfaceTextureHelper == null ? null : surfaceTextureHelper.getHandler();
    }

    @Override // org.webrtc.VideoCapturer
    public void startCapture(final int width, final int height, final int framerate) {
        Logging.d(TAG, "startCapture requested: " + width + "x" + height + "@" + framerate);
        if (!isInitialized()) {
            throw new IllegalStateException("startCapture called in uninitialized state");
        }
        if (this.surfaceHelper == null) {
            this.frameObserver.onCapturerStarted(false);
            if (this.eventsHandler != null) {
                this.eventsHandler.onCameraError("No SurfaceTexture created.");
                return;
            }
            return;
        }
        if (this.isCameraRunning.getAndSet(true)) {
            Logging.e(TAG, "Camera has already been started.");
            return;
        }
        boolean didPost = maybePostOnCameraThread(new Runnable() { // from class: org.webrtc.VideoCapturerAndroid.4
            @Override // java.lang.Runnable
            public void run() {
                VideoCapturerAndroid.this.openCameraAttempts = 0;
                VideoCapturerAndroid.this.startCaptureOnCameraThread(width, height, framerate);
            }
        });
        if (!didPost) {
            this.frameObserver.onCapturerStarted(false);
            if (this.eventsHandler != null) {
                this.eventsHandler.onCameraError("Could not post task to camera thread.");
            }
            this.isCameraRunning.set(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startCaptureOnCameraThread(final int width, final int height, final int framerate) {
        checkIsOnCameraThread();
        this.startStartTimeNs = System.nanoTime();
        if (!this.isCameraRunning.get()) {
            Logging.e(TAG, "startCaptureOnCameraThread: Camera is stopped");
            return;
        }
        if (this.camera != null) {
            Logging.e(TAG, "startCaptureOnCameraThread: Camera has already been started.");
            return;
        }
        this.firstFrameReported = false;
        try {
            try {
                try {
                    synchronized (this.cameraIdLock) {
                        Logging.d(TAG, "Opening camera " + this.id);
                        if (this.eventsHandler != null) {
                            this.eventsHandler.onCameraOpening(Camera1Enumerator.getDeviceName(this.id));
                        }
                        this.camera = Camera.open(this.id);
                        this.info = new Camera.CameraInfo();
                        Camera.getCameraInfo(this.id, this.info);
                    }
                    this.camera.setPreviewTexture(this.surfaceHelper.getSurfaceTexture());
                    Logging.d(TAG, "Camera orientation: " + this.info.orientation + " .Device orientation: " + getDeviceOrientation());
                    this.camera.setErrorCallback(this.cameraErrorCallback);
                    startPreviewOnCameraThread(width, height, framerate);
                    this.frameObserver.onCapturerStarted(true);
                    if (this.isCapturingToTexture) {
                        this.surfaceHelper.startListening(this);
                    }
                    this.cameraStatistics = new CameraVideoCapturer.CameraStatistics(this.surfaceHelper, this.eventsHandler);
                } catch (RuntimeException e) {
                    e = e;
                    Logging.e(TAG, "startCapture failed", e);
                    stopCaptureOnCameraThread(true);
                    this.frameObserver.onCapturerStarted(false);
                    if (this.eventsHandler != null) {
                        this.eventsHandler.onCameraError("Camera can not be started.");
                    }
                }
            } catch (RuntimeException e2) {
                this.openCameraAttempts++;
                if (this.openCameraAttempts < 3) {
                    Logging.e(TAG, "Camera.open failed, retrying", e2);
                    maybePostDelayedOnCameraThread(OPEN_CAMERA_DELAY_MS, new Runnable() { // from class: org.webrtc.VideoCapturerAndroid.5
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoCapturerAndroid.this.startCaptureOnCameraThread(width, height, framerate);
                        }
                    });
                    return;
                }
                throw e2;
            }
        } catch (IOException e3) {
            e = e3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPreviewOnCameraThread(int width, int height, int framerate) {
        checkIsOnCameraThread();
        if (!this.isCameraRunning.get() || this.camera == null) {
            Logging.e(TAG, "startPreviewOnCameraThread: Camera is stopped");
            return;
        }
        Logging.d(TAG, "startPreviewOnCameraThread requested: " + width + "x" + height + "@" + framerate);
        this.requestedWidth = width;
        this.requestedHeight = height;
        this.requestedFramerate = framerate;
        Camera.Parameters parameters = this.camera.getParameters();
        List<CameraEnumerationAndroid.CaptureFormat.FramerateRange> supportedFramerates = Camera1Enumerator.convertFramerates(parameters.getSupportedPreviewFpsRange());
        Logging.d(TAG, "Available fps ranges: " + supportedFramerates);
        CameraEnumerationAndroid.CaptureFormat.FramerateRange fpsRange = CameraEnumerationAndroid.getClosestSupportedFramerateRange(supportedFramerates, framerate);
        List<Size> supportedPreviewSizes = Camera1Enumerator.convertSizes(parameters.getSupportedPreviewSizes());
        Size previewSize = CameraEnumerationAndroid.getClosestSupportedSize(supportedPreviewSizes, width, height);
        CameraEnumerationAndroid.reportCameraResolution(videoCapturerAndroidResolutionHistogram, previewSize);
        Logging.d(TAG, "Available preview sizes: " + supportedPreviewSizes);
        CameraEnumerationAndroid.CaptureFormat captureFormat = new CameraEnumerationAndroid.CaptureFormat(previewSize.width, previewSize.height, fpsRange);
        if (!captureFormat.equals(this.captureFormat)) {
            Logging.d(TAG, "isVideoStabilizationSupported: " + parameters.isVideoStabilizationSupported());
            if (parameters.isVideoStabilizationSupported()) {
                parameters.setVideoStabilization(true);
            }
            if (captureFormat.framerate.max > 0) {
                parameters.setPreviewFpsRange(captureFormat.framerate.min, captureFormat.framerate.max);
            }
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            if (!this.isCapturingToTexture) {
                captureFormat.getClass();
                parameters.setPreviewFormat(17);
            }
            Size pictureSize = CameraEnumerationAndroid.getClosestSupportedSize(Camera1Enumerator.convertSizes(parameters.getSupportedPictureSizes()), width, height);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            if (this.captureFormat != null) {
                this.camera.stopPreview();
                this.camera.setPreviewCallbackWithBuffer(null);
            }
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                Logging.d(TAG, "Enable continuous auto focus mode.");
                parameters.setFocusMode("continuous-video");
            }
            Logging.d(TAG, "Start capturing: " + captureFormat);
            this.captureFormat = captureFormat;
            this.camera.setParameters(parameters);
            this.camera.setDisplayOrientation(0);
            if (!this.isCapturingToTexture) {
                this.queuedBuffers.clear();
                int frameSize = captureFormat.frameSize();
                for (int i = 0; i < 3; i++) {
                    ByteBuffer buffer = ByteBuffer.allocateDirect(frameSize);
                    this.queuedBuffers.add(buffer.array());
                    this.camera.addCallbackBuffer(buffer.array());
                }
                this.camera.setPreviewCallbackWithBuffer(this);
            }
            this.camera.startPreview();
        }
    }

    @Override // org.webrtc.VideoCapturer
    public void stopCapture() throws InterruptedException {
        Logging.d(TAG, "stopCapture");
        final CountDownLatch barrier = new CountDownLatch(1);
        boolean didPost = maybePostOnCameraThread(new Runnable() { // from class: org.webrtc.VideoCapturerAndroid.6
            @Override // java.lang.Runnable
            public void run() {
                VideoCapturerAndroid.this.stopCaptureOnCameraThread(true);
                barrier.countDown();
            }
        });
        if (!didPost) {
            Logging.e(TAG, "Calling stopCapture() for already stopped camera.");
            return;
        }
        if (!barrier.await(7000L, TimeUnit.MILLISECONDS)) {
            Logging.e(TAG, "Camera stop timeout");
            printStackTrace();
            if (this.eventsHandler != null) {
                this.eventsHandler.onCameraError("Camera stop timeout");
            }
        }
        this.frameObserver.onCapturerStopped();
        Logging.d(TAG, "stopCapture done");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopCaptureOnCameraThread(boolean stopHandler) {
        checkIsOnCameraThread();
        Logging.d(TAG, "stopCaptureOnCameraThread");
        long stopStartTime = System.nanoTime();
        if (this.surfaceHelper != null) {
            this.surfaceHelper.stopListening();
        }
        if (stopHandler) {
            this.isCameraRunning.set(false);
            this.cameraThreadHandler.removeCallbacksAndMessages(this);
        }
        if (this.cameraStatistics != null) {
            this.cameraStatistics.release();
            this.cameraStatistics = null;
        }
        Logging.d(TAG, "Stop preview.");
        if (this.camera != null) {
            this.camera.stopPreview();
            this.camera.setPreviewCallbackWithBuffer(null);
        }
        this.queuedBuffers.clear();
        this.captureFormat = null;
        Logging.d(TAG, "Release camera.");
        if (this.camera != null) {
            this.camera.release();
            this.camera = null;
        }
        if (this.eventsHandler != null) {
            this.eventsHandler.onCameraClosed();
        }
        int stopTimeMs = (int) TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - stopStartTime);
        videoCapturerAndroidStopTimeMsHistogram.addSample(stopTimeMs);
        Logging.d(TAG, "stopCaptureOnCameraThread done");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchCameraOnCameraThread() {
        checkIsOnCameraThread();
        if (!this.isCameraRunning.get()) {
            Logging.e(TAG, "switchCameraOnCameraThread: Camera is stopped");
            return;
        }
        Logging.d(TAG, "switchCameraOnCameraThread");
        stopCaptureOnCameraThread(false);
        synchronized (this.cameraIdLock) {
            this.id = (this.id + 1) % Camera.getNumberOfCameras();
        }
        startCaptureOnCameraThread(this.requestedWidth, this.requestedHeight, this.requestedFramerate);
        Logging.d(TAG, "switchCameraOnCameraThread done");
    }

    private int getDeviceOrientation() {
        WindowManager wm = (WindowManager) this.applicationContext.getSystemService("window");
        switch (wm.getDefaultDisplay().getRotation()) {
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
            default:
                return 0;
        }
    }

    private int getFrameOrientation() {
        int rotation = getDeviceOrientation();
        if (this.info.facing == 0) {
            rotation = 360 - rotation;
        }
        return (this.info.orientation + rotation) % 360;
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] data, Camera callbackCamera) {
        checkIsOnCameraThread();
        if (!this.isCameraRunning.get()) {
            Logging.e(TAG, "onPreviewFrame: Camera is stopped");
            return;
        }
        if (this.queuedBuffers.contains(data)) {
            if (this.camera != callbackCamera) {
                throw new RuntimeException("Unexpected camera in callback!");
            }
            long captureTimeNs = TimeUnit.MILLISECONDS.toNanos(SystemClock.elapsedRealtime());
            if (!this.firstFrameReported) {
                onFirstFrameAvailable();
            }
            this.cameraStatistics.addFrame();
            this.frameObserver.onByteBufferFrameCaptured(data, this.captureFormat.width, this.captureFormat.height, getFrameOrientation(), captureTimeNs);
            this.camera.addCallbackBuffer(data);
        }
    }

    @Override // org.webrtc.SurfaceTextureHelper.OnTextureFrameAvailableListener
    public void onTextureFrameAvailable(int oesTextureId, float[] transformMatrix, long timestampNs) {
        checkIsOnCameraThread();
        if (!this.isCameraRunning.get()) {
            Logging.e(TAG, "onTextureFrameAvailable: Camera is stopped");
            this.surfaceHelper.returnTextureFrame();
            return;
        }
        int rotation = getFrameOrientation();
        if (this.info.facing == 1) {
            transformMatrix = RendererCommon.multiplyMatrices(transformMatrix, RendererCommon.horizontalFlipMatrix());
        }
        if (!this.firstFrameReported) {
            onFirstFrameAvailable();
        }
        this.cameraStatistics.addFrame();
        this.frameObserver.onTextureFrameCaptured(this.captureFormat.width, this.captureFormat.height, oesTextureId, transformMatrix, rotation, timestampNs);
    }

    private void onFirstFrameAvailable() {
        if (this.eventsHandler != null) {
            this.eventsHandler.onFirstFrameAvailable();
        }
        int startTimeMs = (int) TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this.startStartTimeNs);
        videoCapturerAndroidStartTimeMsHistogram.addSample(startTimeMs);
        this.firstFrameReported = true;
    }

    @Override // org.webrtc.VideoCapturer
    public boolean isScreencast() {
        return false;
    }
}
