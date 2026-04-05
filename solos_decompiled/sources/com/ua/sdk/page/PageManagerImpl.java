package com.ua.sdk.page;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class PageManagerImpl extends AbstractManager<Page> {
    public PageManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<Page> diskCache, EntityService<Page> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    public Page fetchPage(EntityRef<Page> ref) throws UaException {
        return fetch(ref);
    }

    public Request fetchPage(EntityRef<Page> ref, FetchCallback<Page> callback) {
        return fetch(ref, callback);
    }

    public EntityList<Page> fetchPageList(EntityListRef<Page> ref) throws UaException {
        return fetchPage(ref);
    }

    public Request fetchPageList(EntityListRef<Page> ref, FetchCallback<EntityList<Page>> callback) {
        return fetchPage(ref, callback);
    }
}
