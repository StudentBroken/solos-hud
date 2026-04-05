package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcig implements Runnable {
    private /* synthetic */ zzcic zzbtj;
    private /* synthetic */ zzcfw zzbtk;

    zzcig(zzcic zzcicVar, zzcfw zzcfwVar) {
        this.zzbtj = zzcicVar;
        this.zzbtk = zzcfwVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtj.zzboi.zzzc();
        this.zzbtj.zzboi.zze(this.zzbtk);
    }
}
