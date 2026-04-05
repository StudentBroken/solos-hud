package com.google.android.gms.internal;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbcd extends zzbbz {
    private zzbfk<?> zzaBA;

    public zzbcd(zzbfk<?> zzbfkVar, TaskCompletionSource<Void> taskCompletionSource) {
        super(4, taskCompletionSource);
        this.zzaBA = zzbfkVar;
    }

    @Override // com.google.android.gms.internal.zzbbz, com.google.android.gms.internal.zzbby
    public final /* bridge */ /* synthetic */ void zza(@NonNull zzbdf zzbdfVar, boolean z) {
    }

    @Override // com.google.android.gms.internal.zzbbz
    public final void zzb(zzbep<?> zzbepVar) throws RemoteException {
        zzbfr zzbfrVarRemove = zzbepVar.zzqq().remove(this.zzaBA);
        if (zzbfrVarRemove != null) {
            zzbfrVarRemove.zzaBx.zzc(zzbepVar.zzpH(), this.zzalG);
            zzbfrVarRemove.zzaBw.zzqF();
        } else {
            Log.wtf("UnregisterListenerTask", "Received call to unregister a listener without a matching registration call.", new Exception());
            this.zzalG.trySetException(new ApiException(Status.zzaBq));
        }
    }

    @Override // com.google.android.gms.internal.zzbbz, com.google.android.gms.internal.zzbby
    public final /* bridge */ /* synthetic */ void zzp(@NonNull Status status) {
        super.zzp(status);
    }
}
