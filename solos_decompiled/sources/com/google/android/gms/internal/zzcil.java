package com.google.android.gms.internal;

import java.util.List;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes36.dex */
final class zzcil implements Callable<List<zzcfw>> {
    private /* synthetic */ String zzbjl;
    private /* synthetic */ zzcic zzbtj;
    private /* synthetic */ String zzbtl;
    private /* synthetic */ String zzbtm;

    zzcil(zzcic zzcicVar, String str, String str2, String str3) {
        this.zzbtj = zzcicVar;
        this.zzbjl = str;
        this.zzbtl = str2;
        this.zzbtm = str3;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<zzcfw> call() throws Exception {
        this.zzbtj.zzboi.zzzc();
        return this.zzbtj.zzboi.zzwy().zzi(this.zzbjl, this.zzbtl, this.zzbtm);
    }
}
