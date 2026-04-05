package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Channel;

/* JADX INFO: loaded from: classes6.dex */
final class zzan extends zzn<Channel.GetInputStreamResult> {
    private /* synthetic */ zzak zzbSm;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzan(zzak zzakVar, GoogleApiClient googleApiClient) {
        super(googleApiClient);
        this.zzbSm = zzakVar;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        String str = this.zzbSm.zzakx;
        zzbd zzbdVar = new zzbd();
        ((zzdn) ((zzfw) zzbVar).zzrd()).zza(new zzfi(this, zzbdVar), zzbdVar, str);
    }

    @Override // com.google.android.gms.internal.zzbcq
    public final /* synthetic */ Result zzb(Status status) {
        return new zzas(status, null);
    }
}
