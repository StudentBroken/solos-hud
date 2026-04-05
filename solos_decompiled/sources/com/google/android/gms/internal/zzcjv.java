package com.google.android.gms.internal;

import android.os.RemoteException;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjv implements Runnable {
    private /* synthetic */ zzcjp zzbue;

    zzcjv(zzcjp zzcjpVar) {
        this.zzbue = zzcjpVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzcgp zzcgpVar = this.zzbue.zzbtY;
        if (zzcgpVar == null) {
            this.zzbue.zzwE().zzyv().log("Failed to send measurementEnabled to service");
            return;
        }
        try {
            zzcgpVar.zzb(this.zzbue.zzwt().zzdW(this.zzbue.zzwE().zzyC()));
            this.zzbue.zzkO();
        } catch (RemoteException e) {
            this.zzbue.zzwE().zzyv().zzj("Failed to send measurementEnabled to the service", e);
        }
    }
}
