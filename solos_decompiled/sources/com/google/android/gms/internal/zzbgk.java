package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.tasks.TaskCompletionSource;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzbgk<A extends Api.zzb, L> {
    private final zzbfk<L> zzaEP;

    protected zzbgk(zzbfk<L> zzbfkVar) {
        this.zzaEP = zzbfkVar;
    }

    protected abstract void zzc(A a, TaskCompletionSource<Void> taskCompletionSource) throws RemoteException;

    public final zzbfk<L> zzqE() {
        return this.zzaEP;
    }
}
