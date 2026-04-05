package com.ua.sdk.activitystory;

import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryTarget extends Parcelable {

    public enum Type {
        GROUP,
        UNKNOWN
    }

    String getId();

    Type getType();
}
