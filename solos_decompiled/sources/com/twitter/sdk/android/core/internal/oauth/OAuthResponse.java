package com.twitter.sdk.android.core.internal.oauth;

import android.os.Parcel;
import android.os.Parcelable;
import com.twitter.sdk.android.core.TwitterAuthToken;

/* JADX INFO: loaded from: classes62.dex */
public class OAuthResponse implements Parcelable {
    public static final Parcelable.Creator<OAuthResponse> CREATOR = new Parcelable.Creator<OAuthResponse>() { // from class: com.twitter.sdk.android.core.internal.oauth.OAuthResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OAuthResponse createFromParcel(Parcel in) {
            return new OAuthResponse(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OAuthResponse[] newArray(int size) {
            return new OAuthResponse[size];
        }
    };
    public final TwitterAuthToken authToken;
    public final long userId;
    public final String userName;

    public OAuthResponse(TwitterAuthToken authToken, String userName, long userId) {
        this.authToken = authToken;
        this.userName = userName;
        this.userId = userId;
    }

    private OAuthResponse(Parcel in) {
        this.authToken = (TwitterAuthToken) in.readParcelable(TwitterAuthToken.class.getClassLoader());
        this.userName = in.readString();
        this.userId = in.readLong();
    }

    public String toString() {
        return "authToken=" + this.authToken + ",userName=" + this.userName + ",userId=" + this.userId;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.authToken, flags);
        out.writeString(this.userName);
        out.writeLong(this.userId);
    }
}
