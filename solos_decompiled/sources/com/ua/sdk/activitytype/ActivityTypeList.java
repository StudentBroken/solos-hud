package com.ua.sdk.activitytype;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTypeList extends AbstractEntityList<ActivityType> {
    public static Parcelable.Creator<ActivityTypeList> CREATOR = new Parcelable.Creator<ActivityTypeList>() { // from class: com.ua.sdk.activitytype.ActivityTypeList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityTypeList createFromParcel(Parcel source) {
            return new ActivityTypeList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityTypeList[] newArray(int size) {
            return new ActivityTypeList[size];
        }
    };
    private static final String LIST_KEY = "activity_types";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }

    public ActivityTypeList() {
    }

    private ActivityTypeList(Parcel in) {
        super(in);
    }
}
