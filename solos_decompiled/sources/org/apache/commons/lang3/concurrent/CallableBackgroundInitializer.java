package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes51.dex */
public class CallableBackgroundInitializer<T> extends BackgroundInitializer<T> {
    private final Callable<T> callable;

    public CallableBackgroundInitializer(Callable<T> call) {
        checkCallable(call);
        this.callable = call;
    }

    public CallableBackgroundInitializer(Callable<T> call, ExecutorService exec) {
        super(exec);
        checkCallable(call);
        this.callable = call;
    }

    @Override // org.apache.commons.lang3.concurrent.BackgroundInitializer
    protected T initialize() throws Exception {
        return this.callable.call();
    }

    private void checkCallable(Callable<T> call) {
        if (call == null) {
            throw new IllegalArgumentException("Callable must not be null!");
        }
    }
}
