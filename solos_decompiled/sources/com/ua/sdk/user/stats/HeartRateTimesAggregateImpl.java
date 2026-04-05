package com.ua.sdk.user.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes65.dex */
public class HeartRateTimesAggregateImpl implements HeartRateTimesAggregate {
    public static Parcelable.Creator<HeartRateTimesAggregateImpl> CREATOR = new Parcelable.Creator<HeartRateTimesAggregateImpl>() { // from class: com.ua.sdk.user.stats.HeartRateTimesAggregateImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateTimesAggregateImpl createFromParcel(Parcel source) {
            return new HeartRateTimesAggregateImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateTimesAggregateImpl[] newArray(int size) {
            return new HeartRateTimesAggregateImpl[size];
        }
    };

    @SerializedName("zone_5")
    Double mTimeInZoneFive;

    @SerializedName("zone_4")
    Double mTimeInZoneFour;

    @SerializedName("zone_1")
    Double mTimeInZoneOne;

    @SerializedName("zone_3")
    Double mTimeInZoneThree;

    @SerializedName("zone_2")
    Double mTimeInZoneTwo;

    @Override // com.ua.sdk.user.stats.HeartRateTimesAggregate
    public Double getTimeInZoneFive() {
        return this.mTimeInZoneFive;
    }

    @Override // com.ua.sdk.user.stats.HeartRateTimesAggregate
    public Double getTimeInZoneFour() {
        return this.mTimeInZoneFour;
    }

    @Override // com.ua.sdk.user.stats.HeartRateTimesAggregate
    public Double getTimeInZoneThree() {
        return this.mTimeInZoneThree;
    }

    @Override // com.ua.sdk.user.stats.HeartRateTimesAggregate
    public Double getTimeInZoneTwo() {
        return this.mTimeInZoneTwo;
    }

    @Override // com.ua.sdk.user.stats.HeartRateTimesAggregate
    public Double getTimeInZoneOne() {
        return this.mTimeInZoneOne;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mTimeInZoneFive);
        dest.writeValue(this.mTimeInZoneFour);
        dest.writeValue(this.mTimeInZoneThree);
        dest.writeValue(this.mTimeInZoneTwo);
        dest.writeValue(this.mTimeInZoneOne);
    }

    public HeartRateTimesAggregateImpl() {
    }

    private HeartRateTimesAggregateImpl(Parcel in) {
        this.mTimeInZoneFive = (Double) in.readValue(Double.class.getClassLoader());
        this.mTimeInZoneFour = (Double) in.readValue(Double.class.getClassLoader());
        this.mTimeInZoneThree = (Double) in.readValue(Double.class.getClassLoader());
        this.mTimeInZoneTwo = (Double) in.readValue(Double.class.getClassLoader());
        this.mTimeInZoneOne = (Double) in.readValue(Double.class.getClassLoader());
    }
}
