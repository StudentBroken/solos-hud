package com.kopin.pupil.ui.elements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import com.kopin.pupil.pagerenderer.StringHelper;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.ui.BasePage;
import com.kopin.pupil.util.MutableInt;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes25.dex */
public final class SingleLineTextElement extends BaseElement {
    public static final String TAG = "SingleLineTextElement";
    private final List<Part> mParts = new ArrayList();
    private int mLeading = 0;
    private int mMaxWidth = -1;

    public static abstract class Part {
        SingleLineTextElement mParent = null;

        abstract int getSize();
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public void onDraw(Canvas canvas, Theme theme, Rect screenBounds, BasePage parentPage) {
        super.onDraw(canvas, theme, screenBounds, parentPage);
        Rect rect = getElementRect(theme, screenBounds.width(), screenBounds.height());
        int positionX = 0;
        int yOffset = getStartYOffset(theme);
        int positionY = rect.top + yOffset;
        int currentSpacing = 0;
        for (Part part : getParts()) {
            if (part instanceof LineEndPart) {
                positionY += getLeading() + currentSpacing;
                currentSpacing = 0;
                positionX = 0;
            } else if (part instanceof TextPart) {
                TextPart textPart = (TextPart) part;
                Paint textPaint = theme.getTextPaint(textPart.getColor(), textPart.getSize(), textPart.getStyle());
                if (textPaint != null) {
                    String text = textPart.getText();
                    if (this.mMaxWidth > 0) {
                        int maxWidth = this.mMaxWidth - positionX;
                        int height = (int) textPaint.getFontSpacing();
                        List<String> lines = StringHelper.wrap(0, text, textPaint, maxWidth, height, true, new MutableInt(0), false);
                        if (lines != null && !lines.isEmpty()) {
                            text = lines.get(0);
                        } else {
                            text = "";
                        }
                    }
                    if (!textPart.alignTop()) {
                        canvas.drawText(text, rect.left + positionX, positionY, textPaint);
                    } else {
                        canvas.drawText(text, rect.left + positionX, (positionY - yOffset) + textPart.getOffsetTop(), textPaint);
                    }
                    currentSpacing = Math.max(currentSpacing, (int) textPaint.getFontSpacing());
                    positionX = (int) (positionX + textPaint.measureText(textPart.getText()));
                }
            }
        }
    }

    public List<Part> getParts() {
        return this.mParts;
    }

    public void addPart(Part part) {
        part.mParent = this;
        this.mParts.add(part);
        this.mHasChanged = true;
    }

    public void setLeading(int leading) {
        this.mLeading = leading;
    }

