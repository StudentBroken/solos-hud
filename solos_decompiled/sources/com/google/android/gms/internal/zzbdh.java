package com.google.android.gms.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

/* JADX INFO: Add missing generic type declarations: [TResult] */
/* JADX INFO: loaded from: classes3.dex */
final class zzbdh<TResult> implements OnCompleteListener<TResult> {
    private /* synthetic */ zzbdf zzaCW;
    private /* synthetic */ TaskCompletionSource zzaCX;

    zzbdh(zzbdf zzbdfVar, TaskCompletionSource taskCompletionSource) {
        this.zzaCW = zzbdfVar;
        this.zzaCX = taskCompletionSource;
    }

    @Override // com.google.android.gms.tasks.OnCompleteListener
    public final void onComplete(@NonNull Task<TResult> task) {
        this.zzaCW.zzaCU.remove(this.zzaCX);
    }
}
