package com.ua.sdk.remoteconnection;

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
public class RemoteConnectionManagerImpl extends AbstractManager<RemoteConnection> implements RemoteConnectionManager {
    public RemoteConnectionManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<RemoteConnection> diskCache, EntityService<RemoteConnection> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public EntityList<RemoteConnection> fetchRemoteConnectionList() throws UaException {
        return fetchPage(null);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public Request fetchRemoteConnectionList(FetchCallback<EntityList<RemoteConnection>> callback) {
        return fetchPage((EntityListRef) null, callback);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public RemoteConnection fetchRemoteConnection(EntityRef<RemoteConnection> ref) throws UaException {
        return fetch(ref);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public Request fetchRemoteConnection(EntityRef<RemoteConnection> ref, FetchCallback<RemoteConnection> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public void deleteRemoteConnection(EntityRef<RemoteConnection> ref) throws UaException {
        delete(ref);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public Request deleteRemoteConnection(EntityRef<RemoteConnection> ref, DeleteCallback<RemoteConnectionRef> callback) {
        return delete(ref, callback);
    }
}
