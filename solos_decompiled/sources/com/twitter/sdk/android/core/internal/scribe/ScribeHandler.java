package com.twitter.sdk.android.core.internal.scribe;

import android.content.Context;
import io.fabric.sdk.android.services.events.DisabledEventsStrategy;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import io.fabric.sdk.android.services.events.EventsHandler;
import io.fabric.sdk.android.services.events.EventsStrategy;
import java.util.concurrent.ScheduledExecutorService;

/* JADX INFO: loaded from: classes62.dex */
class ScribeHandler extends EventsHandler<ScribeEvent> {
    public ScribeHandler(Context context, EventsStrategy<ScribeEvent> strategy, EventsFilesManager filesManager, ScheduledExecutorService executorService) {
        super(context, strategy, filesManager, executorService);
    }

    public void scribe(ScribeEvent event) {
        recordEventAsync(event, false);
    }

    public void scribeAndFlush(ScribeEvent event) {
        recordEventAsync(event, true);
    }

    @Override // io.fabric.sdk.android.services.events.EventsHandler
    protected EventsStrategy<ScribeEvent> getDisabledEventsStrategy() {
        return new DisabledEventsStrategy();
    }
}
