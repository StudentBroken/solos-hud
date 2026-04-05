package com.twitter.sdk.android.tweetui.internal;

import com.twitter.sdk.android.tweetui.TimelineCursor;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes9.dex */
public class TimelineStateHolder {
    TimelineCursor nextCursor;
    TimelineCursor previousCursor;
    public final AtomicBoolean requestInFlight = new AtomicBoolean(false);

    public TimelineStateHolder() {
    }

    public TimelineStateHolder(TimelineCursor nextCursor, TimelineCursor previousCursor) {
        this.nextCursor = nextCursor;
        this.previousCursor = previousCursor;
    }

    public void resetCursors() {
        this.nextCursor = null;
        this.previousCursor = null;
    }

    public Long positionForNext() {
        if (this.nextCursor == null) {
            return null;
        }
        return this.nextCursor.maxPosition;
    }

    public Long positionForPrevious() {
        if (this.previousCursor == null) {
            return null;
        }
        return this.previousCursor.minPosition;
    }

    public void setNextCursor(TimelineCursor timelineCursor) {
        this.nextCursor = timelineCursor;
        setCursorsIfNull(timelineCursor);
    }

    public void setPreviousCursor(TimelineCursor timelineCursor) {
        this.previousCursor = timelineCursor;
        setCursorsIfNull(timelineCursor);
    }

    public void setCursorsIfNull(TimelineCursor timelineCursor) {
        if (this.nextCursor == null) {
            this.nextCursor = timelineCursor;
        }
        if (this.previousCursor == null) {
            this.previousCursor = timelineCursor;
        }
    }

    public boolean startTimelineRequest() {
        return this.requestInFlight.compareAndSet(false, true);
    }

    public void finishTimelineRequest() {
        this.requestInFlight.set(false);
    }
}
