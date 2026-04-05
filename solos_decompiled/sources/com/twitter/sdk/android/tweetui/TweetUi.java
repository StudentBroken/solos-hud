package com.twitter.sdk.android.tweetui;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.scribe.DefaultScribeClient;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;
import com.twitter.sdk.android.core.internal.scribe.ScribeItem;
import com.twitter.sdk.android.tweetui.internal.GuestSessionProvider;
import com.twitter.sdk.android.tweetui.internal.UserSessionProvider;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes9.dex */
@DependsOn({TwitterCore.class})
public class TweetUi extends Kit<Boolean> {
    private static final String KIT_SCRIBE_NAME = "TweetUi";
    static final String LOGTAG = "TweetUi";
    static final String NOT_STARTED_ERROR = "Must start TweetUi Kit in Fabric.with().";
    String advertisingId;
    private final AtomicReference<Gson> gsonRef = new AtomicReference<>();
    private TweetUiAuthRequestQueue guestAuthQueue;
    List<SessionManager<? extends Session>> guestSessionManagers;
    GuestSessionProvider guestSessionProvider;
    private Picasso imageLoader;
    DefaultScribeClient scribeClient;
    private TweetRepository tweetRepository;
    private TweetUiAuthRequestQueue userAuthQueue;
    List<SessionManager<? extends Session>> userSessionManagers;
    UserSessionProvider userSessionProvider;

    public static TweetUi getInstance() {
        checkInitialized();
        return (TweetUi) Fabric.getKit(TweetUi.class);
    }

    @Override // io.fabric.sdk.android.Kit
    public String getIdentifier() {
        return "com.twitter.sdk.android:tweet-ui";
    }

    @Override // io.fabric.sdk.android.Kit
    public String getVersion() {
        return "1.9.0.98";
    }

    @Override // io.fabric.sdk.android.Kit
    protected boolean onPreExecute() {
        super.onPreExecute();
        TwitterCore twitterCore = TwitterCore.getInstance();
        this.userSessionManagers = new ArrayList(1);
        this.userSessionManagers.add(twitterCore.getSessionManager());
        this.userSessionProvider = new UserSessionProvider(this.userSessionManagers);
        this.userAuthQueue = new TweetUiAuthRequestQueue(twitterCore, this.userSessionProvider);
        this.guestSessionManagers = new ArrayList(2);
        this.guestSessionManagers.add(twitterCore.getSessionManager());
        this.guestSessionManagers.add(twitterCore.getAppSessionManager());
        this.guestSessionProvider = new GuestSessionProvider(twitterCore, this.guestSessionManagers);
        this.guestAuthQueue = new TweetUiAuthRequestQueue(twitterCore, this.guestSessionProvider);
        this.tweetRepository = new TweetRepository(getFabric().getMainHandler(), this.userAuthQueue, this.guestAuthQueue);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.fabric.sdk.android.Kit
    public Boolean doInBackground() {
        this.imageLoader = Picasso.with(getContext());
        this.userAuthQueue.sessionRestored(this.userSessionProvider.getActiveSession());
        this.guestAuthQueue.sessionRestored(this.guestSessionProvider.getActiveSession());
        initGson();
        setUpScribeClient();
        this.advertisingId = getIdManager().getAdvertisingId();
        return true;
    }

    private static void checkInitialized() {
        if (Fabric.getKit(TweetUi.class) == null) {
            throw new IllegalStateException(NOT_STARTED_ERROR);
        }
    }

    private void setUpScribeClient() {
        this.scribeClient = new DefaultScribeClient(this, "TweetUi", this.gsonRef.get(), this.guestSessionManagers, getIdManager());
    }

    void scribe(EventNamespace... namespaces) {
        if (this.scribeClient != null) {
            for (EventNamespace ns : namespaces) {
                this.scribeClient.scribe(ns);
            }
        }
    }

    void scribe(EventNamespace ns, List<ScribeItem> items) {
        if (this.scribeClient != null) {
            this.scribeClient.scribe(ns, items);
        }
    }

    void initGson() {
        if (this.gsonRef.get() == null) {
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            this.gsonRef.compareAndSet(null, gson);
        }
    }

    TweetRepository getTweetRepository() {
        return this.tweetRepository;
    }

    TweetUiAuthRequestQueue getGuestAuthQueue() {
        return this.guestAuthQueue;
    }

    void setTweetRepository(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    Picasso getImageLoader() {
        return this.imageLoader;
    }

    void setImageLoader(Picasso imageLoader) {
        this.imageLoader = imageLoader;
    }

    void clearAppSession(long sessionId) {
        TwitterCore.getInstance().getAppSessionManager().clearSession(sessionId);
    }
}
