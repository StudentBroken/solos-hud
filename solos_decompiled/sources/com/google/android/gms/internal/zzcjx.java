package com.google.android.gms.internal;

import android.os.RemoteException;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjx implements Runnable {
    private /* synthetic */ zzcjp zzbue;
    private /* synthetic */ boolean zzbuh = true;
    private /* synthetic */ boolean zzbui;
    private /* synthetic */ zzcfw zzbuj;
    private /* synthetic */ zzcfw zzbuk;

    zzcjx(zzcjp zzcjpVar, boolean z, boolean z2, zzcfw zzcfwVar, zzcfw zzcfwVar2) {
        this.zzbue = zzcjpVar;
        this.zzbui = z2;
        this.zzbuj = zzcfwVar;
        this.zzbuk = zzcfwVar2;
    }

    @Override // java.lang.Runnable
    public final void run() throws Throwable {
        zzcgp zzcgpVar = this.zzbue.zzbtY;
        if (zzcgpVar == null) {
            this.zzbue.zzwE().zzyv().log("Discarding data. Failed to send conditional user property to service");
            return;
        }
        if (this.zzbuh) {
            this.zzbue.zza(zzcgpVar, this.zzbui ? null : this.zzbuj);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzbuk.packageName)) {
                    zzcgpVar.zza(this.zzbuj, this.zzbue.zzwt().zzdW(this.zzbue.zzwE().zzyC()));
                } else {
                    zzcgpVar.zzb(this.zzbuj);
                }
            } catch (RemoteException e) {
                this.zzbue.zzwE().zzyv().zzj("Failed to send conditional user property to the service", e);
            }
        }
        this.zzbue.zzkO();
    }
}
