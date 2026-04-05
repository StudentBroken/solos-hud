package com.ua.sdk.remoteconnection;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.actigraphysettings.ActigraphySettings;
import com.ua.sdk.actigraphysettings.ActigraphySettingsManager;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionTypeManagerImpl extends AbstractManager<RemoteConnectionType> implements RemoteConnectionTypeManager {
    private ActigraphySettingsManager actigraphySettingsManager;
    private RemoteConnectionTypePageImpl mRemoteConnectionTypePage;

    public RemoteConnectionTypeManagerImpl(ActigraphySettingsManager actigraphySettingsManager, CacheSettings cacheSettings, Cache memCache, DiskCache<RemoteConnectionType> diskCache, EntityService<RemoteConnectionType> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
        this.actigraphySettingsManager = actigraphySettingsManager;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionTypeManager
    public EntityList<RemoteConnectionType> fetchRemoteConnectionTypeList() throws UaException {
        return fetchPage(null);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionTypeManager
    public Request fetchRemoteConnectionTypeList(FetchCallback<EntityList<RemoteConnectionType>> callback) {
        return fetchPage((EntityListRef) null, callback);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionTypeManager
    public RemoteConnectionType fetchRemoteConnectionType(EntityRef<RemoteConnectionType> ref) throws UaException {
        return fetch(ref);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionTypeManager
    public Request fetchRemoteConnectionType(EntityRef<RemoteConnectionType> ref, FetchCallback<RemoteConnectionType> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionTypeManager
    public ActigraphySettings fetchRemoteConnectionPriorities() throws UaException {
        return this.actigraphySettingsManager.fetchActigraphySettings();
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionTypeManager
    public Request fetchRemoteConnectionPriorities(FetchCallback<ActigraphySettings> callback) {
        return this.actigraphySettingsManager.fetchActigraphySettings(callback);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionTypeManager
    public void updateSleepConnectionPriorities(EntityRef<RemoteConnectionType> ref) throws UaException {
        this.actigraphySettingsManager.setSleepRecorderPriority(ref);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionTypeManager
    public Request updateSleepConnectionPriorities(EntityRef<RemoteConnectionType> ref, CreateCallback<ActigraphySettings> callback) {
        return this.actigraphySettingsManager.setSleepRecorderPriority(ref, callback);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionTypeManager
    public void updateActivityConnectionPriorities(EntityRef<RemoteConnectionType> ref) throws UaException {
        this.actigraphySettingsManager.setActivityRecorderPriority(ref);
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionTypeManager
    public Request updateActivityConnectionPriorities(EntityRef<RemoteConnectionType> ref, CreateCallback<ActigraphySettings> callback) {
        return this.actigraphySettingsManager.setActivityRecorderPriority(ref, callback);
    }
}
