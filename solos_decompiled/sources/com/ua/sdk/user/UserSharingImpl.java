package com.ua.sdk.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.BuildConfig;

/* JADX INFO: loaded from: classes65.dex */
public class UserSharingImpl implements UserSharing, Parcelable {
    public static Parcelable.Creator<UserSharingImpl> CREATOR = new Parcelable.Creator<UserSharingImpl>() { // from class: com.ua.sdk.user.UserSharingImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserSharingImpl createFromParcel(Parcel source) {
            return new UserSharingImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserSharingImpl[] newArray(int size) {
            return new UserSharingImpl[size];
        }
    };

    @SerializedName("facebook")
    Boolean facebook;

    @SerializedName(BuildConfig.ARTIFACT_ID)
    Boolean twitter;

    public UserSharingImpl() {
    }

    @Override // com.ua.sdk.user.UserSharing
    public Boolean getTwitter() {
        return this.twitter;
    }

    @Override // com.ua.sdk.user.UserSharing
    public boolean isTwitter() {
        return this.twitter != null && this.twitter.booleanValue();
    }

    @Override // com.ua.sdk.user.UserSharing
    public void setTwitter(Boolean twitter) {
        this.twitter = twitter;
    }

    @Override // com.ua.sdk.user.UserSharing
    public Boolean getFacebook() {
        return this.facebook;
    }

    @Override // com.ua.sdk.user.UserSharing
    public boolean isFacebook() {
        return this.facebook != null && this.facebook.booleanValue();
    }

    @Override // com.ua.sdk.user.UserSharing
    public void setFacebook(Boolean facebook) {
        this.facebook = facebook;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean facebook;
        private Boolean twitter;

        public Builder setTwitter(Boolean twitter) {
            this.twitter = twitter;
            return this;
        }

        public Builder setFacebook(Boolean facebook) {
            this.facebook = facebook;
            return this;
        }

        public UserSharingImpl build() {
            UserSharingImpl answer = new UserSharingImpl();
            answer.setTwitter(this.twitter);
            answer.setFacebook(this.facebook);
            return answer;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.twitter);
        dest.writeValue(this.facebook);
    }

    private UserSharingImpl(Parcel in) {
        this.twitter = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.facebook = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }
}
