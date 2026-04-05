package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes67.dex */
public class zzaa extends Exception {
    private long zzA;
    private zzn zzah;

    public zzaa() {
        this.zzah = null;
    }

    public zzaa(zzn zznVar) {
        this.zzah = zznVar;
    }

    public zzaa(Throwable th) {
        super(th);
        this.zzah = null;
    }

    final void zza(long j) {
        this.zzA = j;
    }
}
