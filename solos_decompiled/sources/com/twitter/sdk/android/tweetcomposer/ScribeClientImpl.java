package com.twitter.sdk.android.tweetcomposer;

import com.twitter.sdk.android.core.internal.scribe.DefaultScribeClient;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;
import com.twitter.sdk.android.core.internal.scribe.ScribeItem;
import java.util.List;

/* JADX INFO: loaded from: classes29.dex */
class ScribeClientImpl implements ScribeClient {
    private final DefaultScribeClient scribeClient;

    public ScribeClientImpl(DefaultScribeClient scribeClient) {
        this.scribeClient = scribeClient;
    }

    @Override // com.twitter.sdk.android.tweetcomposer.ScribeClient
    public void scribe(EventNamespace eventNamespace, List<ScribeItem> items) {
        if (this.scribeClient != null) {
            this.scribeClient.scribe(eventNamespace, items);
        }
    }
}
