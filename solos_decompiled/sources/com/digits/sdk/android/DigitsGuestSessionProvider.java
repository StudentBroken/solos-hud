package com.digits.sdk.android;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.SessionProvider;
import com.twitter.sdk.android.core.internal.oauth.OAuth2Service;
import com.twitter.sdk.android.core.internal.oauth.OAuth2Token;
import java.util.List;

/* JADX INFO: loaded from: classes18.dex */
class DigitsGuestSessionProvider extends SessionProvider {
    final SessionManager<DigitsSession> defaultSessionManager;
    final OAuth2Service oAuth2Service;

    DigitsGuestSessionProvider(SessionManager<DigitsSession> defaultSessionManager, List<SessionManager<? extends Session>> sessionManagers) {
        this(defaultSessionManager, sessionManagers, new OAuth2Service(TwitterCore.getInstance(), null, new DigitsApi()));
    }

    DigitsGuestSessionProvider(SessionManager<DigitsSession> defaultSessionManager, List<SessionManager<? extends Session>> sessionManagers, OAuth2Service oAuth2Service) {
        super(sessionManagers);
        this.defaultSessionManager = defaultSessionManager;
        this.oAuth2Service = oAuth2Service;
    }

    @Override // com.twitter.sdk.android.core.internal.SessionProvider
    public void requestAuth(Callback<Session> cb) {
        this.oAuth2Service.requestGuestAuthToken(new GuestAuthCallback(this.defaultSessionManager, cb));
    }

    static class GuestAuthCallback extends Callback<OAuth2Token> {
        final Callback<Session> callback;
        final SessionManager<DigitsSession> sessionManager;

        GuestAuthCallback(SessionManager<DigitsSession> sessionManager, Callback<Session> callback) {
            this.sessionManager = sessionManager;
            this.callback = callback;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<OAuth2Token> result) {
            DigitsSession session = new DigitsSession(result.data);
            this.sessionManager.setSession(session.getId(), session);
            if (this.callback != null) {
                this.callback.success(new Result<>(session, result.response));
            }
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void failure(TwitterException exception) {
            if (this.callback != null) {
                this.callback.failure(exception);
            }
        }
    }
}
