package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.MessageApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzdu extends zzn<Status> {
    private /* synthetic */ MessageApi.MessageListener zzbSW;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzdu(zzds zzdsVar, GoogleApiClient googleApiClient, MessageApi.MessageListener messageListener) {
        super(googleApiClient);
        this.zzbSW = messageListener;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzfw) zzbVar).zza(this, this.zzbSW);
    }

    @Override // com.google.android.gms.internal.zzbcq
    public final /* synthetic */ Result zzb(Status status) {
        return status;
    }
}
