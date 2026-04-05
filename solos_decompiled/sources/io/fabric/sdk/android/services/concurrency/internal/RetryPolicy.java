package io.fabric.sdk.android.services.concurrency.internal;

/* JADX INFO: loaded from: classes66.dex */
public interface RetryPolicy {
    boolean shouldRetry(int i, Throwable th);
}
