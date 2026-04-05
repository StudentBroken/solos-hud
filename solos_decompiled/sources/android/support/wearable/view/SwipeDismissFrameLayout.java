package android.support.wearable.view;

import android.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.UiThread;
import android.support.wearable.internal.view.SwipeDismissLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
public class SwipeDismissFrameLayout extends SwipeDismissLayout {
    private static final float DEFAULT_INTERPOLATION_FACTOR = 1.5f;
    private static final String TAG = "SwipeDismissFrameLayout";
    private static final float TRANSLATION_MIN_ALPHA = 0.5f;
    private final int mAnimationTime;
    private final ArrayList<Callback> mCallbacks;
    private final DecelerateInterpolator mCancelInterpolator;
    private final DecelerateInterpolator mCompleteDismissGestureInterpolator;
    private final AccelerateInterpolator mDismissInterpolator;
    private final SwipeDismissLayout.OnDismissedListener mOnDismissedListener;
    private final SwipeDismissLayout.OnPreSwipeListener mOnPreSwipeListener;
    private final SwipeDismissLayout.OnSwipeProgressChangedListener mOnSwipeProgressListener;
    private boolean mStarted;

    public static abstract class Callback {
        @UiThread
        public boolean onPreSwipeStart(float xDown, float yDown) {
            return true;
        }

        @UiThread
        public void onSwipeStart() {
        }

        @UiThread
        public void onSwipeCancelled() {
        }

        @UiThread
        public void onDismissed(SwipeDismissFrameLayout layout) {
        }
    }

    @UiThread
    public SwipeDismissFrameLayout(Context context) {
        this(context, null, 0);
    }

