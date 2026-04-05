package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzciq implements Runnable {
    private /* synthetic */ zzcft zzbti;
    private /* synthetic */ zzcic zzbtj;
    private /* synthetic */ zzcku zzbto;

    zzciq(zzcic zzcicVar, zzcku zzckuVar, zzcft zzcftVar) {
        this.zzbtj = zzcicVar;
        this.zzbto = zzckuVar;
        this.zzbti = zzcftVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtj.zzboi.zzzc();
        this.zzbtj.zzboi.zzb(this.zzbto, this.zzbti);
    }
}
