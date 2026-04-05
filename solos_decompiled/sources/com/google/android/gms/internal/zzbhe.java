package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/* JADX INFO: loaded from: classes3.dex */
final class zzbhe extends zzbhh {
    zzbhe(zzbhd zzbhdVar, GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzbhl) ((zzbhi) zzbVar).zzrd()).zza(new zzbhf(this));
    }
}
