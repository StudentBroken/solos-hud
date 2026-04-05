package com.twitter.sdk.android.core.internal.scribe;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

/* JADX INFO: loaded from: classes62.dex */
public class ScribeItem {
    public static final int TYPE_MESSAGE = 6;
    public static final int TYPE_TWEET = 0;
    public static final int TYPE_USER = 3;

    @SerializedName("card_event")
    public final CardEvent cardEvent;

    @SerializedName("description")
    public final String description;

    @SerializedName("id")
    public final Long id;

    @SerializedName("item_type")
    public final Integer itemType;

    @SerializedName("media_details")
    public final MediaDetails mediaDetails;

    private ScribeItem(Integer itemType, Long id, String description, CardEvent cardEvent, MediaDetails mediaDetails) {
        this.itemType = itemType;
        this.id = id;
        this.description = description;
        this.cardEvent = cardEvent;
        this.mediaDetails = mediaDetails;
    }

    public static ScribeItem fromTweet(Tweet tweet) {
        return new Builder().setItemType(0).setId(tweet.id).build();
    }

    public static ScribeItem fromUser(User user) {
        return new Builder().setItemType(3).setId(user.id).build();
    }

    public static ScribeItem fromMessage(String message) {
        return new Builder().setItemType(6).setDescription(message).build();
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScribeItem that = (ScribeItem) o;
        if (this.itemType != null) {
            if (!this.itemType.equals(that.itemType)) {
                return false;
            }
        } else if (that.itemType != null) {
            return false;
        }
        if (this.id != null) {
            if (!this.id.equals(that.id)) {
                return false;
            }
        } else if (that.id != null) {
            return false;
        }
        if (this.description != null) {
            if (!this.description.equals(that.description)) {
                return false;
            }
        } else if (that.description != null) {
            return false;
        }
        if (this.cardEvent != null) {
            if (!this.cardEvent.equals(that.cardEvent)) {
                return false;
            }
        } else if (that.cardEvent != null) {
            return false;
        }
        if (this.mediaDetails == null ? that.mediaDetails != null : !this.mediaDetails.equals(that.mediaDetails)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result = this.itemType != null ? this.itemType.hashCode() : 0;
        return (((((((result * 31) + (this.id != null ? this.id.hashCode() : 0)) * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + (this.cardEvent != null ? this.cardEvent.hashCode() : 0)) * 31) + (this.mediaDetails != null ? this.mediaDetails.hashCode() : 0);
    }

    public static class CardEvent {

        @SerializedName("promotion_card_type")
        final int promotionCardType;

        public CardEvent(int cardType) {
            this.promotionCardType = cardType;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CardEvent cardEvent = (CardEvent) o;
            return this.promotionCardType == cardEvent.promotionCardType;
        }

        public int hashCode() {
            return this.promotionCardType;
        }
    }

    public static class MediaDetails {
        public static final int TYPE_AMPLIFY = 2;
        public static final int TYPE_ANIMATED_GIF = 3;
        public static final int TYPE_CONSUMER = 1;
        public static final int TYPE_VINE = 4;

        @SerializedName("content_id")
        public final long contentId;

        @SerializedName("media_type")
        public final int mediaType;

        @SerializedName("publisher_id")
        public final long publisherId;

        public MediaDetails(long contentId, int mediaType, long publisherId) {
            this.contentId = contentId;
            this.mediaType = mediaType;
            this.publisherId = publisherId;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MediaDetails that = (MediaDetails) o;
            if (this.contentId == that.contentId && this.mediaType == that.mediaType) {
                return this.publisherId == that.publisherId;
            }
            return false;
        }

        public int hashCode() {
            int result = (int) (this.contentId ^ (this.contentId >>> 32));
            return (((result * 31) + this.mediaType) * 31) + ((int) (this.publisherId ^ (this.publisherId >>> 32)));
        }
    }

    public static class Builder {
        private CardEvent cardEvent;
        private String description;
        private Long id;
        private Integer itemType;
        private MediaDetails mediaDetails;

        public Builder setItemType(int itemType) {
            this.itemType = Integer.valueOf(itemType);
            return this;
        }

        public Builder setId(long id) {
            this.id = Long.valueOf(id);
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setCardEvent(CardEvent cardEvent) {
            this.cardEvent = cardEvent;
            return this;
        }

        public Builder setMediaDetails(MediaDetails mediaDetails) {
            this.mediaDetails = mediaDetails;
            return this;
        }

        public ScribeItem build() {
            return new ScribeItem(this.itemType, this.id, this.description, this.cardEvent, this.mediaDetails);
        }
    }
}
