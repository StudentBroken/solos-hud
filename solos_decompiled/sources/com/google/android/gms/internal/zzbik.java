package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes67.dex */
public final class zzbik {
    private static Context zzaKl;
    private static Boolean zzaKm;

    public static synchronized boolean zzaN(Context context) {
        boolean zBooleanValue;
        Context applicationContext = context.getApplicationContext();
        if (zzaKl == null || zzaKm == null || zzaKl != applicationContext) {
            zzaKm = null;
            if (com.google.android.gms.common.util.zzs.isAtLeastO()) {
                try {
                    zzaKm = (Boolean) PackageManager.class.getDeclaredMethod("isInstantApp", new Class[0]).invoke(applicationContext.getPackageManager(), new Object[0]);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    zzaKm = false;
                }
            } else {
                try {
                    context.getClassLoader().loadClass("com.google.android.instantapps.supervisor.InstantAppsRuntime");
                    zzaKm = true;
                } catch (ClassNotFoundException e2) {
                    zzaKm = false;
                }
            }
            zzaKl = applicationContext;
            zBooleanValue = zzaKm.booleanValue();
        } else {
            zBooleanValue = zzaKm.booleanValue();
        }
        return zBooleanValue;
    }
}
