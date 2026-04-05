package com.ua.sdk.route;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface RouteBookmarkManager {
    Request createRouteBookmark(RouteBookmark routeBookmark, CreateCallback<RouteBookmark> createCallback);

    RouteBookmark createRouteBookmark(RouteBookmark routeBookmark) throws UaException;

    Request deleteRouteBookmark(EntityRef<RouteBookmark> entityRef, DeleteCallback<EntityRef<RouteBookmark>> deleteCallback);

    void deleteRouteBookmark(EntityRef<RouteBookmark> entityRef) throws UaException;

    EntityList<RouteBookmark> fetchRouteBookmarkList(EntityListRef<RouteBookmark> entityListRef) throws UaException;

    Request fetchRouteBookmarkList(EntityListRef<RouteBookmark> entityListRef, FetchCallback<EntityList<RouteBookmark>> fetchCallback);
}
