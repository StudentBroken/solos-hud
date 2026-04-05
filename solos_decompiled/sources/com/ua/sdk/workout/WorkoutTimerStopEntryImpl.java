package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutTimerStopEntryImpl implements WorkoutTimerStopEntry {
    public static final Parcelable.Creator<WorkoutTimerStopEntryImpl> CREATOR = new Parcelable.Creator<WorkoutTimerStopEntryImpl>() { // from class: com.ua.sdk.workout.WorkoutTimerStopEntryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutTimerStopEntryImpl createFromParcel(Parcel source) {
            return new WorkoutTimerStopEntryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutTimerStopEntryImpl[] newArray(int size) {
            return new WorkoutTimerStopEntryImpl[size];
        }
    };
    private double offset;
    private double stoppedTime;

    public WorkoutTimerStopEntryImpl(Double offset, Double stoppedTime) {
        this.offset = offset.doubleValue();
        this.stoppedTime = stoppedTime.doubleValue();
    }

    @Override // com.ua.sdk.workout.WorkoutTimerStopEntry
    public double getStoppedTime() {
        return this.stoppedTime;
    }

    @Override // com.ua.sdk.workout.BaseTimeSeriesEntry
    public double getOffset() {
        return this.offset;
    }

    @Override // com.ua.sdk.workout.BaseTimeSeriesEntry
    public Date getTime() {
        return null;
    }

    @Override // com.ua.sdk.workout.BaseTimeSeriesEntry
    public Long getTimeInMillis() {
        return null;
    }

    @Override // java.lang.Comparable
    public int compareTo(WorkoutTimerStopEntry another) {
        return Double.compare(this.offset, another.getOffset());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Double.valueOf(this.offset));
        dest.writeValue(Double.valueOf(this.stoppedTime));
    }

    private WorkoutTimerStopEntryImpl(Parcel in) {
        this.offset = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
        this.stoppedTime = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkoutTimerStopEntryImpl that = (WorkoutTimerStopEntryImpl) o;
        return Double.compare(that.offset, this.offset) == 0 && Double.compare(that.stoppedTime, this.stoppedTime) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.offset);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.stoppedTime);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }
}
