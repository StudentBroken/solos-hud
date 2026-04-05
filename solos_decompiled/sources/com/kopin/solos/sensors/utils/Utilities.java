package com.kopin.solos.sensors.utils;

/* JADX INFO: loaded from: classes28.dex */
public class Utilities {
    private static double KM_PER_MILE = 0.621371d;

    public static double convertMetersToKilometers(double distance) {
        return distance / 1000.0d;
    }

    public static double convertMetersToMiles(double distance) {
        return (KM_PER_MILE / 1000.0d) * distance;
    }
}
