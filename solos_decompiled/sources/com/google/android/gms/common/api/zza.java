package com.google.android.gms.common.api;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.internal.zzbcf;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes3.dex */
public final class zza extends Exception {
    private final ArrayMap<zzbcf<?>, ConnectionResult> zzaAD;

    public zza(ArrayMap<zzbcf<?>, ConnectionResult> arrayMap) {
        this.zzaAD = arrayMap;
    }

    @Override // java.lang.Throwable
    public final String getMessage() {
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        for (zzbcf<?> zzbcfVar : this.zzaAD.keySet()) {
            ConnectionResult connectionResult = this.zzaAD.get(zzbcfVar);
            if (connectionResult.isSuccess()) {
                z = false;
            }
            String strValueOf = String.valueOf(zzbcfVar.zzpp());
            String strValueOf2 = String.valueOf(connectionResult);
            arrayList.add(new StringBuilder(String.valueOf(strValueOf).length() + 2 + String.valueOf(strValueOf2).length()).append(strValueOf).append(": ").append(strValueOf2).toString());
        }
        StringBuilder sb = new StringBuilder();
        if (z) {
            sb.append("None of the queried APIs are available. ");
        } else {
            sb.append("Some of the queried APIs are unavailable. ");
        }
        sb.append(TextUtils.join("; ", arrayList));
        return sb.toString();
    }

    public final ConnectionResult zza(GoogleApi<? extends Api.ApiOptions> googleApi) {
        Object objZzpf = googleApi.zzpf();
        zzbr.zzb(this.zzaAD.get(objZzpf) != null, "The given API was not part of the availability request.");
        return this.zzaAD.get(objZzpf);
    }

    public final ArrayMap<zzbcf<?>, ConnectionResult> zzpd() {
        return this.zzaAD;
    }
}
