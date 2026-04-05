package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcif implements Runnable {
    private /* synthetic */ zzcft zzbti;
    private /* synthetic */ zzcic zzbtj;
    private /* synthetic */ zzcfw zzbtk;

    zzcif(zzcic zzcicVar, zzcfw zzcfwVar, zzcft zzcftVar) {
        this.zzbtj = zzcicVar;
        this.zzbtk = zzcfwVar;
        this.zzbti = zzcftVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtj.zzboi.zzzc();
        this.zzbtj.zzboi.zzb(this.zzbtk, this.zzbti);
    }
}
