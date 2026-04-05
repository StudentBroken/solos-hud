package android.support.wearable.internal.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
public class SwipeDismissLayout extends FrameLayout {
    public static final float DEFAULT_DISMISS_DRAG_WIDTH_RATIO = 0.33f;
    private static final float EDGE_SWIPE_THRESHOLD = 0.1f;
    private static final String TAG = "SwipeDismissLayout";
    private int mActiveTouchId;
    private boolean mCanStartSwipe;
    private boolean mDiscardIntercept;
    private float mDismissMinDragWidthRatio;
    private boolean mDismissed;
    private OnDismissedListener mDismissedListener;
    private float mDownX;
    private float mDownY;
    private float mGestureThresholdPx;
    private float mLastX;
    private int mMinFlingVelocity;

    @Nullable
    private OnPreSwipeListener mOnPreSwipeListener;
    private OnSwipeProgressChangedListener mProgressListener;
    private int mSlop;
    private boolean mSwipeable;
    private boolean mSwiping;
    private float mTranslationX;
    private VelocityTracker mVelocityTracker;

    public interface OnDismissedListener {
        void onDismissed(SwipeDismissLayout swipeDismissLayout);
    }

    public interface OnPreSwipeListener {
        @UiThread
        boolean onPreSwipe(float f, float f2);
    }

    public interface OnSwipeProgressChangedListener {
        void onSwipeCancelled(SwipeDismissLayout swipeDismissLayout);

        void onSwipeProgressChanged(SwipeDismissLayout swipeDismissLayout, float f, float f2);
    }

    public SwipeDismissLayout(Context context) {
        super(context);
        this.mCanStartSwipe = true;
        this.mDismissMinDragWidthRatio = 0.33f;
        init(context);
    }

