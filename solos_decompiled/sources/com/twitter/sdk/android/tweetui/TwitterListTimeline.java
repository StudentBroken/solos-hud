package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.GuestCallback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTimeline;
import io.fabric.sdk.android.Fabric;

/* JADX INFO: loaded from: classes9.dex */
public class TwitterListTimeline extends BaseTimeline implements Timeline<Tweet> {
    private static final String SCRIBE_SECTION = "list";
    final Boolean includeRetweets;
    final Long listId;
    final Integer maxItemsPerRequest;
    final Long ownerId;
    final String ownerScreenName;
    final String slug;

    TwitterListTimeline(TweetUi tweetUi, Long listId, String slug, Long ownerId, String ownerScreenName, Integer maxItemsPerRequest, Boolean includeRetweets) {
        super(tweetUi);
        this.listId = listId;
        this.slug = slug;
        this.ownerId = ownerId;
        this.ownerScreenName = ownerScreenName;
        this.maxItemsPerRequest = maxItemsPerRequest;
        this.includeRetweets = includeRetweets;
    }

    @Override // com.twitter.sdk.android.tweetui.Timeline
    public void next(Long sinceId, Callback<TimelineResult<Tweet>> cb) {
        addRequest(createListTimelineRequest(sinceId, null, cb));
    }

    @Override // com.twitter.sdk.android.tweetui.Timeline
    public void previous(Long maxId, Callback<TimelineResult<Tweet>> cb) {
        addRequest(createListTimelineRequest(null, decrementMaxId(maxId), cb));
    }

    Callback<TwitterApiClient> createListTimelineRequest(final Long sinceId, final Long maxId, final Callback<TimelineResult<Tweet>> cb) {
        return new LoggingCallback<TwitterApiClient>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.TwitterListTimeline.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<TwitterApiClient> result) {
                result.data.getListService().statuses(TwitterListTimeline.this.listId, TwitterListTimeline.this.slug, TwitterListTimeline.this.ownerScreenName, TwitterListTimeline.this.ownerId, sinceId, maxId, TwitterListTimeline.this.maxItemsPerRequest, true, TwitterListTimeline.this.includeRetweets, new GuestCallback(new BaseTimeline.TweetsCallback(cb)));
            }
        };
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTimeline
    String getTimelineType() {
        return SCRIBE_SECTION;
    }

    public static class Builder {
        private Boolean includeRetweets;
        private Long listId;
        private Integer maxItemsPerRequest;
        private Long ownerId;
        private String ownerScreenName;
        private String slug;
        private final TweetUi tweetUi;

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

        public Builder id(Long id) {
            this.listId = id;
            return this;
        }

        public Builder slugWithOwnerId(String slug, Long ownerId) {
            this.slug = slug;
            this.ownerId = ownerId;
            return this;
        }

        public Builder slugWithOwnerScreenName(String slug, String ownerScreenName) {
            this.slug = slug;
            this.ownerScreenName = ownerScreenName;
            return this;
        }

        public Builder maxItemsPerRequest(Integer maxItemsPerRequest) {
            this.maxItemsPerRequest = maxItemsPerRequest;
            return this;
        }

        public Builder includeRetweets(Boolean includeRetweets) {
            this.includeRetweets = includeRetweets;
            return this;
        }

        public TwitterListTimeline build() {
            if (!((this.listId == null) ^ (this.slug == null))) {
                throw new IllegalStateException("must specify either a list id or slug/owner pair");
            }
            if (this.slug != null && this.ownerId == null && this.ownerScreenName == null) {
                throw new IllegalStateException("slug/owner pair must set owner via ownerId or ownerScreenName");
            }
            return new TwitterListTimeline(this.tweetUi, this.listId, this.slug, this.ownerId, this.ownerScreenName, this.maxItemsPerRequest, this.includeRetweets);
        }
    }
}
