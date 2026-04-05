package com.ua.sdk.activitystory.target;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.activitystory.ActivityStoryTarget;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryUnknownTarget implements ActivityStoryTarget {
    public static Parcelable.Creator<ActivityStoryUnknownTarget> CREATOR = new Parcelable.Creator<ActivityStoryUnknownTarget>() { // from class: com.ua.sdk.activitystory.target.ActivityStoryUnknownTarget.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryUnknownTarget createFromParcel(Parcel source) {
            return new ActivityStoryUnknownTarget(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryUnknownTarget[] newArray(int size) {
            return new ActivityStoryUnknownTarget[size];
        }
    };

    @SerializedName("id")
    String id;

    @SerializedName(ShareConstants.MEDIA_TYPE)
    ActivityStoryTarget.Type type;

    public ActivityStoryUnknownTarget() {
    }

    private ActivityStoryUnknownTarget(Parcel in) {
        this.id = in.readString();
        this.type = (ActivityStoryTarget.Type) in.readValue(ActivityStoryTarget.Type.class.getClassLoader());
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTarget
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryTarget
    public ActivityStoryTarget.Type getType() {
        return this.type;
    }

    public void setType(ActivityStoryTarget.Type type) {
        this.type = type;
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
