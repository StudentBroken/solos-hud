package com.kopin.solos.storage.util;

import android.content.Context;
import com.kopin.solos.storage.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public class HeartRateZones {
    private static final int[] heartRateZoneNames = {R.string.hr_zone1_name, R.string.hr_zone2_name, R.string.hr_zone3_name, R.string.hr_zone4_name, R.string.hr_zone5_name, R.string.hr_zone6_name};
    private static HeartRateZones self;
    private final String[] HEART_RATE_ZONE_NAMES = new String[heartRateZoneNames.length];
    private final int[] HEART_RATE_ZONE_THRESHOLDS;
    private int mMaxHR;

    private HeartRateZones(Context context) {
        for (int i = 0; i < heartRateZoneNames.length; i++) {
            this.HEART_RATE_ZONE_NAMES[i] = context.getString(heartRateZoneNames[i]);
        }
        this.HEART_RATE_ZONE_THRESHOLDS = context.getResources().getIntArray(R.array.HrZonePrecentages);
    }

    public static void init(Context context) {
        self = new HeartRateZones(context);
    }

    public static int setFromAge(int age) {
        setMaxHR(220 - age);
        return self.mMaxHR;
    }

    public static void setMaxHR(int max) {
        self.mMaxHR = max;
    }

    public static int computeHeartRateZone(int heartRate) {
        if (self.mMaxHR <= 0) {
            return 1;
        }
        int percent = (heartRate * 100) / self.mMaxHR;
        int z = 0;
        while (z < self.HEART_RATE_ZONE_THRESHOLDS.length && percent >= self.HEART_RATE_ZONE_THRESHOLDS[z]) {
            z++;
        }
        return z + 1;
    }

    public static List<Integer> getHeartRateZones() {
        List<Integer> data = new ArrayList<>();
        data.add(0);
        for (int t : self.HEART_RATE_ZONE_THRESHOLDS) {
            data.add(Integer.valueOf((self.mMaxHR * t) / 100));
        }
        return data;
    }

    public static String getHeartRateZoneName(int heartRateZone) {
        return (heartRateZone < 1 || heartRateZone > 6) ? "" : self.HEART_RATE_ZONE_NAMES[heartRateZone - 1];
    }
}
