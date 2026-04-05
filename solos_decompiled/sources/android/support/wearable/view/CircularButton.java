package android.support.wearable.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.DrawableRes;
import android.support.wearable.R;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/* JADX INFO: loaded from: classes33.dex */
@SuppressLint({"ClickableViewAccessibility"})
@TargetApi(21)
@Deprecated
public class CircularButton extends View {
    private static final float DEFAULT_ICON_SIZE_DP = 48.0f;
    public static final int SCALE_MODE_CENTER = 1;
    public static final int SCALE_MODE_FIT = 0;
    private static final double SQRT_2 = Math.sqrt(2.0d);
    private ColorStateList mColors;
    private int mDiameter;
    private Drawable mImage;
    private final Interpolator mInterpolator;
    private int mRippleColor;
    private RippleDrawable mRippleDrawable;
    private int mScaleMode;
    private final ShapeDrawable mShapeDrawable;

    private static int inscribedSize(int r) {
        return (int) Math.floor(((double) r) * SQRT_2);
    }

    private static int encircledRadius(int l) {
        return (int) Math.floor(((double) l) / SQRT_2);
    }

    public CircularButton(Context context) {
        this(context, null);
    }

    public CircularButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircularButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mRippleColor = -1;
        this.mShapeDrawable = new ShapeDrawable(new OvalShape());
        this.mShapeDrawable.getPaint().setColor(-3355444);
        super.setBackgroundDrawable(this.mShapeDrawable);
        setOutlineProvider(new CircleOutlineProvider());
        this.mInterpolator = new AccelerateInterpolator(2.0f);
        this.mScaleMode = 0;
        boolean clickable = true;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularButton, defStyleAttr, defStyleRes);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CircularButton_android_color) {
                this.mColors = a.getColorStateList(attr);
                this.mShapeDrawable.getPaint().setColor(this.mColors.getDefaultColor());
            } else if (attr == R.styleable.CircularButton_android_src) {
                this.mImage = a.getDrawable(attr);
            } else if (attr == R.styleable.CircularButton_buttonRippleColor) {
                setRippleColor(a.getColor(attr, -1));
            } else if (attr == R.styleable.CircularButton_pressedButtonTranslationZ) {
                setPressedTranslationZ(a.getDimension(attr, 0.0f));
            } else if (attr == R.styleable.CircularButton_imageScaleMode) {
                this.mScaleMode = a.getInt(attr, this.mScaleMode);
            } else if (attr == R.styleable.CircularButton_android_clickable) {
                clickable = a.getBoolean(R.styleable.CircularButton_android_clickable, clickable);
            }
        }
        a.recycle();
        setClickable(clickable);
    }

    public void setColor(int color) {
        this.mColors = ColorStateList.valueOf(color);
        this.mShapeDrawable.getPaint().setColor(this.mColors.getDefaultColor());
    }

    public void setColor(ColorStateList colorStateList) {
        this.mColors = colorStateList;
        this.mShapeDrawable.getPaint().setColor(this.mColors.getDefaultColor());
    }

    public void setRippleColor(int rippleColor) {
        this.mRippleColor = rippleColor;
        if (this.mRippleDrawable != null) {
            this.mRippleDrawable.setColor(ColorStateList.valueOf(rippleColor));
            return;
        }
        if (this.mRippleColor != -1 && !isInEditMode()) {
            this.mRippleDrawable = new RippleDrawable(ColorStateList.valueOf(rippleColor), this.mShapeDrawable, this.mShapeDrawable);
            super.setBackgroundDrawable(this.mRippleDrawable);
        } else {
            this.mRippleDrawable = null;
            super.setBackgroundDrawable(this.mShapeDrawable);
        }
    }

    public void setImageResource(@DrawableRes int drawableRes) {
        setImageDrawable(getResources().getDrawable(drawableRes, null));
    }

    public Drawable getImageDrawable() {
        return this.mImage;
    }

    public void setImageDrawable(Drawable drawable) {
        if (this.mImage != null) {
            this.mImage.setCallback(null);
        }
        if (this.mImage != drawable) {
            this.mImage = drawable;
            requestLayout();
            invalidate();
        }
        if (this.mImage != null) {
            this.mImage.setCallback(this);
        }
    }

    public int getImageScaleMode() {
        return this.mScaleMode;
    }

    public void setImageScaleMode(int scaleMode) {
        this.mScaleMode = scaleMode;
        if (this.mImage != null) {
            invalidate();
            requestLayout();
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable who) {
        return this.mImage == who || super.verifyDrawable(who);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable background) {
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mColors != null && this.mColors.isStateful()) {
            this.mShapeDrawable.getPaint().setColor(this.mColors.getColorForState(getDrawableState(), this.mColors.getDefaultColor()));
            this.mShapeDrawable.invalidateSelf();
        }
    }

    private int dpToPx(float dp) {
        return (int) Math.ceil(TypedValue.applyDimension(1, dp, getResources().getDisplayMetrics()));
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int imageSize;
        int atMost;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == 1073741824 && heightMode == 1073741824) {
            this.mDiameter = Math.min(widthSize, heightSize);
        } else if (widthMode == 1073741824) {
            this.mDiameter = widthSize;
        } else if (heightMode == 1073741824) {
            this.mDiameter = heightSize;
        } else {
            if (hasIntrinsicSize(this.mImage)) {
                imageSize = Math.max(this.mImage.getIntrinsicHeight(), this.mImage.getIntrinsicWidth());
            } else {
                imageSize = dpToPx(DEFAULT_ICON_SIZE_DP);
            }
            if (widthMode == Integer.MIN_VALUE || heightMode == Integer.MIN_VALUE) {
                if (widthMode != Integer.MIN_VALUE) {
                    atMost = heightSize;
                } else if (heightMode != Integer.MIN_VALUE) {
                    atMost = widthSize;
                } else {
                    atMost = Math.min(widthSize, heightSize);
                }
                this.mDiameter = Math.min(atMost, encircledRadius(imageSize) * 2);
            } else {
                this.mDiameter = imageSize;
            }
        }
        setMeasuredDimension(this.mDiameter, this.mDiameter);
    }

    private static boolean hasIntrinsicSize(Drawable d) {
        return d != null && d.getIntrinsicHeight() > 0 && d.getIntrinsicWidth() > 0;
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int ih;
        int iw;
        super.onLayout(changed, l, t, r, b);
        int w = r - l;
        int h = b - t;
        if (this.mImage != null) {
            int iw2 = this.mImage.getIntrinsicWidth();
            int ih2 = this.mImage.getIntrinsicHeight();
            if (this.mScaleMode == 0 || !hasIntrinsicSize(this.mImage)) {
                int inscr = inscribedSize(this.mDiameter / 2);
                int vpad = (this.mDiameter - inscr) / 2;
                int hpad = vpad;
                if (!hasIntrinsicSize(this.mImage)) {
                    this.mImage.setBounds(hpad, vpad, hpad + inscr, vpad + inscr);
                    return;
                }
                if (iw2 == ih2) {
                    ih = inscr;
                    iw = inscr;
                } else {
                    float aspect = iw2 / ih2;
                    if (iw2 > ih2) {
                        iw = inscr;
                        ih = (int) (iw / aspect);
                        vpad = (int) ((inscr - ih) / 2.0f);
                    } else {
                        ih = inscr;
                        iw = (int) (ih * aspect);
                        hpad = (int) ((inscr - iw) / 2.0f);
                    }
                }
                this.mImage.setBounds(hpad, vpad, hpad + iw, vpad + ih);
                return;
            }
            int hpad2 = (int) ((w - iw2) / 2.0f);
            int vpad2 = (int) ((h - ih2) / 2.0f);
            this.mImage.setBounds(hpad2, vpad2, hpad2 + iw2, vpad2 + ih2);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mImage != null) {
            this.mImage.draw(canvas);
        }
    }

    public void setPressedTranslationZ(float translationZ) {
        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, setupAnimator(ObjectAnimator.ofFloat(this, "translationZ", translationZ)));
        stateListAnimator.addState(ENABLED_FOCUSED_STATE_SET, setupAnimator(ObjectAnimator.ofFloat(this, "translationZ", translationZ)));
        stateListAnimator.addState(EMPTY_STATE_SET, setupAnimator(ObjectAnimator.ofFloat(this, "translationZ", getElevation())));
        setStateListAnimator(stateListAnimator);
    }

    private Animator setupAnimator(Animator animator) {
        animator.setInterpolator(this.mInterpolator);
        return animator;
    }

    private class CircleOutlineProvider extends ViewOutlineProvider {
        private CircleOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, CircularButton.this.mDiameter, CircularButton.this.mDiameter);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = super.onTouchEvent(event);
        if (handled) {
            switch (event.getAction() & 255) {
                case 0:
                    getBackground().setHotspot(event.getX(), event.getY());
                default:
                    return handled;
            }
        }
        return handled;
    }
}
