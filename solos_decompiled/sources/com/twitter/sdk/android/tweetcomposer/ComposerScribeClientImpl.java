package com.twitter.sdk.android.tweetcomposer;

import com.twitter.sdk.android.core.internal.scribe.EventNamespace;
import com.twitter.sdk.android.core.internal.scribe.ScribeItem;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes29.dex */
class ComposerScribeClientImpl implements ComposerScribeClient {
    private final ScribeClient scribeClient;

    ComposerScribeClientImpl(ScribeClient scribeClient) {
        if (scribeClient == null) {
            throw new NullPointerException("scribeClient must not be null");
        }
        this.scribeClient = scribeClient;
    }

    @Override // com.twitter.sdk.android.tweetcomposer.ComposerScribeClient
    public void impression(Card card) {
        EventNamespace ns = ScribeConstants.ComposerEventBuilder.setComponent("").setElement("").setAction("impression").builder();
        List<ScribeItem> items = new ArrayList<>();
        items.add(ScribeConstants.newCardScribeItem(card));
        this.scribeClient.scribe(ns, items);
    }

    @Override // com.twitter.sdk.android.tweetcomposer.ComposerScribeClient
    public void click(Card card, String element) {
        EventNamespace ns = ScribeConstants.ComposerEventBuilder.setComponent("").setElement(element).setAction("click").builder();
        List<ScribeItem> items = new ArrayList<>();
        items.add(ScribeConstants.newCardScribeItem(card));
        this.scribeClient.scribe(ns, items);
    }
}
