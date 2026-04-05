package com.kopin.pupil.ui.elements;

import android.graphics.Canvas;
import android.graphics.Rect;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.BasePage;
import com.kopin.pupil.util.MathHelper;

/* JADX INFO: loaded from: classes25.dex */
public class ProgressElement extends BaseElement implements Sizable {
    public static final int DEFAULT_HEIGHT = 3;
    public static final int DEFAULT_WIDTH = -1;
    private static final int MAX_PROGRESS = 100;
    private static final int MIN_PROGRESS = 0;
    private float mProgress = 0.0f;
    private int mWidth = -1;
    private int mHeight = 3;
    private int mBackgoundLineHeight = 1;

    public int getProgress() {
        return (int) (this.mProgress * 100.0f);
    }

    public void setProgress(int progress) {
        this.mHasChanged = true;
        this.mProgress = MathHelper.clamp(0, 100, progress) / 100.0f;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getInnerWidth() throws UnsupportedOperationException {
        if (this.mWidth == -1) {
            throw new UnsupportedOperationException("A width must be specified for a progress bar");
        }
        return this.mWidth;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getInnerHeight() throws UnsupportedOperationException {
        return this.mHeight;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getWidth() {
        if (this.mWidth != -1) {
            return this.mWidth;
        }
        return 428;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getHeight() {
        return this.mHeight;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public void setWidth(int width) {
        this.mHasChanged = true;
        this.mWidth = width;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public void setHeight(int height) {
        this.mHasChanged = true;
        this.mHeight = height;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public void onDraw(Canvas canvas, Theme theme, Rect screenBounds, BasePage parentPage) {
        Rect bounds = getElementRect(theme, screenBounds.width(), screenBounds.height());
        canvas.drawRect(new Rect(bounds.left, bounds.centerY(), bounds.right, bounds.centerY() + this.mBackgoundLineHeight), theme.getSolidColorPaint(getColor()));
        int barWidth = (int) (this.mProgress * getWidth());
        Rect barRect = new Rect(bounds);
        barRect.right = barRect.left + barWidth;
        canvas.drawRect(barRect, theme.getSolidColorPaint(getActiveColor()));
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    protected void calculateRect(Theme theme, int screenWidth, int screenHeight, Rect out_bounds) {
        super.calculateRect(theme, screenWidth, screenHeight, out_bounds);
        out_bounds.set(0, 0, getWidth(), getHeight());
    }
}
