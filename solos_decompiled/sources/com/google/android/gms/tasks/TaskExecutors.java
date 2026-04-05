package com.google.android.gms.tasks;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes56.dex */
public final class TaskExecutors {
    public static final Executor MAIN_THREAD = new zza();
    static final Executor zzbMh = new zzm();

    static final class zza implements Executor {
        private final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override // java.util.concurrent.Executor
        public final void execute(@NonNull Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }

    private TaskExecutors() {
    }
}
