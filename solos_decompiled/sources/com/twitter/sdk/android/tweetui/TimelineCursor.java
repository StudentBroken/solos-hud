package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.models.Identifiable;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
public class TimelineCursor {
    public final Long maxPosition;
    public final Long minPosition;

    public TimelineCursor(Long minPosition, Long maxPosition) {
        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
    }

    TimelineCursor(List<? extends Identifiable> items) {
        this.minPosition = items.size() > 0 ? Long.valueOf(items.get(items.size() - 1).getId()) : null;
        this.maxPosition = items.size() > 0 ? Long.valueOf(items.get(0).getId()) : null;
    }
}
