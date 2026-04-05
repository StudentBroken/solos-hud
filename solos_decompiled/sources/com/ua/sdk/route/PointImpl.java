package com.ua.sdk.route;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.internal.ApiTransferObject;

/* JADX INFO: loaded from: classes65.dex */
public class PointImpl extends ApiTransferObject implements Point, Parcelable {
    public static final Parcelable.Creator<PointImpl> CREATOR = new Parcelable.Creator<PointImpl>() { // from class: com.ua.sdk.route.PointImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PointImpl createFromParcel(Parcel source) {
            return new PointImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PointImpl[] newArray(int size) {
            return new PointImpl[size];
        }
    };

    @SerializedName("dis")
    Double distance;

    @SerializedName("elv")
    Double elevation;

    @SerializedName("lat")
    Double latitude;

    @SerializedName("lng")
    Double longitude;

    public PointImpl(Double latitude, Double longitude, Double elevation, Double distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.distance = distance;
    }

    @Override // com.ua.sdk.route.Point
    public Double getLatitude() {
        return this.latitude;
    }

    @Override // com.ua.sdk.route.Point
    public Double getLongitude() {
        return this.longitude;
    }

    @Override // com.ua.sdk.route.Point
    public Double getElevation() {
        return this.elevation;
    }

    @Override // com.ua.sdk.route.Point
    public Double getDistanceMeters() {
        return this.distance;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
        dest.writeValue(this.elevation);
        dest.writeValue(this.distance);
    }

    private PointImpl(Parcel in) {
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.elevation = (Double) in.readValue(Double.class.getClassLoader());
        this.distance = (Double) in.readValue(Double.class.getClassLoader());
    }
}
