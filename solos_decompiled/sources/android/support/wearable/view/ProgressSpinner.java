package android.support.wearable.view;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public class ProgressSpinner extends ProgressBar {
    private static final Property<ProgressSpinner, Float> SHOWINGNESS = new Property<ProgressSpinner, Float>(Float.class, "showingness") { // from class: android.support.wearable.view.ProgressSpinner.1
        @Override // android.util.Property
        public void set(ProgressSpinner object, Float value) {
            object.setShowingness(value.floatValue());
        }

        @Override // android.util.Property
        public Float get(ProgressSpinner object) {
            return Float.valueOf(object.getShowingness());
        }
    };
    private static final int SHOWINGNESS_ANIMATION_MS = 460;
    private ObjectAnimator mAnimator;
    private int[] mColors;
    private final ArgbEvaluator mEvaluator;
    private Interpolator mInterpolator;
    private float mShowingness;
    private int mStartingLevel;

    public ProgressSpinner(Context context) {
        super(context);
        this.mColors = null;
        this.mEvaluator = new ArgbEvaluator();
        init(context, null, 0);
    }

    public ProgressSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mColors = null;
        this.mEvaluator = new ArgbEvaluator();
        init(context, attrs, 0);
    }

    public ProgressSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mColors = null;
        this.mEvaluator = new ArgbEvaluator();
        init(context, attrs, defStyle);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyle) {
        if (!isInEditMode()) {
            this.mInterpolator = AnimationUtils.loadInterpolator(context, R.interpolator.accelerate_decelerate);
        }
        setIndeterminateDrawable(new ProgressDrawable());
        if (getVisibility() == 0) {
            this.mShowingness = 1.0f;
        }
        int[] colors = this.mColors;
        if (attrs != null) {
            colors = getColorsFromAttributes(context, attrs, defStyle);
        }
        if (colors == null) {
            if (isInEditMode()) {
                colors = new int[]{context.getResources().getColor(R.color.holo_orange_light)};
            } else {
                TypedArray typedArray = getResources().obtainTypedArray(android.support.wearable.R.array.progress_spinner_sequence);
                colors = new int[typedArray.length()];
                for (int i = 0; i < typedArray.length(); i++) {
                    colors[i] = typedArray.getColor(i, 0);
                }
                typedArray.recycle();
            }
        }
        setColors(colors);
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            super.setVisibility(visibility);
            switch (visibility) {
                case 0:
                    setShowingness(1.0f);
                    return;
                case 4:
                case 8:
                    setShowingness(0.0f);
                    return;
                default:
                    throw new IllegalArgumentException("Visibility only supports View.VISIBLE, View.INVISIBLE, or View.GONE");
            }
        }
    }

    public void showWithAnimation() {
        showWithAnimation(0L);
    }

    public void showWithAnimation(long delayMs) {
        showWithAnimation(delayMs, null);
    }

    public void showWithAnimation(long delayMs, @Nullable final AnimatorListenerAdapter listener) {
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
        if (getVisibility() != 0) {
            this.mAnimator = ObjectAnimator.ofFloat(this, SHOWINGNESS, 0.0f, 1.0f);
            this.mAnimator.setDuration(460L);
            if (delayMs > 0) {
                this.mAnimator.setStartDelay(delayMs);
            }
            this.mAnimator.addListener(new AnimatorListenerAdapter() { // from class: android.support.wearable.view.ProgressSpinner.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                    if (listener != null) {
                        listener.onAnimationStart(animation);
                    }
                    ProgressSpinner.super.setVisibility(0);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    ProgressSpinner.this.mStartingLevel = ProgressSpinner.this.getIndeterminateDrawable().getLevel();
                    ProgressSpinner.this.mAnimator = null;
                    if (listener != null) {
                        listener.onAnimationEnd(animation);
                    }
                }
            });
            this.mAnimator.start();
            return;
        }
        if (listener != null) {
            listener.onAnimationStart(null);
            listener.onAnimationEnd(null);
        }
    }

    public void hide() {
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
        setVisibility(8);
    }

    public void hideWithAnimation() {
        hideWithAnimation(null);
    }

    public void hideWithAnimation(final AnimatorListenerAdapter listener) {
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
        if (getVisibility() == 0) {
            this.mAnimator = ObjectAnimator.ofFloat(this, SHOWINGNESS, getShowingness(), 0.0f);
            this.mAnimator.setDuration((long) (getShowingness() * 460.0f));
            this.mAnimator.addListener(new AnimatorListenerAdapter() { // from class: android.support.wearable.view.ProgressSpinner.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    ProgressSpinner.this.setVisibility(8);
                    if (listener != null) {
                        listener.onAnimationEnd(animation);
                    }
                }
            });
            this.mAnimator.start();
            return;
        }
        if (listener != null) {
            listener.onAnimationEnd(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getShowingness() {
        return this.mShowingness;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setShowingness(float showingness) {
        this.mShowingness = showingness;
        invalidate();
    }

    @Nullable
    private int[] getColorsFromAttributes(Context context, AttributeSet attrs, int defStyle) {
        int[] colors = null;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, android.support.wearable.R.styleable.ProgressSpinner, defStyle, 0);
        if (typedArray.hasValue(android.support.wearable.R.styleable.ProgressSpinner_color_sequence)) {
            try {
                int resource = typedArray.getResourceId(android.support.wearable.R.styleable.ProgressSpinner_color_sequence, 0);
                colors = getResources().getIntArray(resource);
            } catch (Resources.NotFoundException e) {
            }
            if (colors == null || colors.length <= 0) {
                Integer color = Integer.valueOf(typedArray.getColor(android.support.wearable.R.styleable.ProgressSpinner_color_sequence, getResources().getColor(R.color.transparent)));
                colors = new int[]{color.intValue()};
            }
        }
        typedArray.recycle();
        return colors;
    }

    public void setColors(int[] colors) {
        if (colors != null && colors.length > 0) {
            this.mColors = colors;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getColor(float progress, float max, int color1, int color2) {
        return ((Integer) this.mEvaluator.evaluate(lerpInv(0.0f, max, progress), Integer.valueOf(color1), Integer.valueOf(color2))).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float lerpInvSat(float a, float b, float value) {
        return saturate(lerpInv(a, b, value));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float lerpInv(float a, float b, float value) {
        if (a != b) {
            return (value - a) / (b - a);
        }
        return 0.0f;
    }

    private static float saturate(float value) {
        return clamp(value, 0.0f, 1.0f);
    }

    private static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        return value > max ? max : value;
    }

    private class ProgressDrawable extends Drawable {
        static final float GROW_SHRINK_RATIO = 0.5f;
        static final float INNER_CIRCLE_MAX_SIZE = 0.64285713f;
        static final float INNER_CIRCLE_SHOW_END = 1.0f;
        static final float INNER_CIRCLE_SHOW_START = 0.4f;
        static final int INNER_RING_DEVISOR = 7;
        static final int MAX_LEVEL = 10000;
        static final float MIDDLE_CIRCLE_MAX_SIZE = 0.78571427f;
        static final float MIDDLE_CIRCLE_SHOW_END = 0.8f;
        static final float MIDDLE_CIRCLE_SHOW_START = 0.2f;
        static final int MIN_SEGMENTS = 4;
        static final float SHOW_STEP_VALUE = 0.2f;
        static final float STARTING_ANGLE = -90.0f;
        final RectF mInnerCircleBounds = new RectF();
        final Paint mForegroundPaint = new Paint();

        ProgressDrawable() {
            this.mForegroundPaint.setAntiAlias(true);
            this.mForegroundPaint.setColor(-1);
            this.mForegroundPaint.setStyle(Paint.Style.STROKE);
            this.mForegroundPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        private void drawEditModeSample(Canvas c) {
            RectF bounds = new RectF(getBounds());
            bounds.inset(10.0f, 10.0f);
            this.mForegroundPaint.setColor(ProgressSpinner.this.mColors[0]);
            this.mForegroundPaint.setStrokeWidth(7.0f);
            c.drawArc(bounds, 0.0f, 270.0f, false, this.mForegroundPaint);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas c) {
            float inset;
            float strokeWidth;
            float baseRadius = getBounds().width() / 2;
            if (ProgressSpinner.this.isInEditMode()) {
                drawEditModeSample(c);
                return;
            }
            if (ProgressSpinner.this.mShowingness < 1.0f) {
                float middleCircleScale = ProgressSpinner.this.mInterpolator.getInterpolation(ProgressSpinner.lerpInvSat(0.2f, MIDDLE_CIRCLE_SHOW_END, ProgressSpinner.this.mShowingness));
                float innerCircleScale = ProgressSpinner.this.mInterpolator.getInterpolation(ProgressSpinner.lerpInvSat(INNER_CIRCLE_SHOW_START, 1.0f, ProgressSpinner.this.mShowingness));
                float middleRadius = MIDDLE_CIRCLE_MAX_SIZE * middleCircleScale * baseRadius;
                float innerRadius = INNER_CIRCLE_MAX_SIZE * innerCircleScale * baseRadius;
                inset = (baseRadius - middleRadius) + ((middleRadius - innerRadius) / 2.0f);
                strokeWidth = middleRadius - innerRadius;
            } else {
                inset = getBounds().width() / 7;
                strokeWidth = getBounds().width() / 14;
            }
            this.mInnerCircleBounds.set(getBounds());
            this.mInnerCircleBounds.inset(inset, inset);
            this.mForegroundPaint.setStrokeWidth(strokeWidth);
            int level = ((getLevel() + 10000) - ProgressSpinner.this.mStartingLevel) % 10000;
            float sweepAngle = 360.0f;
            int color = ProgressSpinner.this.mColors[0];
            boolean growing = false;
            float correctionAngle = 0.0f;
            float maxCorrectionAngle = 360.0f - 306.0f;
            if (ProgressSpinner.this.mShowingness >= 1.0f) {
                int mNumberOfSegments = ProgressSpinner.this.mColors.length;
                int mLevelsPerSegment = 10000 / Math.max(4, mNumberOfSegments);
                int i = 0;
                while (true) {
                    if (i >= Math.max(4, mNumberOfSegments)) {
                        break;
                    }
                    if (level > (i + 1) * mLevelsPerSegment) {
                        i++;
                    } else {
                        int offset = i * mLevelsPerSegment;
                        float progress = (level - offset) / mLevelsPerSegment;
                        growing = progress < GROW_SHRINK_RATIO;
                        correctionAngle = maxCorrectionAngle * progress;
                        if (growing) {
                            color = ProgressSpinner.this.getColor(progress, GROW_SHRINK_RATIO, ProgressSpinner.this.mColors[i % ProgressSpinner.this.mColors.length], ProgressSpinner.this.mColors[(i + 1) % ProgressSpinner.this.mColors.length]);
                            sweepAngle = 306.0f * ProgressSpinner.this.mInterpolator.getInterpolation(ProgressSpinner.lerpInv(0.0f, GROW_SHRINK_RATIO, progress));
                        } else {
                            color = ProgressSpinner.this.mColors[(i + 1) % ProgressSpinner.this.mColors.length];
                            sweepAngle = 306.0f * (1.0f - ProgressSpinner.this.mInterpolator.getInterpolation(ProgressSpinner.lerpInv(GROW_SHRINK_RATIO, 1.0f, progress)));
                        }
                    }
                }
            } else {
                sweepAngle = 360.0f;
            }
            this.mForegroundPaint.setColor(color);
            if (sweepAngle < 1.0f) {
                sweepAngle = 1.0f;
            }
            if (strokeWidth > 0.1d) {
                c.rotate((level * 1.0E-4f * 2.0f * 360.0f) + STARTING_ANGLE + correctionAngle, this.mInnerCircleBounds.centerX(), this.mInnerCircleBounds.centerY());
                c.drawArc(this.mInnerCircleBounds, growing ? 0.0f : 306.0f - sweepAngle, sweepAngle, false, this.mForegroundPaint);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }
    }
}
