package android.support.wearable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v4.view.ViewCompat;
import android.support.wearable.R;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public class CardFrame extends ViewGroup {
    private static final float BOX_FACTOR = 0.146467f;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_CONTENT_PADDING_DP = 12;
    private static final int DEFAULT_CONTENT_PADDING_TOP_DP = 8;
    private static final int EDGE_FADE_DISTANCE_DP = 40;
    public static final int EXPAND_DOWN = 1;
    public static final int EXPAND_UP = -1;
    public static final float NO_EXPANSION = 1.0f;
    private static final String TAG = "CardFrame";
    private int mBoxInset;
    private boolean mCanExpand;
    private int mCardBaseHeight;
    private final Rect mChildClipBounds;
    private final Rect mContentPadding;
    private final EdgeFade mEdgeFade;
    private final int mEdgeFadeDistance;
    private int mExpansionDirection;
    private boolean mExpansionEnabled;
    private float mExpansionFactor;
    private boolean mHasBottomInset;
    private final Rect mInsetPadding;
    private boolean mRoundDisplay;

    public CardFrame(Context context) {
        this(context, null, 0);
    }

    public CardFrame(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardFrame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mExpansionEnabled = true;
        this.mExpansionFactor = 1.0f;
        this.mExpansionDirection = 1;
        this.mChildClipBounds = new Rect();
        this.mInsetPadding = new Rect();
        this.mContentPadding = new Rect();
        this.mEdgeFade = new EdgeFade();
        Resources res = context.getResources();
        float density = res.getDisplayMetrics().density;
        this.mEdgeFadeDistance = (int) (40.0f * density);
        setBackgroundResource(R.drawable.card_background);
        int defaultContentPadding = (int) (12.0f * density);
        int defaultContentPaddingTop = (int) (8.0f * density);
        setContentPadding(defaultContentPadding, defaultContentPaddingTop, defaultContentPadding, defaultContentPadding);
    }

    public void setContentPadding(int left, int top, int right, int bottom) {
        this.mContentPadding.set(left, top, right, bottom);
        requestLayout();
    }

    public int getContentPaddingLeft() {
        return this.mContentPadding.left;
    }

    public int getContentPaddingRight() {
        return this.mContentPadding.right;
    }

    public int getContentPaddingTop() {
        return this.mContentPadding.top;
    }

    public int getContentPaddingBottom() {
        return this.mContentPadding.bottom;
    }

    public void setExpansionEnabled(boolean enabled) {
        this.mExpansionEnabled = enabled;
        requestLayout();
        invalidate();
    }

    public void setExpansionDirection(int direction) {
        this.mExpansionDirection = direction;
        requestLayout();
        invalidate();
    }

    public void setExpansionFactor(float expansionFactor) {
        this.mExpansionFactor = expansionFactor;
        requestLayout();
        invalidate();
    }

    public int getExpansionDirection() {
        return this.mExpansionDirection;
    }

    public boolean isExpansionEnabled() {
        return this.mExpansionEnabled;
    }

    public float getExpansionFactor() {
        return this.mExpansionFactor;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestApplyInsets();
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        boolean round = insets.isRound();
        if (round != this.mRoundDisplay) {
            this.mRoundDisplay = round;
            requestLayout();
        }
        boolean inset = insets.getSystemWindowInsetBottom() > 0;
        if (inset != this.mHasBottomInset) {
            this.mHasBottomInset = inset;
            requestLayout();
        }
        return insets.consumeSystemWindowInsets();
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cardMeasuredHeight;
        int childHeightMeasureSpecMode;
        int childHeightMeasureSpecSize;
        int cardMeasuredHeight2;
        int logicalWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int logicalHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        if (this.mRoundDisplay) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
            this.mInsetPadding.setEmpty();
            int outsetLeft = 0;
            int outsetBottom = 0;
            int outsetRight = 0;
            if (lp.leftMargin < 0) {
                outsetLeft = -lp.leftMargin;
                logicalWidth -= outsetLeft;
            }
            if (lp.rightMargin < 0) {
                outsetRight = -lp.rightMargin;
                logicalWidth -= outsetRight;
            }
            if (lp.bottomMargin < 0) {
                outsetBottom = -lp.bottomMargin;
                logicalHeight -= outsetBottom;
            }
            this.mBoxInset = (int) (BOX_FACTOR * Math.max(logicalWidth, logicalHeight));
            this.mInsetPadding.left = this.mBoxInset - (getPaddingLeft() - outsetLeft);
            this.mInsetPadding.right = this.mBoxInset - (getPaddingRight() - outsetRight);
            if (!this.mHasBottomInset) {
                this.mInsetPadding.bottom = this.mBoxInset - (getPaddingBottom() - outsetBottom);
            }
        }
        int cardMeasuredWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec, true);
        int cardMeasuredHeight3 = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec, false);
        if (getChildCount() == 0) {
            setMeasuredDimension(cardMeasuredWidth, cardMeasuredHeight3);
            return;
        }
        View content = getChildAt(0);
        int parentHeightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int parentHeightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        boolean cardHeightMatchContent = false;
        this.mCanExpand = this.mExpansionEnabled;
        if (parentHeightMode == 0 || parentHeightSize == 0) {
            Log.w(TAG, "height measure spec passed with mode UNSPECIFIED, or zero height.");
            this.mCanExpand = false;
            this.mCardBaseHeight = 0;
            cardMeasuredHeight = 0;
            cardHeightMatchContent = true;
            childHeightMeasureSpecMode = 0;
            childHeightMeasureSpecSize = 0;
        } else if (parentHeightMode == 1073741824) {
            Log.w(TAG, "height measure spec passed with mode EXACT");
            this.mCanExpand = false;
            this.mCardBaseHeight = parentHeightSize;
            cardMeasuredHeight = this.mCardBaseHeight;
            childHeightMeasureSpecMode = 1073741824;
            childHeightMeasureSpecSize = cardMeasuredHeight;
        } else {
            this.mCardBaseHeight = parentHeightSize;
            cardMeasuredHeight = this.mCardBaseHeight;
            if (this.mCanExpand) {
                cardMeasuredHeight = (int) (cardMeasuredHeight * this.mExpansionFactor);
            }
            if (this.mExpansionDirection == -1) {
                childHeightMeasureSpecMode = 0;
                childHeightMeasureSpecSize = 0;
            } else {
                childHeightMeasureSpecMode = Integer.MIN_VALUE;
                int childHeightMeasureSpecSize2 = cardMeasuredHeight;
                childHeightMeasureSpecSize = childHeightMeasureSpecSize2 + getPaddingBottom();
            }
        }
        int paddingWidth = getPaddingLeft() + getPaddingRight() + this.mContentPadding.left + this.mContentPadding.right + this.mInsetPadding.left + this.mInsetPadding.right;
        int paddingHeight = getPaddingTop() + getPaddingBottom() + this.mContentPadding.top + this.mContentPadding.bottom + this.mInsetPadding.top + this.mInsetPadding.bottom;
        int childWidthSpec = View.MeasureSpec.makeMeasureSpec(cardMeasuredWidth - paddingWidth, 1073741824);
        int childHeightSpec = View.MeasureSpec.makeMeasureSpec(childHeightMeasureSpecSize - paddingHeight, childHeightMeasureSpecMode);
        content.measure(getChildMeasureSpec(childWidthSpec, 0, content.getLayoutParams().width), childHeightSpec);
        if (cardHeightMatchContent) {
            cardMeasuredHeight2 = content.getMeasuredHeight() + paddingHeight;
        } else {
            cardMeasuredHeight2 = Math.min(cardMeasuredHeight, content.getMeasuredHeight() + paddingHeight);
            this.mCanExpand = (content.getMeasuredHeight() > cardMeasuredHeight2 - paddingHeight) & this.mCanExpand;
        }
        setMeasuredDimension(cardMeasuredWidth, cardMeasuredHeight2);
    }

    public static int getDefaultSize(int size, int measureSpec, boolean greedy) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Integer.MIN_VALUE:
                if (!greedy) {
                }
                break;
            case 0:
                break;
            case 1073741824:
                break;
        }
        return size;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int t;
        int b;
        if (getChildCount() != 0) {
            View content = getChildAt(0);
            int parentHeight = bottom - top;
            int l = getPaddingLeft() + this.mInsetPadding.left + this.mContentPadding.left;
            int r = l + content.getMeasuredWidth();
            if (this.mExpansionDirection == -1) {
                b = parentHeight;
                t = b - (((content.getMeasuredHeight() + getPaddingBottom()) + this.mInsetPadding.bottom) + this.mContentPadding.bottom);
            } else {
                t = getPaddingTop() + this.mInsetPadding.top + this.mContentPadding.top;
                b = t + content.getMeasuredHeight();
            }
            content.layout(l, t, r, b);
        }
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int fadeDistance = this.mEdgeFadeDistance;
        boolean bottomFade = false;
        boolean topFade = false;
        this.mChildClipBounds.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        int paddingHeight = getPaddingTop() + getPaddingBottom();
        int contentHeight = child.getHeight();
        if (this.mCanExpand) {
            if (this.mExpansionDirection == -1 && contentHeight + paddingHeight > getHeight()) {
                topFade = true;
                this.mChildClipBounds.top = getPaddingTop();
            } else if (this.mExpansionDirection == 1 && contentHeight + paddingHeight > getHeight()) {
                bottomFade = true;
                this.mChildClipBounds.bottom = getHeight() - getPaddingBottom();
            }
        }
        int saveCount = canvas.getSaveCount();
        canvas.clipRect(this.mChildClipBounds);
        if (topFade) {
            canvas.saveLayer(this.mChildClipBounds.left, this.mChildClipBounds.top, this.mChildClipBounds.right, this.mChildClipBounds.top + fadeDistance, null, 4);
        }
        if (bottomFade) {
            canvas.saveLayer(this.mChildClipBounds.left, this.mChildClipBounds.bottom - fadeDistance, this.mChildClipBounds.right, this.mChildClipBounds.bottom, null, 4);
        }
        boolean more = super.drawChild(canvas, child, drawingTime);
        if (topFade) {
            this.mEdgeFade.matrix.reset();
            this.mEdgeFade.matrix.setScale(1.0f, fadeDistance);
            this.mEdgeFade.matrix.postTranslate(this.mChildClipBounds.left, this.mChildClipBounds.top);
            this.mEdgeFade.shader.setLocalMatrix(this.mEdgeFade.matrix);
            this.mEdgeFade.paint.setShader(this.mEdgeFade.shader);
            canvas.drawRect(this.mChildClipBounds.left, this.mChildClipBounds.top, this.mChildClipBounds.right, this.mChildClipBounds.top + fadeDistance, this.mEdgeFade.paint);
        }
        if (bottomFade) {
            this.mEdgeFade.matrix.reset();
            this.mEdgeFade.matrix.setScale(1.0f, fadeDistance);
            this.mEdgeFade.matrix.postRotate(180.0f);
            this.mEdgeFade.matrix.postTranslate(this.mChildClipBounds.left, this.mChildClipBounds.bottom);
            this.mEdgeFade.shader.setLocalMatrix(this.mEdgeFade.matrix);
            this.mEdgeFade.paint.setShader(this.mEdgeFade.shader);
            canvas.drawRect(this.mChildClipBounds.left, this.mChildClipBounds.bottom - fadeDistance, this.mChildClipBounds.right, this.mChildClipBounds.bottom, this.mEdgeFade.paint);
        }
        canvas.restoreToCount(saveCount);
        return more;
    }

    @Override // android.view.ViewGroup
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("CardFrame can host only one direct child");
        }
        super.addView(child);
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("CardFrame can host only one direct child");
        }
        super.addView(child, index);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("CardFrame can host only one direct child");
        }
        super.addView(child, params);
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("CardFrame can host only one direct child");
        }
        super.addView(child, index, params);
    }

    private static class EdgeFade {
        private final Matrix matrix = new Matrix();
        private final Shader shader = new LinearGradient(0.0f, 0.0f, 0.0f, 1.0f, ViewCompat.MEASURED_STATE_MASK, 0, Shader.TileMode.CLAMP);
        private final Paint paint = new Paint();

        public EdgeFade() {
            this.paint.setShader(this.shader);
            this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        }
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(CardFrame.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(CardFrame.class.getName());
    }
}
