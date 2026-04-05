package com.ua.sdk.cache;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.Resource;
import com.ua.sdk.cache.DiskCache;

/* JADX INFO: loaded from: classes65.dex */
public class NullDiskCache<T extends Resource> implements DiskCache<T> {
    @Override // com.ua.sdk.cache.DiskCache
    public T get(Reference ref) {
        return null;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public void updateAfterFetch(T entity) {
    }

    @Override // com.ua.sdk.cache.DiskCache
    public void updateAfterFetch(EntityListRef<T> requestRef, EntityList<T> entities, boolean partial) {
    }

    @Override // com.ua.sdk.cache.DiskCache
    public EntityList<T> getList(Reference ref) {
        return null;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public long putForCreate(T entity) {
        return 0L;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public void updateAfterCreate(long localId, T entity) {
    }

    @Override // com.ua.sdk.cache.DiskCache
    public void putForSave(T entity) {
    }

    @Override // com.ua.sdk.cache.DiskCache
    public void updateAfterSave(T entity) {
    }

    @Override // com.ua.sdk.cache.DiskCache
    public void markForDelete(Reference ref) {
    }

    @Override // com.ua.sdk.cache.DiskCache
    public void delete(Reference ref) {
    }

    @Override // com.ua.sdk.cache.DiskCache
    public void deleteList(EntityListRef<T> ref) {
    }

    @Override // com.ua.sdk.cache.DiskCache
    public long getCacheAge(Reference ref) {
        return 0L;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public long getLastSynced(Reference ref) {
        return 0L;
    }

    @Override // com.ua.sdk.cache.DiskCache
    public DiskCache.State getState(Reference ref) {
        return null;
    }
}
