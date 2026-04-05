package com.google.maps.android.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes69.dex */
public class RotationLayout extends FrameLayout {
    private int mRotation;

    public RotationLayout(Context context) {
        super(context);
    }

    public RotationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotationLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mRotation == 1 || this.mRotation == 3) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setViewRotation(int degrees) {
        this.mRotation = ((degrees + 360) % 360) / 90;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (this.mRotation == 0) {
            super.dispatchDraw(canvas);
            return;
        }
        if (this.mRotation == 1) {
            canvas.translate(getWidth(), 0.0f);
            canvas.rotate(90.0f, getWidth() / 2, 0.0f);
            canvas.translate(getHeight() / 2, getWidth() / 2);
        } else if (this.mRotation == 2) {
            canvas.rotate(180.0f, getWidth() / 2, getHeight() / 2);
        } else {
            canvas.translate(0.0f, getHeight());
            canvas.rotate(270.0f, getWidth() / 2, 0.0f);
            canvas.translate(getHeight() / 2, (-getWidth()) / 2);
        }
        super.dispatchDraw(canvas);
    }
}
