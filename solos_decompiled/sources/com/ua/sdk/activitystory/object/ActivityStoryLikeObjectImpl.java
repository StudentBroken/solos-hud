package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.activitystory.ActivityStoryLikeObject;
import com.ua.sdk.activitystory.ActivityStoryObject;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryLikeObjectImpl implements ActivityStoryLikeObject {
    public static Parcelable.Creator<ActivityStoryLikeObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryLikeObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryLikeObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryLikeObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryLikeObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryLikeObjectImpl[] newArray(int size) {
            return new ActivityStoryLikeObjectImpl[size];
        }
    };

    public ActivityStoryLikeObjectImpl() {
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryLikeObject, com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.LIKE;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
    }

    private ActivityStoryLikeObjectImpl(Parcel in) {
    }
}
