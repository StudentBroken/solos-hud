package com.google.android.gms.common.stats;

import com.google.android.gms.common.internal.ReflectedParcelable;

/* JADX INFO: loaded from: classes67.dex */
public abstract class StatsEvent extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable {
    public abstract int getEventType();

    public abstract long getTimeMillis();

    public String toString() {
        long timeMillis = getTimeMillis();
        String strValueOf = String.valueOf("\t");
        int eventType = getEventType();
        String strValueOf2 = String.valueOf("\t");
        long jZzrU = zzrU();
        String strValueOf3 = String.valueOf(zzrV());
        return new StringBuilder(String.valueOf(strValueOf).length() + 51 + String.valueOf(strValueOf2).length() + String.valueOf(strValueOf3).length()).append(timeMillis).append(strValueOf).append(eventType).append(strValueOf2).append(jZzrU).append(strValueOf3).toString();
    }

    public abstract long zzrU();

    public abstract String zzrV();
}
