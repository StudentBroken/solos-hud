package com.ua.sdk.route;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.user.User;
import java.util.ArrayList;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface Route extends Entity {
    EntityRef<ActivityType> getActivityTypeRef();

    String getCity();

    ArrayList<Climb> getClimbs();

    String getCountry();

    Date getCreatedDate();

    String getDataSource();

    String getDescription();

    Double getDistanceAt(int i);

    Double getDistanceMeters();

    Double getElevationAt(int i);

    Double getLatitudeAt(int i);

    Double getLongitudeAt(int i);

    Double getMaxElevation();

    Double getMinElevation();

    String getName();

    Point getPointAt(int i);

    String getPostalCode();

    Privacy getPrivacy();

    @Override // com.ua.sdk.Entity, com.ua.sdk.Resource
    RouteRef getRef();

    String getStartPointType();

    Double getStartingLatitude();

    Double getStartingLongitude();

    String getState();

    String getThumbnailLink();

    Double getTotalAscent();

    Double getTotalDescent();

    int getTotalPoints();

    Date getUpdatedDate();

    EntityRef<User> getUserRef();
}
