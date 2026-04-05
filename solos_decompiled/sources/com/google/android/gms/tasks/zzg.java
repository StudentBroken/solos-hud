package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes56.dex */
final class zzg<TResult> implements zzk<TResult> {
    private final Object mLock = new Object();
    private final Executor zzbEs;
    private OnFailureListener zzbMa;

    public zzg(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        this.zzbEs = executor;
        this.zzbMa = onFailureListener;
    }

    @Override // com.google.android.gms.tasks.zzk
    public final void cancel() {
        synchronized (this.mLock) {
            this.zzbMa = null;
        }
    }

    @Override // com.google.android.gms.tasks.zzk
    public final void onComplete(@NonNull Task<TResult> task) {
        if (task.isSuccessful()) {
            return;
        }
        synchronized (this.mLock) {
            if (this.zzbMa != null) {
                this.zzbEs.execute(new zzh(this, task));
            }
        }
    }
}
