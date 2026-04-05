package com.ua.sdk.aggregate;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.datapoint.DataField;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class AggregateSummaryImpl implements AggregateSummary {
    public static Parcelable.Creator<AggregateSummaryImpl> CREATOR = new Parcelable.Creator<AggregateSummaryImpl>() { // from class: com.ua.sdk.aggregate.AggregateSummaryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AggregateSummaryImpl createFromParcel(Parcel source) {
            return new AggregateSummaryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AggregateSummaryImpl[] newArray(int size) {
            return new AggregateSummaryImpl[size];
        }
    };

    @SerializedName("datetime")
    Date datetime;

    @SerializedName("start_datetime")
    Date startDatetime;

    @SerializedName(FirebaseAnalytics.Param.VALUE)
    Map<String, Object> value;

    public AggregateSummaryImpl() {
        this.value = new HashMap(4);
    }

    private AggregateSummaryImpl(Parcel in) {
        this.value = new HashMap(4);
        this.startDatetime = (Date) in.readValue(Date.class.getClassLoader());
        this.datetime = (Date) in.readValue(Date.class.getClassLoader());
        in.readMap(this.value, HashMap.class.getClassLoader());
    }

    @Override // com.ua.sdk.aggregate.AggregateSummary
    public Date getStartDatetime() {
        return this.startDatetime;
    }

    @Override // com.ua.sdk.aggregate.AggregateSummary
    public Date getDatetime() {
        return this.datetime;
    }

    @Override // com.ua.sdk.aggregate.AggregateSummary
    public Long getValueLong(DataField field) {
        if (field == null) {
            return null;
        }
        Object value = this.value.get(field.getId());
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Double) {
            return Long.valueOf(((Double) value).longValue());
        }
        return null;
    }

    @Override // com.ua.sdk.aggregate.AggregateSummary
    public Double getValueDouble(DataField field) {
        if (field == null) {
            return null;
        }
        Object value = this.value.get(field.getId());
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Long) {
            return Double.valueOf(((Long) value).doubleValue());
        }
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.startDatetime);
        dest.writeValue(this.datetime);
        dest.writeMap(this.value);
    }
}
