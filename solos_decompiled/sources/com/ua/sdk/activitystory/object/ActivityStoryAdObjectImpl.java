package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.activitystory.ActivityStoryAdObject;
import com.ua.sdk.activitystory.ActivityStoryObject;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryAdObjectImpl implements ActivityStoryAdObject {
    public static Parcelable.Creator<ActivityStoryAdObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryAdObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryAdObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryAdObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryAdObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryAdObjectImpl[] newArray(int size) {
            return new ActivityStoryAdObjectImpl[size];
        }
    };

    public ActivityStoryAdObjectImpl() {
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryAdObject, com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.AD;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
    }

    private ActivityStoryAdObjectImpl(Parcel in) {
    }
}
