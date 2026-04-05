package android.support.v4.widget;

import android.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import com.nuance.android.vocalizer.VocalizerEngine;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes63.dex */
public class DrawerLayout extends ViewGroup {
    private static final boolean ALLOW_EDGE_LOCK = false;
    static final boolean CAN_HIDE_DESCENDANTS;
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final int DRAWER_ELEVATION = 10;
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNDEFINED = 3;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    private final ChildAccessibilityDelegate mChildAccessibilityDelegate;
    private boolean mChildrenCanceledTouch;
    private boolean mDisallowInterceptRequested;
    private boolean mDrawStatusBarBackground;
    private float mDrawerElevation;
    private int mDrawerState;
    private boolean mFirstLayout;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private Object mLastInsets;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;

    @Nullable
    private DrawerListener mListener;
    private List<DrawerListener> mListeners;
    private int mLockModeEnd;
    private int mLockModeLeft;
    private int mLockModeRight;
    private int mLockModeStart;
    private int mMinDrawerMargin;
    private final ArrayList<View> mNonDrawerViews;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor;
    private float mScrimOpacity;
    private Paint mScrimPaint;
    private Drawable mShadowEnd;
    private Drawable mShadowLeft;
    private Drawable mShadowLeftResolved;
    private Drawable mShadowRight;
    private Drawable mShadowRightResolved;
    private Drawable mShadowStart;
    private Drawable mStatusBarBackground;
    private CharSequence mTitleLeft;
    private CharSequence mTitleRight;
    private static final int[] THEME_ATTRS = {R.attr.colorPrimaryDark};
    static final int[] LAYOUT_ATTRS = {R.attr.layout_gravity};

    public interface DrawerListener {
        void onDrawerClosed(View view);

        void onDrawerOpened(View view);

        void onDrawerSlide(View view, float f);

