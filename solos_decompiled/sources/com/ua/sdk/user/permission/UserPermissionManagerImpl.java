package com.ua.sdk.user.permission;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.concurrent.FetchRequest;
import com.ua.sdk.internal.AbstractManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class UserPermissionManagerImpl extends AbstractManager<UserPermission> implements UserPermissionManager {
    private final UserPermissionService service;

    public UserPermissionManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<UserPermission> diskCache, UserPermissionService service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, null, executor);
        this.service = service;
    }

    @Override // com.ua.sdk.user.permission.UserPermissionManager
    public EntityList<UserPermission> fetchUserPermission(EntityRef ref) throws UaException {
        return this.service.fetchUserPermission(ref);
    }

    @Override // com.ua.sdk.user.permission.UserPermissionManager
    public Request fetchUserPermission(final EntityRef ref, FetchCallback<EntityList<UserPermission>> callback) {
        final FetchRequest<EntityList<UserPermission>> request = new FetchRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.user.permission.UserPermissionManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    EntityList<UserPermission> permissions = UserPermissionManagerImpl.this.fetchUserPermission(ref);
                    request.done(permissions, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.user.permission.UserPermissionManager
    public EntityList<UserPermission> fetchUserPermissionList() throws UaException {
        return this.service.fetchUserPermissions();
    }

    @Override // com.ua.sdk.user.permission.UserPermissionManager
    public Request fetchUserPermissionList(FetchCallback<EntityList<UserPermission>> callback) {
        final FetchRequest<EntityList<UserPermission>> request = new FetchRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.user.permission.UserPermissionManagerImpl.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    EntityList<UserPermission> permissions = UserPermissionManagerImpl.this.fetchUserPermissionList();
                    request.done(permissions, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }
}
