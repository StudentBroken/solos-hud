package org.apache.commons.lang3.concurrent;

/* JADX INFO: loaded from: classes51.dex */
public abstract class LazyInitializer<T> implements ConcurrentInitializer<T> {
    private volatile T object;

    protected abstract T initialize() throws ConcurrentException;

    @Override // org.apache.commons.lang3.concurrent.ConcurrentInitializer
    public T get() throws ConcurrentException {
        T result = this.object;
        if (result == null) {
            synchronized (this) {
                result = this.object;
                if (result == null) {
                    result = initialize();
                    this.object = result;
                }
            }
        }
        return result;
    }
}
