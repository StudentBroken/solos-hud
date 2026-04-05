package com.ua.sdk.workout;

import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public interface TimeSeriesData extends Parcelable {
    TimeSeries<WorkoutCadenceEntry> getCadenceTimeSeries();

    TimeSeries<WorkoutDistanceEntry> getDistanceTimeSeries();

    TimeSeries<WorkoutHeartRateEntry> getHeartRateTimeSeries();

    TimeSeries<WorkoutPositionEntry> getPositionTimeSeries();

    TimeSeries<WorkoutPowerEntry> getPowerTimeSeries();

    TimeSeries<WorkoutSpeedEntry> getSpeedTimeSeries();

    TimeSeries<WorkoutStepsEntry> getStepsTimeSeries();

    TimeSeries<WorkoutTimerStopEntry> getTimerStopTimeSeries();

    TimeSeries<WorkoutTorqueEntry> getTorqueTimeSeries();
}
