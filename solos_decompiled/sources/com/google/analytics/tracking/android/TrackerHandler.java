package com.google.analytics.tracking.android;

import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
abstract class TrackerHandler {
    abstract void sendHit(Map<String, String> map);

    TrackerHandler() {
    }
}
