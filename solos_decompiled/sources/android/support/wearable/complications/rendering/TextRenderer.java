package android.support.wearable.complications.rendering;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import java.util.Objects;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class TextRenderer {
    private static final int DEFAULT_MINIMUM_CHARACTERS_SHOWN = 7;
    private static final int MEDIUM_BLACK_CIRCLE_EMOJI = 9899;
    private static final int TEXT_SIZE_STEP_SIZE = 1;

    @Nullable
    private String mAmbientModeText;
    private boolean mNeedCalculateBounds;
    private boolean mNeedUpdateLayout;
    private TextPaint mPaint;
    private float mRelativePaddingBottom;
    private float mRelativePaddingEnd;
    private float mRelativePaddingStart;
    private float mRelativePaddingTop;
    private StaticLayout mStaticLayout;

    @Nullable
    private CharSequence mText;
    private final Rect mBounds = new Rect();
    private int mGravity = 17;
    private int mMaxLines = 1;
    private int mMinCharactersShown = 7;
    private TextUtils.TruncateAt mEllipsize = TextUtils.TruncateAt.END;
    private Layout.Alignment mAlignment = Layout.Alignment.ALIGN_CENTER;
    private final Rect mWorkingRect = new Rect();
    private final Rect mOutputRect = new Rect();
    private boolean mInAmbientMode = false;

    public void draw(Canvas canvas, Rect bounds) {
        if (!TextUtils.isEmpty(this.mText)) {
            if (this.mNeedUpdateLayout || this.mBounds.width() != bounds.width()) {
                updateLayout(bounds.width());
                this.mNeedUpdateLayout = false;
                this.mNeedCalculateBounds = true;
            }
            if (this.mNeedCalculateBounds || !this.mBounds.equals(bounds)) {
                this.mBounds.set(bounds);
                calculateBounds();
                this.mNeedCalculateBounds = false;
            }
            canvas.save();
            canvas.translate(this.mOutputRect.left, this.mOutputRect.top);
            this.mStaticLayout.draw(canvas);
            canvas.restore();
        }
    }

    public void requestUpdateLayout() {
        this.mNeedUpdateLayout = true;
    }

    public void setText(@Nullable CharSequence text) {
        if (!Objects.equals(this.mText, text)) {
            this.mText = text;
            this.mAmbientModeText = EmojiHelper.replaceEmoji(text, MEDIUM_BLACK_CIRCLE_EMOJI);
            this.mNeedUpdateLayout = true;
        }
    }

    public void setPaint(TextPaint paint) {
        this.mPaint = paint;
        this.mNeedUpdateLayout = true;
    }

    public void setRelativePadding(float start, float top, float end, float bottom) {
        if (this.mRelativePaddingStart != start || this.mRelativePaddingTop != top || this.mRelativePaddingEnd != end || this.mRelativePaddingBottom != bottom) {
            this.mRelativePaddingStart = start;
            this.mRelativePaddingTop = top;
            this.mRelativePaddingEnd = end;
            this.mRelativePaddingBottom = bottom;
            this.mNeedUpdateLayout = true;
        }
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            this.mGravity = gravity;
            this.mNeedCalculateBounds = true;
        }
    }

    public void setMaxLines(int maxLines) {
        if (this.mMaxLines != maxLines) {
            this.mMaxLines = maxLines;
            this.mNeedUpdateLayout = true;
        }
    }

    public void setMinimumCharactersShown(int minCharactersShown) {
        if (this.mMinCharactersShown != minCharactersShown) {
            this.mMinCharactersShown = minCharactersShown;
            this.mNeedUpdateLayout = true;
        }
    }

    public void setEllipsize(@Nullable TextUtils.TruncateAt ellipsize) {
        if (this.mEllipsize != ellipsize) {
            this.mEllipsize = ellipsize;
            this.mNeedUpdateLayout = true;
        }
    }

    public void setAlignment(Layout.Alignment alignment) {
        if (this.mAlignment != alignment) {
            this.mAlignment = alignment;
            this.mNeedUpdateLayout = true;
        }
    }

    public boolean hasText() {
        return !TextUtils.isEmpty(this.mText);
    }

    public boolean isLtr() {
        return this.mStaticLayout.getParagraphDirection(0) == 1;
    }

    public void setInAmbientMode(boolean inAmbientMode) {
        if (this.mInAmbientMode != inAmbientMode) {
            this.mInAmbientMode = inAmbientMode;
            if (!TextUtils.equals(this.mAmbientModeText, this.mText)) {
                this.mNeedUpdateLayout = true;
            }
        }
    }

    private void updateLayout(int width) {
        if (this.mPaint == null) {
            setPaint(new TextPaint());
        }
        int availableWidth = (int) (width * ((1.0f - this.mRelativePaddingStart) - this.mRelativePaddingEnd));
        TextPaint paint = new TextPaint(this.mPaint);
        float textWidth = paint.measureText(this.mText, 0, this.mText.length());
        if (textWidth > availableWidth) {
            int charactersShown = this.mMinCharactersShown;
            if (this.mEllipsize != null && this.mEllipsize != TextUtils.TruncateAt.MARQUEE) {
                charactersShown++;
            }
            CharSequence textToFit = this.mText.subSequence(0, Math.min(charactersShown, this.mText.length()));
            for (float textWidth2 = paint.measureText(textToFit, 0, textToFit.length()); textWidth2 > availableWidth; textWidth2 = paint.measureText(textToFit, 0, textToFit.length())) {
                paint.setTextSize(paint.getTextSize() - 1.0f);
            }
        }
        CharSequence text = this.mInAmbientMode ? this.mAmbientModeText : this.mText;
        StaticLayout.Builder builder = StaticLayout.Builder.obtain(text, 0, text.length(), paint, availableWidth);
        builder.setBreakStrategy(1);
        builder.setEllipsize(this.mEllipsize);
        builder.setHyphenationFrequency(2);
        builder.setMaxLines(this.mMaxLines);
        builder.setAlignment(this.mAlignment);
        this.mStaticLayout = builder.build();
    }

    private void calculateBounds() {
        int layoutDirection = isLtr() ? 0 : 1;
        int leftPadding = (int) ((isLtr() ? this.mRelativePaddingStart : this.mRelativePaddingEnd) * this.mBounds.width());
        int rightPadding = (int) ((isLtr() ? this.mRelativePaddingEnd : this.mRelativePaddingStart) * this.mBounds.width());
        int topPadding = (int) (this.mBounds.height() * this.mRelativePaddingTop);
        int bottomPadding = (int) (this.mBounds.height() * this.mRelativePaddingBottom);
        this.mWorkingRect.set(this.mBounds.left + leftPadding, this.mBounds.top + topPadding, this.mBounds.right - rightPadding, this.mBounds.bottom - bottomPadding);
        Gravity.apply(this.mGravity, this.mStaticLayout.getWidth(), this.mStaticLayout.getHeight(), this.mWorkingRect, this.mOutputRect, layoutDirection);
    }
}
