package com.google.android.gms.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbgh {
    public static final Status zzaFl = new Status(8, "The connection to Google Play services was lost");
    private static final zzbcq<?>[] zzaFm = new zzbcq[0];
    private final Map<Api.zzc<?>, Api.zze> zzaDH;
    final Set<zzbcq<?>> zzaFn = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap()));
    private final zzbgj zzaFo = new zzbgi(this);

    public zzbgh(Map<Api.zzc<?>, Api.zze> map) {
        this.zzaDH = map;
    }

    public final void release() {
        for (zzbcq zzbcqVar : (zzbcq[]) this.zzaFn.toArray(zzaFm)) {
            zzbcqVar.zza((zzbgj) null);
            zzbcqVar.zzpm();
            if (zzbcqVar.zzpz()) {
                this.zzaFn.remove(zzbcqVar);
            }
        }
    }

    final void zzb(zzbcq<? extends Result> zzbcqVar) {
        this.zzaFn.add(zzbcqVar);
        zzbcqVar.zza(this.zzaFo);
    }

    public final void zzqK() {
        for (zzbcq zzbcqVar : (zzbcq[]) this.zzaFn.toArray(zzaFm)) {
            zzbcqVar.zzs(zzaFl);
        }
    }
}
