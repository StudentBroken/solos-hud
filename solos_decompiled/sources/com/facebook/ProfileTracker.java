package com.facebook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.facebook.internal.Validate;

/* JADX INFO: loaded from: classes41.dex */
public abstract class ProfileTracker {
    private final LocalBroadcastManager broadcastManager;
    private boolean isTracking = false;
    private final BroadcastReceiver receiver;

    protected abstract void onCurrentProfileChanged(Profile profile, Profile profile2);

    public ProfileTracker() {
        Validate.sdkInitialized();
        this.receiver = new ProfileBroadcastReceiver();
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

    private class ProfileBroadcastReceiver extends BroadcastReceiver {
        private ProfileBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("com.facebook.sdk.ACTION_CURRENT_PROFILE_CHANGED".equals(intent.getAction())) {
                Profile oldProfile = (Profile) intent.getParcelableExtra("com.facebook.sdk.EXTRA_OLD_PROFILE");
                Profile newProfile = (Profile) intent.getParcelableExtra("com.facebook.sdk.EXTRA_NEW_PROFILE");
                ProfileTracker.this.onCurrentProfileChanged(oldProfile, newProfile);
            }
        }
    }

    private void addBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.facebook.sdk.ACTION_CURRENT_PROFILE_CHANGED");
        this.broadcastManager.registerReceiver(this.receiver, filter);
    }
}
