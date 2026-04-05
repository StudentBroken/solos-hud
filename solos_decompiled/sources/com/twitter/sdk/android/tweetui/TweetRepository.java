package com.twitter.sdk.android.tweetui;

import android.os.Handler;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.GuestCallback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import io.fabric.sdk.android.Fabric;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
class TweetRepository {
    private static final String AUTH_ERROR = "Auth could not be obtained.";
    private static final int DEFAULT_CACHE_SIZE = 20;
    private static final String TAG = "TweetUi";
    private final TweetUiAuthRequestQueue guestAuthQueue;
    private final Handler mainHandler;
    private final TweetUiAuthRequestQueue userAuthQueue;
    final LruCache<Long, Tweet> tweetCache = new LruCache<>(20);
    final LruCache<Long, FormattedTweetText> formatCache = new LruCache<>(20);

    TweetRepository(Handler mainHandler, TweetUiAuthRequestQueue userAuthQueue, TweetUiAuthRequestQueue guestAuthQueue) {
        this.mainHandler = mainHandler;
        this.userAuthQueue = userAuthQueue;
        this.guestAuthQueue = guestAuthQueue;
    }

    FormattedTweetText formatTweetText(Tweet tweet) {
        if (tweet == null) {
            return null;
        }
        FormattedTweetText cached = this.formatCache.get(Long.valueOf(tweet.id));
        if (cached == null) {
            FormattedTweetText formattedTweetText = TweetTextUtils.formatTweetText(tweet);
            if (formattedTweetText != null && !TextUtils.isEmpty(formattedTweetText.text)) {
                this.formatCache.put(Long.valueOf(tweet.id), formattedTweetText);
            }
            return formattedTweetText;
        }
        return cached;
    }

    protected void updateCache(Tweet tweet) {
        this.tweetCache.put(Long.valueOf(tweet.id), tweet);
    }

    private void deliverTweet(final Tweet tweet, final Callback<Tweet> cb) {
        if (cb != null) {
            this.mainHandler.post(new Runnable() { // from class: com.twitter.sdk.android.tweetui.TweetRepository.1
                @Override // java.lang.Runnable
                public void run() {
                    cb.success(new Result(tweet, null));
                }
            });
        }
    }

    void favorite(final long tweetId, final Callback<Tweet> cb) {
        this.userAuthQueue.addClientRequest(new LoggingCallback<TwitterApiClient>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.TweetRepository.2
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<TwitterApiClient> result) {
                result.data.getFavoriteService().create(Long.valueOf(tweetId), true, cb);
            }
        });
    }

    void unfavorite(final long tweetId, final Callback<Tweet> cb) {
        this.userAuthQueue.addClientRequest(new LoggingCallback<TwitterApiClient>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.TweetRepository.3
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<TwitterApiClient> result) {
                result.data.getFavoriteService().destroy(Long.valueOf(tweetId), true, cb);
            }
        });
    }

    void retweet(final long tweetId, final Callback<Tweet> cb) {
        this.userAuthQueue.addClientRequest(new LoggingCallback<TwitterApiClient>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.TweetRepository.4
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<TwitterApiClient> result) {
                result.data.getStatusesService().retweet(Long.valueOf(tweetId), false, cb);
            }
        });
    }

    void unretweet(final long tweetId, final Callback<Tweet> cb) {
        this.userAuthQueue.addClientRequest(new LoggingCallback<TwitterApiClient>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.TweetRepository.5
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<TwitterApiClient> result) {
                result.data.getStatusesService().unretweet(Long.valueOf(tweetId), false, cb);
            }
        });
    }

    void loadTweet(final long tweetId, final Callback<Tweet> cb) {
        Tweet cachedTweet = this.tweetCache.get(Long.valueOf(tweetId));
        if (cachedTweet != null) {
            deliverTweet(cachedTweet, cb);
        } else {
            this.guestAuthQueue.addClientRequest(new Callback<TwitterApiClient>() { // from class: com.twitter.sdk.android.tweetui.TweetRepository.6
                @Override // com.twitter.sdk.android.core.Callback
                public void success(Result<TwitterApiClient> result) {
                    result.data.getStatusesService().show(Long.valueOf(tweetId), null, null, null, TweetRepository.this.new SingleTweetCallback(cb));
                }

                @Override // com.twitter.sdk.android.core.Callback
                public void failure(TwitterException exception) {
                    Fabric.getLogger().e(TweetRepository.TAG, TweetRepository.AUTH_ERROR, exception);
                    if (cb != null) {
                        cb.failure(exception);
                    }
                }
            });
        }
    }

    void loadTweets(final List<Long> tweetIds, final Callback<List<Tweet>> cb) {
        this.guestAuthQueue.addClientRequest(new Callback<TwitterApiClient>() { // from class: com.twitter.sdk.android.tweetui.TweetRepository.7
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<TwitterApiClient> result) {
                String commaSepIds = TextUtils.join(",", tweetIds);
                result.data.getStatusesService().lookup(commaSepIds, null, null, null, TweetRepository.this.new MultiTweetsCallback(tweetIds, cb));
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException exception) {
                Fabric.getLogger().e(TweetRepository.TAG, TweetRepository.AUTH_ERROR, exception);
                if (cb != null) {
                    cb.failure(exception);
                }
            }
        });
    }

    class SingleTweetCallback extends GuestCallback<Tweet> {
        SingleTweetCallback(Callback<Tweet> cb) {
            super(cb);
        }

        /* JADX WARN: Type inference incomplete: some casts might be missing */
        @Override // com.twitter.sdk.android.core.GuestCallback, com.twitter.sdk.android.core.Callback
        public void success(Result<Tweet> result) {
            Tweet tweet = result.data;
            TweetRepository.this.updateCache(tweet);
            if (this.cb != 0) {
                this.cb.success((Result<T>) new Result(tweet, result.response));
            }
        }
    }

    class MultiTweetsCallback extends GuestCallback<List<Tweet>> {
        final List<Long> tweetIds;

        MultiTweetsCallback(List<Long> tweetIds, Callback<List<Tweet>> cb) {
            super(cb);
            this.tweetIds = tweetIds;
        }

        /* JADX WARN: Type inference incomplete: some casts might be missing */
        @Override // com.twitter.sdk.android.core.GuestCallback, com.twitter.sdk.android.core.Callback
        public void success(Result<List<Tweet>> result) {
            if (this.cb != 0) {
                this.cb.success((Result<T>) new Result(Utils.orderTweets(this.tweetIds, result.data), result.response));
            }
        }
    }
}
