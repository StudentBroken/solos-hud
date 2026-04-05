package com.ua.sdk.heartrate;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;

/* JADX INFO: loaded from: classes65.dex */
public class HeartRateZone extends ApiTransferObject implements Parcelable {
    public static final Parcelable.Creator<HeartRateZone> CREATOR = new Parcelable.Creator<HeartRateZone>() { // from class: com.ua.sdk.heartrate.HeartRateZone.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateZone createFromParcel(Parcel source) {
            return new HeartRateZone(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateZone[] newArray(int size) {
            return new HeartRateZone[size];
        }
    };

    @SerializedName("end")
    int end;

    @SerializedName("name")
    private String name;
    private transient EntityRef<HeartRateZones> selfRef;

    @SerializedName("start")
    private int start;

    public HeartRateZone() {
    }

    public HeartRateZone(String name, int start, int end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    private HeartRateZone(Parcel in) {
        super(in);
        this.name = in.readString();
        this.start = in.readInt();
        this.end = in.readInt();
    }

    public String getName() {
        return this.name;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeInt(this.start);
        dest.writeInt(this.end);
    }
}
