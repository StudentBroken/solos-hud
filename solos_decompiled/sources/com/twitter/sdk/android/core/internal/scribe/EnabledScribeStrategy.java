package com.twitter.sdk.android.core.internal.scribe;

import android.content.Context;
import io.fabric.sdk.android.services.events.EnabledEventsStrategy;
import io.fabric.sdk.android.services.events.FilesSender;
import java.util.concurrent.ScheduledExecutorService;

/* JADX INFO: loaded from: classes62.dex */
class EnabledScribeStrategy extends EnabledEventsStrategy<ScribeEvent> {
    private final FilesSender filesSender;

    public EnabledScribeStrategy(Context context, ScheduledExecutorService executorService, ScribeFilesManager filesManager, ScribeConfig config, ScribeFilesSender filesSender) {
        super(context, executorService, filesManager);
        this.filesSender = filesSender;
        configureRollover(config.sendIntervalSeconds);
    }

    @Override // io.fabric.sdk.android.services.events.EventsStrategy
    public FilesSender getFilesSender() {
        return this.filesSender;
    }
}
