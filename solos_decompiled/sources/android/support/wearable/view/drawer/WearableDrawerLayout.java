package android.support.wearable.view.drawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.wearable.view.drawer.FlingWatcher;
import android.support.wearable.view.drawer.ViewDragHelper;
import android.support.wearable.view.drawer.WearableDrawerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import com.kopin.solos.view.graphics.Bar;
import com.nuance.android.vocalizer.VocalizerEngine;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class WearableDrawerLayout extends FrameLayout implements View.OnLayoutChangeListener, NestedScrollingParent, FlingWatcher.FlingListener {
    private static final int DOWN = 1;
    private static final int GRAVITY_UNDEFINED = -1;
    private static final int NESTED_SCROLL_SLOP_DP = 5;
    private static final float OPENED_PERCENT_THRESHOLD = 0.5f;
    private static final int PEEK_AUTO_CLOSE_DELAY_MS = 1000;
    private static final int PEEK_FADE_DURATION_MS = 150;
    private static final String TAG = "WearableDrawerLayout";
    private static final int UP = -1;
    private final ViewDragHelper mBottomDrawerDragger;

    @VisibleForTesting
    final ViewDragHelper.Callback mBottomDrawerDraggerCallback;

    @Nullable
    private WearableDrawerView mBottomDrawerView;
    private boolean mCanBottomDrawerBeClosed;
    private boolean mCanTopDrawerBeClosed;
    private final ClosePeekRunnable mCloseBottomPeekRunnable;
    private final ClosePeekRunnable mCloseTopPeekRunnable;
    private int mCurrentNestedScrollSlopTracker;
    private MotionEvent mDrawerOpenLastInterceptedTouchEvent;
    private DrawerStateCallback mDrawerStateCallback;
    private final FlingWatcher mFlingWatcher;
    private final boolean mIsAccessibilityEnabled;
    private boolean mLastScrollWasFling;
    private final Handler mMainThreadHandler;
    private final int mNestedScrollSlopPx;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;

    @Nullable
    private View mScrollingContentView;
    private boolean mShouldOpenBottomDrawerAfterLayout;
    private boolean mShouldOpenTopDrawerAfterLayout;
    private boolean mShouldPeekBottomDrawerAfterLayout;
    private boolean mShouldPeekTopDrawerAfterLayout;
    private int mSystemWindowInsetBottom;
    private final ViewDragHelper mTopDrawerDragger;

    @VisibleForTesting
    final ViewDragHelper.Callback mTopDrawerDraggerCallback;

    @Nullable
    private WearableDrawerView mTopDrawerView;

    public static abstract class DrawerStateCallback {
        public abstract void onDrawerClosed(View view);

        public abstract void onDrawerOpened(View view);

        public abstract void onDrawerStateChanged(@WearableDrawerView.DrawerState int i);
    }

    public WearableDrawerLayout(Context context) {
        this(context, null);
    }

    public WearableDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public WearableDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mMainThreadHandler = new Handler(Looper.getMainLooper());
        this.mCloseTopPeekRunnable = new ClosePeekRunnable(48);
        this.mCloseBottomPeekRunnable = new ClosePeekRunnable(80);
        this.mFlingWatcher = new FlingWatcher(this);
        this.mTopDrawerDraggerCallback = new TopDrawerDraggerCallback();
        this.mTopDrawerDragger = ViewDragHelper.create(this, 1.0f, this.mTopDrawerDraggerCallback);
        this.mTopDrawerDragger.setEdgeTrackingEnabled(4);
        this.mBottomDrawerDraggerCallback = new BottomDrawerDraggerCallback();
        this.mBottomDrawerDragger = ViewDragHelper.create(this, 1.0f, this.mBottomDrawerDraggerCallback);
        this.mBottomDrawerDragger.setEdgeTrackingEnabled(8);
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        this.mNestedScrollSlopPx = Math.round(metrics.density * 5.0f);
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        this.mIsAccessibilityEnabled = accessibilityManager.isEnabled();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    @VisibleForTesting
    WearableDrawerLayout(Context context, FlingWatcher flingWatcher, @Nullable WearableDrawerView topDrawerView, @Nullable WearableDrawerView bottomDrawerView, ViewDragHelper topDrawerDragger, ViewDragHelper bottomDrawerDragger, boolean isAccessibilityEnabled) {
        super(context);
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mMainThreadHandler = new Handler(Looper.getMainLooper());
        this.mCloseTopPeekRunnable = new ClosePeekRunnable(48);
        this.mCloseBottomPeekRunnable = new ClosePeekRunnable(80);
        this.mFlingWatcher = flingWatcher;
        this.mTopDrawerDragger = topDrawerDragger;
        this.mBottomDrawerDragger = bottomDrawerDragger;
        this.mTopDrawerView = topDrawerView;
        this.mBottomDrawerView = bottomDrawerView;
        this.mTopDrawerDraggerCallback = new TopDrawerDraggerCallback();
        this.mBottomDrawerDraggerCallback = new BottomDrawerDraggerCallback();
        this.mNestedScrollSlopPx = 5;
        this.mIsAccessibilityEnabled = isAccessibilityEnabled;
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mTopDrawerDragger.refreshEdgeSize();
        this.mBottomDrawerDragger.refreshEdgeSize();
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        this.mSystemWindowInsetBottom = insets.getSystemWindowInsetBottom();
        if (this.mSystemWindowInsetBottom != 0) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
            layoutParams.bottomMargin = this.mSystemWindowInsetBottom;
            setLayoutParams(layoutParams);
        }
        return super.onApplyWindowInsets(insets);
    }

    @VisibleForTesting
    void closeDrawerDelayed(int gravity, long delayMs) {
        switch (gravity) {
            case Bar.DEFAULT_HEIGHT /* 48 */:
                this.mMainThreadHandler.removeCallbacks(this.mCloseTopPeekRunnable);
                this.mMainThreadHandler.postDelayed(this.mCloseTopPeekRunnable, delayMs);
                break;
            case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                this.mMainThreadHandler.removeCallbacks(this.mCloseBottomPeekRunnable);
                this.mMainThreadHandler.postDelayed(this.mCloseBottomPeekRunnable, delayMs);
                break;
            default:
                Log.w(TAG, new StringBuilder(67).append("Invoked a delayed drawer close with an invalid gravity: ").append(gravity).toString());
                break;
        }
    }

    public void closeDrawer(int gravity) {
        closeDrawer(findDrawerWithGravity(gravity));
    }

    public void closeDrawer(View drawer) {
        if (drawer != null) {
            if (drawer == this.mTopDrawerView) {
                this.mTopDrawerDragger.smoothSlideViewTo(this.mTopDrawerView, 0, -this.mTopDrawerView.getHeight());
                invalidate();
            } else if (drawer == this.mBottomDrawerView) {
                this.mBottomDrawerDragger.smoothSlideViewTo(this.mBottomDrawerView, 0, getHeight());
                invalidate();
            } else {
                Log.w(TAG, "closeDrawer(View) should be passed in the top or bottom drawer");
            }
        }
    }

    public void openDrawer(int gravity) {
        if (!isLaidOut()) {
            switch (gravity) {
                case Bar.DEFAULT_HEIGHT /* 48 */:
                    this.mShouldOpenTopDrawerAfterLayout = true;
                    break;
                case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                    this.mShouldOpenBottomDrawerAfterLayout = true;
                    break;
            }
            return;
        }
        openDrawer(findDrawerWithGravity(gravity));
    }

    public void openDrawer(View drawer) {
        if (drawer != null) {
            if (!isLaidOut()) {
                if (drawer == this.mTopDrawerView) {
                    this.mShouldOpenTopDrawerAfterLayout = true;
                    return;
                } else {
                    if (drawer == this.mBottomDrawerView) {
                        this.mShouldOpenBottomDrawerAfterLayout = true;
                        return;
                    }
                    return;
                }
            }
            if (drawer == this.mTopDrawerView) {
                this.mTopDrawerDragger.smoothSlideViewTo(this.mTopDrawerView, 0, 0);
                showDrawerContentMaybeAnimate(this.mTopDrawerView);
                invalidate();
            } else {
                if (drawer == this.mBottomDrawerView) {
                    this.mBottomDrawerDragger.smoothSlideViewTo(this.mBottomDrawerView, 0, getHeight() - this.mBottomDrawerView.getHeight());
                    showDrawerContentMaybeAnimate(this.mBottomDrawerView);
                    invalidate();
                    return;
                }
                Log.w(TAG, "openDrawer(View) should be passed in the top or bottom drawer");
            }
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if ((this.mBottomDrawerView != null && this.mBottomDrawerView.isOpened() && !this.mCanBottomDrawerBeClosed) || (this.mTopDrawerView != null && this.mTopDrawerView.isOpened() && !this.mCanTopDrawerBeClosed)) {
            this.mDrawerOpenLastInterceptedTouchEvent = ev;
            return false;
        }
        boolean shouldInterceptTop = this.mTopDrawerDragger.shouldInterceptTouchEvent(ev);
        boolean shouldInterceptBottom = this.mBottomDrawerDragger.shouldInterceptTouchEvent(ev);
        return shouldInterceptTop || shouldInterceptBottom;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev == null) {
            Log.w(TAG, "null MotionEvent passed to onTouchEvent");
            return false;
        }
        this.mTopDrawerDragger.processTouchEvent(ev);
        this.mBottomDrawerDragger.processTouchEvent(ev);
        return true;
    }

    @Override // android.view.View
    public void computeScroll() {
        boolean topSettling = this.mTopDrawerDragger.continueSettling(true);
        boolean bottomSettling = this.mBottomDrawerDragger.continueSettling(true);
        if (topSettling || bottomSettling) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        WearableDrawerView drawerView;
        super.addView(child, index, params);
        if (child instanceof WearableDrawerView) {
            WearableDrawerView drawerChild = (WearableDrawerView) child;
            int childGravity = ((FrameLayout.LayoutParams) params).gravity;
            if (childGravity == 0 || childGravity == -1) {
                ((FrameLayout.LayoutParams) params).gravity = drawerChild.preferGravity();
                childGravity = drawerChild.preferGravity();
                drawerChild.setLayoutParams(params);
            }
            if (childGravity == 48) {
                this.mTopDrawerView = drawerChild;
                drawerView = this.mTopDrawerView;
            } else if (childGravity == 80) {
                this.mBottomDrawerView = drawerChild;
                drawerView = this.mBottomDrawerView;
            } else {
                drawerView = null;
            }
            if (drawerView != null) {
                drawerView.addOnLayoutChangeListener(this);
            }
        }
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (v == this.mTopDrawerView) {
            float openedPercent = this.mTopDrawerView.getOpenedPercent();
            int height = v.getHeight();
            int childTop = (-height) + ((int) (height * openedPercent));
            v.layout(v.getLeft(), childTop, v.getRight(), childTop + height);
            return;
        }
        if (v == this.mBottomDrawerView) {
            float openedPercent2 = this.mBottomDrawerView.getOpenedPercent();
            int height2 = v.getHeight();
            int childTop2 = (int) (getHeight() - (height2 * openedPercent2));
            v.layout(v.getLeft(), childTop2, v.getRight(), childTop2 + height2);
        }
    }

    public void setDrawerStateCallback(DrawerStateCallback callback) {
        this.mDrawerStateCallback = callback;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mShouldPeekBottomDrawerAfterLayout || this.mShouldPeekTopDrawerAfterLayout || this.mShouldOpenTopDrawerAfterLayout || this.mShouldOpenBottomDrawerAfterLayout) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: android.support.wearable.view.drawer.WearableDrawerLayout.1
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public void onGlobalLayout() {
                    WearableDrawerLayout.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if (WearableDrawerLayout.this.mShouldOpenBottomDrawerAfterLayout) {
                        WearableDrawerLayout.this.openDrawerWithoutAnimation(WearableDrawerLayout.this.mBottomDrawerView);
                        WearableDrawerLayout.this.mShouldOpenBottomDrawerAfterLayout = false;
                    } else if (WearableDrawerLayout.this.mShouldPeekBottomDrawerAfterLayout) {
                        WearableDrawerLayout.this.peekDrawer(80);
                        WearableDrawerLayout.this.mShouldPeekBottomDrawerAfterLayout = false;
                    }
                    if (WearableDrawerLayout.this.mShouldOpenTopDrawerAfterLayout) {
                        WearableDrawerLayout.this.openDrawerWithoutAnimation(WearableDrawerLayout.this.mTopDrawerView);
                        WearableDrawerLayout.this.mShouldOpenTopDrawerAfterLayout = false;
                    } else if (WearableDrawerLayout.this.mShouldPeekTopDrawerAfterLayout) {
                        WearableDrawerLayout.this.peekDrawer(48);
                        WearableDrawerLayout.this.mShouldPeekTopDrawerAfterLayout = false;
                    }
                }
            });
        }
    }

    public void peekDrawer(int gravity) {
        if (!isLaidOut()) {
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "WearableDrawerLayout not laid out yet. Postponing peek.");
            }
            switch (gravity) {
                case Bar.DEFAULT_HEIGHT /* 48 */:
                    this.mShouldPeekTopDrawerAfterLayout = true;
                    break;
                case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                    this.mShouldPeekBottomDrawerAfterLayout = true;
                    break;
            }
            return;
        }
        WearableDrawerView drawerView = findDrawerWithGravity(gravity);
        maybePeekDrawer(drawerView);
    }

    public void peekDrawer(WearableDrawerView drawer) {
        if (drawer == null) {
            throw new IllegalArgumentException("peekDrawer(View) received a null drawer.");
        }
        if (drawer != this.mTopDrawerView && drawer != this.mBottomDrawerView) {
            throw new IllegalArgumentException("peekDrawer(View) received a drawer that isn't a child.");
        }
        if (!isLaidOut()) {
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "WearableDrawerLayout not laid out yet. Postponing peek.");
            }
            if (drawer == this.mTopDrawerView) {
                this.mShouldPeekTopDrawerAfterLayout = true;
                return;
            } else {
                if (drawer == this.mBottomDrawerView) {
                    this.mShouldPeekBottomDrawerAfterLayout = true;
                    return;
                }
                return;
            }
        }
        maybePeekDrawer(drawer);
    }

    @Override // android.support.wearable.view.drawer.FlingWatcher.FlingListener
    public void onFlingComplete(View view) {
        boolean canTopPeek = this.mTopDrawerView != null && this.mTopDrawerView.canAutoPeek();
        boolean canBottomPeek = this.mBottomDrawerView != null && this.mBottomDrawerView.canAutoPeek();
        boolean canScrollUp = view.canScrollVertically(-1);
        boolean canScrollDown = view.canScrollVertically(1);
        if (canTopPeek && !canScrollUp && !this.mTopDrawerView.isPeeking()) {
            peekDrawer(48);
        }
        if (canBottomPeek) {
            if ((!canScrollUp || !canScrollDown) && !this.mBottomDrawerView.isPeeking()) {
                peekDrawer(80);
            }
        }
    }

    @Override // android.view.ViewGroup, android.support.v4.view.NestedScrollingParent
    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        maybeUpdateScrollingContentView(target);
        this.mLastScrollWasFling = true;
        if (target == this.mScrollingContentView) {
            this.mFlingWatcher.start(this.mScrollingContentView);
            return false;
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        maybeUpdateScrollingContentView(target);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        boolean scrolledUp = dyConsumed < 0;
        boolean scrolledDown = dyConsumed > 0;
        boolean overScrolledUp = dyUnconsumed < 0;
        boolean overScrolledDown = dyUnconsumed > 0;
        if (this.mTopDrawerView != null && this.mTopDrawerView.isOpened()) {
            this.mCanTopDrawerBeClosed = overScrolledDown || !this.mTopDrawerView.getDrawerContent().canScrollVertically(1);
            if (this.mCanTopDrawerBeClosed && this.mLastScrollWasFling) {
                onTouchEvent(this.mDrawerOpenLastInterceptedTouchEvent);
            }
            this.mLastScrollWasFling = false;
            return;
        }
        if (this.mBottomDrawerView != null && this.mBottomDrawerView.isOpened()) {
            this.mCanBottomDrawerBeClosed = overScrolledUp;
            if (this.mCanBottomDrawerBeClosed && this.mLastScrollWasFling) {
                onTouchEvent(this.mDrawerOpenLastInterceptedTouchEvent);
            }
            this.mLastScrollWasFling = false;
            return;
        }
        this.mLastScrollWasFling = false;
        boolean canTopAutoPeek = this.mTopDrawerView != null && this.mTopDrawerView.canAutoPeek();
        boolean canBottomAutoPeek = this.mBottomDrawerView != null && this.mBottomDrawerView.canAutoPeek();
        boolean isTopDrawerPeeking = this.mTopDrawerView != null && this.mTopDrawerView.isPeeking();
        boolean isBottomDrawerPeeking = this.mBottomDrawerView != null && this.mBottomDrawerView.isPeeking();
        boolean scrolledDownPastSlop = false;
        boolean shouldPeekOnScrollDown = this.mBottomDrawerView != null && this.mBottomDrawerView.shouldPeekOnScrollDown();
        if (scrolledDown) {
            this.mCurrentNestedScrollSlopTracker += dyConsumed;
            scrolledDownPastSlop = this.mCurrentNestedScrollSlopTracker > this.mNestedScrollSlopPx;
        }
        if (canTopAutoPeek) {
            if (overScrolledUp && !isTopDrawerPeeking) {
                peekDrawer(48);
            } else if (scrolledDown && isTopDrawerPeeking && !isClosingPeek(this.mTopDrawerView)) {
                closeDrawer(48);
            }
        }
        if (canBottomAutoPeek) {
            if ((overScrolledDown || overScrolledUp) && !isBottomDrawerPeeking) {
                peekDrawer(80);
                return;
            }
            if (shouldPeekOnScrollDown && scrolledDownPastSlop && !isBottomDrawerPeeking) {
                peekDrawer(80);
                return;
            }
            if ((scrolledUp || (!shouldPeekOnScrollDown && scrolledDown)) && isBottomDrawerPeeking && !isClosingPeek(this.mBottomDrawerView)) {
                closeDrawer(this.mBottomDrawerView);
            }
        }
    }

    private void maybePeekDrawer(WearableDrawerView drawerView) {
        View peekView;
        if (drawerView != null && (peekView = drawerView.getPeekContainer()) != null) {
            View drawerContent = drawerView.getDrawerContent();
            int layoutGravity = ((FrameLayout.LayoutParams) drawerView.getLayoutParams()).gravity;
            int gravity = layoutGravity == 0 ? drawerView.preferGravity() : layoutGravity;
            drawerView.setIsPeeking(true);
            peekView.setAlpha(1.0f);
            peekView.setScaleX(1.0f);
            peekView.setScaleY(1.0f);
            peekView.setVisibility(0);
            if (drawerContent != null) {
                drawerContent.setAlpha(0.0f);
                drawerContent.setVisibility(8);
            }
            if (gravity == 80) {
                this.mBottomDrawerDragger.smoothSlideViewTo(drawerView, 0, getHeight() - peekView.getHeight());
            } else if (gravity == 48) {
                this.mTopDrawerDragger.smoothSlideViewTo(drawerView, 0, -(drawerView.getHeight() - peekView.getHeight()));
                if (!this.mIsAccessibilityEnabled) {
                    closeDrawerDelayed(gravity, 1000L);
                }
            }
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openDrawerWithoutAnimation(WearableDrawerView drawer) {
        int offset;
        if (drawer != null) {
            if (drawer == this.mTopDrawerView) {
                offset = this.mTopDrawerView.getHeight();
            } else if (drawer == this.mBottomDrawerView) {
                offset = -this.mBottomDrawerView.getHeight();
            } else {
                Log.w(TAG, "openDrawer(View) should be passed in the top or bottom drawer");
                return;
            }
            drawer.offsetTopAndBottom(offset);
            drawer.setOpenedPercent(1.0f);
            drawer.onDrawerOpened();
            if (this.mDrawerStateCallback != null) {
                this.mDrawerStateCallback.onDrawerOpened(drawer);
            }
            showDrawerContentMaybeAnimate(drawer);
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void animatePeekVisibleAfterBeingClosed(WearableDrawerView drawer) {
        final View content = drawer.getDrawerContent();
        if (content != null) {
            content.animate().setDuration(150L).alpha(0.0f).withEndAction(new Runnable() { // from class: android.support.wearable.view.drawer.WearableDrawerLayout.2
                @Override // java.lang.Runnable
                public void run() {
                    content.setVisibility(8);
                }
            }).start();
        }
        ViewGroup peek = drawer.getPeekContainer();
        peek.setVisibility(0);
        peek.animate().setStartDelay(150L).setDuration(150L).alpha(1.0f).scaleX(1.0f).scaleY(1.0f).start();
        drawer.setIsPeeking(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nullable
    public WearableDrawerView findDrawerWithGravity(int gravity) {
        switch (gravity) {
            case Bar.DEFAULT_HEIGHT /* 48 */:
                return this.mTopDrawerView;
            case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                return this.mBottomDrawerView;
            default:
                Log.w(TAG, new StringBuilder(35).append("Invalid drawer gravity: ").append(gravity).toString());
                return null;
        }
    }

    private void maybeUpdateScrollingContentView(View view) {
        if (view != this.mScrollingContentView && !isDrawerOrChildOfDrawer(view)) {
            this.mScrollingContentView = view;
        }
    }

    private boolean isDrawerOrChildOfDrawer(View view) {
        while (view != null && view != this) {
            if (view instanceof WearableDrawerView) {
                return true;
            }
            view = (View) view.getParent();
        }
        return false;
    }

    private boolean isClosingPeek(WearableDrawerView drawerView) {
        return drawerView != null && drawerView.getDrawerState() == 2;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        this.mCurrentNestedScrollSlopTracker = 0;
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public void onStopNestedScroll(View target) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(target);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canDrawerContentScrollVertically(@Nullable WearableDrawerView drawerView, int direction) {
        View drawerContent;
        if (drawerView == null || (drawerContent = drawerView.getDrawerContent()) == null) {
            return false;
        }
        return drawerContent.canScrollVertically(direction);
    }

    private abstract class DrawerDraggerCallback extends ViewDragHelper.Callback {
        public abstract WearableDrawerView getDrawerView();

        private DrawerDraggerCallback() {
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public boolean tryCaptureView(View child, int pointerId) {
            WearableDrawerView drawerView = getDrawerView();
            return child == drawerView && !drawerView.isLocked() && drawerView.hasDrawerContent();
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public int getViewVerticalDragRange(View child) {
            if (child == getDrawerView()) {
                return child.getHeight();
            }
            return 0;
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public void onViewCaptured(View capturedChild, int activePointerId) {
            WearableDrawerLayout.showDrawerContentMaybeAnimate((WearableDrawerView) capturedChild);
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public void onViewDragStateChanged(int state) {
            WearableDrawerView drawerView = getDrawerView();
            switch (state) {
                case 0:
                    boolean openedOrClosed = false;
                    if (drawerView.isOpened()) {
                        openedOrClosed = true;
                        drawerView.onDrawerOpened();
                        if (WearableDrawerLayout.this.mDrawerStateCallback != null) {
                            WearableDrawerLayout.this.mDrawerStateCallback.onDrawerOpened(drawerView);
                        }
                        WearableDrawerLayout.this.mCanTopDrawerBeClosed = !WearableDrawerLayout.this.canDrawerContentScrollVertically(WearableDrawerLayout.this.mTopDrawerView, 1);
                        WearableDrawerLayout.this.mCanBottomDrawerBeClosed = WearableDrawerLayout.this.canDrawerContentScrollVertically(WearableDrawerLayout.this.mBottomDrawerView, -1) ? false : true;
                    } else if (drawerView.isClosed()) {
                        openedOrClosed = true;
                        drawerView.onDrawerClosed();
                        if (WearableDrawerLayout.this.mDrawerStateCallback != null) {
                            WearableDrawerLayout.this.mDrawerStateCallback.onDrawerClosed(drawerView);
                        }
                    }
                    if (openedOrClosed && drawerView.isPeeking()) {
                        drawerView.setIsPeeking(false);
                        drawerView.getPeekContainer().setVisibility(4);
                    }
                    break;
            }
            if (drawerView.getDrawerState() != state) {
                drawerView.setDrawerState(state);
                drawerView.onDrawerStateChanged(state);
                if (WearableDrawerLayout.this.mDrawerStateCallback != null) {
                    WearableDrawerLayout.this.mDrawerStateCallback.onDrawerStateChanged(state);
                }
            }
        }
    }

    private class TopDrawerDraggerCallback extends DrawerDraggerCallback {
        private TopDrawerDraggerCallback() {
            super();
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (WearableDrawerLayout.this.mTopDrawerView != child) {
                return 0;
            }
            int peekHeight = WearableDrawerLayout.this.mTopDrawerView.getPeekContainer().getHeight();
            return Math.max(peekHeight - child.getHeight(), Math.min(top, 0));
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            if (WearableDrawerLayout.this.mTopDrawerView != null && edgeFlags == 4 && !WearableDrawerLayout.this.mTopDrawerView.isLocked()) {
                if ((WearableDrawerLayout.this.mBottomDrawerView == null || !WearableDrawerLayout.this.mBottomDrawerView.isOpened()) && WearableDrawerLayout.this.mTopDrawerView.hasDrawerContent()) {
                    boolean atTop = WearableDrawerLayout.this.mScrollingContentView == null || !WearableDrawerLayout.this.mScrollingContentView.canScrollVertically(-1);
                    if (!WearableDrawerLayout.this.mTopDrawerView.shouldOnlyOpenWhenAtTop() || atTop) {
                        WearableDrawerLayout.this.mTopDrawerDragger.captureChildView(WearableDrawerLayout.this.mTopDrawerView, pointerId);
                    }
                }
            }
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalTop;
            if (releasedChild == WearableDrawerLayout.this.mTopDrawerView) {
                float openedPercent = WearableDrawerLayout.this.mTopDrawerView.getOpenedPercent();
                if (yvel <= 0.0f && (yvel != 0.0f || openedPercent <= WearableDrawerLayout.OPENED_PERCENT_THRESHOLD)) {
                    WearableDrawerLayout.animatePeekVisibleAfterBeingClosed(WearableDrawerLayout.this.mTopDrawerView);
                    finalTop = WearableDrawerLayout.this.mTopDrawerView.getPeekContainer().getHeight() - releasedChild.getHeight();
                } else {
                    finalTop = 0;
                }
                WearableDrawerLayout.this.mTopDrawerDragger.settleCapturedViewAt(0, finalTop);
                WearableDrawerLayout.this.invalidate();
            }
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == WearableDrawerLayout.this.mTopDrawerView) {
                int height = changedView.getHeight();
                WearableDrawerLayout.this.mTopDrawerView.setOpenedPercent((top + height) / height);
                WearableDrawerLayout.this.invalidate();
            }
        }

        @Override // android.support.wearable.view.drawer.WearableDrawerLayout.DrawerDraggerCallback
        public WearableDrawerView getDrawerView() {
            return WearableDrawerLayout.this.mTopDrawerView;
        }
    }

    private class BottomDrawerDraggerCallback extends DrawerDraggerCallback {
        private BottomDrawerDraggerCallback() {
            super();
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (WearableDrawerLayout.this.mBottomDrawerView != child) {
                return 0;
            }
            int parentHeight = WearableDrawerLayout.this.getHeight();
            int peekHeight = WearableDrawerLayout.this.mBottomDrawerView.getPeekContainer().getHeight();
            return Math.max(parentHeight - child.getHeight(), Math.min(top, parentHeight - peekHeight));
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            if (WearableDrawerLayout.this.mBottomDrawerView != null && edgeFlags == 8 && !WearableDrawerLayout.this.mBottomDrawerView.isLocked()) {
                if ((WearableDrawerLayout.this.mTopDrawerView == null || !WearableDrawerLayout.this.mTopDrawerView.isOpened()) && WearableDrawerLayout.this.mBottomDrawerView.hasDrawerContent()) {
                    WearableDrawerLayout.this.mBottomDrawerDragger.captureChildView(WearableDrawerLayout.this.mBottomDrawerView, pointerId);
                }
            }
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalTop;
            if (releasedChild == WearableDrawerLayout.this.mBottomDrawerView) {
                int parentHeight = WearableDrawerLayout.this.getHeight();
                float openedPercent = WearableDrawerLayout.this.mBottomDrawerView.getOpenedPercent();
                if (yvel >= 0.0f && (yvel != 0.0f || openedPercent <= WearableDrawerLayout.OPENED_PERCENT_THRESHOLD)) {
                    WearableDrawerLayout.animatePeekVisibleAfterBeingClosed(WearableDrawerLayout.this.mBottomDrawerView);
                    finalTop = WearableDrawerLayout.this.getHeight() - WearableDrawerLayout.this.mBottomDrawerView.getPeekContainer().getHeight();
                } else {
                    finalTop = parentHeight - releasedChild.getHeight();
                }
                WearableDrawerLayout.this.mBottomDrawerDragger.settleCapturedViewAt(0, finalTop);
                WearableDrawerLayout.this.invalidate();
            }
        }

        @Override // android.support.wearable.view.drawer.ViewDragHelper.Callback
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == WearableDrawerLayout.this.mBottomDrawerView) {
                int height = changedView.getHeight();
                int parentHeight = WearableDrawerLayout.this.getHeight();
                WearableDrawerLayout.this.mBottomDrawerView.setOpenedPercent((parentHeight - top) / height);
                WearableDrawerLayout.this.invalidate();
            }
        }

        @Override // android.support.wearable.view.drawer.WearableDrawerLayout.DrawerDraggerCallback
        public WearableDrawerView getDrawerView() {
            return WearableDrawerLayout.this.mBottomDrawerView;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showDrawerContentMaybeAnimate(WearableDrawerView drawerView) {
        drawerView.bringToFront();
        View contentView = drawerView.getDrawerContent();
        if (contentView != null) {
            contentView.setVisibility(0);
        }
        if (drawerView.isPeeking()) {
            View peekView = drawerView.getPeekContainer();
            peekView.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setDuration(150L).start();
            if (contentView != null) {
                contentView.setAlpha(0.0f);
                contentView.animate().setStartDelay(150L).alpha(1.0f).setDuration(150L).start();
                return;
            }
            return;
        }
        drawerView.getPeekContainer().setAlpha(0.0f);
        if (contentView != null) {
            contentView.setAlpha(1.0f);
        }
    }

    private class ClosePeekRunnable implements Runnable {
        private final int gravity;

        private ClosePeekRunnable(int gravity) {
            this.gravity = gravity;
        }

        @Override // java.lang.Runnable
        public void run() {
            WearableDrawerView drawer = WearableDrawerLayout.this.findDrawerWithGravity(this.gravity);
            if (drawer != null && !drawer.isOpened() && drawer.getDrawerState() == 0) {
                WearableDrawerLayout.this.closeDrawer(this.gravity);
            }
        }
    }
}
