package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

/* JADX INFO: loaded from: classes6.dex */
final class zzam extends zzn<Status> {
    private /* synthetic */ int zzKf;
    private /* synthetic */ zzak zzbSm;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzam(zzak zzakVar, GoogleApiClient googleApiClient, int i) {
        super(googleApiClient);
        this.zzbSm = zzakVar;
        this.zzKf = i;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzdn) ((zzfw) zzbVar).zzrd()).zzb(new zzfe(this), this.zzbSm.zzakx, this.zzKf);
    }

    @Override // com.google.android.gms.internal.zzbcq
    protected final /* synthetic */ Result zzb(Status status) {
        return status;
    }
}
