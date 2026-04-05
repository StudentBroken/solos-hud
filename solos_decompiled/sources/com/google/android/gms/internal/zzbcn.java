package com.google.android.gms.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes3.dex */
final class zzbcn {
    private final int zzaBR;
    private final ConnectionResult zzaBS;

    zzbcn(ConnectionResult connectionResult, int i) {
        zzbr.zzu(connectionResult);
        this.zzaBS = connectionResult;
        this.zzaBR = i;
    }

    final int zzpw() {
        return this.zzaBR;
    }

    final ConnectionResult zzpx() {
        return this.zzaBS;
    }
}
