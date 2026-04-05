package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.CapabilityApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzp extends zzn<CapabilityApi.GetCapabilityResult> {
    private /* synthetic */ String zzbRX;
    private /* synthetic */ int zzbRY;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzp(zzo zzoVar, GoogleApiClient googleApiClient, String str, int i) {
        super(googleApiClient);
        this.zzbRX = str;
        this.zzbRY = i;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzdn) ((zzfw) zzbVar).zzrd()).zza(new zzfh(this), this.zzbRX, this.zzbRY);
    }

    @Override // com.google.android.gms.internal.zzbcq
    protected final /* synthetic */ Result zzb(Status status) {
        return new zzy(status, null);
    }
}
