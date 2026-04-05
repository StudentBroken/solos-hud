package com.twitter.sdk.android.core;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.internal.oauth.AuthHeaders;

/* JADX INFO: loaded from: classes62.dex */
public abstract class AuthToken implements AuthHeaders {

    @SerializedName("created_at")
    protected final long createdAt;

    public abstract boolean isExpired();

    public AuthToken() {
        this.createdAt = System.currentTimeMillis();
    }

    protected AuthToken(long createdAt) {
        this.createdAt = createdAt;
    }
}
