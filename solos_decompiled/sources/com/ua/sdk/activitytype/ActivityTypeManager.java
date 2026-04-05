package com.ua.sdk.activitytype;

import com.ua.sdk.EntityList;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityTypeManager {
    Request fetchActivityType(ActivityTypeRef activityTypeRef, FetchCallback<ActivityType> fetchCallback);

    ActivityType fetchActivityType(ActivityTypeRef activityTypeRef) throws UaException;

    EntityList<ActivityType> fetchActivityTypeList() throws UaException;

    Request fetchActivityTypeList(FetchCallback<EntityList<ActivityType>> fetchCallback);
}
