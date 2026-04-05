package com.digits.sdk.android;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.AuthRequestQueue;
import com.twitter.sdk.android.core.internal.SessionProvider;

/* JADX INFO: loaded from: classes18.dex */
class DigitsAuthRequestQueue extends AuthRequestQueue {
    final DigitsClient digitsClient;

    DigitsAuthRequestQueue(DigitsClient digitsClient, SessionProvider sessionProvider) {
        super(sessionProvider);
        this.digitsClient = digitsClient;
    }

    protected synchronized boolean addClientRequest(final Callback<DigitsApiClient> callback) {
        return addRequest(new Callback<Session>() { // from class: com.digits.sdk.android.DigitsAuthRequestQueue.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<Session> result) {
                callback.success(new Result(DigitsAuthRequestQueue.this.digitsClient.getApiClient(result.data), null));
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException exception) {
                callback.failure(exception);
            }
        });
    }
}
