package org.apache.commons.lang3.concurrent;

import org.apache.commons.lang3.ObjectUtils;

/* JADX INFO: loaded from: classes51.dex */
public class ConstantInitializer<T> implements ConcurrentInitializer<T> {
    private static final String FMT_TO_STRING = "ConstantInitializer@%d [ object = %s ]";
    private final T object;

    public ConstantInitializer(T obj) {
        this.object = obj;
    }

    public final T getObject() {
        return this.object;
    }

    @Override // org.apache.commons.lang3.concurrent.ConcurrentInitializer
    public T get() throws ConcurrentException {
        return getObject();
    }

    public int hashCode() {
        if (getObject() != null) {
            return getObject().hashCode();
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConstantInitializer)) {
            return false;
        }
        ConstantInitializer<?> c = (ConstantInitializer) obj;
        return ObjectUtils.equals(getObject(), c.getObject());
    }

    public String toString() {
        return String.format(FMT_TO_STRING, Integer.valueOf(System.identityHashCode(this)), String.valueOf(getObject()));
    }
}
