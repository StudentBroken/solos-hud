package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjh implements Runnable {
    private /* synthetic */ boolean zzbtA;
    private /* synthetic */ zzcix zzbtx;
    private /* synthetic */ AtomicReference zzbtz;

    zzcjh(zzcix zzcixVar, AtomicReference atomicReference, boolean z) {
        this.zzbtx = zzcixVar;
        this.zzbtz = atomicReference;
        this.zzbtA = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtx.zzwv().zza(this.zzbtz, this.zzbtA);
    }
}
