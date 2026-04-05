package android.support.wearable.view.drawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.wearable.R;
import android.support.wearable.internal.view.drawer.MultiPagePresenter;
import android.support.wearable.internal.view.drawer.MultiPageUi;
import android.support.wearable.internal.view.drawer.SinglePagePresenter;
import android.support.wearable.internal.view.drawer.SinglePageUi;
import android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class WearableNavigationDrawer extends WearableDrawerView {
    private static final long AUTO_CLOSE_DRAWER_DELAY_MS = TimeUnit.SECONDS.toMillis(5);
    private static final int DEFAULT_STYLE = 1;
    private static final String TAG = "WearableNavDrawer";
    private final Runnable mCloseDrawerRunnable;

    @Nullable
    private final GestureDetector mGestureDetector;
    private final boolean mIsAccessibilityEnabled;
    private final Handler mMainThreadHandler;
    private final GestureDetector.SimpleOnGestureListener mOnGestureListener;
    private final WearableNavigationDrawerPresenter mPresenter;

    @Retention(RetentionPolicy.SOURCE)
    public @interface NavigationStyle {
        public static final int MULTI_PAGE = 1;
        public static final int SINGLE_PAGE = 0;
    }

    public static abstract class WearableNavigationDrawerAdapter {

        @Nullable
        private WearableNavigationDrawerPresenter mPresenter;

        public abstract int getCount();

        public abstract Drawable getItemDrawable(int i);

        public abstract String getItemText(int i);

        public abstract void onItemSelected(int i);

        public void notifyDataSetChanged() {
            if (this.mPresenter != null) {
                this.mPresenter.onDataSetChanged();
            } else {
                Log.w(WearableNavigationDrawer.TAG, "adapter.notifyDataSetChanged called before drawer.setAdapter; ignoring.");
            }
        }

        public void setPresenter(WearableNavigationDrawerPresenter presenter) {
            this.mPresenter = presenter;
        }
    }

    public WearableNavigationDrawer(Context context) {
        this(context, (AttributeSet) null);
    }

    public WearableNavigationDrawer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableNavigationDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        WearableNavigationDrawerPresenter multiPagePresenter;
        super(context, attrs, defStyleAttr);
        this.mMainThreadHandler = new Handler(Looper.getMainLooper());
        this.mCloseDrawerRunnable = new Runnable() { // from class: android.support.wearable.view.drawer.WearableNavigationDrawer.1
            @Override // java.lang.Runnable
            public void run() {
                WearableNavigationDrawer.this.closeDrawer();
            }
        };
        this.mOnGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: android.support.wearable.view.drawer.WearableNavigationDrawer.2
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent e) {
                return WearableNavigationDrawer.this.mPresenter.onDrawerTapped();
            }
        };
        this.mGestureDetector = new GestureDetector(getContext(), this.mOnGestureListener);
        boolean singlePage = false;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WearableNavigationDrawer, defStyleAttr, 0);
            try {
                int navigationStyle = typedArray.getInt(R.styleable.WearableNavigationDrawer_navigation_style, 1);
                singlePage = navigationStyle == 0;
            } finally {
                typedArray.recycle();
            }
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        this.mIsAccessibilityEnabled = accessibilityManager.isEnabled();
        if (singlePage) {
            multiPagePresenter = new SinglePagePresenter(new SinglePageUi(this), this.mIsAccessibilityEnabled);
        } else {
            multiPagePresenter = new MultiPagePresenter(this, new MultiPageUi(), this.mIsAccessibilityEnabled);
        }
        this.mPresenter = multiPagePresenter;
        getPeekContainer().setContentDescription(context.getString(R.string.navigation_drawer_content_description));
        setShouldOnlyOpenWhenAtTop(true);
    }

    @VisibleForTesting
    public WearableNavigationDrawer(Context context, WearableNavigationDrawerPresenter presenter, GestureDetector gestureDetector) {
        super(context);
        this.mMainThreadHandler = new Handler(Looper.getMainLooper());
        this.mCloseDrawerRunnable = new Runnable() { // from class: android.support.wearable.view.drawer.WearableNavigationDrawer.1
            @Override // java.lang.Runnable
            public void run() {
                WearableNavigationDrawer.this.closeDrawer();
            }
        };
        this.mOnGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: android.support.wearable.view.drawer.WearableNavigationDrawer.2
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent e) {
                return WearableNavigationDrawer.this.mPresenter.onDrawerTapped();
            }
        };
        this.mPresenter = presenter;
        this.mGestureDetector = gestureDetector;
        this.mIsAccessibilityEnabled = false;
    }

    public void setAdapter(WearableNavigationDrawerAdapter adapter) {
        this.mPresenter.onNewAdapter(adapter);
    }

    public void setCurrentItem(int index, boolean smoothScrollTo) {
        this.mPresenter.onSetCurrentItemRequested(index, smoothScrollTo);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        autoCloseDrawerAfterDelay();
        return this.mGestureDetector != null && this.mGestureDetector.onTouchEvent(ev);
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int direction) {
        return isOpened();
    }

    @Override // android.support.wearable.view.drawer.WearableDrawerView
    public void onDrawerOpened() {
        autoCloseDrawerAfterDelay();
    }

    @Override // android.support.wearable.view.drawer.WearableDrawerView
    public void onDrawerClosed() {
        this.mMainThreadHandler.removeCallbacks(this.mCloseDrawerRunnable);
    }

    private void autoCloseDrawerAfterDelay() {
        if (!this.mIsAccessibilityEnabled) {
            this.mMainThreadHandler.removeCallbacks(this.mCloseDrawerRunnable);
            this.mMainThreadHandler.postDelayed(this.mCloseDrawerRunnable, AUTO_CLOSE_DRAWER_DELAY_MS);
        }
    }

    @Override // android.support.wearable.view.drawer.WearableDrawerView
    int preferGravity() {
        return 48;
    }
}
