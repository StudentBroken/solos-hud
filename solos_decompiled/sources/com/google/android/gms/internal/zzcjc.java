package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjc implements Runnable {
    private /* synthetic */ String zzbjl;
    private /* synthetic */ boolean zzbtA;
    private /* synthetic */ String zzbtl;
    private /* synthetic */ String zzbtm;
    private /* synthetic */ zzcix zzbtx;
    private /* synthetic */ AtomicReference zzbtz;

    zzcjc(zzcix zzcixVar, AtomicReference atomicReference, String str, String str2, String str3, boolean z) {
        this.zzbtx = zzcixVar;
        this.zzbtz = atomicReference;
        this.zzbjl = str;
        this.zzbtl = str2;
        this.zzbtm = str3;
        this.zzbtA = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtx.zzboi.zzwv().zza(this.zzbtz, this.zzbjl, this.zzbtl, this.zzbtm, this.zzbtA);
    }
}
