package com.google.android.gms.internal;

import com.google.android.gms.measurement.AppMeasurement;

/* JADX INFO: loaded from: classes36.dex */
final class zzciz implements Runnable {
    private /* synthetic */ zzcix zzbtx;
    private /* synthetic */ AppMeasurement.ConditionalUserProperty zzbty;

    zzciz(zzcix zzcixVar, AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        this.zzbtx = zzcixVar;
        this.zzbty = conditionalUserProperty;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtx.zzb(this.zzbty);
    }
}
