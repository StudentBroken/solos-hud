package com.twitter.sdk.android.tweetcomposer;

import com.twitter.sdk.android.core.internal.scribe.EventNamespace;
import com.twitter.sdk.android.core.internal.scribe.ScribeItem;

/* JADX INFO: loaded from: classes29.dex */
class ScribeConstants {
    static final String SCRIBE_CANCEL_ELEMENT = "cancel";
    static final String SCRIBE_CLICK_ACTION = "click";
    static final String SCRIBE_COMPONENT = "";
    static final String SCRIBE_IMPRESSION_ACTION = "impression";
    static final String SCRIBE_IMPRESSION_ELEMENT = "";
    static final String SCRIBE_PAGE = "android";
    static final int SCRIBE_PROMO_APP_CARD_TYPE = 8;
    static final String SCRIBE_TFW_CLIENT = "tfw";
    static final String SCRIBE_TWEET_ELEMENT = "tweet";
    static final String SCRIBE_SECTION = "composer";
    static final EventNamespace.Builder ComposerEventBuilder = new EventNamespace.Builder().setClient("tfw").setPage("android").setSection(SCRIBE_SECTION);

    ScribeConstants() {
    }

    static ScribeItem newCardScribeItem(Card card) {
        return new ScribeItem.Builder().setItemType(0).setCardEvent(new ScribeItem.CardEvent(8)).build();
    }
}
