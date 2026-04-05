package com.ua.sdk.activitystory.target;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.activitystory.ActivityStoryTarget;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryGroupTarget implements ActivityStoryTarget {
    public static Parcelable.Creator<ActivityStoryGroupTarget> CREATOR = new Parcelable.Creator<ActivityStoryGroupTarget>() { // from class: com.ua.sdk.activitystory.target.ActivityStoryGroupTarget.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryGroupTarget createFromParcel(Parcel source) {
            return new ActivityStoryGroupTarget(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryGroupTarget[] newArray(int size) {
            return new ActivityStoryGroupTarget[size];
        }
    };

    @SerializedName("id")
    String id;

    @SerializedName(ShareConstants.MEDIA_TYPE)
    ActivityStoryTarget.Type type;

    @Override // com.ua.sdk.activitystory.ActivityStoryTarget
    public String getId() {
        return this.id;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTarget
    public ActivityStoryTarget.Type getType() {
        return ActivityStoryTarget.Type.GROUP;
    }

    public ActivityStoryGroupTarget() {
    }

    public ActivityStoryGroupTarget(String id) {
        this.id = id;
        this.type = ActivityStoryTarget.Type.GROUP;
    }

    private ActivityStoryGroupTarget(Parcel in) {
        this.id = in.readString();
        this.type = (ActivityStoryTarget.Type) in.readValue(ActivityStoryTarget.Type.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeValue(this.type);
    }
}
