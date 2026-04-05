package com.twitter.sdk.android.core.internal;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterCollection {

    @SerializedName("objects")
    public final Content contents;

    @SerializedName("response")
    public final Metadata metadata;

    public TwitterCollection(Content contents, Metadata metadata) {
        this.contents = contents;
        this.metadata = metadata;
    }

    public static final class Content {

        @SerializedName("tweets")
        public final Map<Long, Tweet> tweetMap;

        @SerializedName("users")
        public final Map<Long, User> userMap;

        public Content(Map<Long, Tweet> tweetMap, Map<Long, User> userMap) {
            this.tweetMap = tweetMap;
            this.userMap = userMap;
        }
    }

    public static final class Metadata {

        @SerializedName("position")
        public final Position position;

        @SerializedName("timeline_id")
        public final String timelineId;

        @SerializedName("timeline")
        public final List<TimelineItem> timelineItems;

        public Metadata(String timelineId, Position position, List<TimelineItem> timelines) {
            this.timelineId = timelineId;
            this.position = position;
            this.timelineItems = timelines;
        }

        public static final class Position {

            @SerializedName("max_position")
            public final Long maxPosition;

            @SerializedName("min_position")
            public final Long minPosition;

            public Position(Long maxPosition, Long minPosition) {
                this.maxPosition = maxPosition;
                this.minPosition = minPosition;
            }
        }
    }

    public static class TimelineItem {

        @SerializedName("tweet")
        public final TweetItem tweetItem;

        public TimelineItem(TweetItem tweetItem) {
            this.tweetItem = tweetItem;
        }

        public static final class TweetItem {

            @SerializedName("id")
            public final Long id;

            public TweetItem(Long id) {
                this.id = id;
            }
        }
    }
}
