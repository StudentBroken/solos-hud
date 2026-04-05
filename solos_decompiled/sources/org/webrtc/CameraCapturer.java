package org.webrtc;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.util.Arrays;
import org.webrtc.CameraSession;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.VideoCapturer;

/* JADX INFO: loaded from: classes57.dex */
public abstract class CameraCapturer implements CameraVideoCapturer {
    private static final int MAX_OPEN_CAMERA_ATTEMPTS = 3;
    private static final int OPEN_CAMERA_DELAY_MS = 500;
    private static final int OPEN_CAMERA_TIMEOUT = 10000;
    private static final String TAG = "CameraCapturer";
    private Context applicationContext;
    private final CameraEnumerator cameraEnumerator;
    private String cameraName;
    private CameraVideoCapturer.CameraStatistics cameraStatistics;
    private Handler cameraThreadHandler;
    private VideoCapturer.CapturerObserver capturerObserver;
    private CameraSession currentSession;
    private final CameraVideoCapturer.CameraEventsHandler eventsHandler;
    private boolean firstFrameObserved;
    private int framerate;
    private int height;
    private int openAttemptsRemaining;
    private boolean sessionOpening;
    private SurfaceTextureHelper surfaceHelper;
    private CameraVideoCapturer.CameraSwitchHandler switchEventsHandler;
    private final Handler uiThreadHandler;
    private int width;
    private final CameraSession.CreateSessionCallback createSessionCallback = new CameraSession.CreateSessionCallback() { // from class: org.webrtc.CameraCapturer.1
        @Override // org.webrtc.CameraSession.CreateSessionCallback
        public void onDone(CameraSession session) {
            CameraCapturer.this.checkIsOnCameraThread();
            Logging.d(CameraCapturer.TAG, "Create session done");
            CameraCapturer.this.uiThreadHandler.removeCallbacks(CameraCapturer.this.openCameraTimeoutRunnable);
            synchronized (CameraCapturer.this.stateLock) {
                CameraCapturer.this.capturerObserver.onCapturerStarted(true);
                CameraCapturer.this.sessionOpening = false;
                CameraCapturer.this.currentSession = session;
                CameraCapturer.this.cameraStatistics = new CameraVideoCapturer.CameraStatistics(CameraCapturer.this.surfaceHelper, CameraCapturer.this.eventsHandler);
                CameraCapturer.this.firstFrameObserved = false;
                CameraCapturer.this.stateLock.notifyAll();
                if (CameraCapturer.this.switchState == SwitchState.IN_PROGRESS) {
                    if (CameraCapturer.this.switchEventsHandler != null) {
                        CameraCapturer.this.switchEventsHandler.onCameraSwitchDone(CameraCapturer.this.cameraEnumerator.isFrontFacing(CameraCapturer.this.cameraName));
                        CameraCapturer.this.switchEventsHandler = null;
                    }
                    CameraCapturer.this.switchState = SwitchState.IDLE;
                } else if (CameraCapturer.this.switchState == SwitchState.PENDING) {
                    CameraCapturer.this.switchState = SwitchState.IDLE;
                    CameraCapturer.this.switchCameraInternal(CameraCapturer.this.switchEventsHandler);
                }
            }
        }

        @Override // org.webrtc.CameraSession.CreateSessionCallback
        public void onFailure(String error) {
            CameraCapturer.this.checkIsOnCameraThread();
            CameraCapturer.this.uiThreadHandler.removeCallbacks(CameraCapturer.this.openCameraTimeoutRunnable);
            synchronized (CameraCapturer.this.stateLock) {
                CameraCapturer.this.capturerObserver.onCapturerStarted(false);
                CameraCapturer.access$1610(CameraCapturer.this);
                if (CameraCapturer.this.openAttemptsRemaining <= 0) {
                    Logging.w(CameraCapturer.TAG, "Opening camera failed, passing: " + error);
                    CameraCapturer.this.sessionOpening = false;
                    CameraCapturer.this.stateLock.notifyAll();
                    if (CameraCapturer.this.switchState != SwitchState.IDLE) {
                        if (CameraCapturer.this.switchEventsHandler != null) {
                            CameraCapturer.this.switchEventsHandler.onCameraSwitchError(error);
                            CameraCapturer.this.switchEventsHandler = null;
                        }
                        CameraCapturer.this.switchState = SwitchState.IDLE;
                    }
                    CameraCapturer.this.eventsHandler.onCameraError(error);
                } else {
                    Logging.w(CameraCapturer.TAG, "Opening camera failed, retry: " + error);
                    CameraCapturer.this.createSessionInternal(CameraCapturer.OPEN_CAMERA_DELAY_MS);
                }
            }
        }
    };
    private final CameraSession.Events cameraSessionEventsHandler = new CameraSession.Events() { // from class: org.webrtc.CameraCapturer.2
        @Override // org.webrtc.CameraSession.Events
        public void onCameraOpening() {
            CameraCapturer.this.checkIsOnCameraThread();
            synchronized (CameraCapturer.this.stateLock) {
                if (CameraCapturer.this.currentSession == null) {
                    CameraCapturer.this.eventsHandler.onCameraOpening(CameraCapturer.this.cameraName);
                } else {
                    Logging.w(CameraCapturer.TAG, "onCameraOpening while session was open.");
                }
            }
        }

        @Override // org.webrtc.CameraSession.Events
        public void onCameraError(CameraSession session, String error) {
            CameraCapturer.this.checkIsOnCameraThread();
            synchronized (CameraCapturer.this.stateLock) {
                if (session == CameraCapturer.this.currentSession) {
                    CameraCapturer.this.eventsHandler.onCameraError(error);
                    CameraCapturer.this.stopCapture();
                } else {
                    Logging.w(CameraCapturer.TAG, "onCameraError from another session: " + error);
                }
            }
        }

        @Override // org.webrtc.CameraSession.Events
        public void onCameraDisconnected(CameraSession session) {
            CameraCapturer.this.checkIsOnCameraThread();
            synchronized (CameraCapturer.this.stateLock) {
                if (session == CameraCapturer.this.currentSession) {
                    CameraCapturer.this.eventsHandler.onCameraDisconnected();
                    CameraCapturer.this.stopCapture();
                } else {
                    Logging.w(CameraCapturer.TAG, "onCameraDisconnected from another session.");
                }
            }
        }

        @Override // org.webrtc.CameraSession.Events
        public void onCameraClosed(CameraSession session) {
            CameraCapturer.this.checkIsOnCameraThread();
            synchronized (CameraCapturer.this.stateLock) {
                if (session == CameraCapturer.this.currentSession || CameraCapturer.this.currentSession == null) {
                    CameraCapturer.this.eventsHandler.onCameraClosed();
                } else {
                    Logging.d(CameraCapturer.TAG, "onCameraClosed from another session.");
                }
            }
        }

        @Override // org.webrtc.CameraSession.Events
        public void onByteBufferFrameCaptured(CameraSession session, byte[] data, int width, int height, int rotation, long timestamp) {
            CameraCapturer.this.checkIsOnCameraThread();
            synchronized (CameraCapturer.this.stateLock) {
                if (session == CameraCapturer.this.currentSession) {
                    if (!CameraCapturer.this.firstFrameObserved) {
                        CameraCapturer.this.eventsHandler.onFirstFrameAvailable();
                        CameraCapturer.this.firstFrameObserved = true;
                    }
                    CameraCapturer.this.cameraStatistics.addFrame();
                    CameraCapturer.this.capturerObserver.onByteBufferFrameCaptured(data, width, height, rotation, timestamp);
                    return;
                }
                Logging.w(CameraCapturer.TAG, "onByteBufferFrameCaptured from another session.");
            }
        }

        @Override // org.webrtc.CameraSession.Events
        public void onTextureFrameCaptured(CameraSession session, int width, int height, int oesTextureId, float[] transformMatrix, int rotation, long timestamp) {
            CameraCapturer.this.checkIsOnCameraThread();
            synchronized (CameraCapturer.this.stateLock) {
                if (session == CameraCapturer.this.currentSession) {
                    if (!CameraCapturer.this.firstFrameObserved) {
                        CameraCapturer.this.eventsHandler.onFirstFrameAvailable();
                        CameraCapturer.this.firstFrameObserved = true;
                    }
                    CameraCapturer.this.cameraStatistics.addFrame();
                    CameraCapturer.this.capturerObserver.onTextureFrameCaptured(width, height, oesTextureId, transformMatrix, rotation, timestamp);
                    return;
                }
                Logging.w(CameraCapturer.TAG, "onTextureFrameCaptured from another session.");
                CameraCapturer.this.surfaceHelper.returnTextureFrame();
            }
        }
    };
    private final Runnable openCameraTimeoutRunnable = new Runnable() { // from class: org.webrtc.CameraCapturer.3
        @Override // java.lang.Runnable
        public void run() {
            CameraCapturer.this.eventsHandler.onCameraError("Camera failed to start within timeout.");
        }
    };
    private final Object stateLock = new Object();
    private SwitchState switchState = SwitchState.IDLE;

