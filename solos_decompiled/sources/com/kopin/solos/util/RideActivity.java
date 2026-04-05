package com.kopin.solos.util;

import android.content.Context;
import com.kopin.solos.share.R;

/* JADX INFO: loaded from: classes4.dex */
public class RideActivity {

    public enum RideType {
        Cycle_Speedway(R.string.activity_type_cycle_speedway, R.drawable.cycle_speedway),
        Cyclo_Cross(R.string.activity_type_cyclo_cross, R.drawable.cyclo_cross),
        Mountain_Practice(R.string.activity_type_mountain_practice, R.drawable.mountain_practice),
        Mountain_Race(R.string.activity_type_mountain_race, R.drawable.mountain_race),
        Road_Practice(R.string.activity_type_road_practice, R.drawable.road_practice),
        Road_Race(R.string.activity_type_road_race, R.drawable.road_race),
        Track_Practice(R.string.activity_type_track_practice, R.drawable.track_practice),
        Track_Race(R.string.activity_type_track_race, R.drawable.track_race);

        int mIcon;
        int mLabel;

        RideType(int label, int icon) {
            this.mLabel = label;
            this.mIcon = icon;
        }

        public static int getIcon(int index) {
            if (index < 0 || index >= values().length) {
                return 0;
            }
            return values()[index].mIcon;
        }

        public static int getLabel(int index) {
            if (index < 0 || index >= values().length) {
                return 0;
            }
            return values()[index].mLabel;
        }

        public static String[] getStringLabels(Context context) {
            String[] labels = new String[values().length];
            int i = 0;
            for (RideType rideType : values()) {
                labels[i] = context.getString(rideType.mLabel);
                i++;
            }
            return labels;
        }

        public static RideType getRideType(String s) {
            for (RideType rideType : values()) {
                if (rideType.name().equalsIgnoreCase(s)) {
                    return rideType;
                }
            }
            return Road_Practice;
        }

        public static RideType getRideType(int index) {
            return (index < 0 || index >= values().length) ? Road_Practice : values()[index];
        }

        public static int getIndex(Context context, String s) {
            String[] labels = getStringLabels(context);
            int i = 0;
            for (String label : labels) {
                if (!label.equalsIgnoreCase(s)) {
                    i++;
                } else {
                    return i;
                }
            }
            return -1;
        }
    }
}
