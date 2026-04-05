package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.GuestCallback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.TwitterCollection;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetBuilder;
import com.twitter.sdk.android.core.models.User;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes9.dex */
public class CollectionTimeline extends BaseTimeline implements Timeline<Tweet> {
    static final String COLLECTION_PREFIX = "custom-";
    private static final String SCRIBE_SECTION = "collection";
    final String collectionIdentifier;
    final Integer maxItemsPerRequest;

    CollectionTimeline(TweetUi tweetUi, Long collectionId, Integer maxItemsPerRequest) {
        super(tweetUi);
        if (collectionId == null) {
            this.collectionIdentifier = null;
        } else {
            this.collectionIdentifier = COLLECTION_PREFIX + Long.toString(collectionId.longValue());
        }
        this.maxItemsPerRequest = maxItemsPerRequest;
    }

    @Override // com.twitter.sdk.android.tweetui.Timeline
    public void next(Long minPosition, Callback<TimelineResult<Tweet>> cb) {
        addRequest(createCollectionRequest(minPosition, null, cb));
    }

    @Override // com.twitter.sdk.android.tweetui.Timeline
    public void previous(Long maxPosition, Callback<TimelineResult<Tweet>> cb) {
        addRequest(createCollectionRequest(null, maxPosition, cb));
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTimeline
    String getTimelineType() {
        return SCRIBE_SECTION;
    }

    Callback<TwitterApiClient> createCollectionRequest(final Long minPosition, final Long maxPosition, final Callback<TimelineResult<Tweet>> cb) {
        return new LoggingCallback<TwitterApiClient>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.CollectionTimeline.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<TwitterApiClient> result) {
                result.data.getCollectionService().collection(CollectionTimeline.this.collectionIdentifier, CollectionTimeline.this.maxItemsPerRequest, maxPosition, minPosition, new GuestCallback(CollectionTimeline.this.new CollectionCallback(cb)));
            }
        };
    }

    class CollectionCallback extends Callback<TwitterCollection> {
        private final Callback<TimelineResult<Tweet>> cb;

        CollectionCallback(Callback<TimelineResult<Tweet>> cb) {
            this.cb = cb;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<TwitterCollection> result) {
            TimelineResult<Tweet> timelineResult;
            TimelineCursor timelineCursor = CollectionTimeline.getTimelineCursor(result.data);
            List<Tweet> tweets = CollectionTimeline.getOrderedTweets(result.data);
            if (timelineCursor != null) {
                timelineResult = new TimelineResult<>(timelineCursor, tweets);
            } else {
                timelineResult = new TimelineResult<>(null, Collections.emptyList());
            }
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

    static List<Tweet> getOrderedTweets(TwitterCollection collection) {
        if (collection == null || collection.contents == null || collection.contents.tweetMap == null || collection.contents.userMap == null || collection.metadata == null || collection.metadata.timelineItems == null || collection.metadata.position == null) {
            return Collections.emptyList();
        }
        List<Tweet> tweets = new ArrayList<>();
        Map<Long, Tweet> tweetMap = new HashMap<>();
        for (Tweet trimmedTweet : collection.contents.tweetMap.values()) {
            Long userId = Long.valueOf(trimmedTweet.user.id);
            User user = collection.contents.userMap.get(userId);
            Tweet tweet = new TweetBuilder().copy(trimmedTweet).setUser(user).build();
            tweetMap.put(Long.valueOf(tweet.id), tweet);
        }
        for (TwitterCollection.TimelineItem item : collection.metadata.timelineItems) {
            tweets.add(tweetMap.get(item.tweetItem.id));
        }
        return tweets;
    }

    static TimelineCursor getTimelineCursor(TwitterCollection twitterCollection) {
        if (twitterCollection == null || twitterCollection.metadata == null || twitterCollection.metadata.position == null) {
            return null;
        }
        Long minPosition = twitterCollection.metadata.position.minPosition;
        Long maxPosition = twitterCollection.metadata.position.maxPosition;
        return new TimelineCursor(minPosition, maxPosition);
    }

    public static class Builder {
        private Long collectionId;
        private Integer maxItemsPerRequest;
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

        public Builder id(Long collectionId) {
            this.collectionId = collectionId;
            return this;
        }

        public Builder maxItemsPerRequest(Integer maxItemsPerRequest) {
            this.maxItemsPerRequest = maxItemsPerRequest;
            return this;
        }

        public CollectionTimeline build() {
            if (this.collectionId == null) {
                throw new IllegalStateException("collection id must not be null");
            }
            return new CollectionTimeline(this.tweetUi, this.collectionId, this.maxItemsPerRequest);
        }
    }
}
