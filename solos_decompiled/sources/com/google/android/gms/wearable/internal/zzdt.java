package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.MessageApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzdt extends zzn<MessageApi.SendMessageResult> {
    private /* synthetic */ byte[] zzbKS;
    private /* synthetic */ String zzbSV;
    private /* synthetic */ String zzbSg;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzdt(zzds zzdsVar, GoogleApiClient googleApiClient, String str, String str2, byte[] bArr) {
        super(googleApiClient);
        this.zzbSg = str;
        this.zzbSV = str2;
        this.zzbKS = bArr;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzdn) ((zzfw) zzbVar).zzrd()).zza(new zzfu(this), this.zzbSg, this.zzbSV, this.zzbKS);
    }

    @Override // com.google.android.gms.internal.zzbcq
    protected final /* synthetic */ Result zzb(Status status) {
        return new zzdw(status, -1);
    }
}
