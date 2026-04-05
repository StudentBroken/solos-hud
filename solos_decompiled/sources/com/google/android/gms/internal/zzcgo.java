package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgo<V> {
    private final String zzBQ;
    private final V zzahX;
    private final zzbgl<V> zzahY;

    private zzcgo(String str, zzbgl<V> zzbglVar, V v) {
        zzbr.zzu(zzbglVar);
        this.zzahY = zzbglVar;
        this.zzahX = v;
        this.zzBQ = str;
    }

    static zzcgo<Long> zzb(String str, long j, long j2) {
        return new zzcgo<>(str, zzbgl.zza(str, Long.valueOf(j2)), Long.valueOf(j));
    }

    static zzcgo<Boolean> zzb(String str, boolean z, boolean z2) {
        return new zzcgo<>(str, zzbgl.zzg(str, z2), Boolean.valueOf(z));
    }

    static zzcgo<String> zzj(String str, String str2, String str3) {
        return new zzcgo<>(str, zzbgl.zzv(str, str3), str2);
    }

    static zzcgo<Integer> zzm(String str, int i, int i2) {
        return new zzcgo<>(str, zzbgl.zza(str, Integer.valueOf(i2)), Integer.valueOf(i));
    }

    public final V get() {
        return this.zzahX;
    }

    public final V get(V v) {
        return v != null ? v : this.zzahX;
    }

    public final String getKey() {
        return this.zzBQ;
    }
}