    @UiThread
    public SwipeDismissFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    @UiThread
    public SwipeDismissFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mOnPreSwipeListener = new MyOnPreSwipeListener();
        this.mOnDismissedListener = new MyOnDismissedListener();
        this.mOnSwipeProgressListener = new MyOnSwipeProgressChangedListener();
        this.mCallbacks = new ArrayList<>();
        setOnPreSwipeListener(this.mOnPreSwipeListener);
        setOnDismissedListener(this.mOnDismissedListener);
        setOnSwipeProgressChangedListener(this.mOnSwipeProgressListener);
        this.mAnimationTime = getContext().getResources().getInteger(R.integer.config_shortAnimTime);
        this.mCancelInterpolator = new DecelerateInterpolator(DEFAULT_INTERPOLATION_FACTOR);
        this.mDismissInterpolator = new AccelerateInterpolator(DEFAULT_INTERPOLATION_FACTOR);
        this.mCompleteDismissGestureInterpolator = new DecelerateInterpolator(DEFAULT_INTERPOLATION_FACTOR);
    }

    @UiThread
    public void setDismissEnabled(boolean isDismissEnabled) {
        setSwipeable(isDismissEnabled);
    }

    @UiThread
    public boolean isDismissEnabled() {
        return isSwipeable();
    }

    @UiThread
    public void addCallback(Callback callback) {
        if (callback == null) {
            throw new NullPointerException("addCallback called with null callback");
        }
        this.mCallbacks.add(callback);
    }

    @UiThread
    public void removeCallback(Callback callback) {
        if (callback == null) {
            throw new NullPointerException("removeCallback called with null callback");
        }
        if (!this.mCallbacks.remove(callback)) {
            throw new IllegalStateException("removeCallback called with nonexistent callback");
        }
    }

    @UiThread
    public void reset() {
        animate().cancel();
        setTranslationX(0.0f);
        setAlpha(1.0f);
        this.mStarted = false;
    }

    @UiThread
    public void dismiss(boolean decelerate) {
        for (int i = this.mCallbacks.size() - 1; i >= 0; i--) {
            Callback callbacks = this.mCallbacks.get(i);
            callbacks.onSwipeStart();
        }
        if (getVisibility() == 0) {
            if (decelerate) {
                this.mStarted = true;
            }
            this.mOnDismissedListener.onDismissed(this);
        }
    }

    private final class MyOnPreSwipeListener implements SwipeDismissLayout.OnPreSwipeListener {
        private MyOnPreSwipeListener() {
        }

        @Override // android.support.wearable.internal.view.SwipeDismissLayout.OnPreSwipeListener
        public boolean onPreSwipe(float xDown, float yDown) {
            for (Callback callback : SwipeDismissFrameLayout.this.mCallbacks) {
                if (!callback.onPreSwipeStart(xDown, yDown)) {
                    return false;
                }
            }
            return true;
        }
    }

    private final class MyOnDismissedListener implements SwipeDismissLayout.OnDismissedListener {
        private MyOnDismissedListener() {
        }

        @Override // android.support.wearable.internal.view.SwipeDismissLayout.OnDismissedListener
        public void onDismissed(SwipeDismissLayout layout) {
            if (Log.isLoggable(SwipeDismissFrameLayout.TAG, 3)) {
                Log.d(SwipeDismissFrameLayout.TAG, "onDismissed()");
            }
            SwipeDismissFrameLayout.this.animate().translationX(SwipeDismissFrameLayout.this.getWidth()).alpha(0.0f).setDuration(SwipeDismissFrameLayout.this.mAnimationTime).setInterpolator(SwipeDismissFrameLayout.this.mStarted ? SwipeDismissFrameLayout.this.mCompleteDismissGestureInterpolator : SwipeDismissFrameLayout.this.mDismissInterpolator).withEndAction(new Runnable() { // from class: android.support.wearable.view.SwipeDismissFrameLayout.MyOnDismissedListener.1
                @Override // java.lang.Runnable
                public void run() {
                    for (int i = SwipeDismissFrameLayout.this.mCallbacks.size() - 1; i >= 0; i--) {
                        Callback callbacks = (Callback) SwipeDismissFrameLayout.this.mCallbacks.get(i);
                        callbacks.onDismissed(SwipeDismissFrameLayout.this);
                    }
                }
            });
        }
    }

    private final class MyOnSwipeProgressChangedListener implements SwipeDismissLayout.OnSwipeProgressChangedListener {
        private MyOnSwipeProgressChangedListener() {
        }

        @Override // android.support.wearable.internal.view.SwipeDismissLayout.OnSwipeProgressChangedListener
        public void onSwipeProgressChanged(SwipeDismissLayout layout, float progress, float translate) {
            if (Log.isLoggable(SwipeDismissFrameLayout.TAG, 3)) {
                Log.d(SwipeDismissFrameLayout.TAG, new StringBuilder(42).append("onSwipeProgressChanged() - ").append(translate).toString());
            }
            SwipeDismissFrameLayout.this.setTranslationX(translate);
            SwipeDismissFrameLayout.this.setAlpha(1.0f - (SwipeDismissFrameLayout.TRANSLATION_MIN_ALPHA * progress));
            if (!SwipeDismissFrameLayout.this.mStarted) {
                for (int i = SwipeDismissFrameLayout.this.mCallbacks.size() - 1; i >= 0; i--) {
                    Callback callbacks = (Callback) SwipeDismissFrameLayout.this.mCallbacks.get(i);
                    callbacks.onSwipeStart();
                }
                SwipeDismissFrameLayout.this.mStarted = true;
            }
        }

        @Override // android.support.wearable.internal.view.SwipeDismissLayout.OnSwipeProgressChangedListener
        public void onSwipeCancelled(SwipeDismissLayout layout) {
            if (Log.isLoggable(SwipeDismissFrameLayout.TAG, 3)) {
                Log.d(SwipeDismissFrameLayout.TAG, "onSwipeCancelled() run swipe cancel animation");
            }
            SwipeDismissFrameLayout.this.mStarted = false;
            SwipeDismissFrameLayout.this.animate().translationX(0.0f).alpha(1.0f).setDuration(SwipeDismissFrameLayout.this.mAnimationTime).setInterpolator(SwipeDismissFrameLayout.this.mCancelInterpolator).withEndAction(new Runnable() { // from class: android.support.wearable.view.SwipeDismissFrameLayout.MyOnSwipeProgressChangedListener.1
                @Override // java.lang.Runnable
                public void run() {
                    for (int i = SwipeDismissFrameLayout.this.mCallbacks.size() - 1; i >= 0; i--) {
                        Callback callbacks = (Callback) SwipeDismissFrameLayout.this.mCallbacks.get(i);
                        callbacks.onSwipeCancelled();
                    }
                }
            });
        }
    }
}
