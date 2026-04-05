package com.ua.sdk.activitystory;

import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryActor extends Parcelable {

    public enum Type {
        USER,
        SITE,
        BRAND,
        PAGE,
        GROUP,
        UNKNOWN
    }

    String getId();

    Type getType();
}
