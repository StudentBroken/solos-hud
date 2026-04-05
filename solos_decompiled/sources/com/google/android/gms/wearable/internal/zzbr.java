package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzbr extends zzn<Status> {
    private /* synthetic */ DataApi.DataListener zzbSC;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzbr(zzbi zzbiVar, GoogleApiClient googleApiClient, DataApi.DataListener dataListener) {
        super(googleApiClient);
        this.zzbSC = dataListener;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzfw) zzbVar).zza(this, this.zzbSC);
    }

    @Override // com.google.android.gms.internal.zzbcq
    public final /* synthetic */ Result zzb(Status status) {
        return status;
    }
}
