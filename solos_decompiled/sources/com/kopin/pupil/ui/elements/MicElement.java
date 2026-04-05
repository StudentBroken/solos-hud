package com.kopin.pupil.ui.elements;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.BasePage;
import com.kopin.pupil.util.MathHelper;

/* JADX INFO: loaded from: classes25.dex */
public class MicElement extends BaseElement {
    private static final int BOX_COUNT = 7;
    private static final int BOX_HEIGHT = 6;
    private static final int BOX_SPACING = 1;
    public static final byte STATE_LISTENING = 2;
    public static final byte STATE_OFF = 0;
    public static final byte STATE_PROCESSING = 1;
    private int mBoxWidth;
    private int mHalfCellCount;
    private int mMiddleCell;
    private int mProcessingProgress;
    private boolean mPulseGoingDown;
    private float mPulseLevel;
    private Point mSize;
    private byte mState;
    private volatile int mVolume;

    public MicElement() {
        this(0, (byte) 2);
    }

    public MicElement(int width, byte state) {
        this(width, 6, state);
    }

    public MicElement(int width, int height, byte state) {
        this.mVolume = 0;
        this.mBoxWidth = 0;
        this.mMiddleCell = 0;
        this.mHalfCellCount = 0;
        this.mSize = new Point(0, 0);
        this.mState = (byte) 0;
        this.mProcessingProgress = 0;
        this.mPulseLevel = 0.0f;
        this.mPulseGoingDown = false;
        setSize(new Point(width, height));
        setBackColor(0);
        this.mBoxWidth = width / 7;
        this.mHalfCellCount = (int) Math.ceil(3.5d);
        this.mMiddleCell = (int) Math.floor(3.5d);
        this.mState = state;
    }

    public final Point getSize() {
        return this.mSize;
    }

    public void setSize(Point size) {
        this.mHasChanged = true;
        this.mSize = new Point(size);
    }

    public int getWidth() {
        return this.mSize.x;
    }

    public void setWidth(int width) {
        this.mHasChanged = true;
        this.mSize.x = width;
    }

    public int getHeight() {
        return this.mSize.y;
    }

    public void setHeight(int height) {
        this.mHasChanged = true;
        this.mSize.y = height;
    }

    public void setState(byte state) {
        this.mHasChanged = true;
        this.mState = state;
    }

    public void update(float percent) {
        this.mVolume = (int) (this.mHalfCellCount * percent);
        this.mHasChanged = true;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public void onDraw(Canvas canvas, Theme theme, Rect screenBounds, BasePage parentPage) {
        super.onDraw(canvas, theme, screenBounds, parentPage);
        Rect bounds = getElementRect(theme, screenBounds.width(), screenBounds.height());
        int volume = this.mVolume;
        for (int i = 0; i < 7; i++) {
            float left = bounds.left + ((this.mBoxWidth + 1) * i);
            float top = bounds.top;
            RectF rect = new RectF(left, top, this.mBoxWidth + left, 6.0f + top);
            canvas.drawRect(rect, theme.getSolidColorPaint(getInActiveColor()));
        }
        switch (this.mState) {
            case 1:
                this.mPulseLevel = 0.0f;
                int first = MathHelper.wrap(-1, 7, this.mProcessingProgress - 3);
                int second = MathHelper.wrap(-1, 7, this.mProcessingProgress - 2);
                int third = MathHelper.wrap(-1, 7, this.mProcessingProgress - 1);
                drawBlock(first, getActiveColor(), canvas, theme, bounds, 0.1f);
                drawBlock(second, getActiveColor(), canvas, theme, bounds, 0.5f);
                drawBlock(third, getActiveColor(), canvas, theme, bounds, 0.75f);
                drawBlock(this.mProcessingProgress, getActiveColor(), canvas, theme, bounds, 1.0f);
                this.mProcessingProgress++;
                this.mProcessingProgress = MathHelper.wrap(-1, 7, this.mProcessingProgress);
                break;
            case 2:
                if (volume > 0) {
                    this.mPulseLevel = 0.0f;
                    for (int i2 = this.mMiddleCell; i2 < this.mMiddleCell + volume; i2++) {
                        float left2 = bounds.left + ((this.mBoxWidth + 1) * i2);
                        float top2 = bounds.top;
                        RectF rect2 = new RectF(left2, top2, this.mBoxWidth + left2, 6.0f + top2);
                        canvas.drawRect(rect2, theme.getSolidColorPaint(getActiveColor()));
                    }
                    for (int i3 = this.mMiddleCell; i3 > this.mMiddleCell - volume; i3--) {
                        float left3 = bounds.left + ((this.mBoxWidth + 1) * i3);
                        float top3 = bounds.top;
                        RectF rect3 = new RectF(left3, top3, this.mBoxWidth + left3, 6.0f + top3);
                        canvas.drawRect(rect3, theme.getSolidColorPaint(getActiveColor()));
                    }
                } else {
                    if (this.mPulseLevel <= 0.0f) {
                        this.mPulseGoingDown = false;
                    } else if (this.mPulseLevel >= 1.0f) {
                        this.mPulseGoingDown = true;
                    }
                    if (this.mPulseGoingDown) {
                        this.mPulseLevel -= 0.1f;
                    } else {
                        this.mPulseLevel += 0.1f;
                    }
                    for (int i4 = 0; i4 < 7; i4++) {
                        drawBlock(i4, getActiveColor(), canvas, theme, bounds, MathHelper.clamp(0.0f, 1.0f, this.mPulseLevel));
                    }
                }
                break;
        }
    }

    private void drawBlock(int index, int color, Canvas canvas, Theme theme, Rect bounds, float alpha) {
        float left = bounds.left + ((this.mBoxWidth + 1) * index);
        float top = bounds.top;
        RectF rect = new RectF(left, top, this.mBoxWidth + left, 6.0f + top);
        canvas.drawRect(rect, theme.getSolidColorPaint((16777215 & color) | (((int) (255.0f * alpha)) << 24)));
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    protected void calculateRect(Theme theme, int screenWidth, int screenHeight, Rect out_bounds) {
        super.calculateRect(theme, screenWidth, screenHeight, out_bounds);
        out_bounds.set(0, 0, getWidth(), getHeight());
    }
}
