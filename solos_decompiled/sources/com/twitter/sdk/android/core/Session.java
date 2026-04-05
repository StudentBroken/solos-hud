package com.twitter.sdk.android.core;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.AuthToken;

/* JADX INFO: loaded from: classes62.dex */
public class Session<T extends AuthToken> {

    @SerializedName("auth_token")
    private final T authToken;

    @SerializedName("id")
    private final long id;

    public Session(T authToken, long id) {
        this.authToken = authToken;
        this.id = id;
    }

    public T getAuthToken() {
        return this.authToken;
    }

    public long getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        if (this.id != session.id) {
            return false;
        }
        if (this.authToken != null) {
            if (this.authToken.equals(session.authToken)) {
                return true;
            }
        } else if (session.authToken == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.authToken != null ? this.authToken.hashCode() : 0;
        return (result * 31) + ((int) (this.id ^ (this.id >>> 32)));
    }
}
