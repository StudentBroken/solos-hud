package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;
import java.lang.ref.WeakReference;
import java.util.Map;

/* JADX INFO: loaded from: classes6.dex */
final class zzdr<T> extends zzfc<Status> {
    private WeakReference<Map<T, zzga<T>>> zzbST;
    private WeakReference<T> zzbSU;

    zzdr(Map<T, zzga<T>> map, T t, zzbcl<Status> zzbclVar) {
        super(zzbclVar);
        this.zzbST = new WeakReference<>(map);
        this.zzbSU = new WeakReference<>(t);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(Status status) {
        Map<T, zzga<T>> map = this.zzbST.get();
        T t = this.zzbSU.get();
        if (status.getStatus().getStatusCode() == 4002 && map != null && t != null) {
            synchronized (map) {
                zzga<T> zzgaVarRemove = map.remove(t);
                if (zzgaVarRemove != null) {
                    zzgaVarRemove.clear();
                }
            }
        }
        zzR(status);
    }
}
