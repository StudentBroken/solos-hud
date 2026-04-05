package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.tasks.TaskCompletionSource;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzbfq<A extends Api.zzb, L> {
    private final zzbfi<L> zzaEW;

    protected zzbfq(zzbfi<L> zzbfiVar) {
        this.zzaEW = zzbfiVar;
    }

    protected abstract void zzb(A a, TaskCompletionSource<Void> taskCompletionSource) throws RemoteException;

    public final zzbfk<L> zzqE() {
        return this.zzaEW.zzqE();
    }

    public final void zzqF() {
        this.zzaEW.clear();
    }
}
