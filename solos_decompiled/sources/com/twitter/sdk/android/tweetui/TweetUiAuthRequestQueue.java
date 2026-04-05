package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.AuthRequestQueue;
import com.twitter.sdk.android.core.internal.SessionProvider;

/* JADX INFO: loaded from: classes9.dex */
class TweetUiAuthRequestQueue extends AuthRequestQueue {
    private final TwitterCore twitterCore;

    TweetUiAuthRequestQueue(TwitterCore twitterCore, SessionProvider sessionProvider) {
        super(sessionProvider);
        this.twitterCore = twitterCore;
    }

    protected synchronized boolean addClientRequest(final Callback<TwitterApiClient> callback) {
        return addRequest(new Callback<Session>() { // from class: com.twitter.sdk.android.tweetui.TweetUiAuthRequestQueue.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<Session> result) {
                callback.success(new Result(TweetUiAuthRequestQueue.this.twitterCore.getApiClient(result.data), null));
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException exception) {
                callback.failure(exception);
            }
        });
    }
}
