package org.webrtc;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import org.webrtc.EglBase;
import org.webrtc.EglBase10;
import org.webrtc.EglBase14;
import org.webrtc.RendererCommon;
import org.webrtc.VideoRenderer;

/* JADX INFO: loaded from: classes57.dex */
public class VideoRendererGui implements GLSurfaceView.Renderer {
    private static final String TAG = "VideoRendererGui";
    private static Thread drawThread;
    private static Thread renderFrameThread;
    private boolean onSurfaceCreatedCalled;
    private int screenHeight;
    private int screenWidth;
    private GLSurfaceView surface;
    private final ArrayList<YuvImageRenderer> yuvImageRenderers;
    private static VideoRendererGui instance = null;
    private static Runnable eglContextReady = null;
    private static EglBase.Context eglContext = null;

    private VideoRendererGui(GLSurfaceView surface) {
        this.surface = surface;
        surface.setPreserveEGLContextOnPause(true);
        surface.setEGLContextClientVersion(2);
        surface.setRenderer(this);
        surface.setRenderMode(0);
        this.yuvImageRenderers = new ArrayList<>();
    }

    private static class YuvImageRenderer implements VideoRenderer.Callbacks {
        private long copyTimeNs;
        private final Rect displayLayout;
        private long drawTimeNs;
        private final RendererCommon.GlDrawer drawer;
        private int framesDropped;
        private int framesReceived;
        private int framesRendered;
        private int id;
        private final Rect layoutInPercentage;
        private float[] layoutMatrix;
        private boolean mirror;
        private VideoRenderer.I420Frame pendingFrame;
        private final Object pendingFrameLock;
        private RendererCommon.RendererEvents rendererEvents;
        private RendererType rendererType;
        private float[] rotatedSamplingMatrix;
        private int rotationDegree;
        private RendererCommon.ScalingType scalingType;
        private int screenHeight;
        private int screenWidth;
        boolean seenFrame;
        private long startTimeNs;
        private GLSurfaceView surface;
        private GlTextureFrameBuffer textureCopy;
        private final Object updateLayoutLock;
        private boolean updateLayoutProperties;
        private int videoHeight;
        private int videoWidth;
        private int[] yuvTextures;
        private final RendererCommon.YuvUploader yuvUploader;

        private enum RendererType {
            RENDERER_YUV,
            RENDERER_TEXTURE
        }

        private YuvImageRenderer(GLSurfaceView surface, int id, int x, int y, int width, int height, RendererCommon.ScalingType scalingType, boolean mirror, RendererCommon.GlDrawer drawer) {
            this.yuvTextures = new int[]{0, 0, 0};
            this.yuvUploader = new RendererCommon.YuvUploader();
            this.pendingFrameLock = new Object();
            this.startTimeNs = -1L;
            this.displayLayout = new Rect();
            this.updateLayoutLock = new Object();
            Logging.d(VideoRendererGui.TAG, "YuvImageRenderer.Create id: " + id);
            this.surface = surface;
            this.id = id;
            this.scalingType = scalingType;
            this.mirror = mirror;
            this.drawer = drawer;
            this.layoutInPercentage = new Rect(x, y, Math.min(100, x + width), Math.min(100, y + height));
            this.updateLayoutProperties = false;
            this.rotationDegree = 0;
        }

