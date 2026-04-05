package android.support.wearable.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.v4.view.GravityCompat;
import android.support.wearable.R;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import com.kopin.solos.view.graphics.Bar;
import com.nuance.android.vocalizer.VocalizerEngine;
import java.util.Objects;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public class ActionLabel extends View {
    private static final boolean DEBUG = false;
    static final int MAX_TEXT_SIZE = 60;
    static final int MIN_TEXT_SIZE = 10;
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final String TAG = "ActionLabel";
    private int mCurTextColor;
    private float mCurrentTextSize;
    private int mDrawMaxLines;
    private int mGravity;
    private Layout mLayout;
    private float mLineSpacingAdd;
    private float mLineSpacingMult;
    private int mMaxLines;
    private float mMaxTextSize;
    private float mMinTextSize;
    private float mSpacingAdd;
    private float mSpacingMult;
    private CharSequence mText;
    private ColorStateList mTextColor;
    private final TextPaint mTextPaint;

    public ActionLabel(Context context) {
        this(context, null);
    }

    public ActionLabel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ActionLabel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mGravity = 8388659;
        this.mSpacingMult = 1.0f;
        this.mSpacingAdd = 0.0f;
        this.mMaxLines = Integer.MAX_VALUE;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float f = dm.density;
        float scaledDensity = dm.scaledDensity;
        this.mMinTextSize = 10.0f * scaledDensity;
        this.mMaxTextSize = 60.0f * scaledDensity;
        this.mTextPaint = new TextPaint(1);
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.ActionLabel, defStyleAttr, defStyleRes);
        this.mText = a.getText(R.styleable.ActionLabel_android_text);
        this.mMinTextSize = a.getDimension(R.styleable.ActionLabel_minTextSize, this.mMinTextSize);
        this.mMaxTextSize = a.getDimension(R.styleable.ActionLabel_maxTextSize, this.mMaxTextSize);
        this.mTextColor = a.getColorStateList(R.styleable.ActionLabel_android_textColor);
        this.mMaxLines = a.getInt(R.styleable.ActionLabel_android_maxLines, 2);
        if (this.mTextColor != null) {
            updateTextColors();
        }
        this.mTextPaint.setTextSize(this.mMaxTextSize);
        String fontFamily = a.getString(R.styleable.ActionLabel_android_fontFamily);
        int typefaceIndex = a.getInt(R.styleable.ActionLabel_android_typeface, -1);
        int styleIndex = a.getInt(R.styleable.ActionLabel_android_textStyle, -1);
        setTypefaceFromAttrs(fontFamily, typefaceIndex, styleIndex);
        this.mGravity = a.getInt(R.styleable.ActionLabel_android_gravity, this.mGravity);
        this.mLineSpacingAdd = a.getDimensionPixelSize(R.styleable.ActionLabel_android_lineSpacingExtra, (int) this.mLineSpacingAdd);
        this.mLineSpacingMult = a.getFloat(R.styleable.ActionLabel_android_lineSpacingMultiplier, this.mLineSpacingMult);
        a.recycle();
        if (this.mText == null) {
            this.mText = "";
        }
    }

    public void setText(CharSequence text) {
        if (text == null) {
            throw new RuntimeException("Can not set ActionLabel text to null");
        }
        if (!Objects.equals(this.mText, text)) {
            this.mLayout = null;
            this.mText = text;
            requestLayout();
            invalidate();
        }
    }

    public void setMinTextSize(float size) {
        setMinTextSize(2, size);
    }

    public void setMinTextSize(int unit, float size) {
        float sizePx = TypedValue.applyDimension(unit, size, getContext().getResources().getDisplayMetrics());
        if (sizePx != this.mMinTextSize) {
            this.mLayout = null;
            this.mMinTextSize = sizePx;
            requestLayout();
            invalidate();
        }
    }

    public void setMaxTextSize(float size) {
        setMaxTextSize(2, size);
    }

    public void setMaxTextSize(int unit, float size) {
        float sizePx = TypedValue.applyDimension(unit, size, getContext().getResources().getDisplayMetrics());
        if (sizePx != this.mMaxTextSize) {
            this.mLayout = null;
            this.mMaxTextSize = sizePx;
            requestLayout();
            invalidate();
        }
    }

    public void setTypeface(Typeface tf) {
        if (!Objects.equals(this.mTextPaint.getTypeface(), tf)) {
            this.mTextPaint.setTypeface(tf);
            if (this.mLayout != null) {
                requestLayout();
                invalidate();
            }
        }
    }

    public void setTypeface(Typeface tf, int style) {
        Typeface tf2;
        if (style > 0) {
            if (tf == null) {
                tf2 = Typeface.defaultFromStyle(style);
            } else {
                tf2 = Typeface.create(tf, style);
            }
            setTypeface(tf2);
            int typefaceStyle = tf2 != null ? tf2.getStyle() : 0;
            int need = style & (typefaceStyle ^ (-1));
            this.mTextPaint.setFakeBoldText((need & 1) != 0);
            this.mTextPaint.setTextSkewX((need & 2) != 0 ? -0.25f : 0.0f);
            return;
        }
        this.mTextPaint.setFakeBoldText(false);
        this.mTextPaint.setTextSkewX(0.0f);
        setTypeface(tf);
    }

    public Typeface getTypeface() {
        return this.mTextPaint.getTypeface();
    }

    void setTypefaceFromAttrs(String familyName, int typefaceIndex, int styleIndex) {
        Typeface tf = null;
        if (familyName != null && (tf = Typeface.create(familyName, styleIndex)) != null) {
            setTypeface(tf);
            return;
        }
        switch (typefaceIndex) {
            case 1:
                tf = Typeface.SANS_SERIF;
                break;
            case 2:
                tf = Typeface.SERIF;
                break;
            case 3:
                tf = Typeface.MONOSPACE;
                break;
        }
        setTypeface(tf, styleIndex);
    }

    public void setLineSpacing(float add, float mult) {
        if (this.mSpacingAdd != add || this.mSpacingMult != mult) {
            this.mSpacingAdd = add;
            this.mSpacingMult = mult;
            if (this.mLayout != null) {
                this.mLayout = null;
                requestLayout();
                invalidate();
            }
        }
    }

    public float getLineSpacingMultiplier() {
        return this.mSpacingMult;
    }

    public float getLineSpacingExtra() {
        return this.mSpacingAdd;
    }

    public void setTextColor(int color) {
        this.mTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            throw new NullPointerException();
        }
        this.mTextColor = colors;
        updateTextColors();
    }

    public final ColorStateList getTextColors() {
        return this.mTextColor;
    }

    public final int getCurrentTextColor() {
        return this.mCurTextColor;
    }

    public int getMaxLines() {
        return this.mMaxLines;
    }

    public void setMaxLines(int lines) {
        if (this.mMaxLines != lines) {
            this.mMaxLines = lines;
            this.mLayout = null;
            requestLayout();
            invalidate();
        }
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            this.mGravity = gravity;
            invalidate();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mTextColor != null && this.mTextColor.isStateful()) {
            updateTextColors();
        }
    }

    private void updateTextColors() {
        int color = this.mTextColor.getColorForState(getDrawableState(), 0);
        if (color != this.mCurTextColor) {
            this.mCurTextColor = color;
            invalidate();
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        this.mLayout = null;
        requestLayout();
        invalidate();
    }

    @SuppressLint({"RtlHardcoded"})
    private Layout.Alignment getLayoutAlignment() {
        switch (getTextAlignment()) {
            case 1:
                switch (this.mGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
                    case 1:
                        Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
                        return alignment;
                    case 3:
                        Layout.Alignment alignment2 = Layout.Alignment.ALIGN_NORMAL;
                        return alignment2;
                    case 5:
                        Layout.Alignment alignment3 = Layout.Alignment.ALIGN_OPPOSITE;
                        return alignment3;
                    case GravityCompat.START /* 8388611 */:
                        Layout.Alignment alignment4 = Layout.Alignment.ALIGN_NORMAL;
                        return alignment4;
                    case GravityCompat.END /* 8388613 */:
                        Layout.Alignment alignment5 = Layout.Alignment.ALIGN_OPPOSITE;
                        return alignment5;
                    default:
                        Layout.Alignment alignment6 = Layout.Alignment.ALIGN_NORMAL;
                        return alignment6;
                }
            case 2:
                Layout.Alignment alignment7 = Layout.Alignment.ALIGN_NORMAL;
                return alignment7;
            case 3:
                Layout.Alignment alignment8 = Layout.Alignment.ALIGN_OPPOSITE;
                return alignment8;
            case 4:
                Layout.Alignment alignment9 = Layout.Alignment.ALIGN_CENTER;
                return alignment9;
            default:
                Layout.Alignment alignment10 = Layout.Alignment.ALIGN_NORMAL;
                return alignment10;
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int width = -1;
        int height = -1;
        if (widthMode == 1073741824) {
            width = widthSize;
        }
        if (heightMode == 1073741824) {
            height = heightSize;
        }
        if (width == -1) {
            this.mTextPaint.setTextSize(this.mMaxTextSize);
            width = (int) Math.ceil(Layout.getDesiredWidth(this.mText, this.mTextPaint));
            this.mTextPaint.setTextSize(this.mCurrentTextSize);
        }
        if (widthMode == Integer.MIN_VALUE) {
            width = Math.min(width, widthSize);
        }
        Layout.Alignment alignment = getLayoutAlignment();
        if (height == -1) {
            height = heightMode == Integer.MIN_VALUE ? heightSize : Integer.MAX_VALUE;
        }
        if (this.mLayout == null) {
            this.mLayout = makeNewLayout(width, height, alignment);
        } else {
            boolean widthChanged = this.mLayout.getWidth() != width;
            boolean heightChanged = this.mLayout.getHeight() != height;
            if (widthChanged || heightChanged) {
                this.mLayout = makeNewLayout(width, height, alignment);
            }
        }
        if (this.mLayout == null) {
            setMeasuredDimension(0, 0);
            return;
        }
        if (heightMode != 1073741824) {
            height = this.mLayout.getLineTop(this.mLayout.getLineCount());
        }
        if (heightMode == Integer.MIN_VALUE) {
            height = Math.min(height, heightSize);
        }
        setMeasuredDimension(width, height);
    }

    private Layout makeNewLayout(int width, int height, Layout.Alignment alignment) {
        if (height <= 0 || width <= 0) {
            return null;
        }
        int availableHeight = height - (getPaddingTop() + getPaddingBottom());
        int availableWidth = width - (getPaddingLeft() + getPaddingRight());
        this.mCurrentTextSize = this.mMaxTextSize;
        this.mTextPaint.setTextSize(this.mMaxTextSize);
        int tries = 1;
        Layout layout = new StaticLayout(this.mText, this.mTextPaint, availableWidth, alignment, this.mSpacingMult, this.mSpacingAdd, true);
        boolean tooManyLines = layout.getLineCount() > this.mMaxLines;
        boolean tooTall = layout.getLineTop(layout.getLineCount()) > availableHeight;
        boolean textCanShrink = this.mTextPaint.getTextSize() > this.mMinTextSize;
        if (tooManyLines || tooTall) {
            while (true) {
                if ((!tooManyLines && !tooTall) || !textCanShrink) {
                    break;
                }
                this.mCurrentTextSize -= 1.0f;
                this.mTextPaint.setTextSize(this.mCurrentTextSize);
                layout = new StaticLayout(this.mText, this.mTextPaint, availableWidth, alignment, this.mSpacingMult, this.mSpacingAdd, true);
                tooTall = layout.getLineTop(layout.getLineCount()) > availableHeight;
                tooManyLines = layout.getLineCount() > this.mMaxLines;
                textCanShrink = this.mTextPaint.getTextSize() > this.mMinTextSize;
                tries++;
            }
        }
        this.mDrawMaxLines = Math.min(this.mMaxLines, layout.getLineCount());
        return layout;
    }

    private int getAvailableHeight() {
        return getHeight() - (getPaddingTop() + getPaddingBottom());
    }

    int getVerticalOffset() {
        int availHeight = getAvailableHeight();
        int textHeight = this.mLayout.getLineTop(this.mDrawMaxLines);
        int gravity = this.mGravity & 112;
        switch (gravity) {
            case 16:
                int voffset = (availHeight - textHeight) / 2;
                return voffset;
            case Bar.DEFAULT_HEIGHT /* 48 */:
                return 0;
            case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                int voffset2 = availHeight - textHeight;
                return voffset2;
            default:
                return 0;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mLayout != null) {
            canvas.save();
            this.mTextPaint.setColor(this.mCurTextColor);
            this.mTextPaint.drawableState = getDrawableState();
            canvas.translate(getPaddingLeft(), getPaddingTop() + getVerticalOffset());
            canvas.clipRect(0, 0, getWidth() - getPaddingRight(), this.mLayout.getLineTop(this.mDrawMaxLines));
            this.mLayout.draw(canvas);
            canvas.restore();
        }
    }
}
