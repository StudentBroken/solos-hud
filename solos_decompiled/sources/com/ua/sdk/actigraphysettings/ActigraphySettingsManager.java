package com.ua.sdk.actigraphysettings;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.remoteconnection.RemoteConnectionType;
import com.ua.sdk.remoteconnection.RemoteConnectionTypeManager;

/* JADX INFO: loaded from: classes65.dex */
public interface ActigraphySettingsManager {
    Request fetchActigraphySettings(FetchCallback<ActigraphySettings> fetchCallback);

    ActigraphySettings fetchActigraphySettings() throws UaException;

    Request setActivityRecorderPriority(EntityRef<RemoteConnectionType> entityRef, CreateCallback<ActigraphySettings> createCallback);

    ActigraphySettings setActivityRecorderPriority(EntityRef<RemoteConnectionType> entityRef) throws UaException;

    void setRemoteConnectionTypeManager(RemoteConnectionTypeManager remoteConnectionTypeManager);

    Request setSleepRecorderPriority(EntityRef<RemoteConnectionType> entityRef, CreateCallback<ActigraphySettings> createCallback);

    ActigraphySettings setSleepRecorderPriority(EntityRef<RemoteConnectionType> entityRef) throws UaException;
}
