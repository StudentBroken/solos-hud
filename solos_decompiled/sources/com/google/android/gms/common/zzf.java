package com.google.android.gms.common;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.zzbb;
import com.google.android.gms.common.internal.zzbc;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.dynamite.DynamiteModule;

/* JADX INFO: loaded from: classes67.dex */
final class zzf {
    private static zzbb zzaAf;
    private static final Object zzaAg = new Object();
    private static Context zzaAh;

    static boolean zza(String str, zzg zzgVar) {
        return zza(str, zzgVar, false);
    }

    private static boolean zza(String str, zzg zzgVar, boolean z) {
        if (!zzoV()) {
            return false;
        }
        zzbr.zzu(zzaAh);
        try {
            return zzaAf.zza(new zzm(str, zzgVar, z), com.google.android.gms.dynamic.zzn.zzw(zzaAh.getPackageManager()));
        } catch (RemoteException e) {
            Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
            return false;
        }
    }

    static synchronized void zzav(Context context) {
        if (zzaAh != null) {
            Log.w("GoogleCertificates", "GoogleCertificates has been initialized already");
        } else if (context != null) {
            zzaAh = context.getApplicationContext();
        }
    }

    static boolean zzb(String str, zzg zzgVar) {
        return zza(str, zzgVar, true);
    }

    private static boolean zzoV() {
        boolean z = true;
        if (zzaAf == null) {
            zzbr.zzu(zzaAh);
            synchronized (zzaAg) {
                if (zzaAf == null) {
                    try {
                        zzaAf = zzbc.zzJ(DynamiteModule.zza(zzaAh, DynamiteModule.zzaST, "com.google.android.gms.googlecertificates").zzcW("com.google.android.gms.common.GoogleCertificatesImpl"));
                    } catch (DynamiteModule.zzc e) {
                        Log.e("GoogleCertificates", "Failed to load com.google.android.gms.googlecertificates", e);
                        z = false;
                    }
                }
            }
        }
        return z;
    }
}
