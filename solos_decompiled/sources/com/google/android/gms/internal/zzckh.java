package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzckh implements Runnable {
    private /* synthetic */ zzckc zzbuo;

    zzckh(zzckc zzckcVar) {
        this.zzbuo = zzckcVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzcjp.zza(this.zzbuo.zzbue, (zzcgp) null);
        this.zzbuo.zzbue.zzzj();
    }
}
