package com.google.tagmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes49.dex */
public final class InstallReferrerReceiver extends BroadcastReceiver {
    static final String INSTALL_ACTION = "com.android.vending.INSTALL_REFERRER";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        if (INSTALL_ACTION.equals(intent.getAction()) && referrer != null) {
            InstallReferrerUtil.cacheInstallReferrer(referrer);
            Intent serviceIntent = new Intent(context, (Class<?>) InstallReferrerService.class);
            serviceIntent.putExtra("referrer", referrer);
            context.startService(serviceIntent);
        }
    }
}
