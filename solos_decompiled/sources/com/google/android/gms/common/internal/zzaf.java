package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;

/* JADX INFO: loaded from: classes67.dex */
public abstract class zzaf {
    private static final Object zzaHN = new Object();
    private static zzaf zzaHO;

    public static zzaf zzaC(Context context) {
        synchronized (zzaHN) {
            if (zzaHO == null) {
                zzaHO = new zzah(context.getApplicationContext());
            }
        }
        return zzaHO;
    }

    public final void zza(String str, String str2, ServiceConnection serviceConnection, String str3) {
        zzb(new zzag(str, str2), serviceConnection, str3);
    }

    public final boolean zza(ComponentName componentName, ServiceConnection serviceConnection, String str) {
        return zza(new zzag(componentName), serviceConnection, str);
    }

    protected abstract boolean zza(zzag zzagVar, ServiceConnection serviceConnection, String str);

    public final void zzb(ComponentName componentName, ServiceConnection serviceConnection, String str) {
        zzb(new zzag(componentName), serviceConnection, str);
    }

    protected abstract void zzb(zzag zzagVar, ServiceConnection serviceConnection, String str);
}
