package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutPowerEntryImpl implements WorkoutPowerEntry {
    public static final Parcelable.Creator<WorkoutPowerEntryImpl> CREATOR = new Parcelable.Creator<WorkoutPowerEntryImpl>() { // from class: com.ua.sdk.workout.WorkoutPowerEntryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutPowerEntryImpl createFromParcel(Parcel source) {
            return new WorkoutPowerEntryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutPowerEntryImpl[] newArray(int size) {
            return new WorkoutPowerEntryImpl[size];
        }
    };
    private double offset;
    private double power;

    public WorkoutPowerEntryImpl(Double offset, Double power) {
        this.offset = offset.doubleValue();
        this.power = power.doubleValue();
    }

    @Override // com.ua.sdk.workout.WorkoutPowerEntry
    public double getInstantaneousPower() {
        return this.power;
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
    public int compareTo(WorkoutPowerEntry another) {
        return Double.compare(this.offset, another.getOffset());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Double.valueOf(this.offset));
        dest.writeValue(Double.valueOf(this.power));
    }

    private WorkoutPowerEntryImpl(Parcel in) {
        this.offset = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
        this.power = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkoutPowerEntryImpl that = (WorkoutPowerEntryImpl) o;
        return Double.compare(that.offset, this.offset) == 0 && Double.compare(that.power, this.power) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.offset);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.power);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }
}
