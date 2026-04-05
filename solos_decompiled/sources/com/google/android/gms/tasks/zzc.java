package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes56.dex */
final class zzc<TResult, TContinuationResult> implements OnFailureListener, OnSuccessListener<TContinuationResult>, zzk<TResult> {
    private final Executor zzbEs;
    private final Continuation<TResult, Task<TContinuationResult>> zzbLT;
    private final zzn<TContinuationResult> zzbLU;

    public zzc(@NonNull Executor executor, @NonNull Continuation<TResult, Task<TContinuationResult>> continuation, @NonNull zzn<TContinuationResult> zznVar) {
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
        this.zzbEs.execute(new zzd(this, task));
    }

    @Override // com.google.android.gms.tasks.OnFailureListener
    public final void onFailure(@NonNull Exception exc) {
        this.zzbLU.setException(exc);
    }

    @Override // com.google.android.gms.tasks.OnSuccessListener
    public final void onSuccess(TContinuationResult tcontinuationresult) {
        this.zzbLU.setResult(tcontinuationresult);
    }
}