        void onDrawerStateChanged(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface EdgeGravity {
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface LockMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface State {
    }

    static {
        CAN_HIDE_DESCENDANTS = Build.VERSION.SDK_INT >= 19;
        SET_DRAWER_SHADOW_FROM_ELEVATION = Build.VERSION.SDK_INT >= 21;
    }

    public static abstract class SimpleDrawerListener implements DrawerListener {
        @Override // android.support.v4.widget.DrawerLayout.DrawerListener
        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        @Override // android.support.v4.widget.DrawerLayout.DrawerListener
        public void onDrawerOpened(View drawerView) {
        }

        @Override // android.support.v4.widget.DrawerLayout.DrawerListener
        public void onDrawerClosed(View drawerView) {
        }

        @Override // android.support.v4.widget.DrawerLayout.DrawerListener
        public void onDrawerStateChanged(int newState) {
        }
    }

    public DrawerLayout(Context context) {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
        this.mScrimColor = DEFAULT_SCRIM_COLOR;
        this.mScrimPaint = new Paint();
        this.mFirstLayout = true;
        this.mLockModeLeft = 3;
        this.mLockModeRight = 3;
        this.mLockModeStart = 3;
        this.mLockModeEnd = 3;
        this.mShadowStart = null;
        this.mShadowEnd = null;
        this.mShadowLeft = null;
        this.mShadowRight = null;
        setDescendantFocusability(262144);
        float density = getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int) ((64.0f * density) + 0.5f);
        float minVel = 400.0f * density;
        this.mLeftCallback = new ViewDragCallback(3);
        this.mRightCallback = new ViewDragCallback(5);
        this.mLeftDragger = ViewDragHelper.create(this, 1.0f, this.mLeftCallback);
        this.mLeftDragger.setEdgeTrackingEnabled(1);
        this.mLeftDragger.setMinVelocity(minVel);
        this.mLeftCallback.setDragger(this.mLeftDragger);
        this.mRightDragger = ViewDragHelper.create(this, 1.0f, this.mRightCallback);
        this.mRightDragger.setEdgeTrackingEnabled(2);
        this.mRightDragger.setMinVelocity(minVel);
        this.mRightCallback.setDragger(this.mRightDragger);
        setFocusableInTouchMode(true);
        ViewCompat.setImportantForAccessibility(this, 1);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
        if (ViewCompat.getFitsSystemWindows(this)) {
            if (Build.VERSION.SDK_INT >= 21) {
                setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: android.support.v4.widget.DrawerLayout.1
                    @Override // android.view.View.OnApplyWindowInsetsListener
                    @TargetApi(21)
                    public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                        DrawerLayout drawerLayout = (DrawerLayout) view;
                        drawerLayout.setChildInsets(insets, insets.getSystemWindowInsetTop() > 0);
                        return insets.consumeSystemWindowInsets();
                    }
                });
                setSystemUiVisibility(1280);
                TypedArray a = context.obtainStyledAttributes(THEME_ATTRS);
                try {
                    this.mStatusBarBackground = a.getDrawable(0);
                } finally {
                    a.recycle();
                }
            } else {
                this.mStatusBarBackground = null;
            }
        }
        this.mDrawerElevation = 10.0f * density;
        this.mNonDrawerViews = new ArrayList<>();
    }

    public void setDrawerElevation(float elevation) {
        this.mDrawerElevation = elevation;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (isDrawerView(child)) {
                ViewCompat.setElevation(child, this.mDrawerElevation);
            }
        }
    }

    public float getDrawerElevation() {
        if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return this.mDrawerElevation;
        }
        return 0.0f;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void setChildInsets(Object insets, boolean draw) {
        this.mLastInsets = insets;
        this.mDrawStatusBarBackground = draw;
        setWillNotDraw(!draw && getBackground() == null);
        requestLayout();
    }

    public void setDrawerShadow(Drawable shadowDrawable, int gravity) {
        if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
            if ((gravity & GravityCompat.START) == 8388611) {
                this.mShadowStart = shadowDrawable;
            } else if ((gravity & GravityCompat.END) == 8388613) {
                this.mShadowEnd = shadowDrawable;
            } else if ((gravity & 3) == 3) {
                this.mShadowLeft = shadowDrawable;
            } else if ((gravity & 5) == 5) {
                this.mShadowRight = shadowDrawable;
            } else {
                return;
            }
            resolveShadowDrawables();
            invalidate();
        }
    }

    public void setDrawerShadow(@DrawableRes int resId, int gravity) {
        setDrawerShadow(ContextCompat.getDrawable(getContext(), resId), gravity);
    }

    public void setScrimColor(@ColorInt int color) {
        this.mScrimColor = color;
        invalidate();
    }

    @Deprecated
    public void setDrawerListener(DrawerListener listener) {
        if (this.mListener != null) {
            removeDrawerListener(this.mListener);
        }
        if (listener != null) {
            addDrawerListener(listener);
        }
        this.mListener = listener;
    }

    public void addDrawerListener(@NonNull DrawerListener listener) {
        if (listener != null) {
            if (this.mListeners == null) {
                this.mListeners = new ArrayList();
            }
            this.mListeners.add(listener);
        }
    }

    public void removeDrawerListener(@NonNull DrawerListener listener) {
        if (listener != null && this.mListeners != null) {
            this.mListeners.remove(listener);
        }
    }

    public void setDrawerLockMode(int lockMode) {
        setDrawerLockMode(lockMode, 3);
        setDrawerLockMode(lockMode, 5);
    }

    public void setDrawerLockMode(int lockMode, int edgeGravity) {
        int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        switch (edgeGravity) {
            case 3:
                this.mLockModeLeft = lockMode;
                break;
            case 5:
                this.mLockModeRight = lockMode;
                break;
            case GravityCompat.START /* 8388611 */:
                this.mLockModeStart = lockMode;
                break;
            case GravityCompat.END /* 8388613 */:
                this.mLockModeEnd = lockMode;
                break;
        }
        if (lockMode != 0) {
            ViewDragHelper helper = absGravity == 3 ? this.mLeftDragger : this.mRightDragger;
            helper.cancel();
        }
        switch (lockMode) {
            case 1:
                View toClose = findDrawerWithGravity(absGravity);
                if (toClose != null) {
                    closeDrawer(toClose);
                }
                break;
            case 2:
                View toOpen = findDrawerWithGravity(absGravity);
                if (toOpen != null) {
                    openDrawer(toOpen);
                }
                break;
        }
    }

    public void setDrawerLockMode(int lockMode, View drawerView) {
        if (!isDrawerView(drawerView)) {
            throw new IllegalArgumentException("View " + drawerView + " is not a drawer with appropriate layout_gravity");
        }
        int gravity = ((LayoutParams) drawerView.getLayoutParams()).gravity;
        setDrawerLockMode(lockMode, gravity);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:4:0x0008 A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int getDrawerLockMode(int r8) {
        /*
            r7 = this;
            r6 = 3
            int r1 = android.support.v4.view.ViewCompat.getLayoutDirection(r7)
            switch(r8) {
                case 3: goto La;
                case 5: goto L1b;
                case 8388611: goto L2d;
                case 8388613: goto L3f;
                default: goto L8;
            }
        L8:
            r2 = 0
        L9:
            return r2
        La:
            int r5 = r7.mLockModeLeft
            if (r5 == r6) goto L11
            int r2 = r7.mLockModeLeft
            goto L9
        L11:
            if (r1 != 0) goto L18
            int r2 = r7.mLockModeStart
        L15:
            if (r2 == r6) goto L8
            goto L9
        L18:
            int r2 = r7.mLockModeEnd
            goto L15
        L1b:
            int r5 = r7.mLockModeRight
            if (r5 == r6) goto L22
            int r2 = r7.mLockModeRight
            goto L9
        L22:
            if (r1 != 0) goto L2a
            int r3 = r7.mLockModeEnd
        L26:
            if (r3 == r6) goto L8
            r2 = r3
            goto L9
        L2a:
            int r3 = r7.mLockModeStart
            goto L26
        L2d:
            int r5 = r7.mLockModeStart
            if (r5 == r6) goto L34
            int r2 = r7.mLockModeStart
            goto L9
        L34:
            if (r1 != 0) goto L3c
            int r4 = r7.mLockModeLeft
        L38:
            if (r4 == r6) goto L8
            r2 = r4
            goto L9
        L3c:
            int r4 = r7.mLockModeRight
            goto L38
        L3f:
            int r5 = r7.mLockModeEnd
            if (r5 == r6) goto L46
            int r2 = r7.mLockModeEnd
            goto L9
        L46:
            if (r1 != 0) goto L4e
            int r0 = r7.mLockModeRight
        L4a:
            if (r0 == r6) goto L8
            r2 = r0
            goto L9
        L4e:
            int r0 = r7.mLockModeLeft
            goto L4a
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.DrawerLayout.getDrawerLockMode(int):int");
    }

    public int getDrawerLockMode(View drawerView) {
        if (!isDrawerView(drawerView)) {
            throw new IllegalArgumentException("View " + drawerView + " is not a drawer");
        }
        int drawerGravity = ((LayoutParams) drawerView.getLayoutParams()).gravity;
        return getDrawerLockMode(drawerGravity);
    }

    public void setDrawerTitle(int edgeGravity, CharSequence title) {
        int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (absGravity == 3) {
            this.mTitleLeft = title;
        } else if (absGravity == 5) {
            this.mTitleRight = title;
        }
    }

    @Nullable
    public CharSequence getDrawerTitle(int edgeGravity) {
        int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (absGravity == 3) {
            return this.mTitleLeft;
        }
        if (absGravity == 5) {
            return this.mTitleRight;
        }
        return null;
    }

    void updateDrawerState(int forGravity, int activeState, View activeDrawer) {
        int state;
        int leftState = this.mLeftDragger.getViewDragState();
        int rightState = this.mRightDragger.getViewDragState();
        if (leftState == 1 || rightState == 1) {
            state = 1;
        } else if (leftState == 2 || rightState == 2) {
            state = 2;
        } else {
            state = 0;
        }
        if (activeDrawer != null && activeState == 0) {
            LayoutParams lp = (LayoutParams) activeDrawer.getLayoutParams();
            if (lp.onScreen == 0.0f) {
                dispatchOnDrawerClosed(activeDrawer);
            } else if (lp.onScreen == 1.0f) {
                dispatchOnDrawerOpened(activeDrawer);
            }
        }
        if (state != this.mDrawerState) {
            this.mDrawerState = state;
            if (this.mListeners != null) {
                int listenerCount = this.mListeners.size();
                for (int i = listenerCount - 1; i >= 0; i--) {
                    this.mListeners.get(i).onDrawerStateChanged(state);
                }
            }
        }
    }

    void dispatchOnDrawerClosed(View drawerView) {
        View rootView;
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if ((lp.openState & 1) == 1) {
            lp.openState = 0;
            if (this.mListeners != null) {
                int listenerCount = this.mListeners.size();
                for (int i = listenerCount - 1; i >= 0; i--) {
                    this.mListeners.get(i).onDrawerClosed(drawerView);
                }
            }
            updateChildrenImportantForAccessibility(drawerView, false);
            if (hasWindowFocus() && (rootView = getRootView()) != null) {
                rootView.sendAccessibilityEvent(32);
            }
        }
    }

    void dispatchOnDrawerOpened(View drawerView) {
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if ((lp.openState & 1) == 0) {
            lp.openState = 1;
            if (this.mListeners != null) {
                int listenerCount = this.mListeners.size();
                for (int i = listenerCount - 1; i >= 0; i--) {
                    this.mListeners.get(i).onDrawerOpened(drawerView);
                }
            }
            updateChildrenImportantForAccessibility(drawerView, true);
            if (hasWindowFocus()) {
                sendAccessibilityEvent(32);
            }
        }
    }

    private void updateChildrenImportantForAccessibility(View drawerView, boolean isDrawerOpen) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if ((!isDrawerOpen && !isDrawerView(child)) || (isDrawerOpen && child == drawerView)) {
                ViewCompat.setImportantForAccessibility(child, 1);
            } else {
                ViewCompat.setImportantForAccessibility(child, 4);
            }
        }
    }

    void dispatchOnDrawerSlide(View drawerView, float slideOffset) {
        if (this.mListeners != null) {
            int listenerCount = this.mListeners.size();
            for (int i = listenerCount - 1; i >= 0; i--) {
                this.mListeners.get(i).onDrawerSlide(drawerView, slideOffset);
            }
        }
    }

    void setDrawerViewOffset(View drawerView, float slideOffset) {
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if (slideOffset != lp.onScreen) {
            lp.onScreen = slideOffset;
            dispatchOnDrawerSlide(drawerView, slideOffset);
        }
    }

    float getDrawerViewOffset(View drawerView) {
        return ((LayoutParams) drawerView.getLayoutParams()).onScreen;
    }

    int getDrawerViewAbsoluteGravity(View drawerView) {
        int gravity = ((LayoutParams) drawerView.getLayoutParams()).gravity;
        return GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this));
    }

    boolean checkDrawerViewAbsoluteGravity(View drawerView, int checkFor) {
        int absGravity = getDrawerViewAbsoluteGravity(drawerView);
        return (absGravity & checkFor) == checkFor;
    }

    View findOpenDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams childLp = (LayoutParams) child.getLayoutParams();
            if ((childLp.openState & 1) == 1) {
                return child;
            }
        }
        return null;
    }

    void moveDrawerToOffset(View drawerView, float slideOffset) {
        float oldOffset = getDrawerViewOffset(drawerView);
        int width = drawerView.getWidth();
        int oldPos = (int) (width * oldOffset);
        int newPos = (int) (width * slideOffset);
        int dx = newPos - oldPos;
        if (!checkDrawerViewAbsoluteGravity(drawerView, 3)) {
            dx = -dx;
        }
        drawerView.offsetLeftAndRight(dx);
        setDrawerViewOffset(drawerView, slideOffset);
    }

    View findDrawerWithGravity(int gravity) {
        int absHorizGravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this)) & 7;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int childAbsGravity = getDrawerViewAbsoluteGravity(child);
            if ((childAbsGravity & 7) == absHorizGravity) {
                return child;
            }
        }
        return null;
    }

    static String gravityToString(int gravity) {
        if ((gravity & 3) == 3) {
            return "LEFT";
        }
        if ((gravity & 5) == 5) {
            return "RIGHT";
        }
        return Integer.toHexString(gravity);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != 1073741824 || heightMode != 1073741824) {
            if (isInEditMode()) {
                if (widthMode != Integer.MIN_VALUE && widthMode == 0) {
                    widthSize = 300;
                }
                if (heightMode != Integer.MIN_VALUE && heightMode == 0) {
                    heightSize = 300;
                }
            } else {
                throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            }
        }
        setMeasuredDimension(widthSize, heightSize);
        boolean applyInsets = this.mLastInsets != null && ViewCompat.getFitsSystemWindows(this);
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        boolean hasDrawerOnLeftEdge = false;
        boolean hasDrawerOnRightEdge = false;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (applyInsets) {
                    int cgrav = GravityCompat.getAbsoluteGravity(lp.gravity, layoutDirection);
                    if (ViewCompat.getFitsSystemWindows(child)) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            WindowInsets wi = (WindowInsets) this.mLastInsets;
                            if (cgrav == 3) {
                                wi = wi.replaceSystemWindowInsets(wi.getSystemWindowInsetLeft(), wi.getSystemWindowInsetTop(), 0, wi.getSystemWindowInsetBottom());
                            } else if (cgrav == 5) {
                                wi = wi.replaceSystemWindowInsets(0, wi.getSystemWindowInsetTop(), wi.getSystemWindowInsetRight(), wi.getSystemWindowInsetBottom());
                            }
                            child.dispatchApplyWindowInsets(wi);
                        }
                    } else if (Build.VERSION.SDK_INT >= 21) {
                        WindowInsets wi2 = (WindowInsets) this.mLastInsets;
                        if (cgrav == 3) {
                            wi2 = wi2.replaceSystemWindowInsets(wi2.getSystemWindowInsetLeft(), wi2.getSystemWindowInsetTop(), 0, wi2.getSystemWindowInsetBottom());
                        } else if (cgrav == 5) {
                            wi2 = wi2.replaceSystemWindowInsets(0, wi2.getSystemWindowInsetTop(), wi2.getSystemWindowInsetRight(), wi2.getSystemWindowInsetBottom());
                        }
                        lp.leftMargin = wi2.getSystemWindowInsetLeft();
                        lp.topMargin = wi2.getSystemWindowInsetTop();
                        lp.rightMargin = wi2.getSystemWindowInsetRight();
                        lp.bottomMargin = wi2.getSystemWindowInsetBottom();
                    }
                }
                if (isContentView(child)) {
                    int contentWidthSpec = View.MeasureSpec.makeMeasureSpec((widthSize - lp.leftMargin) - lp.rightMargin, 1073741824);
                    int contentHeightSpec = View.MeasureSpec.makeMeasureSpec((heightSize - lp.topMargin) - lp.bottomMargin, 1073741824);
                    child.measure(contentWidthSpec, contentHeightSpec);
                } else if (isDrawerView(child)) {
                    if (SET_DRAWER_SHADOW_FROM_ELEVATION && ViewCompat.getElevation(child) != this.mDrawerElevation) {
                        ViewCompat.setElevation(child, this.mDrawerElevation);
                    }
                    int childGravity = getDrawerViewAbsoluteGravity(child) & 7;
                    boolean isLeftEdgeDrawer = childGravity == 3;
                    if ((isLeftEdgeDrawer && hasDrawerOnLeftEdge) || (!isLeftEdgeDrawer && hasDrawerOnRightEdge)) {
                        throw new IllegalStateException("Child drawer has absolute gravity " + gravityToString(childGravity) + " but this " + TAG + " already has a drawer view along that edge");
                    }
                    if (isLeftEdgeDrawer) {
                        hasDrawerOnLeftEdge = true;
                    } else {
                        hasDrawerOnRightEdge = true;
                    }
                    int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec, this.mMinDrawerMargin + lp.leftMargin + lp.rightMargin, lp.width);
                    int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height);
                    child.measure(drawerWidthSpec, drawerHeightSpec);
                } else {
                    throw new IllegalStateException("Child " + child + " at index " + i + " does not have a valid layout_gravity - must be Gravity.LEFT, Gravity.RIGHT or Gravity.NO_GRAVITY");
                }
            }
        }
    }

    private void resolveShadowDrawables() {
        if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
            this.mShadowLeftResolved = resolveLeftShadow();
            this.mShadowRightResolved = resolveRightShadow();
        }
    }

    private Drawable resolveLeftShadow() {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        if (layoutDirection == 0) {
            if (this.mShadowStart != null) {
                mirror(this.mShadowStart, layoutDirection);
                return this.mShadowStart;
            }
        } else if (this.mShadowEnd != null) {
            mirror(this.mShadowEnd, layoutDirection);
            return this.mShadowEnd;
        }
        return this.mShadowLeft;
    }

    private Drawable resolveRightShadow() {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        if (layoutDirection == 0) {
            if (this.mShadowEnd != null) {
                mirror(this.mShadowEnd, layoutDirection);
                return this.mShadowEnd;
            }
        } else if (this.mShadowStart != null) {
            mirror(this.mShadowStart, layoutDirection);
            return this.mShadowStart;
        }
        return this.mShadowRight;
    }

    private boolean mirror(Drawable drawable, int layoutDirection) {
        if (drawable == null || !DrawableCompat.isAutoMirrored(drawable)) {
            return false;
        }
        DrawableCompat.setLayoutDirection(drawable, layoutDirection);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft;
        float newOffset;
        this.mInLayout = true;
        int width = r - l;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (isContentView(child)) {
                    child.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + child.getMeasuredWidth(), lp.topMargin + child.getMeasuredHeight());
                } else {
                    int childWidth = child.getMeasuredWidth();
                    int childHeight = child.getMeasuredHeight();
                    if (checkDrawerViewAbsoluteGravity(child, 3)) {
                        childLeft = (-childWidth) + ((int) (childWidth * lp.onScreen));
                        newOffset = (childWidth + childLeft) / childWidth;
                    } else {
                        childLeft = width - ((int) (childWidth * lp.onScreen));
                        newOffset = (width - childLeft) / childWidth;
                    }
                    boolean changeOffset = newOffset != lp.onScreen;
                    int vgrav = lp.gravity & 112;
                    switch (vgrav) {
                        case 16:
                            int height = b - t;
                            int childTop = (height - childHeight) / 2;
                            if (childTop < lp.topMargin) {
                                childTop = lp.topMargin;
                            } else if (childTop + childHeight > height - lp.bottomMargin) {
                                childTop = (height - lp.bottomMargin) - childHeight;
                            }
                            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                            break;
                        case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                            int height2 = b - t;
                            child.layout(childLeft, (height2 - lp.bottomMargin) - child.getMeasuredHeight(), childLeft + childWidth, height2 - lp.bottomMargin);
                            break;
                        default:
                            child.layout(childLeft, lp.topMargin, childLeft + childWidth, lp.topMargin + childHeight);
                            break;
                    }
                    if (changeOffset) {
                        setDrawerViewOffset(child, newOffset);
                    }
                    int newVisibility = lp.onScreen > 0.0f ? 0 : 4;
                    if (child.getVisibility() != newVisibility) {
                        child.setVisibility(newVisibility);
                    }
                }
            }
        }
        this.mInLayout = false;
        this.mFirstLayout = false;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    @Override // android.view.View
    public void computeScroll() {
        int childCount = getChildCount();
        float scrimOpacity = 0.0f;
        for (int i = 0; i < childCount; i++) {
            float onscreen = ((LayoutParams) getChildAt(i).getLayoutParams()).onScreen;
            scrimOpacity = Math.max(scrimOpacity, onscreen);
        }
        this.mScrimOpacity = scrimOpacity;
        boolean leftDraggerSettling = this.mLeftDragger.continueSettling(true);
        boolean rightDraggerSettling = this.mRightDragger.continueSettling(true);
        if (leftDraggerSettling || rightDraggerSettling) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private static boolean hasOpaqueBackground(View v) {
        Drawable bg = v.getBackground();
        return bg != null && bg.getOpacity() == -1;
    }

    public void setStatusBarBackground(Drawable bg) {
        this.mStatusBarBackground = bg;
        invalidate();
    }

    public Drawable getStatusBarBackgroundDrawable() {
        return this.mStatusBarBackground;
    }

    public void setStatusBarBackground(int resId) {
        this.mStatusBarBackground = resId != 0 ? ContextCompat.getDrawable(getContext(), resId) : null;
        invalidate();
    }

    public void setStatusBarBackgroundColor(@ColorInt int color) {
        this.mStatusBarBackground = new ColorDrawable(color);
        invalidate();
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int layoutDirection) {
        resolveShadowDrawables();
    }

    @Override // android.view.View
    public void onDraw(Canvas c) {
        int inset;
        super.onDraw(c);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                inset = this.mLastInsets != null ? ((WindowInsets) this.mLastInsets).getSystemWindowInsetTop() : 0;
            } else {
                inset = 0;
            }
            if (inset > 0) {
                this.mStatusBarBackground.setBounds(0, 0, getWidth(), inset);
                this.mStatusBarBackground.draw(c);
            }
        }
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int height = getHeight();
        boolean drawingContent = isContentView(child);
        int clipLeft = 0;
        int clipRight = getWidth();
        int restoreCount = canvas.save();
        if (drawingContent) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                if (v != child && v.getVisibility() == 0 && hasOpaqueBackground(v) && isDrawerView(v) && v.getHeight() >= height) {
                    if (checkDrawerViewAbsoluteGravity(v, 3)) {
                        int vright = v.getRight();
                        if (vright > clipLeft) {
                            clipLeft = vright;
                        }
                    } else {
                        int vleft = v.getLeft();
                        if (vleft < clipRight) {
                            clipRight = vleft;
                        }
                    }
                }
            }
            canvas.clipRect(clipLeft, 0, clipRight, getHeight());
        }
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(restoreCount);
        if (this.mScrimOpacity > 0.0f && drawingContent) {
            int baseAlpha = (this.mScrimColor & ViewCompat.MEASURED_STATE_MASK) >>> 24;
            int imag = (int) (baseAlpha * this.mScrimOpacity);
            int color = (imag << 24) | (this.mScrimColor & ViewCompat.MEASURED_SIZE_MASK);
            this.mScrimPaint.setColor(color);
            canvas.drawRect(clipLeft, 0.0f, clipRight, getHeight(), this.mScrimPaint);
        } else if (this.mShadowLeftResolved != null && checkDrawerViewAbsoluteGravity(child, 3)) {
            int shadowWidth = this.mShadowLeftResolved.getIntrinsicWidth();
            int childRight = child.getRight();
            int drawerPeekDistance = this.mLeftDragger.getEdgeSize();
            float alpha = Math.max(0.0f, Math.min(childRight / drawerPeekDistance, 1.0f));
            this.mShadowLeftResolved.setBounds(childRight, child.getTop(), childRight + shadowWidth, child.getBottom());
            this.mShadowLeftResolved.setAlpha((int) (255.0f * alpha));
            this.mShadowLeftResolved.draw(canvas);
        } else if (this.mShadowRightResolved != null && checkDrawerViewAbsoluteGravity(child, 5)) {
            int shadowWidth2 = this.mShadowRightResolved.getIntrinsicWidth();
            int childLeft = child.getLeft();
            int showing = getWidth() - childLeft;
            int drawerPeekDistance2 = this.mRightDragger.getEdgeSize();
            float alpha2 = Math.max(0.0f, Math.min(showing / drawerPeekDistance2, 1.0f));
            this.mShadowRightResolved.setBounds(childLeft - shadowWidth2, child.getTop(), childLeft, child.getBottom());
            this.mShadowRightResolved.setAlpha((int) (255.0f * alpha2));
            this.mShadowRightResolved.draw(canvas);
        }
        return result;
    }

    boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == 0;
    }

    boolean isDrawerView(View child) {
        int gravity = ((LayoutParams) child.getLayoutParams()).gravity;
        int absGravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(child));
        return ((absGravity & 3) == 0 && (absGravity & 5) == 0) ? false : true;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        View child;
        int action = ev.getActionMasked();
        boolean interceptForDrag = this.mLeftDragger.shouldInterceptTouchEvent(ev) | this.mRightDragger.shouldInterceptTouchEvent(ev);
        boolean interceptForTap = false;
        switch (action) {
            case 0:
                float x = ev.getX();
                float y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                if (this.mScrimOpacity > 0.0f && (child = this.mLeftDragger.findTopChildUnder((int) x, (int) y)) != null && isContentView(child)) {
                    interceptForTap = true;
                }
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
            case 1:
            case 3:
                closeDrawers(true);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
            case 2:
                if (this.mLeftDragger.checkTouchSlop(3)) {
                    this.mLeftCallback.removeCallbacks();
                    this.mRightCallback.removeCallbacks();
                }
                break;
        }
        return interceptForDrag || interceptForTap || hasPeekingDrawer() || this.mChildrenCanceledTouch;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        View openDrawer;
        this.mLeftDragger.processTouchEvent(ev);
        this.mRightDragger.processTouchEvent(ev);
        int action = ev.getAction();
        switch (action & 255) {
            case 0:
                float x = ev.getX();
                float y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                return true;
            case 1:
                float x2 = ev.getX();
                float y2 = ev.getY();
                boolean peekingOnly = true;
                View touchedView = this.mLeftDragger.findTopChildUnder((int) x2, (int) y2);
                if (touchedView != null && isContentView(touchedView)) {
                    float dx = x2 - this.mInitialMotionX;
                    float dy = y2 - this.mInitialMotionY;
                    int slop = this.mLeftDragger.getTouchSlop();
                    if ((dx * dx) + (dy * dy) < slop * slop && (openDrawer = findOpenDrawer()) != null) {
                        peekingOnly = getDrawerLockMode(openDrawer) == 2;
                    }
                }
                closeDrawers(peekingOnly);
                this.mDisallowInterceptRequested = false;
                return true;
            case 2:
            default:
                return true;
            case 3:
                closeDrawers(true);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                return true;
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        this.mDisallowInterceptRequested = disallowIntercept;
        if (disallowIntercept) {
            closeDrawers(true);
        }
    }

    public void closeDrawers() {
        closeDrawers(false);
    }

    void closeDrawers(boolean peekingOnly) {
        boolean needsInvalidate = false;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (isDrawerView(child) && (!peekingOnly || lp.isPeeking)) {
                int childWidth = child.getWidth();
                if (checkDrawerViewAbsoluteGravity(child, 3)) {
                    needsInvalidate |= this.mLeftDragger.smoothSlideViewTo(child, -childWidth, child.getTop());
                } else {
                    needsInvalidate |= this.mRightDragger.smoothSlideViewTo(child, getWidth(), child.getTop());
                }
                lp.isPeeking = false;
            }
        }
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        if (needsInvalidate) {
            invalidate();
        }
    }

    public void openDrawer(View drawerView) {
        openDrawer(drawerView, true);
    }

    public void openDrawer(View drawerView, boolean animate) {
        if (!isDrawerView(drawerView)) {
            throw new IllegalArgumentException("View " + drawerView + " is not a sliding drawer");
        }
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if (this.mFirstLayout) {
            lp.onScreen = 1.0f;
            lp.openState = 1;
            updateChildrenImportantForAccessibility(drawerView, true);
        } else if (animate) {
            lp.openState |= 2;
            if (checkDrawerViewAbsoluteGravity(drawerView, 3)) {
                this.mLeftDragger.smoothSlideViewTo(drawerView, 0, drawerView.getTop());
            } else {
                this.mRightDragger.smoothSlideViewTo(drawerView, getWidth() - drawerView.getWidth(), drawerView.getTop());
            }
        } else {
            moveDrawerToOffset(drawerView, 1.0f);
            updateDrawerState(lp.gravity, 0, drawerView);
            drawerView.setVisibility(0);
        }
        invalidate();
    }

    public void openDrawer(int gravity) {
        openDrawer(gravity, true);
    }

    public void openDrawer(int gravity, boolean animate) {
        View drawerView = findDrawerWithGravity(gravity);
        if (drawerView == null) {
            throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(gravity));
        }
        openDrawer(drawerView, animate);
    }

    public void closeDrawer(View drawerView) {
        closeDrawer(drawerView, true);
    }

    public void closeDrawer(View drawerView, boolean animate) {
        if (!isDrawerView(drawerView)) {
            throw new IllegalArgumentException("View " + drawerView + " is not a sliding drawer");
        }
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if (this.mFirstLayout) {
            lp.onScreen = 0.0f;
            lp.openState = 0;
        } else if (animate) {
            lp.openState |= 4;
            if (checkDrawerViewAbsoluteGravity(drawerView, 3)) {
                this.mLeftDragger.smoothSlideViewTo(drawerView, -drawerView.getWidth(), drawerView.getTop());
            } else {
                this.mRightDragger.smoothSlideViewTo(drawerView, getWidth(), drawerView.getTop());
            }
        } else {
            moveDrawerToOffset(drawerView, 0.0f);
            updateDrawerState(lp.gravity, 0, drawerView);
            drawerView.setVisibility(4);
        }
        invalidate();
    }

    public void closeDrawer(int gravity) {
        closeDrawer(gravity, true);
    }

    public void closeDrawer(int gravity, boolean animate) {
        View drawerView = findDrawerWithGravity(gravity);
        if (drawerView == null) {
            throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(gravity));
        }
        closeDrawer(drawerView, animate);
    }

    public boolean isDrawerOpen(View drawer) {
        if (!isDrawerView(drawer)) {
            throw new IllegalArgumentException("View " + drawer + " is not a drawer");
        }
        LayoutParams drawerLp = (LayoutParams) drawer.getLayoutParams();
        return (drawerLp.openState & 1) == 1;
    }

    public boolean isDrawerOpen(int drawerGravity) {
        View drawerView = findDrawerWithGravity(drawerGravity);
        if (drawerView != null) {
            return isDrawerOpen(drawerView);
        }
        return false;
    }

    public boolean isDrawerVisible(View drawer) {
        if (isDrawerView(drawer)) {
            return ((LayoutParams) drawer.getLayoutParams()).onScreen > 0.0f;
        }
        throw new IllegalArgumentException("View " + drawer + " is not a drawer");
    }

    public boolean isDrawerVisible(int drawerGravity) {
        View drawerView = findDrawerWithGravity(drawerGravity);
        if (drawerView != null) {
            return isDrawerVisible(drawerView);
        }
        return false;
    }

    private boolean hasPeekingDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
            if (lp.isPeeking) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams ? new LayoutParams((LayoutParams) p) : p instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) p) : new LayoutParams(p);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (getDescendantFocusability() != 393216) {
            int childCount = getChildCount();
            boolean isDrawerOpen = false;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (isDrawerView(child)) {
                    if (isDrawerOpen(child)) {
                        isDrawerOpen = true;
                        child.addFocusables(views, direction, focusableMode);
                    }
                } else {
                    this.mNonDrawerViews.add(child);
                }
            }
            if (!isDrawerOpen) {
                int nonDrawerViewsCount = this.mNonDrawerViews.size();
                for (int i2 = 0; i2 < nonDrawerViewsCount; i2++) {
                    View child2 = this.mNonDrawerViews.get(i2);
                    if (child2.getVisibility() == 0) {
                        child2.addFocusables(views, direction, focusableMode);
                    }
                }
            }
            this.mNonDrawerViews.clear();
        }
    }

    private boolean hasVisibleDrawer() {
        return findVisibleDrawer() != null;
    }

    View findVisibleDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (isDrawerView(child) && isDrawerVisible(child)) {
                return child;
            }
        }
        return null;
    }

    void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            long now = SystemClock.uptimeMillis();
            MotionEvent cancelEvent = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).dispatchTouchEvent(cancelEvent);
            }
            cancelEvent.recycle();
            this.mChildrenCanceledTouch = true;
        }
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !hasVisibleDrawer()) {
            return super.onKeyDown(keyCode, event);
        }
        event.startTracking();
        return true;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            View visibleDrawer = findVisibleDrawer();
            if (visibleDrawer != null && getDrawerLockMode(visibleDrawer) == 0) {
                closeDrawers();
            }
            return visibleDrawer != null;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        View toOpen;
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.openDrawerGravity != 0 && (toOpen = findDrawerWithGravity(ss.openDrawerGravity)) != null) {
            openDrawer(toOpen);
        }
        if (ss.lockModeLeft != 3) {
            setDrawerLockMode(ss.lockModeLeft, 3);
        }
        if (ss.lockModeRight != 3) {
            setDrawerLockMode(ss.lockModeRight, 5);
        }
        if (ss.lockModeStart != 3) {
            setDrawerLockMode(ss.lockModeStart, GravityCompat.START);
        }
        if (ss.lockModeEnd != 3) {
            setDrawerLockMode(ss.lockModeEnd, GravityCompat.END);
        }
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            boolean isOpenedAndNotClosing = lp.openState == 1;
            boolean isClosedAndOpening = lp.openState == 2;
            if (isOpenedAndNotClosing || isClosedAndOpening) {
                ss.openDrawerGravity = lp.gravity;
                break;
            }
        }
        ss.lockModeLeft = this.mLockModeLeft;
        ss.lockModeRight = this.mLockModeRight;
        ss.lockModeStart = this.mLockModeStart;
        ss.lockModeEnd = this.mLockModeEnd;
        return ss;
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        View openDrawer = findOpenDrawer();
        if (openDrawer != null || isDrawerView(child)) {
            ViewCompat.setImportantForAccessibility(child, 4);
        } else {
            ViewCompat.setImportantForAccessibility(child, 1);
        }
        if (!CAN_HIDE_DESCENDANTS) {
            ViewCompat.setAccessibilityDelegate(child, this.mChildAccessibilityDelegate);
        }
    }

    static boolean includeChildForAccessibility(View child) {
        return (ViewCompat.getImportantForAccessibility(child) == 4 || ViewCompat.getImportantForAccessibility(child) == 2) ? false : true;
    }

    protected static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: android.support.v4.widget.DrawerLayout.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int lockModeEnd;
        int lockModeLeft;
        int lockModeRight;
        int lockModeStart;
        int openDrawerGravity;

        public SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.openDrawerGravity = 0;
            this.openDrawerGravity = in.readInt();
            this.lockModeLeft = in.readInt();
            this.lockModeRight = in.readInt();
            this.lockModeStart = in.readInt();
            this.lockModeEnd = in.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
            this.openDrawerGravity = 0;
        }

        @Override // android.support.v4.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.openDrawerGravity);
            dest.writeInt(this.lockModeLeft);
            dest.writeInt(this.lockModeRight);
            dest.writeInt(this.lockModeStart);
            dest.writeInt(this.lockModeEnd);
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable = new Runnable() { // from class: android.support.v4.widget.DrawerLayout.ViewDragCallback.1
            @Override // java.lang.Runnable
            public void run() {
                ViewDragCallback.this.peekDrawer();
            }
        };

        ViewDragCallback(int gravity) {
            this.mAbsGravity = gravity;
        }

        public void setDragger(ViewDragHelper dragger) {
            this.mDragger = dragger;
        }

        public void removeCallbacks() {
            DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public boolean tryCaptureView(View child, int pointerId) {
            return DrawerLayout.this.isDrawerView(child) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(child, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(child) == 0;
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewDragStateChanged(int state) {
            DrawerLayout.this.updateDrawerState(this.mAbsGravity, state, this.mDragger.getCapturedView());
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            float offset;
            int childWidth = changedView.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(changedView, 3)) {
                offset = (childWidth + left) / childWidth;
            } else {
                int width = DrawerLayout.this.getWidth();
                offset = (width - left) / childWidth;
            }
            DrawerLayout.this.setDrawerViewOffset(changedView, offset);
            changedView.setVisibility(offset == 0.0f ? 4 : 0);
            DrawerLayout.this.invalidate();
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewCaptured(View capturedChild, int activePointerId) {
            LayoutParams lp = (LayoutParams) capturedChild.getLayoutParams();
            lp.isPeeking = false;
            closeOtherDrawer();
        }

        private void closeOtherDrawer() {
            int otherGrav = this.mAbsGravity == 3 ? 5 : 3;
            View toClose = DrawerLayout.this.findDrawerWithGravity(otherGrav);
            if (toClose != null) {
                DrawerLayout.this.closeDrawer(toClose);
            }
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int left;
            float offset = DrawerLayout.this.getDrawerViewOffset(releasedChild);
            int childWidth = releasedChild.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(releasedChild, 3)) {
                left = (xvel > 0.0f || (xvel == 0.0f && offset > 0.5f)) ? 0 : -childWidth;
            } else {
                int width = DrawerLayout.this.getWidth();
                left = (xvel < 0.0f || (xvel == 0.0f && offset > 0.5f)) ? width - childWidth : width;
            }
            this.mDragger.settleCapturedViewAt(left, releasedChild.getTop());
            DrawerLayout.this.invalidate();
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
        }

        void peekDrawer() {
            View toCapture;
            int childLeft;
            int peekDistance = this.mDragger.getEdgeSize();
            boolean leftEdge = this.mAbsGravity == 3;
            if (leftEdge) {
                toCapture = DrawerLayout.this.findDrawerWithGravity(3);
                childLeft = (toCapture != null ? -toCapture.getWidth() : 0) + peekDistance;
            } else {
                toCapture = DrawerLayout.this.findDrawerWithGravity(5);
                childLeft = DrawerLayout.this.getWidth() - peekDistance;
            }
            if (toCapture != null) {
                if (((leftEdge && toCapture.getLeft() < childLeft) || (!leftEdge && toCapture.getLeft() > childLeft)) && DrawerLayout.this.getDrawerLockMode(toCapture) == 0) {
                    LayoutParams lp = (LayoutParams) toCapture.getLayoutParams();
                    this.mDragger.smoothSlideViewTo(toCapture, childLeft, toCapture.getTop());
                    lp.isPeeking = true;
                    DrawerLayout.this.invalidate();
                    closeOtherDrawer();
                    DrawerLayout.this.cancelChildViewTouch();
                }
            }
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public boolean onEdgeLock(int edgeFlags) {
            return false;
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            View toCapture;
            if ((edgeFlags & 1) == 1) {
                toCapture = DrawerLayout.this.findDrawerWithGravity(3);
            } else {
                toCapture = DrawerLayout.this.findDrawerWithGravity(5);
            }
            if (toCapture != null && DrawerLayout.this.getDrawerLockMode(toCapture) == 0) {
                this.mDragger.captureChildView(toCapture, pointerId);
            }
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public int getViewHorizontalDragRange(View child) {
            if (DrawerLayout.this.isDrawerView(child)) {
                return child.getWidth();
            }
            return 0;
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(child, 3)) {
                return Math.max(-child.getWidth(), Math.min(left, 0));
            }
            int width = DrawerLayout.this.getWidth();
            return Math.max(width - child.getWidth(), Math.min(left, width));
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public int clampViewPositionVertical(View child, int top, int dy) {
            return child.getTop();
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static final int FLAG_IS_CLOSING = 4;
        private static final int FLAG_IS_OPENED = 1;
        private static final int FLAG_IS_OPENING = 2;
        public int gravity;
        boolean isPeeking;
        float onScreen;
        int openState;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.gravity = 0;
            TypedArray a = c.obtainStyledAttributes(attrs, DrawerLayout.LAYOUT_ATTRS);
            this.gravity = a.getInt(0, 0);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = 0;
        }

        public LayoutParams(int width, int height, int gravity) {
            this(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.gravity = 0;
            this.gravity = source.gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.gravity = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.gravity = 0;
        }
    }

    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect = new Rect();

        AccessibilityDelegate() {
        }

        @Override // android.support.v4.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
                super.onInitializeAccessibilityNodeInfo(host, info);
            } else {
                AccessibilityNodeInfoCompat superNode = AccessibilityNodeInfoCompat.obtain(info);
                super.onInitializeAccessibilityNodeInfo(host, superNode);
                info.setSource(host);
                Object parentForAccessibility = ViewCompat.getParentForAccessibility(host);
                if (parentForAccessibility instanceof View) {
                    info.setParent((View) parentForAccessibility);
                }
                copyNodeInfoNoChildren(info, superNode);
                superNode.recycle();
                addChildrenForAccessibility(info, (ViewGroup) host);
            }
            info.setClassName(DrawerLayout.class.getName());
            info.setFocusable(false);
            info.setFocused(false);
            info.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
            info.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
        }

        @Override // android.support.v4.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(DrawerLayout.class.getName());
        }

        @Override // android.support.v4.view.AccessibilityDelegateCompat
        public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            if (event.getEventType() != 32) {
                return super.dispatchPopulateAccessibilityEvent(host, event);
            }
            List<CharSequence> eventText = event.getText();
            View visibleDrawer = DrawerLayout.this.findVisibleDrawer();
            if (visibleDrawer != null) {
                int edgeGravity = DrawerLayout.this.getDrawerViewAbsoluteGravity(visibleDrawer);
                CharSequence title = DrawerLayout.this.getDrawerTitle(edgeGravity);
                if (title != null) {
                    eventText.add(title);
                }
            }
            return true;
        }

        @Override // android.support.v4.view.AccessibilityDelegateCompat
        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            if (DrawerLayout.CAN_HIDE_DESCENDANTS || DrawerLayout.includeChildForAccessibility(child)) {
                return super.onRequestSendAccessibilityEvent(host, child, event);
            }
            return false;
        }

        private void addChildrenForAccessibility(AccessibilityNodeInfoCompat info, ViewGroup v) {
            int childCount = v.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = v.getChildAt(i);
                if (DrawerLayout.includeChildForAccessibility(child)) {
                    info.addChild(child);
                }
            }
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat dest, AccessibilityNodeInfoCompat src) {
            Rect rect = this.mTmpRect;
            src.getBoundsInParent(rect);
            dest.setBoundsInParent(rect);
            src.getBoundsInScreen(rect);
            dest.setBoundsInScreen(rect);
            dest.setVisibleToUser(src.isVisibleToUser());
            dest.setPackageName(src.getPackageName());
            dest.setClassName(src.getClassName());
            dest.setContentDescription(src.getContentDescription());
            dest.setEnabled(src.isEnabled());
            dest.setClickable(src.isClickable());
            dest.setFocusable(src.isFocusable());
            dest.setFocused(src.isFocused());
            dest.setAccessibilityFocused(src.isAccessibilityFocused());
            dest.setSelected(src.isSelected());
            dest.setLongClickable(src.isLongClickable());
            dest.addAction(src.getActions());
        }
    }

    static final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
        ChildAccessibilityDelegate() {
        }

        @Override // android.support.v4.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View child, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(child, info);
            if (!DrawerLayout.includeChildForAccessibility(child)) {
                info.setParent(null);
            }
        }
    }
}
