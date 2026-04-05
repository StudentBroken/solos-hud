package com.ua.sdk.activitytimeseries;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.Precondition;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTimeSeriesManagerImpl extends AbstractManager<ActivityTimeSeries> implements ActivityTimeSeriesManager {
    public ActivityTimeSeriesManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<ActivityTimeSeries> diskCache, EntityService<ActivityTimeSeries> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeriesManager
    public ActivityTimeSeriesBuilder getActivityTimeSeriesBuilder() {
        return new ActivityTimeSeriesBuilderImpl();
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeriesManager
    public ActivityTimeSeries createTimeSeries(ActivityTimeSeries timeSeries) throws UaException {
        checkNull(timeSeries);
        return create(timeSeries);
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeriesManager
    public Request createTimeSeries(ActivityTimeSeries timeSeries, CreateCallback<ActivityTimeSeries> callback) {
        checkNull(timeSeries);
        return create(timeSeries, callback);
    }

    private void checkNull(ActivityTimeSeries timeSeries) {
        Precondition.isNotNull(timeSeries.getRecorderTypeKey());
        Precondition.isNotNull(timeSeries.getRecorderIdentifier());
    }
}
