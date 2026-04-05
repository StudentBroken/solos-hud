package com.twitter.sdk.android.tweetui.internal;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

/* JADX INFO: loaded from: classes9.dex */
public class MultiTouchImageView extends ImageView {
    private static final float DOUBLE_TAP_SCALE_FACTOR = 2.0f;
    private static final float MINIMUM_SCALE_FACTOR = 1.0f;
    private static final long SCALE_ANIMATION_DURATION = 300;
    final Matrix baseMatrix;
    final Matrix drawMatrix;
    final RectF drawRect;
    final GestureDetector gestureDetector;
    final float[] matrixValues;
    final ScaleGestureDetector scaleGestureDetector;
    final Matrix updateMatrix;
    final RectF viewRect;

    public MultiTouchImageView(Context context) {
        this(context, null);
    }

    public MultiTouchImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTouchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.drawMatrix = new Matrix();
        this.baseMatrix = new Matrix();
        this.updateMatrix = new Matrix();
        this.viewRect = new RectF();
        this.drawRect = new RectF();
        this.matrixValues = new float[9];
        this.scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() { // from class: com.twitter.sdk.android.tweetui.internal.MultiTouchImageView.1
            @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                MultiTouchImageView.this.setScale(scaleGestureDetector.getScaleFactor(), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                MultiTouchImageView.this.setImageMatrix();
                return true;
            }

            @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector detector) {
                if (MultiTouchImageView.this.getScale() < 1.0f) {
                    MultiTouchImageView.this.reset();
                    MultiTouchImageView.this.setImageMatrix();
                }
            }
        });
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() { // from class: com.twitter.sdk.android.tweetui.internal.MultiTouchImageView.2
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
                MultiTouchImageView.this.setTranslate(-dx, -dy);
                MultiTouchImageView.this.setImageMatrix();
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent e) {
                if (MultiTouchImageView.this.getScale() > 1.0f) {
                    MultiTouchImageView.this.animateScale(MultiTouchImageView.this.getScale(), 1.0f, e.getX(), e.getY());
                    return true;
                }
                MultiTouchImageView.this.animateScale(MultiTouchImageView.this.getScale(), MultiTouchImageView.DOUBLE_TAP_SCALE_FACTOR, e.getX(), e.getY());
                return true;
            }
        });
    }

    boolean isInitializationComplete() {
        Drawable drawable = getDrawable();
        return drawable != null && drawable.getIntrinsicWidth() > 0;
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isInitializationComplete()) {
            initializeViewRect();
            initializeBaseMatrix(getDrawable());
            setImageMatrix();
        }
    }

    void initializeViewRect() {
        this.viewRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
    }

    void initializeBaseMatrix(Drawable drawable) {
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        RectF srcRect = new RectF(0.0f, 0.0f, drawableWidth, drawableHeight);
        this.baseMatrix.reset();
        this.baseMatrix.setRectToRect(srcRect, this.viewRect, Matrix.ScaleToFit.CENTER);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (!isInitializationComplete()) {
            return false;
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        boolean retVal = this.scaleGestureDetector.onTouchEvent(event);
        boolean retVal2 = this.gestureDetector.onTouchEvent(event) || retVal;
        return retVal2 || super.onTouchEvent(event);
    }

    void setScale(float ds, float px, float py) {
        this.updateMatrix.postScale(ds, ds, px, py);
    }

    float getScale() {
        this.updateMatrix.getValues(this.matrixValues);
        return this.matrixValues[0];
    }

    void setTranslate(float dx, float dy) {
        this.updateMatrix.postTranslate(dx, dy);
    }

    void reset() {
        this.updateMatrix.reset();
    }

    void updateMatrixBounds() {
        RectF rect = getDrawRect(getDrawMatrix());
        float dy = 0.0f;
        float dx = 0.0f;
        if (rect.height() <= this.viewRect.height()) {
            dy = ((this.viewRect.height() - rect.height()) / DOUBLE_TAP_SCALE_FACTOR) - rect.top;
        } else if (rect.top > 0.0f) {
            dy = -rect.top;
        } else if (rect.bottom < this.viewRect.height()) {
            dy = this.viewRect.height() - rect.bottom;
        }
        if (rect.width() <= this.viewRect.width()) {
            dx = ((this.viewRect.width() - rect.width()) / DOUBLE_TAP_SCALE_FACTOR) - rect.left;
        } else if (rect.left > 0.0f) {
            dx = -rect.left;
        } else if (rect.right < this.viewRect.width()) {
            dx = this.viewRect.width() - rect.right;
        }
        setTranslate(dx, dy);
    }

    RectF getDrawRect(Matrix matrix) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            this.drawRect.set(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.mapRect(this.drawRect);
        }
        return this.drawRect;
    }

    Matrix getDrawMatrix() {
        this.drawMatrix.set(this.baseMatrix);
        this.drawMatrix.postConcat(this.updateMatrix);
        return this.drawMatrix;
    }

    void setImageMatrix() {
        updateMatrixBounds();
        setScaleType(ImageView.ScaleType.MATRIX);
        setImageMatrix(getDrawMatrix());
    }

    void animateScale(float start, float end, float px, float py) {
        if (Build.VERSION.SDK_INT >= 11) {
            animateScaleHoneyComb(start, end, px, py);
        } else {
            setScale(end / getScale(), px, py);
            setImageMatrix();
        }
    }

    @TargetApi(11)
    void animateScaleHoneyComb(float start, float end, final float px, final float py) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(SCALE_ANIMATION_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.twitter.sdk.android.tweetui.internal.MultiTouchImageView.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                float ds = scale / MultiTouchImageView.this.getScale();
                MultiTouchImageView.this.setScale(ds, px, py);
                MultiTouchImageView.this.setImageMatrix();
            }
        });
        animator.start();
    }
}
