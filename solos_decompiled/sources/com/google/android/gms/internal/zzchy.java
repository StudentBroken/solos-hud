package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzchy implements Runnable {
    private /* synthetic */ zzchx zzbtc;

    zzchy(zzchx zzchxVar) {
        this.zzbtc = zzchxVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtc.start();
    }
}
