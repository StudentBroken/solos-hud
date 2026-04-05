package com.ua.sdk.actigraphysettings;

import com.kopin.solos.storage.Ride;
import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.concurrent.CreateRequest;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.remoteconnection.RemoteConnectionType;
import com.ua.sdk.remoteconnection.RemoteConnectionTypeManager;
import com.ua.sdk.remoteconnection.RemoteConnectionTypeRef;
import com.ua.sdk.user.UserManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphySettingsManagerImpl extends AbstractManager<ActigraphySettings> implements ActigraphySettingsManager {
    private RemoteConnectionTypeManager remoteConnectionTypeManager;
    private UserManager userManager;

    public ActigraphySettingsManagerImpl(UserManager userManager, CacheSettings cacheSettings, Cache memCache, DiskCache<ActigraphySettings> diskCache, EntityService<ActigraphySettings> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
        this.userManager = userManager;
    }

    @Override // com.ua.sdk.actigraphysettings.ActigraphySettingsManager
    public void setRemoteConnectionTypeManager(RemoteConnectionTypeManager remoteConnectionTypeManager) {
        this.remoteConnectionTypeManager = remoteConnectionTypeManager;
    }

    @Override // com.ua.sdk.actigraphysettings.ActigraphySettingsManager
    public ActigraphySettings fetchActigraphySettings() throws UaException {
        return fetch(this.userManager.getCurrentUserRef());
    }

    @Override // com.ua.sdk.actigraphysettings.ActigraphySettingsManager
    public Request fetchActigraphySettings(FetchCallback<ActigraphySettings> callback) {
        return fetch(this.userManager.getCurrentUserRef(), callback);
    }

    @Override // com.ua.sdk.actigraphysettings.ActigraphySettingsManager
    public ActigraphySettings setSleepRecorderPriority(EntityRef<RemoteConnectionType> ref) throws UaException {
        if ((ref instanceof RemoteConnectionTypeRef) && ((RemoteConnectionTypeRef) ref).getRecorderTypeKey() != null && !((RemoteConnectionTypeRef) ref).getRecorderTypeKey().equals("")) {
            RemoteConnectionTypeRef remoteConnectionTypeRef = (RemoteConnectionTypeRef) ref;
            return create(buildPrioritySetting("sleep", remoteConnectionTypeRef));
        }
        RemoteConnectionType remoteConnectionType = this.remoteConnectionTypeManager.fetchRemoteConnectionType(ref);
        return create(buildPrioritySetting("sleep", remoteConnectionType));
    }

    @Override // com.ua.sdk.actigraphysettings.ActigraphySettingsManager
    public Request setSleepRecorderPriority(final EntityRef<RemoteConnectionType> ref, CreateCallback<ActigraphySettings> callback) {
        final CreateRequest<ActigraphySettings> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.actigraphysettings.ActigraphySettingsManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ActigraphySettings createdEntity = ActigraphySettingsManagerImpl.this.setSleepRecorderPriority(ref);
                    request.done(createdEntity, null);
                } catch (UaException e) {
                    UaLog.error("Failed to set sleep priority");
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.actigraphysettings.ActigraphySettingsManager
    public ActigraphySettings setActivityRecorderPriority(EntityRef<RemoteConnectionType> ref) throws UaException {
        if ((ref instanceof RemoteConnectionTypeRef) && ((RemoteConnectionTypeRef) ref).getRecorderTypeKey() != null && !((RemoteConnectionTypeRef) ref).getRecorderTypeKey().equals("")) {
            RemoteConnectionTypeRef remoteConnectionTypeRef = (RemoteConnectionTypeRef) ref;
            return create(buildPrioritySetting(Ride.ACTIVITY, remoteConnectionTypeRef));
        }
        RemoteConnectionType remoteConnectionType = this.remoteConnectionTypeManager.fetchRemoteConnectionType(ref);
        return create(buildPrioritySetting(Ride.ACTIVITY, remoteConnectionType));
    }

    @Override // com.ua.sdk.actigraphysettings.ActigraphySettingsManager
    public Request setActivityRecorderPriority(final EntityRef<RemoteConnectionType> ref, CreateCallback<ActigraphySettings> callback) {
        final CreateRequest<ActigraphySettings> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.actigraphysettings.ActigraphySettingsManagerImpl.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ActigraphySettings createdEntity = ActigraphySettingsManagerImpl.this.setActivityRecorderPriority(ref);
                    request.done(createdEntity, null);
                } catch (UaException e) {
                    UaLog.error("Failed to set activity priority");
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    private ActigraphySettings buildPrioritySetting(String type, RemoteConnectionType connectionType) {
        ActigraphySettingsImpl settings = new ActigraphySettingsImpl();
        List<String> priorities = new ArrayList<>();
        if (type.equals("sleep")) {
            priorities.add(connectionType.getRecorderTypeKey());
            settings.setSleepPriority(priorities);
        } else {
            priorities.add(connectionType.getRecorderTypeKey());
            settings.setActivityPriority(priorities);
        }
        return settings;
    }

    private ActigraphySettings buildPrioritySetting(String type, RemoteConnectionTypeRef ref) {
        ActigraphySettingsImpl settings = new ActigraphySettingsImpl();
        List<String> priorities = new ArrayList<>();
        if (type.equals("sleep")) {
            priorities.add(ref.getRecorderTypeKey());
            settings.setSleepPriority(priorities);
        } else {
            priorities.add(ref.getRecorderTypeKey());
            settings.setActivityPriority(priorities);
        }
        return settings;
    }
}
