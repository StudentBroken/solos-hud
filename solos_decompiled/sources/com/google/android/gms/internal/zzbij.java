package com.google.android.gms.internal;

import android.os.Process;

/* JADX INFO: loaded from: classes67.dex */
final class zzbij implements Runnable {
    private final int mPriority;
    private final Runnable zzv;

    public zzbij(Runnable runnable, int i) {
        this.zzv = runnable;
        this.mPriority = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Process.setThreadPriority(this.mPriority);
        this.zzv.run();
    }
}
