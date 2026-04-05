package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.kopin.pupil.ui.PageHelper;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.activitystory.ActivityStoryStatusObject;
import com.ua.sdk.privacy.Privacy;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryStatusObjectImpl implements ActivityStoryStatusObject {
    public static Parcelable.Creator<ActivityStoryStatusObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryStatusObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryStatusObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryStatusObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryStatusObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryStatusObjectImpl[] newArray(int size) {
            return new ActivityStoryStatusObjectImpl[size];
        }
    };

    @SerializedName(ShareConstants.WEB_DIALOG_PARAM_PRIVACY)
    Privacy mPrivacy;

    @SerializedName(PageHelper.TEXT_PART_TAG)
    String mText;

    public ActivityStoryStatusObjectImpl(String text, Privacy privacy) {
        this.mText = text;
        this.mPrivacy = privacy;
    }

    public ActivityStoryStatusObjectImpl() {
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryStatusObject
    public String getText() {
        return this.mText;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryStatusObject
    public Privacy getPrivacy() {
        return this.mPrivacy;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryStatusObject, com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.STATUS;
    }

    public void setPrivacy(Privacy privacy) {
        this.mPrivacy = privacy;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mText);
        dest.writeParcelable(this.mPrivacy, flags);
    }

    protected ActivityStoryStatusObjectImpl(Parcel in) {
        this.mText = in.readString();
        this.mPrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
    }
}
