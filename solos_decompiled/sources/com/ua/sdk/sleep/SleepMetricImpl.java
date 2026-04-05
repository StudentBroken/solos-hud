package com.ua.sdk.sleep;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.sleep.SleepMetric;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public class SleepMetricImpl extends ApiTransferObject implements SleepMetric {
    public static final Parcelable.Creator<SleepMetricImpl> CREATOR = new Parcelable.Creator<SleepMetricImpl>() { // from class: com.ua.sdk.sleep.SleepMetricImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepMetricImpl createFromParcel(Parcel source) {
            return new SleepMetricImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepMetricImpl[] newArray(int size) {
            return new SleepMetricImpl[size];
        }
    };

    @SerializedName("aggregates")
    Aggregates aggregates;

    @SerializedName("created_datetime")
    Date createdDateTime;

    @SerializedName("end_datetime_utc")
    Date endDateTime;

    @SerializedName("recorder_type_key")
    String recordTypeKey;

    @SerializedName("reference_key")
    String referenceKey;

    @SerializedName("start_datetime_utc")
    Date startDateTime;

    @SerializedName("start_datetime_timezone")
    TimeZone startTimeZone;

    @SerializedName("time_series")
    TimeSeries timeSeries;

    @SerializedName("updated_datetime")
    Date updatedDateTime;

    protected SleepMetricImpl(SleepMetricBuilderImpl builder) {
        this.recordTypeKey = builder.recorderTypeKey;
        this.referenceKey = builder.referenceKey;
        this.startDateTime = builder.startDateTime;
        this.endDateTime = builder.endDateTime;
        this.startTimeZone = builder.startTimeZone;
        this.aggregates = builder.aggregates;
        this.timeSeries = builder.timeSeries;
    }

    @Override // com.ua.sdk.sleep.SleepMetric
    public String getRecorderTypeKey() {
        return this.recordTypeKey;
    }

    @Override // com.ua.sdk.sleep.SleepMetric
    public String getReferenceKey() {
        return this.referenceKey;
    }

    @Override // com.ua.sdk.sleep.SleepMetric
    public TimeZone getStartTimeZone() {
        return this.startTimeZone;
    }

    @Override // com.ua.sdk.sleep.SleepMetric
    public Date getStartDateTime() {
        return this.startDateTime;
    }

    @Override // com.ua.sdk.sleep.SleepMetric
    public Date getEndDateTime() {
        return this.endDateTime;
    }

    @Override // com.ua.sdk.sleep.SleepMetric
    public SleepMetric.TimeSeries getTimeSeries() {
        return this.timeSeries;
    }

    @Override // com.ua.sdk.sleep.SleepMetric
    public SleepMetric.Aggregates getAggregates() {
        return this.aggregates;
    }

    @Override // com.ua.sdk.sleep.SleepMetric
    public Date getUpdatedDateTime() {
        return this.updatedDateTime;
    }

    @Override // com.ua.sdk.sleep.SleepMetric
    public Date getCreatedDateTime() {
        return this.createdDateTime;
    }

    @Override // com.ua.sdk.Resource
    public SleepRef getRef() {
        Link self = getLink("self");
        if (self == null) {
            return null;
        }
        return new SleepRef(self.getId(), self.getHref());
    }

    public static class TimeSeries implements SleepMetric.TimeSeries {
        public static final Parcelable.Creator<TimeSeries> CREATOR = new Parcelable.Creator<TimeSeries>() { // from class: com.ua.sdk.sleep.SleepMetricImpl.TimeSeries.1
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
        final ArrayList<SleepStateEntry> events;

        public TimeSeries() {
            this.events = new ArrayList<>();
        }

        @Override // com.ua.sdk.sleep.SleepMetric.TimeSeries
        public int size() {
            return this.events.size();
        }

        @Override // com.ua.sdk.sleep.SleepMetric.TimeSeries
        public long getEpoch(int index) {
            return this.events.get(index).epoch;
        }

        @Override // com.ua.sdk.sleep.SleepMetric.TimeSeries
        public SleepMetric.State getState(int index) {
            return this.events.get(index).state;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.events);
        }

        private TimeSeries(Parcel in) {
            this.events = in.readArrayList(SleepStateEntry.class.getClassLoader());
        }
    }

    public static class Aggregates implements SleepMetric.Aggregates {
        public static final Parcelable.Creator<Aggregates> CREATOR = new Parcelable.Creator<Aggregates>() { // from class: com.ua.sdk.sleep.SleepMetricImpl.Aggregates.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Aggregates createFromParcel(Parcel source) {
                return new Aggregates(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Aggregates[] newArray(int size) {
                return new Aggregates[size];
            }
        };

        @SerializedName("time_to_sleep")
        int timeToSleep;

        @SerializedName("times_awakened")
        int timesAwakened;

        @SerializedName("total_deep_sleep")
        int totalDeepSleep;

        @SerializedName("total_light_sleep")
        int totalLightSleep;

        @SerializedName("total_sleep")
        int totalSleep;

        @SerializedName("total_time_awake")
        int totalTimeAwake;

        @Override // com.ua.sdk.sleep.SleepMetric.Aggregates
        public int getTotalTimeAwake() {
            return this.totalTimeAwake;
        }

        @Override // com.ua.sdk.sleep.SleepMetric.Aggregates
        public int getTotalLightSleep() {
            return this.totalLightSleep;
        }

        @Override // com.ua.sdk.sleep.SleepMetric.Aggregates
        public int getTotalDeepSleep() {
            return this.totalDeepSleep;
        }

        @Override // com.ua.sdk.sleep.SleepMetric.Aggregates
        public int getTotalSleep() {
            return this.totalSleep;
        }

        @Override // com.ua.sdk.sleep.SleepMetric.Aggregates
        public int getTimesAwakened() {
            return this.timesAwakened;
        }

        @Override // com.ua.sdk.sleep.SleepMetric.Aggregates
        public int getTimeToSleep() {
            return this.timeToSleep;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.timeToSleep);
            dest.writeInt(this.totalTimeAwake);
            dest.writeInt(this.totalLightSleep);
            dest.writeInt(this.totalDeepSleep);
            dest.writeInt(this.totalSleep);
            dest.writeInt(this.timesAwakened);
        }

        public Aggregates() {
        }

        private Aggregates(Parcel in) {
            this.timeToSleep = in.readInt();
            this.totalTimeAwake = in.readInt();
            this.totalLightSleep = in.readInt();
            this.totalDeepSleep = in.readInt();
            this.totalSleep = in.readInt();
            this.timesAwakened = in.readInt();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.timeSeries, 0);
        dest.writeString(this.recordTypeKey);
        dest.writeString(this.referenceKey);
        dest.writeLong(this.startDateTime != null ? this.startDateTime.getTime() : -1L);
        dest.writeLong(this.endDateTime != null ? this.endDateTime.getTime() : -1L);
        dest.writeSerializable(this.startTimeZone);
        dest.writeParcelable(this.aggregates, 0);
        dest.writeLong(this.createdDateTime != null ? this.createdDateTime.getTime() : -1L);
        dest.writeLong(this.updatedDateTime != null ? this.updatedDateTime.getTime() : -1L);
    }

    public SleepMetricImpl() {
    }

    private SleepMetricImpl(Parcel in) {
        this.timeSeries = (TimeSeries) in.readParcelable(TimeSeries.class.getClassLoader());
        this.recordTypeKey = in.readString();
        this.referenceKey = in.readString();
        long tmpStartDateTime = in.readLong();
        this.startDateTime = tmpStartDateTime == -1 ? null : new Date(tmpStartDateTime);
        long tmpEndDateTime = in.readLong();
        this.endDateTime = tmpEndDateTime == -1 ? null : new Date(tmpEndDateTime);
        this.startTimeZone = (TimeZone) in.readSerializable();
        this.aggregates = (Aggregates) in.readParcelable(Aggregates.class.getClassLoader());
        long tmpCreatedDateTime = in.readLong();
        this.createdDateTime = tmpCreatedDateTime == -1 ? null : new Date(tmpCreatedDateTime);
        long tmpUpdatedDateTime = in.readLong();
        this.updatedDateTime = tmpUpdatedDateTime != -1 ? new Date(tmpUpdatedDateTime) : null;
    }
}
