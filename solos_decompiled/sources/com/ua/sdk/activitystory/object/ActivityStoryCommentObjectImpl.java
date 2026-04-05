package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.kopin.pupil.ui.PageHelper;
import com.ua.sdk.activitystory.ActivityStoryCommentObject;
import com.ua.sdk.activitystory.ActivityStoryObject;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryCommentObjectImpl implements ActivityStoryCommentObject {
    public static Parcelable.Creator<ActivityStoryCommentObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryCommentObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryCommentObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryCommentObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryCommentObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryCommentObjectImpl[] newArray(int size) {
            return new ActivityStoryCommentObjectImpl[size];
        }
    };

    @SerializedName(PageHelper.TEXT_PART_TAG)
    String mText;

    public ActivityStoryCommentObjectImpl(String text) {
        this.mText = text;
    }

    public ActivityStoryCommentObjectImpl() {
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryCommentObject
    public String getText() {
        return this.mText;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryCommentObject, com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.COMMENT;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mText);
    }

    private ActivityStoryCommentObjectImpl(Parcel in) {
        this.mText = in.readString();
    }
}
