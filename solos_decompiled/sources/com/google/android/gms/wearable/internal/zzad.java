package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.ChannelApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzad extends zzn<ChannelApi.OpenChannelResult> {
    private /* synthetic */ String zzKV;
    private /* synthetic */ String zzbSg;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzad(zzac zzacVar, GoogleApiClient googleApiClient, String str, String str2) {
        super(googleApiClient);
        this.zzbSg = str;
        this.zzKV = str2;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzdn) ((zzfw) zzbVar).zzrd()).zza(new zzfq(this), this.zzbSg, this.zzKV);
    }

    @Override // com.google.android.gms.internal.zzbcq
    public final /* synthetic */ Result zzb(Status status) {
        return new zzaf(status, null);
    }
}
