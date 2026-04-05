package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes19.dex */
class RoundRectDrawable extends Drawable {
    private final RectF mBoundsF;
    private final Rect mBoundsI;
    private float mPadding;
    private float mRadius;
    private ColorStateList mTint;
    private PorterDuffColorFilter mTintFilter;
    private boolean mInsetForPadding = false;
    private boolean mInsetForRadius = true;
    private PorterDuff.Mode mTintMode = PorterDuff.Mode.SRC_IN;
    private final Paint mPaint = new Paint(5);

    public RoundRectDrawable(int backgroundColor, float radius) {
        this.mRadius = radius;
        this.mPaint.setColor(backgroundColor);
        this.mBoundsF = new RectF();
        this.mBoundsI = new Rect();
    }

    void setPadding(float padding, boolean insetForPadding, boolean insetForRadius) {
        if (padding != this.mPadding || this.mInsetForPadding != insetForPadding || this.mInsetForRadius != insetForRadius) {
            this.mPadding = padding;
            this.mInsetForPadding = insetForPadding;
            this.mInsetForRadius = insetForRadius;
            updateBounds(null);
            invalidateSelf();
        }
    }

    float getPadding() {
        return this.mPadding;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        boolean clearColorFilter;
        Paint paint = this.mPaint;
        if (this.mTintFilter != null && paint.getColorFilter() == null) {
            paint.setColorFilter(this.mTintFilter);
            clearColorFilter = true;
        } else {
            clearColorFilter = false;
        }
        canvas.drawRoundRect(this.mBoundsF, this.mRadius, this.mRadius, paint);
        if (clearColorFilter) {
            paint.setColorFilter(null);
        }
    }

    private void updateBounds(Rect bounds) {
        if (bounds == null) {
            bounds = getBounds();
        }
        this.mBoundsF.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        this.mBoundsI.set(bounds);
        if (this.mInsetForPadding) {
            float vInset = RoundRectDrawableWithShadow.calculateVerticalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
            float hInset = RoundRectDrawableWithShadow.calculateHorizontalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
            this.mBoundsI.inset((int) Math.ceil(hInset), (int) Math.ceil(vInset));
            this.mBoundsF.set(this.mBoundsI);
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateBounds(bounds);
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        outline.setRoundRect(this.mBoundsI, this.mRadius);
    }

    void setRadius(float radius) {
        if (radius != this.mRadius) {
            this.mRadius = radius;
            updateBounds(null);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList tint) {
        this.mTint = tint;
        this.mTintFilter = createTintFilter(this.mTint, this.mTintMode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode tintMode) {
        this.mTintMode = tintMode;
        this.mTintFilter = createTintFilter(this.mTint, this.mTintMode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] stateSet) {
        if (this.mTint == null || this.mTintMode == null) {
            return false;
        }
        this.mTintFilter = createTintFilter(this.mTint, this.mTintMode);
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return (this.mTint != null && this.mTint.isStateful()) || super.isStateful();
    }

    private PorterDuffColorFilter createTintFilter(ColorStateList tint, PorterDuff.Mode tintMode) {
        if (tint == null || tintMode == null) {
            return null;
        }
        int color = tint.getColorForState(getState(), 0);
        return new PorterDuffColorFilter(color, tintMode);
    }
}
