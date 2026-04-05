package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzckd implements Runnable {
    private /* synthetic */ zzcgp zzbun;
    private /* synthetic */ zzckc zzbuo;

    zzckd(zzckc zzckcVar, zzcgp zzcgpVar) {
        this.zzbuo = zzckcVar;
        this.zzbun = zzcgpVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzbuo) {
            zzckc.zza(this.zzbuo, false);
            if (!this.zzbuo.zzbue.isConnected()) {
                this.zzbuo.zzbue.zzwE().zzyB().log("Connected to service");
                this.zzbuo.zzbue.zza(this.zzbun);
            }
        }
    }
}
