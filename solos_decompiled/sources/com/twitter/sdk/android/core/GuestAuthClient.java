package com.twitter.sdk.android.core;

import com.twitter.sdk.android.core.internal.oauth.OAuth2Service;
import com.twitter.sdk.android.core.internal.oauth.OAuth2Token;

/* JADX INFO: loaded from: classes62.dex */
class GuestAuthClient {
    private final OAuth2Service service;

    GuestAuthClient(OAuth2Service service) {
        if (service == null) {
            throw new IllegalArgumentException("OAuth2Service must not be null");
        }
        this.service = service;
    }

    void authorize(SessionManager<AppSession> appSessionManager, Callback<AppSession> callback) {
        if (appSessionManager == null) {
            throw new IllegalArgumentException("SessionManager must not be null");
        }
        this.service.requestGuestAuthToken(new CallbackWrapper(appSessionManager, callback));
    }

    class CallbackWrapper extends Callback<OAuth2Token> {
        private final SessionManager<AppSession> appSessionManager;
        private final Callback<AppSession> callback;

        CallbackWrapper(SessionManager<AppSession> appSessionManager, Callback<AppSession> callback) {
            this.appSessionManager = appSessionManager;
            this.callback = callback;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<OAuth2Token> result) {
            AppSession session = new AppSession(result.data);
            this.appSessionManager.setSession(session.getId(), session);
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
