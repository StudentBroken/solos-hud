package com.google.android.gms.internal;

import android.os.RemoteException;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjw implements Runnable {
    private /* synthetic */ String zzbjl;
    private /* synthetic */ zzcgl zzbtn;
    private /* synthetic */ zzcjp zzbue;
    private /* synthetic */ boolean zzbuh = true;
    private /* synthetic */ boolean zzbui;

    zzcjw(zzcjp zzcjpVar, boolean z, boolean z2, zzcgl zzcglVar, String str) {
        this.zzbue = zzcjpVar;
        this.zzbui = z2;
        this.zzbtn = zzcglVar;
        this.zzbjl = str;
    }

    @Override // java.lang.Runnable
    public final void run() throws Throwable {
        zzcgp zzcgpVar = this.zzbue.zzbtY;
        if (zzcgpVar == null) {
            this.zzbue.zzwE().zzyv().log("Discarding data. Failed to send event to service");
            return;
        }
        if (this.zzbuh) {
            this.zzbue.zza(zzcgpVar, this.zzbui ? null : this.zzbtn);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzbjl)) {
                    zzcgpVar.zza(this.zzbtn, this.zzbue.zzwt().zzdW(this.zzbue.zzwE().zzyC()));
                } else {
                    zzcgpVar.zza(this.zzbtn, this.zzbjl, this.zzbue.zzwE().zzyC());
                }
            } catch (RemoteException e) {
                this.zzbue.zzwE().zzyv().zzj("Failed to send event to the service", e);
            }
        }
        this.zzbue.zzkO();
    }
}
