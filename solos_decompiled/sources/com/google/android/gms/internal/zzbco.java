package com.google.android.gms.internal;

import android.support.annotation.MainThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;

/* JADX INFO: loaded from: classes3.dex */
final class zzbco implements Runnable {
    private final zzbcn zzaBT;
    final /* synthetic */ zzbcm zzaBU;

    zzbco(zzbcm zzbcmVar, zzbcn zzbcnVar) {
        this.zzaBU = zzbcmVar;
        this.zzaBT = zzbcnVar;
    }

    @Override // java.lang.Runnable
    @MainThread
    public final void run() {
        if (this.zzaBU.mStarted) {
            ConnectionResult connectionResultZzpx = this.zzaBT.zzpx();
            if (connectionResultZzpx.hasResolution()) {
                this.zzaBU.zzaEI.startActivityForResult(GoogleApiActivity.zza(this.zzaBU.getActivity(), connectionResultZzpx.getResolution(), this.zzaBT.zzpw(), false), 1);
                return;
            }
            if (this.zzaBU.zzaBf.isUserResolvableError(connectionResultZzpx.getErrorCode())) {
                this.zzaBU.zzaBf.zza(this.zzaBU.getActivity(), this.zzaBU.zzaEI, connectionResultZzpx.getErrorCode(), 2, this.zzaBU);
            } else if (connectionResultZzpx.getErrorCode() != 18) {
                this.zzaBU.zza(connectionResultZzpx, this.zzaBT.zzpw());
            } else {
                GoogleApiAvailability.zza(this.zzaBU.getActivity().getApplicationContext(), new zzbcp(this, GoogleApiAvailability.zza(this.zzaBU.getActivity(), this.zzaBU)));
            }
        }
    }
}
