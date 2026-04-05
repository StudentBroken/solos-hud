package com.ua.sdk.group.invite;

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
import com.ua.sdk.concurrent.CreateRequest;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class GroupInviteManagerImpl extends AbstractManager<GroupInvite> implements GroupInviteManager {
    public GroupInviteManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<GroupInvite> diskCache, EntityService<GroupInvite> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.group.invite.GroupInviteManager
    public EntityList<GroupInvite> fetchGroupInviteList(EntityListRef<GroupInvite> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.group.invite.GroupInviteManager
    public Request fetchGroupInviteList(EntityListRef<GroupInvite> ref, FetchCallback<EntityList<GroupInvite>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.group.invite.GroupInviteManager
    public GroupInvite createGroupInvite(GroupInvite entity) throws UaException {
        return create(entity);
    }

    @Override // com.ua.sdk.group.invite.GroupInviteManager
    public Request createGroupInvite(GroupInvite entity, CreateCallback<GroupInvite> callback) {
        return create(entity, callback);
    }

    @Override // com.ua.sdk.group.invite.GroupInviteManager
    public EntityList<GroupInvite> patchGroupInvite(GroupInvite entity) throws UaException {
        return ((GroupInviteService) this.service).patch(entity);
    }

    @Override // com.ua.sdk.group.invite.GroupInviteManager
    public Request patchGroupInvite(final GroupInvite entity, CreateCallback<EntityList<GroupInvite>> callback) {
        final CreateRequest<EntityList<GroupInvite>> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.group.invite.GroupInviteManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    request.done(GroupInviteManagerImpl.this.patchGroupInvite(entity), null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.group.invite.GroupInviteManager
    public EntityRef<GroupInvite> deleteGroupInvite(EntityRef<GroupInvite> ref) throws UaException {
        return (EntityRef) delete(ref);
    }

    @Override // com.ua.sdk.group.invite.GroupInviteManager
    public Request deleteGroupInvite(EntityRef<GroupInvite> ref, DeleteCallback<EntityRef<GroupInvite>> callback) {
        return delete(ref, callback);
    }
}
