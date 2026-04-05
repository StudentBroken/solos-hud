package org.webrtc;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import org.webrtc.EglBase10;
import org.webrtc.EglBase14;

/* JADX INFO: loaded from: classes57.dex */
public abstract class EglBase {
    private static final int EGL_OPENGL_ES2_BIT = 4;
    public static final Object lock = new Object();
    public static final int[] CONFIG_PLAIN = {12324, 8, 12323, 8, 12322, 8, 12352, 4, 12344};
    public static final int[] CONFIG_RGBA = {12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12344};
    public static final int[] CONFIG_PIXEL_BUFFER = {12324, 8, 12323, 8, 12322, 8, 12352, 4, 12339, 1, 12344};
    public static final int[] CONFIG_PIXEL_RGBA_BUFFER = {12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12339, 1, 12344};
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    public static final int[] CONFIG_RECORDABLE = {12324, 8, 12323, 8, 12322, 8, 12352, 4, EGL_RECORDABLE_ANDROID, 1, 12344};

    public static class Context {
    }

    public abstract void createDummyPbufferSurface();

    public abstract void createPbufferSurface(int i, int i2);

    public abstract void createSurface(SurfaceTexture surfaceTexture);

    public abstract void createSurface(Surface surface);

    public abstract void detachCurrent();

    public abstract Context getEglBaseContext();

    public abstract boolean hasSurface();

    public abstract void makeCurrent();

    public abstract void release();

    public abstract void releaseSurface();

    public abstract int surfaceHeight();

    public abstract int surfaceWidth();

    public abstract void swapBuffers();

    public static EglBase create(Context sharedContext, int[] configAttributes) {
        return (EglBase14.isEGL14Supported() && (sharedContext == null || (sharedContext instanceof EglBase14.Context))) ? new EglBase14((EglBase14.Context) sharedContext, configAttributes) : new EglBase10((EglBase10.Context) sharedContext, configAttributes);
    }

    public static EglBase create() {
        return create(null, CONFIG_PLAIN);
    }

    public static EglBase create(Context sharedContext) {
        return create(sharedContext, CONFIG_PLAIN);
    }
}
