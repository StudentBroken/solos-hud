package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.internal.scribe.EventNamespace;
import com.twitter.sdk.android.core.internal.scribe.SyndicationClientEvent;

/* JADX INFO: loaded from: classes9.dex */
public class ScribeConstants {
    static final String SCRIBE_IMPRESSION_ACTION = "impression";
    static final String SCRIBE_INITIAL_COMPONENT = "initial";
    static final String SCRIBE_INITIAL_ELEMENT = "initial";
    static final String SCRIBE_TIMELINE_PAGE = "timeline";
    static final String SCRIBE_TIMELINE_SECTION = "timeline";
    static final String SYNDICATED_SDK_IMPRESSION_ELEMENT = "";
    static final String TFW_CLIENT_EVENT_PAGE = "android";

    private ScribeConstants() {
    }

    static EventNamespace getSyndicatedSdkTimelineNamespace(String timelineType) {
        return new EventNamespace.Builder().setClient("android").setPage("timeline").setSection(timelineType).setComponent("initial").setElement("").setAction(SCRIBE_IMPRESSION_ACTION).builder();
    }

    static EventNamespace getTfwClientTimelineNamespace(String timelineType) {
        return new EventNamespace.Builder().setClient(SyndicationClientEvent.CLIENT_NAME).setPage("android").setSection("timeline").setComponent(timelineType).setElement("initial").setAction(SCRIBE_IMPRESSION_ACTION).builder();
    }
}
