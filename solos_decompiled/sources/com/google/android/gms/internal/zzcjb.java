package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjb implements Runnable {
    private /* synthetic */ String zzbjl;
    private /* synthetic */ String zzbtl;
    private /* synthetic */ String zzbtm;
    private /* synthetic */ zzcix zzbtx;
    private /* synthetic */ AtomicReference zzbtz;

    zzcjb(zzcix zzcixVar, AtomicReference atomicReference, String str, String str2, String str3) {
        this.zzbtx = zzcixVar;
        this.zzbtz = atomicReference;
        this.zzbjl = str;
        this.zzbtl = str2;
        this.zzbtm = str3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtx.zzboi.zzwv().zza(this.zzbtz, this.zzbjl, this.zzbtl, this.zzbtm);
    }
}
