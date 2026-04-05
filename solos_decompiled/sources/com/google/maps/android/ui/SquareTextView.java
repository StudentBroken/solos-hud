package com.google.maps.android.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/* JADX INFO: loaded from: classes69.dex */
public class SquareTextView extends TextView {
    private int mOffsetLeft;
    private int mOffsetTop;

    public SquareTextView(Context context) {
        super(context);
        this.mOffsetTop = 0;
        this.mOffsetLeft = 0;
    }

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mOffsetTop = 0;
        this.mOffsetLeft = 0;
    }

    public SquareTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mOffsetTop = 0;
        this.mOffsetLeft = 0;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int dimension = Math.max(width, height);
        if (width > height) {
            this.mOffsetTop = width - height;
            this.mOffsetLeft = 0;
        } else {
            this.mOffsetTop = 0;
            this.mOffsetLeft = height - width;
        }
        setMeasuredDimension(dimension, dimension);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        canvas.translate(this.mOffsetLeft / 2, this.mOffsetTop / 2);
        super.draw(canvas);
    }
}
