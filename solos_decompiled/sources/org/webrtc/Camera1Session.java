package org.webrtc;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.SystemClock;
import android.view.WindowManager;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.webrtc.CameraEnumerationAndroid;
import org.webrtc.CameraSession;
import org.webrtc.Metrics;
import org.webrtc.SurfaceTextureHelper;

/* JADX INFO: loaded from: classes57.dex */
public class Camera1Session implements CameraSession {
    private static final int NUMBER_OF_CAPTURE_BUFFERS = 3;
    private static final String TAG = "Camera1Session";
    private final Context applicationContext;
    private final Camera camera;
    private final int cameraId;
    private final Handler cameraThreadHandler;
    private final CameraEnumerationAndroid.CaptureFormat captureFormat;
    private final boolean captureToTexture;
    private final long constructionTimeNs;
    private final CameraSession.Events events;
    private boolean firstFrameReported = false;
    private final int framerate;
    private final int height;
    private final Camera.CameraInfo info;
    private SessionState state;
    private final SurfaceTextureHelper surfaceTextureHelper;
    private final int width;
    private static final Metrics.Histogram camera1StartTimeMsHistogram = Metrics.Histogram.createCounts("WebRTC.Android.Camera1.StartTimeMs", 1, AbstractSpiCall.DEFAULT_TIMEOUT, 50);
    private static final Metrics.Histogram camera1StopTimeMsHistogram = Metrics.Histogram.createCounts("WebRTC.Android.Camera1.StopTimeMs", 1, AbstractSpiCall.DEFAULT_TIMEOUT, 50);
    private static final Metrics.Histogram camera1ResolutionHistogram = Metrics.Histogram.createEnumeration("WebRTC.Android.Camera1.Resolution", CameraEnumerationAndroid.COMMON_RESOLUTIONS.size());

    private enum SessionState {
        RUNNING,
        STOPPED
    }

    public static void create(CameraSession.CreateSessionCallback callback, CameraSession.Events events, boolean captureToTexture, Context applicationContext, SurfaceTextureHelper surfaceTextureHelper, int cameraId, int width, int height, int framerate) {
        long constructionTimeNs = System.nanoTime();
        Logging.d(TAG, "Open camera " + cameraId);
        events.onCameraOpening();
        try {
            Camera camera = Camera.open(cameraId);
            try {
                camera.setPreviewTexture(surfaceTextureHelper.getSurfaceTexture());
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(cameraId, info);
                Camera.Parameters parameters = camera.getParameters();
                CameraEnumerationAndroid.CaptureFormat captureFormat = findClosestCaptureFormat(parameters, width, height, framerate);
                Size pictureSize = findClosestPictureSize(parameters, width, height);
                updateCameraParameters(camera, parameters, captureFormat, pictureSize, captureToTexture);
                if (!captureToTexture) {
                    int frameSize = captureFormat.frameSize();
                    for (int i = 0; i < 3; i++) {
                        ByteBuffer buffer = ByteBuffer.allocateDirect(frameSize);
                        camera.addCallbackBuffer(buffer.array());
                    }
                }
                camera.setDisplayOrientation(0);
                callback.onDone(new Camera1Session(events, captureToTexture, applicationContext, surfaceTextureHelper, cameraId, width, height, framerate, camera, info, captureFormat, constructionTimeNs));
            } catch (IOException e) {
                camera.release();
                callback.onFailure(e.getMessage());
            }
        } catch (RuntimeException e2) {
            callback.onFailure(e2.getMessage());
        }
    }

    private static void updateCameraParameters(Camera camera, Camera.Parameters parameters, CameraEnumerationAndroid.CaptureFormat captureFormat, Size pictureSize, boolean captureToTexture) {
        List<String> focusModes = parameters.getSupportedFocusModes();
        parameters.setPreviewFpsRange(captureFormat.framerate.min, captureFormat.framerate.max);
        parameters.setPreviewSize(captureFormat.width, captureFormat.height);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        if (!captureToTexture) {
            captureFormat.getClass();
            parameters.setPreviewFormat(17);
        }
        if (parameters.isVideoStabilizationSupported()) {
            parameters.setVideoStabilization(true);
        }
        if (focusModes.contains("continuous-video")) {
            parameters.setFocusMode("continuous-video");
        }
        camera.setParameters(parameters);
    }

