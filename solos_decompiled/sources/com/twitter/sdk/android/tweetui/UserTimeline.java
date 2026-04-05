package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.GuestCallback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTimeline;
import io.fabric.sdk.android.Fabric;

/* JADX INFO: loaded from: classes9.dex */
public class UserTimeline extends BaseTimeline implements Timeline<Tweet> {
    private static final String SCRIBE_SECTION = "user";
    final Boolean includeReplies;
    final Boolean includeRetweets;
    final Integer maxItemsPerRequest;
    final String screenName;
    final Long userId;

    UserTimeline(TweetUi tweetUi, Long userId, String screenName, Integer maxItemsPerRequest, Boolean includeReplies, Boolean includeRetweets) {
        super(tweetUi);
        this.userId = userId;
        this.screenName = screenName;
        this.maxItemsPerRequest = maxItemsPerRequest;
        this.includeReplies = Boolean.valueOf(includeReplies == null ? false : includeReplies.booleanValue());
        this.includeRetweets = includeRetweets;
    }

    @Override // com.twitter.sdk.android.tweetui.Timeline
    public void next(Long sinceId, Callback<TimelineResult<Tweet>> cb) {
        addRequest(createUserTimelineRequest(sinceId, null, cb));
    }

    @Override // com.twitter.sdk.android.tweetui.Timeline
    public void previous(Long maxId, Callback<TimelineResult<Tweet>> cb) {
        addRequest(createUserTimelineRequest(null, decrementMaxId(maxId), cb));
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTimeline
    String getTimelineType() {
        return "user";
    }

    Callback<TwitterApiClient> createUserTimelineRequest(final Long sinceId, final Long maxId, final Callback<TimelineResult<Tweet>> cb) {
        return new LoggingCallback<TwitterApiClient>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.UserTimeline.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<TwitterApiClient> result) {
                result.data.getStatusesService().userTimeline(UserTimeline.this.userId, UserTimeline.this.screenName, UserTimeline.this.maxItemsPerRequest, sinceId, maxId, false, Boolean.valueOf(UserTimeline.this.includeReplies.booleanValue() ? false : true), null, UserTimeline.this.includeRetweets, new GuestCallback(new BaseTimeline.TweetsCallback(cb)));
            }
        };
    }

    public static class Builder {
        private Boolean includeReplies;
        private Boolean includeRetweets;
        private Integer maxItemsPerRequest;
        private String screenName;
        private final TweetUi tweetUi;
        private Long userId;

        public Builder() {
            this(TweetUi.getInstance());
        }

        public Builder(TweetUi tweetUi) {
            this.maxItemsPerRequest = 30;
            if (tweetUi == null) {
                throw new IllegalArgumentException("TweetUi instance must not be null");
            }
            this.tweetUi = tweetUi;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder screenName(String screenName) {
            this.screenName = screenName;
            return this;
        }

        public Builder maxItemsPerRequest(Integer maxItemsPerRequest) {
            this.maxItemsPerRequest = maxItemsPerRequest;
            return this;
        }

        public Builder includeReplies(Boolean includeReplies) {
            this.includeReplies = includeReplies;
            return this;
        }

        public Builder includeRetweets(Boolean includeRetweets) {
            this.includeRetweets = includeRetweets;
            return this;
        }

        public UserTimeline build() {
            return new UserTimeline(this.tweetUi, this.userId, this.screenName, this.maxItemsPerRequest, this.includeReplies, this.includeRetweets);
        }
    }
}
