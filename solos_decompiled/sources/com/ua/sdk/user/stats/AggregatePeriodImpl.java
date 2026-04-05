package com.ua.sdk.user.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.LocalDate;

/* JADX INFO: loaded from: classes65.dex */
public class AggregatePeriodImpl implements AggregatePeriod {
    public static Parcelable.Creator<AggregatePeriodImpl> CREATOR = new Parcelable.Creator<AggregatePeriodImpl>() { // from class: com.ua.sdk.user.stats.AggregatePeriodImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AggregatePeriodImpl createFromParcel(Parcel source) {
            return new AggregatePeriodImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AggregatePeriodImpl[] newArray(int size) {
            return new AggregatePeriodImpl[size];
        }
    };

    @SerializedName("end")
    LocalDate mEndDate;

    @SerializedName("start")
    LocalDate mStartDate;

    protected AggregatePeriodImpl() {
    }

    @Override // com.ua.sdk.user.stats.AggregatePeriod
    public LocalDate getStartDate() {
        return this.mStartDate;
    }

    @Override // com.ua.sdk.user.stats.AggregatePeriod
    public LocalDate getEndDate() {
        return this.mEndDate;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mStartDate, 0);
        dest.writeParcelable(this.mEndDate, 0);
    }

    private AggregatePeriodImpl(Parcel in) {
        this.mStartDate = (LocalDate) in.readParcelable(LocalDate.class.getClassLoader());
        this.mEndDate = (LocalDate) in.readParcelable(LocalDate.class.getClassLoader());
    }
}
