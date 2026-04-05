package org.webrtc;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.util.Range;
import android.view.Surface;
import android.view.WindowManager;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.webrtc.CameraEnumerationAndroid;
import org.webrtc.CameraSession;
import org.webrtc.Metrics;
import org.webrtc.SurfaceTextureHelper;

/* JADX INFO: loaded from: classes57.dex */
@TargetApi(21)
public class Camera2Session implements CameraSession {
    private static final String TAG = "Camera2Session";
    private final Context applicationContext;
    private final CameraSession.CreateSessionCallback callback;
    private CameraCharacteristics cameraCharacteristics;
    private CameraDevice cameraDevice;
    private final String cameraId;
    private final CameraManager cameraManager;
    private int cameraOrientation;
    private final Handler cameraThreadHandler;
    private CameraEnumerationAndroid.CaptureFormat captureFormat;
    private CameraCaptureSession captureSession;
    private final long constructionTimeNs;
    private final CameraSession.Events events;
    private int fpsUnitFactor;
    private final int framerate;
    private final int height;
    private boolean isCameraFrontFacing;
    private Surface surface;
    private final SurfaceTextureHelper surfaceTextureHelper;
    private final int width;
    private static final Metrics.Histogram camera2StartTimeMsHistogram = Metrics.Histogram.createCounts("WebRTC.Android.Camera2.StartTimeMs", 1, AbstractSpiCall.DEFAULT_TIMEOUT, 50);
    private static final Metrics.Histogram camera2StopTimeMsHistogram = Metrics.Histogram.createCounts("WebRTC.Android.Camera2.StopTimeMs", 1, AbstractSpiCall.DEFAULT_TIMEOUT, 50);
    private static final Metrics.Histogram camera2ResolutionHistogram = Metrics.Histogram.createEnumeration("WebRTC.Android.Camera2.Resolution", CameraEnumerationAndroid.COMMON_RESOLUTIONS.size());
    private SessionState state = SessionState.RUNNING;
    private boolean firstFrameReported = false;

    private enum SessionState {
        RUNNING,
        STOPPED
    }

    private class CameraStateCallback extends CameraDevice.StateCallback {
        private CameraStateCallback() {
        }

