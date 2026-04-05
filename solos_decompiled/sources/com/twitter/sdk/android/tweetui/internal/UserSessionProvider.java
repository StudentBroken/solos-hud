package com.twitter.sdk.android.tweetui.internal;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.internal.SessionProvider;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
public class UserSessionProvider extends SessionProvider {
    public UserSessionProvider(List<SessionManager<? extends Session>> sessionManagers) {
        super(sessionManagers);
    }

    @Override // com.twitter.sdk.android.core.internal.SessionProvider
    public void requestAuth(Callback<Session> cb) {
        cb.failure(new TwitterAuthException("Twitter login required."));
    }
}
