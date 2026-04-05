package android.support.wearable.watchface;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.wearable.watchface.WatchFaceService;
import android.util.Log;
import android.view.Choreographer;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public abstract class Gles2WatchFaceService extends WatchFaceService {
    private static final int[] EGL_CONFIG_ATTRIB_LIST = {12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344};
    private static final int[] EGL_CONTEXT_ATTRIB_LIST = {12440, 2, 12344};
    private static final int[] EGL_SURFACE_ATTRIB_LIST = {12344};
    private static final boolean LOG_VERBOSE = false;
    private static final String TAG = "Gles2WatchFaceService";
    private static final boolean TRACE_DRAW = false;

    @Override // android.support.wearable.watchface.WatchFaceService, android.service.wallpaper.WallpaperService
    public Engine onCreateEngine() {
        return new Engine(this);
    }

    public class Engine extends WatchFaceService.Engine {
        private static final int MSG_INVALIDATE = 0;
        private boolean mCalledOnGlContextCreated;
        private final Choreographer mChoreographer;
        private boolean mDestroyed;
        private boolean mDrawRequested;
        private EGLConfig mEglConfig;
        private EGLContext mEglContext;
        private EGLDisplay mEglDisplay;
        private EGLSurface mEglSurface;
        private final Choreographer.FrameCallback mFrameCallback;
        private final Handler mHandler;
        private int mInsetBottom;
        private int mInsetLeft;

        public Engine(Gles2WatchFaceService this$0) {
            super();
            this.mChoreographer = Choreographer.getInstance();
            this.mFrameCallback = new Choreographer.FrameCallback() { // from class: android.support.wearable.watchface.Gles2WatchFaceService.Engine.1
                @Override // android.view.Choreographer.FrameCallback
                public void doFrame(long frameTimeNs) {
                    if (!Engine.this.mDestroyed && Engine.this.mDrawRequested) {
                        Engine.this.drawFrame();
                    }
                }
            };
            this.mHandler = new Handler() { // from class: android.support.wearable.watchface.Gles2WatchFaceService.Engine.2
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case 0:
                            Engine.this.invalidate();
                            break;
                    }
                }
            };
        }

        public EGLDisplay initializeEglDisplay() {
            EGLDisplay result = EGL14.eglGetDisplay(0);
            if (result == EGL14.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay returned EGL_NO_DISPLAY");
            }
            int[] version = new int[2];
            if (!EGL14.eglInitialize(result, version, 0, version, 1)) {
                throw new RuntimeException("eglInitialize failed");
            }
            if (Log.isLoggable(Gles2WatchFaceService.TAG, 3)) {
                int i = version[0];
                Log.d(Gles2WatchFaceService.TAG, new StringBuilder(35).append("EGL version ").append(i).append(".").append(version[1]).toString());
            }
            return result;
        }

        public EGLConfig chooseEglConfig(EGLDisplay eglDisplay) {
            int[] numEglConfigs = new int[1];
            EGLConfig[] eglConfigs = new EGLConfig[1];
            if (!EGL14.eglChooseConfig(eglDisplay, Gles2WatchFaceService.EGL_CONFIG_ATTRIB_LIST, 0, eglConfigs, 0, eglConfigs.length, numEglConfigs, 0)) {
                throw new RuntimeException("eglChooseConfig failed");
            }
            if (numEglConfigs[0] == 0) {
                throw new RuntimeException("no matching EGL configs");
            }
            return eglConfigs[0];
        }

        public EGLContext createEglContext(EGLDisplay eglDisplay, EGLConfig eglConfig) {
            EGLContext result = EGL14.eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, Gles2WatchFaceService.EGL_CONTEXT_ATTRIB_LIST, 0);
            if (result == EGL14.EGL_NO_CONTEXT) {
                throw new RuntimeException("eglCreateContext failed");
            }
            return result;
        }

        public EGLSurface createWindowSurface(EGLDisplay eglDisplay, EGLConfig eglConfig, SurfaceHolder surfaceHolder) {
            EGLSurface result = EGL14.eglCreateWindowSurface(eglDisplay, eglConfig, surfaceHolder.getSurface(), Gles2WatchFaceService.EGL_SURFACE_ATTRIB_LIST, 0);
            if (result == EGL14.EGL_NO_SURFACE) {
                throw new RuntimeException("eglCreateWindowSurface failed");
            }
            return result;
        }

        private void makeContextCurrent() {
            if (!EGL14.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext)) {
                throw new RuntimeException("eglMakeCurrent failed");
            }
        }

        @Override // android.support.wearable.watchface.WatchFaceService.Engine, android.service.wallpaper.WallpaperService.Engine
        @CallSuper
        public void onCreate(SurfaceHolder surfaceHolder) {
            if (Log.isLoggable(Gles2WatchFaceService.TAG, 3)) {
                Log.d(Gles2WatchFaceService.TAG, "onCreate");
            }
            super.onCreate(surfaceHolder);
            if (this.mEglDisplay == null) {
                this.mEglDisplay = initializeEglDisplay();
            }
            if (this.mEglConfig == null) {
                this.mEglConfig = chooseEglConfig(this.mEglDisplay);
            }
            if (this.mEglContext == null) {
                this.mEglContext = createEglContext(this.mEglDisplay, this.mEglConfig);
            }
        }

        @Override // android.support.wearable.watchface.WatchFaceService.Engine, android.service.wallpaper.WallpaperService.Engine
        @CallSuper
        public void onDestroy() {
            this.mDestroyed = true;
            this.mHandler.removeMessages(0);
            this.mChoreographer.removeFrameCallback(this.mFrameCallback);
            if (this.mEglSurface != null) {
                if (!EGL14.eglDestroySurface(this.mEglDisplay, this.mEglSurface)) {
                    Log.w(Gles2WatchFaceService.TAG, "eglDestroySurface failed");
                }
                this.mEglSurface = null;
            }
            if (this.mEglContext != null) {
                if (!EGL14.eglDestroyContext(this.mEglDisplay, this.mEglContext)) {
                    Log.w(Gles2WatchFaceService.TAG, "eglDestroyContext failed");
                }
                this.mEglContext = null;
            }
            if (this.mEglDisplay != null) {
                if (!EGL14.eglTerminate(this.mEglDisplay)) {
                    Log.w(Gles2WatchFaceService.TAG, "eglTerminate failed");
                }
                this.mEglDisplay = null;
            }
            super.onDestroy();
        }

        public void onGlContextCreated() {
        }

        public void onGlSurfaceCreated(int width, int height) {
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onApplyWindowInsets(WindowInsets insets) {
            if (Log.isLoggable(Gles2WatchFaceService.TAG, 3)) {
                String strValueOf = String.valueOf(insets);
                Log.d(Gles2WatchFaceService.TAG, new StringBuilder(String.valueOf(strValueOf).length() + 21).append("onApplyWindowInsets: ").append(strValueOf).toString());
            }
            super.onApplyWindowInsets(insets);
            if (Build.VERSION.SDK_INT <= 21) {
                Rect bounds = getSurfaceHolder().getSurfaceFrame();
                this.mInsetLeft = insets.getSystemWindowInsetLeft();
                this.mInsetBottom = insets.getSystemWindowInsetBottom();
                makeContextCurrent();
                GLES20.glViewport(-this.mInsetLeft, -this.mInsetBottom, bounds.width(), bounds.height());
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public final void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (Log.isLoggable(Gles2WatchFaceService.TAG, 3)) {
                Log.d(Gles2WatchFaceService.TAG, "onSurfaceChanged");
            }
            super.onSurfaceChanged(holder, format, width, height);
            if (this.mEglSurface != null && !EGL14.eglDestroySurface(this.mEglDisplay, this.mEglSurface)) {
                Log.w(Gles2WatchFaceService.TAG, "eglDestroySurface failed");
            }
            this.mEglSurface = createWindowSurface(this.mEglDisplay, this.mEglConfig, holder);
            makeContextCurrent();
            GLES20.glViewport(-this.mInsetLeft, -this.mInsetBottom, width, height);
            if (!this.mCalledOnGlContextCreated) {
                this.mCalledOnGlContextCreated = true;
                onGlContextCreated();
            }
            onGlSurfaceCreated(width, height);
            invalidate();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public final void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            if (Log.isLoggable(Gles2WatchFaceService.TAG, 3)) {
                Log.d(Gles2WatchFaceService.TAG, "onSurfaceRedrawNeeded");
            }
            super.onSurfaceRedrawNeeded(holder);
            drawFrame();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public final void onSurfaceDestroyed(SurfaceHolder holder) {
            if (Log.isLoggable(Gles2WatchFaceService.TAG, 3)) {
                Log.d(Gles2WatchFaceService.TAG, "onSurfaceDestroyed");
            }
            try {
                if (!EGL14.eglDestroySurface(this.mEglDisplay, this.mEglSurface)) {
                    Log.w(Gles2WatchFaceService.TAG, "eglDestroySurface failed");
                }
                this.mEglSurface = null;
            } finally {
                super.onSurfaceDestroyed(holder);
            }
        }

        public final void invalidate() {
            if (!this.mDrawRequested) {
                this.mDrawRequested = true;
                this.mChoreographer.postFrameCallback(this.mFrameCallback);
            }
        }

        public final void postInvalidate() {
            this.mHandler.sendEmptyMessage(0);
        }

        public void onDraw() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void drawFrame() {
            this.mDrawRequested = false;
            if (this.mEglSurface != null) {
                makeContextCurrent();
                onDraw();
                if (!EGL14.eglSwapBuffers(this.mEglDisplay, this.mEglSurface)) {
                    Log.w(Gles2WatchFaceService.TAG, "eglSwapBuffers failed");
                }
            }
        }
    }
}
