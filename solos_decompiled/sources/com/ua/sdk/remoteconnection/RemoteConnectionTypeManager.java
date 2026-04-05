package com.ua.sdk.remoteconnection;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.actigraphysettings.ActigraphySettings;

/* JADX INFO: loaded from: classes65.dex */
public interface RemoteConnectionTypeManager {
    Request fetchRemoteConnectionPriorities(FetchCallback<ActigraphySettings> fetchCallback);

    ActigraphySettings fetchRemoteConnectionPriorities() throws UaException;

    Request fetchRemoteConnectionType(EntityRef<RemoteConnectionType> entityRef, FetchCallback<RemoteConnectionType> fetchCallback);

    RemoteConnectionType fetchRemoteConnectionType(EntityRef<RemoteConnectionType> entityRef) throws UaException;

    EntityList<RemoteConnectionType> fetchRemoteConnectionTypeList() throws UaException;

    Request fetchRemoteConnectionTypeList(FetchCallback<EntityList<RemoteConnectionType>> fetchCallback);

    Request updateActivityConnectionPriorities(EntityRef<RemoteConnectionType> entityRef, CreateCallback<ActigraphySettings> createCallback);

    void updateActivityConnectionPriorities(EntityRef<RemoteConnectionType> entityRef) throws UaException;

    Request updateSleepConnectionPriorities(EntityRef<RemoteConnectionType> entityRef, CreateCallback<ActigraphySettings> createCallback);

    void updateSleepConnectionPriorities(EntityRef<RemoteConnectionType> entityRef) throws UaException;
}
