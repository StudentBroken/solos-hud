package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcfp implements Runnable {
    private /* synthetic */ long zzbox;
    private /* synthetic */ zzcfo zzboy;
    private /* synthetic */ String zztG;

    zzcfp(zzcfo zzcfoVar, String str, long j) {
        this.zzboy = zzcfoVar;
        this.zztG = str;
        this.zzbox = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzboy.zzd(this.zztG, this.zzbox);
    }
}
