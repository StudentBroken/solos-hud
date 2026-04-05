package com.opentok.android.grafika.gles;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLSurface;
import com.opentok.android.OtLog;

/* JADX INFO: loaded from: classes15.dex */
@TargetApi(19)
public class EglSurfaceBase {
    private static final OtLog.LogToken log = new OtLog.LogToken();
    protected EglCore mEglCore;
    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
    private int mWidth = -1;
    private int mHeight = -1;

    protected EglSurfaceBase(EglCore eglCore) {
        this.mEglCore = eglCore;
    }

    public void createWindowSurface(Object surface) {
        if (this.mEGLSurface != EGL14.EGL_NO_SURFACE) {
            throw new IllegalStateException("surface already created");
        }
        this.mEGLSurface = this.mEglCore.createWindowSurface(surface);
    }

    public void createOffscreenSurface(int width, int height) {
        if (this.mEGLSurface != EGL14.EGL_NO_SURFACE) {
            throw new IllegalStateException("surface already created");
        }
        this.mEGLSurface = this.mEglCore.createOffscreenSurface(width, height);
        this.mWidth = width;
        this.mHeight = height;
    }

    public int getWidth() {
        return this.mWidth < 0 ? this.mEglCore.querySurface(this.mEGLSurface, 12375) : this.mWidth;
    }

    public int getHeight() {
        return this.mHeight < 0 ? this.mEglCore.querySurface(this.mEGLSurface, 12374) : this.mHeight;
    }

    public void releaseEglSurface() {
        this.mEglCore.releaseSurface(this.mEGLSurface);
        this.mEGLSurface = EGL14.EGL_NO_SURFACE;
        this.mHeight = -1;
        this.mWidth = -1;
    }

    public void makeCurrent() {
        this.mEglCore.makeCurrent(this.mEGLSurface);
    }

    public void makeCurrentReadFrom(EglSurfaceBase readSurface) {
        this.mEglCore.makeCurrent(this.mEGLSurface, readSurface.mEGLSurface);
    }

    public boolean swapBuffers() {
        boolean result = this.mEglCore.swapBuffers(this.mEGLSurface);
        if (!result) {
            log.d("WARNING: swapBuffers() failed", new Object[0]);
        }
        return result;
    }

    public void setPresentationTime(long nsecs) {
        this.mEglCore.setPresentationTime(this.mEGLSurface, nsecs);
    }
}
