package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzbn extends zzn<DataApi.DeleteDataItemsResult> {
    private /* synthetic */ int zzbSz;
    private /* synthetic */ Uri zzbzV;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbn(zzbi zzbiVar, GoogleApiClient googleApiClient, Uri uri, int i) {
        super(googleApiClient);
        this.zzbzV = uri;
        this.zzbSz = i;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzdn) ((zzfw) zzbVar).zzrd()).zzb(new zzff(this), this.zzbzV, this.zzbSz);
    }

    @Override // com.google.android.gms.internal.zzbcq
    protected final /* synthetic */ Result zzb(Status status) {
        return new zzbt(status, 0);
    }
}
