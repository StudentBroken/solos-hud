package com.opentok.android.grafika.gles;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.view.Surface;
import com.opentok.android.OtLog;

/* JADX INFO: loaded from: classes15.dex */
@TargetApi(19)
public final class EglCore {
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    public static final int FLAG_RECORDABLE = 1;
    public static final int FLAG_TRY_GLES3 = 2;
    private static final OtLog.LogToken log = new OtLog.LogToken();
    private EGLConfig mEGLConfig;
    private EGLContext mEGLContext;
    private EGLDisplay mEGLDisplay;
    private int mGlVersion;

    public EglCore() {
        this(null, 0);
    }

    public EglCore(EGLContext sharedContext, int flags) {
        EGLConfig config;
        this.mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        this.mEGLContext = EGL14.EGL_NO_CONTEXT;
        this.mEGLConfig = null;
        this.mGlVersion = -1;
        if (this.mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("EGL already set up");
        }
        sharedContext = sharedContext == null ? EGL14.EGL_NO_CONTEXT : sharedContext;
        this.mEGLDisplay = EGL14.eglGetDisplay(0);
        if (this.mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL14 display");
        }
        int[] version = new int[2];
        if (!EGL14.eglInitialize(this.mEGLDisplay, version, 0, version, 1)) {
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }
        if ((flags & 2) != 0 && (config = getConfig(flags, 3)) != null) {
            int[] attrib3_list = {12440, 3, 12344};
            EGLContext context = EGL14.eglCreateContext(this.mEGLDisplay, config, sharedContext, attrib3_list, 0);
            if (EGL14.eglGetError() == 12288) {
                this.mEGLConfig = config;
                this.mEGLContext = context;
                this.mGlVersion = 3;
            }
        }
        if (this.mEGLContext == EGL14.EGL_NO_CONTEXT) {
            EGLConfig config2 = getConfig(flags, 2);
            if (config2 == null) {
                throw new RuntimeException("Unable to find a suitable EGLConfig");
            }
            int[] attrib2_list = {12440, 2, 12344};
            EGLContext context2 = EGL14.eglCreateContext(this.mEGLDisplay, config2, sharedContext, attrib2_list, 0);
            checkEglError("eglCreateContext");
            this.mEGLConfig = config2;
            this.mEGLContext = context2;
            this.mGlVersion = 2;
        }
        int[] values = new int[1];
        EGL14.eglQueryContext(this.mEGLDisplay, this.mEGLContext, 12440, values, 0);
        log.d("EGLContext created, client version %d", Integer.valueOf(values[0]));
    }

    private EGLConfig getConfig(int flags, int version) {
        int renderableType = 4;
        if (version >= 3) {
            renderableType = 4 | 64;
        }
        int[] attribList = new int[13];
        attribList[0] = 12324;
        attribList[1] = 8;
        attribList[2] = 12323;
        attribList[3] = 8;
        attribList[4] = 12322;
        attribList[5] = 8;
        attribList[6] = 12321;
        attribList[7] = 8;
        attribList[8] = 12352;
        attribList[9] = renderableType;
        attribList[10] = 12344;
        attribList[11] = 0;
        attribList[12] = 12344;
        if ((flags & 1) != 0) {
            attribList[attribList.length - 3] = EGL_RECORDABLE_ANDROID;
            attribList[attribList.length - 2] = 1;
        }
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (EGL14.eglChooseConfig(this.mEGLDisplay, attribList, 0, configs, 0, configs.length, numConfigs, 0)) {
            return configs[0];
        }
        log.w("unable to find RGB8888 / %d EGLConfig", Integer.valueOf(version));
        return null;
    }

    public void release() {
        if (this.mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglMakeCurrent(this.mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(this.mEGLDisplay);
        }
        this.mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        this.mEGLContext = EGL14.EGL_NO_CONTEXT;
        this.mEGLConfig = null;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
                log.w("WARNING: EglCore was not explicitly released -- state may be leaked", new Object[0]);
                release();
            }
        } finally {
            super.finalize();
        }
    }

    public void releaseSurface(EGLSurface eglSurface) {
        EGL14.eglDestroySurface(this.mEGLDisplay, eglSurface);
    }

    public EGLSurface createWindowSurface(Object surface) {
        if (!(surface instanceof Surface) && !(surface instanceof SurfaceTexture)) {
            throw new RuntimeException("invalid surface: " + surface);
        }
        int[] surfaceAttribs = {12344};
        EGLSurface eglSurface = EGL14.eglCreateWindowSurface(this.mEGLDisplay, this.mEGLConfig, surface, surfaceAttribs, 0);
        checkEglError("eglCreateWindowSurface");
        if (eglSurface == null) {
            throw new RuntimeException("surface was null");
        }
        return eglSurface;
    }

    public EGLSurface createOffscreenSurface(int width, int height) {
        int[] surfaceAttribs = {12375, width, 12374, height, 12344};
        EGLSurface eglSurface = EGL14.eglCreatePbufferSurface(this.mEGLDisplay, this.mEGLConfig, surfaceAttribs, 0);
        checkEglError("eglCreatePbufferSurface");
        if (eglSurface == null) {
            throw new RuntimeException("surface was null");
        }
        return eglSurface;
    }

    public void makeCurrent(EGLSurface eglSurface) {
        if (this.mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            log.w("NOTE: makeCurrent w/o display", new Object[0]);
        }
        if (!EGL14.eglMakeCurrent(this.mEGLDisplay, eglSurface, eglSurface, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public void makeCurrent(EGLSurface drawSurface, EGLSurface readSurface) {
        if (this.mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            log.w("NOTE: makeCurrent w/o display", new Object[0]);
        }
        if (!EGL14.eglMakeCurrent(this.mEGLDisplay, drawSurface, readSurface, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent(draw,read) failed");
        }
    }

    public void makeNothingCurrent() {
        if (!EGL14.eglMakeCurrent(this.mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public boolean swapBuffers(EGLSurface eglSurface) {
        return EGL14.eglSwapBuffers(this.mEGLDisplay, eglSurface);
    }

    public void setPresentationTime(EGLSurface eglSurface, long nsecs) {
        EGLExt.eglPresentationTimeANDROID(this.mEGLDisplay, eglSurface, nsecs);
    }

    public boolean isCurrent(EGLSurface eglSurface) {
        return this.mEGLContext.equals(EGL14.eglGetCurrentContext()) && eglSurface.equals(EGL14.eglGetCurrentSurface(12377));
    }

    public int querySurface(EGLSurface eglSurface, int what) {
        int[] value = new int[1];
        EGL14.eglQuerySurface(this.mEGLDisplay, eglSurface, what, value, 0);
        return value[0];
    }

    public String queryString(int what) {
        return EGL14.eglQueryString(this.mEGLDisplay, what);
    }

    public int getGlVersion() {
        return this.mGlVersion;
    }

    public static void logCurrent(String msg) {
        EGLDisplay display = EGL14.eglGetCurrentDisplay();
        EGLContext context = EGL14.eglGetCurrentContext();
        EGLSurface surface = EGL14.eglGetCurrentSurface(12377);
        log.w("Current EGL (%s): display=%p, context=%p, surface=%p", msg, display, context, surface);
    }

    private void checkEglError(String msg) {
        int error = EGL14.eglGetError();
        if (error != 12288) {
            throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
        }
    }
}
