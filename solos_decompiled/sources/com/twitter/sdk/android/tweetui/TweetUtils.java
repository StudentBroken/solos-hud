package com.twitter.sdk.android.tweetui;

import android.net.Uri;
import android.text.TextUtils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import io.fabric.sdk.android.Fabric;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes9.dex */
public final class TweetUtils {
    static final String LOAD_TWEET_DEBUG = "loadTweet failure for Tweet Id %d.";
    private static final String PERMALINK_FORMAT = "https://twitter.com/%s/status/%d";
    private static final String TAG = "TweetUi";
    private static final String UNKNOWN_SCREEN_NAME = "twitter_unknown";

    private TweetUtils() {
    }

    public static void loadTweet(long tweetId, final Callback<Tweet> cb) {
        TweetUi.getInstance().getTweetRepository().loadTweet(tweetId, new LoggingCallback<Tweet>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.TweetUtils.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<Tweet> result) {
                if (cb != null) {
                    cb.success(result);
                }
            }
        });
    }

    public static void loadTweets(List<Long> tweetIds, final Callback<List<Tweet>> cb) {
        TweetUi.getInstance().getTweetRepository().loadTweets(tweetIds, new LoggingCallback<List<Tweet>>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.TweetUtils.2
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<List<Tweet>> result) {
                if (cb != null) {
                    cb.success(result);
                }
            }
        });
    }

    @Deprecated
    public static void loadTweet(long tweetId, LoadCallback<Tweet> loadCallback) {
        final Callback<Tweet> cb = new CallbackAdapter<>(loadCallback);
        loadTweet(tweetId, new LoggingCallback<Tweet>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.TweetUtils.3
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<Tweet> result) {
                if (cb != null) {
                    cb.success(result);
                }
            }
        });
    }

    @Deprecated
    public static void loadTweets(List<Long> tweetIds, LoadCallback<List<Tweet>> loadCallback) {
        final Callback<List<Tweet>> cb = new CallbackAdapter<>(loadCallback);
        loadTweets(tweetIds, new LoggingCallback<List<Tweet>>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.TweetUtils.4
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<List<Tweet>> result) {
                if (cb != null) {
                    cb.success(result);
                }
            }
        });
    }

    static boolean isTweetResolvable(Tweet tweet) {
        return (tweet == null || tweet.id <= 0 || tweet.user == null || TextUtils.isEmpty(tweet.user.screenName)) ? false : true;
    }

    static Tweet getDisplayTweet(Tweet tweet) {
        return (tweet == null || tweet.retweetedStatus == null) ? tweet : tweet.retweetedStatus;
    }

    static Uri getPermalink(String screenName, long tweetId) {
        String permalink;
        if (tweetId <= 0) {
            return null;
        }
        if (TextUtils.isEmpty(screenName)) {
            permalink = String.format(Locale.US, PERMALINK_FORMAT, UNKNOWN_SCREEN_NAME, Long.valueOf(tweetId));
        } else {
            permalink = String.format(Locale.US, PERMALINK_FORMAT, screenName, Long.valueOf(tweetId));
        }
        return Uri.parse(permalink);
    }

    public static class CallbackAdapter<T> extends Callback<T> {
        private LoadCallback<T> cb;

        CallbackAdapter(LoadCallback<T> cb) {
            this.cb = cb;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<T> result) {
            if (this.cb != null) {
                this.cb.success(result.data);
            }
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void failure(TwitterException exception) {
            if (this.cb != null) {
                this.cb.failure(exception);
            }
        }
    }
}
