package com.twitter.sdk.android.core.internal.scribe;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public class SyndicatedSdkImpressionEvent extends ScribeEvent {
    public static final String CLIENT_NAME = "android";
    private static final String SCRIBE_CATEGORY = "syndicated_sdk_impression";

    @SerializedName("device_id_created_at")
    public final long deviceIdCreatedAt;

    @SerializedName("external_ids")
    public final ExternalIds externalIds;

    @SerializedName("language")
    public final String language;

    public SyndicatedSdkImpressionEvent(EventNamespace eventNamespace, long timestamp, String language, String adId) {
        this(eventNamespace, timestamp, language, adId, Collections.emptyList());
    }

    public SyndicatedSdkImpressionEvent(EventNamespace eventNamespace, long timestamp, String language, String adId, List<ScribeItem> items) {
        super(SCRIBE_CATEGORY, eventNamespace, timestamp, items);
        this.language = language;
        this.externalIds = new ExternalIds(adId);
        this.deviceIdCreatedAt = 0L;
    }

    public class ExternalIds {

        @SerializedName("AD_ID")
        public final String adId;

        public ExternalIds(String adId) {
            this.adId = adId;
        }
    }
}
