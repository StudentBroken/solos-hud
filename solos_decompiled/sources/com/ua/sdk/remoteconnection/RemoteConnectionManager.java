package com.ua.sdk.remoteconnection;

import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface RemoteConnectionManager {
    Request deleteRemoteConnection(EntityRef<RemoteConnection> entityRef, DeleteCallback<RemoteConnectionRef> deleteCallback);

    void deleteRemoteConnection(EntityRef<RemoteConnection> entityRef) throws UaException;

    Request fetchRemoteConnection(EntityRef<RemoteConnection> entityRef, FetchCallback<RemoteConnection> fetchCallback);

    RemoteConnection fetchRemoteConnection(EntityRef<RemoteConnection> entityRef) throws UaException;

    EntityList<RemoteConnection> fetchRemoteConnectionList() throws UaException;

    Request fetchRemoteConnectionList(FetchCallback<EntityList<RemoteConnection>> fetchCallback);
}
