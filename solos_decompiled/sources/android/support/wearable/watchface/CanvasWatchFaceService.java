package android.support.wearable.watchface;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.wearable.watchface.WatchFaceService;
import android.util.Log;
import android.view.Choreographer;
import android.view.SurfaceHolder;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public abstract class CanvasWatchFaceService extends WatchFaceService {
    private static final boolean LOG_VERBOSE = false;
    private static final String TAG = "CanvasWatchFaceService";
    private static final boolean TRACE_DRAW = false;

    @Override // android.support.wearable.watchface.WatchFaceService, android.service.wallpaper.WallpaperService
    public Engine onCreateEngine() {
        return new Engine(this);
    }

    public class Engine extends WatchFaceService.Engine {
        private static final int MSG_INVALIDATE = 0;
        private final Choreographer mChoreographer;
        private boolean mDestroyed;
        private boolean mDrawRequested;
        private final Choreographer.FrameCallback mFrameCallback;
        private final Handler mHandler;

        public Engine(CanvasWatchFaceService this$0) {
            super();
            this.mChoreographer = Choreographer.getInstance();
            this.mFrameCallback = new Choreographer.FrameCallback() { // from class: android.support.wearable.watchface.CanvasWatchFaceService.Engine.1
                @Override // android.view.Choreographer.FrameCallback
                public void doFrame(long frameTimeNs) {
                    if (!Engine.this.mDestroyed && Engine.this.mDrawRequested) {
                        Engine.this.draw(Engine.this.getSurfaceHolder());
                    }
                }
            };
            this.mHandler = new Handler() { // from class: android.support.wearable.watchface.CanvasWatchFaceService.Engine.2
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

        @Override // android.support.wearable.watchface.WatchFaceService.Engine, android.service.wallpaper.WallpaperService.Engine
        @CallSuper
        public void onDestroy() {
            this.mDestroyed = true;
            this.mHandler.removeMessages(0);
            this.mChoreographer.removeFrameCallback(this.mFrameCallback);
            super.onDestroy();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (Log.isLoggable(CanvasWatchFaceService.TAG, 3)) {
                Log.d(CanvasWatchFaceService.TAG, "onSurfaceChanged");
            }
            super.onSurfaceChanged(holder, format, width, height);
            invalidate();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            if (Log.isLoggable(CanvasWatchFaceService.TAG, 3)) {
                Log.d(CanvasWatchFaceService.TAG, "onSurfaceRedrawNeeded");
            }
            super.onSurfaceRedrawNeeded(holder);
            draw(holder);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public void onSurfaceCreated(SurfaceHolder holder) {
            if (Log.isLoggable(CanvasWatchFaceService.TAG, 3)) {
                Log.d(CanvasWatchFaceService.TAG, "onSurfaceCreated");
            }
            super.onSurfaceCreated(holder);
            invalidate();
        }

        public void invalidate() {
            if (!this.mDrawRequested) {
                this.mDrawRequested = true;
                this.mChoreographer.postFrameCallback(this.mFrameCallback);
            }
        }

        public void postInvalidate() {
            this.mHandler.sendEmptyMessage(0);
        }

        public void onDraw(Canvas canvas, Rect bounds) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void draw(SurfaceHolder holder) {
            this.mDrawRequested = false;
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                try {
                    onDraw(canvas, holder.getSurfaceFrame());
                } finally {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
