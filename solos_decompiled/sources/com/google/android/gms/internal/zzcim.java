package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcim implements Runnable {
    private /* synthetic */ zzcft zzbti;
    private /* synthetic */ zzcic zzbtj;
    private /* synthetic */ zzcgl zzbtn;

    zzcim(zzcic zzcicVar, zzcgl zzcglVar, zzcft zzcftVar) {
        this.zzbtj = zzcicVar;
        this.zzbtn = zzcglVar;
        this.zzbti = zzcftVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtj.zzboi.zzzc();
        this.zzbtj.zzboi.zzb(this.zzbtn, this.zzbti);
    }
}
