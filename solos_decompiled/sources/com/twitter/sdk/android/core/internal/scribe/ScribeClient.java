package com.twitter.sdk.android.core.internal.scribe;

import android.content.Context;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.internal.scribe.ScribeEvent;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import io.fabric.sdk.android.services.events.DisabledEventsStrategy;
import io.fabric.sdk.android.services.events.EventsStrategy;
import io.fabric.sdk.android.services.events.QueueFileEventStorage;
import io.fabric.sdk.android.services.persistence.FileStoreImpl;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import javax.net.ssl.SSLSocketFactory;

/* JADX INFO: loaded from: classes62.dex */
public class ScribeClient {
    private static final String STORAGE_DIR_BASE = "_se_to_send";
    private static final String WORKING_FILENAME_BASE = "_se.tap";
    private final TwitterAuthConfig authConfig;
    private final ScheduledExecutorService executor;
    private final IdManager idManager;
    private final Kit kit;
    private final ScribeConfig scribeConfig;
    final ConcurrentHashMap<Long, ScribeHandler> scribeHandlers = new ConcurrentHashMap<>(2);
    private final List<SessionManager<? extends Session>> sessionManagers;
    private final SSLSocketFactory sslSocketFactory;
    private final ScribeEvent.Transform transform;

    public ScribeClient(Kit kit, ScheduledExecutorService executor, ScribeConfig scribeConfig, ScribeEvent.Transform transform, TwitterAuthConfig authConfig, List<SessionManager<? extends Session>> sessionManagers, SSLSocketFactory sslSocketFactory, IdManager idManager) {
        this.kit = kit;
        this.executor = executor;
        this.scribeConfig = scribeConfig;
        this.transform = transform;
        this.authConfig = authConfig;
        this.sessionManagers = sessionManagers;
        this.sslSocketFactory = sslSocketFactory;
        this.idManager = idManager;
    }

    public boolean scribe(ScribeEvent event, long ownerId) {
        try {
            getScribeHandler(ownerId).scribe(event);
            return true;
        } catch (IOException e) {
            CommonUtils.logControlledError(this.kit.getContext(), "Failed to scribe event", e);
            return false;
        }
    }

    public boolean scribeAndFlush(ScribeEvent event, long ownerId) {
        try {
            getScribeHandler(ownerId).scribeAndFlush(event);
            return true;
        } catch (IOException e) {
            CommonUtils.logControlledError(this.kit.getContext(), "Failed to scribe event", e);
            return false;
        }
    }

    ScribeHandler getScribeHandler(long ownerId) throws IOException {
        if (!this.scribeHandlers.containsKey(Long.valueOf(ownerId))) {
            this.scribeHandlers.putIfAbsent(Long.valueOf(ownerId), newScribeHandler(ownerId));
        }
        return this.scribeHandlers.get(Long.valueOf(ownerId));
    }

    private ScribeHandler newScribeHandler(long ownerId) throws IOException {
        Context context = this.kit.getContext();
        QueueFileEventStorage storage = new QueueFileEventStorage(context, new FileStoreImpl(this.kit).getFilesDir(), getWorkingFileNameForOwner(ownerId), getStorageDirForOwner(ownerId));
        ScribeFilesManager filesManager = new ScribeFilesManager(context, this.transform, new SystemCurrentTimeProvider(), storage, this.scribeConfig.maxFilesToKeep);
        return new ScribeHandler(context, getScribeStrategy(ownerId, filesManager), filesManager, this.executor);
    }

    EventsStrategy<ScribeEvent> getScribeStrategy(long ownerId, ScribeFilesManager filesManager) {
        Context context = this.kit.getContext();
        if (this.scribeConfig.isEnabled) {
            CommonUtils.logControlled(context, "Scribe enabled");
            return new EnabledScribeStrategy(context, this.executor, filesManager, this.scribeConfig, new ScribeFilesSender(context, this.scribeConfig, ownerId, this.authConfig, this.sessionManagers, this.sslSocketFactory, this.executor, this.idManager));
        }
        CommonUtils.logControlled(context, "Scribe disabled");
        return new DisabledEventsStrategy();
    }

    String getWorkingFileNameForOwner(long ownerId) {
        return ownerId + WORKING_FILENAME_BASE;
    }

    String getStorageDirForOwner(long ownerId) {
        return ownerId + STORAGE_DIR_BASE;
    }
}
