package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/* JADX INFO: loaded from: classes56.dex */
public final class zzh implements ServiceConnection {
    private final Intent zzcnj;
    private final ScheduledExecutorService zzcnk;
    private final Queue<zzd> zzcnl;
    private zzf zzcnm;
    private boolean zzcnn;
    private final Context zzqG;

    public zzh(Context context, String str) {
        this(context, str, new ScheduledThreadPoolExecutor(0));
    }

    @VisibleForTesting
    private zzh(Context context, String str, ScheduledExecutorService scheduledExecutorService) {
        this.zzcnl = new LinkedList();
        this.zzcnn = false;
        this.zzqG = context.getApplicationContext();
        this.zzcnj = new Intent(str).setPackage(this.zzqG.getPackageName());
        this.zzcnk = scheduledExecutorService;
    }

    private final synchronized void zzKd() {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "flush queue called");
        }
        while (!this.zzcnl.isEmpty()) {
            if (Log.isLoggable("EnhancedIntentService", 3)) {
                Log.d("EnhancedIntentService", "found intent to be delivered");
            }
            if (this.zzcnm == null || !this.zzcnm.isBinderAlive()) {
                if (Log.isLoggable("EnhancedIntentService", 3)) {
                    Log.d("EnhancedIntentService", new StringBuilder(39).append("binder is dead. start connection? ").append(!this.zzcnn).toString());
                }
                if (!this.zzcnn) {
                    this.zzcnn = true;
                    try {
                    } catch (SecurityException e) {
                        Log.e("EnhancedIntentService", "Exception while binding the service", e);
                    }
                    if (!com.google.android.gms.common.stats.zza.zzrT().zza(this.zzqG, this.zzcnj, this, 65)) {
                        Log.e("EnhancedIntentService", "binding to the service failed");
                        while (!this.zzcnl.isEmpty()) {
                            this.zzcnl.poll().finish();
                        }
                    }
                }
            } else {
                if (Log.isLoggable("EnhancedIntentService", 3)) {
                    Log.d("EnhancedIntentService", "binder is alive, sending the intent.");
                }
                this.zzcnm.zza(this.zzcnl.poll());
            }
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (this) {
            this.zzcnn = false;
            this.zzcnm = (zzf) iBinder;
            if (Log.isLoggable("EnhancedIntentService", 3)) {
                String strValueOf = String.valueOf(componentName);
                Log.d("EnhancedIntentService", new StringBuilder(String.valueOf(strValueOf).length() + 20).append("onServiceConnected: ").append(strValueOf).toString());
            }
            zzKd();
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            String strValueOf = String.valueOf(componentName);
            Log.d("EnhancedIntentService", new StringBuilder(String.valueOf(strValueOf).length() + 23).append("onServiceDisconnected: ").append(strValueOf).toString());
        }
        zzKd();
    }

    public final synchronized void zza(Intent intent, BroadcastReceiver.PendingResult pendingResult) {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "new intent queued in the bind-strategy delivery");
        }
        this.zzcnl.add(new zzd(intent, pendingResult, this.zzcnk));
        zzKd();
    }
}
