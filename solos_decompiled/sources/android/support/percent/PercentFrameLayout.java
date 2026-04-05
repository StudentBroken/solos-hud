package android.support.percent;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RequiresApi;
import android.support.percent.PercentLayoutHelper;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes7.dex */
public class PercentFrameLayout extends FrameLayout {
    private final PercentLayoutHelper mHelper;

    public PercentFrameLayout(Context context) {
        super(context);
        this.mHelper = new PercentLayoutHelper(this);
    }

    public PercentFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHelper = new PercentLayoutHelper(this);
    }

    public PercentFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mHelper = new PercentLayoutHelper(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mHelper.handleMeasuredStateTooSmall()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mHelper.restoreOriginalParams();
    }

    public static class LayoutParams extends FrameLayout.LayoutParams implements PercentLayoutHelper.PercentLayoutParams {
        private PercentLayoutHelper.PercentLayoutInfo mPercentLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(FrameLayout.LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.gravity = source.gravity;
        }

        @RequiresApi(19)
        @TargetApi(19)
        public LayoutParams(LayoutParams source) {
            this((FrameLayout.LayoutParams) source);
            this.mPercentLayoutInfo = source.mPercentLayoutInfo;
        }

        @Override // android.support.percent.PercentLayoutHelper.PercentLayoutParams
        public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo() {
            if (this.mPercentLayoutInfo == null) {
                this.mPercentLayoutInfo = new PercentLayoutHelper.PercentLayoutInfo();
            }
            return this.mPercentLayoutInfo;
        }

        @Override // android.view.ViewGroup.LayoutParams
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
        }
    }
}
