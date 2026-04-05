package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutStepsEntryImpl implements WorkoutStepsEntry {
    public static final Parcelable.Creator<WorkoutStepsEntryImpl> CREATOR = new Parcelable.Creator<WorkoutStepsEntryImpl>() { // from class: com.ua.sdk.workout.WorkoutStepsEntryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutStepsEntryImpl createFromParcel(Parcel source) {
            return new WorkoutStepsEntryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutStepsEntryImpl[] newArray(int size) {
            return new WorkoutStepsEntryImpl[size];
        }
    };
    double offset;
    int steps;

    public WorkoutStepsEntryImpl(Double offset, Integer steps) {
        this.offset = offset.doubleValue();
        this.steps = steps.intValue();
    }

    @Override // com.ua.sdk.workout.WorkoutStepsEntry
    public int getInstantaneousSteps() {
        return this.steps;
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
    public int compareTo(WorkoutStepsEntry another) {
        return Double.compare(this.offset, another.getOffset());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Double.valueOf(this.offset));
        dest.writeValue(Integer.valueOf(this.steps));
    }

    private WorkoutStepsEntryImpl(Parcel in) {
        this.offset = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
        this.steps = ((Integer) in.readValue(Integer.class.getClassLoader())).intValue();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkoutStepsEntryImpl that = (WorkoutStepsEntryImpl) o;
        return Double.compare(that.offset, this.offset) == 0 && this.steps == that.steps;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.offset);
        int result = (int) ((temp >>> 32) ^ temp);
        return (result * 31) + this.steps;
    }
}
