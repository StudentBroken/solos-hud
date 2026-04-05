package com.ua.sdk.authentication;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class InternalTokenCredentialImpl implements InternalTokenCredential, Parcelable {
    public static final Parcelable.Creator<InternalTokenCredentialImpl> CREATOR = new Parcelable.Creator<InternalTokenCredentialImpl>() { // from class: com.ua.sdk.authentication.InternalTokenCredentialImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InternalTokenCredentialImpl createFromParcel(Parcel source) {
            return new InternalTokenCredentialImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InternalTokenCredentialImpl[] newArray(int size) {
            return new InternalTokenCredentialImpl[size];
        }
    };
    private String secret;
    private String token;
    private String tokenType;

    @Override // com.ua.sdk.authentication.InternalTokenCredential
    public String getTokenType() {
        return this.tokenType;
    }

    @Override // com.ua.sdk.authentication.InternalTokenCredential
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override // com.ua.sdk.authentication.InternalTokenCredential
    public String getToken() {
        return this.token;
    }

    @Override // com.ua.sdk.authentication.InternalTokenCredential
    public void setToken(String token) {
        this.token = token;
    }

    @Override // com.ua.sdk.authentication.InternalTokenCredential
    public String getSecret() {
        return this.secret;
    }

    @Override // com.ua.sdk.authentication.InternalTokenCredential
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenType);
        dest.writeString(this.token);
        dest.writeString(this.secret);
    }

    public InternalTokenCredentialImpl() {
    }

    private InternalTokenCredentialImpl(Parcel in) {
        this.tokenType = in.readString();
        this.token = in.readString();
        this.secret = in.readString();
    }
}
