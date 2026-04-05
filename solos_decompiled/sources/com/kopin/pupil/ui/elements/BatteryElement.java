package com.kopin.pupil.ui.elements;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.BasePage;
import com.kopin.pupil.util.MathHelper;

/* JADX INFO: loaded from: classes25.dex */
public class BatteryElement extends BaseElement {
    private static final int BAR_COUNT = 5;
    private static final int BAR_WIDTH = 4;
    private static final int ELEMENT_HEIGHT = 13;
    private static final int ELEMENT_WIDTH = 32;
    private static final int SPACING = 3;
    private float mLevel = 0.0f;

    public void setLevel(float level) {
        this.mHasChanged = true;
        this.mLevel = MathHelper.clamp(0.0f, 1.0f, level);
    }

    public float getLevel() {
        return this.mLevel;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public void onDraw(Canvas canvas, Theme theme, Rect screenBounds, BasePage parentPage) {
        super.onDraw(canvas, theme, screenBounds, parentPage);
        int fullBarCount = Math.round(this.mLevel * 5.0f);
        Point offset = new Point(getElementRect(theme, screenBounds.width(), screenBounds.height()).left, getElementRect(theme, screenBounds.width(), screenBounds.height()).top);
        for (int i = 0; i < 5; i++) {
            int xOffset = i * 7;
            canvas.drawRect(new Rect(offset.x + xOffset, offset.y, offset.x + xOffset + 4, offset.y + 13), theme.getSolidColorPaint(getInActiveColor()));
        }
        int fullBarCount2 = Math.max(1, fullBarCount);
        for (int i2 = 0; i2 < fullBarCount2; i2++) {
            int xOffset2 = i2 * 7;
            canvas.drawRect(new Rect(offset.x + xOffset2, offset.y, offset.x + xOffset2 + 4, offset.y + 13), theme.getSolidColorPaint(getActiveColor()));
        }
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    protected void calculateRect(Theme theme, int screenWidth, int screenHeight, Rect out_bounds) {
        super.calculateRect(theme, screenWidth, screenHeight, out_bounds);
        out_bounds.set(0, 0, 32, 13);
    }
}
