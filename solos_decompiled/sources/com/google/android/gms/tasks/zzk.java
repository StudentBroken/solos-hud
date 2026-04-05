package com.google.android.gms.tasks;

import android.support.annotation.NonNull;

/* JADX INFO: loaded from: classes56.dex */
interface zzk<TResult> {
    void cancel();

    void onComplete(@NonNull Task<TResult> task);
}