    public SwipeDismissLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCanStartSwipe = true;
        this.mDismissMinDragWidthRatio = 0.33f;
        init(context);
    }

    public SwipeDismissLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCanStartSwipe = true;
        this.mDismissMinDragWidthRatio = 0.33f;
        init(context);
    }

    public void setDismissMinDragWidthRatio(float ratio) {
        this.mDismissMinDragWidthRatio = ratio;
    }

    public void setSwipeable(boolean swipeable) {
        this.mSwipeable = swipeable;
    }

    public boolean isSwipeable() {
        return this.mSwipeable;
    }

    private void init(Context context) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        this.mSlop = vc.getScaledTouchSlop();
        this.mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        this.mGestureThresholdPx = Resources.getSystem().getDisplayMetrics().widthPixels * EDGE_SWIPE_THRESHOLD;
        setSwipeable(true);
    }

    @UiThread
    public void setOnPreSwipeListener(@Nullable OnPreSwipeListener listener) {
        this.mOnPreSwipeListener = listener;
    }

    public void setOnDismissedListener(OnDismissedListener listener) {
        this.mDismissedListener = listener;
    }

    public void setOnSwipeProgressChangedListener(OnSwipeProgressChangedListener listener) {
        this.mProgressListener = listener;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!this.mSwipeable) {
            return super.onInterceptTouchEvent(ev);
        }
        ev.offsetLocation(this.mTranslationX, 0.0f);
        switch (ev.getActionMasked()) {
            case 0:
                resetMembers();
                this.mDownX = ev.getRawX();
                this.mDownY = ev.getRawY();
                this.mActiveTouchId = ev.getPointerId(0);
                this.mVelocityTracker = VelocityTracker.obtain();
                this.mVelocityTracker.addMovement(ev);
                break;
            case 1:
            case 3:
                resetMembers();
                break;
            case 2:
                if (this.mVelocityTracker != null && !this.mDiscardIntercept) {
                    int pointerIndex = ev.findPointerIndex(this.mActiveTouchId);
                    if (pointerIndex == -1) {
                        Log.e(TAG, "Invalid pointer index: ignoring.");
                        this.mDiscardIntercept = true;
                    } else {
                        float dx = ev.getRawX() - this.mDownX;
                        float x = ev.getX(pointerIndex);
                        float y = ev.getY(pointerIndex);
                        if (dx != 0.0f && this.mDownX >= this.mGestureThresholdPx && canScroll(this, false, dx, x, y)) {
                            this.mDiscardIntercept = true;
                        } else {
                            updateSwiping(ev);
                        }
                    }
                }
                break;
            case 5:
                this.mActiveTouchId = ev.getPointerId(ev.getActionIndex());
                break;
            case 6:
                int actionIndex = ev.getActionIndex();
                int pointerId = ev.getPointerId(actionIndex);
                if (pointerId == this.mActiveTouchId) {
                    int newActionIndex = actionIndex == 0 ? 1 : 0;
                    this.mActiveTouchId = ev.getPointerId(newActionIndex);
                }
                break;
        }
        if (this.mOnPreSwipeListener == null || this.mOnPreSwipeListener.onPreSwipe(this.mDownX, this.mDownY)) {
            return !this.mDiscardIntercept && this.mSwiping;
        }
        return false;
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int direction) {
        return direction < 0 && isSwipeable() && getVisibility() == 0;
    }

    private boolean isPotentialSwipe(float dx, float dy) {
        return (dx * dx) + (dy * dy) > ((float) (this.mSlop * this.mSlop));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (!this.mSwipeable) {
            return super.onTouchEvent(ev);
        }
        if (this.mVelocityTracker == null) {
            return super.onTouchEvent(ev);
        }
        ev.offsetLocation(this.mTranslationX, 0.0f);
        switch (ev.getActionMasked()) {
            case 1:
                updateDismiss(ev);
                if (this.mDismissed) {
                    dismiss();
                } else if (this.mSwiping) {
                    cancel();
                }
                resetMembers();
                return true;
            case 2:
                this.mVelocityTracker.addMovement(ev);
                this.mLastX = ev.getRawX();
                updateSwiping(ev);
                if (this.mSwiping) {
                    setProgress(ev.getRawX() - this.mDownX);
                }
                return true;
            case 3:
                cancel();
                resetMembers();
                return true;
            default:
                return true;
        }
    }

    private void setProgress(float deltaX) {
        this.mTranslationX = deltaX;
        if (this.mProgressListener != null && deltaX >= 0.0f) {
            this.mProgressListener.onSwipeProgressChanged(this, deltaX / getWidth(), deltaX);
        }
    }

    private void dismiss() {
        if (this.mDismissedListener != null) {
            this.mDismissedListener.onDismissed(this);
        }
    }

    protected void cancel() {
        if (this.mProgressListener != null) {
            this.mProgressListener.onSwipeCancelled(this);
        }
    }

    private void resetMembers() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
        }
        this.mVelocityTracker = null;
        this.mTranslationX = 0.0f;
        this.mDownX = 0.0f;
        this.mDownY = 0.0f;
        this.mSwiping = false;
        this.mDismissed = false;
        this.mDiscardIntercept = false;
        this.mCanStartSwipe = true;
    }

    private void updateSwiping(MotionEvent ev) {
        if (!this.mSwiping) {
            float deltaX = ev.getRawX() - this.mDownX;
            float deltaY = ev.getRawY() - this.mDownY;
            if (isPotentialSwipe(deltaX, deltaY)) {
                this.mSwiping = this.mCanStartSwipe && Math.abs(deltaY) < Math.abs(deltaX) && deltaX > 0.0f;
                this.mCanStartSwipe = this.mSwiping;
            }
        }
    }

    private void updateDismiss(MotionEvent ev) {
        float deltaX = ev.getRawX() - this.mDownX;
        this.mVelocityTracker.addMovement(ev);
        this.mVelocityTracker.computeCurrentVelocity(1000);
        if (!this.mDismissed && ((deltaX > getWidth() * this.mDismissMinDragWidthRatio && ev.getRawX() >= this.mLastX) || this.mVelocityTracker.getXVelocity() >= this.mMinFlingVelocity)) {
            this.mDismissed = true;
        }
        if (this.mDismissed && this.mSwiping && this.mVelocityTracker.getXVelocity() < (-this.mMinFlingVelocity)) {
            this.mDismissed = false;
        }
    }

    protected boolean canScroll(View v, boolean checkV, float dx, float x, float y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int count = group.getChildCount();
            for (int i = count - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if (scrollX + x >= child.getLeft() && scrollX + x < child.getRight() && scrollY + y >= child.getTop() && scrollY + y < child.getBottom() && canScroll(child, true, dx, (scrollX + x) - child.getLeft(), (scrollY + y) - child.getTop())) {
                    return true;
                }
            }
        }
        return checkV && v.canScrollHorizontally((int) (-dx));
    }
}
