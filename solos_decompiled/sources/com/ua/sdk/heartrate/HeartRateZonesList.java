package com.ua.sdk.heartrate;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class HeartRateZonesList extends AbstractEntityList<HeartRateZones> {
    public static Parcelable.Creator<HeartRateZonesList> CREATOR = new Parcelable.Creator<HeartRateZonesList>() { // from class: com.ua.sdk.heartrate.HeartRateZonesList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateZonesList createFromParcel(Parcel source) {
            return new HeartRateZonesList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateZonesList[] newArray(int size) {
            return new HeartRateZonesList[size];
        }
    };
    private static final String LIST_KEY = "heart_rate_zones";

    public HeartRateZonesList() {
    }

    private HeartRateZonesList(Parcel in) {
        super(in);
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }
}
