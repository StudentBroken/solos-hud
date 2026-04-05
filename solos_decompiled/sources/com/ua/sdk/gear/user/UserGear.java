package com.ua.sdk.gear.user;

import android.os.Parcelable;
import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.LocalDate;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.gear.Gear;
import com.ua.sdk.user.User;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface UserGear extends Entity, Parcelable {
    Double getCurrentDistance();

    List<EntityRef<ActivityType>> getDefaultActivities();

    Gear getGear();

    Double getInitialDistance();

    String getName();

    LocalDate getPurchaseDate();

    Double getTargetDistance();

    EntityRef<User> getUser();

    Boolean isRetired();

    void setDefaultActivities(List<EntityRef<ActivityType>> list);

    void setGear(Gear gear);

    void setInitialDistance(Double d);

    void setName(String str);

    void setPurchaseDate(LocalDate localDate);

    void setRetired(Boolean bool);

    void setTargetDistance(Double d);
}
