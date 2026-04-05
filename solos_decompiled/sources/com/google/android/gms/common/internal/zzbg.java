package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.internal.zzbim;

/* JADX INFO: loaded from: classes67.dex */
public final class zzbg {
    private static boolean zzRm;
    private static String zzaIh;
    private static int zzaIi;
    private static Object zzuI = new Object();

    public static String zzaD(Context context) {
        zzaF(context);
        return zzaIh;
    }

    public static int zzaE(Context context) {
        zzaF(context);
        return zzaIi;
    }

    private static void zzaF(Context context) {
        synchronized (zzuI) {
            if (zzRm) {
                return;
            }
            zzRm = true;
            try {
                Bundle bundle = zzbim.zzaP(context).getApplicationInfo(context.getPackageName(), 128).metaData;
                if (bundle == null) {
                    return;
                }
                zzaIh = bundle.getString("com.google.app.id");
                zzaIi = bundle.getInt("com.google.android.gms.version");
            } catch (PackageManager.NameNotFoundException e) {
                Log.wtf("MetadataValueReader", "This should never happen.", e);
            }
        }
    }
}
