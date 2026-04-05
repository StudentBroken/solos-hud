package com.kopin.solos.view.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.kopin.solos.view.R;

/* JADX INFO: loaded from: classes48.dex */
public class Bar {
    public static final int DEFAULT_HEIGHT = 48;
    public static final int DEFAULT_WIDTH = 196;
    public static final int MAX_BAR_SEGMENTS = 9;
    public static final int MAX_BAR_VALUE = 300;
    private Bitmap mBackground;
    private int mBarNr;
    private float mBarWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Bitmap mForeground;
    private int mHeight;
    private int mMaxValue;
    private volatile boolean mModified;
    private volatile int mValue;
    private int mWidth;

    public Bar(Context context, int barNr, int maxValue, int width, int height, boolean reversed) {
        if (context == null) {
            throw new NullPointerException("Passed null context");
        }
        this.mBarNr = barNr;
        this.mMaxValue = maxValue;
        this.mWidth = width;
        this.mHeight = height;
        this.mBarWidth = width / barNr;
        this.mBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.mBitmap);
        setBackground(context.getResources(), reversed ? R.drawable.heart_rate_zones_neutral : R.drawable.power_bar_neutral);
        setForeground(context.getResources(), reversed ? R.drawable.heart_rate_zones_color : R.drawable.power_bar_colour);
    }

    public void addValue(int value) {
        if (value < 0) {
            value = -value;
        }
        if (value > this.mMaxValue) {
            value = this.mMaxValue;
        }
        this.mValue = value;
        this.mModified = true;
    }

    private void draw() {
        int bars = Math.round((this.mBarNr * this.mValue) / this.mMaxValue);
        this.mBitmap.eraseColor(0);
        this.mCanvas.drawBitmap(this.mBackground, 0.0f, 0.0f, (Paint) null);
        this.mCanvas.save();
        this.mCanvas.clipRect(0.0f, 0.0f, bars * this.mBarWidth, this.mHeight);
        this.mCanvas.drawBitmap(this.mForeground, 0.0f, 0.0f, (Paint) null);
        this.mCanvas.restore();
    }

    public Bitmap getBitmap() {
        if (this.mModified) {
            draw();
        }
        return this.mBitmap;
    }

    public void setBackground(Resources resources, int resourceId) {
        setBackground(BitmapFactory.decodeResource(resources, resourceId));
    }

    public void setBackground(Bitmap bitmap) {
        this.mBackground = Bitmap.createScaledBitmap(bitmap, this.mWidth, this.mHeight, false);
    }

    public void setForeground(Resources resources, int resourceId) {
        setForeground(BitmapFactory.decodeResource(resources, resourceId));
    }

    public void setForeground(Bitmap bitmap) {
        this.mForeground = Bitmap.createScaledBitmap(bitmap, this.mWidth, this.mHeight, false);
    }
}
