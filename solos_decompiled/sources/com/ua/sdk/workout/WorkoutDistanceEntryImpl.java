package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutDistanceEntryImpl implements WorkoutDistanceEntry {
    public static final Parcelable.Creator<WorkoutDistanceEntryImpl> CREATOR = new Parcelable.Creator<WorkoutDistanceEntryImpl>() { // from class: com.ua.sdk.workout.WorkoutDistanceEntryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutDistanceEntryImpl createFromParcel(Parcel source) {
            return new WorkoutDistanceEntryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutDistanceEntryImpl[] newArray(int size) {
            return new WorkoutDistanceEntryImpl[size];
        }
    };
    double distance;
    double offset;

    public WorkoutDistanceEntryImpl(Double offset, Double distance) {
        this.offset = offset.doubleValue();
        this.distance = distance.doubleValue();
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

    @Override // com.ua.sdk.workout.WorkoutDistanceEntry
    public double getDistance() {
        return this.distance;
    }

    @Override // java.lang.Comparable
    public int compareTo(WorkoutDistanceEntry another) {
        return Double.compare(this.offset, another.getOffset());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Double.valueOf(this.offset));
        dest.writeValue(Double.valueOf(this.distance));
    }

    private WorkoutDistanceEntryImpl(Parcel in) {
        this.offset = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
        this.distance = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkoutDistanceEntryImpl that = (WorkoutDistanceEntryImpl) o;
        return Double.compare(that.distance, this.distance) == 0 && Double.compare(that.offset, this.offset) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.offset);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.distance);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }
}