    enum SwitchState {
        IDLE,
        PENDING,
        IN_PROGRESS
    }

    protected abstract void createCameraSession(CameraSession.CreateSessionCallback createSessionCallback, CameraSession.Events events, Context context, SurfaceTextureHelper surfaceTextureHelper, String str, int i, int i2, int i3);

    static /* synthetic */ int access$1610(CameraCapturer x0) {
        int i = x0.openAttemptsRemaining;
        x0.openAttemptsRemaining = i - 1;
        return i;
    }

    public CameraCapturer(String cameraName, CameraVideoCapturer.CameraEventsHandler eventsHandler, CameraEnumerator cameraEnumerator) {
        this.eventsHandler = eventsHandler == null ? new CameraVideoCapturer.CameraEventsHandler() { // from class: org.webrtc.CameraCapturer.4
            @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
            public void onCameraError(String errorDescription) {
            }

            @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
            public void onCameraDisconnected() {
            }

            @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
            public void onCameraFreezed(String errorDescription) {
            }

            @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
            public void onCameraOpening(String cameraName2) {
            }

            @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
            public void onFirstFrameAvailable() {
            }

            @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
            public void onCameraClosed() {
            }
        } : eventsHandler;
        this.cameraEnumerator = cameraEnumerator;
        this.cameraName = cameraName;
        this.uiThreadHandler = new Handler(Looper.getMainLooper());
        String[] deviceNames = cameraEnumerator.getDeviceNames();
        if (deviceNames.length == 0) {
            throw new RuntimeException("No cameras attached.");
        }
        if (!Arrays.asList(deviceNames).contains(this.cameraName)) {
            throw new IllegalArgumentException("Camera name " + this.cameraName + " does not match any known camera device.");
        }
    }

