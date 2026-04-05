package com.google.firebase.iid;

import android.os.Binder;
import android.os.Process;
import android.util.Log;

/* JADX INFO: loaded from: classes56.dex */
public final class zzf extends Binder {
    private final zzb zzcng;

    zzf(zzb zzbVar) {
        this.zzcng = zzbVar;
    }

    public final void zza(zzd zzdVar) {
        if (Binder.getCallingUid() != Process.myUid()) {
            throw new SecurityException("Binding only allowed within app");
        }
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "service received new intent via bind strategy");
        }
        if (this.zzcng.zzo(zzdVar.intent)) {
            zzdVar.finish();
            return;
        }
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "intent being queued for bg execution");
        }
        this.zzcng.zzbrZ.execute(new zzg(this, zzdVar));
    }
}
