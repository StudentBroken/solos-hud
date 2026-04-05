package com.google.android.gms.wearable;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.internal.zzfw;

/* JADX INFO: loaded from: classes6.dex */
final class zzj extends Api.zza<zzfw, Wearable.WearableOptions> {
    zzj() {
    }

    @Override // com.google.android.gms.common.api.Api.zza
    public final /* synthetic */ Api.zze zza(Context context, Looper looper, com.google.android.gms.common.internal.zzq zzqVar, Wearable.WearableOptions wearableOptions, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        if (wearableOptions == null) {
            new Wearable.WearableOptions(new Wearable.WearableOptions.Builder(), null);
        }
        return new zzfw(context, looper, connectionCallbacks, onConnectionFailedListener, zzqVar);
    }
}
