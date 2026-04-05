package com.google.android.gms.internal;

import java.util.List;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes36.dex */
final class zzcik implements Callable<List<zzcfw>> {
    private /* synthetic */ zzcft zzbti;
    private /* synthetic */ zzcic zzbtj;
    private /* synthetic */ String zzbtl;
    private /* synthetic */ String zzbtm;

    zzcik(zzcic zzcicVar, zzcft zzcftVar, String str, String str2) {
        this.zzbtj = zzcicVar;
        this.zzbti = zzcftVar;
        this.zzbtl = str;
        this.zzbtm = str2;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<zzcfw> call() throws Exception {
        this.zzbtj.zzboi.zzzc();
        return this.zzbtj.zzboi.zzwy().zzi(this.zzbti.packageName, this.zzbtl, this.zzbtm);
    }
}
