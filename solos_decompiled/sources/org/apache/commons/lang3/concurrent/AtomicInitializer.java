package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes51.dex */
public abstract class AtomicInitializer<T> implements ConcurrentInitializer<T> {
    private final AtomicReference<T> reference = new AtomicReference<>();

    protected abstract T initialize() throws ConcurrentException;

    @Override // org.apache.commons.lang3.concurrent.ConcurrentInitializer
    public T get() throws ConcurrentException {
        T result = this.reference.get();
        if (result == null) {
            T result2 = initialize();
            if (!this.reference.compareAndSet(null, result2)) {
                return this.reference.get();
            }
            return result2;
        }
        return result;
    }
}
