package com.ua.sdk.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes65.dex */
public class LocationImpl implements Location {
    public static Parcelable.Creator<LocationImpl> CREATOR = new Parcelable.Creator<LocationImpl>() { // from class: com.ua.sdk.location.LocationImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocationImpl createFromParcel(Parcel source) {
            return new LocationImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocationImpl[] newArray(int size) {
            return new LocationImpl[size];
        }
    };

    @SerializedName("address")
    String address;

    @SerializedName("country")
    String country;

    @SerializedName("locality")
    String locality;

    @SerializedName("region")
    String region;

    public LocationImpl() {
    }

    public LocationImpl(String country, String region, String locality, String address) {
        this.country = country;
        this.region = region;
        this.locality = locality;
        this.address = address;
    }

    @Override // com.ua.sdk.location.Location
    public String getCountry() {
        return this.country;
    }

    @Override // com.ua.sdk.location.Location
    public void setCountry(String country) {
        this.country = country;
    }

    @Override // com.ua.sdk.location.Location
    public String getRegion() {
        return this.region;
    }

    @Override // com.ua.sdk.location.Location
    public void setRegion(String region) {
        this.region = region;
    }

    @Override // com.ua.sdk.location.Location
    public String getLocality() {
        return this.locality;
    }

    @Override // com.ua.sdk.location.Location
    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override // com.ua.sdk.location.Location
    public String getAddress() {
        return this.address;
    }

    @Override // com.ua.sdk.location.Location
    public void setAddress(String address) {
        this.address = address;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.region);
        dest.writeString(this.locality);
        dest.writeString(this.address);
    }

    private LocationImpl(Parcel in) {
        this.country = in.readString();
        this.region = in.readString();
        this.locality = in.readString();
        this.address = in.readString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationImpl)) {
            return false;
        }
        LocationImpl location = (LocationImpl) o;
        if (this.address == null ? location.address != null : !this.address.equals(location.address)) {
            return false;
        }
        if (this.country == null ? location.country != null : !this.country.equals(location.country)) {
            return false;
        }
        if (this.locality == null ? location.locality != null : !this.locality.equals(location.locality)) {
            return false;
        }
        if (this.region != null) {
            if (this.region.equals(location.region)) {
                return true;
            }
        } else if (location.region == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.country != null ? this.country.hashCode() : 0;
        return (((((result * 31) + (this.region != null ? this.region.hashCode() : 0)) * 31) + (this.locality != null ? this.locality.hashCode() : 0)) * 31) + (this.address != null ? this.address.hashCode() : 0);
    }
}
