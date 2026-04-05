package com.google.firebase.iid;

import android.util.Log;

/* JADX INFO: loaded from: classes56.dex */
final class zzg implements Runnable {
    private /* synthetic */ zzd zzcnh;
    private /* synthetic */ zzf zzcni;

    zzg(zzf zzfVar, zzd zzdVar) {
        this.zzcni = zzfVar;
        this.zzcnh = zzdVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "bg processing of the intent starting now");
        }
        this.zzcni.zzcng.handleIntent(this.zzcnh.intent);
        this.zzcnh.finish();
    }
}
