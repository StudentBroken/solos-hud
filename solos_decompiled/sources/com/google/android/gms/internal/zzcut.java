package com.google.android.gms.internal;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/* JADX INFO: loaded from: classes3.dex */
final class zzcut extends Api.zza<zzcvg, zzcux> {
    zzcut() {
    }

    @Override // com.google.android.gms.common.api.Api.zza
    public final /* synthetic */ Api.zze zza(Context context, Looper looper, com.google.android.gms.common.internal.zzq zzqVar, zzcux zzcuxVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzcux zzcuxVar2 = zzcuxVar;
        return new zzcvg(context, looper, true, zzqVar, zzcuxVar2 == null ? zzcux.zzbCQ : zzcuxVar2, connectionCallbacks, onConnectionFailedListener);
    }
}
