package com.google.maps.android.ui;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.google.maps.android.R;

/* JADX INFO: loaded from: classes69.dex */
class BubbleDrawable extends Drawable {
    private int mColor = -1;
    private final Drawable mMask;
    private final Drawable mShadow;

    public BubbleDrawable(Resources res) {
        this.mMask = res.getDrawable(R.drawable.amu_bubble_mask);
        this.mShadow = res.getDrawable(R.drawable.amu_bubble_shadow);
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.mMask.draw(canvas);
        canvas.drawColor(this.mColor, PorterDuff.Mode.SRC_IN);
        this.mShadow.draw(canvas);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        throw new UnsupportedOperationException();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        throw new UnsupportedOperationException();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int left, int top, int right, int bottom) {
        this.mMask.setBounds(left, top, right, bottom);
        this.mShadow.setBounds(left, top, right, bottom);
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(Rect bounds) {
        this.mMask.setBounds(bounds);
        this.mShadow.setBounds(bounds);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect padding) {
        return this.mMask.getPadding(padding);
    }
}
