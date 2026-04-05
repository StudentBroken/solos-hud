package com.ua.sdk.remoteconnection;

import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.Link;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionManagerDummyImpl implements RemoteConnectionManager {
    private Map<Long, RemoteConnectionImpl> connectionMap = new HashMap();
    private Random random = new Random();

    public RemoteConnectionManagerDummyImpl() {
        long id;
        for (int i = 0; i < 14; i++) {
            do {
                id = this.random.nextLong();
            } while (this.connectionMap.containsKey(Long.valueOf(id)));
            this.connectionMap.put(Long.valueOf(id), createRemoteConnection(id));
        }
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public RemoteConnectionPageImpl fetchRemoteConnectionList() throws UaException {
        RemoteConnectionPageImpl remoteConnectionPage = new RemoteConnectionPageImpl();
        for (Long id : this.connectionMap.keySet()) {
            remoteConnectionPage.add(this.connectionMap.get(id));
        }
        return remoteConnectionPage;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public Request fetchRemoteConnectionList(FetchCallback<EntityList<RemoteConnection>> callback) {
        return null;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public RemoteConnection fetchRemoteConnection(EntityRef<RemoteConnection> ref) throws UaException {
        for (RemoteConnectionImpl rci : this.connectionMap.values()) {
            if (rci.getLinks("self").get(0).getId().equals(ref.getId())) {
                return rci;
            }
        }
        return null;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public Request fetchRemoteConnection(EntityRef<RemoteConnection> ref, FetchCallback<RemoteConnection> callback) {
        return null;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public void deleteRemoteConnection(EntityRef<RemoteConnection> ref) throws UaException {
        for (Long key : this.connectionMap.keySet()) {
            if (this.connectionMap.get(key).getLinks("self").get(0).getId().equals(ref.getId())) {
                this.connectionMap.remove(key);
                return;
            }
        }
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionManager
    public Request deleteRemoteConnection(EntityRef<RemoteConnection> ref, DeleteCallback<RemoteConnectionRef> callback) {
        return null;
    }

    public RemoteConnectionImpl createRemoteConnection(long id) {
        long syncDate;
        RemoteConnectionImpl rc = new RemoteConnectionImpl();
        ArrayList<Link> links = new ArrayList<>();
        long createDate = Math.abs(this.random.nextLong());
        rc.setCreatedDateTime(new Date(createDate));
        do {
            syncDate = Math.abs(this.random.nextLong());
        } while (syncDate < createDate);
        rc.setLastSyncTime(new Date(syncDate));
        rc.setType(this.random.nextBoolean() ? "jawboneup" : "withings");
        links.add(new Link("/vx/remoteconnection/" + id + "/", String.valueOf(id)));
        rc.setLinksForRelation("self", links);
        int userId = Math.abs(this.random.nextInt());
        links.add(new Link("/v7.0/user/" + userId + "/", String.valueOf(userId)));
        rc.setLinksForRelation("user", links);
        return rc;
    }

    public void addConnection(RemoteConnectionImpl rci) {
        this.connectionMap.put(Long.valueOf(rci.getLinks("self").get(0).getId()), rci);
    }
}
