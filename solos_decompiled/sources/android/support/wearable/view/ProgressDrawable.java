package android.support.wearable.view;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.LinearInterpolator;
import com.google.firebase.analytics.FirebaseAnalytics;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
class ProgressDrawable extends Drawable {
    private static final long ANIMATION_DURATION = 6000;
    private static final int CORRECTION_ANGLE = 54;
    private static final int FULL_CIRCLE = 360;
    private static final float GROW_SHRINK_RATIO = 0.5f;
    private static final int LEVELS_PER_SEGMENT = 2000;
    private static final int MAX_LEVEL = 10000;
    private static final int MAX_SWEEP = 306;
    private static final int NUMBER_OF_SEGMENTS = 5;
    private static final float STARTING_ANGLE = -90.0f;
    private final ObjectAnimator mAnimator;
    private int mCircleBorderColor;
    private float mCircleBorderWidth;
    private final RectF mInnerCircleBounds = new RectF();
    private final Paint mPaint = new Paint();
    private static final Property<ProgressDrawable, Integer> LEVEL = new Property<ProgressDrawable, Integer>(Integer.class, FirebaseAnalytics.Param.LEVEL) { // from class: android.support.wearable.view.ProgressDrawable.1
        @Override // android.util.Property
        public Integer get(ProgressDrawable drawable) {
            return Integer.valueOf(drawable.getLevel());
        }

        @Override // android.util.Property
        public void set(ProgressDrawable drawable, Integer value) {
            drawable.setLevel(value.intValue());
            drawable.invalidateSelf();
        }
    };
    private static final TimeInterpolator mInterpolator = Gusterpolator.INSTANCE;

    public ProgressDrawable() {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mAnimator = ObjectAnimator.ofInt(this, LEVEL, 0, 10000);
        this.mAnimator.setRepeatCount(-1);
        this.mAnimator.setRepeatMode(1);
        this.mAnimator.setDuration(6000L);
        this.mAnimator.setInterpolator(new LinearInterpolator());
    }

    public void setRingColor(int color) {
        this.mCircleBorderColor = color;
    }

    public void setRingWidth(float width) {
        this.mCircleBorderWidth = width;
    }

    public void startAnimation() {
        if (!this.mAnimator.isStarted()) {
            this.mAnimator.start();
        }
    }

    public void stopAnimation() {
        this.mAnimator.cancel();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        float sweepAngle;
        canvas.save();
        this.mInnerCircleBounds.set(getBounds());
        this.mInnerCircleBounds.inset(this.mCircleBorderWidth / 2.0f, this.mCircleBorderWidth / 2.0f);
        this.mPaint.setStrokeWidth(this.mCircleBorderWidth);
        this.mPaint.setColor(this.mCircleBorderColor);
        int level = getLevel();
        int currentSegment = level / LEVELS_PER_SEGMENT;
        int offset = currentSegment * LEVELS_PER_SEGMENT;
        float progress = (level - offset) / 2000.0f;
        boolean growing = progress < GROW_SHRINK_RATIO;
        float correctionAngle = 54.0f * progress;
        if (growing) {
            sweepAngle = 306.0f * mInterpolator.getInterpolation(lerpInv(0.0f, GROW_SHRINK_RATIO, progress));
        } else {
            sweepAngle = 306.0f * (1.0f - mInterpolator.getInterpolation(lerpInv(GROW_SHRINK_RATIO, 1.0f, progress)));
        }
        float sweepAngle2 = Math.max(1.0f, sweepAngle);
        canvas.rotate((level * 1.0E-4f * 2.0f * 360.0f) + STARTING_ANGLE + correctionAngle, this.mInnerCircleBounds.centerX(), this.mInnerCircleBounds.centerY());
        canvas.drawArc(this.mInnerCircleBounds, growing ? 0.0f : 306.0f - sweepAngle2, sweepAngle2, false, this.mPaint);
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int level) {
        return true;
    }

    private static float lerpInv(float a, float b, float value) {
        if (a != b) {
            return (value - a) / (b - a);
        }
        return 0.0f;
    }
}
