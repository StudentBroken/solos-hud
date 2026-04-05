package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzckp implements Runnable {
    private /* synthetic */ long zzbox;
    private /* synthetic */ zzckm zzbuy;

    zzckp(zzckm zzckmVar, long j) {
        this.zzbuy = zzckmVar;
        this.zzbox = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbuy.zzae(this.zzbox);
    }
}
