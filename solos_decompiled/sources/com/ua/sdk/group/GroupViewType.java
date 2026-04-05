package com.ua.sdk.group;

/* JADX INFO: loaded from: classes65.dex */
public enum GroupViewType {
    MEMBER("member"),
    INVITED("invited"),
    COMPLETED("completed"),
    IN_PROGRESS("in_progress"),
    ALL("all");

    private final String name;

    GroupViewType(String name) {
        this.name = name;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.name;
    }
}
