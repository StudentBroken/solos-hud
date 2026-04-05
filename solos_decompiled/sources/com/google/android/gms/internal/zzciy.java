package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzciy implements Runnable {
    private /* synthetic */ boolean val$enabled;
    private /* synthetic */ zzcix zzbtx;

    zzciy(zzcix zzcixVar, boolean z) {
        this.zzbtx = zzcixVar;
        this.val$enabled = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtx.zzan(this.val$enabled);
    }
}
