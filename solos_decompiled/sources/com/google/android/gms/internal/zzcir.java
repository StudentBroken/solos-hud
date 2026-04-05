package com.google.android.gms.internal;

import java.util.List;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes36.dex */
final class zzcir implements Callable<List<zzckw>> {
    private /* synthetic */ zzcft zzbti;
    private /* synthetic */ zzcic zzbtj;

    zzcir(zzcic zzcicVar, zzcft zzcftVar) {
        this.zzbtj = zzcicVar;
        this.zzbti = zzcftVar;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<zzckw> call() throws Exception {
        this.zzbtj.zzboi.zzzc();
        return this.zzbtj.zzboi.zzwy().zzdQ(this.zzbti.packageName);
    }
}
