package com.kopin.pupil.ui.elements;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import com.kopin.pupil.pagerenderer.StringHelper;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.pagerenderer.ThemeElement;
import com.kopin.pupil.ui.BasePage;
import com.kopin.pupil.ui.PageParserHelper;
import com.kopin.pupil.util.MathHelper;
import com.kopin.pupil.util.MutableInt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes25.dex */
public final class TextElement extends BaseElement implements TextInterface, Sizable {
    private static final int ICON_MARGIN = 6;
    public static final String TEXT_CURRENT_PAGE = "currentpage";
    public static final String TEXT_PAGE_COUNT = "pagecount";
    public static final String TEXT_PAGING_ATTRIBUTE = "paging";
    private TextInterface mCurrentPageIdView;
    private String mCurrentPageIdViewId;
    private TextInterface mTotalPageCounterView;
    private String mTotalPageCounterViewId;
    private int mWrapWidth = 0;
    private int mWrapHeight = 0;
    private String mText = "";
    private int mTextSize = 0;
    private int mTextStyle = 0;
    private boolean mEllipse = true;
    private boolean mIsSingleLine = false;
    private List<List<String>> mPages = new ArrayList();
    private final Object mPageLock = new Object();
    private int mPageID = 0;
    private boolean mIsPaging = false;
    private boolean mHasWrapped = false;
    private boolean mIsFit = false;
    private Theme mTheme = null;
    private int mLeading = 0;
    private int mInnerWidth = -1;
    private int mInnerHeight = -1;
    private PageParserHelper.HorizontalAlign mHorizontalAlign = PageParserHelper.HorizontalAlign.LEFT;
    private Bitmap mImage = null;
    private String mSource = null;
    private int mWidth = -1;
    private int mHeight = -1;

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public void styleFromTheme(Theme theme) {
        super.styleFromTheme(theme);
        this.mTheme = theme;
        ThemeElement element = theme.getTheme(getClass());
        this.mTextSize = element.textSize;
        this.mTextStyle = element.textStyle;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public void onDraw(Canvas canvas, Theme theme, Rect screenBounds, BasePage parentPage) {
        List<String> lines;
        super.onDraw(canvas, theme, screenBounds, parentPage);
        Paint paint = theme.getTextPaint(getColor(), this.mTextSize, this.mTextStyle);
        wrapText(paint);
        Rect rect = getElementRect(theme, screenBounds.width(), screenBounds.height());
        int offsetY = getStartYOffset(theme);
        synchronized (this.mPageLock) {
            this.mPageID = MathHelper.clamp(0, this.mPages.size() - 1, this.mPageID);
            lines = this.mPages.get(this.mPageID);
        }
        if (lines != null) {
            int i = 0;
            int iconLeft = 0;
            int iconTop = rect.top - 6;
            Iterator<String> it = lines.iterator();
            while (it.hasNext()) {
                String line = it.next().trim();
                int left = rect.left;
                switch (this.mHorizontalAlign) {
                    case CENTER:
                        left = rect.left + (((int) (rect.width() - paint.measureText(line))) / 2);
                        if (this.mWidth > 0) {
                            int left2 = rect.left + (((int) (rect.width() - ((paint.measureText(line) + 6.0f) + this.mWidth))) / 2);
                            left = left2 + this.mWidth + 6;
                        }
                        break;
                    case RIGHT:
                        left = rect.right - ((int) paint.measureText(line));
                        break;
                    case LEFT:
                        left = rect.left;
                        if (this.mWidth > 0) {
                            left += this.mWidth + 6;
                        }
                        break;
                }
                if (i == 0 && this.mSource != null) {
                    iconLeft = left - (this.mWidth + 6);
                }
                canvas.drawText(line, left, rect.top + offsetY, paint);
                offsetY = (int) (offsetY + paint.getFontSpacing());
                i++;
            }
            if (this.mImage != null) {
                if (hasChanged()) {
                    this.mHasChanged = false;
                    this.mImage = parentPage.getImage(this.mSource);
                }
                Rect rect2 = new Rect(iconLeft, iconTop, this.mWidth + iconLeft, this.mHeight + iconTop);
                canvas.drawBitmap(this.mImage, (Rect) null, rect2, theme.getMaskPaint(getColor()));
                return;
            }
            if (this.mSource != null) {
                this.mImage = parentPage.getImage(this.mSource);
                if (this.mImage != null) {
                    if (iconLeft < 0) {
                        iconLeft = 0;
                    }
                    Rect rect22 = new Rect(iconLeft, iconTop, this.mWidth + iconLeft, this.mHeight + iconTop);
                    canvas.drawBitmap(this.mImage, (Rect) null, rect22, theme.getMaskPaint(getColor()));
                    this.mImage = null;
                }
            }
        }
    }

    public int getWrapWidth() {
        return this.mIsFit ? getInnerWidth() : this.mWrapWidth;
    }

    public void setLeading(int leading) {
        this.mLeading = leading;
    }

    public int getLeading() {
        return this.mLeading;
    }

    public void setWrapWidth(int wrapWidth) {
        this.mHasChanged = true;
        this.mHasWrapped = false;
        this.mWrapWidth = Math.max(wrapWidth, 0);
    }

    public int getWrapHeight() {
        return this.mWrapHeight;
    }

    public void setWrapHeight(int wrapHeight) {
        this.mHasChanged = true;
        this.mHasWrapped = false;
        this.mWrapHeight = Math.max(wrapHeight, 0);
    }

    public int getSize() {
        return this.mTextSize;
    }

    public void setSize(int size) {
        this.mHasChanged = true;
        this.mHasWrapped = false;
        this.mTextSize = size;
    }

    public int getStyle() {
        return this.mTextStyle;
    }

    public void setStyle(int style) {
        this.mHasChanged = true;
        this.mHasWrapped = false;
        this.mTextStyle = style;
    }

    public String getText() {
        return this.mText;
    }

    @Override // com.kopin.pupil.ui.elements.TextInterface
    public void setText(String text) {
        this.mHasChanged = this.mHasChanged || !text.trim().contentEquals(this.mText);
        this.mHasWrapped = false;
        this.mText = text.trim();
        resetTotalPageCounterView();
    }

    public boolean isEllipse() {
        return this.mEllipse;
    }

    public void setEllipse(boolean ellipse) {
        this.mHasChanged = true;
        this.mHasWrapped = false;
        this.mEllipse = ellipse;
    }

    public boolean isSingleLine() {
        return this.mIsSingleLine;
    }

    public void setSingleLine(boolean singleLine) {
        this.mIsSingleLine = singleLine;
    }

    public void setCurrentPageIdView(TextInterface view) {
        this.mCurrentPageIdView = view;
    }

    public void setTotalPageCounterView(TextInterface view) {
        this.mTotalPageCounterView = view;
    }

    public void setCurrentPageIdViewId(String currentPageIdViewId) {
        this.mCurrentPageIdViewId = currentPageIdViewId;
    }

    public void setTotalPageCounterViewId(String totalPageCounterViewId) {
        this.mTotalPageCounterViewId = totalPageCounterViewId;
    }

    public void setHorizontalAlign(PageParserHelper.HorizontalAlign horizontalAlign) {
        this.mHorizontalAlign = horizontalAlign;
    }

    public void resetTotalPageCounterView() {
        if (this.mTotalPageCounterView != null) {
            this.mTotalPageCounterView.setText(String.valueOf(getPageCount()));
        }
    }

    public String getCurrentPageIdViewId() {
        return this.mCurrentPageIdViewId;
    }

    public String getTotalPageCounterViewId() {
        return this.mTotalPageCounterViewId;
    }

    public int getPageCount() {
        wrapText(this.mTheme.getTextPaint(getColor(), getSize(), getStyle()));
        return this.mPages.size();
    }

    public boolean isPaging() {
        return this.mIsPaging;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    public Bundle getProperties() {
        Bundle properties = new Bundle();
        properties.putBoolean("paging", isPaging());
        properties.putInt(TEXT_CURRENT_PAGE, getPage());
        properties.putInt(TEXT_PAGE_COUNT, getPageCount());
        return properties;
    }

    public void setPaging(boolean enable) {
        this.mHasChanged = true;
        this.mHasWrapped = false;
        this.mIsPaging = enable;
    }

    public void setPage(int pageID) {
        this.mHasChanged = true;
        this.mPageID = pageID;
        if (this.mCurrentPageIdView != null) {
            this.mCurrentPageIdView.setText(String.valueOf(pageID + 1));
        }
    }

    public int getPage() {
        return this.mPageID;
    }

    public int getStartYOffset(Theme theme) {
        return getLineHeight(theme);
    }

    public int getLineHeight(Theme theme) {
        int lineHeight = (int) theme.getTextPaint(getColor(), getSize(), getStyle()).getTextSize();
        return lineHeight;
    }

    @Override // com.kopin.pupil.ui.elements.BaseElement
    protected void calculateRect(Theme theme, int screenWidth, int screenHeight, Rect out_bounds) {
        super.calculateRect(theme, screenWidth, screenHeight, out_bounds);
        int wrapHeight = this.mWrapHeight;
        if (isSingleLine()) {
            wrapHeight = (int) theme.getTextPaint(getColor(), getSize(), getStyle()).getFontSpacing();
        }
        out_bounds.set(0, 0, getWrapWidth(), wrapHeight);
    }

    private void wrapText(Paint textPaint) {
        if (!this.mHasWrapped) {
            List<List<String>> pages = new ArrayList<>();
            int wrapHeight = this.mWrapHeight;
            if (isSingleLine()) {
                wrapHeight = (int) textPaint.getFontSpacing();
            }
            MutableInt endPosition = new MutableInt(0);
            List<String> lines = StringHelper.wrap(0, this.mText, textPaint, getWrapWidth(), wrapHeight, this.mEllipse, endPosition, this.mIsPaging);
            pages.add(lines);
            if (this.mIsPaging) {
                int currentPosition = 0;
                while (true) {
                    currentPosition += endPosition.getValue();
                    if (currentPosition >= this.mText.length()) {
                        break;
                    }
                    List<String> lines2 = StringHelper.wrap(currentPosition, this.mText, textPaint, getWrapWidth(), wrapHeight, this.mEllipse, endPosition, this.mIsPaging);
                    pages.add(lines2);
                }
            }
            synchronized (this.mPageLock) {
                this.mPages = pages;
            }
        }
        this.mHasWrapped = true;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getInnerWidth() throws UnsupportedOperationException {
        if (this.mInnerWidth >= 0) {
            return this.mInnerWidth;
        }
        Paint paint = this.mTheme.getTextPaint(getColor(), this.mTextSize, this.mTextStyle);
        int iMeasureText = (int) paint.measureText(this.mText);
        this.mInnerWidth = iMeasureText;
        return iMeasureText;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getInnerHeight() throws UnsupportedOperationException {
        if (this.mInnerHeight >= 0) {
            return this.mInnerHeight;
        }
        Paint paint = this.mTheme.getTextPaint(getColor(), this.mTextSize, this.mTextStyle);
        int fontSpacing = (int) paint.getFontSpacing();
        this.mInnerHeight = fontSpacing;
        return fontSpacing;
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getWidth() {
        return getWrapWidth();
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public int getHeight() {
        return getWrapHeight();
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public void setWidth(int width) {
        if (width == -1) {
            width = getInnerWidth();
        }
        setWrapWidth(width);
    }

    @Override // com.kopin.pupil.ui.elements.Sizable
    public void setHeight(int height) {
        setWrapHeight(height);
    }

    public void setIsFit(boolean fit) {
        this.mHasChanged = true;
        this.mHasWrapped = false;
        this.mIsFit = fit;
    }

    public void setIcon(String iconName, String source, String iconWidth, String iconHeight) {
        this.mSource = source;
        this.mImage = null;
        this.mSource = source;
        this.mWidth = Integer.parseInt(iconWidth);
        this.mHeight = Integer.parseInt(iconHeight);
    }

    public void setSource(String source) {
        Log.i("TextElem", "setSource " + source);
        this.mSource = source;
        this.mHasChanged = true;
    }

    public String getSource() {
        return this.mSource;
    }
}
