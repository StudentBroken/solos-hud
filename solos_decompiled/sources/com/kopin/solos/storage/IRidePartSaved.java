package com.kopin.solos.storage;

import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SavedWorkout;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public interface IRidePartSaved extends IRidePartData {
    void foreachMetric(Record.MetricData metricData, int i, SavedWorkout.foreachMetricCallback foreachmetriccallback);

    void foreachRecord(int i, SavedWorkout.foreachRecordCallback foreachrecordcallback);

    double getAverageIntensity();

    double getAverageNormalisedPower();

    double getAveragePace();

    double getAveragePaceForLocale();

    double getAverageSpeedForLocale();

    double getAverageStride();

    double getAverageStrideForLocale();

    double getDistanceForLocale();

    long getDuration();

    float getGainedAltitude();

    float getGainedAltitudeForLocale();

    double getMaxAltitudeDiffForLocale();

    double getMaxCadence();

    int getMaxHeartrate();

    double getMaxIntensity();

    double getMaxNormalisedPower();

    int getMaxOxygen();

    double getMaxPace();

    double getMaxPaceForLocale();

    double getMaxPower();

    double getMaxSpeed();

    double getMaxSpeedForLocale();

    double getMaxStride();

    double getMaxStrideForLocale();

    int getMinOxygen();

    long getRideId();

    List<Coordinate> getRouteDetails();
}
