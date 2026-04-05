package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.GuestCallback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import io.fabric.sdk.android.Fabric;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
public class SearchTimeline extends BaseTimeline implements Timeline<Tweet> {
    static final String FILTER_RETWEETS = " -filter:retweets";
    static final String RESULT_TYPE = "filtered";
    private static final String SCRIBE_SECTION = "search";
    final String languageCode;
    final Integer maxItemsPerRequest;
    final String query;

    SearchTimeline(TweetUi tweetUi, String query, String languageCode, Integer maxItemsPerRequest) {
        super(tweetUi);
        this.languageCode = languageCode;
        this.maxItemsPerRequest = maxItemsPerRequest;
        this.query = query == null ? null : query + FILTER_RETWEETS;
    }

    @Override // com.twitter.sdk.android.tweetui.Timeline
    public void next(Long sinceId, Callback<TimelineResult<Tweet>> cb) {
        addRequest(createSearchRequest(sinceId, null, cb));
    }

    @Override // com.twitter.sdk.android.tweetui.Timeline
    public void previous(Long maxId, Callback<TimelineResult<Tweet>> cb) {
        addRequest(createSearchRequest(null, decrementMaxId(maxId), cb));
    }

    @Override // com.twitter.sdk.android.tweetui.BaseTimeline
    String getTimelineType() {
        return "search";
    }

    Callback<TwitterApiClient> createSearchRequest(final Long sinceId, final Long maxId, final Callback<TimelineResult<Tweet>> cb) {
        return new LoggingCallback<TwitterApiClient>(cb, Fabric.getLogger()) { // from class: com.twitter.sdk.android.tweetui.SearchTimeline.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<TwitterApiClient> result) {
                result.data.getSearchService().tweets(SearchTimeline.this.query, null, SearchTimeline.this.languageCode, null, SearchTimeline.RESULT_TYPE, SearchTimeline.this.maxItemsPerRequest, null, sinceId, maxId, true, new GuestCallback(SearchTimeline.this.new SearchCallback(cb)));
            }
        };
    }

    class SearchCallback extends Callback<Search> {
        protected final Callback<TimelineResult<Tweet>> cb;

        SearchCallback(Callback<TimelineResult<Tweet>> cb) {
            this.cb = cb;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<Search> result) {
            List<Tweet> tweets = result.data.tweets;
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

    public static class Builder {
        private String lang;
        private Integer maxItemsPerRequest;
        private String query;
        private TweetUi tweetUi;

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

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder languageCode(String languageCode) {
            this.lang = languageCode;
            return this;
        }

        public Builder maxItemsPerRequest(Integer maxItemsPerRequest) {
            this.maxItemsPerRequest = maxItemsPerRequest;
            return this;
        }

        public SearchTimeline build() {
            if (this.query == null) {
                throw new IllegalStateException("query must not be null");
            }
            return new SearchTimeline(this.tweetUi, this.query, this.lang, this.maxItemsPerRequest);
        }
    }
}
