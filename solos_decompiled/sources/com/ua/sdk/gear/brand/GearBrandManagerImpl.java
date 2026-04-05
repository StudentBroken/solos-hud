package com.ua.sdk.gear.brand;

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
public class GearBrandManagerImpl extends AbstractManager<GearBrand> implements GearBrandManager {
    public GearBrandManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<GearBrand> diskCache, EntityService<GearBrand> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.gear.brand.GearBrandManager
    public EntityList<GearBrand> fetchGearBrandList(EntityListRef<GearBrand> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.gear.brand.GearBrandManager
    public Request fetchGearBrand(EntityListRef<GearBrand> ref, FetchCallback<EntityList<GearBrand>> callback) {
        return fetchPage(ref, callback);
    }
}
