package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjj implements Runnable {
    private /* synthetic */ zzcix zzbtx;
    private /* synthetic */ AtomicReference zzbtz;

    zzcjj(zzcix zzcixVar, AtomicReference atomicReference) {
        this.zzbtx = zzcixVar;
        this.zzbtz = atomicReference;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtx.zzwv().zza(this.zzbtz);
    }
}
