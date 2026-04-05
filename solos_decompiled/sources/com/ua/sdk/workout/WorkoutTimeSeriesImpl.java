package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutTimeSeriesImpl implements TimeSeriesData {
    public static final Parcelable.Creator<WorkoutTimeSeriesImpl> CREATOR = new Parcelable.Creator<WorkoutTimeSeriesImpl>() { // from class: com.ua.sdk.workout.WorkoutTimeSeriesImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutTimeSeriesImpl createFromParcel(Parcel source) {
            return new WorkoutTimeSeriesImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutTimeSeriesImpl[] newArray(int size) {
            return new WorkoutTimeSeriesImpl[size];
        }
    };
    TimeSeriesImpl<WorkoutCadenceEntry> workoutCadenceEntryTimeSeries;
    TimeSeriesImpl<WorkoutDistanceEntry> workoutDistanceTimeSeries;
    TimeSeriesImpl<WorkoutHeartRateEntry> workoutHeartRateEntryTimeSeries;
    TimeSeriesImpl<WorkoutPositionEntry> workoutPositionEntryTimeSeries;
    TimeSeriesImpl<WorkoutPowerEntry> workoutPowerEntryTimeSeries;
    TimeSeriesImpl<WorkoutSpeedEntry> workoutSpeedEntryTimeSeries;
    TimeSeriesImpl<WorkoutStepsEntry> workoutStepsEntryTimeSeries;
    TimeSeriesImpl<WorkoutTimerStopEntry> workoutStopTimeEntryTimeSeries;
    TimeSeriesImpl<WorkoutTorqueEntry> workoutTorqueEntryTimeSeries;

    public WorkoutTimeSeriesImpl() {
    }

    @Override // com.ua.sdk.workout.TimeSeriesData
    public TimeSeries<WorkoutHeartRateEntry> getHeartRateTimeSeries() {
        return this.workoutHeartRateEntryTimeSeries;
    }

    @Override // com.ua.sdk.workout.TimeSeriesData
    public TimeSeries<WorkoutSpeedEntry> getSpeedTimeSeries() {
        return this.workoutSpeedEntryTimeSeries;
    }

    @Override // com.ua.sdk.workout.TimeSeriesData
    public TimeSeries<WorkoutCadenceEntry> getCadenceTimeSeries() {
        return this.workoutCadenceEntryTimeSeries;
    }

    @Override // com.ua.sdk.workout.TimeSeriesData
    public TimeSeries<WorkoutPowerEntry> getPowerTimeSeries() {
        return this.workoutPowerEntryTimeSeries;
    }

    @Override // com.ua.sdk.workout.TimeSeriesData
    public TimeSeries<WorkoutTorqueEntry> getTorqueTimeSeries() {
        return this.workoutTorqueEntryTimeSeries;
    }

    @Override // com.ua.sdk.workout.TimeSeriesData
    public TimeSeries<WorkoutDistanceEntry> getDistanceTimeSeries() {
        return this.workoutDistanceTimeSeries;
    }

    @Override // com.ua.sdk.workout.TimeSeriesData
    public TimeSeries<WorkoutStepsEntry> getStepsTimeSeries() {
        return this.workoutStepsEntryTimeSeries;
    }

    @Override // com.ua.sdk.workout.TimeSeriesData
    public TimeSeries<WorkoutPositionEntry> getPositionTimeSeries() {
        return this.workoutPositionEntryTimeSeries;
    }

    @Override // com.ua.sdk.workout.TimeSeriesData
    public TimeSeries<WorkoutTimerStopEntry> getTimerStopTimeSeries() {
        return this.workoutStopTimeEntryTimeSeries;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.workoutHeartRateEntryTimeSeries, flags);
        dest.writeParcelable(this.workoutSpeedEntryTimeSeries, flags);
        dest.writeParcelable(this.workoutCadenceEntryTimeSeries, flags);
        dest.writeParcelable(this.workoutPowerEntryTimeSeries, flags);
        dest.writeParcelable(this.workoutTorqueEntryTimeSeries, flags);
        dest.writeParcelable(this.workoutDistanceTimeSeries, flags);
        dest.writeParcelable(this.workoutStepsEntryTimeSeries, flags);
        dest.writeParcelable(this.workoutPositionEntryTimeSeries, flags);
        dest.writeParcelable(this.workoutStopTimeEntryTimeSeries, flags);
    }

    private WorkoutTimeSeriesImpl(Parcel in) {
        this.workoutHeartRateEntryTimeSeries = (TimeSeriesImpl) in.readParcelable(TimeSeriesImpl.class.getClassLoader());
        this.workoutSpeedEntryTimeSeries = (TimeSeriesImpl) in.readParcelable(TimeSeriesImpl.class.getClassLoader());
        this.workoutCadenceEntryTimeSeries = (TimeSeriesImpl) in.readParcelable(TimeSeriesImpl.class.getClassLoader());
        this.workoutPowerEntryTimeSeries = (TimeSeriesImpl) in.readParcelable(TimeSeriesImpl.class.getClassLoader());
        this.workoutTorqueEntryTimeSeries = (TimeSeriesImpl) in.readParcelable(TimeSeriesImpl.class.getClassLoader());
        this.workoutDistanceTimeSeries = (TimeSeriesImpl) in.readParcelable(TimeSeriesImpl.class.getClassLoader());
        this.workoutStepsEntryTimeSeries = (TimeSeriesImpl) in.readParcelable(TimeSeriesImpl.class.getClassLoader());
        this.workoutPositionEntryTimeSeries = (TimeSeriesImpl) in.readParcelable(TimeSeriesImpl.class.getClassLoader());
        this.workoutStopTimeEntryTimeSeries = (TimeSeriesImpl) in.readParcelable(TimeSeriesImpl.class.getClassLoader());
    }
}
