package com.ua.sdk.activitystory;

import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.privacy.Privacy;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryActigraphyObject extends ActivityStoryObject {
    Date getEndTime();

    List<ActivityStoryHighlight> getHighlights();

    Privacy getPrivacy();

    Date getPublishedTime();

    Date getStartTime();

    Integer getSteps();

    @Override // com.ua.sdk.activitystory.ActivityStoryObject
    ActivityStoryObject.Type getType();
}
