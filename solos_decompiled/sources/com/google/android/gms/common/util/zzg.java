package com.google.android.gms.common.util;

import android.support.v4.util.ArrayMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes67.dex */
public final class zzg {
    public static <K, V> Map<K, V> zza(K k, V v, K k2, V v2) {
        Map mapZzg = zzg(2, false);
        mapZzg.put(k, v);
        mapZzg.put(k2, v2);
        return Collections.unmodifiableMap(mapZzg);
    }

    public static <K, V> Map<K, V> zza(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map mapZzg = zzg(6, false);
        mapZzg.put(k, v);
        mapZzg.put(k2, v2);
        mapZzg.put(k3, v3);
        mapZzg.put(k4, v4);
        mapZzg.put(k5, v5);
        mapZzg.put(k6, v6);
        return Collections.unmodifiableMap(mapZzg);
    }

    public static <T> Set<T> zza(T t, T t2, T t3) {
        Set setZzf = zzf(3, false);
        setZzf.add(t);
        setZzf.add(t2);
        setZzf.add(t3);
        return Collections.unmodifiableSet(setZzf);
    }

    public static <T> Set<T> zzb(T... tArr) {
        switch (tArr.length) {
            case 0:
                return Collections.emptySet();
            case 1:
                return Collections.singleton(tArr[0]);
            case 2:
                T t = tArr[0];
                T t2 = tArr[1];
                Set setZzf = zzf(2, false);
                setZzf.add(t);
                setZzf.add(t2);
                return Collections.unmodifiableSet(setZzf);
            case 3:
                return zza(tArr[0], tArr[1], tArr[2]);
            case 4:
                T t3 = tArr[0];
                T t4 = tArr[1];
                T t5 = tArr[2];
                T t6 = tArr[3];
                Set setZzf2 = zzf(4, false);
                setZzf2.add(t3);
                setZzf2.add(t4);
                setZzf2.add(t5);
                setZzf2.add(t6);
                return Collections.unmodifiableSet(setZzf2);
            default:
                Set setZzf3 = zzf(tArr.length, false);
                Collections.addAll(setZzf3, tArr);
                return Collections.unmodifiableSet(setZzf3);
        }
    }

    private static <T> Set<T> zzf(int i, boolean z) {
        return i <= 256 ? new zzb(i) : new HashSet(i, 1.0f);
    }

    private static <K, V> Map<K, V> zzg(int i, boolean z) {
        return i <= 256 ? new ArrayMap(i) : new HashMap(i, 1.0f);
    }
}
