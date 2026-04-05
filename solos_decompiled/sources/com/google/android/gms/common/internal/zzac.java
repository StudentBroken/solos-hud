package com.google.android.gms.common.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/* JADX INFO: loaded from: classes3.dex */
final class zzac implements zzg {
    private /* synthetic */ GoogleApiClient.OnConnectionFailedListener zzaHF;

    zzac(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zzaHF = onConnectionFailedListener;
    }

    @Override // com.google.android.gms.common.internal.zzg
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.zzaHF.onConnectionFailed(connectionResult);
    }
}
