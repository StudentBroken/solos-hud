package com.google.android.gms.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.internal.zzbim;

/* JADX INFO: loaded from: classes67.dex */
public class zzp {
    private static zzp zzaAw;
    private final Context mContext;

    private zzp(Context context) {
        this.mContext = context.getApplicationContext();
    }

    static zzg zza(PackageInfo packageInfo, zzg... zzgVarArr) {
        if (packageInfo.signatures == null) {
            return null;
        }
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return null;
        }
        zzh zzhVar = new zzh(packageInfo.signatures[0].toByteArray());
        for (int i = 0; i < zzgVarArr.length; i++) {
            if (zzgVarArr[i].equals(zzhVar)) {
                return zzgVarArr[i];
            }
        }
        return null;
    }

    private static boolean zza(PackageInfo packageInfo, boolean z) {
        if (packageInfo != null && packageInfo.signatures != null) {
            if ((z ? zza(packageInfo, zzj.zzaAm) : zza(packageInfo, zzj.zzaAm[0])) != null) {
                return true;
            }
        }
        return false;
    }

    public static zzp zzax(Context context) {
        zzbr.zzu(context);
        synchronized (zzp.class) {
            if (zzaAw == null) {
                zzf.zzav(context);
                zzaAw = new zzp(context);
            }
        }
        return zzaAw;
    }

    private static boolean zzb(PackageInfo packageInfo, boolean z) {
        boolean zZzb = false;
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
        } else {
            zzh zzhVar = new zzh(packageInfo.signatures[0].toByteArray());
            String str = packageInfo.packageName;
            zZzb = z ? zzf.zzb(str, zzhVar) : zzf.zza(str, zzhVar);
            if (!zZzb) {
                Log.d("GoogleSignatureVerifier", new StringBuilder(27).append("Cert not in list. atk=").append(z).toString());
            }
        }
        return zZzb;
    }

    private final boolean zzct(String str) {
        try {
            PackageInfo packageInfo = zzbim.zzaP(this.mContext).getPackageInfo(str, 64);
            if (packageInfo == null) {
                return false;
            }
            if (zzo.zzaw(this.mContext)) {
                return zzb(packageInfo, true);
            }
            boolean zZzb = zzb(packageInfo, false);
            if (!zZzb && zzb(packageInfo, true)) {
                Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
            }
            return zZzb;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Deprecated
    public final boolean zza(PackageManager packageManager, int i) {
        String[] packagesForUid = zzbim.zzaP(this.mContext).getPackagesForUid(i);
        if (packagesForUid == null || packagesForUid.length == 0) {
            return false;
        }
        for (String str : packagesForUid) {
            if (zzct(str)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public final boolean zza(PackageManager packageManager, PackageInfo packageInfo) {
        if (packageInfo != null) {
            if (zza(packageInfo, false)) {
                return true;
            }
            if (zza(packageInfo, true)) {
                if (zzo.zzaw(this.mContext)) {
                    return true;
                }
                Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
            }
        }
        return false;
    }
}
