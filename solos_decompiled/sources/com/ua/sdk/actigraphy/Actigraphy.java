package com.ua.sdk.actigraphy;

import com.ua.sdk.EntityRef;
import com.ua.sdk.LocalDate;
import com.ua.sdk.Resource;
import com.ua.sdk.user.User;
import com.ua.sdk.workout.WorkoutSummary;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public interface Actigraphy extends Resource {
    ActigraphyAggregates getActigraphyAggregates();

    LocalDate getDate();

    Date getEndDateTime();

    ActigraphyMetrics getMetrics();

    Date getStartDateTime();

    TimeZone getTimeZone();

    EntityRef<User> getUserRef();

    WorkoutSummary[] getWorkoutSummaries();
}
