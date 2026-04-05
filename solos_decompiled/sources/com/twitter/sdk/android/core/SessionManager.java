package com.twitter.sdk.android.core;

import com.twitter.sdk.android.core.Session;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public interface SessionManager<T extends Session> {
    void clearActiveSession();

    void clearSession(long j);

    T getActiveSession();

    T getSession(long j);

    Map<Long, T> getSessionMap();

    void setActiveSession(T t);

    void setSession(long j, T t);
}
