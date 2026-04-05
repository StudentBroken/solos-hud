package com.digits.sdk.android;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/* JADX INFO: loaded from: classes18.dex */
class AuthConfig implements Parcelable, Serializable {
    public static final Parcelable.Creator<AuthConfig> CREATOR = new Parcelable.Creator<AuthConfig>() { // from class: com.digits.sdk.android.AuthConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AuthConfig createFromParcel(Parcel in) {
            return new AuthConfig(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AuthConfig[] newArray(int size) {
            return new AuthConfig[size];
        }
    };
    private static final long serialVersionUID = 5677912742763353323L;

    @SerializedName(DigitsClient.EXTRA_EMAIL)
    public boolean isEmailEnabled;

    @SerializedName("voice_enabled")
    public boolean isVoiceEnabled;

    @SerializedName("tos_update")
    public boolean tosUpdate;

    public AuthConfig() {
    }

    protected AuthConfig(Parcel in) {
        this.tosUpdate = in.readInt() == 1;
        this.isVoiceEnabled = in.readInt() == 1;
        this.isEmailEnabled = in.readInt() == 1;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tosUpdate ? 1 : 0);
        dest.writeInt(this.isVoiceEnabled ? 1 : 0);
        dest.writeInt(this.isEmailEnabled ? 1 : 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthConfig that = (AuthConfig) o;
        return this.tosUpdate == that.tosUpdate && this.isVoiceEnabled == that.isVoiceEnabled && this.isEmailEnabled == that.isEmailEnabled;
    }

    public int hashCode() {
        int result = this.tosUpdate ? 1 : 0;
        return (((result * 31) + (this.isVoiceEnabled ? 1 : 0)) * 31) + (this.isEmailEnabled ? 1 : 0);
    }
}
