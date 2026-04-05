package android.support.wearable.view.drawer;

import android.annotation.TargetApi;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;
import android.view.View;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class FlingWatcher {
    private static final int POLLING_DELAY_MS = 100;
    private final FlingListener mFlingListener;
    private int mLastScrollY;
    private View mView;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mIsRunning = false;
    private final Runnable mCheckForChangeRunnable = new Runnable() { // from class: android.support.wearable.view.drawer.FlingWatcher.1
        @Override // java.lang.Runnable
        public void run() {
            FlingWatcher.this.checkForChange();
        }
    };

    public interface FlingListener {
        void onFlingComplete(View view);
    }

    public FlingWatcher(FlingListener flingListener) {
        this.mFlingListener = flingListener;
    }

    public void start(View view) {
        if (!this.mIsRunning) {
            this.mIsRunning = true;
            this.mView = view;
            this.mLastScrollY = view.getScrollY();
            scheduleNextCheckForChange();
        }
    }

    @VisibleForTesting
    void scheduleNextCheckForChange() {
        this.mHandler.postDelayed(this.mCheckForChangeRunnable, 100L);
    }

    @VisibleForTesting
    void checkForChange() {
        if (this.mIsRunning && this.mView != null) {
            int currentScrollY = this.mView.getScrollY();
            if (currentScrollY == this.mLastScrollY) {
                this.mIsRunning = false;
                this.mFlingListener.onFlingComplete(this.mView);
            } else {
                this.mLastScrollY = currentScrollY;
                scheduleNextCheckForChange();
            }
        }
    }
}
