package com.ua.sdk.datasourceidentifier;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.device.Device;

/* JADX INFO: loaded from: classes65.dex */
public class DataSourceIdentifierBuilderImpl implements DataSourceIdentifierBuilder {
    public static final Parcelable.Creator<DataSourceIdentifierBuilderImpl> CREATOR = new Parcelable.Creator<DataSourceIdentifierBuilderImpl>() { // from class: com.ua.sdk.datasourceidentifier.DataSourceIdentifierBuilderImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataSourceIdentifierBuilderImpl createFromParcel(Parcel source) {
            return new DataSourceIdentifierBuilderImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataSourceIdentifierBuilderImpl[] newArray(int size) {
            return new DataSourceIdentifierBuilderImpl[size];
        }
    };
    Device device;
    String name;

    @Override // com.ua.sdk.datasourceidentifier.DataSourceIdentifierBuilder
    public DataSourceIdentifierBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override // com.ua.sdk.datasourceidentifier.DataSourceIdentifierBuilder
    public DataSourceIdentifierBuilder setDevice(Device device) {
        this.device = device;
        return this;
    }

    @Override // com.ua.sdk.datasourceidentifier.DataSourceIdentifierBuilder
    public DataSourceIdentifier build() {
        if (this.name == null) {
            throw new IllegalArgumentException("A name must be set!");
        }
        if (this.device == null) {
            throw new IllegalArgumentException("A device must be set!");
        }
        return new DataSourceIdentifierImpl(this.device, this.name);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.device, flags);
    }

    public DataSourceIdentifierBuilderImpl() {
    }

    private DataSourceIdentifierBuilderImpl(Parcel in) {
        this.name = in.readString();
        this.device = (Device) in.readParcelable(Device.class.getClassLoader());
    }
}
