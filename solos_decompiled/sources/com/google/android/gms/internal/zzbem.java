package com.google.android.gms.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbem {
    private static final ExecutorService zzaEd = Executors.newFixedThreadPool(2, new zzbii("GAC_Executor"));

    public static ExecutorService zzqh() {
        return zzaEd;
    }
}
