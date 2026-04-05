package com.google.android.gms.common.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes67.dex */
public final class zzm implements zzj {
    private /* synthetic */ zzd zzaHg;

    public zzm(zzd zzdVar) {
        this.zzaHg = zzdVar;
    }

    @Override // com.google.android.gms.common.internal.zzj
    public final void zzf(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.isSuccess()) {
            this.zzaHg.zza((zzam) null, this.zzaHg.zzrf());
        } else if (this.zzaHg.zzaGY != null) {
            this.zzaHg.zzaGY.onConnectionFailed(connectionResult);
        }
    }
}
