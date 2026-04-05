package com.ua.sdk.authentication;

import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface AuthenticationService {
    OAuth2Credentials authenticateClient() throws UaException;

    OAuth2Credentials authenticateUser(CharSequence charSequence, CharSequence charSequence2) throws UaException;

    OAuth2Credentials authenticateUser(String str) throws UaException;

    String getUserAuthorizationUrl(String str);

    OAuth2Credentials refreshAuthentication(OAuth2Credentials oAuth2Credentials) throws UaException;

    void requestUserAuthorization(String str);
}
