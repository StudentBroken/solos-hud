package android.support.wearable.view;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public class CrossfadeDrawable extends Drawable implements Drawable.Callback {
    private int mAlpha;
    private Drawable mBase;
    private int mChangingConfigs;
    private ColorFilter mColorFilter;
    private int mColorFilterColor;
    private PorterDuff.Mode mColorFilterMode;
    private boolean mDither;
    private Drawable mFading;
    private boolean mFilterBitmap;
    private float mProgress;

    public void setFading(Drawable d) {
        if (this.mFading != d) {
            if (this.mFading != null) {
                this.mFading.setCallback(null);
            }
            this.mFading = d;
            if (d != null) {
                initDrawable(d);
            }
            invalidateSelf();
        }
    }

    public void setBase(Drawable d) {
        if (this.mBase != d) {
            if (this.mBase != null) {
                this.mBase.setCallback(null);
            }
            this.mBase = d;
            initDrawable(d);
            invalidateSelf();
        }
    }

    public void setProgress(float progress) {
        float updated = Func.clamp(progress, 0, 1);
        if (updated != this.mProgress) {
            this.mProgress = updated;
            invalidateSelf();
        }
    }

    private void initDrawable(Drawable d) {
        d.setCallback(this);
        d.setState(getState());
        if (this.mColorFilter != null) {
            d.setColorFilter(this.mColorFilter);
        }
        if (this.mColorFilterMode != null) {
            d.setColorFilter(this.mColorFilterColor, this.mColorFilterMode);
        }
        d.setDither(this.mDither);
        d.setFilterBitmap(this.mFilterBitmap);
        d.setBounds(getBounds());
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.mBase != null && (this.mProgress < 1.0f || this.mFading == null)) {
            this.mBase.setAlpha(255);
            this.mBase.draw(canvas);
        }
        if (this.mFading != null && this.mProgress > 0.0f) {
            this.mFading.setAlpha((int) (255.0f * this.mProgress));
            this.mFading.draw(canvas);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        int fading = this.mFading == null ? -1 : this.mFading.getIntrinsicWidth();
        int base = this.mBase == null ? -1 : this.mBase.getIntrinsicHeight();
        return Math.max(fading, base);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        int fading = this.mFading == null ? -1 : this.mFading.getIntrinsicHeight();
        int base = this.mBase == null ? -1 : this.mBase.getIntrinsicHeight();
        return Math.max(fading, base);
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        if (this.mBase != null) {
            this.mBase.setBounds(bounds);
        }
        if (this.mFading != null) {
            this.mFading.setBounds(bounds);
        }
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        if (this.mFading != null) {
            this.mFading.jumpToCurrentState();
        }
        if (this.mBase != null) {
            this.mBase.jumpToCurrentState();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setChangingConfigurations(int configs) {
        if (this.mChangingConfigs != configs) {
            this.mChangingConfigs = configs;
            if (this.mFading != null) {
                this.mFading.setChangingConfigurations(configs);
            }
            if (this.mBase != null) {
                this.mBase.setChangingConfigurations(configs);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean filter) {
        if (this.mFilterBitmap != filter) {
            this.mFilterBitmap = filter;
            if (this.mFading != null) {
                this.mFading.setFilterBitmap(filter);
            }
            if (this.mBase != null) {
                this.mBase.setFilterBitmap(filter);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean dither) {
        if (this.mDither != dither) {
            this.mDither = dither;
            if (this.mFading != null) {
                this.mFading.setDither(dither);
            }
            if (this.mBase != null) {
                this.mBase.setDither(dither);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        if (this.mColorFilter != cf) {
            this.mColorFilter = cf;
            if (this.mFading != null) {
                this.mFading.setColorFilter(cf);
            }
            if (this.mBase != null) {
                this.mBase.setColorFilter(cf);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(int color, PorterDuff.Mode mode) {
        if (this.mColorFilterColor != color || this.mColorFilterMode != mode) {
            this.mColorFilterColor = color;
            this.mColorFilterMode = mode;
            if (this.mFading != null) {
                this.mFading.setColorFilter(color, mode);
            }
            if (this.mBase != null) {
                this.mBase.setColorFilter(color, mode);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void clearColorFilter() {
        if (this.mColorFilterMode != null) {
            this.mColorFilterMode = null;
            if (this.mFading != null) {
                this.mFading.clearColorFilter();
            }
            if (this.mBase != null) {
                this.mBase.clearColorFilter();
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return this.mChangingConfigs;
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] state) {
        boolean changed = this.mFading != null ? false | this.mFading.setState(state) : false;
        if (this.mBase != null) {
            return changed | this.mBase.setState(state);
        }
        return changed;
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int level) {
        boolean changed = this.mFading != null ? false | this.mFading.setLevel(level) : false;
        if (this.mBase != null) {
            return changed | this.mBase.setLevel(level);
        }
        return changed;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return (this.mFading != null && this.mFading.isStateful()) || (this.mBase != null && this.mBase.isStateful());
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mAlpha;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        if (alpha != this.mAlpha) {
            this.mAlpha = alpha;
            invalidateSelf();
        }
    }

    public Drawable getBase() {
        return this.mBase;
    }

    public Drawable getFading() {
        return this.mFading;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return resolveOpacity(this.mFading == null ? 0 : this.mFading.getOpacity(), this.mBase != null ? this.mBase.getOpacity() : 0);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable who) {
        if ((who == this.mFading || who == this.mBase) && getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if ((who == this.mFading || who == this.mBase) && getCallback() != null) {
            getCallback().scheduleDrawable(this, what, when);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable who, Runnable what) {
        if ((who == this.mFading || who == this.mBase) && getCallback() != null) {
            getCallback().unscheduleDrawable(this, what);
        }
    }
}
