package com.twitter.sdk.android.core.models;

import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public class TweetBuilder {
    private Coordinates coordinates;
    private String createdAt;
    private Object currentUserRetweet;
    private TweetEntities entities;
    private TweetEntities extendedEtities;
    private Integer favoriteCount;
    private boolean favorited;
    private String filterLevel;
    private long id = -1;
    private String idStr;
    private String inReplyToScreenName;
    private long inReplyToStatusId;
    private String inReplyToStatusIdStr;
    private long inReplyToUserId;
    private String inReplyToUserIdStr;
    private String lang;
    private Place place;
    private boolean possiblySensitive;
    private int retweetCount;
    private boolean retweeted;
    private Tweet retweetedStatus;
    private Object scopes;
    private String source;
    private String text;
    private boolean truncated;
    private User user;
    private boolean withheldCopyright;
    private List<String> withheldInCountries;
    private String withheldScope;

    public TweetBuilder setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public TweetBuilder setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public TweetBuilder setCurrentUserRetweet(Object currentUserRetweet) {
        this.currentUserRetweet = currentUserRetweet;
        return this;
    }

    public TweetBuilder setEntities(TweetEntities entities) {
        this.entities = entities;
        return this;
    }

    public TweetBuilder setExtendedEntities(TweetEntities extendedEtities) {
        this.extendedEtities = extendedEtities;
        return this;
    }

    public TweetBuilder setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
        return this;
    }

    public TweetBuilder setFavorited(boolean favorited) {
        this.favorited = favorited;
        return this;
    }

    public TweetBuilder setFilterLevel(String filterLevel) {
        this.filterLevel = filterLevel;
        return this;
    }

    public TweetBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public TweetBuilder setIdStr(String idStr) {
        this.idStr = idStr;
        return this;
    }

    public TweetBuilder setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
        return this;
    }

    public TweetBuilder setInReplyToStatusId(long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
        return this;
    }

    public TweetBuilder setInReplyToStatusIdStr(String inReplyToStatusIdStr) {
        this.inReplyToStatusIdStr = inReplyToStatusIdStr;
        return this;
    }

    public TweetBuilder setInReplyToUserId(long inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
        return this;
    }

    public TweetBuilder setInReplyToUserIdStr(String inReplyToUserIdStr) {
        this.inReplyToUserIdStr = inReplyToUserIdStr;
        return this;
    }

    public TweetBuilder setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public TweetBuilder setPlace(Place place) {
        this.place = place;
        return this;
    }

    public TweetBuilder setPossiblySensitive(boolean possiblySensitive) {
        this.possiblySensitive = possiblySensitive;
        return this;
    }

    public TweetBuilder setScopes(Object scopes) {
        this.scopes = scopes;
        return this;
    }

    public TweetBuilder setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
        return this;
    }

    public TweetBuilder setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
        return this;
    }

    public TweetBuilder setRetweetedStatus(Tweet retweetedStatus) {
        this.retweetedStatus = retweetedStatus;
        return this;
    }

    public TweetBuilder setSource(String source) {
        this.source = source;
        return this;
    }

    public TweetBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public TweetBuilder setTruncated(boolean truncated) {
        this.truncated = truncated;
        return this;
    }

    public TweetBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public TweetBuilder setWithheldCopyright(boolean withheldCopyright) {
        this.withheldCopyright = withheldCopyright;
        return this;
    }

    public TweetBuilder setWithheldInCountries(List<String> withheldInCountries) {
        this.withheldInCountries = withheldInCountries;
        return this;
    }

    public TweetBuilder setWithheldScope(String withheldScope) {
        this.withheldScope = withheldScope;
        return this;
    }

    public TweetBuilder copy(Tweet tweet) {
        this.coordinates = tweet.coordinates;
        this.createdAt = tweet.createdAt;
        this.currentUserRetweet = tweet.currentUserRetweet;
        this.entities = tweet.entities;
        this.extendedEtities = tweet.extendedEtities;
        this.favoriteCount = tweet.favoriteCount;
        this.favorited = tweet.favorited;
        this.filterLevel = tweet.filterLevel;
        this.id = tweet.id;
        this.idStr = tweet.idStr;
        this.inReplyToScreenName = tweet.inReplyToScreenName;
        this.inReplyToStatusId = tweet.inReplyToStatusId;
        this.inReplyToStatusIdStr = tweet.inReplyToStatusIdStr;
        this.inReplyToUserId = tweet.inReplyToUserId;
        this.inReplyToUserIdStr = tweet.inReplyToStatusIdStr;
        this.lang = tweet.lang;
        this.place = tweet.place;
        this.possiblySensitive = tweet.possiblySensitive;
        this.scopes = tweet.scopes;
        this.retweetCount = tweet.retweetCount;
        this.retweeted = tweet.retweeted;
        this.retweetedStatus = tweet.retweetedStatus;
        this.source = tweet.source;
        this.text = tweet.text;
        this.truncated = tweet.truncated;
        this.user = tweet.user;
        this.withheldCopyright = tweet.withheldCopyright;
        this.withheldInCountries = tweet.withheldInCountries;
        this.withheldScope = tweet.withheldScope;
        return this;
    }

    public Tweet build() {
        return new Tweet(this.coordinates, this.createdAt, this.currentUserRetweet, this.entities, this.extendedEtities, this.favoriteCount, this.favorited, this.filterLevel, this.id, this.idStr, this.inReplyToScreenName, this.inReplyToStatusId, this.inReplyToStatusIdStr, this.inReplyToUserId, this.inReplyToUserIdStr, this.lang, this.place, this.possiblySensitive, this.scopes, this.retweetCount, this.retweeted, this.retweetedStatus, this.source, this.text, this.truncated, this.user, this.withheldCopyright, this.withheldInCountries, this.withheldScope);
    }
}
