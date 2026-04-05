package android.support.wearable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.wearable.R;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import com.nuance.android.vocalizer.VocalizerEngine;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
public class BoxInsetLayout extends FrameLayout {
    private static final int DEFAULT_CHILD_GRAVITY = 8388659;
    private static final float FACTOR = 0.146467f;
    private Rect mForegroundPadding;
    private Rect mInsets;
    private boolean mLastKnownRound;
    private final int mScreenHeight;
    private final int mScreenWidth;

    public BoxInsetLayout(Context context) {
        this(context, null);
    }

    public BoxInsetLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoxInsetLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (this.mForegroundPadding == null) {
            this.mForegroundPadding = new Rect();
        }
        if (this.mInsets == null) {
            this.mInsets = new Rect();
        }
        this.mScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        this.mScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT < 23) {
            requestApplyInsets();
            return;
        }
        this.mLastKnownRound = getResources().getConfiguration().isScreenRound();
        WindowInsets insets = getRootWindowInsets();
        this.mInsets.set(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        WindowInsets insets2 = super.onApplyWindowInsets(insets);
        if (Build.VERSION.SDK_INT < 23) {
            boolean round = insets2.isRound();
            if (round != this.mLastKnownRound) {
                this.mLastKnownRound = round;
                requestLayout();
            }
            this.mInsets.set(insets2.getSystemWindowInsetLeft(), insets2.getSystemWindowInsetTop(), insets2.getSystemWindowInsetRight(), insets2.getSystemWindowInsetBottom());
        }
        return insets2;
    }

    public boolean isRound() {
        return this.mLastKnownRound;
    }

    public Rect getInsets() {
        return this.mInsets;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxWidth = 0;
        int maxHeight = 0;
        int childState = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int marginLeft = 0;
                int marginRight = 0;
                int marginTop = 0;
                int marginBottom = 0;
                if (this.mLastKnownRound) {
                    if ((lp.boxedEdges & 1) == 0) {
                        marginLeft = lp.leftMargin;
                    }
                    if ((lp.boxedEdges & 4) == 0) {
                        marginRight = lp.rightMargin;
                    }
                    if ((lp.boxedEdges & 2) == 0) {
                        marginTop = lp.topMargin;
                    }
                    if ((lp.boxedEdges & 8) == 0) {
                        marginBottom = lp.bottomMargin;
                    }
                } else {
                    marginLeft = lp.leftMargin;
                    marginTop = lp.topMargin;
                    marginRight = lp.rightMargin;
                    marginBottom = lp.bottomMargin;
                }
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + marginLeft + marginRight);
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + marginTop + marginBottom);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }
        int maxWidth2 = maxWidth + getPaddingLeft() + this.mForegroundPadding.left + getPaddingRight() + this.mForegroundPadding.right;
        int maxHeight2 = Math.max(maxHeight + getPaddingTop() + this.mForegroundPadding.top + getPaddingBottom() + this.mForegroundPadding.bottom, getSuggestedMinimumHeight());
        int maxWidth3 = Math.max(maxWidth2, getSuggestedMinimumWidth());
        Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight2 = Math.max(maxHeight2, drawable.getMinimumHeight());
            maxWidth3 = Math.max(maxWidth3, drawable.getMinimumWidth());
        }
        int measuredWidth = resolveSizeAndState(maxWidth3, widthMeasureSpec, childState);
        int measuredHeight = resolveSizeAndState(maxHeight2, heightMeasureSpec, childState << 16);
        setMeasuredDimension(measuredWidth, measuredHeight);
        int boxInset = calculateInset();
        for (int i2 = 0; i2 < count; i2++) {
            measureChild(widthMeasureSpec, heightMeasureSpec, boxInset, i2);
        }
    }

    private void measureChild(int widthMeasureSpec, int heightMeasureSpec, int desiredMinInset, int i) {
        View child = getChildAt(i);
        LayoutParams childLayoutParams = (LayoutParams) child.getLayoutParams();
        int gravity = childLayoutParams.gravity;
        if (gravity == -1) {
            gravity = DEFAULT_CHILD_GRAVITY;
        }
        int verticalGravity = gravity & 112;
        int horizontalGravity = gravity & 7;
        int leftParentPadding = getPaddingLeft() + this.mForegroundPadding.left;
        int rightParentPadding = getPaddingRight() + this.mForegroundPadding.right;
        int topParentPadding = getPaddingTop() + this.mForegroundPadding.top;
        int bottomParentPadding = getPaddingBottom() + this.mForegroundPadding.bottom;
        int totalWidthMargin = leftParentPadding + rightParentPadding + calculateChildLeftMargin(childLayoutParams, horizontalGravity, desiredMinInset) + calculateChildRightMargin(childLayoutParams, horizontalGravity, desiredMinInset);
        int totalHeightMargin = topParentPadding + bottomParentPadding + calculateChildTopMargin(childLayoutParams, verticalGravity, desiredMinInset) + calculateChildBottomMargin(childLayoutParams, verticalGravity, desiredMinInset);
        int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, totalWidthMargin, childLayoutParams.width);
        int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, totalHeightMargin, childLayoutParams.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childLeft;
        int childTop;
        int count = getChildCount();
        int parentLeft = getPaddingLeft() + this.mForegroundPadding.left;
        int parentRight = ((right - left) - getPaddingRight()) - this.mForegroundPadding.right;
        int parentTop = getPaddingTop() + this.mForegroundPadding.top;
        int parentBottom = ((bottom - top) - getPaddingBottom()) - this.mForegroundPadding.bottom;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int gravity = lp.gravity;
                if (gravity == -1) {
                    gravity = DEFAULT_CHILD_GRAVITY;
                }
                int layoutDirection = getLayoutDirection();
                int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                int verticalGravity = gravity & 112;
                int horizontalGravity = gravity & 7;
                int desiredInset = calculateInset();
                int leftChildMargin = calculateChildLeftMargin(lp, horizontalGravity, desiredInset);
                int rightChildMargin = calculateChildRightMargin(lp, horizontalGravity, desiredInset);
                if (lp.width == -1) {
                    childLeft = parentLeft + leftChildMargin;
                } else {
                    switch (absoluteGravity & 7) {
                        case 1:
                            childLeft = (((((parentRight - parentLeft) - width) / 2) + parentLeft) + leftChildMargin) - rightChildMargin;
                            break;
                        case 5:
                            childLeft = (parentRight - width) - rightChildMargin;
                            break;
                        default:
                            childLeft = parentLeft + leftChildMargin;
                            break;
                    }
                }
                int topChildMargin = calculateChildTopMargin(lp, verticalGravity, desiredInset);
                int bottomChildMargin = calculateChildBottomMargin(lp, verticalGravity, desiredInset);
                if (lp.height == -1) {
                    childTop = parentTop + topChildMargin;
                } else {
                    switch (verticalGravity) {
                        case 16:
                            childTop = (((((parentBottom - parentTop) - height) / 2) + parentTop) + topChildMargin) - bottomChildMargin;
                            break;
                        case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                            childTop = (parentBottom - height) - bottomChildMargin;
                            break;
                        default:
                            childTop = parentTop + topChildMargin;
                            break;
                    }
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    private int calculateChildLeftMargin(LayoutParams lp, int horizontalGravity, int desiredMinInset) {
        return (this.mLastKnownRound && (lp.boxedEdges & 1) != 0 && (lp.width == -1 || horizontalGravity == 3)) ? lp.leftMargin + desiredMinInset : lp.leftMargin;
    }

    private int calculateChildRightMargin(LayoutParams lp, int horizontalGravity, int desiredMinInset) {
        return (this.mLastKnownRound && (lp.boxedEdges & 4) != 0 && (lp.width == -1 || horizontalGravity == 5)) ? lp.rightMargin + desiredMinInset : lp.rightMargin;
    }

    private int calculateChildTopMargin(LayoutParams lp, int verticalGravity, int desiredMinInset) {
        return (this.mLastKnownRound && (lp.boxedEdges & 2) != 0 && (lp.height == -1 || verticalGravity == 48)) ? lp.topMargin + desiredMinInset : lp.topMargin;
    }

    private int calculateChildBottomMargin(LayoutParams lp, int verticalGravity, int desiredMinInset) {
        return (this.mLastKnownRound && (lp.boxedEdges & 8) != 0 && (lp.height == -1 || verticalGravity == 80)) ? lp.bottomMargin + desiredMinInset : lp.bottomMargin;
    }

    private int calculateInset() {
        int rightEdge = Math.min(getMeasuredWidth(), this.mScreenWidth);
        int bottomEdge = Math.min(getMeasuredHeight(), this.mScreenHeight);
        return (int) (FACTOR * Math.max(rightEdge, bottomEdge));
    }

    @Override // android.view.View
    public void setForeground(Drawable drawable) {
        super.setForeground(drawable);
        if (this.mForegroundPadding == null) {
            this.mForegroundPadding = new Rect();
        }
        drawable.getPadding(this.mForegroundPadding);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {
        public static final int BOX_ALL = 15;
        public static final int BOX_BOTTOM = 8;
        public static final int BOX_LEFT = 1;
        public static final int BOX_NONE = 0;
        public static final int BOX_RIGHT = 4;
        public static final int BOX_TOP = 2;
        public int boxedEdges;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.boxedEdges = 0;
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BoxInsetLayout_Layout, 0, 0);
            this.boxedEdges = a.getInt(R.styleable.BoxInsetLayout_Layout_layout_box, 0);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.boxedEdges = 0;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
            this.boxedEdges = 0;
        }

        public LayoutParams(int width, int height, int gravity, int boxed) {
            super(width, height, gravity);
            this.boxedEdges = 0;
            this.boxedEdges = boxed;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.boxedEdges = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.boxedEdges = 0;
        }

        public LayoutParams(FrameLayout.LayoutParams source) {
            super(source);
            this.boxedEdges = 0;
        }

        public LayoutParams(LayoutParams source) {
            super((FrameLayout.LayoutParams) source);
            this.boxedEdges = 0;
            this.boxedEdges = source.boxedEdges;
            this.gravity = source.gravity;
        }
    }
}
