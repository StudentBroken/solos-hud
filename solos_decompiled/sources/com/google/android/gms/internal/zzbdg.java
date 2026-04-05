package com.google.android.gms.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

/* JADX INFO: loaded from: classes3.dex */
final class zzbdg implements PendingResult.zza {
    private /* synthetic */ zzbcq zzaCV;
    private /* synthetic */ zzbdf zzaCW;

    zzbdg(zzbdf zzbdfVar, zzbcq zzbcqVar) {
        this.zzaCW = zzbdfVar;
        this.zzaCV = zzbcqVar;
    }

    @Override // com.google.android.gms.common.api.PendingResult.zza
    public final void zzo(Status status) {
        this.zzaCW.zzaCT.remove(this.zzaCV);
    }
}
