package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.measurement.AppMeasurement;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjt implements Runnable {
    private /* synthetic */ zzcjp zzbue;
    private /* synthetic */ AppMeasurement.zzb zzbug;

    zzcjt(zzcjp zzcjpVar, AppMeasurement.zzb zzbVar) {
        this.zzbue = zzcjpVar;
        this.zzbug = zzbVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzcgp zzcgpVar = this.zzbue.zzbtY;
        if (zzcgpVar == null) {
            this.zzbue.zzwE().zzyv().log("Failed to send current screen to service");
            return;
        }
        try {
            if (this.zzbug == null) {
                zzcgpVar.zza(0L, (String) null, (String) null, this.zzbue.getContext().getPackageName());
            } else {
                zzcgpVar.zza(this.zzbug.zzbop, this.zzbug.zzbon, this.zzbug.zzboo, this.zzbue.getContext().getPackageName());
            }
            this.zzbue.zzkO();
        } catch (RemoteException e) {
            this.zzbue.zzwE().zzyv().zzj("Failed to send current screen to the service", e);
        }
    }
}
