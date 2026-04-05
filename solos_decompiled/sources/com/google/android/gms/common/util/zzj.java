package com.google.android.gms.common.util;

import android.os.SystemClock;

/* JADX INFO: loaded from: classes67.dex */
public final class zzj implements zzf {
    private static zzj zzaJK = new zzj();

    private zzj() {
    }

    public static zzf zzrX() {
        return zzaJK;
    }

    @Override // com.google.android.gms.common.util.zzf
    public final long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override // com.google.android.gms.common.util.zzf
    public final long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    @Override // com.google.android.gms.common.util.zzf
    public final long nanoTime() {
        return System.nanoTime();
    }
}
