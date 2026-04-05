package io.fabric.sdk.android.services.persistence;

/* JADX INFO: loaded from: classes66.dex */
public interface PersistenceStrategy<T> {
    void clear();

    T restore();

    void save(T t);
}
