package com.ua.sdk.cache.memory;

import com.ua.sdk.Reference;
import com.ua.sdk.Resource;
import com.ua.sdk.cache.Cache;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes65.dex */
public class MemoryCache implements Cache {
    WeakHashMap<Reference, Resource> map = new WeakHashMap<>();

    @Override // com.ua.sdk.cache.Cache
    public <R extends Reference> Resource<R> get(R ref) {
        return this.map.get(ref);
    }

    @Override // com.ua.sdk.cache.Cache
    public void put(Resource entity) {
        if (entity != null) {
            this.map.put(entity.getRef(), entity);
        }
    }

    @Override // com.ua.sdk.cache.Cache
    public boolean remove(Resource entity) {
        return (entity == null || this.map.remove(entity.getRef()) == null) ? false : true;
    }

    @Override // com.ua.sdk.cache.Cache
    public boolean remove(Reference ref) {
        return this.map.remove(ref) != null;
    }

    @Override // com.ua.sdk.cache.Cache
    public long getCacheAge(Reference ref) {
        return 0L;
    }
}
