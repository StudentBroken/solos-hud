package com.google.android.gms.internal;

import android.os.Bundle;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjf implements Runnable {
    private /* synthetic */ String val$name;
    private /* synthetic */ String zzbjl;
    private /* synthetic */ long zzbtC;
    private /* synthetic */ Bundle zzbtD;
    private /* synthetic */ boolean zzbtE;
    private /* synthetic */ boolean zzbtF;
    private /* synthetic */ boolean zzbtG;
    private /* synthetic */ String zzbtl;
    private /* synthetic */ zzcix zzbtx;

    zzcjf(zzcix zzcixVar, String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        this.zzbtx = zzcixVar;
        this.zzbtl = str;
        this.val$name = str2;
        this.zzbtC = j;
        this.zzbtD = bundle;
        this.zzbtE = z;
        this.zzbtF = z2;
        this.zzbtG = z3;
        this.zzbjl = str3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtx.zzb(this.zzbtl, this.val$name, this.zzbtC, this.zzbtD, this.zzbtE, this.zzbtF, this.zzbtG, this.zzbjl);
    }
}
