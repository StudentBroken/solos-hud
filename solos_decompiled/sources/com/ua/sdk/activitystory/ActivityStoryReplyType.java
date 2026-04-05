package com.ua.sdk.activitystory;

/* JADX INFO: loaded from: classes65.dex */
public enum ActivityStoryReplyType {
    COMMENTS("comment"),
    LIKES("like"),
    REPOSTS("repost");

    private String value;

    ActivityStoryReplyType(String value) {
        this.value = value;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.value;
    }
}
