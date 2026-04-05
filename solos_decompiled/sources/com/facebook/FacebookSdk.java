package com.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Base64;
import android.util.Log;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.AppEventsLoggerUtility;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.BoltsMeasurementEventListener;
import com.facebook.internal.LockOnGetVariable;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.File;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public final class FacebookSdk {
    public static final String APPLICATION_ID_PROPERTY = "com.facebook.sdk.ApplicationId";
    public static final String APPLICATION_NAME_PROPERTY = "com.facebook.sdk.ApplicationName";
    private static final String ATTRIBUTION_PREFERENCES = "com.facebook.sdk.attributionTracking";
    static final String CALLBACK_OFFSET_CHANGED_AFTER_INIT = "The callback request code offset can't be updated once the SDK is initialized.";
    static final String CALLBACK_OFFSET_NEGATIVE = "The callback request code offset can't be negative.";
    public static final String CLIENT_TOKEN_PROPERTY = "com.facebook.sdk.ClientToken";
    private static final int DEFAULT_CORE_POOL_SIZE = 5;
    private static final int DEFAULT_KEEP_ALIVE = 1;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 128;
    private static final int MAX_REQUEST_CODE_RANGE = 100;
    private static final String PUBLISH_ACTIVITY_PATH = "%s/activities";
    public static final String WEB_DIALOG_THEME = "com.facebook.sdk.WebDialogTheme";
    private static volatile String appClientToken;
    private static Context applicationContext;
    private static volatile String applicationId;
    private static volatile String applicationName;
    private static LockOnGetVariable<File> cacheDir;
    private static volatile Executor executor;
    private static volatile int webDialogTheme;
    private static final String TAG = FacebookSdk.class.getCanonicalName();
    private static final HashSet<LoggingBehavior> loggingBehaviors = new HashSet<>(Arrays.asList(LoggingBehavior.DEVELOPER_ERRORS));
    private static final String FACEBOOK_COM = "facebook.com";
    private static volatile String facebookDomain = FACEBOOK_COM;
    private static AtomicLong onProgressThreshold = new AtomicLong(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH);
    private static volatile boolean isDebugEnabled = false;
    private static boolean isLegacyTokenUpgradeSupported = false;
    private static int callbackRequestCodeOffset = 64206;
    private static final Object LOCK = new Object();
    private static final BlockingQueue<Runnable> DEFAULT_WORK_QUEUE = new LinkedBlockingQueue(10);
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() { // from class: com.facebook.FacebookSdk.1
        private final AtomicInteger counter = new AtomicInteger(0);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "FacebookSdk #" + this.counter.incrementAndGet());
        }
    };
    private static Boolean sdkInitialized = false;

    public interface InitializeCallback {
        void onInitialized();
    }

    public static synchronized void sdkInitialize(Context applicationContext2, int callbackRequestCodeOffset2) {
        sdkInitialize(applicationContext2, callbackRequestCodeOffset2, null);
    }

    public static synchronized void sdkInitialize(Context applicationContext2, int callbackRequestCodeOffset2, InitializeCallback callback) {
        if (sdkInitialized.booleanValue() && callbackRequestCodeOffset2 != callbackRequestCodeOffset) {
            throw new FacebookException(CALLBACK_OFFSET_CHANGED_AFTER_INIT);
        }
        if (callbackRequestCodeOffset2 < 0) {
            throw new FacebookException(CALLBACK_OFFSET_NEGATIVE);
        }
        callbackRequestCodeOffset = callbackRequestCodeOffset2;
        sdkInitialize(applicationContext2, callback);
    }

    public static synchronized void sdkInitialize(Context applicationContext2) {
        sdkInitialize(applicationContext2, (InitializeCallback) null);
    }

    public static synchronized void sdkInitialize(final Context applicationContext2, final InitializeCallback callback) {
        if (sdkInitialized.booleanValue()) {
            if (callback != null) {
                callback.onInitialized();
            }
        } else {
            Validate.notNull(applicationContext2, "applicationContext");
            Validate.hasFacebookActivity(applicationContext2, false);
            Validate.hasInternetPermissions(applicationContext2, false);
            applicationContext = applicationContext2.getApplicationContext();
            loadDefaultsFromMetadata(applicationContext);
            sdkInitialized = true;
            Utility.loadAppSettingsAsync(applicationContext, applicationId);
            NativeProtocol.updateAllAvailableProtocolVersionsAsync();
            BoltsMeasurementEventListener.getInstance(applicationContext);
            cacheDir = new LockOnGetVariable<>((Callable) new Callable<File>() { // from class: com.facebook.FacebookSdk.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public File call() throws Exception {
                    return FacebookSdk.applicationContext.getCacheDir();
                }
            });
            FutureTask<Void> futureTask = new FutureTask<>(new Callable<Void>() { // from class: com.facebook.FacebookSdk.3
                @Override // java.util.concurrent.Callable
                public Void call() throws Exception {
                    AccessTokenManager.getInstance().loadCurrentAccessToken();
                    ProfileManager.getInstance().loadCurrentProfile();
                    if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() == null) {
                        Profile.fetchProfileForCurrentAccessToken();
                    }
                    if (callback != null) {
                        callback.onInitialized();
                    }
                    AppEventsLogger.newLogger(applicationContext2.getApplicationContext()).flush();
                    return null;
                }
            });
            getExecutor().execute(futureTask);
        }
    }

    public static synchronized boolean isInitialized() {
        return sdkInitialized.booleanValue();
    }

    public static Set<LoggingBehavior> getLoggingBehaviors() {
        Set<LoggingBehavior> setUnmodifiableSet;
        synchronized (loggingBehaviors) {
            setUnmodifiableSet = Collections.unmodifiableSet(new HashSet(loggingBehaviors));
        }
        return setUnmodifiableSet;
    }

    public static void addLoggingBehavior(LoggingBehavior behavior) {
        synchronized (loggingBehaviors) {
            loggingBehaviors.add(behavior);
            updateGraphDebugBehavior();
        }
    }

    public static void removeLoggingBehavior(LoggingBehavior behavior) {
        synchronized (loggingBehaviors) {
            loggingBehaviors.remove(behavior);
        }
    }

    public static void clearLoggingBehaviors() {
        synchronized (loggingBehaviors) {
            loggingBehaviors.clear();
        }
    }

    public static boolean isLoggingBehaviorEnabled(LoggingBehavior behavior) {
        boolean z;
        synchronized (loggingBehaviors) {
            z = isDebugEnabled() && loggingBehaviors.contains(behavior);
        }
        return z;
    }

    public static boolean isDebugEnabled() {
        return isDebugEnabled;
    }

    public static void setIsDebugEnabled(boolean enabled) {
        isDebugEnabled = enabled;
    }

    public static boolean isLegacyTokenUpgradeSupported() {
        return isLegacyTokenUpgradeSupported;
    }

    private static void updateGraphDebugBehavior() {
        if (loggingBehaviors.contains(LoggingBehavior.GRAPH_API_DEBUG_INFO) && !loggingBehaviors.contains(LoggingBehavior.GRAPH_API_DEBUG_WARNING)) {
            loggingBehaviors.add(LoggingBehavior.GRAPH_API_DEBUG_WARNING);
        }
    }

    public static void setLegacyTokenUpgradeSupported(boolean supported) {
        isLegacyTokenUpgradeSupported = supported;
    }

    public static Executor getExecutor() {
        synchronized (LOCK) {
            if (executor == null) {
                executor = AsyncTask.THREAD_POOL_EXECUTOR;
            }
        }
        return executor;
    }

    public static void setExecutor(Executor executor2) {
        Validate.notNull(executor2, "executor");
        synchronized (LOCK) {
            executor = executor2;
        }
    }

    public static String getFacebookDomain() {
        return facebookDomain;
    }

    public static void setFacebookDomain(String facebookDomain2) {
        Log.w(TAG, "WARNING: Calling setFacebookDomain from non-DEBUG code.");
        facebookDomain = facebookDomain2;
    }

    public static Context getApplicationContext() {
        Validate.sdkInitialized();
        return applicationContext;
    }

    public static void publishInstallAsync(Context context, final String applicationId2) {
        final Context applicationContext2 = context.getApplicationContext();
        getExecutor().execute(new Runnable() { // from class: com.facebook.FacebookSdk.4
            @Override // java.lang.Runnable
            public void run() {
                FacebookSdk.publishInstallAndWaitForResponse(applicationContext2, applicationId2);
            }
        });
    }

    static GraphResponse publishInstallAndWaitForResponse(Context context, String applicationId2) {
        try {
            if (context == null || applicationId2 == null) {
                throw new IllegalArgumentException("Both context and applicationId must be non-null");
            }
            AttributionIdentifiers identifiers = AttributionIdentifiers.getAttributionIdentifiers(context);
            SharedPreferences preferences = context.getSharedPreferences(ATTRIBUTION_PREFERENCES, 0);
            String pingKey = applicationId2 + "ping";
            String jsonKey = applicationId2 + "json";
            long lastPing = preferences.getLong(pingKey, 0L);
            String lastResponseJSON = preferences.getString(jsonKey, null);
            try {
                JSONObject publishParams = AppEventsLoggerUtility.getJSONObjectForGraphAPICall(AppEventsLoggerUtility.GraphAPIActivityType.MOBILE_INSTALL_EVENT, identifiers, AppEventsLogger.getAnonymousAppDeviceGUID(context), getLimitEventAndDataUsage(context), context);
                String publishUrl = String.format(PUBLISH_ACTIVITY_PATH, applicationId2);
                GraphRequest publishRequest = GraphRequest.newPostRequest(null, publishUrl, publishParams, null);
                if (lastPing != 0) {
                    JSONObject graphObject = null;
                    if (lastResponseJSON != null) {
                        try {
                            graphObject = new JSONObject(lastResponseJSON);
                        } catch (JSONException e) {
                        }
                    }
                    if (graphObject == null) {
                        return GraphResponse.createResponsesFromString(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE, null, new GraphRequestBatch(publishRequest)).get(0);
                    }
                    return new GraphResponse((GraphRequest) null, (HttpURLConnection) null, (String) null, graphObject);
                }
                GraphResponse publishResponse = publishRequest.executeAndWait();
                SharedPreferences.Editor editor = preferences.edit();
                long lastPing2 = System.currentTimeMillis();
                editor.putLong(pingKey, lastPing2);
                if (publishResponse.getJSONObject() != null) {
                    editor.putString(jsonKey, publishResponse.getJSONObject().toString());
                }
                editor.apply();
                return publishResponse;
            } catch (JSONException e2) {
                throw new FacebookException("An error occurred while publishing install.", e2);
            }
        } catch (Exception e3) {
            Utility.logd("Facebook-publish", e3);
            return new GraphResponse(null, null, new FacebookRequestError((HttpURLConnection) null, e3));
        }
    }

    public static String getSdkVersion() {
        return FacebookSdkVersion.BUILD;
    }

    public static boolean getLimitEventAndDataUsage(Context context) {
        Validate.sdkInitialized();
        SharedPreferences preferences = context.getSharedPreferences(AppEventsLogger.APP_EVENT_PREFERENCES, 0);
        return preferences.getBoolean("limitEventUsage", false);
    }

    public static void setLimitEventAndDataUsage(Context context, boolean limitEventUsage) {
        context.getSharedPreferences(AppEventsLogger.APP_EVENT_PREFERENCES, 0).edit().putBoolean("limitEventUsage", limitEventUsage).apply();
    }

    public static long getOnProgressThreshold() {
        Validate.sdkInitialized();
        return onProgressThreshold.get();
    }

    public static void setOnProgressThreshold(long threshold) {
        onProgressThreshold.set(threshold);
    }

    static void loadDefaultsFromMetadata(Context context) {
        if (context != null) {
            try {
                ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                if (ai != null && ai.metaData != null) {
                    if (applicationId == null) {
                        Object appId = ai.metaData.get(APPLICATION_ID_PROPERTY);
                        if (appId instanceof String) {
                            String appIdString = (String) appId;
                            if (appIdString.toLowerCase(Locale.ROOT).startsWith("fb")) {
                                applicationId = appIdString.substring(2);
                            } else {
                                applicationId = appIdString;
                            }
                        } else if (appId instanceof Integer) {
                            throw new FacebookException("App Ids cannot be directly placed in the manifest.They must be prefixed by 'fb' or be placed in the string resource file.");
                        }
                    }
                    if (applicationName == null) {
                        applicationName = ai.metaData.getString(APPLICATION_NAME_PROPERTY);
                    }
                    if (appClientToken == null) {
                        appClientToken = ai.metaData.getString(CLIENT_TOKEN_PROPERTY);
                    }
                    if (webDialogTheme == 0) {
                        setWebDialogTheme(ai.metaData.getInt(WEB_DIALOG_THEME));
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
    }

    public static String getApplicationSignature(Context context) {
        PackageManager packageManager;
        Validate.sdkInitialized();
        if (context == null || (packageManager = context.getPackageManager()) == null) {
            return null;
        }
        String packageName = context.getPackageName();
        try {
            PackageInfo pInfo = packageManager.getPackageInfo(packageName, 64);
            Signature[] signatures = pInfo.signatures;
            if (signatures == null || signatures.length == 0) {
                return null;
            }
            try {
                MessageDigest md = MessageDigest.getInstance(CommonUtils.SHA1_INSTANCE);
                md.update(pInfo.signatures[0].toByteArray());
                return Base64.encodeToString(md.digest(), 9);
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        } catch (PackageManager.NameNotFoundException e2) {
            return null;
        }
    }

    public static String getApplicationId() {
        Validate.sdkInitialized();
        return applicationId;
    }

    public static void setApplicationId(String applicationId2) {
        applicationId = applicationId2;
    }

    public static String getApplicationName() {
        Validate.sdkInitialized();
        return applicationName;
    }

    public static void setApplicationName(String applicationName2) {
        applicationName = applicationName2;
    }

    public static String getClientToken() {
        Validate.sdkInitialized();
        return appClientToken;
    }

    public static void setClientToken(String clientToken) {
        appClientToken = clientToken;
    }

    public static int getWebDialogTheme() {
        Validate.sdkInitialized();
        return webDialogTheme;
    }

    public static void setWebDialogTheme(int theme) {
        if (theme == 0) {
            theme = 16973840;
        }
        webDialogTheme = theme;
    }

    public static File getCacheDir() {
        Validate.sdkInitialized();
        return cacheDir.getValue();
    }

    public static void setCacheDir(File cacheDir2) {
        cacheDir = new LockOnGetVariable<>(cacheDir2);
    }

    public static int getCallbackRequestCodeOffset() {
        Validate.sdkInitialized();
        return callbackRequestCodeOffset;
    }

    public static boolean isFacebookRequestCode(int requestCode) {
        return requestCode >= callbackRequestCodeOffset && requestCode < callbackRequestCodeOffset + 100;
    }
}
