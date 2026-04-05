package com.ua.sdk.datapoint;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class DataTypeImpl implements DataType {
    public static Parcelable.Creator<DataTypeImpl> CREATOR = new Parcelable.Creator<DataTypeImpl>() { // from class: com.ua.sdk.datapoint.DataTypeImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataTypeImpl createFromParcel(Parcel source) {
            return new DataTypeImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataTypeImpl[] newArray(int size) {
            return new DataTypeImpl[size];
        }
    };
    private String description;
    private List<DataField> fields;
    private String id;
    private DataPeriod period;

    public DataTypeImpl() {
    }

    public DataTypeImpl(String id, DataPeriod period, String description, List<DataField> fields) {
        this.id = id;
        this.period = period;
        this.description = description;
        this.fields = fields;
    }

    private DataTypeImpl(Parcel in) {
        this.id = in.readString();
        this.period = (DataPeriod) in.readValue(DataPeriod.class.getClassLoader());
        this.description = in.readString();
        this.fields = new ArrayList();
        in.readList(this.fields, DataField.class.getClassLoader());
    }

    @Override // com.ua.sdk.datapoint.DataType
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override // com.ua.sdk.datapoint.DataType
    public DataPeriod getPeriod() {
        return this.period;
    }

    public void setPeriod(DataPeriod period) {
        this.period = period;
    }

    @Override // com.ua.sdk.datapoint.DataType
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override // com.ua.sdk.datapoint.DataType
    public List<DataField> getFields() {
        return this.fields;
    }

    public void setFields(List<DataField> fields) {
        this.fields = fields;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataTypeImpl dataType = (DataTypeImpl) o;
        return this.id.equals(dataType.id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    @Override // com.ua.sdk.Resource
    public DataTypeRef getRef() {
        if (this.id == null) {
            return null;
        }
        return DataTypeRef.getBuilder().setId(this.id).build();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeValue(this.period);
        dest.writeString(this.description);
        dest.writeList(this.fields);
    }
}
