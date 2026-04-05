package com.ua.sdk.gear;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class GearList extends AbstractEntityList<Gear> {
    public static final Parcelable.Creator<GearList> CREATOR = new Parcelable.Creator<GearList>() { // from class: com.ua.sdk.gear.GearList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearList createFromParcel(Parcel source) {
            return new GearList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearList[] newArray(int size) {
            return new GearList[size];
        }
    };
    private static final String LIST_KEY = "gear";

    public GearList() {
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    private GearList(Parcel in) {
        super(in);
    }
}
