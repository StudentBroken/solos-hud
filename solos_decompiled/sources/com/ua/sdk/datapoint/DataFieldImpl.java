package com.ua.sdk.datapoint;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class DataFieldImpl implements DataField {
    public static Parcelable.Creator<DataFieldImpl> CREATOR = new Parcelable.Creator<DataFieldImpl>() { // from class: com.ua.sdk.datapoint.DataFieldImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataFieldImpl createFromParcel(Parcel source) {
            return new DataFieldImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataFieldImpl[] newArray(int size) {
            return new DataFieldImpl[size];
        }
    };
    private DataUnits dataUnits;
    private String id;
    private String type;

    public DataFieldImpl() {
    }

    public DataFieldImpl(String id, String type, DataUnits dataUnits) {
        this.id = id;
        this.type = type;
        this.dataUnits = dataUnits;
    }

    private DataFieldImpl(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.dataUnits = (DataUnits) in.readValue(DataUnits.class.getClassLoader());
    }

    @Override // com.ua.sdk.datapoint.DataField
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override // com.ua.sdk.datapoint.DataField
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override // com.ua.sdk.datapoint.DataField
    public DataUnits getUnits() {
        return this.dataUnits;
    }

    public void setUnits(DataUnits dataUnits) {
        this.dataUnits = dataUnits;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataFieldImpl dataField = (DataFieldImpl) o;
        return this.id.equals(dataField.id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeValue(this.dataUnits);
    }
}
