package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

/* JADX INFO: loaded from: classes9.dex */
class ResetTweetCallback extends Callback<Tweet> {
    BaseTweetView baseTweetView;
    Callback<Tweet> cb;
    TweetRepository tweetRepository;

    ResetTweetCallback(BaseTweetView baseTweetView, TweetRepository tweetRepository, Callback<Tweet> cb) {
        this.baseTweetView = baseTweetView;
        this.tweetRepository = tweetRepository;
        this.cb = cb;
    }

    @Override // com.twitter.sdk.android.core.Callback
    public void success(Result<Tweet> result) {
        this.tweetRepository.updateCache(result.data);
        this.baseTweetView.setTweet(result.data);
        if (this.cb != null) {
            this.cb.success(result);
        }
    }

    @Override // com.twitter.sdk.android.core.Callback
    public void failure(TwitterException exception) {
        if (this.cb != null) {
            this.cb.failure(exception);
        }
    }
}
