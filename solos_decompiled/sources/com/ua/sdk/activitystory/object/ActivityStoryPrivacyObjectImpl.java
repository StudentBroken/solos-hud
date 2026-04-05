package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.privacy.PrivacyHelper;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryPrivacyObjectImpl implements ActivityStoryObject {
    public static Parcelable.Creator<ActivityStoryPrivacyObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryPrivacyObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryPrivacyObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryPrivacyObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryPrivacyObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryPrivacyObjectImpl[] newArray(int size) {
            return new ActivityStoryPrivacyObjectImpl[size];
        }
    };

    @SerializedName(ShareConstants.WEB_DIALOG_PARAM_PRIVACY)
    Privacy privacy;

    @SerializedName(ShareConstants.MEDIA_TYPE)
    ActivityStoryObject.Type type;

    public ActivityStoryPrivacyObjectImpl() {
    }

    private ActivityStoryPrivacyObjectImpl(Builder init) {
        this.type = init.type;
        this.privacy = init.privacy;
    }

    private ActivityStoryPrivacyObjectImpl(Parcel in) {
        this.type = (ActivityStoryObject.Type) in.readSerializable();
        this.privacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return this.type;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.type);
        dest.writeParcelable(this.privacy, flags);
    }

    public static class Builder {
        final Privacy privacy;
        final ActivityStoryObject.Type type;

        public Builder(ActivityStoryObject.Type type, Privacy.Level privacyLevel) {
            this.type = type;
            this.privacy = PrivacyHelper.getPrivacy(privacyLevel);
        }

        public ActivityStoryPrivacyObjectImpl build() {
            return new ActivityStoryPrivacyObjectImpl(this);
        }
    }
}
