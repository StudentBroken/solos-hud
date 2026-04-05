package com.ua.sdk.group;

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
import com.ua.sdk.concurrent.SaveRequest;
import com.ua.sdk.group.objective.GroupObjectiveImpl;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.Precondition;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class GroupManagerImpl extends AbstractManager<Group> implements GroupManager {
    public GroupManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<Group> diskCache, EntityService<Group> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.group.GroupManager
    public EntityList<Group> fetchGroupList(EntityListRef<Group> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.group.GroupManager
    public Request fetchGroupList(EntityListRef<Group> ref, FetchCallback<EntityList<Group>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.group.GroupManager
    public Group fetchGroup(EntityRef<Group> ref) throws UaException {
        return fetch(ref);
    }

    @Override // com.ua.sdk.group.GroupManager
    public Request fetchGroup(EntityRef<Group> ref, FetchCallback<Group> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.group.GroupManager
    public Group createGroup(Group group) throws UaException {
        return create(group);
    }

    @Override // com.ua.sdk.group.GroupManager
    public Request createGroup(Group group, CreateCallback<Group> callback) {
        return create(group, callback);
    }

    @Override // com.ua.sdk.group.GroupManager
    public Group updateGroup(Group group, EntityRef<Group> ref) throws UaException {
        return patch(group, ref);
    }

    @Override // com.ua.sdk.group.GroupManager
    public Request updateGroup(Group group, EntityRef<Group> ref, CreateCallback<Group> callback) {
        return patch(group, ref, callback);
    }

    @Override // com.ua.sdk.group.GroupManager
    public Group endGroupChallenge(Group group) throws UaException {
        Precondition.isNotNull(group);
        Precondition.isNotNull((GroupObjectiveImpl) group.getGroupObjective());
        ((GroupObjectiveImpl) group.getGroupObjective()).setEndDate(new Date());
        return update(group);
    }

    @Override // com.ua.sdk.group.GroupManager
    public Request endGroupChallenge(final Group group, SaveCallback<Group> callback) {
        final SaveRequest<Group> request = new SaveRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.group.GroupManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    request.done(GroupManagerImpl.this.endGroupChallenge(group), null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.group.GroupManager
    public Group endGroupChallenge(EntityRef<Group> ref) throws UaException {
        Precondition.isNotNull(ref);
        return endGroupChallenge(fetchGroup(ref));
    }

    @Override // com.ua.sdk.group.GroupManager
    public Request endGroupChallenge(final EntityRef<Group> ref, SaveCallback<Group> callback) {
        final SaveRequest<Group> request = new SaveRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.group.GroupManagerImpl.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    request.done(GroupManagerImpl.this.endGroupChallenge(ref), null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.group.GroupManager
    public void deleteGroup(EntityRef<Group> ref) throws UaException {
        delete(ref);
    }

    @Override // com.ua.sdk.group.GroupManager
    public Request deleteGroup(EntityRef<Group> ref, DeleteCallback<EntityRef<Group>> callback) {
        return delete(ref, callback);
    }
}
