package com.ua.sdk.route;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface RouteManager {
    Request createRoute(Route route, CreateCallback<Route> createCallback);

    void createRoute(Route route) throws UaException;

    Request createRouteBookmark(Route route, CreateCallback<RouteBookmark> createCallback);

    RouteBookmark createRouteBookmark(Route route) throws UaException;

    Request deleteRoute(RouteRef routeRef, DeleteCallback<RouteRef> deleteCallback);

    void deleteRoute(RouteRef routeRef) throws UaException;

    Request deleteRouteBookmark(EntityRef<RouteBookmark> entityRef, DeleteCallback<EntityRef<RouteBookmark>> deleteCallback);

    void deleteRouteBookmark(EntityRef<RouteBookmark> entityRef) throws UaException;

    EntityList<Route> fetchBookmarkedRoutes(EntityListRef<RouteBookmark> entityListRef) throws UaException;

    Request fetchBookmarkedRoutes(EntityListRef<RouteBookmark> entityListRef, FetchCallback<EntityList<Route>> fetchCallback);

    Request fetchRoute(RouteRef routeRef, FetchCallback<Route> fetchCallback);

    Route fetchRoute(RouteRef routeRef) throws UaException;

    EntityList<RouteBookmark> fetchRouteBookmarkList(EntityListRef<RouteBookmark> entityListRef) throws UaException;

    Request fetchRouteBookmarkList(EntityListRef<RouteBookmark> entityListRef, FetchCallback<EntityList<RouteBookmark>> fetchCallback);

    EntityList<Route> fetchRouteList(EntityListRef<Route> entityListRef) throws UaException;

    Request fetchRouteList(EntityListRef<Route> entityListRef, FetchCallback<EntityList<Route>> fetchCallback);

    RouteBuilder getRouteBuilder();

    Request updateRoute(Route route, SaveCallback<Route> saveCallback);

    void updateRoute(Route route) throws UaException;
}
