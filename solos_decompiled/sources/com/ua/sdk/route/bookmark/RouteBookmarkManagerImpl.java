package com.ua.sdk.route.bookmark;

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
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.route.RouteBookmark;
import com.ua.sdk.route.RouteBookmarkManager;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class RouteBookmarkManagerImpl extends AbstractManager<RouteBookmark> implements RouteBookmarkManager {
    public RouteBookmarkManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<RouteBookmark> diskCache, EntityService<RouteBookmark> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.route.RouteBookmarkManager
    public EntityList<RouteBookmark> fetchRouteBookmarkList(EntityListRef<RouteBookmark> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.route.RouteBookmarkManager
    public Request fetchRouteBookmarkList(EntityListRef<RouteBookmark> ref, FetchCallback<EntityList<RouteBookmark>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.route.RouteBookmarkManager
    public RouteBookmark createRouteBookmark(RouteBookmark entity) throws UaException {
        return create(entity);
    }

    @Override // com.ua.sdk.route.RouteBookmarkManager
    public Request createRouteBookmark(RouteBookmark entity, CreateCallback<RouteBookmark> callback) {
        return create(entity, callback);
    }

    @Override // com.ua.sdk.route.RouteBookmarkManager
    public void deleteRouteBookmark(EntityRef<RouteBookmark> ref) throws UaException {
        delete(ref);
    }

    @Override // com.ua.sdk.route.RouteBookmarkManager
    public Request deleteRouteBookmark(EntityRef<RouteBookmark> ref, DeleteCallback<EntityRef<RouteBookmark>> callback) {
        return delete(ref, callback);
    }
}
