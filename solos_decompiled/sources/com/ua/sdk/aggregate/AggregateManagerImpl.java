package com.ua.sdk.aggregate;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
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
public class AggregateManagerImpl extends AbstractManager<Aggregate> implements AggregateManager {
    public AggregateManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<Aggregate> diskCache, EntityService<Aggregate> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.aggregate.AggregateManager
    public EntityList<Aggregate> fetchAggregateList(EntityListRef<Aggregate> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.aggregate.AggregateManager
    public Request fetchAggregateList(EntityListRef<Aggregate> ref, FetchCallback<EntityList<Aggregate>> callback) {
        return fetchPage(ref, callback);
    }
}
