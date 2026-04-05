package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.Intent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes56.dex */
final class zzd {
    final Intent intent;
    private final BroadcastReceiver.PendingResult zzcnc;
    private boolean zzcnd = false;
    private final ScheduledFuture<?> zzcne;

    zzd(Intent intent, BroadcastReceiver.PendingResult pendingResult, ScheduledExecutorService scheduledExecutorService) {
        this.intent = intent;
        this.zzcnc = pendingResult;
        this.zzcne = scheduledExecutorService.schedule(new zze(this, intent), 9500L, TimeUnit.MILLISECONDS);
    }

    final synchronized void finish() {
        if (!this.zzcnd) {
            this.zzcnc.finish();
            this.zzcne.cancel(false);
            this.zzcnd = true;
        }
    }
}
