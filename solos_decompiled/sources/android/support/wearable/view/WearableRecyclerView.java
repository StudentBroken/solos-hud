package android.support.wearable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.R;
import android.support.wearable.input.RotaryEncoder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class WearableRecyclerView extends RecyclerView {
    private static final int NO_VALUE = Integer.MIN_VALUE;
    private static final String TAG = WearableRecyclerView.class.getSimpleName();
    private boolean mCenterEdgeItems;
    private boolean mCenterEdgeItemsWhenThereAreChildren;
    private boolean mCircularScrollingEnabled;
    private OffsettingHelper mOffsettingHelper;
    private int mOriginalPaddingBottom;
    private int mOriginalPaddingTop;
    private final ViewTreeObserver.OnPreDrawListener mPaddingPreDrawListener;
    private final ScrollManager mScrollManager;

    @Deprecated
    public static abstract class OffsettingHelper {
        public abstract void updateChild(View view, WearableRecyclerView wearableRecyclerView);
    }

    public static abstract class ChildLayoutManager extends LinearLayoutManager {
        public abstract void updateChild(View view, WearableRecyclerView wearableRecyclerView);

        public ChildLayoutManager(Context context) {
            super(context, 1, false);
        }

        @Override // android.support.v7.widget.LinearLayoutManager, android.support.v7.widget.RecyclerView.LayoutManager
        public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            updateLayout();
            return scrolled;
        }

        @Override // android.support.v7.widget.LinearLayoutManager, android.support.v7.widget.RecyclerView.LayoutManager
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            super.onLayoutChildren(recycler, state);
            if (getChildCount() != 0) {
                updateLayout();
            }
        }

        private void updateLayout() {
            for (int count = 0; count < getChildCount(); count++) {
                View child = getChildAt(count);
                updateChild(child, (WearableRecyclerView) child.getParent());
            }
        }
    }

    public WearableRecyclerView(Context context) {
        this(context, null);
    }

    public WearableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mScrollManager = new ScrollManager();
        this.mOriginalPaddingTop = Integer.MIN_VALUE;
        this.mOriginalPaddingBottom = Integer.MIN_VALUE;
        this.mPaddingPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: android.support.wearable.view.WearableRecyclerView.1
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                if (WearableRecyclerView.this.mCenterEdgeItemsWhenThereAreChildren && WearableRecyclerView.this.getChildCount() > 0) {
                    WearableRecyclerView.this.setupCenteredPadding();
                    WearableRecyclerView.this.mCenterEdgeItemsWhenThereAreChildren = false;
                    return true;
                }
                return true;
            }
        };
        setHasFixedSize(true);
        setClipToPadding(false);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView, defStyle, 0);
            setCircularScrollingGestureEnabled(a.getBoolean(R.styleable.WearableRecyclerView_circular_scrolling_gesture_enabled, this.mCircularScrollingEnabled));
            setBezelWidth(a.getFloat(R.styleable.WearableRecyclerView_bezel_width, this.mScrollManager.getBezelWidth()));
            setScrollDegreesPerScreen(a.getFloat(R.styleable.WearableRecyclerView_scroll_degrees_per_screen, this.mScrollManager.getScrollDegreesPerScreen()));
            a.recycle();
        }
        setLayoutManager(new OffsettingLinearLayoutManager(getContext()));
    }

    @Override // android.support.v7.widget.RecyclerView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mCircularScrollingEnabled && this.mScrollManager.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override // android.support.v7.widget.RecyclerView, android.view.View
    public boolean onGenericMotionEvent(MotionEvent ev) {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || isLayoutFrozen()) {
            return false;
        }
        if (ev.getAction() == 8 && RotaryEncoder.isFromRotaryEncoder(ev)) {
            int delta = Math.round((-RotaryEncoder.getRotaryAxisValue(ev)) * RotaryEncoder.getScaledScrollFactor(getContext()));
            if (layoutManager.canScrollVertically()) {
                scrollBy(0, delta);
                return true;
            }
            if (layoutManager.canScrollHorizontally()) {
                scrollBy(delta, 0);
                return true;
            }
        }
        return super.onGenericMotionEvent(ev);
    }

    @Override // android.support.v7.widget.RecyclerView, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mScrollManager.setRecyclerView(this);
        getViewTreeObserver().addOnPreDrawListener(this.mPaddingPreDrawListener);
    }

    @Override // android.support.v7.widget.RecyclerView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mScrollManager.clearRecyclerView();
        getViewTreeObserver().removeOnPreDrawListener(this.mPaddingPreDrawListener);
    }

    public void setScrollDegreesPerScreen(float degreesPerScreen) {
        this.mScrollManager.setScrollDegreesPerScreen(degreesPerScreen);
    }

    public float getScrollDegreesPerScreen() {
        return this.mScrollManager.getScrollDegreesPerScreen();
    }

    public void setBezelWidth(float fraction) {
        this.mScrollManager.setBezelWidth(fraction);
    }

    public float getBezelWidth() {
        return this.mScrollManager.getBezelWidth();
    }

    public void setCircularScrollingGestureEnabled(boolean circularScrollingGestureEnabled) {
        this.mCircularScrollingEnabled = circularScrollingGestureEnabled;
    }

    public boolean isCircularScrollingGestureEnabled() {
        return this.mCircularScrollingEnabled;
    }

    @Deprecated
    public void setOffsettingHelper(@Nullable OffsettingHelper offsettingHelper) {
        this.mOffsettingHelper = offsettingHelper;
    }

    @Deprecated
    public void clearOffsettingHelper() {
        setOffsettingHelper(null);
    }

    @Nullable
    @Deprecated
    public OffsettingHelper getOffsettingHelper() {
        return this.mOffsettingHelper;
    }

    public void setCenterEdgeItems(boolean centerEdgeItems) {
        this.mCenterEdgeItems = centerEdgeItems;
        if (this.mCenterEdgeItems) {
            if (getChildCount() > 0) {
                setupCenteredPadding();
                return;
            } else {
                this.mCenterEdgeItemsWhenThereAreChildren = true;
                return;
            }
        }
        setupOriginalPadding();
        this.mCenterEdgeItemsWhenThereAreChildren = false;
    }

    public boolean getCenterEdgeItems() {
        return this.mCenterEdgeItems;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupCenteredPadding() {
        if (!this.mCenterEdgeItems || getChildCount() < 1) {
            Log.w(TAG, "No children available");
            return;
        }
        View child = getChildAt(0);
        int height = child.getHeight();
        int desiredPadding = (int) ((getHeight() * 0.5f) - (height * 0.5f));
        if (getPaddingTop() != desiredPadding) {
            this.mOriginalPaddingTop = getPaddingTop();
            this.mOriginalPaddingBottom = getPaddingBottom();
            setPadding(getPaddingLeft(), desiredPadding, getPaddingRight(), desiredPadding);
            View focusedChild = getFocusedChild();
            int focusedPosition = focusedChild != null ? getLayoutManager().getPosition(focusedChild) : 0;
            getLayoutManager().scrollToPosition(focusedPosition);
        }
    }

    private void setupOriginalPadding() {
        if (this.mOriginalPaddingTop != Integer.MIN_VALUE) {
            setPadding(getPaddingLeft(), this.mOriginalPaddingTop, getPaddingRight(), this.mOriginalPaddingBottom);
        }
    }

    @Deprecated
    private final class OffsettingLinearLayoutManager extends LinearLayoutManager {
        public OffsettingLinearLayoutManager(Context context) {
            super(context, 1, false);
        }

        @Override // android.support.v7.widget.LinearLayoutManager, android.support.v7.widget.RecyclerView.LayoutManager
        public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            updateLayout();
            return scrolled;
        }

        @Override // android.support.v7.widget.LinearLayoutManager, android.support.v7.widget.RecyclerView.LayoutManager
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            super.onLayoutChildren(recycler, state);
            if (getChildCount() != 0) {
                updateLayout();
            }
        }

        private void updateLayout() {
            if (WearableRecyclerView.this.mOffsettingHelper != null) {
                for (int count = 0; count < getChildCount(); count++) {
                    View child = getChildAt(count);
                    WearableRecyclerView.this.mOffsettingHelper.updateChild(child, WearableRecyclerView.this);
                }
            }
        }
    }
}
