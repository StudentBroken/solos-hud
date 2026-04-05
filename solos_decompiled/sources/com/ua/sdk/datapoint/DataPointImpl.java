package com.ua.sdk.datapoint;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class DataPointImpl implements DataPoint {
    public static Parcelable.Creator<DataPointImpl> CREATOR = new Parcelable.Creator<DataPointImpl>() { // from class: com.ua.sdk.datapoint.DataPointImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataPointImpl createFromParcel(Parcel source) {
            return new DataPointImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataPointImpl[] newArray(int size) {
            return new DataPointImpl[size];
        }
    };
    private Date datetime;
    private Map<DataField, Object> fields;
    private Date startDatetime;

    public DataPointImpl() {
        this.fields = new HashMap(4);
    }

    public DataPointImpl(DataPointImpl other) {
        this.fields = new HashMap(4);
        this.startDatetime = other.startDatetime;
        this.datetime = other.datetime;
        this.fields.putAll(other.fields);
    }

    private DataPointImpl(Parcel in) {
        this.fields = new HashMap(4);
        this.startDatetime = (Date) in.readValue(Date.class.getClassLoader());
        this.datetime = (Date) in.readValue(Date.class.getClassLoader());
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            DataField key = (DataField) in.readValue(DataField.class.getClassLoader());
            Object value = in.readValue(Object.class.getClassLoader());
            this.fields.put(key, value);
        }
    }

    @Override // com.ua.sdk.datapoint.DataPoint
    public Date getStartDatetime() {
        return this.startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    @Override // com.ua.sdk.datapoint.DataPoint
    public Date getDatetime() {
        return this.datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public void setValue(DataField dataField, Long value) {
        this.fields.put(dataField, value);
    }

    public void setValue(DataField dataField, Double value) {
        this.fields.put(dataField, value);
    }

    @Override // com.ua.sdk.datapoint.DataPoint
    public Long getValueLong(DataField dataField) {
        Object value = this.fields.get(dataField);
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Double) {
            return Long.valueOf(((Double) value).longValue());
        }
        return null;
    }

    @Override // com.ua.sdk.datapoint.DataPoint
    public Double getValueDouble(DataField dataField) {
        Object value = this.fields.get(dataField);
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Long) {
            return Double.valueOf(((Long) value).doubleValue());
        }
        return null;
    }

    public void reset() {
        this.startDatetime = null;
        this.datetime = null;
        this.fields.clear();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataPointImpl dataPoint = (DataPointImpl) o;
        if (this.datetime == null ? dataPoint.datetime != null : !this.datetime.equals(dataPoint.datetime)) {
            return false;
        }
        if (this.startDatetime == null ? dataPoint.startDatetime != null : !this.startDatetime.equals(dataPoint.startDatetime)) {
            return false;
        }
        if (this.fields != null) {
            if (this.fields.equals(dataPoint.fields)) {
                return true;
            }
        } else if (dataPoint.fields == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.startDatetime != null ? this.startDatetime.hashCode() : 0;
        return (((result * 31) + (this.datetime != null ? this.datetime.hashCode() : 0)) * 31) + (this.fields != null ? this.fields.hashCode() : 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.startDatetime);
        dest.writeValue(this.datetime);
        dest.writeInt(this.fields.size());
        for (DataField key : this.fields.keySet()) {
            dest.writeValue(key);
            dest.writeValue(this.fields.get(key));
        }
    }
}
