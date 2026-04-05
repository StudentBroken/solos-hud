package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.internal.scribe.EventNamespace;
import com.twitter.sdk.android.core.internal.scribe.ScribeItem;
import com.twitter.sdk.android.core.internal.scribe.SyndicationClientEvent;
import com.twitter.sdk.android.core.models.MediaEntity;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
class VideoScribeClientImpl implements VideoScribeClient {
    static final String SCRIBE_IMPRESSION_ACTION = "impression";
    static final String SCRIBE_PLAY_ACTION = "play";
    static final String TFW_CLIENT_EVENT_PAGE = "android";
    static final String TFW_CLIENT_EVENT_SECTION = "video";
    final TweetUi tweetUi;

    VideoScribeClientImpl(TweetUi tweetUi) {
        this.tweetUi = tweetUi;
    }

    @Override // com.twitter.sdk.android.tweetui.VideoScribeClient
    public void impression(long tweetId, MediaEntity mediaEntity) {
        List<ScribeItem> items = new ArrayList<>();
        items.add(createScribeItem(tweetId, mediaEntity));
        this.tweetUi.scribe(getTfwImpressionNamespace(), items);
    }

    @Override // com.twitter.sdk.android.tweetui.VideoScribeClient
    public void play(long tweetId, MediaEntity mediaEntity) {
        List<ScribeItem> items = new ArrayList<>();
        items.add(createScribeItem(tweetId, mediaEntity));
        this.tweetUi.scribe(getTfwPlayNamespace(), items);
    }

    static ScribeItem createScribeItem(long tweetId, MediaEntity mediaEntity) {
        return new ScribeItem.Builder().setItemType(0).setId(tweetId).setMediaDetails(createMediaDetails(tweetId, mediaEntity)).build();
    }

    static ScribeItem.MediaDetails createMediaDetails(long tweetId, MediaEntity mediaEntity) {
        return new ScribeItem.MediaDetails(tweetId, getMediaType(mediaEntity), mediaEntity.id);
    }

    static int getMediaType(MediaEntity mediaEntity) {
        return TweetMediaUtils.GIF_TYPE.equals(mediaEntity.type) ? 3 : 1;
    }

    static EventNamespace getTfwImpressionNamespace() {
        return new EventNamespace.Builder().setClient(SyndicationClientEvent.CLIENT_NAME).setPage("android").setSection("video").setAction(SCRIBE_IMPRESSION_ACTION).builder();
    }

    static EventNamespace getTfwPlayNamespace() {
        return new EventNamespace.Builder().setClient(SyndicationClientEvent.CLIENT_NAME).setPage("android").setSection("video").setAction(SCRIBE_PLAY_ACTION).builder();
    }
}
