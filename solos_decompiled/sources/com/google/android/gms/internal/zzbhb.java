package com.google.android.gms.internal;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/* JADX INFO: loaded from: classes3.dex */
final class zzbhb extends Api.zza<zzbhi, Api.ApiOptions.NoOptions> {
    zzbhb() {
    }

    @Override // com.google.android.gms.common.api.Api.zza
    public final /* synthetic */ Api.zze zza(Context context, Looper looper, com.google.android.gms.common.internal.zzq zzqVar, Api.ApiOptions.NoOptions noOptions, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        return new zzbhi(context, looper, zzqVar, connectionCallbacks, onConnectionFailedListener);
    }
}
