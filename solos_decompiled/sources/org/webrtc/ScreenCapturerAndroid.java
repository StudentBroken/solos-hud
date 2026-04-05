package org.webrtc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.view.Surface;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;

/* JADX INFO: loaded from: classes57.dex */
@TargetApi(21)
public class ScreenCapturerAndroid implements VideoCapturer, SurfaceTextureHelper.OnTextureFrameAvailableListener {
    private static final int DISPLAY_FLAGS = 3;
    private static final int VIRTUAL_DISPLAY_DPI = 400;
    private VideoCapturer.CapturerObserver capturerObserver;
    private int height;
    private MediaProjection mediaProjection;
    private final MediaProjection.Callback mediaProjectionCallback;
    private MediaProjectionManager mediaProjectionManager;
    private final Intent mediaProjectionPermissionResultData;
    private SurfaceTextureHelper surfaceTextureHelper;
    private VirtualDisplay virtualDisplay;
    private int width;
    private long numCapturedFrames = 0;
    private boolean isDisposed = false;

    public ScreenCapturerAndroid(Intent mediaProjectionPermissionResultData, MediaProjection.Callback mediaProjectionCallback) {
        this.mediaProjectionPermissionResultData = mediaProjectionPermissionResultData;
        this.mediaProjectionCallback = mediaProjectionCallback;
    }

    private void checkNotDisposed() {
        if (this.isDisposed) {
            throw new RuntimeException("capturer is disposed.");
        }
    }

    @Override // org.webrtc.VideoCapturer
    public synchronized void initialize(SurfaceTextureHelper surfaceTextureHelper, Context applicationContext, VideoCapturer.CapturerObserver capturerObserver) {
        checkNotDisposed();
        if (capturerObserver == null) {
            throw new RuntimeException("capturerObserver not set.");
        }
        this.capturerObserver = capturerObserver;
        if (surfaceTextureHelper == null) {
            throw new RuntimeException("surfaceTextureHelper not set.");
        }
        this.surfaceTextureHelper = surfaceTextureHelper;
        this.mediaProjectionManager = (MediaProjectionManager) applicationContext.getSystemService("media_projection");
    }

    @Override // org.webrtc.VideoCapturer
    public synchronized void startCapture(int width, int height, int ignoredFramerate) {
        checkNotDisposed();
        this.width = width;
        this.height = height;
        this.mediaProjection = this.mediaProjectionManager.getMediaProjection(-1, this.mediaProjectionPermissionResultData);
        this.mediaProjection.registerCallback(this.mediaProjectionCallback, this.surfaceTextureHelper.getHandler());
        createVirtualDisplay();
        this.capturerObserver.onCapturerStarted(true);
        this.surfaceTextureHelper.startListening(this);
    }

    @Override // org.webrtc.VideoCapturer
    public synchronized void stopCapture() {
        checkNotDisposed();
        ThreadUtils.invokeAtFrontUninterruptibly(this.surfaceTextureHelper.getHandler(), new Runnable() { // from class: org.webrtc.ScreenCapturerAndroid.1
            @Override // java.lang.Runnable
            public void run() {
                ScreenCapturerAndroid.this.surfaceTextureHelper.stopListening();
                ScreenCapturerAndroid.this.capturerObserver.onCapturerStopped();
                if (ScreenCapturerAndroid.this.virtualDisplay != null) {
                    ScreenCapturerAndroid.this.virtualDisplay.release();
                    ScreenCapturerAndroid.this.virtualDisplay = null;
                }
                if (ScreenCapturerAndroid.this.mediaProjection != null) {
                    ScreenCapturerAndroid.this.mediaProjection.unregisterCallback(ScreenCapturerAndroid.this.mediaProjectionCallback);
                    ScreenCapturerAndroid.this.mediaProjection.stop();
                    ScreenCapturerAndroid.this.mediaProjection = null;
                }
            }
        });
    }

    @Override // org.webrtc.VideoCapturer
    public synchronized void dispose() {
        this.isDisposed = true;
    }

    @Override // org.webrtc.VideoCapturer
    public synchronized void changeCaptureFormat(int width, int height, int ignoredFramerate) {
        checkNotDisposed();
        this.width = width;
        this.height = height;
        if (this.virtualDisplay != null) {
            ThreadUtils.invokeAtFrontUninterruptibly(this.surfaceTextureHelper.getHandler(), new Runnable() { // from class: org.webrtc.ScreenCapturerAndroid.2
                @Override // java.lang.Runnable
                public void run() {
                    ScreenCapturerAndroid.this.virtualDisplay.release();
                    ScreenCapturerAndroid.this.createVirtualDisplay();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createVirtualDisplay() {
        this.surfaceTextureHelper.getSurfaceTexture().setDefaultBufferSize(this.width, this.height);
        this.virtualDisplay = this.mediaProjection.createVirtualDisplay("WebRTC_ScreenCapture", this.width, this.height, VIRTUAL_DISPLAY_DPI, 3, new Surface(this.surfaceTextureHelper.getSurfaceTexture()), null, null);
    }

    @Override // org.webrtc.SurfaceTextureHelper.OnTextureFrameAvailableListener
    public void onTextureFrameAvailable(int oesTextureId, float[] transformMatrix, long timestampNs) {
        this.numCapturedFrames++;
        this.capturerObserver.onTextureFrameCaptured(this.width, this.height, oesTextureId, transformMatrix, 0, timestampNs);
    }

    @Override // org.webrtc.VideoCapturer
    public boolean isScreencast() {
        return true;
    }

    public long getNumCapturedFrames() {
        return this.numCapturedFrames;
    }
}
