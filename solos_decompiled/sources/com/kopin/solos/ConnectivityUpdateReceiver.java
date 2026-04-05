package com.kopin.solos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes37.dex */
public class ConnectivityUpdateReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        boolean connected = Utility.isNetworkAvailable(context);
        Sync.setConnected(connected);
        if (connected) {
            Log.i("ConnUpdateReceiver", "network connection is ON ******* " + connected);
            Sync.sync(Prefs.isForceSync(), true);
        }
    }
}
