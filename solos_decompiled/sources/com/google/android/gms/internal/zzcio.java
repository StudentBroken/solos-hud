package com.google.android.gms.internal;

import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes36.dex */
final class zzcio implements Callable<byte[]> {
    private /* synthetic */ String zzbjl;
    private /* synthetic */ zzcic zzbtj;
    private /* synthetic */ zzcgl zzbtn;

    zzcio(zzcic zzcicVar, zzcgl zzcglVar, String str) {
        this.zzbtj = zzcicVar;
        this.zzbtn = zzcglVar;
        this.zzbjl = str;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ byte[] call() throws Exception {
        this.zzbtj.zzboi.zzzc();
        return this.zzbtj.zzboi.zza(this.zzbtn, this.zzbjl);
    }
}
