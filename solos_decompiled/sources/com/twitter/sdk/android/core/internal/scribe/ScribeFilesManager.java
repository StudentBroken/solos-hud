package com.twitter.sdk.android.core.internal.scribe;

import android.content.Context;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.events.EventTransform;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import io.fabric.sdk.android.services.events.QueueFileEventStorage;
import java.io.IOException;
import java.util.UUID;

/* JADX INFO: loaded from: classes62.dex */
class ScribeFilesManager extends EventsFilesManager<ScribeEvent> {
    static final String FILE_EXTENSION = ".tap";
    static final String FILE_PREFIX = "se";

    public ScribeFilesManager(Context context, EventTransform<ScribeEvent> transform, CurrentTimeProvider currentTimeProvider, QueueFileEventStorage eventsStorage, int defaultMaxFilesToKeep) throws IOException {
        super(context, transform, currentTimeProvider, eventsStorage, defaultMaxFilesToKeep);
    }

    @Override // io.fabric.sdk.android.services.events.EventsFilesManager
    protected String generateUniqueRollOverFileName() {
        UUID targetUUIDComponent = UUID.randomUUID();
        return FILE_PREFIX + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + targetUUIDComponent.toString() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + this.currentTimeProvider.getCurrentTimeMillis() + FILE_EXTENSION;
    }
}
