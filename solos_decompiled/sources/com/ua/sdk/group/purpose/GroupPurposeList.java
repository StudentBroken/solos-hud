package com.ua.sdk.group.purpose;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class GroupPurposeList extends AbstractEntityList<GroupPurpose> {
    public static Parcelable.Creator<GroupPurposeList> CREATOR = new Parcelable.Creator<GroupPurposeList>() { // from class: com.ua.sdk.group.purpose.GroupPurposeList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupPurposeList createFromParcel(Parcel source) {
            return new GroupPurposeList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupPurposeList[] newArray(int size) {
            return new GroupPurposeList[size];
        }
    };
    private static final String LIST_KEY = "group_purposes";

    public GroupPurposeList() {
    }

    private GroupPurposeList(Parcel in) {
        super(in);
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }
}
