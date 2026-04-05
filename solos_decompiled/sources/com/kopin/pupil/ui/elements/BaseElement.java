package com.kopin.pupil.ui.elements;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.pagerenderer.ThemeElement;
import com.kopin.pupil.ui.BasePage;
import com.kopin.pupil.ui.elements.RelativeElement;

/* JADX INFO: loaded from: classes25.dex */
public abstract class BaseElement implements RelativeElement {
    public static final int ALIGN_BOTTOM = 8;
    public static final int ALIGN_CENTRE = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ALIGN_TOP = 4;
    public static final int DEFAULT_ELEMENT_DISTANCE = 9;
    private boolean configured;
    private String elementName;
    private RelativeElement elementOnX;
    private int mActiveColor;
    private int mAlignment;
    private int mBackColor;
    private int mBorderColor;
    private int mBorderThickness;
    protected Rect mBounds;
    private int mColor;
    protected boolean mHasChanged;
    private int mInactiveColor;
    private String mName;
    private Point mOffset;
    protected BasePage mParentPage;
    protected Rect mPreviousBounds;
    private String mTag;
    private RelativeElement.RelativePosition relativeX;

    public BaseElement() {
        this("element");
    }

    public BaseElement(String name) {
        this.mAlignment = 0;
        this.mOffset = new Point(0, 0);
        this.mColor = ViewCompat.MEASURED_STATE_MASK;
        this.mActiveColor = 0;
        this.mInactiveColor = 0;
        this.mBackColor = 0;
        this.mBorderColor = 0;
        this.mBorderThickness = 0;
        this.mName = null;
        this.mTag = null;
        this.mHasChanged = true;
        this.mBounds = null;
        this.mPreviousBounds = null;
        this.mParentPage = null;
        this.configured = false;
        this.relativeX = RelativeElement.RelativePosition.NONE;
        this.mName = name;
    }

    public void styleFromTheme(Theme theme) {
        ThemeElement element = theme.getTheme(getClass());
        if (element != null) {
            this.mColor = element.color;
            this.mBackColor = element.backColor;
            this.mActiveColor = element.activeColor;
            this.mInactiveColor = element.inactiveColor;
            this.mBorderColor = element.borderColor;
            this.mBorderThickness = element.borderThickness;
        }
    }

    public boolean hasChanged() {
        return this.mHasChanged;
    }

    public void onDraw(Canvas canvas, Theme theme, Rect screenBounds, BasePage parentPage) {
        Rect rect = getElementRect(theme, screenBounds.width(), screenBounds.height());
        if (this.mBorderThickness > 0 && isVisible(this.mBorderColor)) {
            canvas.restore();
            Rect tempRect = new Rect(rect);
            tempRect.inset(-this.mBorderThickness, -this.mBorderThickness);
            canvas.drawRect(rect, theme.getStrokePaint(this.mBorderColor, this.mBorderThickness / 2));
            canvas.save();
            canvas.clipRect(rect);
        }
        if (isVisible(this.mBackColor)) {
            canvas.drawRect(rect, theme.getSolidColorPaint(this.mBackColor));
        }
    }

    public int getAlignment() {
        return this.mAlignment;
    }

    public void setAlignment(int alignment) {
        this.mAlignment = alignment;
        this.mHasChanged = true;
    }

    @Override // com.kopin.pupil.ui.elements.RelativeElement
    public Point getOffset() {
        return this.mOffset;
    }

    public void setOffset(Point offset) {
        this.mOffset = offset;
        this.mHasChanged = true;
    }

    public void setOffsetX(int x) {
        this.mOffset.x = x;
    }

    public void setOffsetY(int y) {
        this.mOffset.y = y;
    }

    public void setOffset(int x, int y) {
        setOffset(new Point(x, y));
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
        this.mHasChanged = true;
    }

    public void setParent(BasePage parent) {
        this.mParentPage = parent;
    }

    public int getActiveColor() {
        return this.mActiveColor;
    }

    public void setActiveColor(int activeColor) {
        this.mActiveColor = activeColor;
    }

    public int getInActiveColor() {
        return this.mInactiveColor;
    }

    public void setInActiveColor(int inActiveColor) {
        this.mInactiveColor = inActiveColor;
    }

    public int getBackColor() {
        return this.mBackColor;
    }

    public void setBackColor(int backColor) {
        this.mBackColor = backColor;
        this.mHasChanged = true;
    }

