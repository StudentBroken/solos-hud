package android.support.wearable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.wearable.R;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import com.kopin.pupil.ui.PageParser;
import com.kopin.solos.view.graphics.Bar;
import com.nuance.android.vocalizer.VocalizerEngine;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
@RemoteViews.RemoteView
@Deprecated
public class WearableFrameLayout extends ViewGroup {
    private static final int DEFAULT_CHILD_GRAVITY = 8388659;
    private static final String TAG = "WearableFrameLayout";

    @ViewDebug.ExportedProperty(category = "drawing")
    private Drawable mForeground;
    boolean mForegroundBoundsChanged;

    @ViewDebug.ExportedProperty(category = "drawing")
    private int mForegroundGravity;

    @ViewDebug.ExportedProperty(category = "drawing")
    private boolean mForegroundInPadding;

    @ViewDebug.ExportedProperty(category = PageParser.PADDING)
    private int mForegroundPaddingBottom;

    @ViewDebug.ExportedProperty(category = PageParser.PADDING)
    private int mForegroundPaddingLeft;

    @ViewDebug.ExportedProperty(category = PageParser.PADDING)
    private int mForegroundPaddingRight;

    @ViewDebug.ExportedProperty(category = PageParser.PADDING)
    private int mForegroundPaddingTop;
    private ColorStateList mForegroundTintList;
    private PorterDuff.Mode mForegroundTintMode;
    private boolean mHasForegroundTint;
    private boolean mHasForegroundTintMode;
    private final ArrayList<View> mMatchParentChildren;

    @ViewDebug.ExportedProperty(category = "measurement")
    boolean mMeasureAllChildren;
    private final Rect mOverlayBounds;
    private boolean mRound;
    private final Rect mSelfBounds;

    public WearableFrameLayout(Context context) {
        super(context);
        this.mMeasureAllChildren = false;
        this.mForegroundTintList = null;
        this.mForegroundTintMode = null;
        this.mHasForegroundTint = false;
        this.mHasForegroundTintMode = false;
        this.mForegroundPaddingLeft = 0;
        this.mForegroundPaddingTop = 0;
        this.mForegroundPaddingRight = 0;
        this.mForegroundPaddingBottom = 0;
        this.mSelfBounds = new Rect();
        this.mOverlayBounds = new Rect();
        this.mForegroundGravity = 119;
        this.mForegroundInPadding = true;
        this.mForegroundBoundsChanged = false;
        this.mMatchParentChildren = new ArrayList<>(1);
        this.mRound = false;
    }

