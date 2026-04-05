package com.twitter.sdk.android.core.identity;

import android.app.Activity;
import android.content.Intent;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

/* JADX INFO: loaded from: classes62.dex */
public abstract class AuthHandler {
    static final String EXTRA_AUTH_ERROR = "auth_error";
    static final String EXTRA_SCREEN_NAME = "screen_name";
    static final String EXTRA_TOKEN = "tk";
    static final String EXTRA_TOKEN_SECRET = "ts";
    static final String EXTRA_USER_ID = "user_id";
    static final int RESULT_CODE_ERROR = 1;
    private final Callback<TwitterSession> callback;
    private final TwitterAuthConfig config;
    protected final int requestCode;

    public abstract boolean authorize(Activity activity);

    AuthHandler(TwitterAuthConfig authConfig, Callback<TwitterSession> callback, int requestCode) {
        this.config = authConfig;
        this.callback = callback;
        this.requestCode = requestCode;
    }

    TwitterAuthConfig getAuthConfig() {
        return this.config;
    }

    Callback<TwitterSession> getCallback() {
        return this.callback;
    }

    public boolean handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.requestCode != requestCode) {
            return false;
        }
        Callback<TwitterSession> callback = getCallback();
        if (callback != null) {
            if (resultCode == -1) {
                String token = data.getStringExtra(EXTRA_TOKEN);
                String tokenSecret = data.getStringExtra(EXTRA_TOKEN_SECRET);
                String screenName = data.getStringExtra(EXTRA_SCREEN_NAME);
                long userId = data.getLongExtra("user_id", 0L);
                callback.success(new TwitterSession(new TwitterAuthToken(token, tokenSecret), userId, screenName), null);
            } else if (data != null && data.hasExtra(EXTRA_AUTH_ERROR)) {
                callback.failure((TwitterAuthException) data.getSerializableExtra(EXTRA_AUTH_ERROR));
            } else {
                callback.failure(new TwitterAuthException("Authorize failed."));
            }
        }
        return true;
    }
}
