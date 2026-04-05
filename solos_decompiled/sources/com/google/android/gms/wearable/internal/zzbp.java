package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItemAsset;

/* JADX INFO: loaded from: classes6.dex */
final class zzbp extends zzn<DataApi.GetFdForAssetResult> {
    private /* synthetic */ DataItemAsset zzbSB;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbp(zzbi zzbiVar, GoogleApiClient googleApiClient, DataItemAsset dataItemAsset) {
        super(googleApiClient);
        this.zzbSB = dataItemAsset;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzfw) zzbVar).zza(this, Asset.createFromRef(this.zzbSB.getId()));
    }

    @Override // com.google.android.gms.internal.zzbcq
    protected final /* synthetic */ Result zzb(Status status) {
        return new zzbu(status, null);
    }
}
