package com.kopin.peloton.groupcom;

/* JADX INFO: loaded from: classes61.dex */
public class ChatGroup {
    public String GroupId;
    public String GroupName;
    public SessionState sessionState = SessionState.NONE;
    public long sessionTime;

    public enum SessionState {
        NONE,
        DISCONNECTED,
        RECONNECTING,
        CONNECTING,
        CONNECTED
    }

    public String toString() {
        return this.GroupName;
    }
}
