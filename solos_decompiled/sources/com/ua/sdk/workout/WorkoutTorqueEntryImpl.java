package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutTorqueEntryImpl implements WorkoutTorqueEntry {
    public static final Parcelable.Creator<WorkoutTorqueEntryImpl> CREATOR = new Parcelable.Creator<WorkoutTorqueEntryImpl>() { // from class: com.ua.sdk.workout.WorkoutTorqueEntryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutTorqueEntryImpl createFromParcel(Parcel source) {
            return new WorkoutTorqueEntryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutTorqueEntryImpl[] newArray(int size) {
            return new WorkoutTorqueEntryImpl[size];
        }
    };
    private double offset;
    private double torque;

    public WorkoutTorqueEntryImpl(Double offset, Double torque) {
        this.offset = offset.doubleValue();
        this.torque = torque.doubleValue();
    }

    @Override // com.ua.sdk.workout.WorkoutTorqueEntry
    public double getInstantaneousTorque() {
        return this.torque;
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
    public int compareTo(WorkoutTorqueEntry another) {
        return Double.compare(this.offset, another.getOffset());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Double.valueOf(this.offset));
        dest.writeValue(Double.valueOf(this.torque));
    }

    private WorkoutTorqueEntryImpl(Parcel in) {
        this.offset = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
        this.torque = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkoutTorqueEntryImpl that = (WorkoutTorqueEntryImpl) o;
        return Double.compare(that.offset, this.offset) == 0 && Double.compare(that.torque, this.torque) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.offset);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.torque);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }
}
