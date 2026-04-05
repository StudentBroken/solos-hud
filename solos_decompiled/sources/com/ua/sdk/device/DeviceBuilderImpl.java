package com.ua.sdk.device;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class DeviceBuilderImpl implements DeviceBuilder {
    public static final Parcelable.Creator<DeviceBuilderImpl> CREATOR = new Parcelable.Creator<DeviceBuilderImpl>() { // from class: com.ua.sdk.device.DeviceBuilderImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceBuilderImpl createFromParcel(Parcel source) {
            return new DeviceBuilderImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceBuilderImpl[] newArray(int size) {
            return new DeviceBuilderImpl[size];
        }
    };
    String description;
    String manufacturer;
    String model;
    String name;

    @Override // com.ua.sdk.device.DeviceBuilder
    public DeviceBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override // com.ua.sdk.device.DeviceBuilder
    public DeviceBuilder setModel(String model) {
        this.model = model;
        return this;
    }

    @Override // com.ua.sdk.device.DeviceBuilder
    public DeviceBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override // com.ua.sdk.device.DeviceBuilder
    public DeviceBuilder setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    @Override // com.ua.sdk.device.DeviceBuilder
    public Device build() {
        if (this.name == null) {
            throw new IllegalArgumentException("A name must be set!");
        }
        if (this.model == null) {
            throw new IllegalArgumentException("A model must be set!");
        }
        if (this.manufacturer == null) {
            throw new IllegalArgumentException("A manufacturer must be set!");
        }
        return new DeviceImpl(this.name, this.manufacturer, this.description, this.model);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.model);
        dest.writeString(this.description);
        dest.writeString(this.manufacturer);
    }

    public DeviceBuilderImpl() {
    }

    private DeviceBuilderImpl(Parcel in) {
        this.name = in.readString();
        this.model = in.readString();
        this.description = in.readString();
        this.manufacturer = in.readString();
    }
}
