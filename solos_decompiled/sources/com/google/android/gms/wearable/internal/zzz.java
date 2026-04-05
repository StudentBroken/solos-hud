package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.CapabilityApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzz extends zzn<Status> {
    private CapabilityApi.CapabilityListener zzbSa;

    private zzz(GoogleApiClient googleApiClient, CapabilityApi.CapabilityListener capabilityListener) {
        super(googleApiClient);
        this.zzbSa = capabilityListener;
    }

    /* synthetic */ zzz(GoogleApiClient googleApiClient, CapabilityApi.CapabilityListener capabilityListener, zzp zzpVar) {
        this(googleApiClient, capabilityListener);
    }

    @Override // com.google.android.gms.internal.zzbck
    protected final /* synthetic */ void zza(Api.zzb zzbVar) throws RemoteException {
        ((zzfw) zzbVar).zza(this, this.zzbSa);
        this.zzbSa = null;
    }

    @Override // com.google.android.gms.internal.zzbcq
    public final /* synthetic */ Result zzb(Status status) {
        this.zzbSa = null;
        return status;
    }
}
