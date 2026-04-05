package com.google.android.gms.internal;

import com.google.android.gms.measurement.AppMeasurement;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjn implements Runnable {
    private /* synthetic */ zzcjl zzbtU;
    private /* synthetic */ zzcjo zzbtV;

    zzcjn(zzcjl zzcjlVar, zzcjo zzcjoVar) {
        this.zzbtU = zzcjlVar;
        this.zzbtV = zzcjoVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtU.zza(this.zzbtV);
        this.zzbtU.zzbtI = null;
        this.zzbtU.zzwv().zza((AppMeasurement.zzb) null);
    }
}
