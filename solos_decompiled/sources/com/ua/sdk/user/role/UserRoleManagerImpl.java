package com.ua.sdk.user.role;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.concurrent.CreateRequest;
import com.ua.sdk.concurrent.FetchRequest;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class UserRoleManagerImpl extends AbstractManager<UserRole> implements UserRoleManager {
    public UserRoleManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<UserRole> diskCache, EntityService<UserRole> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.user.role.UserRoleManager
    public UserRole fetchUserRole(EntityRef ref) throws UaException {
        return fetch(ref);
    }

    @Override // com.ua.sdk.user.role.UserRoleManager
    public Request fetchUserRole(final EntityRef ref, FetchCallback<UserRole> callback) {
        final FetchRequest<UserRole> request = new FetchRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.user.role.UserRoleManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    UserRole userRole = UserRoleManagerImpl.this.fetchUserRole(ref);
                    request.done(userRole, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.user.role.UserRoleManager
    public UserRole createUserRole(UserRole entity) throws UaException {
        return create(entity);
    }

    @Override // com.ua.sdk.user.role.UserRoleManager
    public Request createUserRole(final UserRole entity, CreateCallback<UserRole> callback) {
        final CreateRequest<UserRole> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.user.role.UserRoleManagerImpl.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    UserRole userRole = UserRoleManagerImpl.this.createUserRole(entity);
                    request.done(userRole, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }
}
