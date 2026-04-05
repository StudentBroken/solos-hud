package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.gms.internal.zzbim;

/* JADX INFO: loaded from: classes67.dex */
public final class zze {
    public static int zzB(Context context, String str) {
        Bundle bundle;
        PackageInfo packageInfoZzC = zzC(context, str);
        if (packageInfoZzC == null || packageInfoZzC.applicationInfo == null || (bundle = packageInfoZzC.applicationInfo.metaData) == null) {
            return -1;
        }
        return bundle.getInt("com.google.android.gms.version", -1);
    }

    @Nullable
    private static PackageInfo zzC(Context context, String str) {
        try {
            return zzbim.zzaP(context).getPackageInfo(str, 128);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static boolean zzD(Context context, String str) {
        "com.google.android.gms".equals(str);
        try {
            return (zzbim.zzaP(context).getApplicationInfo(str, 0).flags & 2097152) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
