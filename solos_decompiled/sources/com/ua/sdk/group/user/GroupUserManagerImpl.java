package com.ua.sdk.group.user;

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
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class GroupUserManagerImpl extends AbstractManager<GroupUser> implements GroupUserManager {
    public GroupUserManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<GroupUser> diskCache, EntityService<GroupUser> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.group.user.GroupUserManager
    public EntityList<GroupUser> fetchGroupUserList(EntityListRef<GroupUser> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.group.user.GroupUserManager
    public Request fetchGroupUserList(EntityListRef<GroupUser> ref, FetchCallback<EntityList<GroupUser>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.group.user.GroupUserManager
    public GroupUser createGroupUser(GroupUser entity) throws UaException {
        return create(entity);
    }

    @Override // com.ua.sdk.group.user.GroupUserManager
    public Request createGroupUser(GroupUser entity, CreateCallback<GroupUser> callback) {
        return create(entity, callback);
    }

    @Override // com.ua.sdk.group.user.GroupUserManager
    public EntityRef<GroupUser> deleteGroupUser(EntityRef<GroupUser> ref) throws UaException {
        return (EntityRef) delete(ref);
    }

    @Override // com.ua.sdk.group.user.GroupUserManager
    public Request deleteGroupUser(EntityRef<GroupUser> ref, DeleteCallback<EntityRef<GroupUser>> callback) {
        return delete(ref, callback);
    }
}
