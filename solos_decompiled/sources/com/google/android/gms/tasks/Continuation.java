package com.google.android.gms.tasks;

import android.support.annotation.NonNull;

/* JADX INFO: loaded from: classes56.dex */
public interface Continuation<TResult, TContinuationResult> {
    TContinuationResult then(@NonNull Task<TResult> task) throws Exception;
}
