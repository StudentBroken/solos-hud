package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
abstract class BaseTimeline {
    protected final TweetUi tweetUi;

    abstract String getTimelineType();

    BaseTimeline(TweetUi tweetUi) {
        if (tweetUi == null) {
            throw new IllegalArgumentException("TweetUi instance must not be null");
        }
        this.tweetUi = tweetUi;
        scribeImpression();
    }

    private void scribeImpression() {
        this.tweetUi.scribe(ScribeConstants.getSyndicatedSdkTimelineNamespace(getTimelineType()), ScribeConstants.getTfwClientTimelineNamespace(getTimelineType()));
    }

    static Long decrementMaxId(Long maxId) {
        if (maxId == null) {
            return null;
        }
        return Long.valueOf(maxId.longValue() - 1);
    }

    void addRequest(Callback<TwitterApiClient> cb) {
        this.tweetUi.getGuestAuthQueue().addClientRequest(cb);
    }

    static class TweetsCallback extends Callback<List<Tweet>> {
        protected final Callback<TimelineResult<Tweet>> cb;

        TweetsCallback(Callback<TimelineResult<Tweet>> cb) {
            this.cb = cb;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<List<Tweet>> result) {
            List<Tweet> tweets = result.data;
            TimelineResult<Tweet> timelineResult = new TimelineResult<>(new TimelineCursor(tweets), tweets);
            if (this.cb != null) {
                this.cb.success(timelineResult, result.response);
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
