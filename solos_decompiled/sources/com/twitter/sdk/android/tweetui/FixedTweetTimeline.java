package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Tweet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
public class FixedTweetTimeline extends BaseTimeline implements Timeline<Tweet> {
    private static final String SCRIBE_SECTION = "fixed";
    List<Tweet> tweets;

    FixedTweetTimeline(TweetUi tweetUi, List<Tweet> tweets) {
        super(tweetUi);
        this.tweets = tweets == null ? new ArrayList<>() : tweets;
    }

    @Override // com.twitter.sdk.android.tweetui.Timeline
    public void next(Long minPosition, Callback<TimelineResult<Tweet>> cb) {
        TimelineResult<Tweet> timelineResult = new TimelineResult<>(new TimelineCursor(this.tweets), this.tweets);
        cb.success(timelineResult, null);
    }

    @Override // com.twitter.sdk.android.tweetui.Timeline
    public void previous(Long maxPosition, Callback<TimelineResult<Tweet>> cb) {
        List<Tweet> empty = Collections.emptyList();
        TimelineResult<Tweet> timelineResult = new TimelineResult<>(new TimelineCursor(empty), empty);
        cb.success(timelineResult, null);
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTimeline
    String getTimelineType() {
        return SCRIBE_SECTION;
    }

    public static class Builder {
        private final TweetUi tweetUi;
        private List<Tweet> tweets;

        public Builder() {
            this(TweetUi.getInstance());
        }

        public Builder(TweetUi tweetUi) {
            if (tweetUi == null) {
                throw new IllegalArgumentException("TweetUi instance must not be null");
            }
            this.tweetUi = tweetUi;
        }

        public Builder setTweets(List<Tweet> tweets) {
            this.tweets = tweets;
            return this;
        }

        public FixedTweetTimeline build() {
            return new FixedTweetTimeline(this.tweetUi, this.tweets);
        }
    }
}
