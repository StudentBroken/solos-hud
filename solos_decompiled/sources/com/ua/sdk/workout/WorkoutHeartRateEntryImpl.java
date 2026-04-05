package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutHeartRateEntryImpl implements WorkoutHeartRateEntry {
    public static final Parcelable.Creator<WorkoutHeartRateEntryImpl> CREATOR = new Parcelable.Creator<WorkoutHeartRateEntryImpl>() { // from class: com.ua.sdk.workout.WorkoutHeartRateEntryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutHeartRateEntryImpl createFromParcel(Parcel source) {
            return new WorkoutHeartRateEntryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutHeartRateEntryImpl[] newArray(int size) {
            return new WorkoutHeartRateEntryImpl[size];
        }
    };
    private int bpm;
    private double offset;

    public WorkoutHeartRateEntryImpl(Double offset, Integer bpm) {
        this.offset = offset.doubleValue();
        this.bpm = bpm.intValue();
    }

    @Override // com.ua.sdk.workout.WorkoutHeartRateEntry
    public int getBpm() {
        return this.bpm;
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
    public int compareTo(WorkoutHeartRateEntry another) {
        return Double.compare(this.offset, another.getOffset());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Double.valueOf(this.offset));
        dest.writeValue(Integer.valueOf(this.bpm));
    }

    private WorkoutHeartRateEntryImpl(Parcel in) {
        this.offset = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
        this.bpm = ((Integer) in.readValue(Integer.class.getClassLoader())).intValue();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkoutHeartRateEntryImpl that = (WorkoutHeartRateEntryImpl) o;
        return this.bpm == that.bpm && Double.compare(that.offset, this.offset) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.offset);
        int result = (int) ((temp >>> 32) ^ temp);
        return (result * 31) + this.bpm;
    }
}
