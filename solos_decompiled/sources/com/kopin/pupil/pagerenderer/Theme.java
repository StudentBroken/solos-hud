package com.kopin.pupil.pagerenderer;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import com.kopin.pupil.ui.elements.BaseElement;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes25.dex */
public abstract class Theme {
    public static final String COLOR_ASR_FONT = "asr_font";
    public static final String COLOR_CONTRAST_FONT = "contrast_font";
    public static final String COLOR_DEFAULT_FONT = "default_font";
    public static final String COLOR_SUBTITLE_FONT = "subtitle_font";
    public static final String COLOR_THEME = "theme";
    public static final String COLOR_THEME_BORDER = "theme_border";
    public static final String COLOR_THEME_DARK = "theme_dark";
    public static final float ITALIC_SKEW = 0.25f;
    public static final String TEXT_SIZE_DEFAULT = "default";
    public static final String TEXT_SIZE_LARGE = "large";
    public static final String TEXT_SIZE_SMALL = "small";
    public static final byte TEXT_STYLE_BOLD = 1;
    public static final byte TEXT_STYLE_ITALIC = 2;
    public static final byte TEXT_STYLE_REGULAR = 0;
    public static final byte TEXT_STYLE_THIN = 4;
    protected HashMap<String, Integer> mColors;
    private ArrayList<ThemeElement> mElements;
    private Paint mFont;
    private Paint mFontBold;
    private Paint mFontThin;
    private Paint mMask;
    private Paint mSolid;
    private Paint mStroke;
    protected HashMap<String, Integer> mTextSizes;

    public Theme(Context context) {
        this(context, null);
    }

    public Theme(Context context, String fontname) {
        this.mElements = null;
        this.mFont = null;
        this.mFontBold = null;
        this.mFontThin = null;
        this.mMask = null;
        this.mStroke = null;
        this.mSolid = null;
        this.mTextSizes = null;
        this.mColors = null;
        this.mElements = new ArrayList<>();
        this.mTextSizes = new HashMap<>();
        this.mColors = new HashMap<>();
        this.mFont = new Paint(1);
        this.mFont.setTextSize(10.0f);
        this.mFont.setColor(ViewCompat.MEASURED_STATE_MASK);
        setTypeface(context, this.mFont, fontname);
        this.mFontBold = new Paint(1);
        this.mFontBold.setTextSize(10.0f);
        this.mFontBold.setColor(ViewCompat.MEASURED_STATE_MASK);
        setTypeface(context, this.mFontBold, fontname + "-Bold");
        this.mFontThin = new Paint(1);
        this.mFontThin.setTextSize(10.0f);
        this.mFontThin.setColor(ViewCompat.MEASURED_STATE_MASK);
        setTypeface(context, this.mFontThin, fontname + "-Thin");
        this.mMask = new Paint(1);
        this.mSolid = new Paint(1);
        this.mSolid.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mStroke = new Paint(1);
        this.mStroke.setStyle(Paint.Style.STROKE);
    }

    public Integer getTextSize(String name) {
        return this.mTextSizes.get(name);
    }

    public Integer getColor(String name) {
        return this.mColors.get(name);
    }

    public void addTheme(ThemeElement... elements) {
        if (elements != null && elements.length != 0) {
            for (ThemeElement element : elements) {
                if (!this.mElements.contains(element)) {
                    this.mElements.add(element);
                }
            }
        }
    }

    public void removeTheme(ThemeElement... elements) {
        if (elements != null && elements.length != 0 && this.mElements != null) {
            for (ThemeElement element : elements) {
                this.mElements.remove(element);
            }
        }
    }

    public void removeAllThemes() {
        if (this.mElements != null && this.mElements.size() >= 1) {
            this.mElements.clear();
        }
    }

    public final ThemeElement getTheme(Class<?> clazz) {
        ThemeElement defaultTheme = null;
        for (ThemeElement element : this.mElements) {
            if (!clazz.equals(element.element)) {
                if (clazz.equals(BaseElement.class)) {
                    defaultTheme = element;
                }
            } else {
                return element;
            }
        }
        return defaultTheme;
    }

    public final Paint getTextPaint(int color, int size, int style) {
        Paint textPaint;
        if ((style & 1) == 1) {
            textPaint = this.mFontBold;
        } else {
            textPaint = (style & 4) == 4 ? this.mFontThin : this.mFont;
        }
        textPaint.setTextSize(size);
        textPaint.setTextSkewX((style & 2) == 2 ? -0.25f : 0.0f);
        textPaint.setColor(color);
        return textPaint;
    }

    public final Paint getSolidColorPaint(int color) {
        this.mSolid.setColor(color);
        return this.mSolid;
    }

    public final Paint getStrokePaint(int color, int width) {
        this.mStroke.setColor(color);
        this.mStroke.setStrokeWidth(width);
        return this.mStroke;
    }

    public final Paint getMaskPaint(int color) {
        this.mMask.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        return this.mMask;
    }

    private void setTypeface(Context context, Paint fontPaint, String fontname) {
        Typeface typeface;
        try {
            typeface = typefaceFromAssets(context, fontname + ".ttf");
        } catch (NullPointerException e) {
            Log.e("Theme", "could not find font: " + fontname);
            typeface = null;
        }
        if (typeface != null) {
            fontPaint.setTypeface(typeface);
        } else {
            fontPaint.setTypeface(Typeface.DEFAULT);
        }
    }

    private Typeface typefaceFromAssets(Context context, String name) {
        String name2 = "fonts/" + name;
        try {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), name2);
            return typeface;
        } catch (Exception e) {
            Log.e("Theme", "Could not create typeface from assets (" + name2 + ")", e);
            return null;
        }
    }
}
