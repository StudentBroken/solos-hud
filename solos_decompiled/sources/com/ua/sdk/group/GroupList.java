package com.ua.sdk.group;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class GroupList extends AbstractEntityList<Group> implements Parcelable {
    public static final Parcelable.Creator<GroupList> CREATOR = new Parcelable.Creator<GroupList>() { // from class: com.ua.sdk.group.GroupList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupList createFromParcel(Parcel source) {
            return new GroupList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupList[] newArray(int size) {
            return new GroupList[size];
        }
    };

    public GroupList() {
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return "groups";
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    private GroupList(Parcel in) {
        super(in);
    }
}
