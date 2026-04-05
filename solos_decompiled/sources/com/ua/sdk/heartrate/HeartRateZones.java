package com.ua.sdk.heartrate;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.user.User;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface HeartRateZones extends Entity<EntityRef<HeartRateZones>> {
    void add(HeartRateZone heartRateZone);

    EntityRef<User> getUserRef();

    HeartRateZone getZone(int i);

    HeartRateZone getZone(String str);

    List<HeartRateZone> getZones();
}
