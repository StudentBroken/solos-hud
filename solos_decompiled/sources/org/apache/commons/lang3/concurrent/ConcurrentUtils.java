package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes51.dex */
public class ConcurrentUtils {
    private ConcurrentUtils() {
    }

    public static ConcurrentException extractCause(ExecutionException ex) {
        if (ex == null || ex.getCause() == null) {
            return null;
        }
        throwCause(ex);
        return new ConcurrentException(ex.getMessage(), ex.getCause());
    }

    public static ConcurrentRuntimeException extractCauseUnchecked(ExecutionException ex) {
        if (ex == null || ex.getCause() == null) {
            return null;
        }
        throwCause(ex);
        return new ConcurrentRuntimeException(ex.getMessage(), ex.getCause());
    }

    public static void handleCause(ExecutionException ex) throws ConcurrentException {
        ConcurrentException cex = extractCause(ex);
        if (cex != null) {
            throw cex;
        }
    }

    public static void handleCauseUnchecked(ExecutionException ex) {
        ConcurrentRuntimeException crex = extractCauseUnchecked(ex);
        if (crex != null) {
            throw crex;
        }
    }

    static Throwable checkedException(Throwable ex) {
        if (ex != null && !(ex instanceof RuntimeException) && !(ex instanceof Error)) {
            return ex;
        }
        throw new IllegalArgumentException("Not a checked exception: " + ex);
    }

    private static void throwCause(ExecutionException ex) {
        if (ex.getCause() instanceof RuntimeException) {
            throw ((RuntimeException) ex.getCause());
        }
        if (ex.getCause() instanceof Error) {
            throw ((Error) ex.getCause());
        }
    }

    public static <T> T initialize(ConcurrentInitializer<T> initializer) throws ConcurrentException {
        if (initializer != null) {
            return initializer.get();
        }
        return null;
    }

    public static <T> T initializeUnchecked(ConcurrentInitializer<T> concurrentInitializer) {
        try {
            return (T) initialize(concurrentInitializer);
        } catch (ConcurrentException e) {
            throw new ConcurrentRuntimeException(e.getCause());
        }
    }

    public static <K, V> V putIfAbsent(ConcurrentMap<K, V> map, K key, V value) {
        if (map == null) {
            return null;
        }
        V result = map.putIfAbsent(key, value);
        return result == null ? value : result;
    }

    public static <K, V> V createIfAbsent(ConcurrentMap<K, V> concurrentMap, K k, ConcurrentInitializer<V> concurrentInitializer) throws ConcurrentException {
        if (concurrentMap == null || concurrentInitializer == null) {
            return null;
        }
        V v = concurrentMap.get(k);
        if (v == null) {
            return (V) putIfAbsent(concurrentMap, k, concurrentInitializer.get());
        }
        return v;
    }

    public static <K, V> V createIfAbsentUnchecked(ConcurrentMap<K, V> concurrentMap, K k, ConcurrentInitializer<V> concurrentInitializer) {
        try {
            return (V) createIfAbsent(concurrentMap, k, concurrentInitializer);
        } catch (ConcurrentException e) {
            throw new ConcurrentRuntimeException(e.getCause());
        }
    }

    public static <T> Future<T> constantFuture(T value) {
        return new ConstantFuture(value);
    }

    static final class ConstantFuture<T> implements Future<T> {
        private final T value;

        ConstantFuture(T value) {
            this.value = value;
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return true;
        }

        @Override // java.util.concurrent.Future
        public T get() {
            return this.value;
        }

        @Override // java.util.concurrent.Future
        public T get(long timeout, TimeUnit unit) {
            return this.value;
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return false;
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }
    }
}
