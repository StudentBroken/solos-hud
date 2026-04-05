package com.twitter.sdk.android.core.identity;

import android.app.Activity;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes62.dex */
class AuthState {
    final AtomicReference<AuthHandler> authHandlerRef = new AtomicReference<>(null);

    AuthState() {
    }

    public boolean beginAuthorize(Activity activity, AuthHandler authHandler) {
        boolean result = false;
        if (isAuthorizeInProgress()) {
            Fabric.getLogger().w(TwitterCore.TAG, "Authorize already in progress");
        } else if (authHandler.authorize(activity) && !(result = this.authHandlerRef.compareAndSet(null, authHandler))) {
            Fabric.getLogger().w(TwitterCore.TAG, "Failed to update authHandler, authorize already in progress.");
        }
        return result;
    }

    public void endAuthorize() {
        this.authHandlerRef.set(null);
    }

    public boolean isAuthorizeInProgress() {
        return this.authHandlerRef.get() != null;
    }

    public AuthHandler getAuthHandler() {
        return this.authHandlerRef.get();
    }
}
