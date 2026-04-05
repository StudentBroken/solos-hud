package com.ua.sdk.activitystory.actor;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.activitystory.ActivityStoryActor;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryUnknownActorImpl implements ActivityStoryActor {
    public static Parcelable.Creator<ActivityStoryUnknownActorImpl> CREATOR = new Parcelable.Creator<ActivityStoryUnknownActorImpl>() { // from class: com.ua.sdk.activitystory.actor.ActivityStoryUnknownActorImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryUnknownActorImpl createFromParcel(Parcel source) {
            return new ActivityStoryUnknownActorImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryUnknownActorImpl[] newArray(int size) {
            return new ActivityStoryUnknownActorImpl[size];
        }
    };

    public ActivityStoryUnknownActorImpl() {
    }

    private ActivityStoryUnknownActorImpl(Parcel in) {
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActor
    public ActivityStoryActor.Type getType() {
        return ActivityStoryActor.Type.UNKNOWN;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActor
    public String getId() {
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
    }
}
