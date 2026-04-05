package com.kopin.solos.common.permission;

/* JADX INFO: loaded from: classes52.dex */
public enum Permission {
    ACCESS_FINE_LOCATION("android.permission.ACCESS_FINE_LOCATION"),
    ACCESS_COARSE_LOCATION("android.permission.ACCESS_COARSE_LOCATION"),
    CAMERA("android.permission.CAMERA"),
    READ_CONTACTS("android.permission.READ_CONTACTS"),
    READ_EXTERNAL_STORAGE("android.permission.READ_EXTERNAL_STORAGE"),
    CALL_PHONE("android.permission.CALL_PHONE"),
    RECORD_AUDIO("android.permission.RECORD_AUDIO"),
    RECEIVE_SMS("android.permission.RECEIVE_SMS"),
    SEND_SMS("android.permission.SEND_SMS"),
    READ_CALL_LOG("android.permission.READ_CALL_LOG");

    String manifestPermission;

    Permission(String permission) {
        this.manifestPermission = permission;
    }

    public static Permission getPermission(int id) {
        if (id < 0 || id >= values().length) {
            return null;
        }
        return values()[id];
    }
}
