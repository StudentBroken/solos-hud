package com.kopin.solos.storage.util;

/* JADX INFO: loaded from: classes54.dex */
public class CalorieCounter {
    private static double expTrendVal(double speed) {
        return Math.exp(0.09225989878177643d * speed) * 0.014084751717746258d;
    }

    public static int calculate(double riderWeightInKg, double bikeWeightInKg, long time, double avgSpeedMPS, float altitudeGainMeters) {
        if (riderWeightInKg <= 0.0d || time < 1000 || avgSpeedMPS <= 0.0d) {
            return 0;
        }
        double riderWeightPounds = Utility.kilogramsToPounds(riderWeightInKg);
        double bikeWeightPounds = Utility.kilogramsToPounds(bikeWeightInKg);
        double avgSpeedInMph = Conversion.metresPerSecondToMph(avgSpeedMPS);
        float gainInFeet = altitudeGainMeters == -2.14748365E9f ? 0.0f : Utility.metersToFeet(altitudeGainMeters);
        double coefficient = expTrendVal(avgSpeedInMph);
        double minutes = (time / 1000.0f) / 60.0f;
        double calories = (riderWeightPounds + bikeWeightPounds) * coefficient * minutes;
        return (int) (calories + (((double) (gainInFeet / 100.0f)) * (22.0d / (60.0d * minutes))));
    }

    public static int calculate(double distance, double weightInKg) {
        double distanceInKm = distance * 0.001d;
        return (int) (distanceInKm * weightInKg * 1.036d);
    }
}
