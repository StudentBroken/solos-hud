package org.webrtc;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.VideoRenderer;

/* JADX INFO: loaded from: classes57.dex */
public class EglRenderer implements VideoRenderer.Callbacks {
    private static final long LOG_INTERVAL_SEC = 4;
    private static final int MAX_SURFACE_CLEAR_COUNT = 3;
    private static final String TAG = "EglRenderer";
    private GlTextureFrameBuffer bitmapTextureFramebuffer;
    private RendererCommon.GlDrawer drawer;
    private EglBase eglBase;
    private int framesDropped;
    private int framesReceived;
    private int framesRendered;
    private float layoutAspectRatio;
    private long minRenderPeriodNs;
    private boolean mirror;
    private final String name;
    private long nextFrameTimeNs;
    private VideoRenderer.I420Frame pendingFrame;
    private long renderSwapBufferTimeNs;
    private Handler renderThreadHandler;
    private long renderTimeNs;
    private long statisticsStartTimeNs;
    private int surfaceHeight;
    private int surfaceWidth;
    private final Object handlerLock = new Object();
    private final Object frameListenerLock = new Object();
    private final ArrayList<ScaleAndFrameListener> frameListeners = new ArrayList<>();
    private final Object fpsReductionLock = new Object();
    private final RendererCommon.YuvUploader yuvUploader = new RendererCommon.YuvUploader();
    private int[] yuvTextures = null;
    private final Object frameLock = new Object();
    private final Object layoutLock = new Object();
    private final Object statisticsLock = new Object();
    private final Runnable renderFrameRunnable = new Runnable() { // from class: org.webrtc.EglRenderer.1
        @Override // java.lang.Runnable
        public void run() {
            EglRenderer.this.renderFrameOnRenderThread();
        }
    };
    private final Runnable logStatisticsRunnable = new Runnable() { // from class: org.webrtc.EglRenderer.2
        @Override // java.lang.Runnable
        public void run() {
            EglRenderer.this.logStatistics();
            synchronized (EglRenderer.this.handlerLock) {
                if (EglRenderer.this.renderThreadHandler != null) {
                    EglRenderer.this.renderThreadHandler.removeCallbacks(EglRenderer.this.logStatisticsRunnable);
                    EglRenderer.this.renderThreadHandler.postDelayed(EglRenderer.this.logStatisticsRunnable, TimeUnit.SECONDS.toMillis(4L));
                }
            }
        }
    };
    private final EglSurfaceCreation eglSurfaceCreationRunnable = new EglSurfaceCreation();

    public interface FrameListener {
        void onFrame(Bitmap bitmap);
    }

    private static class ScaleAndFrameListener {
        public final FrameListener listener;
        public final float scale;

        public ScaleAndFrameListener(float scale, FrameListener listener) {
            this.scale = scale;
            this.listener = listener;
        }
    }

    private class EglSurfaceCreation implements Runnable {
        private Object surface;

        private EglSurfaceCreation() {
        }

        public synchronized void setSurface(Object surface) {
            this.surface = surface;
        }

        @Override // java.lang.Runnable
        public synchronized void run() {
            if (this.surface != null && EglRenderer.this.eglBase != null && !EglRenderer.this.eglBase.hasSurface()) {
                if (this.surface instanceof Surface) {
                    EglRenderer.this.eglBase.createSurface((Surface) this.surface);
                } else if (this.surface instanceof SurfaceTexture) {
                    EglRenderer.this.eglBase.createSurface((SurfaceTexture) this.surface);
                } else {
                    throw new IllegalStateException("Invalid surface: " + this.surface);
                }
                EglRenderer.this.eglBase.makeCurrent();
                GLES20.glPixelStorei(3317, 1);
            }
        }
    }

    public EglRenderer(String name) {
        this.name = name;
    }

