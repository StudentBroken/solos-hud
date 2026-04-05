package com.ua.sdk.internal;

import com.ua.sdk.IntensityCalculator;
import com.ua.sdk.heartrate.HeartRateZone;
import com.ua.sdk.heartrate.HeartRateZones;

/* JADX INFO: loaded from: classes65.dex */
public class IntensityCalculatorImpl implements IntensityCalculator {
    @Override // com.ua.sdk.IntensityCalculator
    public double calculateCurrentIntensity(HeartRateZones heartRateZones, double currentHeartRate) {
        if (heartRateZones == null) {
            return 0.0d;
        }
        double maxHeartRate = getMaxHr(heartRateZones) - 10.0d;
        if (maxHeartRate <= 0.0d) {
            return 0.0d;
        }
        double intensity = ((2.22d * currentHeartRate) / maxHeartRate) - 1.22d;
        return Math.max(0.0d, Math.min(100.0d, intensity * 100.0d));
    }

    private double getMaxHr(HeartRateZones heartRateZones) {
        HeartRateZone heartRateZone = heartRateZones.getZone(heartRateZones.getZones().size() - 1);
        return heartRateZone.getEnd();
    }
}
