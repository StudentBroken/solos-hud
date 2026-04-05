package com.ua.sdk.workout;

import android.content.Context;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutNameGeneratorImpl implements WorkoutNameGenerator {
    @Override // com.ua.sdk.workout.WorkoutNameGenerator
    public String generateName(User user, ActivityType activityType, Context context, Date startDate, Double distanceMeters) {
        Precondition.isNotNull(activityType, "activity type");
        return activityType.getName();
    }
}
