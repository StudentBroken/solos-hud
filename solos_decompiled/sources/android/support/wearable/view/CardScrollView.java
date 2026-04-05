package android.support.wearable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public class CardScrollView extends FrameLayout {
    private static final int CARD_SHADOW_WIDTH_DP = 8;
    private static final boolean DEBUG = false;
    private static final String TAG = "CardScrollView";
    private CardFrame mCardFrame;
    private final int mCardShadowWidth;
    private boolean mRoundDisplay;

    public CardScrollView(Context context) {
        this(context, null);
    }

    public CardScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.mCardShadowWidth = (int) (8.0f * getResources().getDisplayMetrics().density);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestApplyInsets();
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        boolean round = insets.isRound();
        if (this.mRoundDisplay != round) {
            this.mRoundDisplay = round;
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.mCardFrame.getLayoutParams();
            lp.leftMargin = -this.mCardShadowWidth;
            lp.rightMargin = -this.mCardShadowWidth;
            lp.bottomMargin = -this.mCardShadowWidth;
            this.mCardFrame.setLayoutParams(lp);
        }
        if (insets.getSystemWindowInsetBottom() > 0) {
            int bottomInset = insets.getSystemWindowInsetBottom();
            ViewGroup.LayoutParams lp2 = getLayoutParams();
            if (lp2 instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) lp2).bottomMargin = bottomInset;
            }
        }
        if (this.mRoundDisplay && this.mCardFrame != null) {
            this.mCardFrame.onApplyWindowInsets(insets);
        }
        requestLayout();
        return insets;
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0 || !(child instanceof CardFrame)) {
            throw new IllegalStateException("CardScrollView may contain only a single CardFrame.");
        }
        super.addView(child, index, params);
        this.mCardFrame = (CardFrame) child;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0 || !(getChildAt(0) instanceof CardFrame)) {
            Log.w(TAG, "No CardFrame has been added!");
        }
    }

    private boolean hasCardFrame() {
        if (this.mCardFrame != null) {
            return true;
        }
        Log.w(TAG, "No CardFrame has been added.");
        return false;
    }

    public void setExpansionEnabled(boolean enableExpansion) {
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, new StringBuilder(26).append("setExpansionEnabled: ").append(enableExpansion).toString());
        }
        if (hasCardFrame()) {
            boolean wasEnabled = this.mCardFrame.isExpansionEnabled();
            if (enableExpansion != wasEnabled) {
                this.mCardFrame.setExpansionEnabled(enableExpansion);
                if (!enableExpansion) {
                    scrollTo(0, 0);
                }
            }
        }
    }

    public boolean isExpansionEnabled() {
        if (hasCardFrame()) {
            return this.mCardFrame.isExpansionEnabled();
        }
        return false;
    }

    public void setExpansionDirection(int direction) {
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, new StringBuilder(34).append("setExpansionDirection: ").append(direction).toString());
        }
        if (hasCardFrame()) {
            int curDirection = this.mCardFrame.getExpansionDirection();
            if (direction != curDirection) {
                this.mCardFrame.setExpansionDirection(direction);
                if (direction == 1 && getScrollY() < 0) {
                    scrollTo(0, 0);
                } else if (direction == -1 && getScrollY() > 0) {
                    scrollTo(0, 0);
                }
                requestLayout();
            }
        }
    }

    public float getExpansionFactor() {
        if (hasCardFrame()) {
            return this.mCardFrame.getExpansionFactor();
        }
        return 0.0f;
    }

    public void setExpansionFactor(float expansionFactor) {
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, new StringBuilder(35).append("setExpansionFactor: ").append(expansionFactor).toString());
        }
        if (hasCardFrame()) {
            this.mCardFrame.setExpansionFactor(expansionFactor);
        }
    }

    public int getExpansionDirection() {
        if (hasCardFrame()) {
            return this.mCardFrame.getExpansionDirection();
        }
        return 0;
    }

    public void setCardGravity(int gravity) {
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, new StringBuilder(27).append("setCardGravity: ").append(gravity).toString());
        }
        if (hasCardFrame()) {
            int gravity2 = gravity & 112;
            FrameLayout.LayoutParams existing = (FrameLayout.LayoutParams) this.mCardFrame.getLayoutParams();
            if (existing.gravity != gravity2) {
                this.mCardFrame.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, gravity2));
                requestLayout();
            }
        }
    }

    public int getCardGravity() {
        if (!hasCardFrame()) {
            return 0;
        }
        FrameLayout.LayoutParams existing = (FrameLayout.LayoutParams) this.mCardFrame.getLayoutParams();
        return existing.gravity;
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    public int getAvailableScrollDelta(int direction) {
        if (!hasCardFrame()) {
            return 0;
        }
        int paddingHeight = getPaddingTop() + getPaddingBottom();
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.mCardFrame.getLayoutParams();
        int marginHeight = lp.topMargin + lp.bottomMargin;
        int cardVerticalSpan = this.mCardFrame.getMeasuredHeight() + paddingHeight + marginHeight;
        if (cardVerticalSpan <= getMeasuredHeight()) {
            return 0;
        }
        int extra = cardVerticalSpan - getMeasuredHeight();
        int avail = 0;
        int sy = getScrollY();
        if (this.mCardFrame.getExpansionDirection() == 1) {
            if (sy >= 0) {
                if (direction < 0) {
                    avail = -sy;
                } else if (direction > 0) {
                    avail = Math.max(0, extra - sy);
                }
            }
        } else if (this.mCardFrame.getExpansionDirection() == -1 && sy <= 0) {
            if (direction > 0) {
                avail = -sy;
            } else if (direction < 0) {
                avail = -(extra + sy);
            }
        }
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, new StringBuilder(42).append("getVerticalScrollableDistance: ").append(Math.max(0, avail)).toString());
            return avail;
        }
        return avail;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mCardFrame != null) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) this.mCardFrame.getLayoutParams();
            int paddingWidth = getPaddingLeft() + getPaddingRight();
            int paddingHeight = getPaddingTop() + getPaddingBottom();
            int availableHeight = View.MeasureSpec.getSize(heightMeasureSpec) - paddingHeight;
            int availableWidth = View.MeasureSpec.getSize(widthMeasureSpec) - paddingWidth;
            int availableWidth2 = availableWidth - (lp.leftMargin + lp.rightMargin);
            int availableHeight2 = availableHeight - (lp.topMargin + lp.bottomMargin);
            int widthSpec = View.MeasureSpec.makeMeasureSpec(availableWidth2, 1073741824);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(availableHeight2, Integer.MIN_VALUE);
            this.mCardFrame.measure(widthSpec, heightSpec);
        }
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize(getSuggestedMinimumWidth(), heightMeasureSpec));
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        boolean alignBottom;
        if (this.mCardFrame != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.mCardFrame.getLayoutParams();
            int cardHeight = this.mCardFrame.getMeasuredHeight();
            int cardWidth = this.mCardFrame.getMeasuredWidth();
            int parentHeight = bottom - top;
            if (getPaddingTop() + cardHeight + lp.topMargin <= parentHeight) {
                alignBottom = (lp.gravity & 112) == 80;
            } else {
                int dir = this.mCardFrame.getExpansionDirection();
                alignBottom = dir == -1;
            }
            int l = getPaddingLeft() + lp.leftMargin;
            int t = getPaddingTop() + lp.topMargin;
            int r = l + cardWidth;
            int b = t + cardHeight;
            if (alignBottom) {
                b = parentHeight - (getPaddingBottom() + lp.bottomMargin);
                t = b - cardHeight;
            }
            this.mCardFrame.layout(l, t, r, b);
        }
    }

    int roundAwayFromZero(float v) {
        return (int) (v < 0.0f ? Math.floor(v) : Math.ceil(v));
    }

    int roundTowardZero(float v) {
        return (int) (v > 0.0f ? Math.floor(v) : Math.ceil(v));
    }
}
