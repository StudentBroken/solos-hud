package com.ua.sdk.bodymass;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
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
public class BodyMassManagerImpl extends AbstractManager<BodyMass> implements BodyMassManager {
    public BodyMassManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<BodyMass> diskCache, EntityService<BodyMass> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.bodymass.BodyMassManager
    public EntityList<BodyMass> fetchBodyMassList(EntityListRef<BodyMass> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.bodymass.BodyMassManager
    public Request fetchBodyMassList(EntityListRef<BodyMass> ref, FetchCallback<EntityList<BodyMass>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.bodymass.BodyMassManager
    public BodyMass fetchBodyMass(EntityRef<BodyMass> ref) throws UaException {
        return fetch(ref);
    }

    @Override // com.ua.sdk.bodymass.BodyMassManager
    public Request fetchBodyMass(EntityRef<BodyMass> ref, FetchCallback<BodyMass> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.bodymass.BodyMassManager
    public BodyMass updateBodyMass(BodyMass entity) throws UaException {
        return update(entity);
    }

    @Override // com.ua.sdk.bodymass.BodyMassManager
    public Request updateBodyMass(BodyMass entity, SaveCallback<BodyMass> callback) {
        return update(entity, callback);
    }
}
