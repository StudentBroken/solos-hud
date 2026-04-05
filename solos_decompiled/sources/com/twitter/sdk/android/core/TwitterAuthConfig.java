package com.twitter.sdk.android.core;

import android.os.Parcel;
import android.os.Parcelable;
import com.twitter.sdk.android.core.internal.oauth.OAuth1aService;
import java.net.HttpURLConnection;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterAuthConfig implements Parcelable {
    public static final Parcelable.Creator<TwitterAuthConfig> CREATOR = new Parcelable.Creator<TwitterAuthConfig>() { // from class: com.twitter.sdk.android.core.TwitterAuthConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TwitterAuthConfig createFromParcel(Parcel in) {
            return new TwitterAuthConfig(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TwitterAuthConfig[] newArray(int size) {
            return new TwitterAuthConfig[size];
        }
    };
    public static final int DEFAULT_AUTH_REQUEST_CODE = 140;
    private final String consumerKey;
    private final String consumerSecret;

    public TwitterAuthConfig(String consumerKey, String consumerSecret) {
        if (consumerKey == null || consumerSecret == null) {
            throw new IllegalArgumentException("TwitterAuthConfig must not be created with null consumer key or secret.");
        }
        this.consumerKey = sanitizeAttribute(consumerKey);
        this.consumerSecret = sanitizeAttribute(consumerSecret);
    }

    private TwitterAuthConfig(Parcel in) {
        this.consumerKey = in.readString();
        this.consumerSecret = in.readString();
    }

    public void signRequest(TwitterAuthToken accessToken, HttpURLConnection request) {
        OAuth1aService.signRequest(this, accessToken, request, null);
    }

    public void signRequest(TwitterAuthToken accessToken, HttpURLConnection request, Map<String, String> postParams) {
        OAuth1aService.signRequest(this, accessToken, request, postParams);
    }

    public String getConsumerKey() {
        return this.consumerKey;
    }

    public String getConsumerSecret() {
        return this.consumerSecret;
    }

    public int getRequestCode() {
        return 140;
    }

    static String sanitizeAttribute(String input) {
        if (input != null) {
            return input.trim();
        }
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.consumerKey);
        out.writeString(this.consumerSecret);
    }
}
