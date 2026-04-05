package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjd implements Runnable {
    private /* synthetic */ long zzbtB;
    private /* synthetic */ zzcix zzbtx;

    zzcjd(zzcix zzcixVar, long j) {
        this.zzbtx = zzcixVar;
        this.zzbtB = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtx.zzwF().zzbrB.set(this.zzbtB);
        this.zzbtx.zzwE().zzyA().zzj("Minimum session duration set", Long.valueOf(this.zzbtB));
    }
}
