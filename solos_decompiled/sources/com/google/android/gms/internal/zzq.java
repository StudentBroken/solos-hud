package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes67.dex */
final class zzq implements Runnable {
    private /* synthetic */ String zzO;
    private /* synthetic */ long zzP;
    private /* synthetic */ zzp zzQ;

    zzq(zzp zzpVar, String str, long j) {
        this.zzQ = zzpVar;
        this.zzO = str;
        this.zzP = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzQ.zzB.zza(this.zzO, this.zzP);
        this.zzQ.zzB.zzc(toString());
    }
}
