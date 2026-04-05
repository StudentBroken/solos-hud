package android.support.wearable.view;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Px;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewCompat;
import android.support.wearable.R;
import android.util.AttributeSet;
import android.view.View;
import java.util.Objects;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class CircledImageView extends View {
    private static final ArgbEvaluator ARGB_EVALUATOR = new ArgbEvaluator();
    private static final int SQUARE_DIMEN_HEIGHT = 1;
    private static final int SQUARE_DIMEN_NONE = 0;
    private static final int SQUARE_DIMEN_WIDTH = 2;
    private final ValueAnimator.AnimatorUpdateListener mAnimationListener;
    private Paint.Cap mCircleBorderCap;
    private int mCircleBorderColor;
    private float mCircleBorderWidth;
    private ColorStateList mCircleColor;
    private boolean mCircleHidden;
    private float mCircleRadius;
    private float mCircleRadiusPercent;
    private float mCircleRadiusPressed;
    private float mCircleRadiusPressedPercent;
    private ValueAnimator mColorAnimator;
    private long mColorChangeAnimationDurationMs;
    private int mCurrentColor;
    private Drawable mDrawable;
    private final Drawable.Callback mDrawableCallback;
    private float mImageCirclePercentage;
    private float mImageHorizontalOffcenterPercentage;
    private Integer mImageTint;

    @VisibleForTesting
    final Rect mIndeterminateBounds;
    private final ProgressDrawable mIndeterminateDrawable;
    private final float mInitialCircleRadius;

    @VisibleForTesting
    final RectF mOval;
    private final Paint mPaint;
    private boolean mPressed;
    private float mProgress;
    private boolean mProgressIndeterminate;
    private float mRadiusInset;
    private final OvalShadowPainter mShadowPainter;
    private Integer mSquareDimen;
    private boolean mVisible;
    private boolean mWindowVisible;

    public CircledImageView(Context context) {
        this(context, null);
    }

    public CircledImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircledImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIndeterminateBounds = new Rect();
        this.mCircleHidden = false;
        this.mProgress = 1.0f;
        this.mPressed = false;
        this.mColorChangeAnimationDurationMs = 0L;
        this.mImageCirclePercentage = 1.0f;
        this.mImageHorizontalOffcenterPercentage = 0.0f;
        this.mDrawableCallback = new Drawable.Callback() { // from class: android.support.wearable.view.CircledImageView.1
            @Override // android.graphics.drawable.Drawable.Callback
            public void invalidateDrawable(Drawable drawable) {
                CircledImageView.this.invalidate();
            }

            @Override // android.graphics.drawable.Drawable.Callback
            public void scheduleDrawable(Drawable drawable, Runnable runnable, long l) {
            }

            @Override // android.graphics.drawable.Drawable.Callback
            public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
            }
        };
        this.mAnimationListener = new ValueAnimator.AnimatorUpdateListener() { // from class: android.support.wearable.view.CircledImageView.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = ((Integer) animation.getAnimatedValue()).intValue();
                if (color != CircledImageView.this.mCurrentColor) {
                    CircledImageView.this.mCurrentColor = color;
                    CircledImageView.this.invalidate();
                }
            }
        };
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircledImageView);
        this.mDrawable = a.getDrawable(R.styleable.CircledImageView_android_src);
        if (this.mDrawable != null && this.mDrawable.getConstantState() != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                this.mDrawable = this.mDrawable.getConstantState().newDrawable(context.getResources(), context.getTheme());
            } else {
                this.mDrawable = this.mDrawable.getConstantState().newDrawable(context.getResources());
            }
            this.mDrawable = this.mDrawable.mutate();
        }
        this.mCircleColor = a.getColorStateList(R.styleable.CircledImageView_circle_color);
        if (this.mCircleColor == null) {
            this.mCircleColor = ColorStateList.valueOf(android.R.color.darker_gray);
        }
        this.mCircleRadius = a.getDimension(R.styleable.CircledImageView_circle_radius, 0.0f);
        this.mInitialCircleRadius = this.mCircleRadius;
        this.mCircleRadiusPressed = a.getDimension(R.styleable.CircledImageView_circle_radius_pressed, this.mCircleRadius);
        this.mCircleBorderColor = a.getColor(R.styleable.CircledImageView_circle_border_color, ViewCompat.MEASURED_STATE_MASK);
        this.mCircleBorderCap = Paint.Cap.values()[a.getInt(R.styleable.CircledImageView_circle_border_cap, 0)];
        this.mCircleBorderWidth = a.getDimension(R.styleable.CircledImageView_circle_border_width, 0.0f);
        if (this.mCircleBorderWidth > 0.0f) {
            this.mRadiusInset += this.mCircleBorderWidth / 2.0f;
        }
        float circlePadding = a.getDimension(R.styleable.CircledImageView_circle_padding, 0.0f);
        if (circlePadding > 0.0f) {
            this.mRadiusInset += circlePadding;
        }
        this.mImageCirclePercentage = a.getFloat(R.styleable.CircledImageView_image_circle_percentage, 0.0f);
        this.mImageHorizontalOffcenterPercentage = a.getFloat(R.styleable.CircledImageView_image_horizontal_offcenter_percentage, 0.0f);
        if (a.hasValue(R.styleable.CircledImageView_image_tint)) {
            this.mImageTint = Integer.valueOf(a.getColor(R.styleable.CircledImageView_image_tint, 0));
        }
        if (a.hasValue(R.styleable.CircledImageView_square_dimen)) {
            this.mSquareDimen = Integer.valueOf(a.getInt(R.styleable.CircledImageView_square_dimen, 0));
        }
        this.mCircleRadiusPercent = a.getFraction(R.styleable.CircledImageView_circle_radius_percent, 1, 1, 0.0f);
        this.mCircleRadiusPressedPercent = a.getFraction(R.styleable.CircledImageView_circle_radius_pressed_percent, 1, 1, this.mCircleRadiusPercent);
        float shadowWidth = a.getDimension(R.styleable.CircledImageView_shadow_width, 0.0f);
        a.recycle();
        this.mOval = new RectF();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mShadowPainter = new OvalShadowPainter(shadowWidth, 0.0f, getCircleRadius(), this.mCircleBorderWidth);
        this.mIndeterminateDrawable = new ProgressDrawable();
        this.mIndeterminateDrawable.setCallback(this.mDrawableCallback);
        setWillNotDraw(false);
        setColorForCurrentState();
    }

    public void setCircleHidden(boolean circleHidden) {
        if (circleHidden != this.mCircleHidden) {
            this.mCircleHidden = circleHidden;
            invalidate();
        }
    }

    @Override // android.view.View
    protected boolean onSetAlpha(int alpha) {
        return true;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        float circleRadius = this.mPressed ? getCircleRadiusPressed() : getCircleRadius();
        this.mShadowPainter.draw(canvas, getAlpha());
        this.mOval.set(paddingLeft, paddingTop, getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        this.mOval.set(this.mOval.centerX() - circleRadius, this.mOval.centerY() - circleRadius, this.mOval.centerX() + circleRadius, this.mOval.centerY() + circleRadius);
        if (this.mCircleBorderWidth > 0.0f) {
            this.mPaint.setColor(this.mCircleBorderColor);
            this.mPaint.setAlpha(Math.round(this.mPaint.getAlpha() * getAlpha()));
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(this.mCircleBorderWidth);
            this.mPaint.setStrokeCap(this.mCircleBorderCap);
            if (this.mProgressIndeterminate) {
                this.mOval.roundOut(this.mIndeterminateBounds);
                this.mIndeterminateBounds.inset((int) ((-this.mCircleBorderWidth) / 2.0f), (int) ((-this.mCircleBorderWidth) / 2.0f));
                this.mIndeterminateDrawable.setBounds(this.mIndeterminateBounds);
                this.mIndeterminateDrawable.setRingColor(this.mCircleBorderColor);
                this.mIndeterminateDrawable.setRingWidth(this.mCircleBorderWidth);
                this.mIndeterminateDrawable.draw(canvas);
            } else {
                canvas.drawArc(this.mOval, -90.0f, this.mProgress * 360.0f, false, this.mPaint);
            }
        }
        if (!this.mCircleHidden) {
            this.mPaint.setColor(this.mCurrentColor);
            this.mPaint.setAlpha(Math.round(this.mPaint.getAlpha() * getAlpha()));
            this.mPaint.setStyle(Paint.Style.FILL);
            float centerX = this.mOval.centerX();
            float centerY = this.mOval.centerY();
            canvas.drawCircle(centerX, centerY, circleRadius, this.mPaint);
        }
        if (this.mDrawable != null) {
            this.mDrawable.setAlpha(Math.round(getAlpha() * 255.0f));
            if (this.mImageTint != null) {
                this.mDrawable.setTint(this.mImageTint.intValue());
            }
            this.mDrawable.draw(canvas);
        }
        super.onDraw(canvas);
    }

    private void setColorForCurrentState() {
        int newColor = this.mCircleColor.getColorForState(getDrawableState(), this.mCircleColor.getDefaultColor());
        if (this.mColorChangeAnimationDurationMs > 0) {
            if (this.mColorAnimator != null) {
                this.mColorAnimator.cancel();
            } else {
                this.mColorAnimator = new ValueAnimator();
            }
            this.mColorAnimator.setIntValues(this.mCurrentColor, newColor);
            this.mColorAnimator.setEvaluator(ARGB_EVALUATOR);
            this.mColorAnimator.setDuration(this.mColorChangeAnimationDurationMs);
            this.mColorAnimator.addUpdateListener(this.mAnimationListener);
            this.mColorAnimator.start();
            return;
        }
        if (newColor != this.mCurrentColor) {
            this.mCurrentColor = newColor;
            invalidate();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        float radius = getCircleRadius() + this.mCircleBorderWidth + (this.mShadowPainter.mShadowWidth * this.mShadowPainter.mShadowVisibility);
        float desiredWidth = radius * 2.0f;
        float desiredHeight = radius * 2.0f;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == 1073741824) {
            width = widthSize;
        } else if (widthMode == Integer.MIN_VALUE) {
            width = (int) Math.min(desiredWidth, widthSize);
        } else {
            width = (int) desiredWidth;
        }
        if (heightMode == 1073741824) {
            height = heightSize;
        } else if (heightMode == Integer.MIN_VALUE) {
            height = (int) Math.min(desiredHeight, heightSize);
        } else {
            height = (int) desiredHeight;
        }
        if (this.mSquareDimen != null) {
            switch (this.mSquareDimen.intValue()) {
                case 1:
                    width = height;
                    break;
                case 2:
                    height = width;
                    break;
            }
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(width, 1073741824), View.MeasureSpec.makeMeasureSpec(height, 1073741824));
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        float f;
        float f2;
        if (this.mDrawable != null) {
            int nativeDrawableWidth = this.mDrawable.getIntrinsicWidth();
            int nativeDrawableHeight = this.mDrawable.getIntrinsicHeight();
            int viewWidth = getMeasuredWidth();
            int viewHeight = getMeasuredHeight();
            float imageCirclePercentage = this.mImageCirclePercentage > 0.0f ? this.mImageCirclePercentage : 1.0f;
            if (nativeDrawableWidth != 0.0f) {
                f = (viewWidth * imageCirclePercentage) / nativeDrawableWidth;
            } else {
                f = 1.0f;
            }
            if (nativeDrawableHeight != 0.0f) {
                f2 = (viewHeight * imageCirclePercentage) / nativeDrawableHeight;
            } else {
                f2 = 1.0f;
            }
            float scaleFactor = Math.min(1.0f, Math.min(f, f2));
            int drawableWidth = Math.round(nativeDrawableWidth * scaleFactor);
            int drawableHeight = Math.round(nativeDrawableHeight * scaleFactor);
            int drawableLeft = ((viewWidth - drawableWidth) / 2) + Math.round(this.mImageHorizontalOffcenterPercentage * drawableWidth);
            int drawableTop = (viewHeight - drawableHeight) / 2;
            this.mDrawable.setBounds(drawableLeft, drawableTop, drawableLeft + drawableWidth, drawableTop + drawableHeight);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setImageDrawable(Drawable drawable) {
        if (drawable != this.mDrawable) {
            Drawable existingDrawable = this.mDrawable;
            this.mDrawable = drawable;
            if (this.mDrawable != null && this.mDrawable.getConstantState() != null) {
                this.mDrawable = this.mDrawable.getConstantState().newDrawable(getResources(), getContext().getTheme()).mutate();
            }
            boolean skipLayout = drawable != null && existingDrawable != null && existingDrawable.getIntrinsicHeight() == drawable.getIntrinsicHeight() && existingDrawable.getIntrinsicWidth() == drawable.getIntrinsicWidth();
            if (skipLayout) {
                this.mDrawable.setBounds(existingDrawable.getBounds());
            } else {
                requestLayout();
            }
            invalidate();
        }
    }

    public void setImageResource(int resId) {
        setImageDrawable(resId == 0 ? null : getContext().getDrawable(resId));
    }

    public void setImageCirclePercentage(float percentage) {
        float clamped = Math.max(0.0f, Math.min(1.0f, percentage));
        if (clamped != this.mImageCirclePercentage) {
            this.mImageCirclePercentage = clamped;
            invalidate();
        }
    }

    public void setImageHorizontalOffcenterPercentage(float percentage) {
        if (percentage != this.mImageHorizontalOffcenterPercentage) {
            this.mImageHorizontalOffcenterPercentage = percentage;
            invalidate();
        }
    }

    public void setImageTint(int tint) {
        if (this.mImageTint == null || tint != this.mImageTint.intValue()) {
            this.mImageTint = Integer.valueOf(tint);
            invalidate();
        }
    }

    public float getCircleRadius() {
        float radius = this.mCircleRadius;
        if (this.mCircleRadius <= 0.0f && this.mCircleRadiusPercent > 0.0f) {
            radius = Math.max(getMeasuredHeight(), getMeasuredWidth()) * this.mCircleRadiusPercent;
        }
        return radius - this.mRadiusInset;
    }

    public float getCircleRadiusPercent() {
        return this.mCircleRadiusPercent;
    }

    public float getCircleRadiusPressed() {
        float radius = this.mCircleRadiusPressed;
        if (this.mCircleRadiusPressed <= 0.0f && this.mCircleRadiusPressedPercent > 0.0f) {
            radius = Math.max(getMeasuredHeight(), getMeasuredWidth()) * this.mCircleRadiusPressedPercent;
        }
        return radius - this.mRadiusInset;
    }

    public float getCircleRadiusPressedPercent() {
        return this.mCircleRadiusPressedPercent;
    }

    public void setCircleRadius(float circleRadius) {
        if (circleRadius != this.mCircleRadius) {
            this.mCircleRadius = circleRadius;
            this.mShadowPainter.setInnerCircleRadius(this.mPressed ? getCircleRadiusPressed() : getCircleRadius());
            invalidate();
        }
    }

    public void setCircleRadiusPercent(float circleRadiusPercent) {
        if (circleRadiusPercent != this.mCircleRadiusPercent) {
            this.mCircleRadiusPercent = circleRadiusPercent;
            this.mShadowPainter.setInnerCircleRadius(this.mPressed ? getCircleRadiusPressed() : getCircleRadius());
            invalidate();
        }
    }

    public void setCircleRadiusPressed(float circleRadiusPressed) {
        if (circleRadiusPressed != this.mCircleRadiusPressed) {
            this.mCircleRadiusPressed = circleRadiusPressed;
            invalidate();
        }
    }

    public void setCircleRadiusPressedPercent(float circleRadiusPressedPercent) {
        if (circleRadiusPressedPercent != this.mCircleRadiusPressedPercent) {
            this.mCircleRadiusPressedPercent = circleRadiusPressedPercent;
            this.mShadowPainter.setInnerCircleRadius(this.mPressed ? getCircleRadiusPressed() : getCircleRadius());
            invalidate();
        }
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        setColorForCurrentState();
    }

    public void setCircleColor(int circleColor) {
        setCircleColorStateList(ColorStateList.valueOf(circleColor));
    }

    public void setCircleColorStateList(ColorStateList circleColor) {
        if (!Objects.equals(circleColor, this.mCircleColor)) {
            this.mCircleColor = circleColor;
            setColorForCurrentState();
            invalidate();
        }
    }

    public ColorStateList getCircleColorStateList() {
        return this.mCircleColor;
    }

    public int getDefaultCircleColor() {
        return this.mCircleColor.getDefaultColor();
    }

    public void showIndeterminateProgress(boolean show) {
        this.mProgressIndeterminate = show;
        if (this.mIndeterminateDrawable != null) {
            if (show && this.mVisible && this.mWindowVisible) {
                this.mIndeterminateDrawable.startAnimation();
            } else {
                this.mIndeterminateDrawable.stopAnimation();
            }
        }
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        this.mVisible = visibility == 0;
        showIndeterminateProgress(this.mProgressIndeterminate);
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mWindowVisible = visibility == 0;
        showIndeterminateProgress(this.mProgressIndeterminate);
    }

    public void setProgress(float progress) {
        if (progress != this.mProgress) {
            this.mProgress = progress;
            invalidate();
        }
    }

    public void setShadowVisibility(float shadowVisibility) {
        if (shadowVisibility != this.mShadowPainter.mShadowVisibility) {
            this.mShadowPainter.setShadowVisibility(shadowVisibility);
            invalidate();
        }
    }

    public float getInitialCircleRadius() {
        return this.mInitialCircleRadius;
    }

    public void setCircleBorderColor(int circleBorderColor) {
        this.mCircleBorderColor = circleBorderColor;
    }

    public void setCircleBorderWidth(float circleBorderWidth) {
        if (circleBorderWidth != this.mCircleBorderWidth) {
            this.mCircleBorderWidth = circleBorderWidth;
            this.mShadowPainter.setInnerCircleBorderWidth(circleBorderWidth);
            invalidate();
        }
    }

    public void setCircleBorderCap(Paint.Cap circleBorderCap) {
        if (circleBorderCap != this.mCircleBorderCap) {
            this.mCircleBorderCap = circleBorderCap;
            invalidate();
        }
    }

    @Override // android.view.View
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (pressed != this.mPressed) {
            this.mPressed = pressed;
            this.mShadowPainter.setInnerCircleRadius(this.mPressed ? getCircleRadiusPressed() : getCircleRadius());
            invalidate();
        }
    }

    @Override // android.view.View
    public void setPadding(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        if (left != getPaddingLeft() || top != getPaddingTop() || right != getPaddingRight() || bottom != getPaddingBottom()) {
            this.mShadowPainter.setBounds(left, top, getWidth() - right, getHeight() - bottom);
        }
        super.setPadding(left, top, right, bottom);
    }

    @Override // android.view.View
    public void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        if (newWidth != oldWidth || newHeight != oldHeight) {
            this.mShadowPainter.setBounds(getPaddingLeft(), getPaddingTop(), newWidth - getPaddingRight(), newHeight - getPaddingBottom());
        }
    }

    public Drawable getImageDrawable() {
        return this.mDrawable;
    }

    public long getColorChangeAnimationDuration() {
        return this.mColorChangeAnimationDurationMs;
    }

    public void setColorChangeAnimationDuration(long mColorChangeAnimationDurationMs) {
        this.mColorChangeAnimationDurationMs = mColorChangeAnimationDurationMs;
    }

    private static class OvalShadowPainter {
        private float mInnerCircleBorderWidth;
        private float mInnerCircleRadius;
        private float mShadowRadius;
        private float mShadowVisibility;
        private final float mShadowWidth;
        private final int[] mShaderColors = {ViewCompat.MEASURED_STATE_MASK, 0};
        private final float[] mShaderStops = {0.6f, 1.0f};
        private final RectF mBounds = new RectF();
        private final Paint mShadowPaint = new Paint();

        OvalShadowPainter(float shadowWidth, float shadowVisibility, float innerCircleRadius, float innerCircleBorderWidth) {
            this.mShadowWidth = shadowWidth;
            this.mShadowVisibility = shadowVisibility;
            this.mInnerCircleRadius = innerCircleRadius;
            this.mInnerCircleBorderWidth = innerCircleBorderWidth;
            this.mShadowRadius = this.mInnerCircleRadius + this.mInnerCircleBorderWidth + (this.mShadowWidth * this.mShadowVisibility);
            this.mShadowPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
            this.mShadowPaint.setStyle(Paint.Style.FILL);
            this.mShadowPaint.setAntiAlias(true);
            updateRadialGradient();
        }

        void draw(Canvas canvas, float alpha) {
            if (this.mShadowWidth > 0.0f && this.mShadowVisibility > 0.0f) {
                this.mShadowPaint.setAlpha(Math.round(this.mShadowPaint.getAlpha() * alpha));
                canvas.drawCircle(this.mBounds.centerX(), this.mBounds.centerY(), this.mShadowRadius, this.mShadowPaint);
            }
        }

        void setBounds(@Px int left, @Px int top, @Px int right, @Px int bottom) {
            this.mBounds.set(left, top, right, bottom);
            updateRadialGradient();
        }

        void setInnerCircleRadius(float newInnerCircleRadius) {
            this.mInnerCircleRadius = newInnerCircleRadius;
            updateRadialGradient();
        }

        void setInnerCircleBorderWidth(float newInnerCircleBorderWidth) {
            this.mInnerCircleBorderWidth = newInnerCircleBorderWidth;
            updateRadialGradient();
        }

        void setShadowVisibility(float newShadowVisibility) {
            this.mShadowVisibility = newShadowVisibility;
            updateRadialGradient();
        }

        private void updateRadialGradient() {
            this.mShadowRadius = this.mInnerCircleRadius + this.mInnerCircleBorderWidth + (this.mShadowWidth * this.mShadowVisibility);
            if (this.mShadowRadius > 0.0f) {
                this.mShadowPaint.setShader(new RadialGradient(this.mBounds.centerX(), this.mBounds.centerY(), this.mShadowRadius, this.mShaderColors, this.mShaderStops, Shader.TileMode.MIRROR));
            }
        }
    }
}
