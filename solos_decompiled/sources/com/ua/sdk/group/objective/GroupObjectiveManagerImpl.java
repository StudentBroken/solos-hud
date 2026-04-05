package com.ua.sdk.group.objective;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class GroupObjectiveManagerImpl extends AbstractManager<GroupObjective> implements GroupObjectiveManager {
    public GroupObjectiveManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<GroupObjective> diskCache, EntityService<GroupObjective> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.group.objective.GroupObjectiveManager
    public EntityList<GroupObjective> fetchGroupObjectiveList(EntityListRef<GroupObjective> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.group.objective.GroupObjectiveManager
    public Request fetchGroupObjectiveList(EntityListRef<GroupObjective> ref, FetchCallback<EntityList<GroupObjective>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.group.objective.GroupObjectiveManager
    public GroupObjective fetchGroupObjective(EntityRef<GroupObjective> ref) throws UaException {
        return fetch(ref);
    }

    @Override // com.ua.sdk.group.objective.GroupObjectiveManager
    public Request fetchGroupObjective(EntityRef<GroupObjective> ref, FetchCallback<GroupObjective> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.group.objective.GroupObjectiveManager
    public GroupObjective createGroupObjective(GroupObjective entity) throws UaException {
        return create(entity);
    }

    @Override // com.ua.sdk.group.objective.GroupObjectiveManager
    public Request createGroupObjective(GroupObjective entity, CreateCallback<GroupObjective> callback) {
        return create(entity, callback);
    }

    @Override // com.ua.sdk.group.objective.GroupObjectiveManager
    public GroupObjective updateGroupObjective(GroupObjective entity) throws UaException {
        return update(entity);
    }

    @Override // com.ua.sdk.group.objective.GroupObjectiveManager
    public Request updateGroupObjective(GroupObjective entity, SaveCallback<GroupObjective> callback) {
        return update(entity, callback);
    }

    @Override // com.ua.sdk.group.objective.GroupObjectiveManager
    public EntityRef<GroupObjective> deleteGroupObjective(EntityRef<GroupObjective> ref) throws UaException {
        return (EntityRef) delete(ref);
    }

    @Override // com.ua.sdk.group.objective.GroupObjectiveManager
    public Request deleteGroupObjective(EntityRef<GroupObjective> ref, DeleteCallback<EntityRef<GroupObjective>> callback) {
        return delete(ref, callback);
    }
}
