package com.digits.sdk.android;

import android.annotation.TargetApi;
import com.digits.sdk.android.DigitsAuthConfig;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.PersistedSessionManager;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.MigrationHelper;
import com.twitter.sdk.android.core.internal.SessionMonitor;
import com.twitter.sdk.android.core.internal.scribe.DefaultScribeClient;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes18.dex */
@DependsOn({TwitterCore.class})
public class Digits extends Kit<Void> {
    private static final String KIT_SCRIBE_NAME = "Digits";
    static final String PREF_KEY_ACTIVE_SESSION = "active_session";
    static final String PREF_KEY_SESSION = "session";
    static final String SESSION_PREF_FILE_NAME = "session_store";
    public static final String TAG = "Digits";
    private ActivityClassManager activityClassManager;
    private volatile ContactsClient contactsClient;
    private volatile DigitsClient digitsClient;
    private DigitsSessionVerifier digitsSessionVerifier;
    private DigitsScribeClient scribeClient = new DigitsScribeClientImpl(null);
    private SessionManager<DigitsSession> sessionManager;
    private int themeResId;
    private SessionMonitor<DigitsSession> userSessionMonitor;

    public static Digits getInstance() {
        return (Digits) Fabric.getKit(Digits.class);
    }

    @Deprecated
    public static void authenticate(AuthCallback callback) {
        authenticate(callback, 0);
    }

    @Deprecated
    public static void authenticate(AuthCallback callback, String phoneNumber) {
        DigitsAuthConfig.Builder digitsAuthConfigBuilder = new DigitsAuthConfig.Builder().withAuthCallBack(callback).withThemeResId(0).withPhoneNumber(phoneNumber);
        authenticate(digitsAuthConfigBuilder.build());
    }

    @Deprecated
    public static void authenticate(AuthCallback callback, int themeResId) {
        DigitsAuthConfig.Builder digitsAuthConfigBuilder = new DigitsAuthConfig.Builder().withAuthCallBack(callback).withThemeResId(themeResId);
        authenticate(digitsAuthConfigBuilder.build());
    }

    @Deprecated
    public static void authenticate(AuthCallback callback, int themeResId, String phoneNumber, boolean emailCollection) {
        DigitsAuthConfig.Builder digitsAuthConfigBuilder = new DigitsAuthConfig.Builder().withAuthCallBack(callback).withThemeResId(themeResId).withPhoneNumber(phoneNumber).withEmailCollection(emailCollection);
        authenticate(digitsAuthConfigBuilder.build());
    }

    public static void authenticate(DigitsAuthConfig digitsAuthConfig) {
        getInstance().setTheme(digitsAuthConfig.themeResId);
        getInstance().getDigitsClient().startSignUp(digitsAuthConfig);
    }

    public static SessionManager<DigitsSession> getSessionManager() {
        return getInstance().sessionManager;
    }

    public void addSessionListener(SessionListener sessionListener) {
        if (sessionListener == null) {
            throw new NullPointerException("sessionListener must not be null");
        }
        this.digitsSessionVerifier.addSessionListener(sessionListener);
    }

    public void removeSessionListener(SessionListener sessionListener) {
        if (sessionListener == null) {
            throw new NullPointerException("sessionListener must not be null");
        }
        this.digitsSessionVerifier.removeSessionListener(sessionListener);
    }

    @Override // io.fabric.sdk.android.Kit
    public String getVersion() {
        return "1.9.3.98";
    }

    @Override // io.fabric.sdk.android.Kit
    protected boolean onPreExecute() {
        MigrationHelper migrationHelper = new MigrationHelper();
        migrationHelper.migrateSessionStore(getContext(), getIdentifier(), getIdentifier() + ":" + SESSION_PREF_FILE_NAME + ".xml");
        this.sessionManager = new PersistedSessionManager(new PreferenceStoreImpl(getContext(), SESSION_PREF_FILE_NAME), new DigitsSession.Serializer(), PREF_KEY_ACTIVE_SESSION, "session");
        this.digitsSessionVerifier = new DigitsSessionVerifier();
        return super.onPreExecute();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.fabric.sdk.android.Kit
    public Void doInBackground() {
        this.sessionManager.getActiveSession();
        this.scribeClient = setUpScribing();
        createDigitsClient();
        createContactsClient();
        this.userSessionMonitor = new SessionMonitor<>(getSessionManager(), getExecutorService(), this.digitsSessionVerifier);
        this.userSessionMonitor.monitorActivityLifecycle(getFabric().getActivityLifecycleManager());
        return null;
    }

    @TargetApi(21)
    int getTheme() {
        return this.themeResId != 0 ? this.themeResId : R.style.Digits_default;
    }

    protected void setTheme(int themeResId) {
        this.themeResId = themeResId;
        createActivityClassManager();
    }

    @Override // io.fabric.sdk.android.Kit
    public String getIdentifier() {
        return "com.digits.sdk.android:digits";
    }

    DigitsClient getDigitsClient() {
        if (this.digitsClient == null) {
            createDigitsClient();
        }
        return this.digitsClient;
    }

    protected DigitsScribeClient getScribeClient() {
        return this.scribeClient;
    }

    private synchronized void createDigitsClient() {
        if (this.digitsClient == null) {
            this.digitsClient = new DigitsClient();
        }
    }

    public ContactsClient getContactsClient() {
        if (this.contactsClient == null) {
            createContactsClient();
        }
        return this.contactsClient;
    }

    private synchronized void createContactsClient() {
        if (this.contactsClient == null) {
            this.contactsClient = new ContactsClient();
        }
    }

    protected ExecutorService getExecutorService() {
        return getFabric().getExecutorService();
    }

    private DigitsScribeClient setUpScribing() {
        List<SessionManager<? extends Session>> sessionManagers = new ArrayList<>();
        sessionManagers.add(this.sessionManager);
        return new DigitsScribeClientImpl(new DefaultScribeClient(this, "Digits", sessionManagers, getIdManager()));
    }

    protected ActivityClassManager getActivityClassManager() {
        if (this.activityClassManager == null) {
            createActivityClassManager();
        }
        return this.activityClassManager;
    }

    protected void createActivityClassManager() {
        ActivityClassManagerFactory factory = new ActivityClassManagerFactory();
        this.activityClassManager = factory.createActivityClassManager(getContext(), this.themeResId);
    }

    public TwitterAuthConfig getAuthConfig() {
        return TwitterCore.getInstance().getAuthConfig();
    }
}
