package io.fabric.sdk.android.services.concurrency;

/* JADX INFO: loaded from: classes66.dex */
public class UnmetDependencyException extends RuntimeException {
    public UnmetDependencyException() {
    }

    public UnmetDependencyException(String detailMessage) {
        super(detailMessage);
    }

    public UnmetDependencyException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnmetDependencyException(Throwable throwable) {
        super(throwable);
    }
}
