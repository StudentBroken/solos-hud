package com.ua.sdk.activitystory;

import com.ua.sdk.EntityRef;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.workout.WorkoutRef;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryWorkoutObject extends ActivityStoryObject {
    EntityRef<ActivityType> getActivityTypeRef();

    Double getAveragePace();

    Double getAverageSpeed();

    Double getDistance();

    long getDuration();

    Integer getEnergyBurned();

    List<ActivityStoryHighlight> getHighlights();

    String getNotes();

    Privacy getPrivacy();

    Integer getSteps();

    String getTitle();

    @Override // com.ua.sdk.activitystory.ActivityStoryObject
    ActivityStoryObject.Type getType();

    WorkoutRef getWorkoutRef();
}
