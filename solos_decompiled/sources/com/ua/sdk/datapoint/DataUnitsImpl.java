package com.ua.sdk.datapoint;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class DataUnitsImpl implements DataUnits {
    public static Parcelable.Creator<DataUnitsImpl> CREATOR = new Parcelable.Creator<DataUnitsImpl>() { // from class: com.ua.sdk.datapoint.DataUnitsImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataUnitsImpl createFromParcel(Parcel source) {
            return new DataUnitsImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataUnitsImpl[] newArray(int size) {
            return new DataUnitsImpl[size];
        }
    };
    private String name;
    private String symbol;

    public DataUnitsImpl(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    private DataUnitsImpl(Parcel in) {
        this.name = in.readString();
        this.symbol = in.readString();
    }

    @Override // com.ua.sdk.datapoint.DataUnits
    public String getSymbol() {
        return this.symbol;
    }

    @Override // com.ua.sdk.datapoint.DataUnits
    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataUnitsImpl dataUnits = (DataUnitsImpl) o;
        return this.name.equals(dataUnits.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.symbol);
    }
}
