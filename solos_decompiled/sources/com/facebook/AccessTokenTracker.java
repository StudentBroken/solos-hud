package com.facebook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.facebook.internal.Validate;

/* JADX INFO: loaded from: classes41.dex */
public abstract class AccessTokenTracker {
    private final LocalBroadcastManager broadcastManager;
    private boolean isTracking = false;
    private final BroadcastReceiver receiver;

    protected abstract void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2);

    public AccessTokenTracker() {
        Validate.sdkInitialized();
        this.receiver = new CurrentAccessTokenBroadcastReceiver();
        this.broadcastManager = LocalBroadcastManager.getInstance(FacebookSdk.getApplicationContext());
        startTracking();
    }

    public void startTracking() {
        if (!this.isTracking) {
            addBroadcastReceiver();
            this.isTracking = true;
        }
    }

    public void stopTracking() {
        if (this.isTracking) {
            this.broadcastManager.unregisterReceiver(this.receiver);
            this.isTracking = false;
        }
    }

    public boolean isTracking() {
        return this.isTracking;
    }

    private class CurrentAccessTokenBroadcastReceiver extends BroadcastReceiver {
        private CurrentAccessTokenBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("com.facebook.sdk.ACTION_CURRENT_ACCESS_TOKEN_CHANGED".equals(intent.getAction())) {
                AccessToken oldAccessToken = (AccessToken) intent.getParcelableExtra("com.facebook.sdk.EXTRA_OLD_ACCESS_TOKEN");
                AccessToken newAccessToken = (AccessToken) intent.getParcelableExtra("com.facebook.sdk.EXTRA_NEW_ACCESS_TOKEN");
                AccessTokenTracker.this.onCurrentAccessTokenChanged(oldAccessToken, newAccessToken);
            }
        }
    }

    private void addBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.facebook.sdk.ACTION_CURRENT_ACCESS_TOKEN_CHANGED");
        this.broadcastManager.registerReceiver(this.receiver, filter);
    }
}
