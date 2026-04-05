package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import java.util.Collections;
import java.util.Iterator;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbea implements zzbei {
    private final zzbej zzaDb;

    public zzbea(zzbej zzbejVar) {
        this.zzaDb = zzbejVar;
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void begin() {
        Iterator<Api.zze> it = this.zzaDb.zzaDH.values().iterator();
        while (it.hasNext()) {
            it.next().disconnect();
        }
        this.zzaDb.zzaCn.zzaDI = Collections.emptySet();
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void connect() {
        this.zzaDb.zzqf();
    }

    @Override // com.google.android.gms.internal.zzbei
    public final boolean disconnect() {
        return true;
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void onConnected(Bundle bundle) {
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void onConnectionSuspended(int i) {
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
    }

    @Override // com.google.android.gms.internal.zzbei
    public final <A extends Api.zzb, R extends Result, T extends zzbck<R, A>> T zzd(T t) {
        this.zzaDb.zzaCn.zzaCL.add(t);
        return t;
    }

    @Override // com.google.android.gms.internal.zzbei
    public final <A extends Api.zzb, T extends zzbck<? extends Result, A>> T zze(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
}
