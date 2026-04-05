package com.google.android.gms.common.internal;

import android.support.annotation.Nullable;

/* JADX INFO: loaded from: classes67.dex */
public final class zzbh {
    public static boolean equal(@Nullable Object obj, @Nullable Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static zzbj zzt(Object obj) {
        return new zzbj(obj);
    }
}
