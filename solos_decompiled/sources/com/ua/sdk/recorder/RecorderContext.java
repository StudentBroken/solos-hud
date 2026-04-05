package com.ua.sdk.recorder;

import android.content.Context;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.heartrate.HeartRateZones;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class RecorderContext {
    private ActivityType activityType;
    private Context applicationContext;
    private RecorderClock clock;
    private HeartRateZones heartRateZones;
    private String name;
    private User user;

    public Context getApplicationContext() {
        return this.applicationContext;
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public RecorderClock getClock() {
        return this.clock;
    }

    public void setClock(RecorderClock clock) {
        this.clock = clock;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ActivityType getActivityType() {
        return this.activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public HeartRateZones getHeartRateZones() {
        return this.heartRateZones;
    }

    public void setHeartRateZones(HeartRateZones heartRateZones) {
        this.heartRateZones = heartRateZones;
    }
}
