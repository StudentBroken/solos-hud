package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.ArrayDeque;
import java.util.Queue;

/* JADX INFO: loaded from: classes56.dex */
final class zzl<TResult> {
    private final Object mLock = new Object();
    private Queue<zzk<TResult>> zzbMe;
    private boolean zzbMf;

    zzl() {
    }

    public final void zza(@NonNull Task<TResult> task) {
        zzk<TResult> zzkVarPoll;
        synchronized (this.mLock) {
            if (this.zzbMe == null || this.zzbMf) {
                return;
            }
            this.zzbMf = true;
            while (true) {
                synchronized (this.mLock) {
                    zzkVarPoll = this.zzbMe.poll();
                    if (zzkVarPoll == null) {
                        this.zzbMf = false;
                        return;
                    }
                }
                zzkVarPoll.onComplete(task);
            }
        }
    }

    public final void zza(@NonNull zzk<TResult> zzkVar) {
        synchronized (this.mLock) {
            if (this.zzbMe == null) {
                this.zzbMe = new ArrayDeque();
            }
            this.zzbMe.add(zzkVar);
        }
    }
}
