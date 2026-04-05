package com.ua.sdk.authentication;

import com.ua.sdk.Request;
import com.ua.sdk.Ua;
import com.ua.sdk.UaException;
import java.net.URLConnection;

/* JADX INFO: loaded from: classes65.dex */
public interface AuthenticationManager {

    public enum AuthenticationType {
        NONE,
        USER,
        CLIENT
    }

    OAuth2Credentials getOAuth2Credentials();

    String getUserAuthorizationUrl(String str);

    boolean isAuthenticated();

    Request login(String str, Ua.LoginCallback loginCallback);

    void login(String str) throws UaException;

    void onLogout();

    void refreshToken(long j) throws UaException;

    void requestUserAuthorization(String str);

    void setOAuth2Credentials(OAuth2Credentials oAuth2Credentials);

    void sign(URLConnection uRLConnection, AuthenticationType authenticationType) throws UaException;

    void signAsClient(URLConnection uRLConnection) throws UaException;

    void signAsUser(URLConnection uRLConnection) throws UaException;
}
