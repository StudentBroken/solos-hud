package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

/* JADX INFO: loaded from: classes6.dex */
final class zzap extends zzn<Status> {
    private /* synthetic */ zzak zzbSm;
    private /* synthetic */ boolean zzbSn;
    private /* synthetic */ Uri zzbzV;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzap(zzak zzakVar, GoogleApiClient googleApiClient, Uri uri, boolean z) {
        super(googleApiClient);
        this.zzbSm = zzakVar;
        this.zzbzV = uri;
        this.zzbSn = z;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzfw) zzbVar).zza(this, this.zzbSm.zzakx, this.zzbzV, this.zzbSn);
    }

    @Override // com.google.android.gms.internal.zzbcq
    public final /* synthetic */ Result zzb(Status status) {
        return status;
    }
}
