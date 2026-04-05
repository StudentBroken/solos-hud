package com.ua.sdk.heartrate;

import com.ua.sdk.CreateCallback;
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
public class HeartRateZonesManagerImpl extends AbstractManager<HeartRateZones> implements HeartRateZonesManager {
    private static final int NUM_HR_ZONES = 5;

    public HeartRateZonesManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<HeartRateZones> diskCache, EntityService<HeartRateZones> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.heartrate.HeartRateZonesManager
    public EntityList<HeartRateZones> fetchHeartRateZonesList(EntityListRef<HeartRateZones> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.heartrate.HeartRateZonesManager
    public Request fetchHeartRateZonesList(EntityListRef<HeartRateZones> ref, FetchCallback<EntityList<HeartRateZones>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.heartrate.HeartRateZonesManager
    public HeartRateZones fetchHeartRateZones(EntityRef<HeartRateZones> ref) throws UaException {
        return fetch(ref);
    }

    @Override // com.ua.sdk.heartrate.HeartRateZonesManager
    public Request fetchHeartRateZones(EntityRef<HeartRateZones> ref, FetchCallback<HeartRateZones> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.heartrate.HeartRateZonesManager
    public HeartRateZones createHeartRateZones(HeartRateZones entity) throws UaException {
        return create(entity);
    }

    @Override // com.ua.sdk.heartrate.HeartRateZonesManager
    public Request createHeartRateZones(HeartRateZones entity, CreateCallback<HeartRateZones> callback) {
        return create(entity, callback);
    }

    @Override // com.ua.sdk.heartrate.HeartRateZonesManager
    public HeartRateZones calculateHeartRateZonesWithAge(int age) {
        int maxHeartRate = (int) (208.0d - (0.7d * ((double) age)));
        return calculateHeartRateZonesWithMaxHeartRate(maxHeartRate);
    }

    @Override // com.ua.sdk.heartrate.HeartRateZonesManager
    public HeartRateZones calculateHeartRateZonesWithMaxHeartRate(int maxHR) {
        HeartRateZone[] zones = new HeartRateZone[5];
        zones[0] = new HeartRateZone("zone1", 0, (int) (((double) maxHR) * 0.6d));
        zones[1] = new HeartRateZone("zone2", zones[0].end + 1, (int) (((double) maxHR) * 0.7d));
        zones[2] = new HeartRateZone("zone3", zones[1].end + 1, (int) (((double) maxHR) * 0.8d));
        zones[3] = new HeartRateZone("zone4", zones[2].end + 1, (int) (((double) maxHR) * 0.9d));
        zones[4] = new HeartRateZone("zone5", zones[3].end + 1, maxHR);
        return new HeartRateZonesImpl(zones);
    }
}
