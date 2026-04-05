package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterException;
import io.fabric.sdk.android.Logger;

/* JADX INFO: loaded from: classes9.dex */
abstract class LoggingCallback<T> extends Callback<T> {
    private final Callback cb;
    private final Logger logger;

    LoggingCallback(Callback cb, Logger logger) {
        this.cb = cb;
        this.logger = logger;
    }

    @Override // com.twitter.sdk.android.core.Callback
    public void failure(TwitterException exception) {
        this.logger.e("TweetUi", exception.getMessage(), exception);
        if (this.cb != null) {
            this.cb.failure(exception);
        }
    }
}
