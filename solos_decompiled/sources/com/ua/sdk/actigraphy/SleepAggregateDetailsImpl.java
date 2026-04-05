package com.ua.sdk.actigraphy;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class SleepAggregateDetailsImpl implements Parcelable, SleepAggregateDetails {
    public static Parcelable.Creator<SleepAggregateDetailsImpl> CREATOR = new Parcelable.Creator<SleepAggregateDetailsImpl>() { // from class: com.ua.sdk.actigraphy.SleepAggregateDetailsImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepAggregateDetailsImpl createFromParcel(Parcel source) {
            return new SleepAggregateDetailsImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepAggregateDetailsImpl[] newArray(int size) {
            return new SleepAggregateDetailsImpl[size];
        }
    };
    private Double mAwake;
    private Double mDeepSleep;
    private Double mLightSleep;
    private Double mTimeToSleep;
    private Integer mTimesAwaken;

    protected SleepAggregateDetailsImpl() {
    }

    @Override // com.ua.sdk.actigraphy.SleepAggregateDetails
    public Double getDeepSleep() {
        return this.mDeepSleep;
    }

    @Override // com.ua.sdk.actigraphy.SleepAggregateDetails
    public void setDeepSleep(Double deepSleep) {
        this.mDeepSleep = deepSleep;
    }

    @Override // com.ua.sdk.actigraphy.SleepAggregateDetails
    public Double getAwake() {
        return this.mAwake;
    }

    @Override // com.ua.sdk.actigraphy.SleepAggregateDetails
    public void setAwake(Double awake) {
        this.mAwake = awake;
    }

    @Override // com.ua.sdk.actigraphy.SleepAggregateDetails
    public Double getTimeToSleep() {
        return this.mTimeToSleep;
    }

    @Override // com.ua.sdk.actigraphy.SleepAggregateDetails
    public void setTimeToSleep(Double timeToSleep) {
        this.mTimeToSleep = timeToSleep;
    }

    @Override // com.ua.sdk.actigraphy.SleepAggregateDetails
    public Integer getTimesAwaken() {
        return this.mTimesAwaken;
    }

    @Override // com.ua.sdk.actigraphy.SleepAggregateDetails
    public void setTimesAwaken(Integer timesAwaken) {
        this.mTimesAwaken = timesAwaken;
    }

    @Override // com.ua.sdk.actigraphy.SleepAggregateDetails
    public Double getLightSleep() {
        return this.mLightSleep;
    }

    @Override // com.ua.sdk.actigraphy.SleepAggregateDetails
    public void setLightSleep(Double lightSleep) {
        this.mLightSleep = lightSleep;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mDeepSleep);
        dest.writeValue(this.mAwake);
        dest.writeValue(this.mTimeToSleep);
        dest.writeValue(this.mTimesAwaken);
        dest.writeValue(this.mLightSleep);
    }

    private SleepAggregateDetailsImpl(Parcel in) {
        this.mDeepSleep = (Double) in.readValue(Double.class.getClassLoader());
        this.mAwake = (Double) in.readValue(Double.class.getClassLoader());
        this.mTimeToSleep = (Double) in.readValue(Double.class.getClassLoader());
        this.mTimesAwaken = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mLightSleep = (Double) in.readValue(Double.class.getClassLoader());
    }
}
