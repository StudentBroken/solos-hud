package com.twitter.sdk.android.core.internal.scribe;

import android.os.Build;
import android.text.TextUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.scribe.ScribeEvent;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.ExecutorUtils;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.settings.SettingsData;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/* JADX INFO: loaded from: classes62.dex */
public class DefaultScribeClient extends ScribeClient {
    private static final String DEBUG_BUILD = "debug";
    private static final String SCRIBE_PATH_TYPE = "sdk";
    private static final String SCRIBE_PATH_VERSION = "i";
    private static final String SCRIBE_URL = "https://syndication.twitter.com";
    private static volatile ScheduledExecutorService executor;
    private final String advertisingId;
    private final Kit kit;
    private final List<SessionManager<? extends Session>> sessionManagers;

    public DefaultScribeClient(Kit kit, String kitName, List<SessionManager<? extends Session>> sessionManagers, IdManager idManager) {
        this(kit, kitName, getGson(), sessionManagers, idManager);
    }

    public DefaultScribeClient(Kit kit, String kitName, Gson gson, List<SessionManager<? extends Session>> sessionManagers, IdManager idManager) {
        super(kit, getExecutor(), getScribeConfig(Settings.getInstance().awaitSettingsData(), getUserAgent(kitName, kit)), new ScribeEvent.Transform(gson), TwitterCore.getInstance().getAuthConfig(), sessionManagers, TwitterCore.getInstance().getSSLSocketFactory(), idManager);
        this.sessionManagers = sessionManagers;
        this.kit = kit;
        this.advertisingId = idManager.getAdvertisingId();
    }

    public void scribe(EventNamespace... namespaces) {
        for (EventNamespace ns : namespaces) {
            scribe(ns, Collections.emptyList());
        }
    }

    public void scribe(EventNamespace namespace, List<ScribeItem> items) {
        String language;
        if (this.kit.getContext() != null) {
            language = this.kit.getContext().getResources().getConfiguration().locale.getLanguage();
        } else {
            language = "";
        }
        long timestamp = System.currentTimeMillis();
        scribe(ScribeEventFactory.newScribeEvent(namespace, timestamp, language, this.advertisingId, items));
    }

    public void scribe(ScribeEvent event) {
        super.scribe(event, getScribeSessionId(getActiveSession()));
    }

    Session getActiveSession() {
        Session session = null;
        for (SessionManager<? extends Session> sessionManager : this.sessionManagers) {
            session = sessionManager.getActiveSession();
            if (session != null) {
                break;
            }
        }
        return session;
    }

    long getScribeSessionId(Session activeSession) {
        if (activeSession != null) {
            long scribeSessionId = activeSession.getId();
            return scribeSessionId;
        }
        return 0L;
    }

    private static Gson getGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    private static ScheduledExecutorService getExecutor() {
        if (executor == null) {
            synchronized (DefaultScribeClient.class) {
                if (executor == null) {
                    executor = ExecutorUtils.buildSingleThreadScheduledExecutorService("scribe");
                }
            }
        }
        return executor;
    }

    static ScribeConfig getScribeConfig(SettingsData settingsData, String userAgent) {
        int maxFilesToKeep;
        int sendIntervalSeconds;
        if (settingsData != null && settingsData.analyticsSettingsData != null) {
            maxFilesToKeep = settingsData.analyticsSettingsData.maxPendingSendFileCount;
            sendIntervalSeconds = settingsData.analyticsSettingsData.flushIntervalSeconds;
        } else {
            maxFilesToKeep = 100;
            sendIntervalSeconds = 600;
        }
        String scribeUrl = getScribeUrl(SCRIBE_URL, "");
        return new ScribeConfig(isEnabled(), scribeUrl, SCRIBE_PATH_VERSION, "sdk", "", userAgent, maxFilesToKeep, sendIntervalSeconds);
    }

    private static boolean isEnabled() {
        return !"release".equals("debug");
    }

    static String getUserAgent(String kitName, Kit kit) {
        return "Fabric/" + kit.getFabric().getVersion() + " (Android " + Build.VERSION.SDK_INT + ") " + kitName + "/" + kit.getVersion();
    }

    static String getScribeUrl(String defaultUrl, String overrideUrl) {
        return !TextUtils.isEmpty(overrideUrl) ? overrideUrl : defaultUrl;
    }
}
