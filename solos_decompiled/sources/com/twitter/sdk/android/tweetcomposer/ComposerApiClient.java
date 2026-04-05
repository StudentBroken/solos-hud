package com.twitter.sdk.android.tweetcomposer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.sdk.android.core.AuthenticatedClient;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.SafeListAdapter;
import com.twitter.sdk.android.core.models.SafeMapAdapter;
import com.twitter.sdk.android.tweetcomposer.internal.CardService;
import java.util.concurrent.ExecutorService;
import javax.net.ssl.SSLSocketFactory;
import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.converter.GsonConverter;

/* JADX INFO: loaded from: classes29.dex */
class ComposerApiClient extends TwitterApiClient {
    private static final String CARDS_ENDPOINT = "https://caps.twitter.com";
    final RestAdapter cardsAdapter;

    ComposerApiClient(TwitterAuthConfig authConfig, Session session, SSLSocketFactory sslSocketFactory, ExecutorService executorService) {
        super(session);
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new SafeListAdapter()).registerTypeAdapterFactory(new SafeMapAdapter()).create();
        this.cardsAdapter = new RestAdapter.Builder().setClient(new AuthenticatedClient(authConfig, session, sslSocketFactory)).setEndpoint(CARDS_ENDPOINT).setConverter(new GsonConverter(gson)).setExecutors(executorService, new MainThreadExecutor()).build();
    }

    ComposerApiClient(TwitterSession session) {
        this(TwitterCore.getInstance().getAuthConfig(), session, TwitterCore.getInstance().getSSLSocketFactory(), TwitterCore.getInstance().getFabric().getExecutorService());
    }

    StatusesService getComposerStatusesService() {
        return (StatusesService) getService(StatusesService.class);
    }

    CardService getCardService() {
        return (CardService) getAdapterService(this.cardsAdapter, CardService.class);
    }
}
