package com.ua.sdk.workout;

import android.content.Context;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface WorkoutNameGenerator {
    String generateName(User user, ActivityType activityType, Context context, Date date, Double d);
}
