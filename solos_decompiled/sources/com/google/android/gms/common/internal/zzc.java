package com.google.android.gms.common.internal;

import android.os.Looper;
import android.util.Log;

/* JADX INFO: loaded from: classes67.dex */
public final class zzc {
    public static void zzae(boolean z) {
        if (!z) {
            throw new IllegalStateException();
        }
    }

    public static void zzcz(String str) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            String strValueOf = String.valueOf(Thread.currentThread());
            String strValueOf2 = String.valueOf(Looper.getMainLooper().getThread());
            Log.e("Asserts", new StringBuilder(String.valueOf(strValueOf).length() + 57 + String.valueOf(strValueOf2).length()).append("checkMainThread: current thread ").append(strValueOf).append(" IS NOT the main thread ").append(strValueOf2).append("!").toString());
            throw new IllegalStateException(str);
        }
    }

    public static void zzr(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("null reference");
        }
    }
}
