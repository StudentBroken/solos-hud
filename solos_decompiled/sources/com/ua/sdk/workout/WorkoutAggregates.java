package com.ua.sdk.workout;

import android.os.Parcelable;
import com.ua.sdk.heartrate.HeartRateZones;

/* JADX INFO: loaded from: classes65.dex */
public interface WorkoutAggregates extends Parcelable {
    Double getActiveTimeTotal();

    Integer getCadenceAvg();

    Integer getCadenceMax();

    Integer getCadenceMin();

    Double getDistanceTotal();

    Double getElapsedTimeTotal();

    Integer getHeartRateAvg();

    Integer getHeartRateMax();

    Integer getHeartRateMin();

    Double getIntensityAvg(HeartRateZones heartRateZones);

    Double getIntensityMax(HeartRateZones heartRateZones);

    Double getIntensityMin(HeartRateZones heartRateZones);

    Double getMetabolicEnergyTotal();

    Double getPowerAvg();

    Double getPowerMax();

    Double getPowerMin();

    Double getSpeedAvg();

    Double getSpeedMax();

    Double getSpeedMin();

    Integer getStepsTotal();

    Double getTorqueAvg();

    Double getTorqueMax();

    Double getTorqueMin();

    Double getWillPower();
}