        public synchronized void reset() {
            this.seenFrame = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void release() {
            this.surface = null;
            this.drawer.release();
            synchronized (this.pendingFrameLock) {
                if (this.pendingFrame != null) {
                    VideoRenderer.renderFrameDone(this.pendingFrame);
                    this.pendingFrame = null;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void createTextures() {
            Logging.d(VideoRendererGui.TAG, "  YuvImageRenderer.createTextures " + this.id + " on GL thread:" + Thread.currentThread().getId());
            for (int i = 0; i < 3; i++) {
                this.yuvTextures[i] = GlUtil.generateTexture(3553);
            }
            this.textureCopy = new GlTextureFrameBuffer(6407);
        }

        private void updateLayoutMatrix() {
            float videoAspectRatio;
            synchronized (this.updateLayoutLock) {
                if (this.updateLayoutProperties) {
                    this.displayLayout.set(((this.screenWidth * this.layoutInPercentage.left) + 99) / 100, ((this.screenHeight * this.layoutInPercentage.top) + 99) / 100, (this.screenWidth * this.layoutInPercentage.right) / 100, (this.screenHeight * this.layoutInPercentage.bottom) / 100);
                    Logging.d(VideoRendererGui.TAG, "ID: " + this.id + ". AdjustTextureCoords. Allowed display size: " + this.displayLayout.width() + " x " + this.displayLayout.height() + ". Video: " + this.videoWidth + " x " + this.videoHeight + ". Rotation: " + this.rotationDegree + ". Mirror: " + this.mirror);
                    if (this.rotationDegree % 180 == 0) {
                        videoAspectRatio = this.videoWidth / this.videoHeight;
                    } else {
                        videoAspectRatio = this.videoHeight / this.videoWidth;
                    }
                    Point displaySize = RendererCommon.getDisplaySize(this.scalingType, videoAspectRatio, this.displayLayout.width(), this.displayLayout.height());
                    this.displayLayout.inset((this.displayLayout.width() - displaySize.x) / 2, (this.displayLayout.height() - displaySize.y) / 2);
                    Logging.d(VideoRendererGui.TAG, "  Adjusted display size: " + this.displayLayout.width() + " x " + this.displayLayout.height());
                    this.layoutMatrix = RendererCommon.getLayoutMatrix(this.mirror, videoAspectRatio, this.displayLayout.width() / this.displayLayout.height());
                    this.updateLayoutProperties = false;
                    Logging.d(VideoRendererGui.TAG, "  AdjustTextureCoords done");
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void draw() {
            boolean isNewFrame;
            if (this.seenFrame) {
                long now = System.nanoTime();
                synchronized (this.pendingFrameLock) {
                    isNewFrame = this.pendingFrame != null;
                    if (isNewFrame && this.startTimeNs == -1) {
                        this.startTimeNs = now;
                    }
                    if (isNewFrame) {
                        this.rotatedSamplingMatrix = RendererCommon.rotateTextureMatrix(this.pendingFrame.samplingMatrix, this.pendingFrame.rotationDegree);
                        if (this.pendingFrame.yuvFrame) {
                            this.rendererType = RendererType.RENDERER_YUV;
                            this.yuvUploader.uploadYuvData(this.yuvTextures, this.pendingFrame.width, this.pendingFrame.height, this.pendingFrame.yuvStrides, this.pendingFrame.yuvPlanes);
                        } else {
                            this.rendererType = RendererType.RENDERER_TEXTURE;
                            this.textureCopy.setSize(this.pendingFrame.rotatedWidth(), this.pendingFrame.rotatedHeight());
                            GLES20.glBindFramebuffer(36160, this.textureCopy.getFrameBufferId());
                            GlUtil.checkNoGLES2Error("glBindFramebuffer");
                            this.drawer.drawOes(this.pendingFrame.textureId, this.rotatedSamplingMatrix, this.textureCopy.getWidth(), this.textureCopy.getHeight(), 0, 0, this.textureCopy.getWidth(), this.textureCopy.getHeight());
                            this.rotatedSamplingMatrix = RendererCommon.identityMatrix();
                            GLES20.glBindFramebuffer(36160, 0);
                            GLES20.glFinish();
                        }
                        this.copyTimeNs += System.nanoTime() - now;
                        VideoRenderer.renderFrameDone(this.pendingFrame);
                        this.pendingFrame = null;
                    }
                }
                updateLayoutMatrix();
                float[] texMatrix = RendererCommon.multiplyMatrices(this.rotatedSamplingMatrix, this.layoutMatrix);
                int viewportY = this.screenHeight - this.displayLayout.bottom;
                if (this.rendererType == RendererType.RENDERER_YUV) {
                    this.drawer.drawYuv(this.yuvTextures, texMatrix, this.videoWidth, this.videoHeight, this.displayLayout.left, viewportY, this.displayLayout.width(), this.displayLayout.height());
                } else {
                    this.drawer.drawRgb(this.textureCopy.getTextureId(), texMatrix, this.videoWidth, this.videoHeight, this.displayLayout.left, viewportY, this.displayLayout.width(), this.displayLayout.height());
                }
                if (isNewFrame) {
                    this.framesRendered++;
                    this.drawTimeNs += System.nanoTime() - now;
                    if (this.framesRendered % 300 == 0) {
                        logStatistics();
                    }
                }
            }
        }

        private void logStatistics() {
            long timeSinceFirstFrameNs = System.nanoTime() - this.startTimeNs;
            Logging.d(VideoRendererGui.TAG, "ID: " + this.id + ". Type: " + this.rendererType + ". Frames received: " + this.framesReceived + ". Dropped: " + this.framesDropped + ". Rendered: " + this.framesRendered);
            if (this.framesReceived > 0 && this.framesRendered > 0) {
                Logging.d(VideoRendererGui.TAG, "Duration: " + ((int) (timeSinceFirstFrameNs / 1000000.0d)) + " ms. FPS: " + ((((double) this.framesRendered) * 1.0E9d) / timeSinceFirstFrameNs));
                Logging.d(VideoRendererGui.TAG, "Draw time: " + ((int) (this.drawTimeNs / ((long) (this.framesRendered * 1000)))) + " us. Copy time: " + ((int) (this.copyTimeNs / ((long) (this.framesReceived * 1000)))) + " us");
            }
        }

        public void setScreenSize(int screenWidth, int screenHeight) {
            synchronized (this.updateLayoutLock) {
                if (screenWidth != this.screenWidth || screenHeight != this.screenHeight) {
                    Logging.d(VideoRendererGui.TAG, "ID: " + this.id + ". YuvImageRenderer.setScreenSize: " + screenWidth + " x " + screenHeight);
                    this.screenWidth = screenWidth;
                    this.screenHeight = screenHeight;
                    this.updateLayoutProperties = true;
                }
            }
        }

        public void setPosition(int x, int y, int width, int height, RendererCommon.ScalingType scalingType, boolean mirror) {
            Rect layoutInPercentage = new Rect(x, y, Math.min(100, x + width), Math.min(100, y + height));
            synchronized (this.updateLayoutLock) {
                if (!layoutInPercentage.equals(this.layoutInPercentage) || scalingType != this.scalingType || mirror != this.mirror) {
                    Logging.d(VideoRendererGui.TAG, "ID: " + this.id + ". YuvImageRenderer.setPosition: (" + x + ", " + y + ") " + width + " x " + height + ". Scaling: " + scalingType + ". Mirror: " + mirror);
                    this.layoutInPercentage.set(layoutInPercentage);
                    this.scalingType = scalingType;
                    this.mirror = mirror;
                    this.updateLayoutProperties = true;
                }
            }
        }

        private void setSize(int videoWidth, int videoHeight, int rotation) {
            if (videoWidth != this.videoWidth || videoHeight != this.videoHeight || rotation != this.rotationDegree) {
                if (this.rendererEvents != null) {
                    Logging.d(VideoRendererGui.TAG, "ID: " + this.id + ". Reporting frame resolution changed to " + videoWidth + " x " + videoHeight);
                    this.rendererEvents.onFrameResolutionChanged(videoWidth, videoHeight, rotation);
                }
                synchronized (this.updateLayoutLock) {
                    Logging.d(VideoRendererGui.TAG, "ID: " + this.id + ". YuvImageRenderer.setSize: " + videoWidth + " x " + videoHeight + " rotation " + rotation);
                    this.videoWidth = videoWidth;
                    this.videoHeight = videoHeight;
                    this.rotationDegree = rotation;
                    this.updateLayoutProperties = true;
                    Logging.d(VideoRendererGui.TAG, "  YuvImageRenderer.setSize done.");
                }
            }
        }

        @Override // org.webrtc.VideoRenderer.Callbacks
        public synchronized void renderFrame(VideoRenderer.I420Frame frame) {
            if (this.surface != null) {
                if (VideoRendererGui.renderFrameThread == null) {
                    Thread unused = VideoRendererGui.renderFrameThread = Thread.currentThread();
                }
                if (!this.seenFrame && this.rendererEvents != null) {
                    Logging.d(VideoRendererGui.TAG, "ID: " + this.id + ". Reporting first rendered frame.");
                    this.rendererEvents.onFirstFrameRendered();
                }
                this.framesReceived++;
                synchronized (this.pendingFrameLock) {
                    if (frame.yuvFrame && (frame.yuvStrides[0] < frame.width || frame.yuvStrides[1] < frame.width / 2 || frame.yuvStrides[2] < frame.width / 2)) {
                        Logging.e(VideoRendererGui.TAG, "Incorrect strides " + frame.yuvStrides[0] + ", " + frame.yuvStrides[1] + ", " + frame.yuvStrides[2]);
                        VideoRenderer.renderFrameDone(frame);
                    } else if (this.pendingFrame != null) {
                        this.framesDropped++;
                        VideoRenderer.renderFrameDone(frame);
                        this.seenFrame = true;
                    } else {
                        this.pendingFrame = frame;
                        setSize(frame.width, frame.height, frame.rotationDegree);
                        this.seenFrame = true;
                        this.surface.requestRender();
                    }
                }
            } else {
                VideoRenderer.renderFrameDone(frame);
            }
        }
    }

    public static synchronized void setView(GLSurfaceView surface, Runnable eglContextReadyCallback) {
        Logging.d(TAG, "VideoRendererGui.setView");
        instance = new VideoRendererGui(surface);
        eglContextReady = eglContextReadyCallback;
    }

    public static synchronized EglBase.Context getEglBaseContext() {
        return eglContext;
    }

    public static synchronized void dispose() {
        if (instance != null) {
            Logging.d(TAG, "VideoRendererGui.dispose");
            synchronized (instance.yuvImageRenderers) {
                for (YuvImageRenderer yuvImageRenderer : instance.yuvImageRenderers) {
                    yuvImageRenderer.release();
                }
                instance.yuvImageRenderers.clear();
            }
            renderFrameThread = null;
            drawThread = null;
            instance.surface = null;
            eglContext = null;
            eglContextReady = null;
            instance = null;
        }
    }

    public static VideoRenderer createGui(int x, int y, int width, int height, RendererCommon.ScalingType scalingType, boolean mirror) throws Exception {
        YuvImageRenderer javaGuiRenderer = create(x, y, width, height, scalingType, mirror);
        return new VideoRenderer(javaGuiRenderer);
    }

    public static VideoRenderer.Callbacks createGuiRenderer(int x, int y, int width, int height, RendererCommon.ScalingType scalingType, boolean mirror) {
        return create(x, y, width, height, scalingType, mirror);
    }

    public static synchronized YuvImageRenderer create(int x, int y, int width, int height, RendererCommon.ScalingType scalingType, boolean mirror) {
        return create(x, y, width, height, scalingType, mirror, new GlRectDrawer());
    }

    public static synchronized YuvImageRenderer create(int x, int y, int width, int height, RendererCommon.ScalingType scalingType, boolean mirror, RendererCommon.GlDrawer drawer) {
        final YuvImageRenderer yuvImageRenderer;
        if (x < 0 || x > 100 || y < 0 || y > 100 || width < 0 || width > 100 || height < 0 || height > 100 || x + width > 100 || y + height > 100) {
            throw new RuntimeException("Incorrect window parameters.");
        }
        if (instance == null) {
            throw new RuntimeException("Attempt to create yuv renderer before setting GLSurfaceView");
        }
        yuvImageRenderer = new YuvImageRenderer(instance.surface, instance.yuvImageRenderers.size(), x, y, width, height, scalingType, mirror, drawer);
        synchronized (instance.yuvImageRenderers) {
            if (instance.onSurfaceCreatedCalled) {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                instance.surface.queueEvent(new Runnable() { // from class: org.webrtc.VideoRendererGui.1
                    @Override // java.lang.Runnable
                    public void run() {
                        yuvImageRenderer.createTextures();
                        yuvImageRenderer.setScreenSize(VideoRendererGui.instance.screenWidth, VideoRendererGui.instance.screenHeight);
                        countDownLatch.countDown();
                    }
                });
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            instance.yuvImageRenderers.add(yuvImageRenderer);
        }
        return yuvImageRenderer;
    }

    public static synchronized void update(VideoRenderer.Callbacks renderer, int x, int y, int width, int height, RendererCommon.ScalingType scalingType, boolean mirror) {
        Logging.d(TAG, "VideoRendererGui.update");
        if (instance == null) {
            throw new RuntimeException("Attempt to update yuv renderer before setting GLSurfaceView");
        }
        synchronized (instance.yuvImageRenderers) {
            for (YuvImageRenderer yuvImageRenderer : instance.yuvImageRenderers) {
                if (yuvImageRenderer == renderer) {
                    yuvImageRenderer.setPosition(x, y, width, height, scalingType, mirror);
                }
            }
        }
    }

    public static synchronized void setRendererEvents(VideoRenderer.Callbacks renderer, RendererCommon.RendererEvents rendererEvents) {
        Logging.d(TAG, "VideoRendererGui.setRendererEvents");
        if (instance == null) {
            throw new RuntimeException("Attempt to set renderer events before setting GLSurfaceView");
        }
        synchronized (instance.yuvImageRenderers) {
            for (YuvImageRenderer yuvImageRenderer : instance.yuvImageRenderers) {
                if (yuvImageRenderer == renderer) {
                    yuvImageRenderer.rendererEvents = rendererEvents;
                }
            }
        }
    }

    public static synchronized void remove(VideoRenderer.Callbacks renderer) {
        Logging.d(TAG, "VideoRendererGui.remove");
        if (instance == null) {
            throw new RuntimeException("Attempt to remove renderer before setting GLSurfaceView");
        }
        synchronized (instance.yuvImageRenderers) {
            int index = instance.yuvImageRenderers.indexOf(renderer);
            if (index != -1) {
                instance.yuvImageRenderers.remove(index).release();
            } else {
                Logging.w(TAG, "Couldn't remove renderer (not present in current list)");
            }
        }
    }

    public static synchronized void reset(VideoRenderer.Callbacks renderer) {
        Logging.d(TAG, "VideoRendererGui.reset");
        if (instance == null) {
            throw new RuntimeException("Attempt to reset renderer before setting GLSurfaceView");
        }
        synchronized (instance.yuvImageRenderers) {
            for (YuvImageRenderer yuvImageRenderer : instance.yuvImageRenderers) {
                if (yuvImageRenderer == renderer) {
                    yuvImageRenderer.reset();
                }
            }
        }
    }

    private static void printStackTrace(Thread thread, String threadName) {
        if (thread != null) {
            StackTraceElement[] stackTraces = thread.getStackTrace();
            if (stackTraces.length > 0) {
                Logging.d(TAG, threadName + " stacks trace:");
                for (StackTraceElement stackTrace : stackTraces) {
                    Logging.d(TAG, stackTrace.toString());
                }
            }
        }
    }

    public static synchronized void printStackTraces() {
        if (instance != null) {
            printStackTrace(renderFrameThread, "Render frame thread");
            printStackTrace(drawThread, "Draw thread");
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    @SuppressLint({"NewApi"})
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Logging.d(TAG, "VideoRendererGui.onSurfaceCreated");
        synchronized (VideoRendererGui.class) {
            if (EglBase14.isEGL14Supported()) {
                eglContext = new EglBase14.Context(EGL14.eglGetCurrentContext());
            } else {
                eglContext = new EglBase10.Context(((EGL10) EGLContext.getEGL()).eglGetCurrentContext());
            }
            Logging.d(TAG, "VideoRendererGui EGL Context: " + eglContext);
        }
        synchronized (this.yuvImageRenderers) {
            for (YuvImageRenderer yuvImageRenderer : this.yuvImageRenderers) {
                yuvImageRenderer.createTextures();
            }
            this.onSurfaceCreatedCalled = true;
        }
        GlUtil.checkNoGLES2Error("onSurfaceCreated done");
        GLES20.glPixelStorei(3317, 1);
        GLES20.glClearColor(0.15f, 0.15f, 0.15f, 1.0f);
        synchronized (VideoRendererGui.class) {
            if (eglContextReady != null) {
                eglContextReady.run();
            }
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        Logging.d(TAG, "VideoRendererGui.onSurfaceChanged: " + width + " x " + height + "  ");
        this.screenWidth = width;
        this.screenHeight = height;
        synchronized (this.yuvImageRenderers) {
            for (YuvImageRenderer yuvImageRenderer : this.yuvImageRenderers) {
                yuvImageRenderer.setScreenSize(this.screenWidth, this.screenHeight);
            }
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 unused) {
        if (drawThread == null) {
            drawThread = Thread.currentThread();
        }
        GLES20.glViewport(0, 0, this.screenWidth, this.screenHeight);
        GLES20.glClear(16384);
        synchronized (this.yuvImageRenderers) {
            for (YuvImageRenderer yuvImageRenderer : this.yuvImageRenderers) {
                yuvImageRenderer.draw();
            }
        }
    }
}
