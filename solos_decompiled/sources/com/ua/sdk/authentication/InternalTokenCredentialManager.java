package com.ua.sdk.authentication;

import com.twitter.sdk.android.BuildConfig;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface InternalTokenCredentialManager {
    OAuth2Credentials createForLogin(TokenType tokenType, String str, String str2) throws UaException;

    OAuth2Credentials updateForSync(TokenType tokenType, String str, String str2) throws UaException;

    public enum TokenType {
        FACEBOOK("facebook"),
        TWITTER(BuildConfig.ARTIFACT_ID);

        private String name;

        TokenType(String name) {
            this.name = name;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }
}
