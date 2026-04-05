package org.webrtc;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.concurrent.CountDownLatch;
import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.VideoRenderer;

/* JADX INFO: loaded from: classes57.dex */
public class SurfaceViewRenderer extends SurfaceView implements SurfaceHolder.Callback, VideoRenderer.Callbacks {
    private static final String TAG = "SurfaceViewRenderer";
    private final EglRenderer eglRenderer;
    private int frameRotation;
    private boolean isFirstFrameRendered;
    private final Object layoutLock;
    private RendererCommon.RendererEvents rendererEvents;
    private final String resourceName;
    private int rotatedFrameHeight;
    private int rotatedFrameWidth;
    private final RendererCommon.VideoLayoutMeasure videoLayoutMeasure;

    public SurfaceViewRenderer(Context context) {
        super(context);
        this.videoLayoutMeasure = new RendererCommon.VideoLayoutMeasure();
        this.layoutLock = new Object();
        this.resourceName = getResourceName();
        this.eglRenderer = new EglRenderer(this.resourceName);
        getHolder().addCallback(this);
    }

    public SurfaceViewRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.videoLayoutMeasure = new RendererCommon.VideoLayoutMeasure();
        this.layoutLock = new Object();
        this.resourceName = getResourceName();
        this.eglRenderer = new EglRenderer(this.resourceName);
        getHolder().addCallback(this);
    }

    public void init(EglBase.Context sharedContext, RendererCommon.RendererEvents rendererEvents) {
        init(sharedContext, rendererEvents, EglBase.CONFIG_PLAIN, new GlRectDrawer());
    }

    public void init(EglBase.Context sharedContext, RendererCommon.RendererEvents rendererEvents, int[] configAttributes, RendererCommon.GlDrawer drawer) {
        ThreadUtils.checkIsOnMainThread();
        this.rendererEvents = rendererEvents;
        synchronized (this.layoutLock) {
            this.rotatedFrameWidth = 0;
            this.rotatedFrameHeight = 0;
            this.frameRotation = 0;
        }
        this.eglRenderer.init(sharedContext, configAttributes, drawer);
    }

    public void release() {
        this.eglRenderer.release();
    }

    public void setMirror(boolean mirror) {
        this.eglRenderer.setMirror(mirror);
    }

    public void setScalingType(RendererCommon.ScalingType scalingType) {
        ThreadUtils.checkIsOnMainThread();
        this.videoLayoutMeasure.setScalingType(scalingType);
    }

    public void setScalingType(RendererCommon.ScalingType scalingTypeMatchOrientation, RendererCommon.ScalingType scalingTypeMismatchOrientation) {
        ThreadUtils.checkIsOnMainThread();
        this.videoLayoutMeasure.setScalingType(scalingTypeMatchOrientation, scalingTypeMismatchOrientation);
    }

    @Override // org.webrtc.VideoRenderer.Callbacks
    public void renderFrame(VideoRenderer.I420Frame frame) {
        updateFrameDimensionsAndReportEvents(frame);
        this.eglRenderer.renderFrame(frame);
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int widthSpec, int heightSpec) {
        Point size;
        ThreadUtils.checkIsOnMainThread();
        synchronized (this.layoutLock) {
            size = this.videoLayoutMeasure.measure(widthSpec, heightSpec, this.rotatedFrameWidth, this.rotatedFrameHeight);
        }
        setMeasuredDimension(size.x, size.y);
        logD("onMeasure(). New size: " + size.x + "x" + size.y);
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        ThreadUtils.checkIsOnMainThread();
        this.eglRenderer.setLayoutAspectRatio((right - left) / (bottom - top));
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        ThreadUtils.checkIsOnMainThread();
        this.eglRenderer.createEglSurface(holder.getSurface());
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        ThreadUtils.checkIsOnMainThread();
        final CountDownLatch completionLatch = new CountDownLatch(1);
        this.eglRenderer.releaseEglSurface(new Runnable() { // from class: org.webrtc.SurfaceViewRenderer.1
            @Override // java.lang.Runnable
            public void run() {
                completionLatch.countDown();
            }
        });
        ThreadUtils.awaitUninterruptibly(completionLatch);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        ThreadUtils.checkIsOnMainThread();
        this.eglRenderer.surfaceSizeChanged(width, height);
    }

    private String getResourceName() {
        try {
            return getResources().getResourceEntryName(getId()) + ": ";
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }

    private void updateFrameDimensionsAndReportEvents(VideoRenderer.I420Frame frame) {
        synchronized (this.layoutLock) {
            if (!this.isFirstFrameRendered) {
                this.isFirstFrameRendered = true;
                logD("Reporting first rendered frame.");
                if (this.rendererEvents != null) {
                    this.rendererEvents.onFirstFrameRendered();
                }
            }
            if (this.rotatedFrameWidth != frame.rotatedWidth() || this.rotatedFrameHeight != frame.rotatedHeight() || this.frameRotation != frame.rotationDegree) {
                logD("Reporting frame resolution changed to " + frame.width + "x" + frame.height + " with rotation " + frame.rotationDegree);
                if (this.rendererEvents != null) {
                    this.rendererEvents.onFrameResolutionChanged(frame.width, frame.height, frame.rotationDegree);
                }
                this.rotatedFrameWidth = frame.rotatedWidth();
                this.rotatedFrameHeight = frame.rotatedHeight();
                this.frameRotation = frame.rotationDegree;
                post(new Runnable() { // from class: org.webrtc.SurfaceViewRenderer.2
                    @Override // java.lang.Runnable
                    public void run() {
                        SurfaceViewRenderer.this.requestLayout();
                    }
                });
            }
        }
    }

    private void logD(String string) {
        Logging.d(TAG, this.resourceName + string);
    }
}
