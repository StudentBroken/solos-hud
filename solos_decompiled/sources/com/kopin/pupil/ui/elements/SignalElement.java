package com.kopin.pupil.ui.elements;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.BasePage;
import com.kopin.pupil.util.MathHelper;

/* JADX INFO: loaded from: classes25.dex */
public class SignalElement extends BaseElement {
    private static final int BAR_COUNT = 5;
    private static final int BAR_INCREMENT_HEIGHT = 2;
    private static final int BAR_OFF_HEIGHT = 2;
    private static final int BAR_START_HEIGHT = 5;
    private static final int BAR_WIDTH = 4;
    private static final int ELEMENT_HEIGHT = 13;
    private static final int ELEMENT_WIDTH = 32;
    private static final int SPACING = 3;
    private float mStrength = 0.0f;

    public void setStrength(float strength) {
        this.mHasChanged = true;
        this.mStrength = MathHelper.clamp(0.0f, 1.0f, strength);
    }

    public float getStrength() {
        return this.mStrength;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public void onDraw(Canvas canvas, Theme theme, Rect screenBounds, BasePage parentPage) {
        super.onDraw(canvas, theme, screenBounds, parentPage);
        int fullBarCount = Math.round(this.mStrength * 5.0f);
        Point offset = new Point(getElementRect(theme, screenBounds.width(), screenBounds.height()).left, getElementRect(theme, screenBounds.width(), screenBounds.height()).top);
        for (int i = 0; i < 5; i++) {
            int xOffset = i * 7;
            canvas.drawRect(new Rect(offset.x + xOffset, offset.y + 11, offset.x + xOffset + 4, offset.y + 13), theme.getSolidColorPaint(getColor()));
        }
        for (int i2 = 0; i2 < fullBarCount; i2++) {
            int xOffset2 = i2 * 7;
            int yOffset = (i2 * 2) + 5;
            canvas.drawRect(new Rect(offset.x + xOffset2, offset.y + (13 - yOffset), offset.x + xOffset2 + 4, offset.y + 13), theme.getSolidColorPaint(getColor()));
        }
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    protected void calculateRect(Theme theme, int screenWidth, int screenHeight, Rect out_bounds) {
        super.calculateRect(theme, screenWidth, screenHeight, out_bounds);
        out_bounds.set(0, 0, 32, 13);
    }
}
