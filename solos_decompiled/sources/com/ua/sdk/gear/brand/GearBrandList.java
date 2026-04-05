package com.ua.sdk.gear.brand;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class GearBrandList extends AbstractEntityList<GearBrand> {
    public static final Parcelable.Creator<GearBrandList> CREATOR = new Parcelable.Creator<GearBrandList>() { // from class: com.ua.sdk.gear.brand.GearBrandList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearBrandList createFromParcel(Parcel source) {
            return new GearBrandList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearBrandList[] newArray(int size) {
            return new GearBrandList[size];
        }
    };

    public GearBrandList() {
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return "gearbrand";
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    private GearBrandList(Parcel in) {
        super(in);
    }
}
