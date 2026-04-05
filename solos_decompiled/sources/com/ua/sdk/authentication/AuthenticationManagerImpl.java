package com.ua.sdk.authentication;

import android.content.SharedPreferences;
import android.os.SystemClock;
import com.ua.sdk.NetworkError;
import com.ua.sdk.Request;
import com.ua.sdk.Ua;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.concurrent.LoginRequest;
import com.ua.sdk.internal.Precondition;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class AuthenticationManagerImpl implements AuthenticationManager {
    protected static final String PREF_ACCESS_TOKEN = "mmdk_oauth2_access_token";
    protected static final String PREF_PREFIX = "mmdk_";
    protected static final String PREF_REFRESH_TIME = "mmdk_oauth2_refresh_time";
    protected static final String PREF_REFRESH_TOKEN = "mmdk_oauth2_refresh_token";
    protected static final int TIMEOUT_DELTA = 1200000;
    protected AuthenticationService authService;
    protected ExecutorService executorService;
    protected OAuth2Credentials oAuth2Credentials;
    private long refreshNanos = Long.MIN_VALUE;
    protected SharedPreferences sharedPrefs;

    public void init(AuthenticationService authService, ExecutorService executorService, SharedPreferences sharedPrefs) {
        this.authService = (AuthenticationService) Precondition.isNotNull(authService);
        this.executorService = (ExecutorService) Precondition.isNotNull(executorService);
        this.sharedPrefs = (SharedPreferences) Precondition.isNotNull(sharedPrefs);
        String token = sharedPrefs.getString(PREF_ACCESS_TOKEN, null);
        if (token != null) {
            String refreshToken = sharedPrefs.getString(PREF_REFRESH_TOKEN, null);
            long refreshTime = sharedPrefs.getLong(PREF_REFRESH_TIME, 0L);
            this.oAuth2Credentials = new OAuth2CredentialsImpl();
            this.oAuth2Credentials.setAccessToken(token);
            this.oAuth2Credentials.setRefreshToken(refreshToken);
            this.oAuth2Credentials.setExpiresAt(Long.valueOf(refreshTime));
        }
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public synchronized void refreshToken(long requestNanos) throws UaException {
        if (!isAuthenticated()) {
            UaLog.error("Can't refresh Oauth2Credentials, not authenticated.");
            throw new UaException(UaException.Code.NOT_AUTHENTICATED);
        }
        if (requestNanos > this.refreshNanos) {
            try {
                try {
                    OAuth2Credentials token = this.authService.refreshAuthentication(this.oAuth2Credentials);
                    this.refreshNanos = SystemClock.elapsedRealtime();
                    setOAuth2Credentials(token);
                    UaLog.debug("Oauth2Credentials have been refreshed");
                } catch (NetworkError e) {
                    if (e.getResponseCode() == 401) {
                        UaLog.error("Failed to refresh Oauth2Credentials.", (Throwable) e);
                    }
                    onLogout();
                    throw e;
                }
            } catch (UaException e2) {
                UaLog.error("Failed to refresh Oauth2Credentials.", (Throwable) e2);
                throw e2;
            }
        } else {
            UaLog.debug("Oauth2Credentials were already refreshed. Not refreshing again.");
        }
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public void sign(URLConnection conn, AuthenticationManager.AuthenticationType type) throws UaException {
        if (type != null) {
            switch (type) {
                case USER:
                    signAsUser(conn);
                    break;
                case CLIENT:
                    signAsClient(conn);
                    break;
            }
        }
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public synchronized void signAsUser(URLConnection conn) throws UaException {
        Precondition.isAuthenticated(this);
        if (this.oAuth2Credentials != null) {
            refreshUserTokenIfNeeded();
            signConnection(this.oAuth2Credentials, conn);
        }
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public synchronized void signAsClient(URLConnection conn) throws UaException {
        OAuth2Credentials token = this.authService.authenticateClient();
        signConnection(token, conn);
    }

    private synchronized void signConnection(OAuth2Credentials token, URLConnection conn) {
        conn.setRequestProperty("Authorization", "Bearer " + token.getAccessToken());
    }

    private synchronized void refreshUserTokenIfNeeded() throws UaException {
        if (this.oAuth2Credentials != null && this.oAuth2Credentials.getExpiresAt().longValue() - 1200000 <= System.currentTimeMillis()) {
            refreshToken(System.nanoTime());
        }
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public synchronized void login(String authorizationCode) throws UaException {
        UaLog.debug("Attempting login with authorization code.");
        OAuth2Credentials token = this.authService.authenticateUser(authorizationCode);
        setOAuth2Credentials(token);
        UaLog.debug("Successfully logged in using authorization code.");
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public Request login(final String authorizationCode, Ua.LoginCallback callback) {
        final LoginRequest request = new LoginRequest(callback);
        Future<?> future = this.executorService.submit(new Runnable() { // from class: com.ua.sdk.authentication.AuthenticationManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    AuthenticationManagerImpl.this.login(authorizationCode);
                    request.done(null, null);
                } catch (UaException e) {
                    AuthenticationManagerImpl.this.onLogout();
                    UaLog.error("Failed to log in with authorization code.", (Throwable) e);
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public boolean isAuthenticated() {
        return this.oAuth2Credentials != null;
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public synchronized void onLogout() {
        this.oAuth2Credentials = null;
        this.sharedPrefs.edit().remove(PREF_ACCESS_TOKEN).remove(PREF_REFRESH_TOKEN).remove(PREF_REFRESH_TIME).commit();
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public synchronized void setOAuth2Credentials(OAuth2Credentials oAuth2Credentials) {
        Precondition.isNotNull(oAuth2Credentials, "oAuth2Credentials");
        this.oAuth2Credentials = oAuth2Credentials;
        this.sharedPrefs.edit().putString(PREF_ACCESS_TOKEN, oAuth2Credentials.getAccessToken()).putString(PREF_REFRESH_TOKEN, oAuth2Credentials.getRefreshToken()).putLong(PREF_REFRESH_TIME, oAuth2Credentials.getExpiresAt().longValue()).commit();
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public OAuth2Credentials getOAuth2Credentials() {
        return this.oAuth2Credentials;
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public synchronized void requestUserAuthorization(String redirectUri) {
        this.authService.requestUserAuthorization(redirectUri);
    }

    @Override // com.ua.sdk.authentication.AuthenticationManager
    public synchronized String getUserAuthorizationUrl(String redirectUri) {
        return this.authService.getUserAuthorizationUrl(redirectUri);
    }
}
