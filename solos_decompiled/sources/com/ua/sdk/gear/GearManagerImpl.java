package com.ua.sdk.gear;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.gear.brand.GearBrand;
import com.ua.sdk.gear.brand.GearBrandManager;
import com.ua.sdk.gear.user.UserGear;
import com.ua.sdk.gear.user.UserGearManager;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.Precondition;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class GearManagerImpl extends AbstractManager<Gear> implements GearManager {
    private final GearBrandManager gearBrandManager;
    private final UserGearManager userGearManager;

    public GearManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<Gear> diskCache, EntityService<Gear> service, ExecutorService executor, GearBrandManager gearBrandManager, UserGearManager userGearManager) {
        super(cacheSettings, memCache, diskCache, service, executor);
        this.gearBrandManager = (GearBrandManager) Precondition.isNotNull(gearBrandManager);
        this.userGearManager = (UserGearManager) Precondition.isNotNull(userGearManager);
    }

    @Override // com.ua.sdk.gear.GearManager
    public EntityList<Gear> fetchGearFromBrand(EntityListRef<Gear> gearFromBrandListRef) throws UaException {
        return fetchPage(gearFromBrandListRef);
    }

    @Override // com.ua.sdk.gear.GearManager
    public Request fetchGearFromBrand(EntityListRef<Gear> gearFromBrandListRef, FetchCallback<EntityList<Gear>> callback) {
        return fetchPage(gearFromBrandListRef, callback);
    }

    @Override // com.ua.sdk.gear.GearManager
    public EntityList<GearBrand> fetchGearBrandList(EntityListRef<GearBrand> ref) throws UaException {
        return this.gearBrandManager.fetchGearBrandList(ref);
    }

    @Override // com.ua.sdk.gear.GearManager
    public Request fetchGearBrandList(EntityListRef<GearBrand> ref, FetchCallback<EntityList<GearBrand>> callback) {
        return this.gearBrandManager.fetchGearBrand(ref, callback);
    }

    @Override // com.ua.sdk.gear.GearManager
    public UserGear createEmptyUserGearObject() {
        return this.userGearManager.createEmptyUserGearObject();
    }

    @Override // com.ua.sdk.gear.GearManager
    public EntityList<UserGear> fetchUserGear(EntityListRef<UserGear> ref) throws UaException {
        return this.userGearManager.fetchUserGear(ref);
    }

    @Override // com.ua.sdk.gear.GearManager
    public Request fetchUserGear(EntityListRef<UserGear> ref, FetchCallback<EntityList<UserGear>> callback) {
        return this.userGearManager.fetchUserGear(ref, callback);
    }

    @Override // com.ua.sdk.gear.GearManager
    public UserGear updateUserGear(EntityRef<UserGear> ref, UserGear userGear) throws UaException {
        return this.userGearManager.updateUserGear(ref, userGear);
    }

    @Override // com.ua.sdk.gear.GearManager
    public Request updateUserGear(EntityRef<UserGear> ref, UserGear userGear, CreateCallback<UserGear> callback) {
        return this.userGearManager.updateUserGear(ref, userGear, callback);
    }

    @Override // com.ua.sdk.gear.GearManager
    public UserGear createUserGear(UserGear userGear) throws UaException {
        return this.userGearManager.createUserGear(userGear);
    }

    @Override // com.ua.sdk.gear.GearManager
    public Request createUserGear(UserGear userGear, CreateCallback<UserGear> callback) {
        return this.userGearManager.createUserGear(userGear, callback);
    }

    @Override // com.ua.sdk.gear.GearManager
    public void deleteUserGear(EntityRef<UserGear> ref) throws Exception {
        this.userGearManager.deleteUserGear(ref);
    }

    @Override // com.ua.sdk.gear.GearManager
    public Request deleteUserGear(EntityRef<UserGear> ref, DeleteCallback callback) {
        return this.userGearManager.deleteUserGear(ref, callback);
    }
}
