package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
final class zzckr {
    private long mStartTime;
    private final com.google.android.gms.common.util.zzf zzvz;

    public zzckr(com.google.android.gms.common.util.zzf zzfVar) {
        zzbr.zzu(zzfVar);
        this.zzvz = zzfVar;
    }

    public final void clear() {
        this.mStartTime = 0L;
    }

    public final void start() {
        this.mStartTime = this.zzvz.elapsedRealtime();
    }

    public final boolean zzu(long j) {
        return this.mStartTime == 0 || this.zzvz.elapsedRealtime() - this.mStartTime >= j;
    }
}
