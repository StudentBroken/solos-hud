package com.ua.sdk.actigraphy;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public class MetricImpl implements Parcelable, Metric {
    public static Parcelable.Creator<MetricImpl> CREATOR = new Parcelable.Creator<MetricImpl>() { // from class: com.ua.sdk.actigraphy.MetricImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MetricImpl createFromParcel(Parcel source) {
            return new MetricImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MetricImpl[] newArray(int size) {
            return new MetricImpl[size];
        }
    };
    private AggregateValueImpl mAggregateValueImpl;
    private Date mEndDate;
    private long[] mEpochTimes;
    private Date mStartDate;
    private TimeZone mTimeZone;
    private double[] mValues;

    protected MetricImpl() {
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public Date getStartDateTime() {
        return this.mStartDate;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public void setStartDateTime(Date startDate) {
        this.mStartDate = startDate;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public Date getEndDateTime() {
        return this.mEndDate;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public void setEndDateTime(Date endDate) {
        this.mEndDate = endDate;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public AggregateValueImpl getAggregateValue() {
        return this.mAggregateValueImpl;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public void setAggregateValue(AggregateValueImpl aggregateValue) {
        this.mAggregateValueImpl = aggregateValue;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public long[] getEpochTimes() {
        return this.mEpochTimes;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public void setEpochTimes(long[] epochTimes) {
        this.mEpochTimes = epochTimes;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public long getEpochTime(int i) {
        return this.mEpochTimes[i];
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public void setTimeZone(TimeZone timeZone) {
        this.mTimeZone = timeZone;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public double[] getValues() {
        return this.mValues;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public void setValues(double[] values) {
        this.mValues = values;
    }

    @Override // com.ua.sdk.actigraphy.Metric
    public double getValue(int i) {
        return this.mValues[i];
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Long.valueOf(this.mStartDate != null ? this.mStartDate.getTime() : -1L));
        dest.writeValue(Long.valueOf(this.mEndDate != null ? this.mEndDate.getTime() : -1L));
        dest.writeParcelable(this.mAggregateValueImpl, flags);
        dest.writeLongArray(this.mEpochTimes);
        dest.writeString(this.mTimeZone == null ? null : this.mTimeZone.getID());
        dest.writeDoubleArray(this.mValues);
    }

    protected MetricImpl(Parcel in) {
        long tmpStartDate = ((Long) in.readValue(Long.class.getClassLoader())).longValue();
        this.mStartDate = tmpStartDate == -1 ? null : new Date(tmpStartDate);
        long tmpEndDate = ((Long) in.readValue(Long.class.getClassLoader())).longValue();
        this.mEndDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
        this.mAggregateValueImpl = (AggregateValueImpl) in.readParcelable(AggregateValueImpl.class.getClassLoader());
        this.mEpochTimes = in.createLongArray();
        String tmpString = in.readString();
        this.mTimeZone = tmpString != null ? TimeZone.getTimeZone(tmpString) : null;
        this.mValues = in.createDoubleArray();
    }
}
