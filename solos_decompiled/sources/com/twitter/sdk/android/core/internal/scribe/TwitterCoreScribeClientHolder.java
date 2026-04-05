package com.twitter.sdk.android.core.internal.scribe;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.services.common.IdManager;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterCoreScribeClientHolder {
    private static final String KIT_NAME = "TwitterCore";
    private static DefaultScribeClient instance;

    public static DefaultScribeClient getScribeClient() {
        return instance;
    }

    public static void initialize(TwitterCore kit, List<SessionManager<? extends Session>> sessionManagers, IdManager idManager) {
        instance = new DefaultScribeClient(kit, KIT_NAME, sessionManagers, idManager);
    }
}
