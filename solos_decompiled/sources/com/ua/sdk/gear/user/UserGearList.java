package com.ua.sdk.gear.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class UserGearList extends AbstractEntityList<UserGear> implements Parcelable {
    public static final Parcelable.Creator<UserGearList> CREATOR = new Parcelable.Creator<UserGearList>() { // from class: com.ua.sdk.gear.user.UserGearList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserGearList createFromParcel(Parcel source) {
            return new UserGearList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserGearList[] newArray(int size) {
            return new UserGearList[size];
        }
    };

    public UserGearList() {
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return "usergear";
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    private UserGearList(Parcel in) {
        super(in);
    }
}
