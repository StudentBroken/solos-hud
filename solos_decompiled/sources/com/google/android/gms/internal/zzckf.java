package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzckf implements Runnable {
    private /* synthetic */ zzckc zzbuo;
    private /* synthetic */ zzcgp zzbup;

    zzckf(zzckc zzckcVar, zzcgp zzcgpVar) {
        this.zzbuo = zzckcVar;
        this.zzbup = zzcgpVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzbuo) {
            zzckc.zza(this.zzbuo, false);
            if (!this.zzbuo.zzbue.isConnected()) {
                this.zzbuo.zzbue.zzwE().zzyA().log("Connected to remote service");
                this.zzbuo.zzbue.zza(this.zzbup);
            }
        }
    }
}
