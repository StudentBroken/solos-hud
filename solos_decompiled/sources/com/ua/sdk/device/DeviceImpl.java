package com.ua.sdk.device;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class DeviceImpl extends ApiTransferObject implements Device {
    public static final Parcelable.Creator<DeviceImpl> CREATOR = new Parcelable.Creator<DeviceImpl>() { // from class: com.ua.sdk.device.DeviceImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceImpl createFromParcel(Parcel source) {
            return new DeviceImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceImpl[] newArray(int size) {
            return new DeviceImpl[size];
        }
    };

    @SerializedName("description")
    String description;

    @SerializedName("manufacturer")
    String manufacturer;

    @SerializedName("model")
    String model;

    @SerializedName("name")
    String name;

    public DeviceImpl() {
    }

    public DeviceImpl(String name, String manufacturer, String description, String model) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.model = model;
    }

    @Override // com.ua.sdk.device.Device
    public String getName() {
        return this.name;
    }

    @Override // com.ua.sdk.device.Device
    public String getManufacturer() {
        return this.manufacturer;
    }

    @Override // com.ua.sdk.device.Device
    public String getDescription() {
        return this.description;
    }

    @Override // com.ua.sdk.device.Device
    public String getModel() {
        return this.model;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef getRef() {
        Link link = getLink("self");
        if (link == null) {
            return null;
        }
        return new LinkEntityRef(link.getId(), link.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.manufacturer);
        dest.writeString(this.description);
        dest.writeString(this.model);
    }

    private DeviceImpl(Parcel in) {
        this.name = in.readString();
        this.manufacturer = in.readString();
        this.description = in.readString();
        this.model = in.readString();
    }
}