    public int getBorderColor() {
        return this.mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        this.mBorderColor = borderColor;
        this.mHasChanged = true;
    }

    public int getBorderThickness() {
        return this.mBorderThickness;
    }

    public void setBorderThickness(int thickness) {
        this.mBorderThickness = thickness;
        this.mHasChanged = true;
    }

    public String getName() {
        return this.mName;
    }

    public Bundle getProperties() {
        return null;
    }

    public void setName(String name) {
        this.mName = name;
        this.mHasChanged = true;
    }

    public String getTag() {
        return this.mTag;
    }

    public void setTag(String tag) {
        this.mTag = tag;
        this.mHasChanged = true;
    }

    @Override // com.kopin.pupil.ui.elements.RelativeElement
    public final Rect getElementRect(Theme theme, int screenWidth, int screenHeight) {
        configure(theme, screenWidth, screenHeight);
        if (this.mBounds == null) {
            this.mBounds = new Rect(0, 0, 0, 0);
        }
        if (this.mHasChanged) {
            calculateRect(theme, screenWidth, screenHeight, this.mBounds);
            placeRect(this.mBounds, screenWidth, screenHeight);
        }
        this.mHasChanged = false;
        if (this.mPreviousBounds != null) {
            Rect temp = new Rect(this.mPreviousBounds);
            this.mPreviousBounds = this.mBounds;
            return temp;
        }
        Rect temp2 = new Rect(this.mBounds);
        this.mPreviousBounds = temp2;
        return temp2;
    }

    protected void calculateRect(Theme theme, int screenWidth, int screenHeight, Rect out_bounds) {
        if (out_bounds == null) {
        }
    }

    protected final void placeRect(Rect rect, int screenWidth, int screenHeight) {
        Point size = new Point(rect.width(), rect.height());
        if ((getAlignment() & 1) == 1) {
            rect.left = getOffset().x;
            rect.right = rect.left + size.x;
        } else if ((getAlignment() & 2) == 2) {
            rect.right = screenWidth - getOffset().x;
            rect.left = rect.right - size.x;
        } else {
            rect.left = ((screenWidth - size.x) / 2) + getOffset().x;
            rect.right = rect.left + size.x;
        }
        if ((getAlignment() & 4) == 4) {
            rect.top = getOffset().y;
            rect.bottom = rect.top + size.y;
        } else if ((getAlignment() & 8) == 8) {
            rect.bottom = screenHeight - getOffset().y;
            rect.top = rect.bottom - size.y;
        } else {
            rect.top = ((screenHeight - size.y) / 2) + getOffset().y;
            rect.bottom = rect.top + size.y;
        }
    }

    private boolean isVisible(int colour) {
        return (colour >> 24) != 0;
    }

    @Override // com.kopin.pupil.ui.elements.RelativeElement
    public void setRelation(String elementName, RelativeElement.RelativePosition pos) {
        switch (pos) {
            case NONE:
                this.relativeX = RelativeElement.RelativePosition.NONE;
                this.elementOnX = null;
                break;
            case RIGHT_OF:
                this.relativeX = pos;
                this.elementName = elementName;
                break;
        }
    }

    @Override // com.kopin.pupil.ui.elements.RelativeElement
    public void removeRelation(RelativeElement.RelativePosition pos) {
        switch (pos) {
            case RIGHT_OF:
                this.elementOnX = null;
                this.relativeX = RelativeElement.RelativePosition.NONE;
                break;
        }
    }

    @Override // com.kopin.pupil.ui.elements.RelativeElement
    public boolean hasRelativePosition() {
        return this.relativeX != RelativeElement.RelativePosition.NONE;
    }

    @Override // com.kopin.pupil.ui.elements.RelativeElement
    public void configure(Theme theme, int screenWidth, int screenHeight) {
        if (!this.configured && hasRelativePosition() && this.elementOnX != null) {
            this.configured = true;
            switch (this.relativeX) {
                case RIGHT_OF:
                    Rect rect = this.elementOnX.getElementRect(theme, screenWidth, screenHeight);
                    setAlignment(getAlignment());
                    setOffset(rect.right + 9, rect.top);
                    break;
            }
        }
    }

    @Override // com.kopin.pupil.ui.elements.RelativeElement
    public void setElement(RelativeElement element) {
        this.elementOnX = element;
    }

    @Override // com.kopin.pupil.ui.elements.RelativeElement
    public String getElementName() {
        return this.elementName;
    }
}
