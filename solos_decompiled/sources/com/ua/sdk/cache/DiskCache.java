package com.ua.sdk.cache;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public interface DiskCache<T extends Resource> {

    public enum State {
        NONE,
        CREATED,
        MODIFIED,
        SYNCED,
        DELETED
    }

    void delete(Reference reference);

    void deleteList(EntityListRef<T> entityListRef);

    T get(Reference reference);

    long getCacheAge(Reference reference);

    long getLastSynced(Reference reference);

    EntityList<T> getList(Reference reference);

    State getState(Reference reference);

    void markForDelete(Reference reference);

    long putForCreate(T t);

    void putForSave(T t);

    void updateAfterCreate(long j, T t);

    void updateAfterFetch(EntityListRef<T> entityListRef, EntityList<T> entityList, boolean z);

    void updateAfterFetch(T t);

    void updateAfterSave(T t);
}
