package com.kopin.solos.storage.util;

import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.SavedWorkout;

/* JADX INFO: loaded from: classes54.dex */
public class TrainingStressScore {
    public static int TSS_MIN = 0;
    public static int TSS_MAX = 1000;

    public static double calculate(long rideMillis, double normalisedPower, double intensityFactor, double functionalThresholdPower) {
        if (functionalThresholdPower <= 0.0d) {
            return -2.147483648E9d;
        }
        long rideSeconds = rideMillis / 1000;
        return (((rideSeconds * normalisedPower) * intensityFactor) / (3600.0d * functionalThresholdPower)) * 100.0d;
    }

    public static double calculate(IRidePartSaved ridePart) {
        if (!(ridePart instanceof SavedWorkout)) {
            return -2.147483648E9d;
        }
        SavedWorkout ride = (SavedWorkout) ridePart;
        return calculate(ride.getDuration(), ride.getAverageNormalisedPower(), ride.getAverageIntensity(), ride.getFunctionalThresholdPower());
    }
}
