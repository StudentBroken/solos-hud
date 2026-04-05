package io.fabric.sdk.android.services.concurrency;

/* JADX INFO: loaded from: classes66.dex */
public interface Task {
    Throwable getError();

    boolean isFinished();

    void setError(Throwable th);

    void setFinished(boolean z);
}
