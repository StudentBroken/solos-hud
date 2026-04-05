package com.opentok.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Range;
import android.util.Size;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.WindowManager;
import com.opentok.android.BaseVideoCapturer;
import com.opentok.android.OtLog;
import com.opentok.android.Publisher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes15.dex */
@TargetApi(21)
class Camera2VideoCapturer extends BaseVideoCapturer implements BaseVideoCapturer.CaptureSwitch {
    private static final int PIXEL_FORMAT = 35;
    private static final int PREFERRED_FACING_CAMERA = 0;
    private static final boolean debug = false;
    private Range<Integer> camFps;
    private Handler camHandler;
    private HandlerThread camThread;
    private ImageReader cameraFrame;
    private int cameraIndex;
    private CameraManager cameraManager;
    private CaptureRequest captureRequest;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession captureSession;
    private int desiredFps;
    private Display display;
    private Size frameDimensions;
    private static final SparseIntArray rotationTable = new SparseIntArray() { // from class: com.opentok.android.Camera2VideoCapturer.1
        {
            append(0, 0);
            append(1, 90);
            append(2, 180);
            append(3, 270);
        }
    };
    private static final SparseArray<Size> resolutionTable = new SparseArray<Size>() { // from class: com.opentok.android.Camera2VideoCapturer.2
        {
            append(Publisher.CameraCaptureResolution.LOW.ordinal(), new Size(352, 288));
            append(Publisher.CameraCaptureResolution.MEDIUM.ordinal(), new Size(640, 480));
            append(Publisher.CameraCaptureResolution.HIGH.ordinal(), new Size(1280, 720));
        }
    };
    private static final SparseIntArray frameRateTable = new SparseIntArray() { // from class: com.opentok.android.Camera2VideoCapturer.3
        {
            append(Publisher.CameraCaptureFrameRate.FPS_1.ordinal(), 1);
            append(Publisher.CameraCaptureFrameRate.FPS_7.ordinal(), 7);
            append(Publisher.CameraCaptureFrameRate.FPS_15.ordinal(), 15);
            append(Publisher.CameraCaptureFrameRate.FPS_30.ordinal(), 30);
        }
    };
    private OtLog.LogToken log = new OtLog.LogToken("[camera2]", false);
    private CameraDevice.StateCallback cameraObserver = new CameraDevice.StateCallback() { // from class: com.opentok.android.Camera2VideoCapturer.4
        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice camera) {
            Camera2VideoCapturer.this.log.d("onOpened", new Object[0]);
            Camera2VideoCapturer.this.cameraState = CameraState.OPEN;
            Camera2VideoCapturer.this.camera = camera;
            Camera2VideoCapturer.this.signalCamStateChange();
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice camera) {
            try {
                Camera2VideoCapturer.this.log.d("onDisconnected", new Object[0]);
                Camera2VideoCapturer.this.camera.close();
                Camera2VideoCapturer.this.waitForCamStateChange(Camera2VideoCapturer.this.cameraState);
            } catch (NullPointerException e) {
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice camera, int error) {
            try {
                Camera2VideoCapturer.this.log.d("onError", new Object[0]);
                Camera2VideoCapturer.this.camera.close();
                Camera2VideoCapturer.this.waitForCamStateChange(Camera2VideoCapturer.this.cameraState);
            } catch (NullPointerException e) {
            }
            Camera2VideoCapturer.this.postAsyncException(new Camera2Exception("Camera Open Error: " + error));
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice camera) {
            Camera2VideoCapturer.this.log.d("onClosed", new Object[0]);
            super.onClosed(camera);
            Camera2VideoCapturer.this.cameraState = CameraState.CLOSED;
            Camera2VideoCapturer.this.camera = null;
            Camera2VideoCapturer.this.signalCamStateChange();
        }
    };
    private ImageReader.OnImageAvailableListener frameObserver = new ImageReader.OnImageAvailableListener() { // from class: com.opentok.android.Camera2VideoCapturer.5
        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader reader) {
            Image frame = reader.acquireNextImage();
            if (CameraState.CAPTURE == Camera2VideoCapturer.this.cameraState) {
                Camera2VideoCapturer.this.provideBufferFramePlanar(frame.getPlanes()[0].getBuffer(), frame.getPlanes()[1].getBuffer(), frame.getPlanes()[2].getBuffer(), frame.getPlanes()[0].getPixelStride(), frame.getPlanes()[0].getRowStride(), frame.getPlanes()[1].getPixelStride(), frame.getPlanes()[1].getRowStride(), frame.getPlanes()[2].getPixelStride(), frame.getPlanes()[2].getRowStride(), frame.getWidth(), frame.getHeight(), Camera2VideoCapturer.this.calculateCamRotation(), Camera2VideoCapturer.this.isFrontCamera());
            }
            frame.close();
        }
    };
    private CameraCaptureSession.StateCallback captureSessionObserver = new CameraCaptureSession.StateCallback() { // from class: com.opentok.android.Camera2VideoCapturer.6
        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession session) {
            try {
                Camera2VideoCapturer.this.cameraState = CameraState.CAPTURE;
                Camera2VideoCapturer.this.captureSession = session;
                Camera2VideoCapturer.this.captureRequest = Camera2VideoCapturer.this.captureRequestBuilder.build();
                Camera2VideoCapturer.this.captureSession.setRepeatingRequest(Camera2VideoCapturer.this.captureRequest, Camera2VideoCapturer.this.captureNotification, Camera2VideoCapturer.this.camHandler);
                Camera2VideoCapturer.this.signalCamStateChange();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession session) {
            Camera2VideoCapturer.this.cameraState = CameraState.ERROR;
            Camera2VideoCapturer.this.postAsyncException(new Camera2Exception("Camera session configuration failed"));
            Camera2VideoCapturer.this.signalCamStateChange();
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onClosed(CameraCaptureSession session) {
            Camera2VideoCapturer.this.camera.close();
        }
    };
    private CameraCaptureSession.CaptureCallback captureNotification = new CameraCaptureSession.CaptureCallback() { // from class: com.opentok.android.Camera2VideoCapturer.7
        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }
    };
    private CameraDevice camera = null;
    private CameraState cameraState = CameraState.CLOSED;
    private ReentrantLock reentrantLock = new ReentrantLock();
    private Condition condition = this.reentrantLock.newCondition();
    private List<RuntimeException> runtimeExceptionList = new ArrayList();
    private boolean isPaused = false;

    private enum CameraState {
        CLOSED,
        SETUP,
        OPEN,
        CAPTURE,
        ERROR
    }

    public static class Camera2Exception extends RuntimeException {
        public Camera2Exception(String message) {
            super(message);
        }
    }

    public Camera2VideoCapturer(Context ctx, Publisher.CameraCaptureResolution resolution, Publisher.CameraCaptureFrameRate fps) {
        this.cameraManager = (CameraManager) ctx.getSystemService("camera");
        this.display = ((WindowManager) ctx.getSystemService("window")).getDefaultDisplay();
        this.frameDimensions = resolutionTable.get(resolution.ordinal());
        this.desiredFps = frameRateTable.get(fps.ordinal());
        try {
            String camId = selectCamera(0);
            this.cameraIndex = findCameraIndex(camId);
        } catch (CameraAccessException e) {
            throw new Camera2Exception(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void init() {
        this.log.d("init entered", new Object[0]);
        startCamThread();
        initCamera();
        this.log.d("init Exit", new Object[0]);
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized int startCapture() {
        this.log.d("startCapture entered", new Object[0]);
        if (this.camera != null && CameraState.OPEN == this.cameraState) {
            try {
                if (isFrontCamera()) {
                    this.captureRequestBuilder = this.camera.createCaptureRequest(1);
                    this.captureRequestBuilder.addTarget(this.cameraFrame.getSurface());
                    this.captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.camFps);
                    this.captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, 2);
                    this.captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 4);
                    this.captureRequestBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, 1);
                    this.camera.createCaptureSession(Arrays.asList(this.cameraFrame.getSurface()), this.captureSessionObserver, this.camHandler);
                } else {
                    this.captureRequestBuilder = this.camera.createCaptureRequest(3);
                    this.captureRequestBuilder.addTarget(this.cameraFrame.getSurface());
                    this.captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.camFps);
                    this.camera.createCaptureSession(Arrays.asList(this.cameraFrame.getSurface()), this.captureSessionObserver, this.camHandler);
                }
                waitForCamStateChange(CameraState.OPEN);
            } catch (CameraAccessException e) {
                throw new Camera2Exception(e.getMessage());
            }
        } else {
            throw new Camera2Exception("Start Capture called before init successfully completed.");
        }
        return 0;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized int stopCapture() {
        this.log.d("stopCapture entered", new Object[0]);
        if (this.camera != null && this.captureSession != null && CameraState.CLOSED != this.cameraState) {
            CameraState oldState = this.cameraState;
            this.captureSession.close();
            waitForCamStateChange(oldState);
            this.cameraFrame.close();
        }
        this.log.d("stopCapture exited", new Object[0]);
        return 0;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void destroy() {
        stopCamThread();
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public boolean isCaptureStarted() {
        return this.cameraState == CameraState.CAPTURE;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized BaseVideoCapturer.CaptureSettings getCaptureSettings() {
        BaseVideoCapturer.CaptureSettings retObj;
        synchronized (this) {
            retObj = new BaseVideoCapturer.CaptureSettings();
            retObj.fps = this.desiredFps;
            retObj.width = this.cameraFrame != null ? this.cameraFrame.getWidth() : 0;
            retObj.height = this.cameraFrame != null ? this.cameraFrame.getHeight() : 0;
            retObj.format = 1;
            retObj.expectedDelay = 0;
        }
        return retObj;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void onPause() {
        this.log.d("OnPause", new Object[0]);
        switch (this.cameraState) {
            case CAPTURE:
                stopCapture();
                this.isPaused = true;
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public void onResume() {
        this.log.d("onResume", new Object[0]);
        if (this.isPaused) {
            initCamera();
            startCapture();
            this.isPaused = false;
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public synchronized void cycleCamera() {
        try {
            String[] camLst = this.cameraManager.getCameraIdList();
            swapCamera((this.cameraIndex + 1) % camLst.length);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            throw new Camera2Exception(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public int getCameraIndex() {
        return this.cameraIndex;
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public synchronized void swapCamera(int cameraId) {
        CameraState oldState = this.cameraState;
        switch (oldState) {
            case CAPTURE:
                stopCapture();
                break;
        }
        this.cameraIndex = cameraId;
        switch (oldState) {
            case CAPTURE:
                initCamera();
                startCapture();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFrontCamera() {
        try {
            CameraCharacteristics characteristics = this.cameraManager.getCameraCharacteristics(this.camera.getId());
            if (characteristics == null) {
                return false;
            }
            Integer lensFacing = (Integer) characteristics.get(CameraCharacteristics.LENS_FACING);
            return lensFacing.intValue() == 0;
        } catch (CameraAccessException e) {
            this.log.d("Error accesing camera characteristics", new Object[0]);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void waitForCamStateChange(CameraState oldState) throws RuntimeException {
        this.reentrantLock.lock();
        try {
            this.log.d("wait for change from " + oldState, new Object[0]);
            while (oldState == this.cameraState) {
                this.condition.await();
            }
        } catch (InterruptedException e) {
            waitForCamStateChange(oldState);
        }
        this.reentrantLock.unlock();
        Iterator<RuntimeException> it = this.runtimeExceptionList.iterator();
        if (it.hasNext()) {
            RuntimeException e2 = it.next();
            throw e2;
        }
        this.runtimeExceptionList.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void signalCamStateChange() {
        this.reentrantLock.lock();
        this.condition.signalAll();
        this.reentrantLock.unlock();
    }

    private void startCamThread() {
        this.camThread = new HandlerThread("Camera-Thread");
        this.camThread.start();
        this.camHandler = new Handler(this.camThread.getLooper());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void stopCamThread() {
        try {
            this.camThread.quitSafely();
            this.camThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e2) {
        } finally {
            this.camThread = null;
            this.camHandler = null;
        }
    }

    private String selectCamera(int lenseDirection) throws CameraAccessException {
        for (String id : this.cameraManager.getCameraIdList()) {
            CameraCharacteristics info = this.cameraManager.getCameraCharacteristics(id);
            if (lenseDirection == ((Integer) info.get(CameraCharacteristics.LENS_FACING)).intValue()) {
                return id;
            }
        }
        return null;
    }

    private Range<Integer> selectCameraFpsRange(String camId, final int fps) throws CameraAccessException {
        for (String id : this.cameraManager.getCameraIdList()) {
            if (id.equals(camId)) {
                CameraCharacteristics info = this.cameraManager.getCameraCharacteristics(id);
                List<Range<Integer>> fpsLst = new ArrayList<>();
                Collections.addAll(fpsLst, (Object[]) info.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES));
                return (Range) Collections.min(fpsLst, new Comparator<Range<Integer>>() { // from class: com.opentok.android.Camera2VideoCapturer.8
                    private static final int MAX_FPS_DIFF_THRESHOLD = 5000;
                    private static final int MAX_FPS_HIGH_DIFF_WEIGHT = 3;
                    private static final int MAX_FPS_LOW_DIFF_WEIGHT = 1;
                    private static final int MIN_FPS_HIGH_VALUE_WEIGHT = 4;
                    private static final int MIN_FPS_LOW_VALUE_WEIGHT = 1;
                    private static final int MIN_FPS_THRESHOLD = 8000;

                    private int progressivePenalty(int value, int threshold, int lowWeight, int highWeight) {
                        return value < threshold ? value * lowWeight : (threshold * lowWeight) + ((value - threshold) * highWeight);
                    }

                    private int diff(Range<Integer> val) {
                        int minFpsError = progressivePenalty(((Integer) val.getLower()).intValue(), 8000, 1, 4);
                        int maxFpsError = progressivePenalty(Math.abs((fps * 1000) - ((Integer) val.getUpper()).intValue()), MAX_FPS_DIFF_THRESHOLD, 1, 3);
                        return minFpsError + maxFpsError;
                    }

                    @Override // java.util.Comparator
                    public int compare(Range<Integer> lhs, Range<Integer> rhs) {
                        return diff(lhs) - diff(rhs);
                    }
                });
            }
        }
        return null;
    }

    private int findCameraIndex(String camId) throws CameraAccessException {
        String[] idList = this.cameraManager.getCameraIdList();
        for (int ndx = 0; ndx < idList.length; ndx++) {
            if (idList[ndx].equals(camId)) {
                return ndx;
            }
        }
        return -1;
    }

    private Size selectPreferedSize(String camId, final int width, final int height, int format) throws CameraAccessException {
        CameraCharacteristics info = this.cameraManager.getCameraCharacteristics(camId);
        StreamConfigurationMap dimMap = (StreamConfigurationMap) info.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        List<Size> sizeLst = new ArrayList<>();
        dimMap.getOutputFormats();
        Collections.addAll(sizeLst, dimMap.getOutputSizes(35));
        return (Size) Collections.min(sizeLst, new Comparator<Size>() { // from class: com.opentok.android.Camera2VideoCapturer.9
            @Override // java.util.Comparator
            public int compare(Size lhs, Size rhs) {
                int lXerror = Math.abs(lhs.getWidth() - width);
                int lYerror = Math.abs(lhs.getHeight() - height);
                int rXerror = Math.abs(rhs.getWidth() - width);
                int rYerror = Math.abs(rhs.getHeight() - height);
                return (lXerror + lYerror) - (rXerror + rYerror);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int calculateCamRotation() {
        int iAbs;
        try {
            CameraCharacteristics camInfo = this.cameraManager.getCameraCharacteristics(this.camera.getId());
            int cameraRotation = rotationTable.get(this.display.getRotation());
            int cameraOrientation = ((Integer) camInfo.get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
            if (((Integer) camInfo.get(CameraCharacteristics.LENS_FACING)).intValue() != 0) {
                iAbs = Math.abs((cameraRotation - cameraOrientation) % 360);
            } else {
                iAbs = ((cameraRotation + cameraOrientation) + 360) % 360;
            }
            return iAbs;
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void initCamera() {
        try {
            this.cameraState = CameraState.SETUP;
            String[] cameraIdList = this.cameraManager.getCameraIdList();
            String camId = cameraIdList[this.cameraIndex];
            this.camFps = selectCameraFpsRange(camId, this.desiredFps);
            Size preferredSize = selectPreferedSize(camId, this.frameDimensions.getWidth(), this.frameDimensions.getHeight(), 35);
            this.cameraFrame = ImageReader.newInstance(preferredSize.getWidth(), preferredSize.getHeight(), 35, 3);
            this.cameraFrame.setOnImageAvailableListener(this.frameObserver, this.camHandler);
            this.cameraManager.openCamera(camId, this.cameraObserver, this.camHandler);
            waitForCamStateChange(CameraState.SETUP);
        } catch (CameraAccessException exp) {
            throw new Camera2Exception(exp.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postAsyncException(RuntimeException exp) {
        this.runtimeExceptionList.add(exp);
    }
}
