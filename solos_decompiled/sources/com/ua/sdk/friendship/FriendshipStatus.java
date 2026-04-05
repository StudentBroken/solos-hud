package com.ua.sdk.friendship;

/* JADX INFO: loaded from: classes65.dex */
public enum FriendshipStatus {
    NONE("none"),
    PENDING("pending"),
    ACTIVE("active");

    private String value;

    FriendshipStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static FriendshipStatus getStatusFromString(String value) {
        FriendshipStatus[] arr$ = values();
        for (FriendshipStatus status : arr$) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
