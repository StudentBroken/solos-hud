package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.WorkSource;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.internal.zzbim;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes67.dex */
public final class zzz {
    private static final Method zzaKd = zzsf();
    private static final Method zzaKe = zzsg();
    private static final Method zzaKf = zzsh();
    private static final Method zzaKg = zzsi();
    private static final Method zzaKh = zzsj();

    @Nullable
    public static WorkSource zzE(Context context, String str) {
        if (context == null || context.getPackageManager() == null) {
            return null;
        }
        try {
            ApplicationInfo applicationInfo = zzbim.zzaP(context).getApplicationInfo(str, 0);
            if (applicationInfo != null) {
                return zze(applicationInfo.uid, str);
            }
            String strValueOf = String.valueOf(str);
            Log.e("WorkSourceUtil", strValueOf.length() != 0 ? "Could not get applicationInfo from package: ".concat(strValueOf) : new String("Could not get applicationInfo from package: "));
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            String strValueOf2 = String.valueOf(str);
            Log.e("WorkSourceUtil", strValueOf2.length() != 0 ? "Could not find package: ".concat(strValueOf2) : new String("Could not find package: "));
            return null;
        }
    }

    private static int zza(WorkSource workSource) {
        if (zzaKf != null) {
            try {
                return ((Integer) zzaKf.invoke(workSource, new Object[0])).intValue();
            } catch (Exception e) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
            }
        }
        return 0;
    }

    @Nullable
    private static String zza(WorkSource workSource, int i) {
        if (zzaKh != null) {
            try {
                return (String) zzaKh.invoke(workSource, Integer.valueOf(i));
            } catch (Exception e) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
            }
        }
        return null;
    }

    public static boolean zzaM(Context context) {
        return (context == null || context.getPackageManager() == null || zzbim.zzaP(context).checkPermission("android.permission.UPDATE_DEVICE_STATS", context.getPackageName()) != 0) ? false : true;
    }

    public static List<String> zzb(@Nullable WorkSource workSource) {
        int iZza = workSource == null ? 0 : zza(workSource);
        if (iZza == 0) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < iZza; i++) {
            String strZza = zza(workSource, i);
            if (!zzv.zzcM(strZza)) {
                arrayList.add(strZza);
            }
        }
        return arrayList;
    }

    private static WorkSource zze(int i, String str) {
        WorkSource workSource = new WorkSource();
        if (zzaKe != null) {
            if (str == null) {
                str = "";
            }
            try {
                zzaKe.invoke(workSource, Integer.valueOf(i), str);
            } catch (Exception e) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
            }
        } else if (zzaKd != null) {
            try {
                zzaKd.invoke(workSource, Integer.valueOf(i));
            } catch (Exception e2) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e2);
            }
        }
        return workSource;
    }

    private static Method zzsf() {
        try {
            return WorkSource.class.getMethod("add", Integer.TYPE);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method zzsg() {
        if (!zzs.zzsa()) {
            return null;
        }
        try {
            return WorkSource.class.getMethod("add", Integer.TYPE, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method zzsh() {
        try {
            return WorkSource.class.getMethod("size", new Class[0]);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method zzsi() {
        try {
            return WorkSource.class.getMethod("get", Integer.TYPE);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method zzsj() {
        if (!zzs.zzsa()) {
            return null;
        }
        try {
            return WorkSource.class.getMethod("getName", Integer.TYPE);
        } catch (Exception e) {
            return null;
        }
    }
}
