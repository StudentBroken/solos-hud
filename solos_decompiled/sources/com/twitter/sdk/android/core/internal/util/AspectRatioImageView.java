package com.twitter.sdk.android.core.internal.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.twitter.sdk.android.core.R;

/* JADX INFO: loaded from: classes62.dex */
public class AspectRatioImageView extends ImageView {
    static final int ADJUST_DIMENSION_HEIGHT = 0;
    static final int ADJUST_DIMENSION_WIDTH = 1;
    private static final int DEFAULT_ADJUST_DIMENSION = 0;
    private static final float DEFAULT_ASPECT_RATIO = 1.0f;
    private double aspectRatio;
    private int dimensionToAdjust;

    public AspectRatioImageView(Context context) {
        this(context, null);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tw__AspectRatioImageView);
        try {
            this.aspectRatio = a.getFloat(R.styleable.tw__AspectRatioImageView_tw__image_aspect_ratio, 1.0f);
            this.dimensionToAdjust = a.getInt(R.styleable.tw__AspectRatioImageView_tw__image_dimension_to_adjust, 0);
        } finally {
            a.recycle();
        }
    }

    public double getAspectRatio() {
        return this.aspectRatio;
    }

    public int getDimensionToAdjust() {
        return this.dimensionToAdjust;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void resetSize() {
        if (getMeasuredWidth() != 0 || getMeasuredHeight() != 0) {
            measure(View.MeasureSpec.makeMeasureSpec(0, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 1073741824));
            layout(0, 0, 0, 0);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (this.dimensionToAdjust == 0) {
            height = calculateHeight(width, this.aspectRatio);
        } else {
            width = calculateWidth(height, this.aspectRatio);
        }
        setMeasuredDimension(width, height);
    }

    int calculateHeight(int width, double ratio) {
        if (ratio == 0.0d) {
            return 0;
        }
        return (int) Math.round(((double) width) / ratio);
    }

    int calculateWidth(int height, double ratio) {
        return (int) Math.round(((double) height) * ratio);
    }
}
