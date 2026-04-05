package com.ua.sdk.concurrent;

import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public abstract class AsyncRequest<T> implements Request {
    boolean canceled = false;
    boolean done = false;
    Future<?> future = null;

    abstract void onDone(T t, UaException uaException);

    @Override // com.ua.sdk.Request
    public synchronized boolean cancel() {
        boolean z = true;
        synchronized (this) {
            if (this.done) {
                z = false;
            } else {
                if (this.future != null) {
                    this.future.cancel(true);
                }
                this.canceled = true;
                onDone(null, new UaException(UaException.Code.CANCELED));
            }
        }
        return z;
    }

    @Override // com.ua.sdk.Request
    public boolean isCanceled() {
        return this.canceled;
    }

    @Override // com.ua.sdk.Request
    public boolean isAsynchronous() {
        return false;
    }

    public synchronized void setFuture(Future<?> future) {
        this.future = future;
        if (this.canceled) {
            future.cancel(true);
        }
    }

    public synchronized void done(T result, UaException e) {
        if (!this.canceled) {
            onDone(result, e);
            this.done = true;
        }
    }
}
