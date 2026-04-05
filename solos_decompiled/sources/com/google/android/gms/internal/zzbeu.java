package com.google.android.gms.internal;

import com.google.android.gms.common.ConnectionResult;
import java.util.Collections;

/* JADX INFO: loaded from: classes3.dex */
final class zzbeu implements Runnable {
    private /* synthetic */ zzbet zzaEA;
    private /* synthetic */ ConnectionResult zzaEy;

    zzbeu(zzbet zzbetVar, ConnectionResult connectionResult) {
        this.zzaEA = zzbetVar;
        this.zzaEy = connectionResult;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (!this.zzaEy.isSuccess()) {
            ((zzbep) this.zzaEA.zzaEo.zzaCD.get(this.zzaEA.zzaAM)).onConnectionFailed(this.zzaEy);
            return;
        }
        zzbet.zza(this.zzaEA, true);
        if (this.zzaEA.zzaCA.zzmt()) {
            this.zzaEA.zzqx();
        } else {
            this.zzaEA.zzaCA.zza(null, Collections.emptySet());
        }
    }
}
