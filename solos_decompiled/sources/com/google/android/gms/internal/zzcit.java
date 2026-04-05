package com.google.android.gms.internal;

import com.google.android.gms.measurement.AppMeasurement;

/* JADX INFO: loaded from: classes36.dex */
final class zzcit implements Runnable {
    private /* synthetic */ String zzbjl;
    private /* synthetic */ zzcic zzbtj;
    private /* synthetic */ String zzbtp;
    private /* synthetic */ String zzbtq;
    private /* synthetic */ long zzbtr;

    zzcit(zzcic zzcicVar, String str, String str2, String str3, long j) {
        this.zzbtj = zzcicVar;
        this.zzbtp = str;
        this.zzbjl = str2;
        this.zzbtq = str3;
        this.zzbtr = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.zzbtp == null) {
            this.zzbtj.zzboi.zzww().zza(this.zzbjl, (AppMeasurement.zzb) null);
            return;
        }
        AppMeasurement.zzb zzbVar = new AppMeasurement.zzb();
        zzbVar.zzbon = this.zzbtq;
        zzbVar.zzboo = this.zzbtp;
        zzbVar.zzbop = this.zzbtr;
        this.zzbtj.zzboi.zzww().zza(this.zzbjl, zzbVar);
    }
}
