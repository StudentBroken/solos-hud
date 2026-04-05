package com.kopin.solos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.v4.view.InputDeviceCompat;
import android.util.AttributeSet;
import android.view.View;

/* JADX INFO: loaded from: classes48.dex */
public class BarView extends View {
    private int mMax;
    private int mValue;
    private Paint paintBackground;
    private Paint paintBar;

    public BarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paintBackground = new Paint();
        this.paintBar = new Paint();
        this.mMax = 100;
        this.mValue = 0;
        this.paintBackground.setColor(-12303292);
        this.paintBar.setColor(InputDeviceCompat.SOURCE_ANY);
    }

    public void setColorForegroundResource(@ColorRes int resColor) {
        this.paintBar.setColor(getResources().getColor(resColor));
        invalidate();
    }

    public void setColorBackgroundResource(@ColorRes int resColor) {
        this.paintBackground.setColor(getResources().getColor(resColor));
        invalidate();
    }

    public void setData(int value, int max) {
        this.mValue = value;
        this.mMax = max;
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int value = 0;
        if (width > 0 && this.mMax > 0 && this.mValue >= 0) {
            value = (this.mValue * width) / this.mMax;
        }
        canvas.drawRect(0.0f, 0.0f, value, height, this.paintBar);
        canvas.drawRect(value, 0.0f, width, height, this.paintBackground);
    }
}