        private String getErrorDescription(int errorCode) {
            switch (errorCode) {
                case 1:
                    return "Camera device is in use already.";
                case 2:
                    return "Camera device could not be opened because there are too many other open camera devices.";
                case 3:
                    return "Camera device could not be opened due to a device policy.";
                case 4:
                    return "Camera device has encountered a fatal error.";
                case 5:
                    return "Camera service has encountered a fatal error.";
                default:
                    return "Unknown camera error: " + errorCode;
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice camera) {
            Camera2Session.this.checkIsOnCameraThread();
            boolean startFailure = Camera2Session.this.captureSession == null;
            Camera2Session.this.state = SessionState.STOPPED;
            Camera2Session.this.stopInternal();
            if (startFailure) {
                Camera2Session.this.callback.onFailure("Camera disconnected / evicted.");
            } else {
                Camera2Session.this.events.onCameraDisconnected(Camera2Session.this);
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice camera, int errorCode) {
            Camera2Session.this.checkIsOnCameraThread();
            Camera2Session.this.reportError(getErrorDescription(errorCode));
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice camera) {
            Camera2Session.this.checkIsOnCameraThread();
            Logging.d(Camera2Session.TAG, "Camera opened.");
            Camera2Session.this.cameraDevice = camera;
            SurfaceTexture surfaceTexture = Camera2Session.this.surfaceTextureHelper.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(Camera2Session.this.captureFormat.width, Camera2Session.this.captureFormat.height);
            Camera2Session.this.surface = new Surface(surfaceTexture);
            try {
                camera.createCaptureSession(Arrays.asList(Camera2Session.this.surface), new CaptureSessionCallback(), Camera2Session.this.cameraThreadHandler);
            } catch (CameraAccessException e) {
                Camera2Session.this.reportError("Failed to create capture session. " + e);
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice camera) {
            Camera2Session.this.checkIsOnCameraThread();
            Logging.d(Camera2Session.TAG, "Camera device closed.");
            Camera2Session.this.events.onCameraClosed(Camera2Session.this);
        }
    }

    private class CaptureSessionCallback extends CameraCaptureSession.StateCallback {
        private CaptureSessionCallback() {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession session) {
            Camera2Session.this.checkIsOnCameraThread();
            session.close();
            Camera2Session.this.reportError("Failed to configure capture session.");
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession session) {
            Camera2Session.this.checkIsOnCameraThread();
            Logging.d(Camera2Session.TAG, "Camera capture session configured.");
            Camera2Session.this.captureSession = session;
            try {
                CaptureRequest.Builder captureRequestBuilder = Camera2Session.this.cameraDevice.createCaptureRequest(3);
                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, new Range(Integer.valueOf(Camera2Session.this.captureFormat.framerate.min / Camera2Session.this.fpsUnitFactor), Integer.valueOf(Camera2Session.this.captureFormat.framerate.max / Camera2Session.this.fpsUnitFactor)));
                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, 1);
                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_LOCK, false);
                chooseStabilizationMode(captureRequestBuilder);
                chooseFocusMode(captureRequestBuilder);
                captureRequestBuilder.addTarget(Camera2Session.this.surface);
                session.setRepeatingRequest(captureRequestBuilder.build(), new CameraCaptureCallback(), Camera2Session.this.cameraThreadHandler);
                Camera2Session.this.surfaceTextureHelper.startListening(new SurfaceTextureHelper.OnTextureFrameAvailableListener() { // from class: org.webrtc.Camera2Session.CaptureSessionCallback.1
                    @Override // org.webrtc.SurfaceTextureHelper.OnTextureFrameAvailableListener
                    public void onTextureFrameAvailable(int oesTextureId, float[] transformMatrix, long timestampNs) {
                        Camera2Session.this.checkIsOnCameraThread();
                        if (Camera2Session.this.state == SessionState.RUNNING) {
                            if (!Camera2Session.this.firstFrameReported) {
                                Camera2Session.this.firstFrameReported = true;
                                int startTimeMs = (int) TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - Camera2Session.this.constructionTimeNs);
                                Camera2Session.camera2StartTimeMsHistogram.addSample(startTimeMs);
                            }
                            int rotation = Camera2Session.this.getFrameOrientation();
                            if (Camera2Session.this.isCameraFrontFacing) {
                                transformMatrix = RendererCommon.multiplyMatrices(transformMatrix, RendererCommon.horizontalFlipMatrix());
                            }
                            Camera2Session.this.events.onTextureFrameCaptured(Camera2Session.this, Camera2Session.this.captureFormat.width, Camera2Session.this.captureFormat.height, oesTextureId, RendererCommon.rotateTextureMatrix(transformMatrix, -Camera2Session.this.cameraOrientation), rotation, timestampNs);
                            return;
                        }
                        Logging.d(Camera2Session.TAG, "Texture frame captured but camera is no longer running.");
                        Camera2Session.this.surfaceTextureHelper.returnTextureFrame();
                    }
                });
                Logging.d(Camera2Session.TAG, "Camera device successfully started.");
                Camera2Session.this.callback.onDone(Camera2Session.this);
            } catch (CameraAccessException e) {
                Camera2Session.this.reportError("Failed to start capture request. " + e);
            }
        }