    private static CameraEnumerationAndroid.CaptureFormat findClosestCaptureFormat(Camera.Parameters parameters, int width, int height, int framerate) {
        List<CameraEnumerationAndroid.CaptureFormat.FramerateRange> supportedFramerates = Camera1Enumerator.convertFramerates(parameters.getSupportedPreviewFpsRange());
        Logging.d(TAG, "Available fps ranges: " + supportedFramerates);
        CameraEnumerationAndroid.CaptureFormat.FramerateRange fpsRange = CameraEnumerationAndroid.getClosestSupportedFramerateRange(supportedFramerates, framerate);
        Size previewSize = CameraEnumerationAndroid.getClosestSupportedSize(Camera1Enumerator.convertSizes(parameters.getSupportedPreviewSizes()), width, height);
        CameraEnumerationAndroid.reportCameraResolution(camera1ResolutionHistogram, previewSize);
        return new CameraEnumerationAndroid.CaptureFormat(previewSize.width, previewSize.height, fpsRange);
    }

    private static Size findClosestPictureSize(Camera.Parameters parameters, int width, int height) {
        return CameraEnumerationAndroid.getClosestSupportedSize(Camera1Enumerator.convertSizes(parameters.getSupportedPictureSizes()), width, height);
    }

    private Camera1Session(CameraSession.Events events, boolean captureToTexture, Context applicationContext, SurfaceTextureHelper surfaceTextureHelper, int cameraId, int width, int height, int framerate, Camera camera, Camera.CameraInfo info, CameraEnumerationAndroid.CaptureFormat captureFormat, long constructionTimeNs) {
        Logging.d(TAG, "Create new camera1 session on camera " + cameraId);
        this.cameraThreadHandler = new Handler();
        this.events = events;
        this.captureToTexture = captureToTexture;
        this.applicationContext = applicationContext;
        this.surfaceTextureHelper = surfaceTextureHelper;
        this.cameraId = cameraId;
        this.width = width;
        this.height = height;
        this.framerate = framerate;
        this.camera = camera;
        this.info = info;
        this.captureFormat = captureFormat;
        this.constructionTimeNs = constructionTimeNs;
        startCapturing();
    }

