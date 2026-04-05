package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcid implements Runnable {
    private /* synthetic */ zzcft zzbti;
    private /* synthetic */ zzcic zzbtj;

    zzcid(zzcic zzcicVar, zzcft zzcftVar) {
        this.zzbtj = zzcicVar;
        this.zzbti = zzcftVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtj.zzboi.zzzc();
        this.zzbtj.zzboi.zzd(this.zzbti);
    }
}
