package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes56.dex */
final class zze<TResult> implements zzk<TResult> {
    private final Object mLock = new Object();
    private final Executor zzbEs;
    private OnCompleteListener<TResult> zzbLY;

    public zze(@NonNull Executor executor, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        this.zzbEs = executor;
        this.zzbLY = onCompleteListener;
    }

    @Override // com.google.android.gms.tasks.zzk
    public final void cancel() {
        synchronized (this.mLock) {
            this.zzbLY = null;
        }
    }

    @Override // com.google.android.gms.tasks.zzk
    public final void onComplete(@NonNull Task<TResult> task) {
        synchronized (this.mLock) {
            if (this.zzbLY == null) {
                return;
            }
            this.zzbEs.execute(new zzf(this, task));
        }
    }
}
