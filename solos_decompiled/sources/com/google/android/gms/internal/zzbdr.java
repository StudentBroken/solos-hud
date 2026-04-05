package com.google.android.gms.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.zzbr;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes3.dex */
final class zzbdr implements com.google.android.gms.common.internal.zzj {
    private final boolean zzaCl;
    private final WeakReference<zzbdp> zzaDs;
    private final Api<?> zzayY;

    public zzbdr(zzbdp zzbdpVar, Api<?> api, boolean z) {
        this.zzaDs = new WeakReference<>(zzbdpVar);
        this.zzayY = api;
        this.zzaCl = z;
    }

    @Override // com.google.android.gms.common.internal.zzj
    public final void zzf(@NonNull ConnectionResult connectionResult) {
        zzbdp zzbdpVar = this.zzaDs.get();
        if (zzbdpVar == null) {
            return;
        }
        zzbr.zza(Looper.myLooper() == zzbdpVar.zzaDb.zzaCn.getLooper(), "onReportServiceBinding must be called on the GoogleApiClient handler thread");
        zzbdpVar.zzaCx.lock();
        try {
            if (zzbdpVar.zzan(0)) {
                if (!connectionResult.isSuccess()) {
                    zzbdpVar.zzb(connectionResult, this.zzayY, this.zzaCl);
                }
                if (zzbdpVar.zzpU()) {
                    zzbdpVar.zzpV();
                }
            }
        } finally {
            zzbdpVar.zzaCx.unlock();
        }
    }
}
