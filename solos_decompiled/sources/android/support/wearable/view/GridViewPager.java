package android.support.wearable.view;

import android.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ScrollView;
import android.widget.Scroller;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public class GridViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final boolean DEBUG_ADAPTER = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final boolean DEBUG_LIFECYCLE = false;
    private static final boolean DEBUG_LISTENERS = false;
    private static final boolean DEBUG_POPULATE = false;
    private static final boolean DEBUG_ROUND = false;
    private static final boolean DEBUG_SCROLLING = false;
    private static final boolean DEBUG_SETTLING = false;
    private static final boolean DEBUG_TOUCH = false;
    private static final boolean DEBUG_TOUCHSLOP = false;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int MIN_ACCURATE_VELOCITY = 200;
    private static final int MIN_DISTANCE_FOR_FLING_DP = 40;
    private static final int NO_POINTER = -1;
    private static final int SCROLL_AXIS_X = 0;
    private static final int SCROLL_AXIS_Y = 1;
    public static final int SCROLL_STATE_CONTENT_SETTLING = 3;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final int SLIDE_ANIMATION_DURATION_NORMAL_MS = 300;
    private static final String TAG = "GridViewPager";
    private int mActivePointerId;
    private GridPagerAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private boolean mAdapterChangeNotificationPending;
    private final BackgroundController mBackgroundController;
    private boolean mCalledSuper;
    private final int mCloseEnough;
    private int mColMargin;
    private boolean mConsumeInsets;
    private final Point mCurItem;
    private boolean mDatasetChangePending;
    private boolean mDelayPopulate;
    private final Runnable mEndScrollRunnable;
    private int mExpectedCurrentColumnCount;
    private int mExpectedRowCount;
    private boolean mFirstLayout;
    private int mGestureInitialScrollY;
    private float mGestureInitialX;
    private float mGestureInitialY;
    private boolean mInLayout;
    private boolean mIsAbleToDrag;
    private boolean mIsBeingDragged;
    private final SimpleArrayMap<Point, ItemInfo> mItems;
    private final int mMinFlingDistance;
    private final int mMinFlingVelocity;
    private final int mMinUsableVelocity;
    private PagerObserver mObserver;
    private int mOffscreenPageCount;
    private GridPagerAdapter mOldAdapter;
    private View.OnApplyWindowInsetsListener mOnApplyWindowInsetsListener;
    private OnPageChangeListener mOnPageChangeListener;
    private float mPointerLastX;
    private float mPointerLastY;
    private final Rect mPopulatedPageBounds;
    private final Rect mPopulatedPages;
    private final SimpleArrayMap<Point, ItemInfo> mRecycledItems;
    private Parcelable mRestoredAdapterState;
    private ClassLoader mRestoredClassLoader;
    private Point mRestoredCurItem;
    private int mRowMargin;
    private final SparseIntArray mRowScrollX;
    private int mScrollAxis;
    private int mScrollState;
    private final Scroller mScroller;
    private View mScrollingContent;
    private int mSlideAnimationDurationMs;
    private final Point mTempPoint1;
    private final int mTouchSlop;
    private final int mTouchSlopSquared;
    private VelocityTracker mVelocityTracker;
    private WindowInsets mWindowInsets;
    private static final int[] LAYOUT_ATTRS = {R.attr.layout_gravity};
    private static final Interpolator OVERSCROLL_INTERPOLATOR = new DragFrictionInterpolator();
    private static final Interpolator SLIDE_INTERPOLATOR = new DecelerateInterpolator(2.5f);

    public interface OnAdapterChangeListener {
        void onAdapterChanged(GridPagerAdapter gridPagerAdapter, GridPagerAdapter gridPagerAdapter2);

        void onDataSetChanged();
    }

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, int i2, float f, float f2, int i3, int i4);

        void onPageSelected(int i, int i2);
    }

    static class ItemInfo {
        Object object;
        int positionX;
        int positionY;

        ItemInfo() {
        }

        public String toString() {
            int i = this.positionX;
            int i2 = this.positionY;
            String strValueOf = String.valueOf(this.object);
            return new StringBuilder(String.valueOf(strValueOf).length() + 27).append(i).append(",").append(i2).append(" => ").append(strValueOf).toString();
        }
    }

    public GridViewPager(Context context) {
        this(context, null, 0);
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mConsumeInsets = true;
        this.mSlideAnimationDurationMs = 300;
        this.mEndScrollRunnable = new Runnable() { // from class: android.support.wearable.view.GridViewPager.1
            @Override // java.lang.Runnable
            public void run() {
                GridViewPager.this.setScrollState(0);
                GridViewPager.this.populate();
            }
        };
        this.mOffscreenPageCount = 1;
        this.mActivePointerId = -1;
        this.mVelocityTracker = null;
        this.mFirstLayout = true;
        this.mScrollState = 0;
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(vc);
        this.mTouchSlopSquared = this.mTouchSlop * this.mTouchSlop;
        this.mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        this.mMinFlingDistance = (int) (40.0f * density);
        this.mMinUsableVelocity = (int) (200.0f * density);
        this.mCloseEnough = (int) (2.0f * density);
        this.mCurItem = new Point();
        this.mItems = new SimpleArrayMap<>();
        this.mRecycledItems = new SimpleArrayMap<>();
        this.mPopulatedPages = new Rect();
        this.mPopulatedPageBounds = new Rect();
        this.mScroller = new Scroller(context, SLIDE_INTERPOLATOR, true);
        this.mTempPoint1 = new Point();
        setOverScrollMode(1);
        this.mRowScrollX = new SparseIntArray();
        this.mBackgroundController = new BackgroundController();
        this.mBackgroundController.attachTo(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
        getParent().requestFitSystemWindows();
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.dispatchApplyWindowInsets(insets);
        }
        this.mWindowInsets = insets;
        return insets;
    }

    public void setConsumeWindowInsets(boolean consume) {
        this.mConsumeInsets = consume;
    }

    @Override // android.view.View
    public void setOnApplyWindowInsetsListener(View.OnApplyWindowInsetsListener listener) {
        this.mOnApplyWindowInsetsListener = listener;
    }

    @Override // android.view.ViewGroup, android.view.View
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        WindowInsets insets2 = onApplyWindowInsets(insets);
        if (this.mOnApplyWindowInsetsListener != null) {
            this.mOnApplyWindowInsetsListener.onApplyWindowInsets(this, insets2);
        }
        return this.mConsumeInsets ? insets2.consumeSystemWindowInsets() : insets2;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestFitSystemWindows() {
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    public void setAdapter(GridPagerAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.setOnBackgroundChangeListener(null);
            this.mAdapter.startUpdate(this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo ii = this.mItems.valueAt(i);
                this.mAdapter.destroyItem(this, ii.positionY, ii.positionX, ii.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            removeAllViews();
            scrollTo(0, 0);
            this.mRowScrollX.clear();
        }
        GridPagerAdapter oldAdapter = this.mAdapter;
        this.mCurItem.set(0, 0);
        this.mAdapter = adapter;
        this.mExpectedRowCount = 0;
        this.mExpectedCurrentColumnCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mAdapter.setOnBackgroundChangeListener(this.mBackgroundController);
            this.mDelayPopulate = false;
            boolean wasFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedRowCount = this.mAdapter.getRowCount();
            if (this.mExpectedRowCount > 0) {
                this.mCurItem.set(0, 0);
                this.mExpectedCurrentColumnCount = this.mAdapter.getColumnCount(this.mCurItem.y);
            }
            if (this.mRestoredCurItem != null) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem.y, this.mRestoredCurItem.x, false, true);
                this.mRestoredCurItem = null;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (!wasFirstLayout) {
                populate();
            } else {
                requestLayout();
            }
        } else if (this.mIsBeingDragged) {
            cancelDragGesture();
        }
        if (oldAdapter != adapter) {
            if (adapter == null) {
                this.mAdapterChangeNotificationPending = false;
                adapterChanged(oldAdapter, adapter);
                this.mOldAdapter = null;
                return;
            } else {
                this.mAdapterChangeNotificationPending = true;
                this.mOldAdapter = oldAdapter;
                return;
            }
        }
        this.mAdapterChangeNotificationPending = false;
        this.mOldAdapter = null;
    }

    public GridPagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setOnAdapterChangeListener(OnAdapterChangeListener listener) {
        this.mAdapterChangeListener = listener;
        if (listener != null && this.mAdapter != null && !this.mAdapterChangeNotificationPending) {
            listener.onAdapterChanged(null, this.mAdapter);
        }
    }

    private void adapterChanged(GridPagerAdapter oldAdapter, GridPagerAdapter newAdapter) {
        if (this.mAdapterChangeListener != null) {
            this.mAdapterChangeListener.onAdapterChanged(oldAdapter, newAdapter);
        }
        if (this.mBackgroundController != null) {
            this.mBackgroundController.onAdapterChanged(oldAdapter, newAdapter);
        }
    }

    @Override // android.view.View
    public void scrollTo(int x, int y) {
        if (this.mScrollState == 2 && this.mScrollAxis == 1) {
            x = getRowScrollX(this.mCurItem.y);
        }
        super.scrollTo(0, y);
        scrollCurrentRowTo(x);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageScrollStateChanged(newState);
            }
            if (this.mBackgroundController != null) {
                this.mBackgroundController.onPageScrollStateChanged(newState);
            }
        }
    }

    private int getRowScrollX(int row) {
        return this.mRowScrollX.get(row, 0);
    }

    private void setRowScrollX(int row, int scrollX) {
        this.mRowScrollX.put(row, scrollX);
    }

    private void scrollRowTo(int row, int x) {
        if (getRowScrollX(row) != x) {
            int size = getChildCount();
            int scrollAmount = x - getRowScrollX(row);
            for (int i = 0; i < size; i++) {
                View child = getChildAt(i);
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.positionY == row) {
                    child.offsetLeftAndRight(-scrollAmount);
                    postInvalidateOnAnimation();
                }
            }
            setRowScrollX(row, x);
        }
    }

    private void scrollCurrentRowTo(int x) {
        scrollRowTo(this.mCurItem.y, x);
    }

    private int getContentWidth() {
        return getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());
    }

    private int getContentHeight() {
        return getMeasuredHeight() - (getPaddingTop() + getPaddingBottom());
    }

    public void setCurrentItem(int row, int column) {
        this.mDelayPopulate = false;
        setCurrentItemInternal(row, column, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int row, int column, boolean smoothScroll) {
        this.mDelayPopulate = false;
        setCurrentItemInternal(row, column, smoothScroll, false);
    }

    public Point getCurrentItem() {
        return new Point(this.mCurItem);
    }

    void setCurrentItemInternal(int row, int column, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(row, column, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int row, int column, boolean smoothScroll, boolean always, int velocity) {
        boolean dispatchSelected;
        if (this.mAdapter != null && this.mAdapter.getRowCount() > 0) {
            if (always || !this.mCurItem.equals(column, row) || this.mItems.size() == 0) {
                int row2 = limit(row, 0, this.mAdapter.getRowCount() - 1);
                int column2 = limit(column, 0, this.mAdapter.getColumnCount(row2) - 1);
                if (column2 != this.mCurItem.x) {
                    this.mScrollAxis = 0;
                    dispatchSelected = true;
                } else if (row2 != this.mCurItem.y) {
                    this.mScrollAxis = 1;
                    dispatchSelected = true;
                } else {
                    dispatchSelected = false;
                }
                if (this.mFirstLayout) {
                    this.mCurItem.set(0, 0);
                    this.mAdapter.setCurrentColumnForRow(row2, column2);
                    if (dispatchSelected) {
                        if (this.mOnPageChangeListener != null) {
                            this.mOnPageChangeListener.onPageSelected(row2, column2);
                        }
                        if (this.mBackgroundController != null) {
                            this.mBackgroundController.onPageSelected(row2, column2);
                        }
                    }
                    requestLayout();
                    return;
                }
                populate(column2, row2);
                scrollToItem(column2, row2, smoothScroll, velocity, dispatchSelected);
            }
        }
    }

    private void scrollToItem(int x, int y, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        ItemInfo curInfo = infoForPosition(x, y);
        int destX = 0;
        int destY = 0;
        if (curInfo != null) {
            destX = computePageLeft(curInfo.positionX) - getPaddingLeft();
            destY = computePageTop(curInfo.positionY) - getPaddingTop();
        }
        this.mAdapter.setCurrentColumnForRow(y, x);
        if (dispatchSelected) {
            if (this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(y, x);
            }
            if (this.mBackgroundController != null) {
                this.mBackgroundController.onPageSelected(y, x);
            }
        }
        if (smoothScroll) {
            smoothScrollTo(destX, destY, velocity);
            return;
        }
        completeScroll(false);
        scrollTo(destX, destY);
        pageScrolled(destX, destY);
    }

    public int getOffscreenPageCount() {
        return this.mOffscreenPageCount;
    }

    public void setOffscreenPageCount(int limit) {
        if (limit < 1) {
            Log.w(TAG, new StringBuilder(79).append("Requested offscreen page limit ").append(limit).append(" too small; defaulting to ").append(1).toString());
            limit = 1;
        }
        if (limit != this.mOffscreenPageCount) {
            this.mOffscreenPageCount = limit;
            populate();
        }
    }

    public void setPageMargins(int rowMarginPx, int columnMarginPx) {
        int oldRowMargin = this.mRowMargin;
        this.mRowMargin = rowMarginPx;
        int oldColMargin = this.mColMargin;
        this.mColMargin = columnMarginPx;
        int width = getWidth();
        int height = getHeight();
        if (!this.mFirstLayout && !this.mItems.isEmpty()) {
            recomputeScrollPosition(width, width, height, height, this.mColMargin, oldColMargin, this.mRowMargin, oldRowMargin);
            requestLayout();
        }
    }

    public void setSlideAnimationDuration(int slideAnimationDuration) {
        this.mSlideAnimationDurationMs = slideAnimationDuration;
    }

    public int getPageRowMargin() {
        return this.mRowMargin;
    }

    public int getPageColumnMargin() {
        return this.mColMargin;
    }

    void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, 0);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        if (getChildCount() != 0) {
            int sx = getRowScrollX(this.mCurItem.y);
            int sy = getScrollY();
            int dx = x - sx;
            int dy = y - sy;
            if (dx == 0 && dy == 0) {
                completeScroll(false);
                populate();
                setScrollState(0);
            } else {
                setScrollState(2);
                int duration = this.mSlideAnimationDurationMs;
                this.mScroller.startScroll(sx, sy, dx, dy, duration);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    void flingContent(int limitX, int limitY, int velocityX, int velocityY) {
        int minX;
        int maxX;
        int minY;
        int maxY;
        if (this.mScrollingContent != null) {
            if (velocityX == 0 && velocityY == 0) {
                completeScroll(false);
                setScrollState(0);
                return;
            }
            int sx = this.mScrollingContent.getScrollX();
            int sy = this.mScrollingContent.getScrollY();
            setScrollState(3);
            if (velocityX > 0) {
                minX = sx;
                maxX = sx + limitX;
            } else {
                minX = sx + limitX;
                maxX = sx;
            }
            if (velocityY > 0) {
                minY = sy;
                maxY = sy + limitY;
            } else {
                minY = sy + limitY;
                maxY = sy;
            }
            this.mScroller.fling(sx, sy, velocityX, velocityY, minX, maxX, minY, maxY);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private ItemInfo addNewItem(int positionX, int positionY) {
        Point key = new Point(positionX, positionY);
        ItemInfo ii = this.mRecycledItems.remove(key);
        if (ii == null) {
            ii = new ItemInfo();
            ii.object = this.mAdapter.instantiateItem(this, positionY, positionX);
            ii.positionX = positionX;
            ii.positionY = positionY;
        }
        key.set(positionX, positionY);
        ii.positionX = positionX;
        ii.positionY = positionY;
        this.mItems.put(key, ii);
        return ii;
    }

    void rowBackgroundChanged(int row) {
        if (this.mBackgroundController != null) {
            this.mBackgroundController.onRowBackgroundChanged(row);
        }
    }

    void pageBackgroundChanged(int row, int column) {
        if (this.mBackgroundController != null) {
            this.mBackgroundController.onPageBackgroundChanged(row, column);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dataSetChanged() {
        int adapterRowCount = this.mAdapter.getRowCount();
        this.mExpectedRowCount = adapterRowCount;
        Point newCurrItem = new Point(this.mCurItem);
        boolean isUpdating = false;
        SimpleArrayMap<Point, ItemInfo> newItemMap = new SimpleArrayMap<>();
        for (int i = this.mItems.size() - 1; i >= 0; i--) {
            Point itemKey = this.mItems.keyAt(i);
            ItemInfo itemInfo = this.mItems.valueAt(i);
            Point newItemPos = this.mAdapter.getItemPosition(itemInfo.object);
            this.mAdapter.applyItemPosition(itemInfo.object, newItemPos);
            if (newItemPos == GridPagerAdapter.POSITION_UNCHANGED) {
                newItemMap.put(itemKey, itemInfo);
            } else if (newItemPos == GridPagerAdapter.POSITION_NONE) {
                if (!isUpdating) {
                    this.mAdapter.startUpdate(this);
                    isUpdating = true;
                }
                this.mAdapter.destroyItem(this, itemInfo.positionY, itemInfo.positionX, itemInfo.object);
                if (this.mCurItem.equals(itemInfo.positionX, itemInfo.positionY)) {
                    newCurrItem.y = limit(this.mCurItem.y, 0, Math.max(0, adapterRowCount - 1));
                    if (newCurrItem.y < adapterRowCount) {
                        newCurrItem.x = limit(this.mCurItem.x, 0, this.mAdapter.getColumnCount(newCurrItem.y) - 1);
                    } else {
                        newCurrItem.x = 0;
                    }
                }
            } else if (!newItemPos.equals(itemInfo.positionX, itemInfo.positionY)) {
                if (this.mCurItem.equals(itemInfo.positionX, itemInfo.positionY)) {
                    newCurrItem.set(newItemPos.x, newItemPos.y);
                }
                itemInfo.positionX = newItemPos.x;
                itemInfo.positionY = newItemPos.y;
                newItemMap.put(new Point(newItemPos), itemInfo);
            }
        }
        this.mItems.clear();
        this.mItems.putAll(newItemMap);
        if (isUpdating) {
            this.mAdapter.finishUpdate(this);
        }
        if (this.mExpectedRowCount > 0) {
            this.mExpectedCurrentColumnCount = this.mAdapter.getColumnCount(newCurrItem.y);
        } else {
            this.mExpectedCurrentColumnCount = 0;
        }
        dispatchOnDataSetChanged();
        setCurrentItemInternal(newCurrItem.y, newCurrItem.x, false, true);
        requestLayout();
    }

    private void dispatchOnDataSetChanged() {
        if (this.mAdapterChangeListener != null) {
            this.mAdapterChangeListener.onDataSetChanged();
        }
        if (this.mBackgroundController != null) {
            this.mBackgroundController.onDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populate() {
        if (this.mAdapter != null && this.mAdapter.getRowCount() > 0) {
            populate(this.mCurItem.x, this.mCurItem.y);
        }
    }

    private void cancelDragGesture() {
        cancelPendingInputEvents();
        long now = SystemClock.uptimeMillis();
        MotionEvent event = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
        event.setSource(InputDeviceCompat.SOURCE_TOUCHSCREEN);
        dispatchTouchEvent(event);
        event.recycle();
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0155  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void populate(int r24, int r25) {
        /*
            Method dump skipped, instruction units count: 951
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.wearable.view.GridViewPager.populate(int, int):void");
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState state = new SavedState(superState);
        state.currentX = this.mCurItem.x;
        state.currentY = this.mCurItem.y;
        return state;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (pointInRange(ss.currentX, ss.currentY)) {
            this.mRestoredCurItem = new Point(ss.currentX, ss.currentY);
        } else {
            this.mCurItem.set(0, 0);
            scrollTo(0, 0);
        }
    }

    private static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: android.support.wearable.view.GridViewPager.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int currentX;
        int currentY;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.currentX);
            out.writeInt(this.currentY);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentX = in.readInt();
            this.currentY = in.readInt();
        }
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        infoForChild(child);
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        LayoutParams lp = (LayoutParams) params;
        if (this.mInLayout) {
            lp.needsMeasure = true;
            addViewInLayout(child, index, params);
        } else {
            super.addView(child, index, params);
        }
        if (this.mWindowInsets != null) {
            child.onApplyWindowInsets(this.mWindowInsets);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        infoForChild(view);
        if (this.mInLayout) {
            removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    private ItemInfo infoForChild(View child) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = this.mItems.valueAt(i);
            if (ii != null && this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }
        return null;
    }

    private ItemInfo infoForPosition(Point p) {
        return this.mItems.get(p);
    }

    private ItemInfo infoForPosition(int x, int y) {
        this.mTempPoint1.set(x, y);
        return this.mItems.get(this.mTempPoint1);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LayoutParams lp;
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8 && (lp = (LayoutParams) child.getLayoutParams()) != null) {
                measureChild(child, lp);
            }
        }
    }

    public void measureChild(View child, LayoutParams lp) {
        int childDefaultWidth = getContentWidth();
        int childDefaultHeight = getContentHeight();
        int widthMode = lp.width == -2 ? 0 : 1073741824;
        int heightMode = lp.height == -2 ? 0 : 1073741824;
        int widthSpec = View.MeasureSpec.makeMeasureSpec(childDefaultWidth, widthMode);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(childDefaultHeight, heightMode);
        int childWidthMeasureSpec = getChildMeasureSpec(widthSpec, lp.leftMargin + lp.rightMargin, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(heightSpec, lp.topMargin + lp.bottomMargin, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!this.mItems.isEmpty()) {
            recomputeScrollPosition(w, oldw, h, oldh, this.mColMargin, this.mColMargin, this.mRowMargin, this.mRowMargin);
        }
    }

    private int computePageLeft(int column) {
        return ((getContentWidth() + this.mColMargin) * column) + getPaddingLeft();
    }

    private int computePageTop(int row) {
        return ((getContentHeight() + this.mRowMargin) * row) + getPaddingTop();
    }

    private void recomputeScrollPosition(int width, int oldWidth, int height, int oldHeight, int colMargin, int oldColMargin, int rowMargin, int oldRowMargin) {
        if (oldWidth > 0 && oldHeight > 0) {
            int widthWithMargin = ((width - getPaddingLeft()) - getPaddingRight()) + colMargin;
            int oldWidthWithMargin = ((oldWidth - getPaddingLeft()) - getPaddingRight()) + oldColMargin;
            int heightWithMargin = ((height - getPaddingTop()) - getPaddingBottom()) + rowMargin;
            int oldHeightWithMargin = ((oldHeight - getPaddingTop()) - getPaddingBottom()) + oldRowMargin;
            int xpos = getRowScrollX(this.mCurItem.y);
            float pageOffset = xpos / oldWidthWithMargin;
            int newOffsetXPixels = (int) (widthWithMargin * pageOffset);
            int ypos = getScrollY();
            float pageOffsetY = ypos / oldHeightWithMargin;
            int newOffsetYPixels = (int) (heightWithMargin * pageOffsetY);
            scrollTo(newOffsetXPixels, newOffsetYPixels);
            if (!this.mScroller.isFinished()) {
                ItemInfo targetInfo = infoForPosition(this.mCurItem);
                int targetX = computePageLeft(targetInfo.positionX) - getPaddingLeft();
                int targetY = computePageTop(targetInfo.positionY) - getPaddingTop();
                int newDuration = this.mScroller.getDuration() - this.mScroller.timePassed();
                this.mScroller.startScroll(newOffsetXPixels, newOffsetYPixels, targetX, targetY, newDuration);
                return;
            }
            return;
        }
        ItemInfo ii = infoForPosition(this.mCurItem);
        if (ii != null) {
            int targetX2 = computePageLeft(ii.positionX) - getPaddingLeft();
            int targetY2 = computePageTop(ii.positionY) - getPaddingTop();
            if (targetX2 != getRowScrollX(ii.positionY) || targetY2 != getScrollY()) {
                completeScroll(false);
                scrollTo(targetX2, targetY2);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int children = getChildCount();
        for (int i = 0; i < children; i++) {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            if (lp == null) {
                String strValueOf = String.valueOf(view);
                Log.w(TAG, new StringBuilder(String.valueOf(strValueOf).length() + 34).append("Got null layout params for child: ").append(strValueOf).toString());
            } else {
                ItemInfo ii = infoForChild(view);
                if (ii == null) {
                    String strValueOf2 = String.valueOf(view);
                    Log.w(TAG, new StringBuilder(String.valueOf(strValueOf2).length() + 44).append("Unknown child view, not claimed by adapter: ").append(strValueOf2).toString());
                } else {
                    if (lp.needsMeasure) {
                        lp.needsMeasure = false;
                        measureChild(view, lp);
                    }
                    int left = computePageLeft(ii.positionX);
                    int top = computePageTop(ii.positionY);
                    int left2 = (left - getRowScrollX(ii.positionY)) + lp.leftMargin;
                    int top2 = top + lp.topMargin;
                    view.layout(left2, top2, view.getMeasuredWidth() + left2, view.getMeasuredHeight() + top2);
                }
            }
        }
        if (this.mFirstLayout && !this.mItems.isEmpty()) {
            scrollToItem(this.mCurItem.x, this.mCurItem.y, false, 0, false);
        }
        this.mFirstLayout = false;
    }

    @Override // android.view.View
    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            if (this.mScrollState == 3) {
                if (this.mScrollingContent == null) {
                    this.mScroller.abortAnimation();
                } else {
                    this.mScrollingContent.scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
                }
            } else {
                int oldX = getRowScrollX(this.mCurItem.y);
                int oldY = getScrollY();
                int x = this.mScroller.getCurrX();
                int y = this.mScroller.getCurrY();
                if (oldX != x || oldY != y) {
                    scrollTo(x, y);
                    if (!pageScrolled(x, y)) {
                        this.mScroller.abortAnimation();
                        scrollTo(0, 0);
                    }
                }
            }
            ViewCompat.postInvalidateOnAnimation(this);
            return;
        }
        completeScroll(true);
    }

    private static String scrollStateToString(int state) {
        switch (state) {
            case 0:
                return "IDLE";
            case 1:
                return "DRAGGING";
            case 2:
                return "SETTLING";
            case 3:
                return "CONTENT_SETTLING";
            default:
                return "";
        }
    }

    private boolean pageScrolled(int xpos, int ypos) {
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            onPageScrolled(0, 0, 0.0f, 0.0f, 0, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
        ItemInfo ii = infoForCurrentScrollPosition();
        int pageLeft = computePageLeft(ii.positionX);
        int pageTop = computePageTop(ii.positionY);
        int offsetLeftPx = (getPaddingLeft() + xpos) - pageLeft;
        int offsetTopPx = (getPaddingTop() + ypos) - pageTop;
        float offsetLeft = getXIndex(offsetLeftPx);
        float offsetTop = getYIndex(offsetTopPx);
        this.mCalledSuper = false;
        onPageScrolled(ii.positionX, ii.positionY, offsetLeft, offsetTop, offsetLeftPx, offsetTopPx);
        if (!this.mCalledSuper) {
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
        return true;
    }

    public void onPageScrolled(int positionX, int positionY, float offsetX, float offsetY, int offsetLeftPx, int offsetTopPx) {
        this.mCalledSuper = true;
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(positionY, positionX, offsetY, offsetX, offsetTopPx, offsetLeftPx);
        }
        if (this.mBackgroundController != null) {
            this.mBackgroundController.onPageScrolled(positionY, positionX, offsetY, offsetX, offsetTopPx, offsetLeftPx);
        }
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate = this.mScrollState == 2;
        if (needPopulate) {
            this.mScroller.abortAnimation();
            int oldX = getRowScrollX(this.mCurItem.y);
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                scrollTo(x, y);
            }
        }
        this.mScrollingContent = null;
        this.mDelayPopulate = false;
        if (needPopulate) {
            if (postEvents) {
                ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
            } else {
                this.mEndScrollRunnable.run();
            }
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & 255;
        if (action == 3 || action == 1) {
            this.mIsBeingDragged = false;
            this.mIsAbleToDrag = false;
            this.mActivePointerId = -1;
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            return false;
        }
        if (action != 0) {
            if (this.mIsBeingDragged) {
                return true;
            }
            if (!this.mIsAbleToDrag) {
                return false;
            }
        }
        switch (action) {
            case 0:
                handlePointerDown(ev);
                break;
            case 2:
                handlePointerMove(ev);
                break;
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        return this.mIsBeingDragged;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mAdapter == null) {
            return false;
        }
        int action = ev.getAction();
        switch (action & 255) {
            case 0:
                handlePointerDown(ev);
                return true;
            case 1:
            case 3:
                handlePointerUp(ev);
                return true;
            case 2:
                handlePointerMove(ev);
                return true;
            case 4:
            case 5:
            default:
                Log.e(TAG, new StringBuilder(32).append("Unknown action type: ").append(action).toString());
                return true;
            case 6:
                onSecondaryPointerUp(ev);
                return true;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private static float limit(float input, int limit) {
        return limit > 0 ? Math.max(0.0f, Math.min(input, limit)) : Math.min(0.0f, Math.max(input, limit));
    }

    private boolean performDrag(float x, float y) {
        float overscrollX;
        float overscrollY;
        float distanceToFocusPoint;
        View child;
        float deltaX = this.mPointerLastX - x;
        float deltaY = this.mPointerLastY - y;
        this.mPointerLastX = x;
        this.mPointerLastY = y;
        Rect pages = this.mPopulatedPages;
        int leftBound = computePageLeft(pages.left) - getPaddingLeft();
        int rightBound = computePageLeft(pages.right) - getPaddingLeft();
        int topBound = computePageTop(pages.top) - getPaddingTop();
        int bottomBound = computePageTop(pages.bottom) - getPaddingTop();
        float scrollX = getRowScrollX(this.mCurItem.y);
        float scrollY = getScrollY();
        if (this.mScrollAxis == 1) {
            int pageSpacingY = getContentHeight() + this.mRowMargin;
            if (deltaY < 0.0f) {
                distanceToFocusPoint = -(scrollY % pageSpacingY);
            } else {
                distanceToFocusPoint = (pageSpacingY - (scrollY % pageSpacingY)) % pageSpacingY;
            }
            boolean focalPointCrossed = false;
            if (Math.abs(distanceToFocusPoint) <= Math.abs(deltaY)) {
                deltaY -= distanceToFocusPoint;
                scrollY += distanceToFocusPoint;
                focalPointCrossed = true;
            }
            if (focalPointCrossed && (child = getChildForInfo(infoForScrollPosition((int) scrollX, (int) scrollY))) != null) {
                int dir = (int) Math.signum(deltaY);
                int scrollable = getScrollableDistance(child, dir);
                float consumed = limit(deltaY, scrollable);
                child.scrollBy(0, (int) consumed);
                deltaY -= consumed;
                this.mPointerLastY += consumed - ((int) consumed);
            }
        }
        int targetX = (int) (((int) deltaX) + scrollX);
        int targetY = (int) (((int) deltaY) + scrollY);
        boolean wouldOverscroll = targetX < leftBound || targetX > rightBound || targetY < topBound || targetY > bottomBound;
        if (wouldOverscroll) {
            int mode = getOverScrollMode();
            boolean couldScroll = (this.mScrollAxis == 0 && leftBound < rightBound) || (this.mScrollAxis == 1 && topBound < bottomBound);
            if (mode == 0 || (couldScroll && mode == 1)) {
                if (scrollX > rightBound) {
                    overscrollX = scrollX - rightBound;
                } else {
                    overscrollX = scrollX < ((float) leftBound) ? scrollX - leftBound : 0.0f;
                }
                if (scrollY > bottomBound) {
                    overscrollY = scrollY - bottomBound;
                } else {
                    overscrollY = scrollY < ((float) topBound) ? scrollY - topBound : 0.0f;
                }
                if (Math.abs(overscrollX) > 0.0f && Math.signum(overscrollX) == Math.signum(deltaX)) {
                    deltaX *= OVERSCROLL_INTERPOLATOR.getInterpolation(1.0f - (Math.abs(overscrollX) / getContentWidth()));
                }
                if (Math.abs(overscrollY) > 0.0f && Math.signum(overscrollY) == Math.signum(deltaY)) {
                    deltaY *= OVERSCROLL_INTERPOLATOR.getInterpolation(1.0f - (Math.abs(overscrollY) / getContentHeight()));
                }
            } else {
                deltaX = limit(deltaX, leftBound - scrollX, rightBound - scrollX);
                deltaY = limit(deltaY, topBound - scrollY, bottomBound - scrollY);
            }
        }
        float scrollX2 = scrollX + deltaX;
        float scrollY2 = scrollY + deltaY;
        this.mPointerLastX += scrollX2 - ((int) scrollX2);
        this.mPointerLastY += scrollY2 - ((int) scrollY2);
        scrollTo((int) scrollX2, (int) scrollY2);
        pageScrolled((int) scrollX2, (int) scrollY2);
        return true;
    }

    private int getScrollableDistance(View child, int dir) {
        if (child instanceof CardScrollView) {
            int scrollable = ((CardScrollView) child).getAvailableScrollDelta(dir);
            return scrollable;
        }
        if (!(child instanceof ScrollView)) {
            return 0;
        }
        int scrollable2 = getScrollableDistance((ScrollView) child, dir);
        return scrollable2;
    }

    private int getScrollableDistance(ScrollView view, int direction) {
        if (view.getChildCount() <= 0) {
            return 0;
        }
        View content = view.getChildAt(0);
        int height = view.getHeight();
        int contentHeight = content.getHeight();
        int extra = contentHeight - height;
        if (contentHeight <= height) {
            return 0;
        }
        if (direction > 0) {
            int distance = Math.min(extra - view.getScrollY(), 0);
            return distance;
        }
        if (direction >= 0) {
            return 0;
        }
        int distance2 = -view.getScrollY();
        return distance2;
    }

    private View getChildForInfo(ItemInfo ii) {
        if (ii.object != null) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (this.mAdapter.isViewFromObject(child, ii.object)) {
                    return child;
                }
            }
        }
        return null;
    }

    private ItemInfo infoForCurrentScrollPosition() {
        int y = (int) getYIndex(getScrollY());
        return infoForScrollPosition(getRowScrollX(y), getScrollY());
    }

    private ItemInfo infoForScrollPosition(int scrollX, int scrollY) {
        int y = (int) getYIndex(scrollY);
        int x = (int) getXIndex(scrollX);
        ItemInfo ii = infoForPosition(x, y);
        if (ii == null) {
            ItemInfo ii2 = new ItemInfo();
            ii2.positionX = x;
            ii2.positionY = y;
            return ii2;
        }
        return ii;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mPointerLastX = MotionEventCompat.getX(ev, newPointerIndex);
            this.mPointerLastY = MotionEventCompat.getY(ev, newPointerIndex);
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsAbleToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int direction) {
        if (getVisibility() != 0 || this.mAdapter == null || this.mItems.isEmpty()) {
            return false;
        }
        int scrollX = getRowScrollX(this.mCurItem.y);
        int lastColumnIndex = this.mExpectedCurrentColumnCount - 1;
        return direction > 0 ? getPaddingLeft() + scrollX < computePageLeft(lastColumnIndex) : scrollX > 0;
    }

    @Override // android.view.View
    public boolean canScrollVertically(int direction) {
        if (getVisibility() != 0 || this.mAdapter == null || this.mItems.isEmpty()) {
            return false;
        }
        int scrollY = getScrollY();
        int lastRowIndex = this.mExpectedRowCount - 1;
        return direction > 0 ? getPaddingTop() + scrollY < computePageTop(lastRowIndex) : scrollY > 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    private boolean executeKeyEvent(KeyEvent event) {
        boolean handled = false;
        switch (event.getKeyCode()) {
            case 19:
                handled = pageUp();
                break;
            case 20:
                handled = pageDown();
                break;
            case 21:
                handled = pageLeft();
                break;
            case 22:
                handled = pageRight();
                break;
            case 62:
                debug();
                return true;
        }
        return handled;
    }

    private boolean pageLeft() {
        if (this.mCurItem.x <= 0) {
            return false;
        }
        setCurrentItem(this.mCurItem.x - 1, this.mCurItem.y, true);
        return true;
    }

    private boolean pageRight() {
        if (this.mAdapter == null || this.mCurItem.x >= this.mAdapter.getColumnCount(this.mCurItem.y) - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem.x + 1, this.mCurItem.y, true);
        return true;
    }

    private boolean pageUp() {
        if (this.mCurItem.y <= 0) {
            return false;
        }
        setCurrentItem(this.mCurItem.x, this.mCurItem.y - 1, true);
        return true;
    }

    private boolean pageDown() {
        if (this.mAdapter == null || this.mCurItem.y >= this.mAdapter.getRowCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem.x, this.mCurItem.y + 1, true);
        return true;
    }

    private boolean handlePointerDown(MotionEvent ev) {
        if (!this.mIsBeingDragged) {
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
            this.mGestureInitialX = ev.getX();
            this.mGestureInitialY = ev.getY();
            this.mGestureInitialScrollY = getScrollY();
            this.mPointerLastX = this.mGestureInitialX;
            this.mPointerLastY = this.mGestureInitialY;
            this.mIsAbleToDrag = true;
            this.mVelocityTracker = VelocityTracker.obtain();
            this.mVelocityTracker.addMovement(ev);
            this.mScroller.computeScrollOffset();
            if (((this.mScrollState == 2 || this.mScrollState == 3) && this.mScrollAxis == 0 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) || (this.mScrollAxis == 1 && Math.abs(this.mScroller.getFinalY() - this.mScroller.getCurrY()) > this.mCloseEnough)) {
                this.mScroller.abortAnimation();
                this.mDelayPopulate = false;
                populate();
                this.mIsBeingDragged = true;
                requestParentDisallowInterceptTouchEvent(true);
                setScrollState(1);
            } else {
                completeScroll(false);
                this.mIsBeingDragged = false;
            }
        }
        return false;
    }

    private boolean handlePointerMove(MotionEvent ev) {
        float sx;
        float sy;
        int activePointerId = this.mActivePointerId;
        if (activePointerId == -1) {
            return false;
        }
        int pointerIndex = ev.findPointerIndex(activePointerId);
        if (pointerIndex == -1) {
            return this.mIsBeingDragged;
        }
        float x = MotionEventCompat.getX(ev, pointerIndex);
        float y = MotionEventCompat.getY(ev, pointerIndex);
        float dx = x - this.mPointerLastX;
        float xDiff = Math.abs(dx);
        float dy = y - this.mPointerLastY;
        float yDiff = Math.abs(dy);
        if (this.mIsBeingDragged) {
        }
        if (!this.mIsBeingDragged && (xDiff * xDiff) + (yDiff * yDiff) > this.mTouchSlopSquared) {
            this.mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            setScrollState(1);
            if (yDiff >= xDiff) {
                this.mScrollAxis = 1;
            } else {
                this.mScrollAxis = 0;
            }
            if (yDiff > 0.0f && xDiff > 0.0f) {
                double h = Math.hypot(xDiff, yDiff);
                double t = Math.acos(((double) xDiff) / h);
                sy = (float) (Math.sin(t) * ((double) this.mTouchSlop));
                sx = (float) (Math.cos(t) * ((double) this.mTouchSlop));
            } else if (yDiff == 0.0f) {
                sx = this.mTouchSlop;
                sy = 0.0f;
            } else {
                sx = 0.0f;
                sy = this.mTouchSlop;
            }
            this.mPointerLastX = dx > 0.0f ? this.mPointerLastX + sx : this.mPointerLastX - sx;
            this.mPointerLastY = dy > 0.0f ? this.mPointerLastY + sy : this.mPointerLastY - sy;
        }
        if (this.mIsBeingDragged) {
            float dragX = this.mScrollAxis == 0 ? x : this.mPointerLastX;
            float dragY = this.mScrollAxis == 1 ? y : this.mPointerLastY;
            if (performDrag(dragX, dragY)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
        this.mVelocityTracker.addMovement(ev);
        return this.mIsBeingDragged;
    }

    private boolean handlePointerUp(MotionEvent ev) {
        if (!this.mIsBeingDragged || this.mExpectedRowCount == 0) {
            this.mActivePointerId = -1;
            endDrag();
            return false;
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        velocityTracker.addMovement(ev);
        velocityTracker.computeCurrentVelocity(1000);
        int activePointerIndex = ev.findPointerIndex(this.mActivePointerId);
        int targetPageX = this.mCurItem.x;
        int targetPageY = this.mCurItem.y;
        int velocity = 0;
        ItemInfo ii = infoForCurrentScrollPosition();
        switch (this.mScrollAxis) {
            case 0:
                float x = ev.getRawX();
                int totalDeltaX = (int) (x - this.mGestureInitialX);
                velocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                int currentPageX = ii.positionX;
                int distanceX = getRowScrollX(ii.positionY) - computePageLeft(ii.positionX);
                float pageOffsetX = getXIndex(distanceX);
                targetPageX = determineTargetPage(this.mCurItem.x, currentPageX, pageOffsetX, this.mPopulatedPages.left, this.mPopulatedPages.right, velocity, totalDeltaX);
                break;
            case 1:
                ev.getX(activePointerIndex);
                int totalDeltaY = this.mGestureInitialScrollY - getScrollY();
                velocity = (int) velocityTracker.getYVelocity(this.mActivePointerId);
                int currentPageY = ii.positionY;
                int distanceY = getScrollY() - computePageTop(ii.positionY);
                float pageOffsetY = getYIndex(distanceY);
                if (pageOffsetY == 0.0f) {
                    View child = getChildForInfo(infoForCurrentScrollPosition());
                    int scrollable = getScrollableDistance(child, -velocity);
                    if (scrollable != 0) {
                        this.mScrollingContent = child;
                        if (Math.abs(velocity) >= Math.abs(this.mMinFlingVelocity)) {
                            flingContent(0, scrollable, 0, -velocity);
                            endDrag();
                        }
                    }
                } else {
                    targetPageY = determineTargetPage(this.mCurItem.y, currentPageY, pageOffsetY, this.mPopulatedPages.top, this.mPopulatedPages.bottom, velocity, totalDeltaY);
                }
                break;
        }
        if (this.mScrollState != 3) {
            this.mDelayPopulate = true;
            if (targetPageY != this.mCurItem.y) {
                targetPageX = this.mAdapter.getCurrentColumnForRow(targetPageY, this.mCurItem.x);
            }
            setCurrentItemInternal(targetPageY, targetPageX, true, true, velocity);
        }
        this.mActivePointerId = -1;
        endDrag();
        return false;
    }

    private float getXIndex(float distanceX) {
        int width = getContentWidth() + this.mColMargin;
        if (width != 0) {
            return distanceX / width;
        }
        Log.e(TAG, "getXIndex() called with zero width.");
        return 0.0f;
    }

    private float getYIndex(float distanceY) {
        int height = getContentHeight() + this.mRowMargin;
        if (height != 0) {
            return distanceY / height;
        }
        Log.e(TAG, "getYIndex() called with zero height.");
        return 0.0f;
    }

    private int determineTargetPage(int previousPage, int currentPage, float pageOffset, int firstPage, int lastPage, int velocity, int totalDragDistance) {
        int targetPage;
        if (Math.abs(velocity) < this.mMinUsableVelocity) {
            velocity = (int) Math.copySign(velocity, totalDragDistance);
        }
        float flingBoost = (0.5f / Math.max(Math.abs(0.5f - pageOffset), 0.001f)) * 100.0f;
        if (Math.abs(totalDragDistance) > this.mMinFlingDistance && Math.abs(velocity) + flingBoost > this.mMinFlingVelocity) {
            targetPage = velocity > 0 ? currentPage : currentPage + 1;
        } else {
            targetPage = Math.round(currentPage + pageOffset);
        }
        return limit(targetPage, firstPage, lastPage);
    }

    private static int limit(int val, int min, int max) {
        if (val < min) {
            return min;
        }
        return val > max ? max : val;
    }

    private static float limit(float val, float min, float max) {
        if (val < min) {
            return min;
        }
        return val > max ? max : val;
    }

    private static final class DragFrictionInterpolator implements Interpolator {
        private static final float DEFAULT_FALLOFF = 4.0f;
        private final float falloffRate;

        public DragFrictionInterpolator() {
            this(DEFAULT_FALLOFF);
        }

        public DragFrictionInterpolator(float falloffRate) {
            this.falloffRate = falloffRate;
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float input) {
            double e = Math.exp(2.0f * input * this.falloffRate);
            return ((float) ((e - 1.0d) / (1.0d + e))) * (1.0f / this.falloffRate);
        }
    }

    private class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            GridViewPager.this.dataSetChanged();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            GridViewPager.this.dataSetChanged();
        }
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public int gravity;
        public boolean needsMeasure;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, GridViewPager.LAYOUT_ATTRS);
            this.gravity = a.getInteger(0, 48);
            a.recycle();
        }
    }

    public void debug() {
        debug(0);
    }

    @Override // android.view.ViewGroup
    protected void debug(int depth) {
        super.debug(depth);
        String output = debugIndent(depth);
        String strValueOf = String.valueOf(output);
        String strValueOf2 = String.valueOf(this.mCurItem);
        String output2 = new StringBuilder(String.valueOf(strValueOf).length() + 11 + String.valueOf(strValueOf2).length()).append(strValueOf).append("mCurItem={").append(strValueOf2).append("}").toString();
        Log.d("View", output2);
        String output3 = debugIndent(depth);
        String strValueOf3 = String.valueOf(output3);
        String strValueOf4 = String.valueOf(this.mAdapter);
        String output4 = new StringBuilder(String.valueOf(strValueOf3).length() + 11 + String.valueOf(strValueOf4).length()).append(strValueOf3).append("mAdapter={").append(strValueOf4).append("}").toString();
        Log.d("View", output4);
        String output5 = debugIndent(depth);
        String strValueOf5 = String.valueOf(output5);
        String output6 = new StringBuilder(String.valueOf(strValueOf5).length() + 21).append(strValueOf5).append("mRowCount=").append(this.mExpectedRowCount).toString();
        Log.d("View", output6);
        String output7 = debugIndent(depth);
        String strValueOf6 = String.valueOf(output7);
        String output8 = new StringBuilder(String.valueOf(strValueOf6).length() + 31).append(strValueOf6).append("mCurrentColumnCount=").append(this.mExpectedCurrentColumnCount).toString();
        Log.d("View", output8);
        int count = this.mItems.size();
        if (count != 0) {
            String output9 = debugIndent(depth);
            Log.d("View", String.valueOf(output9).concat("mItems={"));
        }
        for (int i = 0; i < count; i++) {
            String output10 = debugIndent(depth + 1);
            String strValueOf7 = String.valueOf(output10);
            String strValueOf8 = String.valueOf(this.mItems.keyAt(i));
            String strValueOf9 = String.valueOf(this.mItems.valueAt(i));
            String output11 = new StringBuilder(String.valueOf(strValueOf7).length() + 4 + String.valueOf(strValueOf8).length() + String.valueOf(strValueOf9).length()).append(strValueOf7).append(strValueOf8).append(" => ").append(strValueOf9).toString();
            Log.d("View", output11);
        }
        if (count != 0) {
            String output12 = debugIndent(depth);
            Log.d("View", String.valueOf(output12).concat("}"));
        }
    }

    private static String debugIndent(int depth) {
        StringBuilder spaces = new StringBuilder(((depth * 2) + 3) * 2);
        for (int i = 0; i < (depth * 2) + 3; i++) {
            spaces.append(' ').append(' ');
        }
        return spaces.toString();
    }

    private static boolean inRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    private boolean pointInRange(int x, int y) {
        return inRange(y, 0, this.mExpectedRowCount + (-1)) && inRange(x, 0, this.mAdapter.getColumnCount(y) + (-1));
    }
}
