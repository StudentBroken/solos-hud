package com.google.android.gms.internal;

import android.os.RemoteException;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjs implements Runnable {
    private /* synthetic */ zzcjp zzbue;

    zzcjs(zzcjp zzcjpVar) {
        this.zzbue = zzcjpVar;
    }

    @Override // java.lang.Runnable
    public final void run() throws Throwable {
        zzcgp zzcgpVar = this.zzbue.zzbtY;
        if (zzcgpVar == null) {
            this.zzbue.zzwE().zzyv().log("Discarding data. Failed to send app launch");
            return;
        }
        try {
            zzcgpVar.zza(this.zzbue.zzwt().zzdW(this.zzbue.zzwE().zzyC()));
            this.zzbue.zza(zzcgpVar, (com.google.android.gms.common.internal.safeparcel.zza) null);
            this.zzbue.zzkO();
        } catch (RemoteException e) {
            this.zzbue.zzwE().zzyv().zzj("Failed to send app launch to the service", e);
        }
    }
}
