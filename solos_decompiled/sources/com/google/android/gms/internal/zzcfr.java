package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcfr implements Runnable {
    private /* synthetic */ long zzbox;
    private /* synthetic */ zzcfo zzboy;

    zzcfr(zzcfo zzcfoVar, long j) {
        this.zzboy = zzcfoVar;
        this.zzbox = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzboy.zzK(this.zzbox);
    }
}
