package com.google.android.gms.internal;

import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes3.dex */
final class zzbes implements Runnable {
    private /* synthetic */ zzbep zzaEx;
    private /* synthetic */ ConnectionResult zzaEy;

    zzbes(zzbep zzbepVar, ConnectionResult connectionResult) {
        this.zzaEx = zzbepVar;
        this.zzaEy = connectionResult;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzaEx.onConnectionFailed(this.zzaEy);
    }
}
