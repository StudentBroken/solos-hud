package com.google.android.gms.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbcc<TResult> extends zzbby {
    private final zzbgc<Api.zzb, TResult> zzaBy;
    private final zzbfy zzaBz;
    private final TaskCompletionSource<TResult> zzalG;

    public zzbcc(int i, zzbgc<Api.zzb, TResult> zzbgcVar, TaskCompletionSource<TResult> taskCompletionSource, zzbfy zzbfyVar) {
        super(i);
        this.zzalG = taskCompletionSource;
        this.zzaBy = zzbgcVar;
        this.zzaBz = zzbfyVar;
    }

    @Override // com.google.android.gms.internal.zzbby
    public final void zza(@NonNull zzbdf zzbdfVar, boolean z) {
        zzbdfVar.zza(this.zzalG, z);
    }

    @Override // com.google.android.gms.internal.zzbby
    public final void zza(zzbep<?> zzbepVar) throws DeadObjectException {
        try {
            this.zzaBy.zza(zzbepVar.zzpH(), this.zzalG);
        } catch (DeadObjectException e) {
            throw e;
        } catch (RemoteException e2) {
            zzp(zzbby.zza(e2));
        }
    }

    @Override // com.google.android.gms.internal.zzbby
    public final void zzp(@NonNull Status status) {
        this.zzalG.trySetException(this.zzaBz.zzq(status));
    }
}
