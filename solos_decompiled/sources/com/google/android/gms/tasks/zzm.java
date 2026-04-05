package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes56.dex */
final class zzm implements Executor {
    zzm() {
    }

    @Override // java.util.concurrent.Executor
    public final void execute(@NonNull Runnable runnable) {
        runnable.run();
    }
}
