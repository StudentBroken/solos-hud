package com.twitter.sdk.android.tweetui;

import android.content.Context;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTweetView;
import com.twitter.sdk.android.tweetui.TweetUtils;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
@Deprecated
public class TweetViewFetchAdapter<T extends BaseTweetView> extends TweetViewAdapter<T> {
    public TweetViewFetchAdapter(Context context) {
        super(context);
    }

    public TweetViewFetchAdapter(Context context, List<Long> tweetIds) {
        this(context, tweetIds, null);
    }

    public TweetViewFetchAdapter(Context context, List<Long> tweetIds, LoadCallback<List<Tweet>> cb) {
        super(context);
        setTweetIds(tweetIds, cb);
    }

    public void setTweetIds(List<Long> tweetIds) {
        setTweetIds(tweetIds, (Callback<List<Tweet>>) null);
    }

    public void setTweetIds(List<Long> tweetIds, final Callback<List<Tweet>> cb) {
        Callback<List<Tweet>> repoCallback = new Callback<List<Tweet>>() { // from class: com.twitter.sdk.android.tweetui.TweetViewFetchAdapter.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<List<Tweet>> result) {
                TweetViewFetchAdapter.this.setTweets(result.data);
                if (cb != null) {
                    cb.success(result);
                }
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException exception) {
                if (cb != null) {
                    cb.failure(exception);
                }
            }
        };
        TweetUi.getInstance().getTweetRepository().loadTweets(tweetIds, repoCallback);
    }

    @Deprecated
    public void setTweetIds(List<Long> tweetIds, LoadCallback<List<Tweet>> loadCallback) {
        Callback<List<Tweet>> cb = new TweetUtils.CallbackAdapter<>(loadCallback);
        setTweetIds(tweetIds, cb);
    }
}
