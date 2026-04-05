package com.google.android.gms.internal;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbdf {
    private final Map<zzbcq<?>, Boolean> zzaCT = Collections.synchronizedMap(new WeakHashMap());
    private final Map<TaskCompletionSource<?>, Boolean> zzaCU = Collections.synchronizedMap(new WeakHashMap());

    private final void zza(boolean z, Status status) {
        HashMap map;
        HashMap map2;
        synchronized (this.zzaCT) {
            map = new HashMap(this.zzaCT);
        }
        synchronized (this.zzaCU) {
            map2 = new HashMap(this.zzaCU);
        }
        for (Map.Entry entry : map.entrySet()) {
            if (z || ((Boolean) entry.getValue()).booleanValue()) {
                ((zzbcq) entry.getKey()).zzs(status);
            }
        }
        for (Map.Entry entry2 : map2.entrySet()) {
            if (z || ((Boolean) entry2.getValue()).booleanValue()) {
                ((TaskCompletionSource) entry2.getKey()).trySetException(new ApiException(status));
            }
        }
    }

    final void zza(zzbcq<? extends Result> zzbcqVar, boolean z) {
        this.zzaCT.put(zzbcqVar, Boolean.valueOf(z));
        zzbcqVar.zza(new zzbdg(this, zzbcqVar));
    }

    final <TResult> void zza(TaskCompletionSource<TResult> taskCompletionSource, boolean z) {
        this.zzaCU.put(taskCompletionSource, Boolean.valueOf(z));
        taskCompletionSource.getTask().addOnCompleteListener(new zzbdh(this, taskCompletionSource));
    }

    final boolean zzpM() {
        return (this.zzaCT.isEmpty() && this.zzaCU.isEmpty()) ? false : true;
    }

    public final void zzpN() {
        zza(false, zzben.zzaEe);
    }

    public final void zzpO() {
        zza(true, zzbgh.zzaFl);
    }
}
