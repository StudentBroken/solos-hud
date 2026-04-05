package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.CapabilityApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzq extends zzn<CapabilityApi.GetAllCapabilitiesResult> {
    private /* synthetic */ int zzbRY;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzq(zzo zzoVar, GoogleApiClient googleApiClient, int i) {
        super(googleApiClient);
        this.zzbRY = i;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzdn) ((zzfw) zzbVar).zzrd()).zza(new zzfg(this), this.zzbRY);
    }

    @Override // com.google.android.gms.internal.zzbcq
    protected final /* synthetic */ Result zzb(Status status) {
        return new zzx(status, null);
    }
}
