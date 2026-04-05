package com.kopin.solos.wear;

/* JADX INFO: loaded from: classes59.dex */
public enum MessageType {
    NONE,
    START_APP,
    WATCH_IN_WATCH_MODE,
    MESSAGE,
    IS_WATCH_APP_INSTALLED,
    WATCH_APP_IS_INSTALLED,
    TRANSFER_INIT,
    TRANSFER_IN_RIDE,
    TRANSFER_COMMAND_RECEIVED,
    TRANSFER,
    TRANSFER_SENT,
    WATCH_CONNECTIONS_DONE,
    DISCONNECT_SENSORS,
    DISCONNECT_SENSORS_FORCE,
    DISCONNECT_CONFIRMED;

    public static MessageType fromString(String path) {
        for (MessageType messageType : values()) {
            if (messageType.name().equalsIgnoreCase(path)) {
                return messageType;
            }
        }
        return NONE;
    }
}
