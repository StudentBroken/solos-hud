package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzbo extends zzn<DataApi.GetFdForAssetResult> {
    private /* synthetic */ Asset zzbSA;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbo(zzbi zzbiVar, GoogleApiClient googleApiClient, Asset asset) {
        super(googleApiClient);
        this.zzbSA = asset;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzfw) zzbVar).zza(this, this.zzbSA);
    }

    @Override // com.google.android.gms.internal.zzbcq
    protected final /* synthetic */ Result zzb(Status status) {
        return new zzbu(status, null);
    }
}
