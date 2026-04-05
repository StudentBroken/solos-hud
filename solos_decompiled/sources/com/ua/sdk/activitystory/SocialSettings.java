package com.ua.sdk.activitystory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.BuildConfig;

/* JADX INFO: loaded from: classes65.dex */
public class SocialSettings implements Parcelable {
    public static final Parcelable.Creator<SocialSettings> CREATOR = new Parcelable.Creator<SocialSettings>() { // from class: com.ua.sdk.activitystory.SocialSettings.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SocialSettings createFromParcel(Parcel source) {
            return new SocialSettings(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SocialSettings[] newArray(int size) {
            return new SocialSettings[size];
        }
    };

    @SerializedName("facebook")
    Boolean facebook;

    @SerializedName(BuildConfig.ARTIFACT_ID)
    Boolean twitter;

    public Boolean getFacebook() {
        return this.facebook;
    }

    public void setFacebook(Boolean facebook) {
        this.facebook = facebook;
    }

    public Boolean getTwitter() {
        return this.twitter;
    }

    public void setTwitter(Boolean twitter) {
        this.twitter = twitter;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.facebook);
        dest.writeValue(this.twitter);
    }

    public SocialSettings() {
    }

    public SocialSettings(Builder init) {
        this.facebook = init.facebook;
        this.twitter = init.twitter;
    }

    private SocialSettings(Parcel in) {
        this.facebook = (Boolean) in.readValue(null);
        this.twitter = (Boolean) in.readValue(null);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SocialSettings that = (SocialSettings) o;
        if (this.facebook == null ? that.facebook != null : !this.facebook.equals(that.facebook)) {
            return false;
        }
        if (this.twitter != null) {
            if (this.twitter.equals(that.twitter)) {
                return true;
            }
        } else if (that.twitter == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.facebook != null ? this.facebook.hashCode() : 0;
        return (result * 31) + (this.twitter != null ? this.twitter.hashCode() : 0);
    }

    public static class Builder {
        Boolean facebook;
        Boolean twitter;

        public Builder setFacebookShare(Boolean facebookShare) {
            this.facebook = facebookShare;
            return this;
        }

        public Builder setTwitterShare(Boolean twitterShare) {
            this.twitter = twitterShare;
            return this;
        }

        public SocialSettings build() {
            return new SocialSettings(this);
        }
    }
}
