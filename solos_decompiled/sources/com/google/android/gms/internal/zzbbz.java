package com.google.android.gms.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;

/* JADX INFO: loaded from: classes3.dex */
abstract class zzbbz extends zzbby {
    protected final TaskCompletionSource<Void> zzalG;

    public zzbbz(int i, TaskCompletionSource<Void> taskCompletionSource) {
        super(i);
        this.zzalG = taskCompletionSource;
    }

    @Override // com.google.android.gms.internal.zzbby
    public void zza(@NonNull zzbdf zzbdfVar, boolean z) {
    }

    @Override // com.google.android.gms.internal.zzbby
    public final void zza(zzbep<?> zzbepVar) throws DeadObjectException {
        try {
            zzb(zzbepVar);
        } catch (DeadObjectException e) {
            zzp(zzbby.zza(e));
            throw e;
        } catch (RemoteException e2) {
            zzp(zzbby.zza(e2));
        }
    }

    protected abstract void zzb(zzbep<?> zzbepVar) throws RemoteException;

    @Override // com.google.android.gms.internal.zzbby
    public void zzp(@NonNull Status status) {
        this.zzalG.trySetException(new ApiException(status));
    }
}
