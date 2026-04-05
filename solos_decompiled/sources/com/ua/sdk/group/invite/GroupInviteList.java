package com.ua.sdk.group.invite;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class GroupInviteList extends AbstractEntityList<GroupInvite> {
    public static Parcelable.Creator<GroupInviteList> CREATOR = new Parcelable.Creator<GroupInviteList>() { // from class: com.ua.sdk.group.invite.GroupInviteList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupInviteList createFromParcel(Parcel source) {
            return new GroupInviteList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupInviteList[] newArray(int size) {
            return new GroupInviteList[size];
        }
    };
    private static final String LIST_KEY = "group_invites";

    public GroupInviteList() {
    }

    private GroupInviteList(Parcel in) {
        super(in);
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }
}
