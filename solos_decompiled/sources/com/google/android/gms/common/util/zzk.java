package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;

/* JADX INFO: loaded from: classes67.dex */
public final class zzk {
    private static Boolean zzaJL;
    private static Boolean zzaJM;
    private static Boolean zzaJN;
    private static Boolean zzaJO;
    private static Boolean zzaJP;

    /* JADX WARN: Removed duplicated region for block: B:21:0x003a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean zza(android.content.res.Resources r5) {
        /*
            r4 = 3
            r1 = 1
            r2 = 0
            if (r5 != 0) goto L6
        L5:
            return r2
        L6:
            java.lang.Boolean r0 = com.google.android.gms.common.util.zzk.zzaJL
            if (r0 != 0) goto L41
            android.content.res.Configuration r0 = r5.getConfiguration()
            int r0 = r0.screenLayout
            r0 = r0 & 15
            if (r0 <= r4) goto L48
            r0 = r1
        L15:
            if (r0 != 0) goto L3a
            java.lang.Boolean r0 = com.google.android.gms.common.util.zzk.zzaJM
            if (r0 != 0) goto L32
            android.content.res.Configuration r0 = r5.getConfiguration()
            int r3 = r0.screenLayout
            r3 = r3 & 15
            if (r3 > r4) goto L4a
            int r0 = r0.smallestScreenWidthDp
            r3 = 600(0x258, float:8.41E-43)
            if (r0 < r3) goto L4a
            r0 = r1
        L2c:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            com.google.android.gms.common.util.zzk.zzaJM = r0
        L32:
            java.lang.Boolean r0 = com.google.android.gms.common.util.zzk.zzaJM
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L3b
        L3a:
            r2 = r1
        L3b:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r2)
            com.google.android.gms.common.util.zzk.zzaJL = r0
        L41:
            java.lang.Boolean r0 = com.google.android.gms.common.util.zzk.zzaJL
            boolean r2 = r0.booleanValue()
            goto L5
        L48:
            r0 = r2
            goto L15
        L4a:
            r0 = r2
            goto L2c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.zzk.zza(android.content.res.Resources):boolean");
    }

    @TargetApi(20)
    public static boolean zzaG(Context context) {
        if (zzaJN == null) {
            zzaJN = Boolean.valueOf(zzs.zzsc() && context.getPackageManager().hasSystemFeature("android.hardware.type.watch"));
        }
        return zzaJN.booleanValue();
    }

    @TargetApi(24)
    public static boolean zzaH(Context context) {
        return (!zzs.isAtLeastN() || zzaI(context)) && zzaG(context);
    }

    @TargetApi(21)
    public static boolean zzaI(Context context) {
        if (zzaJO == null) {
            zzaJO = Boolean.valueOf(zzs.zzsd() && context.getPackageManager().hasSystemFeature("cn.google"));
        }
        return zzaJO.booleanValue();
    }

    public static boolean zzaJ(Context context) {
        if (zzaJP == null) {
            zzaJP = Boolean.valueOf(context.getPackageManager().hasSystemFeature("android.hardware.type.iot") || context.getPackageManager().hasSystemFeature("android.hardware.type.embedded"));
        }
        return zzaJP.booleanValue();
    }
}
