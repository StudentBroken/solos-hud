package com.google.android.gms.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbr;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbfm {
    private final Set<zzbfi<?>> zzauD = Collections.newSetFromMap(new WeakHashMap());

    public static <L> zzbfk<L> zza(@NonNull L l, @NonNull String str) {
        zzbr.zzb(l, "Listener must not be null");
        zzbr.zzb(str, "Listener type must not be null");
        zzbr.zzh(str, "Listener type must not be empty");
        return new zzbfk<>(l, str);
    }

    public static <L> zzbfi<L> zzb(@NonNull L l, @NonNull Looper looper, @NonNull String str) {
        zzbr.zzb(l, "Listener must not be null");
        zzbr.zzb(looper, "Looper must not be null");
        zzbr.zzb(str, "Listener type must not be null");
        return new zzbfi<>(looper, l, str);
    }

    public final void release() {
        Iterator<zzbfi<?>> it = this.zzauD.iterator();
        while (it.hasNext()) {
            it.next().clear();
        }
        this.zzauD.clear();
    }

    public final <L> zzbfi<L> zza(@NonNull L l, @NonNull Looper looper, @NonNull String str) {
        zzbfi<L> zzbfiVarZzb = zzb(l, looper, str);
        this.zzauD.add(zzbfiVarZzb);
        return zzbfiVarZzb;
    }
}
