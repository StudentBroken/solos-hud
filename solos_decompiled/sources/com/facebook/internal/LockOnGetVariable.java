package com.facebook.internal;

import com.facebook.FacebookSdk;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

/* JADX INFO: loaded from: classes41.dex */
public class LockOnGetVariable<T> {
    private CountDownLatch initLatch;
    private T value;

    public LockOnGetVariable(T value) {
        this.value = value;
    }

    public LockOnGetVariable(final Callable<T> callable) {
        this.initLatch = new CountDownLatch(1);
        FacebookSdk.getExecutor().execute(new FutureTask(new Callable<Void>() { // from class: com.facebook.internal.LockOnGetVariable.1
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                try {
                    LockOnGetVariable.this.value = callable.call();
                    LockOnGetVariable.this.initLatch.countDown();
                    return null;
                } catch (Throwable th) {
                    LockOnGetVariable.this.initLatch.countDown();
                    throw th;
                }
            }
        }));
    }

    public T getValue() {
        waitOnInit();
        return this.value;
    }

    private void waitOnInit() {
        if (this.initLatch != null) {
            try {
                this.initLatch.await();
            } catch (InterruptedException e) {
            }
        }
    }
}
