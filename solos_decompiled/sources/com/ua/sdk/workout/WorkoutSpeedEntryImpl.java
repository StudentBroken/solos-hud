package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutSpeedEntryImpl implements WorkoutSpeedEntry {
    public static final Parcelable.Creator<WorkoutSpeedEntryImpl> CREATOR = new Parcelable.Creator<WorkoutSpeedEntryImpl>() { // from class: com.ua.sdk.workout.WorkoutSpeedEntryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutSpeedEntryImpl createFromParcel(Parcel source) {
            return new WorkoutSpeedEntryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutSpeedEntryImpl[] newArray(int size) {
            return new WorkoutSpeedEntryImpl[size];
        }
    };
    private double offset;
    private double speed;

    public WorkoutSpeedEntryImpl(Double offset, Double speed) {
        this.offset = offset.doubleValue();
        this.speed = speed.doubleValue();
    }

    @Override // com.ua.sdk.workout.WorkoutSpeedEntry
    public double getInstantaneousSpeed() {
        return this.speed;
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
    public int compareTo(WorkoutSpeedEntry another) {
        return Double.compare(this.offset, another.getOffset());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Double.valueOf(this.offset));
        dest.writeValue(Double.valueOf(this.speed));
    }

    private WorkoutSpeedEntryImpl(Parcel in) {
        this.offset = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
        this.speed = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkoutSpeedEntryImpl that = (WorkoutSpeedEntryImpl) o;
        return Double.compare(that.offset, this.offset) == 0 && Double.compare(that.speed, this.speed) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.offset);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.speed);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }
}
