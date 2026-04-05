package com.google.android.gms.internal;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.widget.helper.ItemTouchHelper;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbgs extends Drawable implements Drawable.Callback {
    private int mFrom;
    private boolean zzaGA;
    private boolean zzaGB;
    private int zzaGC;
    private boolean zzaGl;
    private int zzaGq;
    private int zzaGr;
    private int zzaGs;
    private int zzaGt;
    private int zzaGu;
    private boolean zzaGv;
    private zzbgw zzaGw;
    private Drawable zzaGx;
    private Drawable zzaGy;
    private boolean zzaGz;
    private long zzahb;

    public zzbgs(Drawable drawable, Drawable drawable2) {
        this(null);
        drawable = drawable == null ? zzbgu.zzaGD : drawable;
        this.zzaGx = drawable;
        drawable.setCallback(this);
        this.zzaGw.zzaGF |= drawable.getChangingConfigurations();
        drawable2 = drawable2 == null ? zzbgu.zzaGD : drawable2;
        this.zzaGy = drawable2;
        drawable2.setCallback(this);
        this.zzaGw.zzaGF |= drawable2.getChangingConfigurations();
    }

    zzbgs(zzbgw zzbgwVar) {
        this.zzaGq = 0;
        this.zzaGs = 255;
        this.zzaGu = 0;
        this.zzaGl = true;
        this.zzaGw = new zzbgw(zzbgwVar);
    }

    private final boolean canConstantState() {
        if (!this.zzaGz) {
            this.zzaGA = (this.zzaGx.getConstantState() == null || this.zzaGy.getConstantState() == null) ? false : true;
            this.zzaGz = true;
        }
        return this.zzaGA;
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        boolean z = false;
        switch (this.zzaGq) {
            case 1:
                this.zzahb = SystemClock.uptimeMillis();
                this.zzaGq = 2;
                break;
            case 2:
                if (this.zzahb >= 0) {
                    float fUptimeMillis = (SystemClock.uptimeMillis() - this.zzahb) / this.zzaGt;
                    z = fUptimeMillis >= 1.0f;
                    if (z) {
                        this.zzaGq = 0;
                    }
                    this.zzaGu = (int) ((Math.min(fUptimeMillis, 1.0f) * this.zzaGr) + 0.0f);
                    break;
                }
            default:
                z = z;
                break;
        }
        int i = this.zzaGu;
        boolean z2 = this.zzaGl;
        Drawable drawable = this.zzaGx;
        Drawable drawable2 = this.zzaGy;
        if (z) {
            if (!z2 || i == 0) {
                drawable.draw(canvas);
            }
            if (i == this.zzaGs) {
                drawable2.setAlpha(this.zzaGs);
                drawable2.draw(canvas);
                return;
            }
            return;
        }
        if (z2) {
            drawable.setAlpha(this.zzaGs - i);
        }
        drawable.draw(canvas);
        if (z2) {
            drawable.setAlpha(this.zzaGs);
        }
        if (i > 0) {
            drawable2.setAlpha(i);
            drawable2.draw(canvas);
            drawable2.setAlpha(this.zzaGs);
        }
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public final int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.zzaGw.mChangingConfigurations | this.zzaGw.zzaGF;
    }

    @Override // android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        if (!canConstantState()) {
            return null;
        }
        this.zzaGw.mChangingConfigurations = getChangingConfigurations();
        return this.zzaGw;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        return Math.max(this.zzaGx.getIntrinsicHeight(), this.zzaGy.getIntrinsicHeight());
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        return Math.max(this.zzaGx.getIntrinsicWidth(), this.zzaGy.getIntrinsicWidth());
    }

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        if (!this.zzaGB) {
            this.zzaGC = Drawable.resolveOpacity(this.zzaGx.getOpacity(), this.zzaGy.getOpacity());
            this.zzaGB = true;
        }
        return this.zzaGC;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void invalidateDrawable(Drawable drawable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final Drawable mutate() {
        if (!this.zzaGv && super.mutate() == this) {
            if (!canConstantState()) {
                throw new IllegalStateException("One or more children of this LayerDrawable does not have constant state; this drawable cannot be mutated.");
            }
            this.zzaGx.mutate();
            this.zzaGy.mutate();
            this.zzaGv = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    protected final void onBoundsChange(Rect rect) {
        this.zzaGx.setBounds(rect);
        this.zzaGy.setBounds(rect);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, runnable, j);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        if (this.zzaGu == this.zzaGs) {
            this.zzaGu = i;
        }
        this.zzaGs = i;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        this.zzaGx.setColorFilter(colorFilter);
        this.zzaGy.setColorFilter(colorFilter);
    }

    public final void startTransition(int i) {
        this.mFrom = 0;
        this.zzaGr = this.zzaGs;
        this.zzaGu = 0;
        this.zzaGt = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
        this.zzaGq = 1;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, runnable);
        }
    }

    public final Drawable zzqU() {
        return this.zzaGy;
    }
}
