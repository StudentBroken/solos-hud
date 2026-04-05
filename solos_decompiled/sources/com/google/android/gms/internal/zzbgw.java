package com.google.android.gms.internal;

import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes3.dex */
final class zzbgw extends Drawable.ConstantState {
    int mChangingConfigurations;
    int zzaGF;

    zzbgw(zzbgw zzbgwVar) {
        if (zzbgwVar != null) {
            this.mChangingConfigurations = zzbgwVar.mChangingConfigurations;
            this.zzaGF = zzbgwVar.zzaGF;
        }
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public final int getChangingConfigurations() {
        return this.mChangingConfigurations;
    }

    @Override // android.graphics.drawable.Drawable.ConstantState
    public final Drawable newDrawable() {
        return new zzbgs(this);
    }
}