    public WearableFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WearableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMeasureAllChildren = false;
        this.mForegroundTintList = null;
        this.mForegroundTintMode = null;
        this.mHasForegroundTint = false;
        this.mHasForegroundTintMode = false;
        this.mForegroundPaddingLeft = 0;
        this.mForegroundPaddingTop = 0;
        this.mForegroundPaddingRight = 0;
        this.mForegroundPaddingBottom = 0;
        this.mSelfBounds = new Rect();
        this.mOverlayBounds = new Rect();
        this.mForegroundGravity = 119;
        this.mForegroundInPadding = true;
        this.mForegroundBoundsChanged = false;
        this.mMatchParentChildren = new ArrayList<>(1);
        this.mRound = false;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WearableFrameLayout, defStyleAttr, defStyleRes);
        this.mForegroundGravity = a.getInt(R.styleable.WearableFrameLayout_android_foregroundGravity, this.mForegroundGravity);
        Drawable d = a.getDrawable(R.styleable.WearableFrameLayout_android_foreground);
        if (d != null) {
            setForeground(d);
        }
        if (a.getBoolean(R.styleable.WearableFrameLayout_android_measureAllChildren, false)) {
            setMeasureAllChildren(true);
        }
        if (a.hasValue(R.styleable.WearableFrameLayout_android_foregroundTint)) {
            this.mForegroundTintList = a.getColorStateList(R.styleable.WearableFrameLayout_android_foregroundTint);
            this.mHasForegroundTint = true;
        }
        a.recycle();
        applyForegroundTint();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestApplyInsets();
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        boolean changed = this.mRound != insets.isRound();
        this.mRound = insets.isRound();
        if (changed) {
            requestLayout();
        }
        return super.onApplyWindowInsets(insets);
    }

    @Override // android.view.View
    public int getForegroundGravity() {
        return this.mForegroundGravity;
    }

    @Override // android.view.View
    public void setForegroundGravity(int foregroundGravity) {
        if (this.mForegroundGravity != foregroundGravity) {
            if ((8388615 & foregroundGravity) == 0) {
                foregroundGravity |= GravityCompat.START;
            }
            if ((foregroundGravity & 112) == 0) {
                foregroundGravity |= 48;
            }
            this.mForegroundGravity = foregroundGravity;
            if (this.mForegroundGravity == 119 && this.mForeground != null) {
                Rect padding = new Rect();
                if (this.mForeground.getPadding(padding)) {
                    this.mForegroundPaddingLeft = padding.left;
                    this.mForegroundPaddingTop = padding.top;
                    this.mForegroundPaddingRight = padding.right;
                    this.mForegroundPaddingBottom = padding.bottom;
                }
            } else {
                this.mForegroundPaddingLeft = 0;
                this.mForegroundPaddingTop = 0;
                this.mForegroundPaddingRight = 0;
                this.mForegroundPaddingBottom = 0;
            }
            requestLayout();
        }
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (this.mForeground != null) {
            this.mForeground.setVisible(visibility == 0, false);
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mForeground;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mForeground != null) {
            this.mForeground.jumpToCurrentState();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mForeground != null && this.mForeground.isStateful()) {
            this.mForeground.setState(getDrawableState());
        }
    }

    @Override // android.view.View
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mForeground != null) {
            this.mForeground.setHotspot(x, y);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.view.View
    public void setForeground(Drawable d) {
        if (this.mForeground != d) {
            if (this.mForeground != null) {
                this.mForeground.setCallback(null);
                unscheduleDrawable(this.mForeground);
            }
            this.mForeground = d;
            this.mForegroundPaddingLeft = 0;
            this.mForegroundPaddingTop = 0;
            this.mForegroundPaddingRight = 0;
            this.mForegroundPaddingBottom = 0;
            if (d != null) {
                setWillNotDraw(false);
                d.setCallback(this);
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
                applyForegroundTint();
                if (this.mForegroundGravity == 119) {
                    Rect padding = new Rect();
                    if (d.getPadding(padding)) {
                        this.mForegroundPaddingLeft = padding.left;
                        this.mForegroundPaddingTop = padding.top;
                        this.mForegroundPaddingRight = padding.right;
                        this.mForegroundPaddingBottom = padding.bottom;
                    }
                }
            } else {
                setWillNotDraw(true);
            }
            requestLayout();
            invalidate();
        }
    }

    @Override // android.view.View
    public Drawable getForeground() {
        return this.mForeground;
    }

    @Override // android.view.View
    public void setForegroundTintList(ColorStateList tint) {
        this.mForegroundTintList = tint;
        this.mHasForegroundTint = true;
        applyForegroundTint();
    }

    public void setForegroundInPadding(boolean value) {
        this.mForegroundInPadding = value;
    }

    @Override // android.view.View
    public ColorStateList getForegroundTintList() {
        return this.mForegroundTintList;
    }

    @Override // android.view.View
    public void setForegroundTintMode(PorterDuff.Mode tintMode) {
        this.mForegroundTintMode = tintMode;
        this.mHasForegroundTintMode = true;
        applyForegroundTint();
    }

    @Override // android.view.View
    public PorterDuff.Mode getForegroundTintMode() {
        return this.mForegroundTintMode;
    }

    private void applyForegroundTint() {
        if (this.mForeground != null) {
            if (this.mHasForegroundTint || this.mHasForegroundTintMode) {
                this.mForeground = this.mForeground.mutate();
                if (this.mHasForegroundTint) {
                    this.mForeground.setTintList(this.mForegroundTintList);
                }
                if (this.mHasForegroundTintMode) {
                    this.mForeground.setTintMode(this.mForegroundTintMode);
                }
                if (this.mForeground.isStateful()) {
                    this.mForeground.setState(getDrawableState());
                }
            }
        }
    }

    int getPaddingLeftWithForeground() {
        if (this.mForegroundInPadding) {
            return Math.max(getPaddingLeft(), this.mForegroundPaddingLeft);
        }
        return getPaddingLeft() + this.mForegroundPaddingLeft;
    }

    int getPaddingRightWithForeground() {
        if (this.mForegroundInPadding) {
            return Math.max(getPaddingRight(), this.mForegroundPaddingRight);
        }
        return getPaddingRight() + this.mForegroundPaddingRight;
    }

    private int getPaddingTopWithForeground() {
        if (this.mForegroundInPadding) {
            return Math.max(getPaddingTop(), this.mForegroundPaddingTop);
        }
        return getPaddingTop() + this.mForegroundPaddingTop;
    }

    private int getPaddingBottomWithForeground() {
        if (this.mForegroundInPadding) {
            return Math.max(getPaddingBottom(), this.mForegroundPaddingBottom);
        }
        return getPaddingBottom() + this.mForegroundPaddingBottom;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;
        int count = getChildCount();
        boolean measureMatchParentChildren = (View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && View.MeasureSpec.getMode(heightMeasureSpec) == 1073741824) ? false : true;
        this.mMatchParentChildren.clear();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (this.mMeasureAllChildren || child.getVisibility() != 8) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + getParamsLeftMargin(lp) + getParamsRightMargin(lp));
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + getParamsTopMargin(lp) + getParamsBottomMargin(lp));
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren && (getParamsWidth(lp) == -1 || getParamsHeight(lp) == -1)) {
                    this.mMatchParentChildren.add(child);
                }
            }
        }
        int maxWidth2 = maxWidth + getPaddingLeftWithForeground() + getPaddingRightWithForeground();
        int maxHeight2 = Math.max(maxHeight + getPaddingTopWithForeground() + getPaddingBottomWithForeground(), getSuggestedMinimumHeight());
        int maxWidth3 = Math.max(maxWidth2, getSuggestedMinimumWidth());
        Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight2 = Math.max(maxHeight2, drawable.getMinimumHeight());
            maxWidth3 = Math.max(maxWidth3, drawable.getMinimumWidth());
        }
        setMeasuredDimension(resolveSizeAndState(maxWidth3, widthMeasureSpec, childState), resolveSizeAndState(maxHeight2, heightMeasureSpec, childState << 16));
        int count2 = this.mMatchParentChildren.size();
        if (count2 > 1) {
            for (int i2 = 0; i2 < count2; i2++) {
                View child2 = this.mMatchParentChildren.get(i2);
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (getParamsWidth(lp2) == -1) {
                    childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec((((getMeasuredWidth() - getPaddingLeftWithForeground()) - getPaddingRightWithForeground()) - getParamsLeftMargin(lp2)) - getParamsRightMargin(lp2), 1073741824);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeftWithForeground() + getPaddingRightWithForeground() + getParamsLeftMargin(lp2) + getParamsRightMargin(lp2), getParamsWidth(lp2));
                }
                if (getParamsHeight(lp2) == -1) {
                    childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((((getMeasuredHeight() - getPaddingTopWithForeground()) - getPaddingBottomWithForeground()) - getParamsTopMargin(lp2)) - getParamsBottomMargin(lp2), 1073741824);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTopWithForeground() + getPaddingBottomWithForeground() + getParamsTopMargin(lp2) + getParamsBottomMargin(lp2), getParamsHeight(lp2));
                }
                child2.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    @Override // android.view.ViewGroup
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight() + getParamsLeftMargin(lp) + getParamsRightMargin(lp) + widthUsed, getParamsWidth(lp));
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom() + getParamsTopMargin(lp) + getParamsBottomMargin(lp) + heightUsed, getParamsHeight(lp));
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildren(left, top, right, bottom, false);
    }

    void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
        int childLeft;
        int childTop;
        int count = getChildCount();
        int parentLeft = getPaddingLeftWithForeground();
        int parentRight = (right - left) - getPaddingRightWithForeground();
        int parentTop = getPaddingTopWithForeground();
        int parentBottom = (bottom - top) - getPaddingBottomWithForeground();
        this.mForegroundBoundsChanged = true;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int gravity = getParamsGravity(lp);
                if (gravity == -1) {
                    gravity = DEFAULT_CHILD_GRAVITY;
                }
                int layoutDirection = getLayoutDirection();
                int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                int verticalGravity = gravity & 112;
                switch (absoluteGravity & 7) {
                    case 1:
                        childLeft = (((((parentRight - parentLeft) - width) / 2) + parentLeft) + getParamsLeftMargin(lp)) - getParamsRightMargin(lp);
                        break;
                    case 5:
                        if (!forceLeftGravity) {
                            childLeft = (parentRight - width) - getParamsRightMargin(lp);
                            break;
                        }
                    default:
                        childLeft = parentLeft + getParamsLeftMargin(lp);
                        break;
                }
                switch (verticalGravity) {
                    case 16:
                        childTop = (((((parentBottom - parentTop) - height) / 2) + parentTop) + getParamsTopMargin(lp)) - getParamsBottomMargin(lp);
                        break;
                    case Bar.DEFAULT_HEIGHT /* 48 */:
                        childTop = parentTop + getParamsTopMargin(lp);
                        break;
                    case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                        childTop = (parentBottom - height) - getParamsBottomMargin(lp);
                        break;
                    default:
                        childTop = parentTop + getParamsTopMargin(lp);
                        break;
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    private int getParamsWidth(LayoutParams params) {
        return this.mRound ? params.widthRound : params.width;
    }

    private int getParamsHeight(LayoutParams params) {
        return this.mRound ? params.heightRound : params.height;
    }

    private int getParamsLeftMargin(LayoutParams params) {
        return this.mRound ? params.leftMarginRound : params.leftMargin;
    }

    private int getParamsTopMargin(LayoutParams params) {
        return this.mRound ? params.topMarginRound : params.topMargin;
    }

    private int getParamsRightMargin(LayoutParams params) {
        return this.mRound ? params.rightMarginRound : params.rightMargin;
    }

    private int getParamsBottomMargin(LayoutParams params) {
        return this.mRound ? params.bottomMarginRound : params.bottomMargin;
    }

    private int getParamsGravity(LayoutParams params) {
        return this.mRound ? params.gravityRound : params.gravity;
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mForegroundBoundsChanged = true;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mForeground != null) {
            Drawable foreground = this.mForeground;
            if (this.mForegroundBoundsChanged) {
                this.mForegroundBoundsChanged = false;
                Rect selfBounds = this.mSelfBounds;
                Rect overlayBounds = this.mOverlayBounds;
                int w = getRight() - getLeft();
                int h = getBottom() - getTop();
                if (this.mForegroundInPadding) {
                    selfBounds.set(0, 0, w, h);
                } else {
                    selfBounds.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
                }
                int layoutDirection = getLayoutDirection();
                Gravity.apply(this.mForegroundGravity, foreground.getIntrinsicWidth(), foreground.getIntrinsicHeight(), selfBounds, overlayBounds, layoutDirection);
                foreground.setBounds(overlayBounds);
            }
            foreground.draw(canvas);
        }
    }

    public void setMeasureAllChildren(boolean measureAll) {
        this.mMeasureAllChildren = measureAll;
    }

    public boolean getMeasureAllChildren() {
        return this.mMeasureAllChildren;
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams((LayoutParams) p);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(WearableFrameLayout.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(WearableFrameLayout.class.getName());
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {
        public int bottomMarginRound;
        public int gravityRound;
        public int heightRound;
        public int leftMarginRound;
        public int rightMarginRound;
        public int topMarginRound;
        public int widthRound;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.gravityRound = -1;
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.WearableFrameLayout);
            this.gravityRound = a.getInt(R.styleable.WearableFrameLayout_layout_gravityRound, this.gravity);
            this.widthRound = a.getLayoutDimension(R.styleable.WearableFrameLayout_layout_widthRound, this.width);
            this.heightRound = a.getLayoutDimension(R.styleable.WearableFrameLayout_layout_heightRound, this.height);
            int marginRound = a.getDimensionPixelSize(R.styleable.WearableFrameLayout_layout_marginRound, -1);
            if (marginRound >= 0) {
                this.bottomMarginRound = marginRound;
                this.topMarginRound = marginRound;
                this.rightMarginRound = marginRound;
                this.leftMarginRound = marginRound;
            } else {
                this.leftMarginRound = a.getDimensionPixelSize(R.styleable.WearableFrameLayout_layout_marginLeftRound, this.leftMargin);
                this.topMarginRound = a.getDimensionPixelSize(R.styleable.WearableFrameLayout_layout_marginTopRound, this.topMargin);
                this.rightMarginRound = a.getDimensionPixelSize(R.styleable.WearableFrameLayout_layout_marginRightRound, this.rightMargin);
                this.bottomMarginRound = a.getDimensionPixelSize(R.styleable.WearableFrameLayout_layout_marginBottomRound, this.bottomMargin);
            }
            a.recycle();
        }

        public LayoutParams(int width, int height, int gravity, int widthRound, int heightRound, int gravityRound) {
            super(width, height, gravity);
            this.gravityRound = -1;
            this.widthRound = widthRound;
            this.heightRound = heightRound;
            this.gravityRound = gravityRound;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
            this.gravityRound = -1;
            this.widthRound = width;
            this.heightRound = height;
            this.gravityRound = gravity;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravityRound = -1;
            this.widthRound = width;
            this.heightRound = height;
        }

        public LayoutParams(LayoutParams source) {
            super((FrameLayout.LayoutParams) source);
            this.gravityRound = -1;
            this.widthRound = source.widthRound;
            this.heightRound = source.heightRound;
            this.gravityRound = source.gravityRound;
            this.leftMarginRound = source.leftMarginRound;
            this.topMarginRound = source.topMarginRound;
            this.rightMarginRound = source.rightMarginRound;
            this.bottomMarginRound = source.bottomMarginRound;
        }
    }
}
