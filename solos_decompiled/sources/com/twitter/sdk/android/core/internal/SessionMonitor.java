package com.twitter.sdk.android.core.internal;

import android.app.Activity;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import io.fabric.sdk.android.ActivityLifecycleManager;
import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes62.dex */
public class SessionMonitor<T extends Session> {
    private final ExecutorService executorService;
    protected final MonitorState monitorState;
    private final SessionManager<T> sessionManager;
    private final SessionVerifier sessionVerifier;
    private final SystemCurrentTimeProvider time;

    public SessionMonitor(SessionManager<T> sessionManager, ExecutorService executorService, SessionVerifier sessionVerifier) {
        this(sessionManager, new SystemCurrentTimeProvider(), executorService, new MonitorState(), sessionVerifier);
    }

    SessionMonitor(SessionManager<T> sessionManager, SystemCurrentTimeProvider time, ExecutorService executorService, MonitorState monitorState, SessionVerifier sessionVerifier) {
        this.time = time;
        this.sessionManager = sessionManager;
        this.executorService = executorService;
        this.monitorState = monitorState;
        this.sessionVerifier = sessionVerifier;
    }

    public void monitorActivityLifecycle(ActivityLifecycleManager activityLifecycleManager) {
        activityLifecycleManager.registerCallbacks(new ActivityLifecycleManager.Callbacks() { // from class: com.twitter.sdk.android.core.internal.SessionMonitor.1
            @Override // io.fabric.sdk.android.ActivityLifecycleManager.Callbacks
            public void onActivityStarted(Activity activity) {
                SessionMonitor.this.triggerVerificationIfNecessary();
            }
        });
    }

    public void triggerVerificationIfNecessary() {
        Session session = this.sessionManager.getActiveSession();
        long currentTime = this.time.getCurrentTimeMillis();
        boolean startVerification = session != null && this.monitorState.beginVerification(currentTime);
        if (startVerification) {
            this.executorService.submit(new Runnable() { // from class: com.twitter.sdk.android.core.internal.SessionMonitor.2
                @Override // java.lang.Runnable
                public void run() {
                    SessionMonitor.this.verifyAll();
                }
            });
        }
    }

    protected void verifyAll() {
        for (T session : this.sessionManager.getSessionMap().values()) {
            this.sessionVerifier.verifySession(session);
        }
        this.monitorState.endVerification(this.time.getCurrentTimeMillis());
    }

    protected static class MonitorState {
        private static final long TIME_THRESHOLD_IN_MILLIS = 21600000;
        public long lastVerification;
        private final Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        public boolean verifying;

        public synchronized boolean beginVerification(long currentTime) {
            boolean z = true;
            synchronized (this) {
                boolean isPastThreshold = currentTime - this.lastVerification > 21600000;
                boolean dayHasChanged = !isOnSameDate(currentTime, this.lastVerification);
                if (this.verifying || !(isPastThreshold || dayHasChanged)) {
                    z = false;
                } else {
                    this.verifying = true;
                }
            }
            return z;
        }

        public synchronized void endVerification(long currentTime) {
            this.verifying = false;
            this.lastVerification = currentTime;
        }

        private boolean isOnSameDate(long timeA, long timeB) {
            this.utcCalendar.setTimeInMillis(timeA);
            int dayA = this.utcCalendar.get(6);
            int yearA = this.utcCalendar.get(1);
            this.utcCalendar.setTimeInMillis(timeB);
            int dayB = this.utcCalendar.get(6);
            int yearB = this.utcCalendar.get(1);
            return dayA == dayB && yearA == yearB;
        }
    }
}
