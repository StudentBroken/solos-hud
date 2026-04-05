package android.support.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes7.dex */
public class PercentLayoutHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "PercentLayout";
    private static final boolean VERBOSE = false;
    private final ViewGroup mHost;

    public interface PercentLayoutParams {
        PercentLayoutInfo getPercentLayoutInfo();
    }

    public PercentLayoutHelper(@NonNull ViewGroup host) {
        if (host == null) {
            throw new IllegalArgumentException("host must be non-null");
        }
        this.mHost = host;
    }

    public static void fetchWidthAndHeight(ViewGroup.LayoutParams params, TypedArray array, int widthAttr, int heightAttr) {
        params.width = array.getLayoutDimension(widthAttr, 0);
        params.height = array.getLayoutDimension(heightAttr, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {
        PercentLayoutInfo percentLayoutInfo;
        int widthHint = (View.MeasureSpec.getSize(widthMeasureSpec) - this.mHost.getPaddingLeft()) - this.mHost.getPaddingRight();
        int heightHint = (View.MeasureSpec.getSize(heightMeasureSpec) - this.mHost.getPaddingTop()) - this.mHost.getPaddingBottom();
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if ((layoutParams instanceof PercentLayoutParams) && (percentLayoutInfo = ((PercentLayoutParams) layoutParams).getPercentLayoutInfo()) != null) {
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    percentLayoutInfo.fillMarginLayoutParams(view, (ViewGroup.MarginLayoutParams) layoutParams, widthHint, heightHint);
                } else {
                    percentLayoutInfo.fillLayoutParams(layoutParams, widthHint, heightHint);
                }
            }
        }
    }

    public static PercentLayoutInfo getPercentLayoutInfo(Context context, AttributeSet attrs) {
        PercentLayoutInfo info = null;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PercentLayout_Layout);
        float value = array.getFraction(R.styleable.PercentLayout_Layout_layout_widthPercent, 1, 1, -1.0f);
        if (value != -1.0f) {
            if (0 == 0) {
                info = new PercentLayoutInfo();
            }
            info.widthPercent = value;
        }
        float value2 = array.getFraction(R.styleable.PercentLayout_Layout_layout_heightPercent, 1, 1, -1.0f);
        if (value2 != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.heightPercent = value2;
        }
        float value3 = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginPercent, 1, 1, -1.0f);
        if (value3 != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.leftMarginPercent = value3;
            info.topMarginPercent = value3;
            info.rightMarginPercent = value3;
            info.bottomMarginPercent = value3;
        }
        float value4 = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginLeftPercent, 1, 1, -1.0f);
        if (value4 != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.leftMarginPercent = value4;
        }
        float value5 = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginTopPercent, 1, 1, -1.0f);
        if (value5 != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.topMarginPercent = value5;
        }
        float value6 = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginRightPercent, 1, 1, -1.0f);
        if (value6 != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.rightMarginPercent = value6;
        }
        float value7 = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginBottomPercent, 1, 1, -1.0f);
        if (value7 != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.bottomMarginPercent = value7;
        }
        float value8 = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginStartPercent, 1, 1, -1.0f);
        if (value8 != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.startMarginPercent = value8;
        }
        float value9 = array.getFraction(R.styleable.PercentLayout_Layout_layout_marginEndPercent, 1, 1, -1.0f);
        if (value9 != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.endMarginPercent = value9;
        }
        float value10 = array.getFraction(R.styleable.PercentLayout_Layout_layout_aspectRatio, 1, 1, -1.0f);
        if (value10 != -1.0f) {
            if (info == null) {
                info = new PercentLayoutInfo();
            }
            info.aspectRatio = value10;
        }
        array.recycle();
        return info;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void restoreOriginalParams() {
        PercentLayoutInfo percentLayoutInfo;
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if ((layoutParams instanceof PercentLayoutParams) && (percentLayoutInfo = ((PercentLayoutParams) layoutParams).getPercentLayoutInfo()) != null) {
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    percentLayoutInfo.restoreMarginLayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
                } else {
                    percentLayoutInfo.restoreLayoutParams(layoutParams);
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean handleMeasuredStateTooSmall() {
        PercentLayoutInfo info;
        boolean needsSecondMeasure = false;
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if ((layoutParams instanceof PercentLayoutParams) && (info = ((PercentLayoutParams) layoutParams).getPercentLayoutInfo()) != null) {
                if (shouldHandleMeasuredWidthTooSmall(view, info)) {
                    needsSecondMeasure = true;
                    layoutParams.width = -2;
                }
                if (shouldHandleMeasuredHeightTooSmall(view, info)) {
                    needsSecondMeasure = true;
                    layoutParams.height = -2;
                }
            }
        }
        return needsSecondMeasure;
    }

    private static boolean shouldHandleMeasuredWidthTooSmall(View view, PercentLayoutInfo info) {
        int state = ViewCompat.getMeasuredWidthAndState(view) & ViewCompat.MEASURED_STATE_MASK;
        return state == 16777216 && info.widthPercent >= 0.0f && info.mPreservedParams.width == -2;
    }

    private static boolean shouldHandleMeasuredHeightTooSmall(View view, PercentLayoutInfo info) {
        int state = ViewCompat.getMeasuredHeightAndState(view) & ViewCompat.MEASURED_STATE_MASK;
        return state == 16777216 && info.heightPercent >= 0.0f && info.mPreservedParams.height == -2;
    }

    static class PercentMarginLayoutParams extends ViewGroup.MarginLayoutParams {
        private boolean mIsHeightComputedFromAspectRatio;
        private boolean mIsWidthComputedFromAspectRatio;

        public PercentMarginLayoutParams(int width, int height) {
            super(width, height);
        }
    }

    public static class PercentLayoutInfo {
        public float aspectRatio;
        public float widthPercent = -1.0f;
        public float heightPercent = -1.0f;
        public float leftMarginPercent = -1.0f;
        public float topMarginPercent = -1.0f;
        public float rightMarginPercent = -1.0f;
        public float bottomMarginPercent = -1.0f;
        public float startMarginPercent = -1.0f;
        public float endMarginPercent = -1.0f;
        final PercentMarginLayoutParams mPreservedParams = new PercentMarginLayoutParams(0, 0);

        public void fillLayoutParams(ViewGroup.LayoutParams params, int widthHint, int heightHint) {
            this.mPreservedParams.width = params.width;
            this.mPreservedParams.height = params.height;
            boolean widthNotSet = (this.mPreservedParams.mIsWidthComputedFromAspectRatio || this.mPreservedParams.width == 0) && this.widthPercent < 0.0f;
            boolean heightNotSet = (this.mPreservedParams.mIsHeightComputedFromAspectRatio || this.mPreservedParams.height == 0) && this.heightPercent < 0.0f;
            if (this.widthPercent >= 0.0f) {
                params.width = Math.round(widthHint * this.widthPercent);
            }
            if (this.heightPercent >= 0.0f) {
                params.height = Math.round(heightHint * this.heightPercent);
            }
            if (this.aspectRatio >= 0.0f) {
                if (widthNotSet) {
                    params.width = Math.round(params.height * this.aspectRatio);
                    this.mPreservedParams.mIsWidthComputedFromAspectRatio = true;
                }
                if (heightNotSet) {
                    params.height = Math.round(params.width / this.aspectRatio);
                    this.mPreservedParams.mIsHeightComputedFromAspectRatio = true;
                }
            }
        }

        @Deprecated
        public void fillMarginLayoutParams(ViewGroup.MarginLayoutParams params, int widthHint, int heightHint) {
            fillMarginLayoutParams(null, params, widthHint, heightHint);
        }

        public void fillMarginLayoutParams(View view, ViewGroup.MarginLayoutParams params, int widthHint, int heightHint) {
            fillLayoutParams(params, widthHint, heightHint);
            this.mPreservedParams.leftMargin = params.leftMargin;
            this.mPreservedParams.topMargin = params.topMargin;
            this.mPreservedParams.rightMargin = params.rightMargin;
            this.mPreservedParams.bottomMargin = params.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(this.mPreservedParams, MarginLayoutParamsCompat.getMarginStart(params));
            MarginLayoutParamsCompat.setMarginEnd(this.mPreservedParams, MarginLayoutParamsCompat.getMarginEnd(params));
            if (this.leftMarginPercent >= 0.0f) {
                params.leftMargin = Math.round(widthHint * this.leftMarginPercent);
            }
            if (this.topMarginPercent >= 0.0f) {
                params.topMargin = Math.round(heightHint * this.topMarginPercent);
            }
            if (this.rightMarginPercent >= 0.0f) {
                params.rightMargin = Math.round(widthHint * this.rightMarginPercent);
            }
            if (this.bottomMarginPercent >= 0.0f) {
                params.bottomMargin = Math.round(heightHint * this.bottomMarginPercent);
            }
            boolean shouldResolveLayoutDirection = false;
            if (this.startMarginPercent >= 0.0f) {
                MarginLayoutParamsCompat.setMarginStart(params, Math.round(widthHint * this.startMarginPercent));
                shouldResolveLayoutDirection = true;
            }
            if (this.endMarginPercent >= 0.0f) {
                MarginLayoutParamsCompat.setMarginEnd(params, Math.round(widthHint * this.endMarginPercent));
                shouldResolveLayoutDirection = true;
            }
            if (shouldResolveLayoutDirection && view != null) {
                MarginLayoutParamsCompat.resolveLayoutDirection(params, ViewCompat.getLayoutDirection(view));
            }
        }

        public String toString() {
            return String.format("PercentLayoutInformation width: %f height %f, margins (%f, %f,  %f, %f, %f, %f)", Float.valueOf(this.widthPercent), Float.valueOf(this.heightPercent), Float.valueOf(this.leftMarginPercent), Float.valueOf(this.topMarginPercent), Float.valueOf(this.rightMarginPercent), Float.valueOf(this.bottomMarginPercent), Float.valueOf(this.startMarginPercent), Float.valueOf(this.endMarginPercent));
        }

        public void restoreMarginLayoutParams(ViewGroup.MarginLayoutParams params) {
            restoreLayoutParams(params);
            params.leftMargin = this.mPreservedParams.leftMargin;
            params.topMargin = this.mPreservedParams.topMargin;
            params.rightMargin = this.mPreservedParams.rightMargin;
            params.bottomMargin = this.mPreservedParams.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(params, MarginLayoutParamsCompat.getMarginStart(this.mPreservedParams));
            MarginLayoutParamsCompat.setMarginEnd(params, MarginLayoutParamsCompat.getMarginEnd(this.mPreservedParams));
        }

        public void restoreLayoutParams(ViewGroup.LayoutParams params) {
            if (!this.mPreservedParams.mIsWidthComputedFromAspectRatio) {
                params.width = this.mPreservedParams.width;
            }
            if (!this.mPreservedParams.mIsHeightComputedFromAspectRatio) {
                params.height = this.mPreservedParams.height;
            }
            this.mPreservedParams.mIsWidthComputedFromAspectRatio = false;
            this.mPreservedParams.mIsHeightComputedFromAspectRatio = false;
        }
    }
}
