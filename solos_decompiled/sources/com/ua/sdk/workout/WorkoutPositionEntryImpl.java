package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutPositionEntryImpl implements WorkoutPositionEntry {
    public static final Parcelable.Creator<WorkoutPositionEntryImpl> CREATOR = new Parcelable.Creator<WorkoutPositionEntryImpl>() { // from class: com.ua.sdk.workout.WorkoutPositionEntryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutPositionEntryImpl createFromParcel(Parcel source) {
            return new WorkoutPositionEntryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutPositionEntryImpl[] newArray(int size) {
            return new WorkoutPositionEntryImpl[size];
        }
    };
    private Double elevation;
    private Double latitude;
    private Double longitude;
    private double offset;

    public WorkoutPositionEntryImpl(Double offset, Double elevation, Double latitude, Double longitude) {
        this.offset = offset.doubleValue();
        this.elevation = elevation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override // com.ua.sdk.workout.WorkoutPositionEntry
    public Double getElevation() {
        return this.elevation;
    }

    @Override // com.ua.sdk.workout.WorkoutPositionEntry
    public Double getLatitude() {
        return this.latitude;
    }

    @Override // com.ua.sdk.workout.WorkoutPositionEntry
    public Double getLongitude() {
        return this.longitude;
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
    public int compareTo(WorkoutPositionEntry another) {
        return Double.compare(this.offset, another.getOffset());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.offset);
        dest.writeValue(this.elevation);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
    }

    private WorkoutPositionEntryImpl(Parcel in) {
        this.offset = in.readDouble();
        this.elevation = (Double) in.readValue(Double.class.getClassLoader());
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkoutPositionEntryImpl that = (WorkoutPositionEntryImpl) o;
        if (Double.compare(that.offset, this.offset) != 0) {
            return false;
        }
        if (this.elevation == null ? that.elevation != null : !this.elevation.equals(that.elevation)) {
            return false;
        }
        if (this.latitude == null ? that.latitude != null : !this.latitude.equals(that.latitude)) {
            return false;
        }
        if (this.longitude != null) {
            if (this.longitude.equals(that.longitude)) {
                return true;
            }
        } else if (that.longitude == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.offset);
        int result = (int) ((temp >>> 32) ^ temp);
        return (((((result * 31) + (this.elevation != null ? this.elevation.hashCode() : 0)) * 31) + (this.latitude != null ? this.latitude.hashCode() : 0)) * 31) + (this.longitude != null ? this.longitude.hashCode() : 0);
    }
}
