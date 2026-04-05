package com.ua.sdk.sleep;

import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class SleepManagerImpl extends AbstractManager<SleepMetric> implements SleepManager {
    public SleepManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<SleepMetric> diskCache, EntityService<SleepMetric> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.sleep.SleepManager
    public SleepMetricBuilder getSleepMetricBuilder() {
        return new SleepMetricBuilderImpl();
    }

    @Override // com.ua.sdk.sleep.SleepManager
    public SleepMetric fetchSleep(SleepRef ref) throws UaException {
        return fetch(ref);
    }

    @Override // com.ua.sdk.sleep.SleepManager
    public Request fetchSleep(SleepRef ref, FetchCallback<SleepMetric> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.sleep.SleepManager
    public EntityList<SleepMetric> fetchSleepList(SleepListRef ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.sleep.SleepManager
    public Request fetchSleepList(SleepListRef ref, FetchCallback<EntityList<SleepMetric>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.sleep.SleepManager
    public SleepMetric updateSleep(SleepMetric sleepMetric) throws UaException {
        return update(sleepMetric);
    }

    @Override // com.ua.sdk.sleep.SleepManager
    public Request updateSleep(SleepMetric sleepMetric, SaveCallback<SleepMetric> callback) {
        return update(sleepMetric, callback);
    }

    @Override // com.ua.sdk.sleep.SleepManager
    public SleepRef deleteSleep(SleepRef ref) throws UaException {
        return (SleepRef) delete(ref);
    }

    @Override // com.ua.sdk.sleep.SleepManager
    public Request deleteSleep(SleepRef ref, DeleteCallback<SleepRef> callback) {
        return delete(ref, callback);
    }
}
