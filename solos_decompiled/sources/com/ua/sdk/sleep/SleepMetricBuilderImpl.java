package com.ua.sdk.sleep;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.sleep.SleepMetric;
import com.ua.sdk.sleep.SleepMetricImpl;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/* JADX INFO: loaded from: classes65.dex */
public class SleepMetricBuilderImpl implements SleepMetricBuilder {
    public static final Parcelable.Creator<SleepMetricBuilderImpl> CREATOR = new Parcelable.Creator<SleepMetricBuilderImpl>() { // from class: com.ua.sdk.sleep.SleepMetricBuilderImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepMetricBuilderImpl createFromParcel(Parcel source) {
            return new SleepMetricBuilderImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepMetricBuilderImpl[] newArray(int size) {
            return new SleepMetricBuilderImpl[size];
        }
    };
    private static final String RECORDER_DEFAULT = "client_manual_creation";
    SleepMetricImpl.Aggregates aggregates;
    int deepSleepSeconds;
    Date endDateTime;
    int lightSleepSeconds;
    String recorderTypeKey;
    String referenceKey;
    Date startDateTime;
    TimeZone startTimeZone;
    int timeAwakeSeconds;
    SleepMetricImpl.TimeSeries timeSeries;
    int timeToSleepSeconds;
    int timesAwakened;

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder setRecorderTypeKey(String recorderTypeKey) {
        this.recorderTypeKey = recorderTypeKey;
        return this;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
        return this;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder setStartTimeZone(TimeZone timeZone) {
        this.startTimeZone = timeZone;
        return this;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder setTotalTimeAwake(int timeAwakeSeconds) {
        this.timeAwakeSeconds = timeAwakeSeconds;
        return this;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder setTotalLightSleep(int lightSleepSeconds) {
        this.lightSleepSeconds = lightSleepSeconds;
        return this;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder setTotalDeepSleep(int deepSleepSeconds) {
        this.deepSleepSeconds = deepSleepSeconds;
        return this;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder setTimesAwakened(int timesAwakened) {
        this.timesAwakened = timesAwakened;
        return this;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder setTimeToSleep(int timeToSleepSeconds) {
        this.timeToSleepSeconds = timeToSleepSeconds;
        return this;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricBuilder addEvent(long epoch, SleepMetric.State state) {
        if (this.timeSeries == null) {
            this.timeSeries = new SleepMetricImpl.TimeSeries();
        }
        this.timeSeries.events.add(new SleepStateEntry(epoch, state));
        return this;
    }

    protected void createAggregates() {
        int timeAwakeSeconds = 0;
        int lightSleepSeconds = 0;
        int deepSleepSeconds = 0;
        int timesAwakened = 0;
        int timeToSleepSeconds = 0;
        boolean sleepStarted = false;
        if (this.timeSeries != null && !this.timeSeries.events.isEmpty()) {
            SleepStateEntry prevEntry = this.timeSeries.events.get(0);
            int size = this.timeSeries.events.size();
            for (int i = 1; i < size; i++) {
                SleepStateEntry entry = this.timeSeries.events.get(i);
                if (sleepStarted) {
                    long seconds = entry.epoch - prevEntry.epoch;
                    switch (prevEntry.state) {
                        case LIGHT_SLEEP:
                            lightSleepSeconds = (int) (((long) lightSleepSeconds) + seconds);
                            break;
                        case DEEP_SLEEP:
                            deepSleepSeconds = (int) (((long) deepSleepSeconds) + seconds);
                            break;
                        case AWAKE:
                            timesAwakened++;
                            timeAwakeSeconds = (int) (((long) timeAwakeSeconds) + seconds);
                            break;
                    }
                } else if (entry.state != SleepMetric.State.AWAKE) {
                    sleepStarted = true;
                    timeToSleepSeconds = (int) (entry.epoch - (this.startDateTime.getTime() / 1000));
                }
                prevEntry = entry;
            }
            long seconds2 = (this.endDateTime.getTime() / 1000) - prevEntry.epoch;
            switch (prevEntry.state) {
                case LIGHT_SLEEP:
                    lightSleepSeconds = (int) (((long) lightSleepSeconds) + seconds2);
                    break;
                case DEEP_SLEEP:
                    deepSleepSeconds = (int) (((long) deepSleepSeconds) + seconds2);
                    break;
                case AWAKE:
                    timesAwakened++;
                    timeAwakeSeconds = (int) (((long) timeAwakeSeconds) + seconds2);
                    break;
            }
        }
        SleepMetricImpl.Aggregates aggregates = new SleepMetricImpl.Aggregates();
        if (this.timeToSleepSeconds >= 0) {
            timeToSleepSeconds = this.timeToSleepSeconds;
        }
        aggregates.timeToSleep = timeToSleepSeconds;
        if (this.timesAwakened >= 0) {
            timesAwakened = this.timesAwakened;
        }
        aggregates.timesAwakened = timesAwakened;
        if (this.timeAwakeSeconds >= 0) {
            timeAwakeSeconds = this.timeAwakeSeconds;
        }
        aggregates.totalTimeAwake = timeAwakeSeconds;
        if (this.lightSleepSeconds >= 0) {
            lightSleepSeconds = this.lightSleepSeconds;
        }
        aggregates.totalLightSleep = lightSleepSeconds;
        if (this.deepSleepSeconds >= 0) {
            deepSleepSeconds = this.deepSleepSeconds;
        }
        aggregates.totalDeepSleep = deepSleepSeconds;
        aggregates.totalSleep = aggregates.totalLightSleep + aggregates.totalDeepSleep;
        this.aggregates = aggregates;
    }

    @Override // com.ua.sdk.sleep.SleepMetricBuilder
    public SleepMetricImpl build() {
        if (this.recorderTypeKey == null) {
            this.recorderTypeKey = RECORDER_DEFAULT;
        }
        if (this.referenceKey == null) {
            this.referenceKey = UUID.randomUUID().toString();
        }
        if (this.startTimeZone == null) {
            this.startTimeZone = TimeZone.getDefault();
        }
        if (this.timeSeries != null) {
            Collections.sort(this.timeSeries.events);
            if (this.startDateTime == null) {
                if (this.timeToSleepSeconds > 0) {
                    for (SleepStateEntry entry : this.timeSeries.events) {
                        if (entry.state == SleepMetric.State.LIGHT_SLEEP || entry.state == SleepMetric.State.DEEP_SLEEP) {
                            this.startDateTime = new Date((entry.epoch - ((long) this.timeToSleepSeconds)) * 1000);
                            break;
                        }
                    }
                }
                if (this.startDateTime == null) {
                    this.startDateTime = new Date(this.timeSeries.events.get(0).epoch * 1000);
                }
            }
            if (this.endDateTime == null) {
                this.endDateTime = new Date(this.timeSeries.events.get(this.timeSeries.events.size() - 1).epoch * 1000);
            }
        } else if (this.startDateTime == null || this.endDateTime == null) {
            throw new IllegalStateException("start datetime and end datetime must be specified if no time series data is set.");
        }
        createAggregates();
        return new SleepMetricImpl(this);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recorderTypeKey);
        dest.writeString(this.referenceKey);
        dest.writeValue(this.startDateTime);
        dest.writeValue(this.endDateTime);
        dest.writeSerializable(this.startTimeZone);
        dest.writeParcelable(this.timeSeries, 0);
        dest.writeParcelable(this.aggregates, 0);
        dest.writeInt(this.timeAwakeSeconds);
        dest.writeInt(this.lightSleepSeconds);
        dest.writeInt(this.deepSleepSeconds);
        dest.writeInt(this.timesAwakened);
        dest.writeInt(this.timeToSleepSeconds);
    }

    public SleepMetricBuilderImpl() {
        this.timeAwakeSeconds = -1;
        this.lightSleepSeconds = -1;
        this.deepSleepSeconds = -1;
        this.timesAwakened = -1;
        this.timeToSleepSeconds = -1;
    }

    private SleepMetricBuilderImpl(Parcel in) {
        this.timeAwakeSeconds = -1;
        this.lightSleepSeconds = -1;
        this.deepSleepSeconds = -1;
        this.timesAwakened = -1;
        this.timeToSleepSeconds = -1;
        this.recorderTypeKey = in.readString();
        this.referenceKey = in.readString();
        this.startDateTime = (Date) in.readValue(Date.class.getClassLoader());
        this.endDateTime = (Date) in.readValue(Date.class.getClassLoader());
        this.startTimeZone = (TimeZone) in.readSerializable();
        this.timeSeries = (SleepMetricImpl.TimeSeries) in.readParcelable(SleepMetricImpl.TimeSeries.class.getClassLoader());
        this.aggregates = (SleepMetricImpl.Aggregates) in.readParcelable(SleepMetricImpl.Aggregates.class.getClassLoader());
        this.timeAwakeSeconds = in.readInt();
        this.lightSleepSeconds = in.readInt();
        this.deepSleepSeconds = in.readInt();
        this.timesAwakened = in.readInt();
        this.timeToSleepSeconds = in.readInt();
    }
}
