package com.ua.sdk.gear.brand;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface GearBrandManager {
    Request fetchGearBrand(EntityListRef<GearBrand> entityListRef, FetchCallback<EntityList<GearBrand>> fetchCallback);

    EntityList<GearBrand> fetchGearBrandList(EntityListRef<GearBrand> entityListRef) throws UaException;
}
