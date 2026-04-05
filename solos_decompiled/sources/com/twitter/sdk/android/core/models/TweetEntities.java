package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public class TweetEntities {

    @SerializedName("hashtags")
    public final List<HashtagEntity> hashtags;

    @SerializedName("media")
    public final List<MediaEntity> media;

    @SerializedName("urls")
    public final List<UrlEntity> urls;

    @SerializedName("user_mentions")
    public final List<MentionEntity> userMentions;

    public TweetEntities(List<UrlEntity> urls, List<MentionEntity> userMentions, List<MediaEntity> media, List<HashtagEntity> hashtags) {
        this.urls = getSafeList(urls);
        this.userMentions = getSafeList(userMentions);
        this.media = getSafeList(media);
        this.hashtags = getSafeList(hashtags);
    }

    private <T> List<T> getSafeList(List<T> entities) {
        return entities == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(entities);
    }
}
