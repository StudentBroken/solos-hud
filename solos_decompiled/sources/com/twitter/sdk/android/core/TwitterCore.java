package com.twitter.sdk.android.core;

import android.app.Activity;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.internal.MigrationHelper;
import com.twitter.sdk.android.core.internal.SessionMonitor;
import com.twitter.sdk.android.core.internal.TwitterApi;
import com.twitter.sdk.android.core.internal.TwitterSessionVerifier;
import com.twitter.sdk.android.core.internal.oauth.OAuth2Service;
import com.twitter.sdk.android.core.internal.scribe.TwitterCoreScribeClientHolder;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.network.NetworkUtils;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.SSLSocketFactory;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterCore extends Kit<Boolean> {
    static final String PREF_KEY_ACTIVE_APP_SESSION = "active_appsession";
    static final String PREF_KEY_ACTIVE_TWITTER_SESSION = "active_twittersession";
    static final String PREF_KEY_APP_SESSION = "appsession";
    static final String PREF_KEY_TWITTER_SESSION = "twittersession";
    static final String SESSION_PREF_FILE_NAME = "session_store";
    public static final String TAG = "Twitter";
    private final ConcurrentHashMap<Session, TwitterApiClient> apiClients;
    SessionManager<AppSession> appSessionManager;
    private final TwitterAuthConfig authConfig;
    SessionMonitor<TwitterSession> sessionMonitor;
    private volatile SSLSocketFactory sslSocketFactory;
    SessionManager<TwitterSession> twitterSessionManager;

    public TwitterCore(TwitterAuthConfig authConfig) {
        this.authConfig = authConfig;
        this.apiClients = new ConcurrentHashMap<>();
    }

    TwitterCore(TwitterAuthConfig authConfig, ConcurrentHashMap<Session, TwitterApiClient> apiClients) {
        this.authConfig = authConfig;
        this.apiClients = apiClients;
    }

    public static TwitterCore getInstance() {
        checkInitialized();
        return (TwitterCore) Fabric.getKit(TwitterCore.class);
    }

    @Override // io.fabric.sdk.android.Kit
    public String getVersion() {
        return "1.6.3.98";
    }

    public TwitterAuthConfig getAuthConfig() {
        return this.authConfig;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        checkInitialized();
        if (this.sslSocketFactory == null) {
            createSSLSocketFactory();
        }
        return this.sslSocketFactory;
    }

    private synchronized void createSSLSocketFactory() {
        if (this.sslSocketFactory == null) {
            try {
                this.sslSocketFactory = NetworkUtils.getSSLSocketFactory(new TwitterPinningInfoProvider(getContext()));
                Fabric.getLogger().d(TAG, "Custom SSL pinning enabled");
            } catch (Exception e) {
                Fabric.getLogger().e(TAG, "Exception setting up custom SSL pinning", e);
            }
        }
    }

    @Override // io.fabric.sdk.android.Kit
    protected boolean onPreExecute() {
        MigrationHelper migrationHelper = new MigrationHelper();
        migrationHelper.migrateSessionStore(getContext(), getIdentifier(), getIdentifier() + ":" + SESSION_PREF_FILE_NAME + ".xml");
        this.twitterSessionManager = new PersistedSessionManager(new PreferenceStoreImpl(getContext(), SESSION_PREF_FILE_NAME), new TwitterSession.Serializer(), PREF_KEY_ACTIVE_TWITTER_SESSION, PREF_KEY_TWITTER_SESSION);
        this.sessionMonitor = new SessionMonitor<>(this.twitterSessionManager, getFabric().getExecutorService(), new TwitterSessionVerifier());
        this.appSessionManager = new PersistedSessionManager(new PreferenceStoreImpl(getContext(), SESSION_PREF_FILE_NAME), new AppSession.Serializer(), PREF_KEY_ACTIVE_APP_SESSION, PREF_KEY_APP_SESSION);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.fabric.sdk.android.Kit
    public Boolean doInBackground() {
        this.twitterSessionManager.getActiveSession();
        this.appSessionManager.getActiveSession();
        getSSLSocketFactory();
        initializeScribeClient();
        this.sessionMonitor.monitorActivityLifecycle(getFabric().getActivityLifecycleManager());
        return true;
    }

    @Override // io.fabric.sdk.android.Kit
    public String getIdentifier() {
        return "com.twitter.sdk.android:twitter-core";
    }

    private static void checkInitialized() {
        if (Fabric.getKit(TwitterCore.class) == null) {
            throw new IllegalStateException("Must start Twitter Kit with Fabric.with() first");
        }
    }

    private void initializeScribeClient() {
        List<SessionManager<? extends Session>> sessionManagers = new ArrayList<>();
        sessionManagers.add(this.twitterSessionManager);
        sessionManagers.add(this.appSessionManager);
        TwitterCoreScribeClientHolder.initialize(this, sessionManagers, getIdManager());
    }

    public void logIn(Activity activity, Callback<TwitterSession> callback) {
        checkInitialized();
        new TwitterAuthClient().authorize(activity, callback);
    }

    public void logInGuest(Callback<AppSession> callback) {
        checkInitialized();
        OAuth2Service service = new OAuth2Service(this, null, new TwitterApi());
        new GuestAuthClient(service).authorize(this.appSessionManager, callback);
    }

    public void logOut() {
        checkInitialized();
        SessionManager<TwitterSession> sessionManager = getSessionManager();
        if (sessionManager != null) {
            sessionManager.clearActiveSession();
        }
    }

    public SessionManager<TwitterSession> getSessionManager() {
        checkInitialized();
        return this.twitterSessionManager;
    }

    public SessionManager<AppSession> getAppSessionManager() {
        checkInitialized();
        return this.appSessionManager;
    }

    private Session getActiveSession() {
        Session session = this.twitterSessionManager.getActiveSession();
        if (session == null) {
            return this.appSessionManager.getActiveSession();
        }
        return session;
    }

    public TwitterApiClient getApiClient() {
        checkInitialized();
        Session session = getActiveSession();
        if (session == null) {
            throw new IllegalStateException("Must have valid session. Did you authenticate with Twitter?");
        }
        return getApiClient(session);
    }

    public TwitterApiClient getApiClient(Session session) {
        checkInitialized();
        if (!this.apiClients.containsKey(session)) {
            this.apiClients.putIfAbsent(session, new TwitterApiClient(session));
        }
        return this.apiClients.get(session);
    }
}
