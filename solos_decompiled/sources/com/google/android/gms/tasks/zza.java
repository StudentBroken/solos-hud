package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes56.dex */
final class zza<TResult, TContinuationResult> implements zzk<TResult> {
    private final Executor zzbEs;
    private final Continuation<TResult, TContinuationResult> zzbLT;
    private final zzn<TContinuationResult> zzbLU;

    public zza(@NonNull Executor executor, @NonNull Continuation<TResult, TContinuationResult> continuation, @NonNull zzn<TContinuationResult> zznVar) {
        this.zzbEs = executor;
        this.zzbLT = continuation;
        this.zzbLU = zznVar;
    }

    @Override // com.google.android.gms.tasks.zzk
    public final void cancel() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.tasks.zzk
    public final void onComplete(@NonNull Task<TResult> task) {
        this.zzbEs.execute(new zzb(this, task));
    }
}
