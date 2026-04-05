package org.webrtc;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.webrtc.EglBase;

/* JADX INFO: loaded from: classes57.dex */
class SurfaceTextureHelper {
    private static final String TAG = "SurfaceTextureHelper";
    private final EglBase eglBase;
    private final Handler handler;
    private boolean hasPendingTexture;
    private boolean isQuitting;
    private volatile boolean isTextureInUse;
    private OnTextureFrameAvailableListener listener;
    private final int oesTextureId;
    private OnTextureFrameAvailableListener pendingListener;
    final Runnable setListenerRunnable;
    private final SurfaceTexture surfaceTexture;
    private YuvConverter yuvConverter;

    public interface OnTextureFrameAvailableListener {
        void onTextureFrameAvailable(int i, float[] fArr, long j);
    }

    public static SurfaceTextureHelper create(final String threadName, final EglBase.Context sharedContext) {
        HandlerThread thread = new HandlerThread(threadName);
        thread.start();
        final Handler handler = new Handler(thread.getLooper());
        return (SurfaceTextureHelper) ThreadUtils.invokeAtFrontUninterruptibly(handler, new Callable<SurfaceTextureHelper>() { // from class: org.webrtc.SurfaceTextureHelper.1
            @Override // java.util.concurrent.Callable
            public SurfaceTextureHelper call() {
                try {
                    return new SurfaceTextureHelper(sharedContext, handler);
                } catch (RuntimeException e) {
                    Logging.e(SurfaceTextureHelper.TAG, threadName + " create failure", e);
                    return null;
                }
            }
        });
    }

    private SurfaceTextureHelper(EglBase.Context sharedContext, Handler handler) {
        this.hasPendingTexture = false;
        this.isTextureInUse = false;
        this.isQuitting = false;
        this.setListenerRunnable = new Runnable() { // from class: org.webrtc.SurfaceTextureHelper.2
            @Override // java.lang.Runnable
            public void run() {
                Logging.d(SurfaceTextureHelper.TAG, "Setting listener to " + SurfaceTextureHelper.this.pendingListener);
                SurfaceTextureHelper.this.listener = SurfaceTextureHelper.this.pendingListener;
                SurfaceTextureHelper.this.pendingListener = null;
                if (SurfaceTextureHelper.this.hasPendingTexture) {
                    SurfaceTextureHelper.this.updateTexImage();
                    SurfaceTextureHelper.this.hasPendingTexture = false;
                }
            }
        };
        if (handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("SurfaceTextureHelper must be created on the handler thread");
        }
        this.handler = handler;
        this.eglBase = EglBase.create(sharedContext, EglBase.CONFIG_PIXEL_BUFFER);
        try {
            this.eglBase.createDummyPbufferSurface();
            this.eglBase.makeCurrent();
            this.oesTextureId = GlUtil.generateTexture(36197);
            this.surfaceTexture = new SurfaceTexture(this.oesTextureId);
            this.surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() { // from class: org.webrtc.SurfaceTextureHelper.3
                @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    SurfaceTextureHelper.this.hasPendingTexture = true;
                    SurfaceTextureHelper.this.tryDeliverTextureFrame();
                }
            });
        } catch (RuntimeException e) {
            this.eglBase.release();
            handler.getLooper().quit();
            throw e;
        }
    }

    public void startListening(OnTextureFrameAvailableListener listener) {
        if (this.listener != null || this.pendingListener != null) {
            throw new IllegalStateException("SurfaceTextureHelper listener has already been set.");
        }
        this.pendingListener = listener;
        this.handler.post(this.setListenerRunnable);
    }

    public void stopListening() {
        Logging.d(TAG, "stopListening()");
        this.handler.removeCallbacks(this.setListenerRunnable);
        ThreadUtils.invokeAtFrontUninterruptibly(this.handler, new Runnable() { // from class: org.webrtc.SurfaceTextureHelper.4
            @Override // java.lang.Runnable
            public void run() {
                SurfaceTextureHelper.this.listener = null;
                SurfaceTextureHelper.this.pendingListener = null;
            }
        });
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.surfaceTexture;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void returnTextureFrame() {
        this.handler.post(new Runnable() { // from class: org.webrtc.SurfaceTextureHelper.5
            @Override // java.lang.Runnable
            public void run() {
                SurfaceTextureHelper.this.isTextureInUse = false;
                if (SurfaceTextureHelper.this.isQuitting) {
                    SurfaceTextureHelper.this.release();
                } else {
                    SurfaceTextureHelper.this.tryDeliverTextureFrame();
                }
            }
        });
    }

    public boolean isTextureInUse() {
        return this.isTextureInUse;
    }

    public void dispose() {
        Logging.d(TAG, "dispose()");
        ThreadUtils.invokeAtFrontUninterruptibly(this.handler, new Runnable() { // from class: org.webrtc.SurfaceTextureHelper.6
            @Override // java.lang.Runnable
            public void run() {
                SurfaceTextureHelper.this.isQuitting = true;
                if (!SurfaceTextureHelper.this.isTextureInUse) {
                    SurfaceTextureHelper.this.release();
                }
            }
        });
    }

    public void textureToYUV(final ByteBuffer buf, final int width, final int height, final int stride, final int textureId, final float[] transformMatrix) {
        if (textureId != this.oesTextureId) {
            throw new IllegalStateException("textureToByteBuffer called with unexpected textureId");
        }
        ThreadUtils.invokeAtFrontUninterruptibly(this.handler, new Runnable() { // from class: org.webrtc.SurfaceTextureHelper.7
            @Override // java.lang.Runnable
            public void run() {
                if (SurfaceTextureHelper.this.yuvConverter == null) {
                    SurfaceTextureHelper.this.yuvConverter = new YuvConverter();
                }
                SurfaceTextureHelper.this.yuvConverter.convert(buf, width, height, stride, textureId, transformMatrix);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTexImage() {
        synchronized (EglBase.lock) {
            this.surfaceTexture.updateTexImage();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryDeliverTextureFrame() {
        long timestampNs;
        if (this.handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Wrong thread.");
        }
        if (!this.isQuitting && this.hasPendingTexture && !this.isTextureInUse && this.listener != null) {
            this.isTextureInUse = true;
            this.hasPendingTexture = false;
            updateTexImage();
            float[] transformMatrix = new float[16];
            this.surfaceTexture.getTransformMatrix(transformMatrix);
            if (Build.VERSION.SDK_INT >= 14) {
                timestampNs = this.surfaceTexture.getTimestamp();
            } else {
                timestampNs = TimeUnit.MILLISECONDS.toNanos(SystemClock.elapsedRealtime());
            }
            this.listener.onTextureFrameAvailable(this.oesTextureId, transformMatrix, timestampNs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void release() {
        if (this.handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Wrong thread.");
        }
        if (this.isTextureInUse || !this.isQuitting) {
            throw new IllegalStateException("Unexpected release.");
        }
        if (this.yuvConverter != null) {
            this.yuvConverter.release();
        }
        GLES20.glDeleteTextures(1, new int[]{this.oesTextureId}, 0);
        this.surfaceTexture.release();
        this.eglBase.release();
        this.handler.getLooper().quit();
    }
}
