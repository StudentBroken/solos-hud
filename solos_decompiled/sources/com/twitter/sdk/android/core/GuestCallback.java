package com.twitter.sdk.android.core;

import io.fabric.sdk.android.Fabric;

/* JADX INFO: loaded from: classes62.dex */
public class GuestCallback<T> extends Callback<T> {
    protected SessionManager<AppSession> appSessionManager;
    protected Callback<T> cb;

    public GuestCallback(Callback<T> cb) {
        this(TwitterCore.getInstance(), cb);
    }

    GuestCallback(TwitterCore twitterCore, Callback<T> cb) {
        this(twitterCore.getAppSessionManager(), cb);
    }

    GuestCallback(SessionManager<AppSession> sessionManager, Callback<T> cb) {
        this.appSessionManager = sessionManager;
        this.cb = cb;
    }

    @Override // com.twitter.sdk.android.core.Callback
    public void success(Result<T> result) {
        if (this.cb != null) {
            this.cb.success(result);
        }
    }

    @Override // com.twitter.sdk.android.core.Callback
    public void failure(TwitterException exception) {
        if (exception instanceof TwitterApiException) {
            TwitterApiException apiException = (TwitterApiException) exception;
            int errorCode = apiException.getErrorCode();
            Fabric.getLogger().e(TwitterCore.TAG, "API call failure.", apiException);
            if ((errorCode == 89 || errorCode == 239) && this.appSessionManager != null) {
                this.appSessionManager.clearSession(0L);
            }
        }
        if (this.cb != null) {
            this.cb.failure(exception);
        }
    }
}
