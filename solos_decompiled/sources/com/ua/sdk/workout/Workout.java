package com.ua.sdk.workout;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.activitystory.Attachment;
import com.ua.sdk.activitystory.SocialSettings;
import com.ua.sdk.activitytype.ActivityTypeRef;
import com.ua.sdk.gear.user.UserGearRef;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.route.RouteRef;
import com.ua.sdk.user.User;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public interface Workout extends Entity {
    ActivityTypeRef getActivityTypeRef();

    WorkoutAggregates getAggregates();

    Attachment getAttachment(int i) throws IndexOutOfBoundsException;

    int getAttachmentCount();

    Date getCreatedTime();

    String getName();

    String getNotes();

    Privacy getPrivacy();

    String getReferenceKey();

    RouteRef getRouteRef();

    SocialSettings getSocialSettings();

    String getSource();

    Date getStartTime();

    TimeSeriesData getTimeSeriesData();

    TimeZone getTimeZone();

    Date getUpdatedTime();

    UserGearRef getUserGearRef();

    EntityRef<User> getUserRef();

    Boolean hasTimeSeries();

    Boolean isVerified();
}
