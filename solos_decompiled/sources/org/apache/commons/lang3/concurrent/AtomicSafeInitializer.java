package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes51.dex */
public abstract class AtomicSafeInitializer<T> implements ConcurrentInitializer<T> {
    private final AtomicReference<AtomicSafeInitializer<T>> factory = new AtomicReference<>();
    private final AtomicReference<T> reference = new AtomicReference<>();

    protected abstract T initialize() throws ConcurrentException;

    @Override // org.apache.commons.lang3.concurrent.ConcurrentInitializer
    public final T get() throws ConcurrentException {
        while (true) {
            T result = this.reference.get();
            if (result == null) {
                if (this.factory.compareAndSet(null, this)) {
                    this.reference.set(initialize());
                }
            } else {
                return result;
            }
        }
    }
}
