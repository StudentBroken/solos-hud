package com.ua.sdk.activitystory;

import com.kopin.solos.analytics.Analytics;

/* JADX INFO: loaded from: classes65.dex */
public enum ActivityStoryType {
    PUBLIC("public", false),
    USER("user", true),
    PAGE("page", true),
    GROUP("group", true),
    REPLY("reply", true),
    WORKOUT(Analytics.Events.WORKOUT, true);

    private final boolean idRequired;
    private final String value;

    ActivityStoryType(String value, boolean idRequired) {
        this.value = value;
        this.idRequired = idRequired;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.value;
    }

    public boolean isIdRequired() {
        return this.idRequired;
    }
}
