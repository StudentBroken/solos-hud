package com.google.android.gms.internal;

import android.content.pm.PackageManager;

/* JADX INFO: loaded from: classes36.dex */
final class zzcis implements Runnable {
    private /* synthetic */ zzcft zzbti;
    private /* synthetic */ zzcic zzbtj;

    zzcis(zzcic zzcicVar, zzcft zzcftVar) {
        this.zzbtj = zzcicVar;
        this.zzbti = zzcftVar;
    }

    @Override // java.lang.Runnable
    public final void run() throws PackageManager.NameNotFoundException {
        this.zzbtj.zzboi.zzzc();
        this.zzbtj.zzboi.zze(this.zzbti);
    }
}
