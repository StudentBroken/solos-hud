package com.ua.sdk.role;

import com.ua.sdk.EntityList;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.concurrent.FetchRequest;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.role.Role;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class RoleManagerImpl extends AbstractManager<Role> implements RoleManager {
    public RoleManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<Role> diskCache, EntityService<Role> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.role.RoleManager
    public Role fetchRole(Role.Type name) {
        return RoleHelper.getRole(name);
    }

    @Override // com.ua.sdk.role.RoleManager
    public EntityList<Role> fetchRoleList() throws UaException {
        return this.service.fetchPage(null);
    }

    @Override // com.ua.sdk.role.RoleManager
    public Request fetchRoleList(FetchCallback<EntityList<Role>> callback) {
        final FetchRequest<EntityList<Role>> request = new FetchRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.role.RoleManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    EntityList<Role> roles = RoleManagerImpl.this.fetchRoleList();
                    request.done(roles, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }
}
