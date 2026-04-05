package com.facebook.appevents;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import bolts.AppLinks;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.internal.ActivityLifecycleTracker;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public class AppEventsLogger {
    public static final String ACTION_APP_EVENTS_FLUSHED = "com.facebook.sdk.APP_EVENTS_FLUSHED";
    public static final String APP_EVENTS_EXTRA_FLUSH_RESULT = "com.facebook.sdk.APP_EVENTS_FLUSH_RESULT";
    public static final String APP_EVENTS_EXTRA_NUM_EVENTS_FLUSHED = "com.facebook.sdk.APP_EVENTS_NUM_EVENTS_FLUSHED";
    private static final String APP_EVENT_NAME_PUSH_OPENED = "fb_mobile_push_opened";
    public static final String APP_EVENT_PREFERENCES = "com.facebook.sdk.appEventPreferences";
    private static final String APP_EVENT_PUSH_PARAMETER_ACTION = "fb_push_action";
    private static final String APP_EVENT_PUSH_PARAMETER_CAMPAIGN = "fb_push_campaign";
    private static final int APP_SUPPORTS_ATTRIBUTION_ID_RECHECK_PERIOD_IN_SECONDS = 86400;
    private static final int FLUSH_APP_SESSION_INFO_IN_SECONDS = 30;
    private static final String PUSH_PAYLOAD_CAMPAIGN_KEY = "campaign";
    private static final String PUSH_PAYLOAD_KEY = "fb_push_payload";
    private static final String SOURCE_APPLICATION_HAS_BEEN_SET_BY_THIS_INTENT = "_fbSourceApplicationHasBeenSet";
    private static String anonymousAppDeviceGUID;
    private static ScheduledThreadPoolExecutor backgroundExecutor;
    private static boolean isActivateAppEventRequested;
    private static boolean isOpenedByApplink;
    private static String pushNotificationsRegistrationId;
    private static String sourceApplication;
    private final AccessTokenAppIdPair accessTokenAppId;
    private final String contextName;
    private static final String TAG = AppEventsLogger.class.getCanonicalName();
    private static FlushBehavior flushBehavior = FlushBehavior.AUTO;
    private static Object staticLock = new Object();

    public enum FlushBehavior {
        AUTO,
        EXPLICIT_ONLY
    }

    public static void activateApp(Application application) {
        activateApp(application, (String) null);
    }

    public static void activateApp(Application application, String applicationId) {
        if (!FacebookSdk.isInitialized()) {
            throw new FacebookException("The Facebook sdk must be initialized before calling activateApp");
        }
        if (applicationId == null) {
            applicationId = FacebookSdk.getApplicationId();
        }
        FacebookSdk.publishInstallAsync(application, applicationId);
        ActivityLifecycleTracker.startTracking(application, applicationId);
    }

    @Deprecated
    public static void activateApp(Context context) {
        if (ActivityLifecycleTracker.isTracking()) {
            Log.w(TAG, "activateApp events are being logged automatically. There's no need to call activateApp explicitly, this is safe to remove.");
        } else {
            FacebookSdk.sdkInitialize(context);
            activateApp(context, Utility.getMetadataApplicationId(context));
        }
    }

    @Deprecated
    public static void activateApp(Context context, String applicationId) {
        if (ActivityLifecycleTracker.isTracking()) {
            Log.w(TAG, "activateApp events are being logged automatically. There's no need to call activateApp explicitly, this is safe to remove.");
            return;
        }
        if (context == null || applicationId == null) {
            throw new IllegalArgumentException("Both context and applicationId must be non-null");
        }
        if (context instanceof Activity) {
            setSourceApplication((Activity) context);
        } else {
            resetSourceApplication();
            Log.d(AppEventsLogger.class.getName(), "To set source application the context of activateApp must be an instance of Activity");
        }
        FacebookSdk.publishInstallAsync(context, applicationId);
        AppEventsLogger logger = new AppEventsLogger(context, applicationId, (AccessToken) null);
        final long eventTime = System.currentTimeMillis();
        final String sourceApplicationInfo = getSourceApplication();
        backgroundExecutor.execute(new Runnable() { // from class: com.facebook.appevents.AppEventsLogger.1
            @Override // java.lang.Runnable
            public void run() {
                AppEventsLogger.this.logAppSessionResumeEvent(eventTime, sourceApplicationInfo);
            }
        });
    }

    @Deprecated
    public static void deactivateApp(Context context) {
        if (ActivityLifecycleTracker.isTracking()) {
            Log.w(TAG, "deactivateApp events are being logged automatically. There's no need to call deactivateApp, this is safe to remove.");
        } else {
            deactivateApp(context, Utility.getMetadataApplicationId(context));
        }
    }

    @Deprecated
    public static void deactivateApp(Context context, String applicationId) {
        if (ActivityLifecycleTracker.isTracking()) {
            Log.w(TAG, "deactivateApp events are being logged automatically. There's no need to call deactivateApp, this is safe to remove.");
            return;
        }
        if (context == null || applicationId == null) {
            throw new IllegalArgumentException("Both context and applicationId must be non-null");
        }
        resetSourceApplication();
        AppEventsLogger logger = new AppEventsLogger(context, applicationId, (AccessToken) null);
        final long eventTime = System.currentTimeMillis();
        backgroundExecutor.execute(new Runnable() { // from class: com.facebook.appevents.AppEventsLogger.2
            @Override // java.lang.Runnable
            public void run() {
                AppEventsLogger.this.logAppSessionSuspendEvent(eventTime);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppSessionResumeEvent(long eventTime, String sourceApplicationInfo) {
        PersistedAppSessionInfo.onResume(FacebookSdk.getApplicationContext(), this.accessTokenAppId, this, eventTime, sourceApplicationInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppSessionSuspendEvent(long eventTime) {
        PersistedAppSessionInfo.onSuspend(FacebookSdk.getApplicationContext(), this.accessTokenAppId, this, eventTime);
    }

    public static AppEventsLogger newLogger(Context context) {
        return new AppEventsLogger(context, (String) null, (AccessToken) null);
    }

    public static AppEventsLogger newLogger(Context context, AccessToken accessToken) {
        return new AppEventsLogger(context, (String) null, accessToken);
    }

    public static AppEventsLogger newLogger(Context context, String applicationId, AccessToken accessToken) {
        return new AppEventsLogger(context, applicationId, accessToken);
    }

    public static AppEventsLogger newLogger(Context context, String applicationId) {
        return new AppEventsLogger(context, applicationId, (AccessToken) null);
    }

    public static FlushBehavior getFlushBehavior() {
        FlushBehavior flushBehavior2;
        synchronized (staticLock) {
            flushBehavior2 = flushBehavior;
        }
        return flushBehavior2;
    }

    public static void setFlushBehavior(FlushBehavior flushBehavior2) {
        synchronized (staticLock) {
            flushBehavior = flushBehavior2;
        }
    }

    public void logEvent(String eventName) {
        logEvent(eventName, (Bundle) null);
    }

    public void logEvent(String eventName, double valueToSum) {
        logEvent(eventName, valueToSum, (Bundle) null);
    }

    public void logEvent(String eventName, Bundle parameters) {
        logEvent(eventName, null, parameters, false, ActivityLifecycleTracker.getCurrentSessionGuid());
    }

    public void logEvent(String eventName, double valueToSum, Bundle parameters) {
        logEvent(eventName, Double.valueOf(valueToSum), parameters, false, ActivityLifecycleTracker.getCurrentSessionGuid());
    }

    public void logPurchase(BigDecimal purchaseAmount, Currency currency) {
        logPurchase(purchaseAmount, currency, null);
    }

    public void logPurchase(BigDecimal purchaseAmount, Currency currency, Bundle parameters) {
        if (purchaseAmount == null) {
            notifyDeveloperError("purchaseAmount cannot be null");
            return;
        }
        if (currency == null) {
            notifyDeveloperError("currency cannot be null");
            return;
        }
        if (parameters == null) {
            parameters = new Bundle();
        }
        parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency.getCurrencyCode());
        logEvent(AppEventsConstants.EVENT_NAME_PURCHASED, purchaseAmount.doubleValue(), parameters);
        eagerFlush();
    }

    public void logPushNotificationOpen(Bundle payload) {
        logPushNotificationOpen(payload, null);
    }

    public void logPushNotificationOpen(Bundle payload, String action) {
        String campaignId = null;
        try {
            String payloadString = payload.getString(PUSH_PAYLOAD_KEY);
            if (!Utility.isNullOrEmpty(payloadString)) {
                JSONObject facebookPayload = new JSONObject(payloadString);
                campaignId = facebookPayload.getString("campaign");
            } else {
                return;
            }
        } catch (JSONException e) {
        }
        if (campaignId == null) {
            Logger.log(LoggingBehavior.DEVELOPER_ERRORS, TAG, "Malformed payload specified for logging a push notification open.");
            return;
        }
        Bundle parameters = new Bundle();
        parameters.putString(APP_EVENT_PUSH_PARAMETER_CAMPAIGN, campaignId);
        if (action != null) {
            parameters.putString(APP_EVENT_PUSH_PARAMETER_ACTION, action);
        }
        logEvent(APP_EVENT_NAME_PUSH_OPENED, parameters);
    }

    public void flush() {
        AppEventQueue.flush(FlushReason.EXPLICIT);
    }

    public static void onContextStop() {
        AppEventQueue.persistToDisk();
    }

    public boolean isValidForAccessToken(AccessToken accessToken) {
        AccessTokenAppIdPair other = new AccessTokenAppIdPair(accessToken);
        return this.accessTokenAppId.equals(other);
    }

    public static void setPushNotificationsRegistrationId(String registrationId) {
        synchronized (staticLock) {
            pushNotificationsRegistrationId = registrationId;
        }
    }

    static String getPushNotificationsRegistrationId() {
        String str;
        synchronized (staticLock) {
            str = pushNotificationsRegistrationId;
        }
        return str;
    }

    public void logSdkEvent(String eventName, Double valueToSum, Bundle parameters) {
        logEvent(eventName, valueToSum, parameters, true, ActivityLifecycleTracker.getCurrentSessionGuid());
    }

    public String getApplicationId() {
        return this.accessTokenAppId.getApplicationId();
    }

    private AppEventsLogger(Context context, String applicationId, AccessToken accessToken) {
        this(Utility.getActivityName(context), applicationId, accessToken);
    }

    protected AppEventsLogger(String activityName, String applicationId, AccessToken accessToken) {
        Validate.sdkInitialized();
        this.contextName = activityName;
        accessToken = accessToken == null ? AccessToken.getCurrentAccessToken() : accessToken;
        if (accessToken != null && (applicationId == null || applicationId.equals(accessToken.getApplicationId()))) {
            this.accessTokenAppId = new AccessTokenAppIdPair(accessToken);
        } else {
            this.accessTokenAppId = new AccessTokenAppIdPair(null, applicationId == null ? Utility.getMetadataApplicationId(FacebookSdk.getApplicationContext()) : applicationId);
        }
        initializeTimersIfNeeded();
    }

    private static void initializeTimersIfNeeded() {
        synchronized (staticLock) {
            if (backgroundExecutor == null) {
                backgroundExecutor = new ScheduledThreadPoolExecutor(1);
                Runnable attributionRecheckRunnable = new Runnable() { // from class: com.facebook.appevents.AppEventsLogger.3
                    @Override // java.lang.Runnable
                    public void run() {
                        Set<String> applicationIds = new HashSet<>();
                        for (AccessTokenAppIdPair accessTokenAppId : AppEventQueue.getKeySet()) {
                            applicationIds.add(accessTokenAppId.getApplicationId());
                        }
                        for (String applicationId : applicationIds) {
                            Utility.queryAppSettings(applicationId, true);
                        }
                    }
                };
                backgroundExecutor.scheduleAtFixedRate(attributionRecheckRunnable, 0L, 86400L, TimeUnit.SECONDS);
            }
        }
    }

    private void logEvent(String eventName, Double valueToSum, Bundle parameters, boolean isImplicitlyLogged, @Nullable UUID currentSessionId) {
        try {
            AppEvent event = new AppEvent(this.contextName, eventName, valueToSum, parameters, isImplicitlyLogged, currentSessionId);
            logEvent(FacebookSdk.getApplicationContext(), event, this.accessTokenAppId);
        } catch (FacebookException e) {
            Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Invalid app event: %s", e.toString());
        } catch (JSONException jsonException) {
            Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "JSON encoding for app event failed: '%s'", jsonException.toString());
        }
    }

    private static void logEvent(Context context, AppEvent event, AccessTokenAppIdPair accessTokenAppId) {
        AppEventQueue.add(accessTokenAppId, event);
        if (!event.getIsImplicit() && !isActivateAppEventRequested) {
            if (event.getName() == AppEventsConstants.EVENT_NAME_ACTIVATED_APP) {
                isActivateAppEventRequested = true;
            } else {
                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Warning: Please call AppEventsLogger.activateApp(...)from the long-lived activity's onResume() methodbefore logging other app events.");
            }
        }
    }

    static void eagerFlush() {
        if (getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY) {
            AppEventQueue.flush(FlushReason.EAGER_FLUSHING_EVENT);
        }
    }

    private static void notifyDeveloperError(String message) {
        Logger.log(LoggingBehavior.DEVELOPER_ERRORS, "AppEvents", message);
    }

    private static void setSourceApplication(Activity activity) {
        ComponentName callingApplication = activity.getCallingActivity();
        if (callingApplication != null) {
            String callingApplicationPackage = callingApplication.getPackageName();
            if (callingApplicationPackage.equals(activity.getPackageName())) {
                resetSourceApplication();
                return;
            }
            sourceApplication = callingApplicationPackage;
        }
        Intent openIntent = activity.getIntent();
        if (openIntent == null || openIntent.getBooleanExtra(SOURCE_APPLICATION_HAS_BEEN_SET_BY_THIS_INTENT, false)) {
            resetSourceApplication();
            return;
        }
        Bundle applinkData = AppLinks.getAppLinkData(openIntent);
        if (applinkData == null) {
            resetSourceApplication();
            return;
        }
        isOpenedByApplink = true;
        Bundle applinkReferrerData = applinkData.getBundle("referer_app_link");
        if (applinkReferrerData == null) {
            sourceApplication = null;
            return;
        }
        String applinkReferrerPackage = applinkReferrerData.getString("package");
        sourceApplication = applinkReferrerPackage;
        openIntent.putExtra(SOURCE_APPLICATION_HAS_BEEN_SET_BY_THIS_INTENT, true);
    }

    static void setSourceApplication(String applicationPackage, boolean openByAppLink) {
        sourceApplication = applicationPackage;
        isOpenedByApplink = openByAppLink;
    }

    static String getSourceApplication() {
        String openType = "Unclassified";
        if (isOpenedByApplink) {
            openType = "Applink";
        }
        if (sourceApplication != null) {
            return openType + "(" + sourceApplication + ")";
        }
        return openType;
    }

    static void resetSourceApplication() {
        sourceApplication = null;
        isOpenedByApplink = false;
    }

    public static String getAnonymousAppDeviceGUID(Context context) {
        if (anonymousAppDeviceGUID == null) {
            synchronized (staticLock) {
                if (anonymousAppDeviceGUID == null) {
                    SharedPreferences preferences = context.getSharedPreferences(APP_EVENT_PREFERENCES, 0);
                    anonymousAppDeviceGUID = preferences.getString("anonymousAppDeviceGUID", null);
                    if (anonymousAppDeviceGUID == null) {
                        anonymousAppDeviceGUID = "XZ" + UUID.randomUUID().toString();
                        context.getSharedPreferences(APP_EVENT_PREFERENCES, 0).edit().putString("anonymousAppDeviceGUID", anonymousAppDeviceGUID).apply();
                    }
                }
            }
        }
        return anonymousAppDeviceGUID;
    }

    static class PersistedAppSessionInfo {
        private static final String PERSISTED_SESSION_INFO_FILENAME = "AppEventsLogger.persistedsessioninfo";
        private static Map<AccessTokenAppIdPair, FacebookTimeSpentData> appSessionInfoMap;
        private static final Object staticLock = new Object();
        private static boolean hasChanges = false;
        private static boolean isLoaded = false;
        private static final Runnable appSessionInfoFlushRunnable = new Runnable() { // from class: com.facebook.appevents.AppEventsLogger.PersistedAppSessionInfo.1
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                PersistedAppSessionInfo.saveAppSessionInformation(FacebookSdk.getApplicationContext());
            }
        };

        PersistedAppSessionInfo() {
        }

        private static void restoreAppSessionInformation(Context context) throws Throwable {
            ObjectInputStream ois;
            ObjectInputStream ois2 = null;
            synchronized (staticLock) {
                try {
                    try {
                        if (!isLoaded) {
                            try {
                                ois = new ObjectInputStream(context.openFileInput(PERSISTED_SESSION_INFO_FILENAME));
                            } catch (FileNotFoundException e) {
                            } catch (Exception e2) {
                                e = e2;
                            }
                            try {
                                appSessionInfoMap = (HashMap) ois.readObject();
                                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "App session info loaded");
                                try {
                                    Utility.closeQuietly(ois);
                                    context.deleteFile(PERSISTED_SESSION_INFO_FILENAME);
                                    if (appSessionInfoMap == null) {
                                        appSessionInfoMap = new HashMap();
                                    }
                                    isLoaded = true;
                                    hasChanges = false;
                                    ois2 = ois;
                                } catch (Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            } catch (FileNotFoundException e3) {
                                ois2 = ois;
                                Utility.closeQuietly(ois2);
                                context.deleteFile(PERSISTED_SESSION_INFO_FILENAME);
                                if (appSessionInfoMap == null) {
                                    appSessionInfoMap = new HashMap();
                                }
                                isLoaded = true;
                                hasChanges = false;
                            } catch (Exception e4) {
                                e = e4;
                                ois2 = ois;
                                Log.w(AppEventsLogger.TAG, "Got unexpected exception restoring app session info: " + e.toString());
                                Utility.closeQuietly(ois2);
                                context.deleteFile(PERSISTED_SESSION_INFO_FILENAME);
                                if (appSessionInfoMap == null) {
                                    appSessionInfoMap = new HashMap();
                                }
                                isLoaded = true;
                                hasChanges = false;
                            } catch (Throwable th2) {
                                th = th2;
                                ois2 = ois;
                                Utility.closeQuietly(ois2);
                                context.deleteFile(PERSISTED_SESSION_INFO_FILENAME);
                                if (appSessionInfoMap == null) {
                                    appSessionInfoMap = new HashMap();
                                }
                                isLoaded = true;
                                hasChanges = false;
                                throw th;
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        }

        static void saveAppSessionInformation(Context context) throws Throwable {
            ObjectOutputStream oos;
            ObjectOutputStream oos2 = null;
            synchronized (staticLock) {
                try {
                    if (hasChanges) {
                        try {
                            try {
                                oos = new ObjectOutputStream(new BufferedOutputStream(context.openFileOutput(PERSISTED_SESSION_INFO_FILENAME, 0)));
                            } catch (Exception e) {
                                e = e;
                            }
                        } catch (Throwable th) {
                            th = th;
                        }
                        try {
                            oos.writeObject(appSessionInfoMap);
                            hasChanges = false;
                            Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "App session info saved");
                            try {
                                Utility.closeQuietly(oos);
                            } catch (Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            oos2 = oos;
                            Log.w(AppEventsLogger.TAG, "Got unexpected exception while writing app session info: " + e.toString());
                            Utility.closeQuietly(oos2);
                        } catch (Throwable th3) {
                            th = th3;
                            oos2 = oos;
                            Utility.closeQuietly(oos2);
                            throw th;
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        }

        static void onResume(Context context, AccessTokenAppIdPair accessTokenAppId, AppEventsLogger logger, long eventTime, String sourceApplicationInfo) {
            synchronized (staticLock) {
                FacebookTimeSpentData timeSpentData = getTimeSpentData(context, accessTokenAppId);
                timeSpentData.onResume(logger, eventTime, sourceApplicationInfo);
                onTimeSpentDataUpdate();
            }
        }

        static void onSuspend(Context context, AccessTokenAppIdPair accessTokenAppId, AppEventsLogger logger, long eventTime) {
            synchronized (staticLock) {
                FacebookTimeSpentData timeSpentData = getTimeSpentData(context, accessTokenAppId);
                timeSpentData.onSuspend(logger, eventTime);
                onTimeSpentDataUpdate();
            }
        }

        private static FacebookTimeSpentData getTimeSpentData(Context context, AccessTokenAppIdPair accessTokenAppId) throws Throwable {
            restoreAppSessionInformation(context);
            FacebookTimeSpentData result = appSessionInfoMap.get(accessTokenAppId);
            if (result == null) {
                FacebookTimeSpentData result2 = new FacebookTimeSpentData();
                appSessionInfoMap.put(accessTokenAppId, result2);
                return result2;
            }
            return result;
        }

        private static void onTimeSpentDataUpdate() {
            if (!hasChanges) {
                hasChanges = true;
                AppEventsLogger.backgroundExecutor.schedule(appSessionInfoFlushRunnable, 30L, TimeUnit.SECONDS);
            }
        }
    }
}
