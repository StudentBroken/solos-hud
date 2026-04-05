package com.twitter.sdk.android.core.internal.scribe;

import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public class ScribeEventFactory {
    public static ScribeEvent newScribeEvent(EventNamespace ns, long timestamp, String language, String advertisingId) {
        return newScribeEvent(ns, timestamp, language, advertisingId, Collections.emptyList());
    }

    public static ScribeEvent newScribeEvent(EventNamespace ns, long timestamp, String language, String advertisingId, List<ScribeItem> items) {
        switch (ns.client) {
            case "tfw":
                return new SyndicationClientEvent(ns, timestamp, language, advertisingId, items);
            default:
                return new SyndicatedSdkImpressionEvent(ns, timestamp, language, advertisingId, items);
        }
    }
}
