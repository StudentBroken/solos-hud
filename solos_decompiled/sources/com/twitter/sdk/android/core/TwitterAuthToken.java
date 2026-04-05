package com.twitter.sdk.android.core;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterAuthToken extends AuthToken implements Parcelable {
    public static final Parcelable.Creator<TwitterAuthToken> CREATOR = new Parcelable.Creator<TwitterAuthToken>() { // from class: com.twitter.sdk.android.core.TwitterAuthToken.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TwitterAuthToken createFromParcel(Parcel in) {
            return new TwitterAuthToken(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TwitterAuthToken[] newArray(int size) {
            return new TwitterAuthToken[size];
        }
    };

    @SerializedName("secret")
    public final String secret;

    @SerializedName("token")
    public final String token;

    public TwitterAuthToken(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    TwitterAuthToken(String token, String secret, long createdAt) {
        super(createdAt);
        this.token = token;
        this.secret = secret;
    }

    private TwitterAuthToken(Parcel in) {
        this.token = in.readString();
        this.secret = in.readString();
    }

    @Override // com.twitter.sdk.android.core.AuthToken
    public boolean isExpired() {
        return false;
    }

    @Override // com.twitter.sdk.android.core.internal.oauth.AuthHeaders
    public Map<String, String> getAuthHeaders(TwitterAuthConfig authConfig, String method, String url, Map<String, String> postParams) {
        Map<String, String> headers = new HashMap<>(1);
        String authorizationHeader = new OAuth1aHeaders().getAuthorizationHeader(authConfig, this, null, method, url, postParams);
        headers.put("Authorization", authorizationHeader);
        return headers;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder().append("token=").append(this.token).append(",secret=").append(this.secret);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.token);
        out.writeString(this.secret);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TwitterAuthToken)) {
            return false;
        }
        TwitterAuthToken that = (TwitterAuthToken) o;
        if (this.secret == null ? that.secret != null : !this.secret.equals(that.secret)) {
            return false;
        }
        if (this.token != null) {
            if (this.token.equals(that.token)) {
                return true;
            }
        } else if (that.token == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.token != null ? this.token.hashCode() : 0;
        return (result * 31) + (this.secret != null ? this.secret.hashCode() : 0);
    }
}
