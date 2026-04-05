package com.ua.sdk.activitystory;

import com.ua.sdk.activitystory.ActivityStoryObject;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryToutObject extends ActivityStoryObject {

    public enum Subtype {
        FIND_FRIENDS
    }

    Subtype getSubtype();

    @Override // com.ua.sdk.activitystory.ActivityStoryObject
    ActivityStoryObject.Type getType();
}
