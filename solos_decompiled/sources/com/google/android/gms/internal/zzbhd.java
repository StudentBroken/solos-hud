package com.google.android.gms.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbhd implements zzbhc {
    @Override // com.google.android.gms.internal.zzbhc
    public final PendingResult<Status> zzd(GoogleApiClient googleApiClient) {
        return googleApiClient.zze(new zzbhe(this, googleApiClient));
    }
}
