package com.google.android.gms.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

/* JADX INFO: loaded from: classes3.dex */
final class zzbee implements GoogleApiClient.OnConnectionFailedListener {
    private /* synthetic */ zzbfz zzaDR;

    zzbee(zzbeb zzbebVar, zzbfz zzbfzVar) {
        this.zzaDR = zzbfzVar;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.zzaDR.setResult(new Status(8));
    }
}
