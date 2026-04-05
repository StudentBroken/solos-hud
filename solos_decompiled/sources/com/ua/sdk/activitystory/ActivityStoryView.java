package com.ua.sdk.activitystory;

import com.kopin.solos.analytics.Analytics;

/* JADX INFO: loaded from: classes65.dex */
public enum ActivityStoryView {
    PUBLIC_STATUS("status", ActivityStoryType.PUBLIC),
    PUBLIC_PHOTO("photo", ActivityStoryType.PUBLIC),
    PUBLIC_VIDEO("video", ActivityStoryType.PUBLIC),
    PUBLIC_WORKOUT(Analytics.Events.WORKOUT, ActivityStoryType.PUBLIC),
    USER_ME("me", ActivityStoryType.USER),
    PAGE_SELF("self", ActivityStoryType.PAGE),
    PAGE_FEATURED("featured", ActivityStoryType.PAGE);

    private final ActivityStoryType type;
    private final String value;

    ActivityStoryView(String value, ActivityStoryType type) {
        this.value = value;
        this.type = type;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.value;
    }

    public ActivityStoryType getActivityStoryType() {
        return this.type;
    }
}
