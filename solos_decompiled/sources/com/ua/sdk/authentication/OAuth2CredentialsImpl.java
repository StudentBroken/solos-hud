package com.ua.sdk.authentication;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.Reference;

/* JADX INFO: loaded from: classes65.dex */
public class OAuth2CredentialsImpl implements OAuth2Credentials, Parcelable {
    public static final Parcelable.Creator<OAuth2CredentialsImpl> CREATOR = new Parcelable.Creator<OAuth2CredentialsImpl>() { // from class: com.ua.sdk.authentication.OAuth2CredentialsImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OAuth2CredentialsImpl createFromParcel(Parcel source) {
            return new OAuth2CredentialsImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OAuth2CredentialsImpl[] newArray(int size) {
            return new OAuth2CredentialsImpl[size];
        }
    };
    private String accessToken;
    private Long expiresAt;
    private String refreshToken;

    public OAuth2CredentialsImpl() {
    }

    @Override // com.ua.sdk.Resource
    public Reference getRef() {
        return null;
    }

    @Override // com.ua.sdk.authentication.OAuth2Credentials
    public String getAccessToken() {
        return this.accessToken;
    }

    @Override // com.ua.sdk.authentication.OAuth2Credentials
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override // com.ua.sdk.authentication.OAuth2Credentials
    public Long getExpiresAt() {
        return this.expiresAt;
    }

    @Override // com.ua.sdk.authentication.OAuth2Credentials
    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override // com.ua.sdk.authentication.OAuth2Credentials
    public String getRefreshToken() {
        return this.refreshToken;
    }

    @Override // com.ua.sdk.authentication.OAuth2Credentials
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeValue(this.expiresAt);
        dest.writeString(this.refreshToken);
    }

    private OAuth2CredentialsImpl(Parcel in) {
        this.accessToken = in.readString();
        this.expiresAt = (Long) in.readValue(Long.class.getClassLoader());
        this.refreshToken = in.readString();
    }
}