        private void chooseStabilizationMode(CaptureRequest.Builder captureRequestBuilder) {
            int[] availableOpticalStabilization = (int[]) Camera2Session.this.cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION);
            if (availableOpticalStabilization != null) {
                for (int mode : availableOpticalStabilization) {
                    if (mode == 1) {
                        captureRequestBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, 1);
                        captureRequestBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, 0);
                        Logging.d(Camera2Session.TAG, "Using optical stabilization.");
                        return;
                    }
                }
            }
            int[] availableVideoStabilization = (int[]) Camera2Session.this.cameraCharacteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES);
            for (int mode2 : availableVideoStabilization) {
                if (mode2 == 1) {
                    captureRequestBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, 1);
                    captureRequestBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, 0);
                    Logging.d(Camera2Session.TAG, "Using video stabilization.");
                    return;
                }
            }
            Logging.d(Camera2Session.TAG, "Stabilization not available.");
        }

        private void chooseFocusMode(CaptureRequest.Builder captureRequestBuilder) {
            int[] availableFocusModes = (int[]) Camera2Session.this.cameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
            for (int mode : availableFocusModes) {
                if (mode == 3) {
                    captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 3);
                    Logging.d(Camera2Session.TAG, "Using continuous video auto-focus.");
                    return;
                }
            }
            Logging.d(Camera2Session.TAG, "Auto-focus is not available.");
        }
    }

    private class CameraCaptureCallback extends CameraCaptureSession.CaptureCallback {
        private CameraCaptureCallback() {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
            Logging.d(Camera2Session.TAG, "Capture failed: " + failure);
        }
    }

    public static void create(CameraSession.CreateSessionCallback callback, CameraSession.Events events, Context applicationContext, CameraManager cameraManager, SurfaceTextureHelper surfaceTextureHelper, String cameraId, int width, int height, int framerate) {
        new Camera2Session(callback, events, applicationContext, cameraManager, surfaceTextureHelper, cameraId, width, height, framerate);
    }

    private Camera2Session(CameraSession.CreateSessionCallback callback, CameraSession.Events events, Context applicationContext, CameraManager cameraManager, SurfaceTextureHelper surfaceTextureHelper, String cameraId, int width, int height, int framerate) {
        Logging.d(TAG, "Create new camera2 session on camera " + cameraId);
        this.constructionTimeNs = System.nanoTime();
        this.cameraThreadHandler = new Handler();
        this.callback = callback;
        this.events = events;
        this.applicationContext = applicationContext;
        this.cameraManager = cameraManager;
        this.surfaceTextureHelper = surfaceTextureHelper;
        this.cameraId = cameraId;
        this.width = width;
        this.height = height;
        this.framerate = framerate;
        start();
    }

    private void start() {
        checkIsOnCameraThread();
        Logging.d(TAG, "start");
        try {
            this.cameraCharacteristics = this.cameraManager.getCameraCharacteristics(this.cameraId);
        } catch (CameraAccessException e) {
            reportError("getCameraCharacteristics(): " + e.getMessage());
        }
        this.cameraOrientation = ((Integer) this.cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
        this.isCameraFrontFacing = ((Integer) this.cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == 0;
        findCaptureFormat();
        openCamera();
    }

    private void findCaptureFormat() {
        checkIsOnCameraThread();
        Range<Integer>[] fpsRanges = (Range[]) this.cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
        this.fpsUnitFactor = Camera2Enumerator.getFpsUnitFactor(fpsRanges);
        List<CameraEnumerationAndroid.CaptureFormat.FramerateRange> framerateRanges = Camera2Enumerator.convertFramerates(fpsRanges, this.fpsUnitFactor);
        List<Size> sizes = Camera2Enumerator.getSupportedSizes(this.cameraCharacteristics);
        Logging.d(TAG, "Available preview sizes: " + sizes);
        Logging.d(TAG, "Available fps ranges: " + framerateRanges);
        if (framerateRanges.isEmpty() || sizes.isEmpty()) {
            reportError("No supported capture formats.");
        }
        CameraEnumerationAndroid.CaptureFormat.FramerateRange bestFpsRange = CameraEnumerationAndroid.getClosestSupportedFramerateRange(framerateRanges, this.framerate);
        Size bestSize = CameraEnumerationAndroid.getClosestSupportedSize(sizes, this.width, this.height);
        CameraEnumerationAndroid.reportCameraResolution(camera2ResolutionHistogram, bestSize);
        this.captureFormat = new CameraEnumerationAndroid.CaptureFormat(bestSize.width, bestSize.height, bestFpsRange);
        Logging.d(TAG, "Using capture format: " + this.captureFormat);
    }

    private void openCamera() {
        checkIsOnCameraThread();
        Logging.d(TAG, "Opening camera " + this.cameraId);
        this.events.onCameraOpening();
        try {
            this.cameraManager.openCamera(this.cameraId, new CameraStateCallback(), this.cameraThreadHandler);
        } catch (CameraAccessException e) {
            reportError("Failed to open camera: " + e);
        }
    }

    @Override // org.webrtc.CameraSession
    public void stop() {
        Logging.d(TAG, "Stop camera2 session on camera " + this.cameraId);
        checkIsOnCameraThread();
        if (this.state != SessionState.STOPPED) {
            long stopStartTime = System.nanoTime();
            this.state = SessionState.STOPPED;
            stopInternal();
            int stopTimeMs = (int) TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - stopStartTime);
            camera2StopTimeMsHistogram.addSample(stopTimeMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopInternal() {
        Logging.d(TAG, "Stop internal");
        checkIsOnCameraThread();
        this.surfaceTextureHelper.stopListening();
        if (this.captureSession != null) {
            this.captureSession.close();
            this.captureSession = null;
        }
        if (this.surface != null) {
            this.surface.release();
            this.surface = null;
        }
        if (this.cameraDevice != null) {
            this.cameraDevice.close();
            this.cameraDevice = null;
        }
        Logging.d(TAG, "Stop done");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportError(String error) {
        checkIsOnCameraThread();
        Logging.e(TAG, "Error: " + error);
        boolean startFailure = this.captureSession == null;
        this.state = SessionState.STOPPED;
        stopInternal();
        if (startFailure) {
            this.callback.onFailure(error);
        } else {
            this.events.onCameraError(this, error);
        }
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
        if (!this.isCameraFrontFacing) {
            rotation = 360 - rotation;
        }
        return (this.cameraOrientation + rotation) % 360;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsOnCameraThread() {
        if (Thread.currentThread() != this.cameraThreadHandler.getLooper().getThread()) {
            throw new IllegalStateException("Wrong thread");
        }
    }
}
