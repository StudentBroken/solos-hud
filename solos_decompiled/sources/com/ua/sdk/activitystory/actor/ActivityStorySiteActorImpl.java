package com.ua.sdk.activitystory.actor;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.activitystory.ActivityStoryActor;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStorySiteActorImpl implements ActivityStoryActor {
    public static Parcelable.Creator<ActivityStorySiteActorImpl> CREATOR = new Parcelable.Creator<ActivityStorySiteActorImpl>() { // from class: com.ua.sdk.activitystory.actor.ActivityStorySiteActorImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStorySiteActorImpl createFromParcel(Parcel source) {
            return new ActivityStorySiteActorImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStorySiteActorImpl[] newArray(int size) {
            return new ActivityStorySiteActorImpl[size];
        }
    };

    @SerializedName("id")
    String mId;

    @Override // com.ua.sdk.activitystory.ActivityStoryActor
    public ActivityStoryActor.Type getType() {
        return ActivityStoryActor.Type.SITE;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActor
    public String getId() {
        return this.mId;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
    }

    public ActivityStorySiteActorImpl() {
    }

    private ActivityStorySiteActorImpl(Parcel in) {
        this.mId = in.readString();
    }
}
