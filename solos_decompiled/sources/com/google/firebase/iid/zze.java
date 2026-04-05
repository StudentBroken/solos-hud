package com.google.firebase.iid;

import android.content.Intent;
import android.util.Log;

/* JADX INFO: loaded from: classes56.dex */
final class zze implements Runnable {
    private /* synthetic */ Intent val$intent;
    private /* synthetic */ zzd zzcnf;

    zze(zzd zzdVar, Intent intent) {
        this.zzcnf = zzdVar;
        this.val$intent = intent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String strValueOf = String.valueOf(this.val$intent.getAction());
        Log.w("EnhancedIntentService", new StringBuilder(String.valueOf(strValueOf).length() + 61).append("Service took too long to process intent: ").append(strValueOf).append(" App may get closed.").toString());
        this.zzcnf.finish();
    }
}
