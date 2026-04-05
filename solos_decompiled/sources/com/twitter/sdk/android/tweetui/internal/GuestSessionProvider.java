package com.twitter.sdk.android.tweetui.internal;

import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.AuthToken;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.SessionProvider;
import com.twitter.sdk.android.core.internal.oauth.GuestAuthToken;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
public class GuestSessionProvider extends SessionProvider {
    private final TwitterCore twitterCore;

    public GuestSessionProvider(TwitterCore twitterCore, List<SessionManager<? extends Session>> sessionManagers) {
        super(sessionManagers);
        this.twitterCore = twitterCore;
    }

    @Override // com.twitter.sdk.android.core.internal.SessionProvider
    public Session getActiveSession() {
        Session session = super.getActiveSession();
        if (session == null) {
            return null;
        }
        AuthToken token = session.getAuthToken();
        if ((token instanceof TwitterAuthToken) || (token instanceof GuestAuthToken)) {
            return session;
        }
        return null;
    }

    @Override // com.twitter.sdk.android.core.internal.SessionProvider
    public void requestAuth(Callback<Session> cb) {
        this.twitterCore.logInGuest(new AppSessionCallback(cb));
    }

    class AppSessionCallback extends Callback<AppSession> {
        private final Callback<Session> cb;

        AppSessionCallback(Callback<Session> cb) {
            this.cb = cb;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<AppSession> result) {
            this.cb.success(new Result<>(result.data, result.response));
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void failure(TwitterException exception) {
            this.cb.failure(exception);
        }
    }
}
