package com.ua.sdk.activitytimeseries;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.Reference;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTimeSeriesImpl implements ActivityTimeSeries, Parcelable {
    public static Parcelable.Creator<ActivityTimeSeriesImpl> CREATOR = new Parcelable.Creator<ActivityTimeSeriesImpl>() { // from class: com.ua.sdk.activitytimeseries.ActivityTimeSeriesImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityTimeSeriesImpl createFromParcel(Parcel source) {
            return new ActivityTimeSeriesImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityTimeSeriesImpl[] newArray(int size) {
            return new ActivityTimeSeriesImpl[size];
        }
    };

    @SerializedName("recorder_identifier")
    String recorderIdentifier;

    @SerializedName("recorder_type_key")
    String recorderTypeKey;

    @SerializedName("time_series")
    TimeSeries timeSeries;

    ActivityTimeSeriesImpl() {
    }

    ActivityTimeSeriesImpl(ActivityTimeSeriesBuilderImpl builder) {
        this.recorderTypeKey = builder.recorderTypeKey;
        this.recorderIdentifier = builder.recorderIdentifier;
        if (builder.distanceEpochs != null || builder.distanceValues != null || builder.calorieEpochs != null || builder.calorieValues != null || builder.stepEpochs != null || builder.stepValues != null) {
            this.timeSeries = new TimeSeries();
            if (builder.stepEpochs != null) {
                this.timeSeries.stepEpochs = builder.stepEpochs.toArray();
                this.timeSeries.stepValues = builder.stepValues.toArray();
            }
            if (builder.calorieEpochs != null) {
                this.timeSeries.calorieEpochs = builder.calorieEpochs.toArray();
                this.timeSeries.calorieValues = builder.calorieValues.toArray();
            }
            if (builder.distanceEpochs != null) {
                this.timeSeries.distanceEpochs = builder.distanceEpochs.toArray();
                this.timeSeries.distanceValues = builder.distanceValues.toArray();
            }
        }
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public String getRecorderTypeKey() {
        return this.recorderTypeKey;
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public String getRecorderIdentifier() {
        return this.recorderIdentifier;
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public int getStepsSize() {
        if (this.timeSeries == null || this.timeSeries.stepEpochs == null) {
            return 0;
        }
        return this.timeSeries.stepEpochs.length;
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public long getStepEpoch(int index) {
        if (this.timeSeries == null || this.timeSeries.stepEpochs == null) {
            throwIndexOutOfBoundsException(index, 0);
        }
        return this.timeSeries.stepEpochs[index];
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public int getStepValue(int index) {
        if (this.timeSeries == null || this.timeSeries.stepValues == null) {
            throwIndexOutOfBoundsException(index, 0);
        }
        return this.timeSeries.stepValues[index];
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public int getDistancesSize() {
        if (this.timeSeries == null || this.timeSeries.distanceEpochs == null) {
            return 0;
        }
        return this.timeSeries.distanceEpochs.length;
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public long getDistanceEpoch(int index) {
        if (this.timeSeries == null || this.timeSeries.distanceEpochs == null) {
            throwIndexOutOfBoundsException(index, 0);
        }
        return this.timeSeries.distanceEpochs[index];
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public double getDistanceValue(int index) {
        if (this.timeSeries == null || this.timeSeries.distanceValues == null) {
            throwIndexOutOfBoundsException(index, 0);
        }
        return this.timeSeries.distanceValues[index];
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public int getCaloriesSize() {
        if (this.timeSeries == null || this.timeSeries.calorieEpochs == null) {
            return 0;
        }
        return this.timeSeries.calorieEpochs.length;
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public long getCalorieEpoch(int index) {
        if (this.timeSeries == null || this.timeSeries.calorieEpochs == null) {
            throwIndexOutOfBoundsException(index, 0);
        }
        return this.timeSeries.calorieEpochs[index];
    }

    @Override // com.ua.sdk.activitytimeseries.ActivityTimeSeries
    public double getCalorieValue(int index) {
        if (this.timeSeries == null || this.timeSeries.calorieValues == null) {
            throwIndexOutOfBoundsException(index, 0);
        }
        return this.timeSeries.calorieValues[index];
    }

    @Override // com.ua.sdk.Resource
    public Reference getRef() {
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    static IndexOutOfBoundsException throwIndexOutOfBoundsException(int index, int size) {
        throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + size);
    }

    public static class TimeSeries implements Parcelable {
        public static final Parcelable.Creator<TimeSeries> CREATOR = new Parcelable.Creator<TimeSeries>() { // from class: com.ua.sdk.activitytimeseries.ActivityTimeSeriesImpl.TimeSeries.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public TimeSeries createFromParcel(Parcel source) {
                return new TimeSeries(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public TimeSeries[] newArray(int size) {
                return new TimeSeries[size];
            }
        };
        long[] calorieEpochs;
        double[] calorieValues;
        long[] distanceEpochs;
        double[] distanceValues;
        long[] stepEpochs;
        int[] stepValues;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLongArray(this.stepEpochs);
            dest.writeIntArray(this.stepValues);
            dest.writeLongArray(this.distanceEpochs);
            dest.writeDoubleArray(this.distanceValues);
            dest.writeLongArray(this.calorieEpochs);
            dest.writeDoubleArray(this.calorieValues);
        }

        public TimeSeries() {
            this.stepEpochs = null;
            this.stepValues = null;
            this.distanceEpochs = null;
            this.distanceValues = null;
            this.calorieEpochs = null;
            this.calorieValues = null;
        }

        private TimeSeries(Parcel in) {
            this.stepEpochs = null;
            this.stepValues = null;
            this.distanceEpochs = null;
            this.distanceValues = null;
            this.calorieEpochs = null;
            this.calorieValues = null;
            this.stepEpochs = in.createLongArray();
            this.stepValues = in.createIntArray();
            this.distanceEpochs = in.createLongArray();
            this.distanceValues = in.createDoubleArray();
            this.calorieEpochs = in.createLongArray();
            this.calorieValues = in.createDoubleArray();
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recorderTypeKey);
        dest.writeString(this.recorderIdentifier);
        dest.writeParcelable(this.timeSeries, flags);
    }

    ActivityTimeSeriesImpl(Parcel in) {
        this.recorderTypeKey = in.readString();
        this.recorderIdentifier = in.readString();
        this.timeSeries = (TimeSeries) in.readParcelable(TimeSeries.class.getClassLoader());
    }
}
