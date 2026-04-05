package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcka implements Runnable {
    private /* synthetic */ zzcku zzbto;
    private /* synthetic */ zzcjp zzbue;
    private /* synthetic */ boolean zzbui;

    zzcka(zzcjp zzcjpVar, boolean z, zzcku zzckuVar) {
        this.zzbue = zzcjpVar;
        this.zzbui = z;
        this.zzbto = zzckuVar;
    }

    @Override // java.lang.Runnable
    public final void run() throws Throwable {
        zzcgp zzcgpVar = this.zzbue.zzbtY;
        if (zzcgpVar == null) {
            this.zzbue.zzwE().zzyv().log("Discarding data. Failed to set user attribute");
        } else {
            this.zzbue.zza(zzcgpVar, this.zzbui ? null : this.zzbto);
            this.zzbue.zzkO();
        }
    }
}
