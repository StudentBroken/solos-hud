package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes3.dex */
final class zzbed implements GoogleApiClient.ConnectionCallbacks {
    private /* synthetic */ zzbeb zzaDP;
    private /* synthetic */ AtomicReference zzaDQ;
    private /* synthetic */ zzbfz zzaDR;

    zzbed(zzbeb zzbebVar, AtomicReference atomicReference, zzbfz zzbfzVar) {
        this.zzaDP = zzbebVar;
        this.zzaDQ = atomicReference;
        this.zzaDR = zzbfzVar;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnected(Bundle bundle) {
        this.zzaDP.zza((GoogleApiClient) this.zzaDQ.get(), this.zzaDR, true);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
    }
}
