package com.google.android.gms.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbfs {
    private static final ExecutorService zzaEd = new ThreadPoolExecutor(0, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), new zzbii("GAC_Transform"));

    public static ExecutorService zzqh() {
        return zzaEd;
    }
}
