package com.kopin.pupil.ui.elements;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.BasePage;

/* JADX INFO: loaded from: classes25.dex */
public final class ImageElement extends BaseElement implements Sizable {
    private int mHeight;
    private Bitmap mImage;
    private String mSource;
    private int mWidth;

    public ImageElement() {
        this("image");
    }

    public ImageElement(String name) {
        super(name);
        this.mImage = null;
        this.mSource = null;
        this.mWidth = -1;
        this.mHeight = -1;
        setColor(-1);
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public void onDraw(Canvas canvas, Theme theme, Rect screenBounds, BasePage parentPage) {
        super.onDraw(canvas, theme, screenBounds, parentPage);
        if (this.mImage != null) {
            Rect rect = getElementRect(theme, screenBounds.width(), screenBounds.height());
            canvas.drawBitmap(this.mImage, (Rect) null, rect, theme.getMaskPaint(getColor()));
        } else if (this.mSource != null) {
            this.mImage = parentPage.getImage(this.mSource);
            if (this.mImage != null) {
                Rect rect2 = getElementRect(theme, screenBounds.width(), screenBounds.height());
                canvas.drawBitmap(this.mImage, (Rect) null, rect2, theme.getMaskPaint(getColor()));
                this.mImage = null;
            }
        }
    }

    public void draw(Canvas canvas, Theme theme, int scrWidth, int scrHeight) {
        Rect rect = getElementRect(theme, scrWidth, scrHeight);
        Paint mask = theme.getMaskPaint(getColor());
        Bitmap image = getImage();
        if (image != null) {
            double widthRatio = ((double) getWidth()) / ((double) image.getWidth());
            double heightRatio = ((double) getHeight()) / ((double) image.getHeight());
            if (heightRatio < widthRatio) {
                double width = ((double) image.getWidth()) * heightRatio;
                int diff = (int) ((width - ((double) getWidth())) / 2.0d);
                rect.left += diff;
                rect.right -= diff;
            } else if (heightRatio > widthRatio) {
                double height = ((double) image.getHeight()) * widthRatio;
                int diff2 = (int) ((height - ((double) getHeight())) / 2.0d);
                rect.top += diff2;
                rect.bottom -= diff2;
            }
            canvas.drawBitmap(getImage(), (Rect) null, rect, mask);
        }
    }

    public Bitmap getImage() {
        if (this.mImage == null) {
            if (this.mSource == null) {
                return null;
            }
            return this.mParentPage.getImage(this.mSource);
        }
        return this.mImage;
    }

    public final Point getSize() {
        return new Point(getWidth(), getHeight());
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
        this.mHasChanged = true;
    }

    public void setSize(Point size) {
        setWidth(size.x);
        setHeight(size.y);
        this.mHasChanged = true;
    }

    public void setImage(Bitmap image) {
        this.mImage = image;
        this.mHasChanged = true;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getInnerWidth() throws UnsupportedOperationException {
        Bitmap bitmap;
        if (this.mImage != null) {
            return this.mImage.getWidth();
        }
        if (this.mParentPage != null && (bitmap = this.mParentPage.getImage(this.mSource)) != null) {
            return bitmap.getWidth();
        }
        throw new UnsupportedOperationException("A width must be specified for an image element");
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getInnerHeight() throws UnsupportedOperationException {
        Bitmap bitmap;
        if (this.mImage != null) {
            return this.mImage.getHeight();
        }
        if (this.mParentPage != null && (bitmap = this.mParentPage.getImage(this.mSource)) != null) {
            return bitmap.getHeight();
        }
        throw new UnsupportedOperationException("A height must be specified for an image element");
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getWidth() {
        Bitmap image;
        if (this.mImage != null) {
            return this.mWidth < 0 ? this.mImage.getWidth() : this.mWidth;
        }
        if (this.mSource == null) {
            if (this.mWidth < 0) {
                return 0;
            }
            return this.mWidth;
        }
        if (this.mParentPage == null || (image = this.mParentPage.getImage(this.mSource)) == null) {
            return 0;
        }
        return this.mWidth < 0 ? image.getWidth() : this.mWidth;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public void setWidth(int width) {
        this.mWidth = width;
        this.mHasChanged = true;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getHeight() {
        Bitmap image;
        if (this.mImage != null) {
            return this.mHeight < 0 ? this.mImage.getHeight() : this.mHeight;
        }
        if (this.mSource == null) {
            if (this.mHeight < 0) {
                return 0;
            }
            return this.mHeight;
        }
        if (this.mParentPage == null || (image = this.mParentPage.getImage(this.mSource)) == null) {
            return 0;
        }
        return this.mHeight < 0 ? image.getHeight() : this.mHeight;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public void setHeight(int height) {
        this.mHeight = height;
        this.mHasChanged = true;
    }

    public String getSource() {
        return this.mSource;
    }

    public void setSource(String source) {
        this.mSource = source;
        this.mHasChanged = true;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    protected void calculateRect(Theme theme, int screenWidth, int screenHeight, Rect out_bounds) {
        super.calculateRect(theme, screenWidth, screenHeight, out_bounds);
        out_bounds.set(0, 0, getWidth(), getHeight());
    }
}
