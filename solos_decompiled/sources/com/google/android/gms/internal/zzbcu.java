package com.google.android.gms.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbcu implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final boolean zzaCl;
    private zzbcv zzaCm;
    public final Api<?> zzayY;

    public zzbcu(Api<?> api, boolean z) {
        this.zzayY = api;
        this.zzaCl = z;
    }

    private final void zzpB() {
        zzbr.zzb(this.zzaCm, "Callbacks must be attached to a ClientConnectionHelper instance before connecting the client.");
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnected(@Nullable Bundle bundle) {
        zzpB();
        this.zzaCm.onConnected(bundle);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        zzpB();
        this.zzaCm.zza(connectionResult, this.zzayY, this.zzaCl);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
        zzpB();
        this.zzaCm.onConnectionSuspended(i);
    }

    public final void zza(zzbcv zzbcvVar) {
        this.zzaCm = zzbcvVar;
    }
}