    @Override // org.webrtc.VideoCapturer
    public void initialize(SurfaceTextureHelper surfaceTextureHelper, Context applicationContext, VideoCapturer.CapturerObserver capturerObserver) {
        this.applicationContext = applicationContext;
        this.capturerObserver = capturerObserver;
        this.surfaceHelper = surfaceTextureHelper;
        this.cameraThreadHandler = surfaceTextureHelper == null ? null : surfaceTextureHelper.getHandler();
    }

    @Override // org.webrtc.VideoCapturer
    public void startCapture(int width, int height, int framerate) {
        Logging.d(TAG, "startCapture: " + width + "x" + height + "@" + framerate);
        synchronized (this.stateLock) {
            if (this.sessionOpening || this.currentSession != null) {
                Logging.w(TAG, "Session already open");
                return;
            }
            this.width = width;
            this.height = height;
            this.framerate = framerate;
            this.sessionOpening = true;
            this.openAttemptsRemaining = 3;
            createSessionInternal(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createSessionInternal(int delayMs) {
        this.uiThreadHandler.postDelayed(this.openCameraTimeoutRunnable, delayMs + 10000);
        this.cameraThreadHandler.postDelayed(new Runnable() { // from class: org.webrtc.CameraCapturer.5
            @Override // java.lang.Runnable
            public void run() {
                CameraCapturer.this.createCameraSession(CameraCapturer.this.createSessionCallback, CameraCapturer.this.cameraSessionEventsHandler, CameraCapturer.this.applicationContext, CameraCapturer.this.surfaceHelper, CameraCapturer.this.cameraName, CameraCapturer.this.width, CameraCapturer.this.height, CameraCapturer.this.framerate);
            }
        }, delayMs);
    }

    @Override // org.webrtc.VideoCapturer
    public void stopCapture() {
        Logging.d(TAG, "Stop capture");
        synchronized (this.stateLock) {
            while (this.sessionOpening) {
                Logging.d(TAG, "Stop capture: Waiting for session to open");
                ThreadUtils.waitUninterruptibly(this.stateLock);
            }
            if (this.currentSession != null) {
                Logging.d(TAG, "Stop capture: Nulling session");
                this.cameraStatistics.release();
                this.cameraStatistics = null;
                final CameraSession oldSession = this.currentSession;
                this.cameraThreadHandler.post(new Runnable() { // from class: org.webrtc.CameraCapturer.6
                    @Override // java.lang.Runnable
                    public void run() {
                        oldSession.stop();
                    }
                });
                this.currentSession = null;
                this.capturerObserver.onCapturerStopped();
            } else {
                Logging.d(TAG, "Stop capture: No session open");
            }
        }
        Logging.d(TAG, "Stop capture done");
    }

    @Override // org.webrtc.VideoCapturer
    public void changeCaptureFormat(int width, int height, int framerate) {
        Logging.d(TAG, "changeCaptureFormat: " + width + "x" + height + "@" + framerate);
        synchronized (this.stateLock) {
            stopCapture();
            startCapture(width, height, framerate);
        }
    }

    @Override // org.webrtc.VideoCapturer
    public void dispose() {
        Logging.d(TAG, "dispose");
        stopCapture();
    }

    @Override // org.webrtc.CameraVideoCapturer
    public void switchCamera(final CameraVideoCapturer.CameraSwitchHandler switchEventsHandler) {
        Logging.d(TAG, "switchCamera");
        this.cameraThreadHandler.post(new Runnable() { // from class: org.webrtc.CameraCapturer.7
            @Override // java.lang.Runnable
            public void run() {
                CameraCapturer.this.switchCameraInternal(switchEventsHandler);
            }
        });
    }

    @Override // org.webrtc.VideoCapturer
    public boolean isScreencast() {
        return false;
    }

    public void printStackTrace() {
        Thread cameraThread = null;
        if (this.cameraThreadHandler != null) {
            cameraThread = this.cameraThreadHandler.getLooper().getThread();
        }
        if (cameraThread != null) {
            StackTraceElement[] cameraStackTrace = cameraThread.getStackTrace();
            if (cameraStackTrace.length > 0) {
                Logging.d(TAG, "CameraCapturer stack trace:");
                for (StackTraceElement traceElem : cameraStackTrace) {
                    Logging.d(TAG, traceElem.toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchCameraInternal(CameraVideoCapturer.CameraSwitchHandler switchEventsHandler) {
        Logging.d(TAG, "switchCamera internal");
        String[] deviceNames = this.cameraEnumerator.getDeviceNames();
        if (deviceNames.length < 2) {
            if (switchEventsHandler != null) {
                switchEventsHandler.onCameraSwitchError("No camera to switch to.");
                return;
            }
            return;
        }
        synchronized (this.stateLock) {
            if (this.switchState != SwitchState.IDLE) {
                Logging.d(TAG, "switchCamera switchInProgress");
                if (switchEventsHandler != null) {
                    switchEventsHandler.onCameraSwitchError("Camera switch already in progress.");
                }
                return;
            }
            if (!this.sessionOpening && this.currentSession == null) {
                Logging.d(TAG, "switchCamera: No session open");
                if (switchEventsHandler != null) {
                    switchEventsHandler.onCameraSwitchError("Camera is not running.");
                }
                return;
            }
            this.switchEventsHandler = switchEventsHandler;
            if (this.sessionOpening) {
                this.switchState = SwitchState.PENDING;
                return;
            }
            this.switchState = SwitchState.IN_PROGRESS;
            Logging.d(TAG, "switchCamera: Stopping session");
            this.cameraStatistics.release();
            this.cameraStatistics = null;
            final CameraSession oldSession = this.currentSession;
            this.cameraThreadHandler.post(new Runnable() { // from class: org.webrtc.CameraCapturer.8
                @Override // java.lang.Runnable
                public void run() {
                    oldSession.stop();
                }
            });
            this.currentSession = null;
            int cameraNameIndex = Arrays.asList(deviceNames).indexOf(this.cameraName);
            this.cameraName = deviceNames[(cameraNameIndex + 1) % deviceNames.length];
            this.sessionOpening = true;
            this.openAttemptsRemaining = 1;
            createSessionInternal(0);
            Logging.d(TAG, "switchCamera done");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsOnCameraThread() {
        if (Thread.currentThread() != this.cameraThreadHandler.getLooper().getThread()) {
            Logging.e(TAG, "Check is on camera thread failed.");
            throw new RuntimeException("Not on camera thread.");
        }
    }

    protected String getCameraName() {
        String str;
        synchronized (this.stateLock) {
            str = this.cameraName;
        }
        return str;
    }
}
