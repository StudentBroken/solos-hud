package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcin implements Runnable {
    private /* synthetic */ String zzbjl;
    private /* synthetic */ zzcic zzbtj;
    private /* synthetic */ zzcgl zzbtn;

    zzcin(zzcic zzcicVar, zzcgl zzcglVar, String str) {
        this.zzbtj = zzcicVar;
        this.zzbtn = zzcglVar;
        this.zzbjl = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtj.zzboi.zzzc();
        this.zzbtj.zzboi.zzb(this.zzbtn, this.zzbjl);
    }
}
