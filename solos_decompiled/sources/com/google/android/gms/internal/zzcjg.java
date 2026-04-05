package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjg implements Runnable {
    private /* synthetic */ String val$name;
    private /* synthetic */ long zzbtC;
    private /* synthetic */ Object zzbtH;
    private /* synthetic */ String zzbtl;
    private /* synthetic */ zzcix zzbtx;

    zzcjg(zzcix zzcixVar, String str, String str2, Object obj, long j) {
        this.zzbtx = zzcixVar;
        this.zzbtl = str;
        this.val$name = str2;
        this.zzbtH = obj;
        this.zzbtC = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbtx.zza(this.zzbtl, this.val$name, this.zzbtH, this.zzbtC);
    }
}
