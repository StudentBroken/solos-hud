package com.facebook.appevents;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.internal.Utility;
import java.io.Serializable;

/* JADX INFO: loaded from: classes41.dex */
class AccessTokenAppIdPair implements Serializable {
    private static final long serialVersionUID = 1;
    private final String accessTokenString;
    private final String applicationId;

    public AccessTokenAppIdPair(AccessToken accessToken) {
        this(accessToken.getToken(), FacebookSdk.getApplicationId());
    }

    public AccessTokenAppIdPair(String accessTokenString, String applicationId) {
        this.accessTokenString = Utility.isNullOrEmpty(accessTokenString) ? null : accessTokenString;
        this.applicationId = applicationId;
    }

    public String getAccessTokenString() {
        return this.accessTokenString;
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    public int hashCode() {
        return (this.accessTokenString == null ? 0 : this.accessTokenString.hashCode()) ^ (this.applicationId != null ? this.applicationId.hashCode() : 0);
    }

    public boolean equals(Object o) {
        if (!(o instanceof AccessTokenAppIdPair)) {
            return false;
        }
        AccessTokenAppIdPair p = (AccessTokenAppIdPair) o;
        return Utility.areObjectsEqual(p.accessTokenString, this.accessTokenString) && Utility.areObjectsEqual(p.applicationId, this.applicationId);
    }

    static class SerializationProxyV1 implements Serializable {
        private static final long serialVersionUID = -2488473066578201069L;
        private final String accessTokenString;
        private final String appId;

        private SerializationProxyV1(String accessTokenString, String appId) {
            this.accessTokenString = accessTokenString;
            this.appId = appId;
        }

        private Object readResolve() {
            return new AccessTokenAppIdPair(this.accessTokenString, this.appId);
        }
    }

    private Object writeReplace() {
        return new SerializationProxyV1(this.accessTokenString, this.applicationId);
    }
}