    public void init(final EglBase.Context sharedContext, final int[] configAttributes, RendererCommon.GlDrawer drawer) {
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler != null) {
                throw new IllegalStateException(this.name + "Already initialized");
            }
            logD("Initializing EglRenderer");
            this.drawer = drawer;
            HandlerThread renderThread = new HandlerThread(this.name + TAG);
            renderThread.start();
            this.renderThreadHandler = new Handler(renderThread.getLooper());
            ThreadUtils.invokeAtFrontUninterruptibly(this.renderThreadHandler, new Runnable() { // from class: org.webrtc.EglRenderer.3
                @Override // java.lang.Runnable
                public void run() {
                    if (sharedContext == null) {
                        EglRenderer.this.logD("EglBase10.create context");
                        EglRenderer.this.eglBase = new EglBase10(null, configAttributes);
                    } else {
                        EglRenderer.this.logD("EglBase.create shared context");
                        EglRenderer.this.eglBase = EglBase.create(sharedContext, configAttributes);
                    }
                }
            });
            this.renderThreadHandler.post(this.eglSurfaceCreationRunnable);
            long currentTimeNs = System.nanoTime();
            resetStatistics(currentTimeNs);
            this.renderThreadHandler.postDelayed(this.logStatisticsRunnable, TimeUnit.SECONDS.toMillis(4L));
        }
    }

    public void createEglSurface(Surface surface) {
        createEglSurfaceInternal(surface);
    }

    public void createEglSurface(SurfaceTexture surfaceTexture) {
        createEglSurfaceInternal(surfaceTexture);
    }

    private void createEglSurfaceInternal(Object surface) {
        this.eglSurfaceCreationRunnable.setSurface(surface);
        postToRenderThread(this.eglSurfaceCreationRunnable);
    }

    public void release() {
        logD("Releasing.");
        final CountDownLatch eglCleanupBarrier = new CountDownLatch(1);
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler == null) {
                logD("Already released");
                return;
            }
            this.renderThreadHandler.removeCallbacks(this.logStatisticsRunnable);
            this.renderThreadHandler.postAtFrontOfQueue(new Runnable() { // from class: org.webrtc.EglRenderer.4
                @Override // java.lang.Runnable
                public void run() {
                    if (EglRenderer.this.drawer != null) {
                        EglRenderer.this.drawer.release();
                        EglRenderer.this.drawer = null;
                    }
                    if (EglRenderer.this.yuvTextures != null) {
                        GLES20.glDeleteTextures(3, EglRenderer.this.yuvTextures, 0);
                        EglRenderer.this.yuvTextures = null;
                    }
                    if (EglRenderer.this.bitmapTextureFramebuffer != null) {
                        EglRenderer.this.bitmapTextureFramebuffer.release();
                        EglRenderer.this.bitmapTextureFramebuffer = null;
                    }
                    if (EglRenderer.this.eglBase != null) {
                        EglRenderer.this.logD("eglBase detach and release.");
                        EglRenderer.this.eglBase.detachCurrent();
                        EglRenderer.this.eglBase.release();
                        EglRenderer.this.eglBase = null;
                    }
                    eglCleanupBarrier.countDown();
                }
            });
            final Looper renderLooper = this.renderThreadHandler.getLooper();
            this.renderThreadHandler.post(new Runnable() { // from class: org.webrtc.EglRenderer.5
                @Override // java.lang.Runnable
                public void run() {
                    EglRenderer.this.logD("Quitting render thread.");
                    renderLooper.quit();
                }
            });
            this.renderThreadHandler = null;
            ThreadUtils.awaitUninterruptibly(eglCleanupBarrier);
            synchronized (this.frameLock) {
                if (this.pendingFrame != null) {
                    VideoRenderer.renderFrameDone(this.pendingFrame);
                    this.pendingFrame = null;
                }
            }
            logD("Releasing done.");
        }
    }

    private void resetStatistics(long currentTimeNs) {
        synchronized (this.statisticsLock) {
            this.statisticsStartTimeNs = currentTimeNs;
            this.framesReceived = 0;
            this.framesDropped = 0;
            this.framesRendered = 0;
            this.renderTimeNs = 0L;
            this.renderSwapBufferTimeNs = 0L;
        }
    }

    public void printStackTrace() {
        synchronized (this.handlerLock) {
            Thread renderThread = this.renderThreadHandler == null ? null : this.renderThreadHandler.getLooper().getThread();
            if (renderThread != null) {
                StackTraceElement[] renderStackTrace = renderThread.getStackTrace();
                if (renderStackTrace.length > 0) {
                    logD("EglRenderer stack trace:");
                    for (StackTraceElement traceElem : renderStackTrace) {
                        logD(traceElem.toString());
                    }
                }
            }
        }
    }

    public void setMirror(boolean mirror) {
        logD("setMirror: " + mirror);
        synchronized (this.layoutLock) {
            this.mirror = mirror;
        }
    }

    public void setLayoutAspectRatio(float layoutAspectRatio) {
        logD("setLayoutAspectRatio: " + layoutAspectRatio);
        synchronized (this.layoutLock) {
            this.layoutAspectRatio = layoutAspectRatio;
        }
    }

    public void setFpsReduction(float fps) {
        logD("setFpsReduction: " + fps);
        synchronized (this.fpsReductionLock) {
            long previousRenderPeriodNs = this.minRenderPeriodNs;
            if (fps <= 0.0f) {
                this.minRenderPeriodNs = Long.MAX_VALUE;
            } else {
                this.minRenderPeriodNs = (long) (TimeUnit.SECONDS.toNanos(1L) / fps);
            }
            if (this.minRenderPeriodNs != previousRenderPeriodNs) {
                this.nextFrameTimeNs = System.nanoTime();
            }
        }
    }

    public void disableFpsReduction() {
        setFpsReduction(Float.POSITIVE_INFINITY);
    }

    public void pauseVideo() {
        setFpsReduction(0.0f);
    }

    public void addFrameListener(FrameListener listener, float scale) {
        synchronized (this.frameListenerLock) {
            this.frameListeners.add(new ScaleAndFrameListener(scale, listener));
        }
    }

    public void removeFrameListener(FrameListener listener) {
        synchronized (this.frameListenerLock) {
            Iterator<ScaleAndFrameListener> iter = this.frameListeners.iterator();
            while (iter.hasNext()) {
                if (iter.next().listener == listener) {
                    iter.remove();
                }
            }
        }
    }

    @Override // org.webrtc.VideoRenderer.Callbacks
    public void renderFrame(VideoRenderer.I420Frame frame) {
        boolean dropOldFrame;
        synchronized (this.statisticsLock) {
            this.framesReceived++;
        }
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler == null) {
                logD("Dropping frame - Not initialized or already released.");
                VideoRenderer.renderFrameDone(frame);
                return;
            }
            synchronized (this.fpsReductionLock) {
                if (this.minRenderPeriodNs > 0) {
                    long currentTimeNs = System.nanoTime();
                    if (currentTimeNs < this.nextFrameTimeNs) {
                        logD("Dropping frame - fps reduction is active.");
                        VideoRenderer.renderFrameDone(frame);
                        return;
                    } else {
                        this.nextFrameTimeNs += this.minRenderPeriodNs;
                        this.nextFrameTimeNs = Math.max(this.nextFrameTimeNs, currentTimeNs);
                    }
                }
                synchronized (this.frameLock) {
                    dropOldFrame = this.pendingFrame != null;
                    if (dropOldFrame) {
                        VideoRenderer.renderFrameDone(this.pendingFrame);
                    }
                    this.pendingFrame = frame;
                    this.renderThreadHandler.post(this.renderFrameRunnable);
                }
                if (dropOldFrame) {
                    synchronized (this.statisticsLock) {
                        this.framesDropped++;
                    }
                }
            }
        }
    }

    public void releaseEglSurface(final Runnable completionCallback) {
        this.eglSurfaceCreationRunnable.setSurface(null);
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler != null) {
                this.renderThreadHandler.removeCallbacks(this.eglSurfaceCreationRunnable);
                this.renderThreadHandler.postAtFrontOfQueue(new Runnable() { // from class: org.webrtc.EglRenderer.6
                    @Override // java.lang.Runnable
                    public void run() {
                        if (EglRenderer.this.eglBase != null) {
                            EglRenderer.this.eglBase.detachCurrent();
                            EglRenderer.this.eglBase.releaseSurface();
                        }
                        completionCallback.run();
                    }
                });
            } else {
                completionCallback.run();
            }
        }
    }

    public void surfaceSizeChanged(int surfaceWidth, int surfaceHeight) {
        logD("Surface size changed: " + surfaceWidth + "x" + surfaceHeight);
        synchronized (this.layoutLock) {
            this.surfaceWidth = surfaceWidth;
            this.surfaceHeight = surfaceHeight;
        }
    }

    private void postToRenderThread(Runnable runnable) {
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler != null) {
                this.renderThreadHandler.post(runnable);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearSurfaceOnRenderThread() {
        if (this.eglBase != null && this.eglBase.hasSurface()) {
            logD("clearSurface");
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GLES20.glClear(16384);
            this.eglBase.swapBuffers();
        }
    }

    public void clearImage() {
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler != null) {
                this.renderThreadHandler.postAtFrontOfQueue(new Runnable() { // from class: org.webrtc.EglRenderer.7
                    @Override // java.lang.Runnable
                    public void run() {
                        EglRenderer.this.clearSurfaceOnRenderThread();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renderFrameOnRenderThread() {
        float[] layoutMatrix;
        synchronized (this.frameLock) {
            if (this.pendingFrame != null) {
                VideoRenderer.I420Frame frame = this.pendingFrame;
                this.pendingFrame = null;
                if (this.eglBase == null || !this.eglBase.hasSurface()) {
                    logD("Dropping frame - No surface");
                    VideoRenderer.renderFrameDone(frame);
                    return;
                }
                long startTimeNs = System.nanoTime();
                float[] texMatrix = RendererCommon.rotateTextureMatrix(frame.samplingMatrix, frame.rotationDegree);
                synchronized (this.layoutLock) {
                    int surfaceClearCount = 0;
                    while (true) {
                        if (this.eglBase.surfaceWidth() != this.surfaceWidth || this.eglBase.surfaceHeight() != this.surfaceHeight) {
                            surfaceClearCount++;
                            if (surfaceClearCount > 3) {
                                logD("Failed to get surface of expected size - dropping frame.");
                                VideoRenderer.renderFrameDone(frame);
                                return;
                            } else {
                                logD("Surface size mismatch - clearing surface. Size: " + this.eglBase.surfaceWidth() + "x" + this.eglBase.surfaceHeight() + " Expected: " + this.surfaceWidth + "x" + this.surfaceHeight);
                                clearSurfaceOnRenderThread();
                            }
                        } else {
                            if (this.layoutAspectRatio > 0.0f) {
                                layoutMatrix = RendererCommon.getLayoutMatrix(this.mirror, frame.rotatedWidth() / frame.rotatedHeight(), this.layoutAspectRatio);
                            } else {
                                layoutMatrix = this.mirror ? RendererCommon.horizontalFlipMatrix() : RendererCommon.identityMatrix();
                            }
                            float[] drawMatrix = RendererCommon.multiplyMatrices(texMatrix, layoutMatrix);
                            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                            GLES20.glClear(16384);
                            if (frame.yuvFrame) {
                                if (this.yuvTextures == null) {
                                    this.yuvTextures = new int[3];
                                    for (int i = 0; i < 3; i++) {
                                        this.yuvTextures[i] = GlUtil.generateTexture(3553);
                                    }
                                }
                                this.yuvUploader.uploadYuvData(this.yuvTextures, frame.width, frame.height, frame.yuvStrides, frame.yuvPlanes);
                                this.drawer.drawYuv(this.yuvTextures, drawMatrix, frame.rotatedWidth(), frame.rotatedHeight(), 0, 0, this.surfaceWidth, this.surfaceHeight);
                            } else {
                                this.drawer.drawOes(frame.textureId, drawMatrix, frame.rotatedWidth(), frame.rotatedHeight(), 0, 0, this.surfaceWidth, this.surfaceHeight);
                            }
                            long swapBuffersStartTimeNs = System.nanoTime();
                            this.eglBase.swapBuffers();
                            long currentTimeNs = System.nanoTime();
                            synchronized (this.statisticsLock) {
                                this.framesRendered++;
                                this.renderTimeNs += currentTimeNs - startTimeNs;
                                this.renderSwapBufferTimeNs += currentTimeNs - swapBuffersStartTimeNs;
                            }
                            notifyCallbacks(frame, texMatrix);
                            VideoRenderer.renderFrameDone(frame);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void notifyCallbacks(VideoRenderer.I420Frame frame, float[] texMatrix) {
        synchronized (this.frameListenerLock) {
            if (!this.frameListeners.isEmpty()) {
                ArrayList<ScaleAndFrameListener> tmpList = new ArrayList<>(this.frameListeners);
                this.frameListeners.clear();
                float[] bitmapMatrix = RendererCommon.multiplyMatrices(RendererCommon.multiplyMatrices(texMatrix, this.mirror ? RendererCommon.horizontalFlipMatrix() : RendererCommon.identityMatrix()), RendererCommon.verticalFlipMatrix());
                for (ScaleAndFrameListener scaleAndListener : tmpList) {
                    int scaledWidth = (int) (scaleAndListener.scale * frame.rotatedWidth());
                    int scaledHeight = (int) (scaleAndListener.scale * frame.rotatedHeight());
                    if (scaledWidth == 0 || scaledHeight == 0) {
                        scaleAndListener.listener.onFrame(null);
                    } else {
                        if (this.bitmapTextureFramebuffer == null) {
                            this.bitmapTextureFramebuffer = new GlTextureFrameBuffer(6408);
                        }
                        this.bitmapTextureFramebuffer.setSize(scaledWidth, scaledHeight);
                        GLES20.glBindFramebuffer(36160, this.bitmapTextureFramebuffer.getFrameBufferId());
                        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.bitmapTextureFramebuffer.getTextureId(), 0);
                        if (frame.yuvFrame) {
                            this.drawer.drawYuv(this.yuvTextures, bitmapMatrix, frame.rotatedWidth(), frame.rotatedHeight(), 0, 0, scaledWidth, scaledHeight);
                        } else {
                            this.drawer.drawOes(frame.textureId, bitmapMatrix, frame.rotatedWidth(), frame.rotatedHeight(), 0, 0, scaledWidth, scaledHeight);
                        }
                        ByteBuffer bitmapBuffer = ByteBuffer.allocateDirect(scaledWidth * scaledHeight * 4);
                        GLES20.glViewport(0, 0, scaledWidth, scaledHeight);
                        GLES20.glReadPixels(0, 0, scaledWidth, scaledHeight, 6408, 5121, bitmapBuffer);
                        GLES20.glBindFramebuffer(36160, 0);
                        GlUtil.checkNoGLES2Error("EglRenderer.notifyCallbacks");
                        Bitmap bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
                        bitmap.copyPixelsFromBuffer(bitmapBuffer);
                        scaleAndListener.listener.onFrame(bitmap);
                    }
                }
            }
        }
    }

    private String averageTimeAsString(long sumTimeNs, int count) {
        return count <= 0 ? "NA" : TimeUnit.NANOSECONDS.toMicros(sumTimeNs / ((long) count)) + " μs";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logStatistics() {
        long currentTimeNs = System.nanoTime();
        synchronized (this.statisticsLock) {
            long elapsedTimeNs = currentTimeNs - this.statisticsStartTimeNs;
            if (elapsedTimeNs > 0) {
                float renderFps = (((long) this.framesRendered) * TimeUnit.SECONDS.toNanos(1L)) / elapsedTimeNs;
                logD("Duration: " + TimeUnit.NANOSECONDS.toMillis(elapsedTimeNs) + " ms. Frames received: " + this.framesReceived + ". Dropped: " + this.framesDropped + ". Rendered: " + this.framesRendered + ". Render fps: " + String.format("%.1f", Float.valueOf(renderFps)) + ". Average render time: " + averageTimeAsString(this.renderTimeNs, this.framesRendered) + ". Average swapBuffer time: " + averageTimeAsString(this.renderSwapBufferTimeNs, this.framesRendered) + ".");
                resetStatistics(currentTimeNs);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String string) {
        Logging.d(TAG, this.name + string);
    }
}
