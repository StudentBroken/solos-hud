package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcip implements Runnable {
    private /* synthetic */ zzcft zzbti;
    private /* synthetic */ zzcic zzbtj;
    private /* synthetic */ zzcku zzbto;

    zzcip(zzcic zzcicVar, zzcku zzckuVar, zzcft zzcftVar) {
        this.zzbtj = zzcicVar;
        this.zzbto = zzckuVar;
        this.zzbti = zzcftVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtj.zzboi.zzzc();
        this.zzbtj.zzboi.zzc(this.zzbto, this.zzbti);
    }
}
