package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzbk extends zzn<DataApi.DataItemResult> {
    private /* synthetic */ Uri zzbzV;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbk(zzbi zzbiVar, GoogleApiClient googleApiClient, Uri uri) {
        super(googleApiClient);
        this.zzbzV = uri;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzdn) ((zzfw) zzbVar).zzrd()).zza(new zzfl(this), this.zzbzV);
    }

    @Override // com.google.android.gms.internal.zzbcq
    protected final /* synthetic */ Result zzb(Status status) {
        return new zzbs(status, null);
    }
}
