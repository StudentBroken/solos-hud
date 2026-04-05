package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzchh implements Runnable {
    private /* synthetic */ boolean zzbrk;
    private /* synthetic */ zzchg zzbrl;

    zzchh(zzchg zzchgVar, boolean z) {
        this.zzbrl = zzchgVar;
        this.zzbrk = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbrl.zzboi.zzam(this.zzbrk);
    }
}
