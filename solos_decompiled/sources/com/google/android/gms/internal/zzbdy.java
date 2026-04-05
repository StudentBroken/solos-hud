package com.google.android.gms.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/* JADX INFO: loaded from: classes3.dex */
final class zzbdy implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private /* synthetic */ zzbdp zzaDr;

    private zzbdy(zzbdp zzbdpVar) {
        this.zzaDr = zzbdpVar;
    }

    /* synthetic */ zzbdy(zzbdp zzbdpVar, zzbdq zzbdqVar) {
        this(zzbdpVar);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnected(Bundle bundle) {
        this.zzaDr.zzaDj.zza(new zzbdw(this.zzaDr));
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.zzaDr.zzaCx.lock();
        try {
            if (this.zzaDr.zzd(connectionResult)) {
                this.zzaDr.zzpX();
                this.zzaDr.zzpV();
            } else {
                this.zzaDr.zze(connectionResult);
            }
        } finally {
            this.zzaDr.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
    }
}