    public int getLeading() {
        return this.mLeading;
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public void removePart(Part part) {
        part.mParent = null;
        this.mParts.remove(part);
        this.mHasChanged = true;
    }

    public void removeAllTextParts() {
        for (Part part : this.mParts) {
            part.mParent = null;
        }
        this.mParts.clear();
        this.mHasChanged = true;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    protected void calculateRect(Theme theme, int screenWidth, int screenHeight, Rect out_bounds) {
        super.calculateRect(theme, screenWidth, screenHeight, out_bounds);
        int totalWidth = 0;
        int totalHeight = 0;
        int currentLineWidth = 0;
        int currentLineHeight = 0;
        for (Part part : this.mParts) {
            if (part instanceof LineEndPart) {
                totalHeight += currentLineHeight;
                currentLineHeight = 0;
                currentLineWidth = 0;
            } else if (part instanceof TextPart) {
                TextPart textPart = (TextPart) part;
                Paint textPaint = theme.getTextPaint(textPart.getColor(), textPart.getSize(), textPart.getStyle());
                currentLineWidth = (int) (((double) currentLineWidth) + Math.ceil(textPaint.measureText(textPart.mText)));
                totalWidth = Math.max(totalWidth, currentLineWidth);
                currentLineHeight = Math.max(currentLineHeight, (int) textPaint.getFontSpacing()) + getLeading();
            }
        }
        out_bounds.set(0, 0, totalWidth, totalHeight + currentLineHeight);
    }

    public int getStartYOffset(Theme theme) {
        if (this.mParts == null || this.mParts.size() <= 0) {
            return 0;
        }
        return getBaseline(theme, this.mParts.get(0));
    }

    public int getLineHeight(Theme theme, Part part, int positionXOffset) {
        if (part == null || !(part instanceof TextPart)) {
            return 0;
        }
        TextPart textPart = (TextPart) part;
        Paint paint = theme.getTextPaint(textPart.getColor(), textPart.getSize(), textPart.getStyle());
        int index = this.mParts.indexOf(part);
        int lineHeight = (int) paint.getFontSpacing();
        for (int i = index + 1; i < this.mParts.size(); i++) {
            Part tempPart = this.mParts.get(i);
            if (tempPart instanceof TextPart) {
                lineHeight = Math.max(lineHeight, (int) paint.getFontSpacing());
            } else {
                return lineHeight;
            }
        }
        return lineHeight;
    }

    private int getBaseline(Theme theme, Part part) {
        int index = this.mParts.indexOf(part);
        TextPart biggestTextPart = null;
        int biggestSize = -1;
        for (int i = index; i < this.mParts.size(); i++) {
            Part tempPart = this.mParts.get(i);
            if (tempPart.getSize() > biggestSize && (tempPart instanceof TextPart)) {
                biggestSize = tempPart.getSize();
                biggestTextPart = (TextPart) tempPart;
            }
        }
        if (biggestTextPart == null) {
            return 0;
        }
        Paint paint = theme.getTextPaint(biggestTextPart.getColor(), biggestTextPart.getSize(), biggestTextPart.getStyle());
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return (int) (-metrics.ascent);
    }

    private int getBaseline(Theme theme, Part part, int positionXOffset) {
        if (part == null || !(part instanceof TextPart)) {
            return 0;
        }
        TextPart textPart = (TextPart) part;
        Paint paint = theme.getTextPaint(textPart.getColor(), textPart.getSize(), textPart.getStyle());
        Paint.FontMetrics metrics = paint.getFontMetrics();
        int index = this.mParts.indexOf(part);
        int lineHeight = (int) (-metrics.ascent);
        for (int i = index + 1; i < this.mParts.size(); i++) {
            Part tempPart = this.mParts.get(i);
            if (tempPart instanceof TextPart) {
                lineHeight = Math.max(lineHeight, (int) (-metrics.ascent));
            } else {
                return lineHeight;
            }
        }
        return lineHeight;
    }

    public static final class TextPart extends Part implements TextInterface {
        private int mColor;
        protected String mName;
        private int mOffsetTop;
        protected int mSize;
        protected int mStyle;
        protected String mText;

        public TextPart() {
            this.mColor = ViewCompat.MEASURED_STATE_MASK;
            this.mSize = 0;
            this.mStyle = 0;
            this.mText = "";
            this.mName = "";
            this.mOffsetTop = -1;
        }

        public TextPart(int color, int size, String text, byte style) {
            this.mColor = ViewCompat.MEASURED_STATE_MASK;
            this.mSize = 0;
            this.mStyle = 0;
            this.mText = "";
            this.mName = "";
            this.mOffsetTop = -1;
            this.mColor = color;
            this.mSize = size;
            this.mStyle = style;
            this.mText = text.replace("\n", "").replace("\r", "");
        }

        public String toString() {
            return this.mText;
        }

        public void setName(String name) {
            setHasChanged();
            this.mName = name;
        }

        public String getName() {
            return this.mName;
        }

        public int getColor() {
            return this.mColor;
        }

        public void setColor(int color) {
            setHasChanged();
            this.mColor = color;
        }

        @Override // com.kopin.pupil.ui.elements.SingleLineTextElement.Part
        public int getSize() {
            return this.mSize;
        }

        public void setSize(int size) {
            setHasChanged();
            this.mSize = size;
        }

        public String getText() {
            return this.mText;
        }

        public boolean alignTop() {
            return this.mOffsetTop > 0;
        }

        public int getOffsetTop() {
            return this.mOffsetTop;
        }

        public void setOffsetTop(int offsetTop) {
            this.mOffsetTop = offsetTop;
        }

        @Override // com.kopin.pupil.ui.elements.TextInterface
        public void setText(String text) {
            setHasChanged();
            if (text == null) {
                this.mText = "";
            } else {
                this.mText = text.replace("\n", "").replace("\r", "");
            }
        }

        public int getStyle() {
            return this.mStyle;
        }

        public void setStyle(int style) {
            setHasChanged();
            this.mStyle = style;
        }

        public boolean hasChanged() {
            return this.mParent.mHasChanged;
        }

        private void setHasChanged() {
            if (this.mParent != null) {
                this.mParent.mHasChanged = true;
            }
        }
    }

    public static final class LineEndPart extends Part {
        @Override // com.kopin.pupil.ui.elements.SingleLineTextElement.Part
        int getSize() {
            return 0;
        }
    }
}
