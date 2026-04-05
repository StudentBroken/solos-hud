package com.facebook.appevents.internal;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.appevents.internal.SourceApplicationInfo;
import com.facebook.internal.Utility;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes41.dex */
public class ActivityLifecycleTracker {
    private static final String INCORRECT_IMPL_WARNING = "Unexpected activity pause without a matching activity resume. Logging data may be incorrect. Make sure you call activateApp from your Application's onCreate method";
    private static final long INTERRUPTION_THRESHOLD_MILLISECONDS = 1000;
    private static String appId;
    private static volatile ScheduledFuture currentFuture;
    private static volatile SessionInfo currentSession;
    private static final String TAG = ActivityLifecycleTracker.class.getCanonicalName();
    private static final ScheduledExecutorService singleThreadExecutor = Executors.newSingleThreadScheduledExecutor();
    private static AtomicInteger foregroundActivityCount = new AtomicInteger(0);
    private static AtomicBoolean tracking = new AtomicBoolean(false);

    public static void startTracking(Application application, String appId2) {
        if (tracking.compareAndSet(false, true)) {
            appId = appId2;
            application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.1
                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    ActivityLifecycleTracker.assertIsMainThread();
                    ActivityLifecycleTracker.onActivityCreated(activity);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStarted(Activity activity) {
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityResumed(Activity activity) {
                    ActivityLifecycleTracker.assertIsMainThread();
                    ActivityLifecycleTracker.onActivityResumed(activity);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityPaused(Activity activity) {
                    ActivityLifecycleTracker.assertIsMainThread();
                    ActivityLifecycleTracker.onActivityPaused(activity);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStopped(Activity activity) {
                    AppEventsLogger.onContextStop();
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityDestroyed(Activity activity) {
                }
            });
        }
    }

    public static boolean isTracking() {
        return tracking.get();
    }

    public static UUID getCurrentSessionGuid() {
        if (currentSession != null) {
            return currentSession.getSessionId();
        }
        return null;
    }

    public static void onActivityCreated(final Activity activity) {
        final long currentTime = System.currentTimeMillis();
        Runnable handleActivityCreate = new Runnable() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.2
            @Override // java.lang.Runnable
            public void run() {
                if (ActivityLifecycleTracker.currentSession == null) {
                    Context applicationContext = activity.getApplicationContext();
                    String activityName = Utility.getActivityName(activity);
                    SessionInfo lastSession = SessionInfo.getStoredSessionInfo();
                    if (lastSession != null) {
                        SessionLogger.logDeactivateApp(applicationContext, activityName, lastSession, ActivityLifecycleTracker.appId);
                    }
                    SessionInfo unused = ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(currentTime), null);
                    SourceApplicationInfo sourceApplicationInfo = SourceApplicationInfo.Factory.create(activity);
                    ActivityLifecycleTracker.currentSession.setSourceApplicationInfo(sourceApplicationInfo);
                    SessionLogger.logActivateApp(applicationContext, activityName, sourceApplicationInfo, ActivityLifecycleTracker.appId);
                }
            }
        };
        singleThreadExecutor.execute(handleActivityCreate);
    }

    public static void onActivityResumed(final Activity activity) {
        foregroundActivityCount.incrementAndGet();
        cancelCurrentTask();
        final long currentTime = System.currentTimeMillis();
        Runnable handleActivityResume = new Runnable() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.3
            @Override // java.lang.Runnable
            public void run() {
                Context applicationContext = activity.getApplicationContext();
                String activityName = Utility.getActivityName(activity);
                if (ActivityLifecycleTracker.currentSession == null) {
                    SessionInfo unused = ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(currentTime), null);
                    SessionLogger.logActivateApp(applicationContext, activityName, null, ActivityLifecycleTracker.appId);
                } else if (ActivityLifecycleTracker.currentSession.getSessionLastEventTime() != null) {
                    long suspendTime = currentTime - ActivityLifecycleTracker.currentSession.getSessionLastEventTime().longValue();
                    if (suspendTime > ActivityLifecycleTracker.getSessionTimeoutInSeconds() * 1000) {
                        SessionLogger.logDeactivateApp(applicationContext, activityName, ActivityLifecycleTracker.currentSession, ActivityLifecycleTracker.appId);
                        SessionLogger.logActivateApp(applicationContext, activityName, null, ActivityLifecycleTracker.appId);
                        SessionInfo unused2 = ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(currentTime), null);
                    } else if (suspendTime > 1000) {
                        ActivityLifecycleTracker.currentSession.incrementInterruptionCount();
                    }
                }
                ActivityLifecycleTracker.currentSession.setSessionLastEventTime(Long.valueOf(currentTime));
                ActivityLifecycleTracker.currentSession.writeSessionToDisk();
            }
        };
        singleThreadExecutor.execute(handleActivityResume);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onActivityPaused(Activity activity) {
        int count = foregroundActivityCount.decrementAndGet();
        if (count < 0) {
            foregroundActivityCount.set(0);
            Log.w(TAG, INCORRECT_IMPL_WARNING);
        }
        cancelCurrentTask();
        final long currentTime = System.currentTimeMillis();
        final Context applicationContext = activity.getApplicationContext();
        final String activityName = Utility.getActivityName(activity);
        Runnable handleActivityPaused = new Runnable() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.4
            @Override // java.lang.Runnable
            public void run() {
                if (ActivityLifecycleTracker.currentSession == null) {
                    SessionInfo unused = ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(currentTime), null);
                }
                ActivityLifecycleTracker.currentSession.setSessionLastEventTime(Long.valueOf(currentTime));
                if (ActivityLifecycleTracker.foregroundActivityCount.get() <= 0) {
                    Runnable task = new Runnable() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ActivityLifecycleTracker.foregroundActivityCount.get() <= 0) {
                                SessionLogger.logDeactivateApp(applicationContext, activityName, ActivityLifecycleTracker.currentSession, ActivityLifecycleTracker.appId);
                                SessionInfo.clearSavedSessionFromDisk();
                                SessionInfo unused2 = ActivityLifecycleTracker.currentSession = null;
                            }
                            ScheduledFuture unused3 = ActivityLifecycleTracker.currentFuture = null;
                        }
                    };
                    ScheduledFuture unused2 = ActivityLifecycleTracker.currentFuture = ActivityLifecycleTracker.singleThreadExecutor.schedule(task, ActivityLifecycleTracker.getSessionTimeoutInSeconds(), TimeUnit.SECONDS);
                }
                ActivityLifecycleTracker.currentSession.writeSessionToDisk();
            }
        };
        singleThreadExecutor.execute(handleActivityPaused);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getSessionTimeoutInSeconds() {
        Utility.FetchedAppSettings settings = Utility.getAppSettingsWithoutQuery(FacebookSdk.getApplicationId());
        return settings == null ? Constants.getDefaultAppEventsSessionTimeoutInSeconds() : settings.getSessionTimeoutInSeconds();
    }

    private static void cancelCurrentTask() {
        if (currentFuture != null) {
            currentFuture.cancel(false);
        }
        currentFuture = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void assertIsMainThread() {
    }
}
