package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.tasks.TaskCompletionSource;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzbgc<A extends Api.zzb, TResult> {
    protected abstract void zza(A a, TaskCompletionSource<TResult> taskCompletionSource) throws RemoteException;
}
