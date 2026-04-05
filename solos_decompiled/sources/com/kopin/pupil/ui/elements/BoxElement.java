package com.kopin.pupil.ui.elements;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.PageParser;

/* JADX INFO: loaded from: classes25.dex */
public class BoxElement extends BaseElement implements Sizable {
    private Point mSize;

    public BoxElement() {
        this(PageParser.BOX_TAG);
    }

    public BoxElement(String name) {
        super(name);
        this.mSize = new Point(0, 0);
        setBackColor(ViewCompat.MEASURED_STATE_MASK);
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public int getColor() {
        return super.getBackColor();
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public void setColor(int backColor) {
        super.setBackColor(backColor);
    }

    public final Point getSize() {
        return this.mSize;
    }

    public void setSize(Point size) {
        this.mHasChanged = true;
        this.mSize = new Point(size);
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getInnerWidth() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("A width must be specified for a box element");
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getInnerHeight() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("A height must be specified for a box element");
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getWidth() {
        return this.mSize.x;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public void setWidth(int width) {
        this.mHasChanged = true;
        this.mSize.x = width;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getHeight() {
        return this.mSize.y;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public void setHeight(int height) {
        this.mHasChanged = true;
        this.mSize.y = height;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    protected void calculateRect(Theme theme, int width, int height, Rect out_bounds) {
        super.calculateRect(theme, width, height, out_bounds);
        out_bounds.set(0, 0, getWidth(), getHeight());
    }
}
