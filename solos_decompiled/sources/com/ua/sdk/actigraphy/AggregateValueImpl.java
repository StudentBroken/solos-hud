package com.ua.sdk.actigraphy;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.aggregate.AggregateDetails;
import com.ua.sdk.aggregate.AggregateValue;

/* JADX INFO: loaded from: classes65.dex */
public class AggregateValueImpl implements Parcelable, AggregateValue {
    public static Parcelable.Creator<AggregateValueImpl> CREATOR = new Parcelable.Creator<AggregateValueImpl>() { // from class: com.ua.sdk.actigraphy.AggregateValueImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AggregateValueImpl createFromParcel(Parcel source) {
            return new AggregateValueImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AggregateValueImpl[] newArray(int size) {
            return new AggregateValueImpl[size];
        }
    };
    private AggregateDetails mAggregateDetails;
    private Double mAverage;
    private Double mCount;
    private Double mLatest;
    private Double mMax;
    private Double mMin;
    private Double mSum;

    protected AggregateValueImpl() {
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public Double getCount() {
        return this.mCount;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public void setCount(Double count) {
        this.mCount = count;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public Double getSum() {
        return this.mSum;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public void setSum(Double sum) {
        this.mSum = sum;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public Double getMin() {
        return this.mMin;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public void setMin(Double min) {
        this.mMin = min;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public Double getMax() {
        return this.mMax;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public void setMax(Double max) {
        this.mMax = max;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public Double getLatest() {
        return this.mLatest;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public void setLatest(Double latest) {
        this.mLatest = latest;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public Double getAverage() {
        return this.mAverage;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public void setAverage(Double average) {
        this.mAverage = average;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public AggregateDetails getAggregateDetails() {
        return this.mAggregateDetails;
    }

    @Override // com.ua.sdk.aggregate.AggregateValue
    public void setAggregateDetails(AggregateDetails aggregateDetails) {
        this.mAggregateDetails = aggregateDetails;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSum == null ? null : this.mSum.toString());
        dest.writeString(this.mMin == null ? null : this.mMin.toString());
        dest.writeString(this.mMax == null ? null : this.mMax.toString());
        dest.writeString(this.mLatest == null ? null : this.mLatest.toString());
        dest.writeString(this.mCount == null ? null : this.mCount.toString());
        dest.writeString(this.mAverage != null ? this.mAverage.toString() : null);
        dest.writeParcelable(this.mAggregateDetails, flags);
    }

    private AggregateValueImpl(Parcel in) {
        String tmpString = in.readString();
        this.mSum = tmpString == null ? null : new Double(tmpString);
        String tmpString2 = in.readString();
        this.mMin = tmpString2 == null ? null : new Double(tmpString2);
        String tmpString3 = in.readString();
        this.mMax = tmpString3 == null ? null : new Double(tmpString3);
        String tmpString4 = in.readString();
        this.mLatest = tmpString4 == null ? null : new Double(tmpString4);
        String tmpString5 = in.readString();
        this.mCount = tmpString5 == null ? null : new Double(tmpString5);
        String tmpString6 = in.readString();
        this.mAverage = tmpString6 != null ? new Double(tmpString6) : null;
        this.mAggregateDetails = (AggregateDetails) in.readParcelable(AggregateDetails.class.getClassLoader());
    }
}
