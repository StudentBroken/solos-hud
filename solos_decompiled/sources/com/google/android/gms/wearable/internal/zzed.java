package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.NodeApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzed extends zzn<Status> {
    private /* synthetic */ NodeApi.NodeListener zzbSZ;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzed(zzdz zzdzVar, GoogleApiClient googleApiClient, NodeApi.NodeListener nodeListener) {
        super(googleApiClient);
        this.zzbSZ = nodeListener;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzfw) zzbVar).zza(this, this.zzbSZ);
    }

    @Override // com.google.android.gms.internal.zzbcq
    public final /* synthetic */ Result zzb(Status status) {
        return status;
    }
}
