package com.google.android.gms.internal;

import android.os.StrictMode;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcbs {
    public static <T> T zzb(Callable<T> callable) throws Exception {
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        try {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
            return callable.call();
        } finally {
            StrictMode.setThreadPolicy(threadPolicy);
        }
    }
}
