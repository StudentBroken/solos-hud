package com.twitter.sdk.android.core.internal.scribe;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public class SyndicationClientEvent extends ScribeEvent {
    public static final String CLIENT_NAME = "tfw";
    private static final String SCRIBE_CATEGORY = "tfw_client_event";

    @SerializedName("external_ids")
    public final ExternalIds externalIds;

    @SerializedName("language")
    public final String language;

    public SyndicationClientEvent(EventNamespace eventNamespace, long timestamp, String language, String adId) {
        this(eventNamespace, timestamp, language, adId, Collections.emptyList());
    }

    public SyndicationClientEvent(EventNamespace eventNamespace, long timestamp, String language, String adId, List<ScribeItem> items) {
        super(SCRIBE_CATEGORY, eventNamespace, timestamp, items);
        this.language = language;
        this.externalIds = new ExternalIds(adId);
    }

    public class ExternalIds {

        @SerializedName("6")
        public final String adId;

        public ExternalIds(String adId) {
            this.adId = adId;
        }
    }
}
