package com.ua.sdk.cache;

import com.ua.sdk.Reference;
import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public interface Cache {
    <R extends Reference> Resource<R> get(R r);

    long getCacheAge(Reference reference);

    void put(Resource resource);

    boolean remove(Reference reference);

    boolean remove(Resource resource);
}
