package com.ua.sdk.group.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class GroupUserList extends AbstractEntityList<GroupUser> {
    public static Parcelable.Creator<GroupUserList> CREATOR = new Parcelable.Creator<GroupUserList>() { // from class: com.ua.sdk.group.user.GroupUserList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupUserList createFromParcel(Parcel source) {
            return new GroupUserList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupUserList[] newArray(int size) {
            return new GroupUserList[size];
        }
    };
    private static final String LIST_KEY = "group_users";

    public GroupUserList() {
    }

    private GroupUserList(Parcel in) {
        super(in);
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }
}
