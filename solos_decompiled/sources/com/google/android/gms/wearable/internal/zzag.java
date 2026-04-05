package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.ChannelApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzag extends zzn<Status> {
    private final String zzakx;
    private ChannelApi.ChannelListener zzbSi;

    zzag(GoogleApiClient googleApiClient, ChannelApi.ChannelListener channelListener, String str) {
        super(googleApiClient);
        this.zzbSi = (ChannelApi.ChannelListener) com.google.android.gms.common.internal.zzbr.zzu(channelListener);
        this.zzakx = str;
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzfw) zzbVar).zza(this, this.zzbSi, this.zzakx);
        this.zzbSi = null;
    }

    @Override // com.google.android.gms.internal.zzbcq
    public final /* synthetic */ Result zzb(Status status) {
        this.zzbSi = null;
        return status;
    }
}
