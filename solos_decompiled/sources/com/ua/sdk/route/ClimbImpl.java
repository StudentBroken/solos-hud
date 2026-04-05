package com.ua.sdk.route;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.internal.ApiTransferObject;

/* JADX INFO: loaded from: classes65.dex */
public class ClimbImpl extends ApiTransferObject implements Climb, Parcelable {
    public static final Parcelable.Creator<ClimbImpl> CREATOR = new Parcelable.Creator<ClimbImpl>() { // from class: com.ua.sdk.route.ClimbImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ClimbImpl createFromParcel(Parcel source) {
            return new ClimbImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ClimbImpl[] newArray(int size) {
            return new ClimbImpl[size];
        }
    };

    @SerializedName("cat")
    String category;

    @SerializedName("change")
    Double change;

    @SerializedName("dis")
    Double distance;

    @SerializedName("end")
    Double end;

    @SerializedName("end_index")
    int endIndex;

    @SerializedName("elevation_max")
    Double maxElevation;

    @SerializedName("start")
    Double start;

    @SerializedName("start_index")
    int startIndex;

    @Override // com.ua.sdk.route.Climb
    public String getCategory() {
        return this.category;
    }

    @Override // com.ua.sdk.route.Climb
    public Double getChangeMeters() {
        return null;
    }

    @Override // com.ua.sdk.route.Climb
    public Double getDistanceMeters() {
        return null;
    }

    @Override // com.ua.sdk.route.Climb
    public Double getMaxElevation() {
        return null;
    }

    @Override // com.ua.sdk.route.Climb
    public Double getEnd() {
        return null;
    }

    @Override // com.ua.sdk.route.Climb
    public int getEndIndex() {
        return this.endIndex;
    }

    @Override // com.ua.sdk.route.Climb
    public Double getStart() {
        return null;
    }

    @Override // com.ua.sdk.route.Climb
    public int getStartIndex() {
        return this.startIndex;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeValue(this.change);
        dest.writeValue(this.distance);
        dest.writeValue(this.maxElevation);
        dest.writeValue(this.end);
        dest.writeInt(this.endIndex);
        dest.writeValue(this.start);
        dest.writeInt(this.startIndex);
    }

    public ClimbImpl() {
    }

    private ClimbImpl(Parcel in) {
        this.category = in.readString();
        this.change = (Double) in.readValue(Double.class.getClassLoader());
        this.distance = (Double) in.readValue(Double.class.getClassLoader());
        this.maxElevation = (Double) in.readValue(Double.class.getClassLoader());
        this.end = (Double) in.readValue(Double.class.getClassLoader());
        this.endIndex = in.readInt();
        this.start = (Double) in.readValue(Double.class.getClassLoader());
        this.startIndex = in.readInt();
    }
}
