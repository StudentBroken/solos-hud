package com.ua.sdk.remoteconnection;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityList;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.cache.EntityDatabase;
import com.ua.sdk.internal.ApiTransferObject;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionPageTO extends ApiTransferObject {
    public static final String KEY_REMOTE_CONNECTIONS = "remoteconnections";

    @SerializedName("_embedded")
    public Map<String, ArrayList<RemoteConnectionTransferObject>> remoteConnections;

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    public Integer totalRemoteConnectionsCount;

    private ArrayList<RemoteConnectionTransferObject> getRemoteConnectionList() {
        if (this.remoteConnections == null) {
            return null;
        }
        return this.remoteConnections.get(KEY_REMOTE_CONNECTIONS);
    }

    public static RemoteConnectionPageTO fromPage(EntityList<RemoteConnection> remoteConnectionPage) {
        if (remoteConnectionPage == null) {
            return null;
        }
        RemoteConnectionPageTO remoteConnectionPageTO = new RemoteConnectionPageTO();
        for (RemoteConnection remoteConnection : remoteConnectionPage.getAll()) {
            remoteConnectionPageTO.getRemoteConnectionList().add(RemoteConnectionTransferObject.fromRemoteConnection(remoteConnection));
        }
        if (remoteConnectionPage instanceof RemoteConnectionListImpl) {
            remoteConnectionPageTO.setLinkMap(((RemoteConnectionListImpl) remoteConnectionPage).getLinkMap());
        }
        remoteConnectionPageTO.totalRemoteConnectionsCount = Integer.valueOf(remoteConnectionPage.getTotalCount());
        return remoteConnectionPageTO;
    }

    public static RemoteConnectionListImpl toPage(RemoteConnectionPageTO to) {
        RemoteConnectionListImpl page = new RemoteConnectionListImpl();
        ArrayList<RemoteConnectionTransferObject> remoteConnectionTransferObjects = to.getRemoteConnectionList();
        for (RemoteConnectionTransferObject remoteConnectionTransferObject : remoteConnectionTransferObjects) {
            try {
                RemoteConnectionImpl remoteConnectionImpl = RemoteConnectionTransferObject.toRemoteConnectionImpl(remoteConnectionTransferObject);
                page.add(remoteConnectionImpl);
            } catch (UaException e) {
                UaLog.error("Error converting RemoteConnectionTransferObject to RemoteConnectionImpl.", (Throwable) e);
            }
        }
        page.setLinkMap(to.getLinkMap());
        page.setTotalCount(to.totalRemoteConnectionsCount.intValue());
        return page;
    }
}
