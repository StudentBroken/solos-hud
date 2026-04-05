package com.ua.sdk.group.objective;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class GroupObjectiveList extends AbstractEntityList<GroupObjective> {
    public static Parcelable.Creator<GroupObjectiveList> CREATOR = new Parcelable.Creator<GroupObjectiveList>() { // from class: com.ua.sdk.group.objective.GroupObjectiveList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupObjectiveList createFromParcel(Parcel source) {
            return new GroupObjectiveList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupObjectiveList[] newArray(int size) {
            return new GroupObjectiveList[size];
        }
    };
    private static final String LIST_KEY = "group_objectives";

    public GroupObjectiveList() {
    }

    private GroupObjectiveList(Parcel in) {
        super(in);
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }
}
