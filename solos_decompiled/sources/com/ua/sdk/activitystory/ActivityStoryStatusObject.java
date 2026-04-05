package com.ua.sdk.activitystory;

import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.privacy.Privacy;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryStatusObject extends ActivityStoryObject {
    Privacy getPrivacy();

    String getText();

    @Override // com.ua.sdk.activitystory.ActivityStoryObject
    ActivityStoryObject.Type getType();
}
