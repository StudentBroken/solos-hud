package com.twitter.sdk.android.core.identity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.digits.sdk.vcard.VCardConfig;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.scribe.DefaultScribeClient;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;
import com.twitter.sdk.android.core.internal.scribe.TwitterCoreScribeClientHolder;
import io.fabric.sdk.android.Fabric;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterAuthClient {
    private static final String SCRIBE_ACTION = "impression";
    private static final String SCRIBE_CLIENT = "android";
    private static final String SCRIBE_COMPONENT = "";
    private static final String SCRIBE_ELEMENT = "";
    private static final String SCRIBE_LOGIN_PAGE = "login";
    private static final String SCRIBE_SECTION = "";
    private static final String SCRIBE_SHARE_EMAIL_PAGE = "shareemail";
    private final TwitterAuthConfig authConfig;
    final AuthState authState;
    private final Context context;
    final SessionManager<TwitterSession> sessionManager;

    private static class AuthStateLazyHolder {
        private static final AuthState INSTANCE = new AuthState();

        private AuthStateLazyHolder() {
        }
    }

    public int getRequestCode() {
        return this.authConfig.getRequestCode();
    }

    public TwitterAuthClient() {
        this(TwitterCore.getInstance().getContext(), TwitterCore.getInstance().getAuthConfig(), TwitterCore.getInstance().getSessionManager(), AuthStateLazyHolder.INSTANCE);
    }

    TwitterAuthClient(Context context, TwitterAuthConfig authConfig, SessionManager<TwitterSession> sessionManager, AuthState authState) {
        this.authState = authState;
        this.context = context;
        this.authConfig = authConfig;
        this.sessionManager = sessionManager;
    }

    public void authorize(Activity activity, Callback<TwitterSession> callback) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity must not be null.");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null.");
        }
        if (activity.isFinishing()) {
            Fabric.getLogger().e(TwitterCore.TAG, "Cannot authorize, activity is finishing.", null);
        } else {
            handleAuthorize(activity, callback);
        }
    }

    private void handleAuthorize(Activity activity, Callback<TwitterSession> callback) {
        scribeAuthorizeImpression();
        CallbackWrapper callbackWrapper = new CallbackWrapper(this.sessionManager, callback);
        if (!authorizeUsingSSO(activity, callbackWrapper) && !authorizeUsingOAuth(activity, callbackWrapper)) {
            callbackWrapper.failure(new TwitterAuthException("Authorize failed."));
        }
    }

    private boolean authorizeUsingSSO(Activity activity, CallbackWrapper callbackWrapper) {
        if (!SSOAuthHandler.isAvailable(activity)) {
            return false;
        }
        Fabric.getLogger().d(TwitterCore.TAG, "Using SSO");
        return this.authState.beginAuthorize(activity, new SSOAuthHandler(this.authConfig, callbackWrapper, this.authConfig.getRequestCode()));
    }

    private boolean authorizeUsingOAuth(Activity activity, CallbackWrapper callbackWrapper) {
        Fabric.getLogger().d(TwitterCore.TAG, "Using OAuth");
        return this.authState.beginAuthorize(activity, new OAuthHandler(this.authConfig, callbackWrapper, this.authConfig.getRequestCode()));
    }

    private void scribeAuthorizeImpression() {
        DefaultScribeClient scribeClient = getScribeClient();
        if (scribeClient != null) {
            EventNamespace ns = new EventNamespace.Builder().setClient("android").setPage("login").setSection("").setComponent("").setElement("").setAction(SCRIBE_ACTION).builder();
            scribeClient.scribe(ns);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fabric.getLogger().d(TwitterCore.TAG, "onActivityResult called with " + requestCode + " " + resultCode);
        if (!this.authState.isAuthorizeInProgress()) {
            Fabric.getLogger().e(TwitterCore.TAG, "Authorize not in progress", null);
            return;
        }
        AuthHandler authHandler = this.authState.getAuthHandler();
        if (authHandler != null && authHandler.handleOnActivityResult(requestCode, resultCode, data)) {
            this.authState.endAuthorize();
        }
    }

    public void requestEmail(TwitterSession session, Callback<String> callback) {
        if (session == null) {
            throw new IllegalArgumentException("Session must not be null.");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null.");
        }
        scribeRequestEmail();
        this.context.startActivity(newShareEmailIntent(session, callback));
    }

    protected DefaultScribeClient getScribeClient() {
        return TwitterCoreScribeClientHolder.getScribeClient();
    }

    private void scribeRequestEmail() {
        DefaultScribeClient scribeClient = getScribeClient();
        if (scribeClient != null) {
            EventNamespace ns = new EventNamespace.Builder().setClient("android").setPage(SCRIBE_SHARE_EMAIL_PAGE).setSection("").setComponent("").setElement("").setAction(SCRIBE_ACTION).builder();
            scribeClient.scribe(ns);
        }
    }

    Intent newShareEmailIntent(TwitterSession session, Callback<String> callback) {
        return new Intent(this.context, (Class<?>) ShareEmailActivity.class).setFlags(VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES).putExtra("session_id", session.getId()).putExtra("result_receiver", new ShareEmailResultReceiver(callback));
    }

    static class CallbackWrapper extends Callback<TwitterSession> {
        private final Callback<TwitterSession> callback;
        private final SessionManager<TwitterSession> sessionManager;

        public CallbackWrapper(SessionManager<TwitterSession> sessionManager, Callback<TwitterSession> callback) {
            this.sessionManager = sessionManager;
            this.callback = callback;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<TwitterSession> result) {
            Fabric.getLogger().d(TwitterCore.TAG, "Authorization completed successfully");
            this.sessionManager.setActiveSession(result.data);
            this.callback.success(result);
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void failure(TwitterException exception) {
            Fabric.getLogger().e(TwitterCore.TAG, "Authorization completed with an error", exception);
            this.callback.failure(exception);
        }
    }
}
