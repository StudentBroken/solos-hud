package com.ua.sdk.cache;

import com.ua.sdk.Reference;
import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public class NullMemCache implements Cache {
    @Override // com.ua.sdk.cache.Cache
    public <R extends Reference> Resource<R> get(R ref) {
        return null;
    }

    @Override // com.ua.sdk.cache.Cache
    public void put(Resource entity) {
    }

    @Override // com.ua.sdk.cache.Cache
    public boolean remove(Resource entity) {
        return false;
    }

    @Override // com.ua.sdk.cache.Cache
    public boolean remove(Reference ref) {
        return false;
    }

    @Override // com.ua.sdk.cache.Cache
    public long getCacheAge(Reference ref) {
        return 0L;
    }
}
