package com.kopin.solos.util;

import com.kopin.solos.share.R;

/* JADX INFO: loaded from: classes4.dex */
public enum RunType {
    Cross_Country(R.string.activity_type_cross_country, R.drawable.cross_country),
    Mountain(R.string.activity_type_mountain, R.drawable.mountain_run),
    Road_Practice(R.string.activity_type_road_practice, R.drawable.road_practice_run),
    Road_Race(R.string.activity_type_road_race, R.drawable.road_race),
    Time_Trial(R.string.activity_type_time_trial, R.drawable.time_trial),
    Track(R.string.activity_type_track, R.drawable.track_run),
    Treadmill(R.string.activity_type_treadmill, R.drawable.treadmill);

    int mIcon;
    int mLabel;
    public static RunType DEFAULT_TYPE = Road_Practice;
    public static int DEFAULT_TYPE_ID = DEFAULT_TYPE.ordinal();

    RunType(int label, int icon) {
        this.mLabel = label;
        this.mIcon = icon;
    }

    public static int getIcon(int id) {
        if (id < 0 || id >= values().length) {
            return 0;
        }
        return values()[id].mIcon;
    }

    public static int getLabel(int id) {
        if (id < 0 || id >= values().length) {
            return 0;
        }
        return values()[id].mLabel;
    }

    public static RunType getRunType(String s) {
        for (RunType rideType : values()) {
            if (rideType.name().equalsIgnoreCase(s)) {
                return rideType;
            }
        }
        return DEFAULT_TYPE;
    }

    public static RunType getRunType(int id) {
        return (id < 0 || id >= values().length) ? DEFAULT_TYPE : values()[id];
    }
}
