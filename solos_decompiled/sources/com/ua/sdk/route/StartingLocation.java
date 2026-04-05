package com.ua.sdk.route;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.kml.KmlPoint;

/* JADX INFO: loaded from: classes65.dex */
public class StartingLocation implements Parcelable {
    public static final Parcelable.Creator<StartingLocation> CREATOR = new Parcelable.Creator<StartingLocation>() { // from class: com.ua.sdk.route.StartingLocation.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StartingLocation createFromParcel(Parcel source) {
            return new StartingLocation(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StartingLocation[] newArray(int size) {
            return new StartingLocation[size];
        }
    };

    @SerializedName("coordinates")
    double[] coordinates;

    @SerializedName(ShareConstants.MEDIA_TYPE)
    String type;

    public StartingLocation() {
        this.type = KmlPoint.GEOMETRY_TYPE;
        this.coordinates = new double[2];
    }

    public StartingLocation(Double lat, Double lng) {
        this.type = KmlPoint.GEOMETRY_TYPE;
        this.coordinates = new double[2];
        this.coordinates[0] = lat.doubleValue();
        this.coordinates[1] = lng.doubleValue();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeValue(Double.valueOf(this.coordinates[0]));
        dest.writeValue(Double.valueOf(this.coordinates[1]));
    }

    private StartingLocation(Parcel in) {
        this.type = in.readString();
        if (this.coordinates == null) {
            this.coordinates = new double[2];
        }
        this.coordinates[0] = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
        this.coordinates[1] = ((Double) in.readValue(Double.class.getClassLoader())).doubleValue();
    }
}
