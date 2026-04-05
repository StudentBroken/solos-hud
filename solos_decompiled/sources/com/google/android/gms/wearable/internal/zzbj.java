package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataRequest;

/* JADX INFO: loaded from: classes6.dex */
final class zzbj extends zzn<DataApi.DataItemResult> {
    private /* synthetic */ PutDataRequest zzbSy;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbj(zzbi zzbiVar, GoogleApiClient googleApiClient, PutDataRequest putDataRequest) {
        super(googleApiClient);
        this.zzbSy = putDataRequest;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzfw) zzbVar).zza(this, this.zzbSy);
    }

    @Override // com.google.android.gms.internal.zzbcq
    public final /* synthetic */ Result zzb(Status status) {
        return new zzbs(status, null);
    }
}
