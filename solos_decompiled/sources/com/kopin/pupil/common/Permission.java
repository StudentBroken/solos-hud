package com.kopin.pupil.common;

/* JADX INFO: loaded from: classes25.dex */
public enum Permission {
    ACCESS_FINE_LOCATION("android.permission.ACCESS_FINE_LOCATION"),
    ACCESS_COARSE_LOCATION("android.permission.ACCESS_COARSE_LOCATION"),
    CAMERA("android.permission.CAMERA"),
    READ_CONTACTS("android.permission.READ_CONTACTS"),
    READ_EXTERNAL_STORAGE("android.permission.READ_EXTERNAL_STORAGE"),
    RECORD_AUDIO("android.permission.RECORD_AUDIO"),
    CALL_PHONE("android.permission.CALL_PHONE"),
    RECEIVE_SMS("android.permission.RECEIVE_SMS"),
    SEND_SMS("android.permission.SEND_SMS");

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
