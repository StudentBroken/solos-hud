package com.ua.sdk.activitystory;

import com.ua.sdk.EntityRef;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.activitystory.ActivityStoryActor;
import com.ua.sdk.page.Page;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryPageActor extends ActivityStoryActor {
    String getAlias();

    ImageUrl getCoverPhoto();

    EntityRef<Page> getPageRef();

    ImageUrl getProfilePhoto();

    String getTitle();

    @Override // com.ua.sdk.activitystory.ActivityStoryActor
    ActivityStoryActor.Type getType();
}
