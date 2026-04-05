package com.google.android.gms.common.api;

import android.os.Looper;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.internal.zzbce;
import com.google.android.gms.internal.zzbfy;

/* JADX INFO: loaded from: classes3.dex */
public final class zzd {
    private zzbfy zzaAO;
    private Looper zzrP;

    public final zzd zza(Looper looper) {
        zzbr.zzb(looper, "Looper must not be null.");
        this.zzrP = looper;
        return this;
    }

    public final zzd zza(zzbfy zzbfyVar) {
        zzbr.zzb(zzbfyVar, "StatusExceptionMapper must not be null.");
        this.zzaAO = zzbfyVar;
        return this;
    }

    public final GoogleApi.zza zzph() {
        if (this.zzaAO == null) {
            this.zzaAO = new zzbce();
        }
        if (this.zzrP == null) {
            if (Looper.myLooper() != null) {
                this.zzrP = Looper.myLooper();
            } else {
                this.zzrP = Looper.getMainLooper();
            }
        }
        return new GoogleApi.zza(this.zzaAO, this.zzrP);
    }
}
