package android.support.wearable.view;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.wearable.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
@Deprecated
public class ActionPage extends ViewGroup {
    private static final float CIRCLE_SIZE_RATIO = 0.45f;
    private static final float CIRCLE_VERT_POSITION_SQUARE = 0.43f;
    private static final boolean DEBUG = false;
    private static final float LABEL_BOTTOM_MARGIN_RATIO_ROUND = 0.09375f;
    private static final float LABEL_WIDTH_RATIO = 0.892f;
    private static final float LABEL_WIDTH_RATIO_ROUND = 0.625f;
    public static final int SCALE_MODE_CENTER = 1;
    public static final int SCALE_MODE_FIT = 0;
    private static final String TAG = "ActionPage";
    private int mBottomInset;
    private final Point mButtonCenter;
    private float mButtonRadius;
    private int mButtonSize;
    private CircularButton mCircularButton;
    private boolean mInsetsApplied;
    private boolean mIsRound;
    private final ActionLabel mLabel;
    private int mTextHeight;
    private int mTextWidth;

    public ActionPage(Context context) {
        this(context, null);
    }

    public ActionPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionPage(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.Widget_ActionPage);
    }

    public ActionPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mButtonCenter = new Point();
        this.mCircularButton = new CircularButton(context);
        this.mLabel = new ActionLabel(context);
        this.mLabel.setGravity(17);
        this.mLabel.setMaxLines(2);
        float lineSpacingMult = 1.0f;
        float lineSpacingExtra = 0.0f;
        String fontFamily = null;
        int typefaceIndex = 1;
        int styleIndex = 0;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionPage, defStyleAttr, defStyleRes);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ActionPage_android_color) {
                this.mCircularButton.setColor(a.getColorStateList(attr));
            } else if (attr == R.styleable.ActionPage_android_src) {
                this.mCircularButton.setImageDrawable(a.getDrawable(attr));
            } else if (attr == R.styleable.ActionPage_imageScaleMode) {
                this.mCircularButton.setImageScaleMode(a.getInt(attr, 0));
            } else if (attr == R.styleable.ActionPage_buttonRippleColor) {
                this.mCircularButton.setRippleColor(a.getColor(attr, -1));
            } else if (attr == R.styleable.ActionPage_pressedButtonTranslationZ) {
                this.mCircularButton.setPressedTranslationZ(a.getDimension(attr, 0.0f));
            } else if (attr == R.styleable.ActionPage_android_text) {
                this.mLabel.setText(a.getText(attr));
            } else if (attr == R.styleable.ActionPage_minTextSize) {
                this.mLabel.setMinTextSize(0, a.getDimension(attr, 10.0f));
            } else if (attr == R.styleable.ActionPage_maxTextSize) {
                this.mLabel.setMaxTextSize(0, a.getDimension(attr, 60.0f));
            } else if (attr == R.styleable.ActionPage_android_textColor) {
                this.mLabel.setTextColor(a.getColorStateList(attr));
            } else if (attr == R.styleable.ActionPage_android_maxLines) {
                this.mLabel.setMaxLines(a.getInt(attr, 2));
            } else if (attr == R.styleable.ActionPage_android_fontFamily) {
                fontFamily = a.getString(attr);
            } else if (attr == R.styleable.ActionPage_android_typeface) {
                typefaceIndex = a.getInt(attr, typefaceIndex);
            } else if (attr == R.styleable.ActionPage_android_textStyle) {
                styleIndex = a.getInt(attr, styleIndex);
            } else if (attr == R.styleable.ActionPage_android_gravity) {
                this.mLabel.setGravity(a.getInt(attr, 17));
            } else if (attr == R.styleable.ActionPage_android_lineSpacingExtra) {
                lineSpacingExtra = a.getDimension(attr, lineSpacingExtra);
            } else if (attr == R.styleable.ActionPage_android_lineSpacingMultiplier) {
                lineSpacingMult = a.getDimension(attr, lineSpacingMult);
            } else if (attr == R.styleable.ActionPage_android_stateListAnimator) {
                this.mCircularButton.setStateListAnimator(AnimatorInflater.loadStateListAnimator(context, a.getResourceId(attr, 0)));
            }
        }
        a.recycle();
        this.mLabel.setLineSpacing(lineSpacingExtra, lineSpacingMult);
        this.mLabel.setTypefaceFromAttrs(fontFamily, typefaceIndex, styleIndex);
        addView(this.mLabel);
        addView(this.mCircularButton);
    }

    public ActionLabel getLabel() {
        return this.mLabel;
    }

    public CircularButton getButton() {
        return this.mCircularButton;
    }

    public void setText(CharSequence text) {
        this.mLabel.setText(text);
    }

    public void setColor(int color) {
        this.mCircularButton.setColor(color);
    }

    public void setColor(ColorStateList color) {
        this.mCircularButton.setColor(color);
    }

    public void setImageDrawable(Drawable drawable) {
        this.mCircularButton.setImageDrawable(drawable);
    }

    public void setImageResource(@DrawableRes int drawableRes) {
        this.mCircularButton.setImageResource(drawableRes);
    }

    public void setImageScaleMode(int scaleMode) {
        this.mCircularButton.setImageScaleMode(scaleMode);
    }

    @Override // android.view.View
    public void setStateListAnimator(StateListAnimator stateListAnimator) {
        if (this.mCircularButton != null) {
            this.mCircularButton.setStateListAnimator(stateListAnimator);
        }
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener l) {
        if (this.mCircularButton != null) {
            this.mCircularButton.setOnClickListener(l);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (this.mCircularButton != null) {
            this.mCircularButton.setEnabled(enabled);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        if (this.mCircularButton.getImageScaleMode() == 1 && this.mCircularButton.getImageDrawable() != null) {
            this.mCircularButton.measure(0, 0);
            this.mButtonSize = Math.min(this.mCircularButton.getMeasuredWidth(), this.mCircularButton.getMeasuredHeight());
            this.mButtonRadius = this.mButtonSize / 2.0f;
        } else {
            this.mButtonSize = (int) (Math.min(width, height) * CIRCLE_SIZE_RATIO);
            this.mButtonRadius = this.mButtonSize / 2.0f;
            this.mCircularButton.measure(View.MeasureSpec.makeMeasureSpec(this.mButtonSize, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mButtonSize, 1073741824));
        }
        if (this.mIsRound) {
            this.mButtonCenter.set(width / 2, height / 2);
            this.mTextWidth = (int) (width * LABEL_WIDTH_RATIO_ROUND);
            this.mBottomInset = (int) (height * LABEL_BOTTOM_MARGIN_RATIO_ROUND);
        } else {
            this.mButtonCenter.set(width / 2, (int) (height * CIRCLE_VERT_POSITION_SQUARE));
            this.mTextWidth = (int) (width * LABEL_WIDTH_RATIO);
        }
        this.mTextHeight = (int) ((height - (this.mButtonCenter.y + this.mButtonRadius)) - this.mBottomInset);
        this.mLabel.measure(View.MeasureSpec.makeMeasureSpec(this.mTextWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mTextHeight, 1073741824));
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mInsetsApplied) {
            requestApplyInsets();
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        this.mInsetsApplied = true;
        if (this.mIsRound != insets.isRound()) {
            this.mIsRound = insets.isRound();
            requestLayout();
        }
        int insetBottom = insets.getSystemWindowInsetBottom();
        if (this.mBottomInset != insetBottom) {
            this.mBottomInset = insetBottom;
            requestLayout();
        }
        if (this.mIsRound) {
            this.mBottomInset = (int) Math.max(this.mBottomInset, LABEL_BOTTOM_MARGIN_RATIO_ROUND * getMeasuredHeight());
        }
        return insets;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = r - l;
        this.mCircularButton.layout((int) (this.mButtonCenter.x - this.mButtonRadius), (int) (this.mButtonCenter.y - this.mButtonRadius), (int) (this.mButtonCenter.x + this.mButtonRadius), (int) (this.mButtonCenter.y + this.mButtonRadius));
        int textHorizPadding = (int) ((w - this.mTextWidth) / 2.0f);
        this.mLabel.layout(textHorizPadding, this.mCircularButton.getBottom(), this.mTextWidth + textHorizPadding, this.mCircularButton.getBottom() + this.mTextHeight);
    }
}
