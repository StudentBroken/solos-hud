package com.opentok.android;

import java.util.Date;

/* JADX INFO: loaded from: classes15.dex */
public class Connection implements Comparable<Connection> {
    protected String connectionId;
    protected Date creationTime;
    protected String data;

    protected Connection(String connectionId, long creationTime, String data) {
        this.connectionId = connectionId;
        this.creationTime = new Date(creationTime);
        this.data = data;
    }

    public String getConnectionId() {
        return this.connectionId;
    }

    public String getData() {
        return this.data;
    }

    public Date getCreationTime() {
        return this.creationTime;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Connection)) {
            return false;
        }
        Connection conn = (Connection) obj;
        return getConnectionId().equals(conn.getConnectionId());
    }

    @Override // java.lang.Comparable
    public int compareTo(Connection another) {
        return getConnectionId().compareTo(another.getConnectionId());
    }

    public int hashCode() {
        return getConnectionId().hashCode();
    }
}
