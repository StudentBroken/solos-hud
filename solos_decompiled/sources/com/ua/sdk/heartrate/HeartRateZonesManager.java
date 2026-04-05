package com.ua.sdk.heartrate;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface HeartRateZonesManager {
    HeartRateZones calculateHeartRateZonesWithAge(int i);

    HeartRateZones calculateHeartRateZonesWithMaxHeartRate(int i);

    Request createHeartRateZones(HeartRateZones heartRateZones, CreateCallback<HeartRateZones> createCallback);

    HeartRateZones createHeartRateZones(HeartRateZones heartRateZones) throws UaException;

    Request fetchHeartRateZones(EntityRef<HeartRateZones> entityRef, FetchCallback<HeartRateZones> fetchCallback);

    HeartRateZones fetchHeartRateZones(EntityRef<HeartRateZones> entityRef) throws UaException;

    EntityList<HeartRateZones> fetchHeartRateZonesList(EntityListRef<HeartRateZones> entityListRef) throws UaException;

    Request fetchHeartRateZonesList(EntityListRef<HeartRateZones> entityListRef, FetchCallback<EntityList<HeartRateZones>> fetchCallback);
}
