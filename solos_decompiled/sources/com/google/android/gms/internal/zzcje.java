package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcje implements Runnable {
    private /* synthetic */ long zzbtB;
    private /* synthetic */ zzcix zzbtx;

    zzcje(zzcix zzcixVar, long j) {
        this.zzbtx = zzcixVar;
        this.zzbtB = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtx.zzwF().zzbrC.set(this.zzbtB);
        this.zzbtx.zzwE().zzyA().zzj("Session timeout duration set", Long.valueOf(this.zzbtB));
    }
}