    @Override // org.webrtc.CameraSession
    public void stop() {
        Logging.d(TAG, "Stop camera1 session on camera " + this.cameraId);
        checkIsOnCameraThread();
        if (this.state != SessionState.STOPPED) {
            long stopStartTime = System.nanoTime();
            this.state = SessionState.STOPPED;
            stopInternal();
            int stopTimeMs = (int) TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - stopStartTime);
            camera1StopTimeMsHistogram.addSample(stopTimeMs);
        }
    }

    private void startCapturing() {
        Logging.d(TAG, "Start capturing");
        checkIsOnCameraThread();
        this.state = SessionState.RUNNING;
        this.camera.setErrorCallback(new Camera.ErrorCallback() { // from class: org.webrtc.Camera1Session.1
            @Override // android.hardware.Camera.ErrorCallback
            public void onError(int error, Camera camera) {
                String errorMessage;
                if (error == 100) {
                    errorMessage = "Camera server died!";
                } else {
                    errorMessage = "Camera error: " + error;
                }
                Logging.e(Camera1Session.TAG, errorMessage);
                Camera1Session.this.state = SessionState.STOPPED;
                Camera1Session.this.stopInternal();
                if (error == 2) {
                    Camera1Session.this.events.onCameraDisconnected(Camera1Session.this);
                } else {
                    Camera1Session.this.events.onCameraError(Camera1Session.this, errorMessage);
                }
            }
        });
        if (this.captureToTexture) {
            listenForTextureFrames();
        } else {
            listenForBytebufferFrames();
        }
        try {
            this.camera.startPreview();
        } catch (RuntimeException e) {
            this.state = SessionState.STOPPED;
            stopInternal();
            this.events.onCameraError(this, e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopInternal() {
        Logging.d(TAG, "Stop internal");
        checkIsOnCameraThread();
        this.surfaceTextureHelper.stopListening();
        this.camera.stopPreview();
        this.camera.release();
        this.events.onCameraClosed(this);
        Logging.d(TAG, "Stop done");
    }

    private void listenForTextureFrames() {
        this.surfaceTextureHelper.startListening(new SurfaceTextureHelper.OnTextureFrameAvailableListener() { // from class: org.webrtc.Camera1Session.2
            @Override // org.webrtc.SurfaceTextureHelper.OnTextureFrameAvailableListener
            public void onTextureFrameAvailable(int oesTextureId, float[] transformMatrix, long timestampNs) {
                Camera1Session.this.checkIsOnCameraThread();
                if (Camera1Session.this.state == SessionState.RUNNING) {
                    if (!Camera1Session.this.firstFrameReported) {
                        int startTimeMs = (int) TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - Camera1Session.this.constructionTimeNs);
                        Camera1Session.camera1StartTimeMsHistogram.addSample(startTimeMs);
                        Camera1Session.this.firstFrameReported = true;
                    }
                    int rotation = Camera1Session.this.getFrameOrientation();
                    if (Camera1Session.this.info.facing == 1) {
                        transformMatrix = RendererCommon.multiplyMatrices(transformMatrix, RendererCommon.horizontalFlipMatrix());
                    }
                    Camera1Session.this.events.onTextureFrameCaptured(Camera1Session.this, Camera1Session.this.captureFormat.width, Camera1Session.this.captureFormat.height, oesTextureId, transformMatrix, rotation, timestampNs);
                    return;
                }
                Logging.d(Camera1Session.TAG, "Texture frame captured but camera is no longer running.");
                Camera1Session.this.surfaceTextureHelper.returnTextureFrame();
            }
        });
    }

    private void listenForBytebufferFrames() {
        this.camera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() { // from class: org.webrtc.Camera1Session.3
            @Override // android.hardware.Camera.PreviewCallback
            public void onPreviewFrame(byte[] data, Camera callbackCamera) {
                Camera1Session.this.checkIsOnCameraThread();
                if (callbackCamera == Camera1Session.this.camera) {
                    if (Camera1Session.this.state != SessionState.RUNNING) {
                        Logging.d(Camera1Session.TAG, "Bytebuffer frame captured but camera is no longer running.");
                        return;
                    }
                    long captureTimeNs = TimeUnit.MILLISECONDS.toNanos(SystemClock.elapsedRealtime());
                    if (!Camera1Session.this.firstFrameReported) {
                        int startTimeMs = (int) TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - Camera1Session.this.constructionTimeNs);
                        Camera1Session.camera1StartTimeMsHistogram.addSample(startTimeMs);
                        Camera1Session.this.firstFrameReported = true;
                    }
                    Camera1Session.this.events.onByteBufferFrameCaptured(Camera1Session.this, data, Camera1Session.this.captureFormat.width, Camera1Session.this.captureFormat.height, Camera1Session.this.getFrameOrientation(), captureTimeNs);
                    Camera1Session.this.camera.addCallbackBuffer(data);
                    return;
                }
                Logging.e(Camera1Session.TAG, "Callback from a different camera. This should never happen.");
            }
        });
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

    /* JADX INFO: Access modifiers changed from: private */
    public int getFrameOrientation() {
        int rotation = getDeviceOrientation();
        if (this.info.facing == 0) {
            rotation = 360 - rotation;
        }
        return (this.info.orientation + rotation) % 360;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsOnCameraThread() {
        if (Thread.currentThread() != this.cameraThreadHandler.getLooper().getThread()) {
            throw new IllegalStateException("Wrong thread");
        }
    }
}
