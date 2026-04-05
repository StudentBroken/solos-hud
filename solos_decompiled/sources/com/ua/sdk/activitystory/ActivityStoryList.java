package com.ua.sdk.activitystory;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryList extends AbstractEntityList<ActivityStory> {
    public static Parcelable.Creator<ActivityStoryList> CREATOR = new Parcelable.Creator<ActivityStoryList>() { // from class: com.ua.sdk.activitystory.ActivityStoryList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryList createFromParcel(Parcel source) {
            return new ActivityStoryList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryList[] newArray(int size) {
            return new ActivityStoryList[size];
        }
    };
    private static final String LIST_KEY = "activity_stories";

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

    public ActivityStoryList() {
    }

    private ActivityStoryList(Parcel in) {
        super(in);
    }
}
