package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutCadenceEntryImpl implements WorkoutCadenceEntry {
    public static final Parcelable.Creator<WorkoutCadenceEntryImpl> CREATOR = new Parcelable.Creator<WorkoutCadenceEntryImpl>() { // from class: com.ua.sdk.workout.WorkoutCadenceEntryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutCadenceEntryImpl createFromParcel(Parcel source) {
            return new WorkoutCadenceEntryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutCadenceEntryImpl[] newArray(int size) {
            return new WorkoutCadenceEntryImpl[size];
        }
    };
    private int cadence;
    private double offset;

    public WorkoutCadenceEntryImpl(Double offset, Integer cadence) {
        this.offset = offset.doubleValue();
        this.cadence = cadence.intValue();
    }

    @Override // com.ua.sdk.workout.WorkoutCadenceEntry
    public int getInstantaneousCadence() {
        return this.cadence;
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // java.lang.Comparable
    public int compareTo(WorkoutCadenceEntry another) {
        return Double.compare(this.offset, another.getOffset());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Double.valueOf(this.offset));
        dest.writeValue(Integer.valueOf(this.cadence));
    }

    private WorkoutCadenceEntryImpl(Parcel in) {
        this.offset = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
        this.cadence = ((Integer) in.readValue(Integer.class.getClassLoader())).intValue();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkoutCadenceEntryImpl that = (WorkoutCadenceEntryImpl) o;
        return this.cadence == that.cadence && Double.compare(that.offset, this.offset) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.offset);
        int result = (int) ((temp >>> 32) ^ temp);
        return (result * 31) + this.cadence;
    }
}
