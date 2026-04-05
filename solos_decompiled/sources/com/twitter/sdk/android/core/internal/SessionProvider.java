package com.twitter.sdk.android.core.internal;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public abstract class SessionProvider {
    private final List<SessionManager<? extends Session>> sessionManagers;

    public abstract void requestAuth(Callback<Session> callback);

    public SessionProvider(List<SessionManager<? extends Session>> sessionManagers) {
        this.sessionManagers = sessionManagers;
    }

    public Session getActiveSession() {
        Session session = null;
        for (SessionManager<? extends Session> sessionManager : this.sessionManagers) {
            session = sessionManager.getActiveSession();
            if (session != null) {
                break;
            }
        }
        return session;
    }
}
