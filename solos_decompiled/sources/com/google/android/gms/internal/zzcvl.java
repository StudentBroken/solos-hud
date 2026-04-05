package com.google.android.gms.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcvl {
    private final Context mContext;
    private final String zzaJr;
    private final String zzaJt;
    private final PowerManager.WakeLock zzbDb;
    private WorkSource zzbDc;
    private final int zzbDd;
    private final String zzbDe;
    private boolean zzbDf;
    private int zzbDg;
    private int zzbDh;
    private static String TAG = "WakeLock";
    private static String zzbDa = "*gcore*:";
    private static boolean DEBUG = false;

    public zzcvl(Context context, int i, String str) {
        this(context, 1, str, null, context == null ? null : context.getPackageName());
    }

    @SuppressLint({"UnwrappedWakeLock"})
    private zzcvl(Context context, int i, String str, String str2, String str3) {
        this(context, 1, str, null, str3, null);
    }

    @SuppressLint({"UnwrappedWakeLock"})
    private zzcvl(Context context, int i, String str, String str2, String str3, String str4) {
        this.zzbDf = true;
        zzbr.zzh(str, "Wake lock name can NOT be empty");
        this.zzbDd = i;
        this.zzbDe = null;
        this.zzaJt = null;
        this.mContext = context.getApplicationContext();
        if ("com.google.android.gms".equals(context.getPackageName())) {
            this.zzaJr = str;
        } else {
            String strValueOf = String.valueOf(zzbDa);
            String strValueOf2 = String.valueOf(str);
            this.zzaJr = strValueOf2.length() != 0 ? strValueOf.concat(strValueOf2) : new String(strValueOf);
        }
        this.zzbDb = ((PowerManager) context.getSystemService("power")).newWakeLock(i, str);
        if (com.google.android.gms.common.util.zzz.zzaM(this.mContext)) {
            this.zzbDc = com.google.android.gms.common.util.zzz.zzE(context, com.google.android.gms.common.util.zzv.zzcM(str3) ? context.getPackageName() : str3);
            WorkSource workSource = this.zzbDc;
            if (workSource == null || !com.google.android.gms.common.util.zzz.zzaM(this.mContext)) {
                return;
            }
            if (this.zzbDc != null) {
                this.zzbDc.add(workSource);
            } else {
                this.zzbDc = workSource;
            }
            try {
                this.zzbDb.setWorkSource(this.zzbDc);
            } catch (IllegalArgumentException e) {
                Log.wtf(TAG, e.toString());
            }
        }
    }

    private final boolean zzeW(String str) {
        String str2 = null;
        return (TextUtils.isEmpty(null) || str2.equals(this.zzbDe)) ? false : true;
    }

    private final String zzi(String str, boolean z) {
        if (this.zzbDf && z) {
            return null;
        }
        return this.zzbDe;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0022 A[Catch: all -> 0x004b, TryCatch #0 {, blocks: (B:4:0x000c, B:6:0x0010, B:13:0x0022, B:14:0x0044, B:9:0x001a, B:11:0x001e), top: B:20:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x001a A[Catch: all -> 0x004b, TryCatch #0 {, blocks: (B:4:0x000c, B:6:0x0010, B:13:0x0022, B:14:0x0044, B:9:0x001a, B:11:0x001e), top: B:20:0x000c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void acquire(long r13) {
        /*
            r12 = this;
            r10 = 1000(0x3e8, double:4.94E-321)
            r1 = 0
            boolean r0 = r12.zzeW(r1)
            java.lang.String r4 = r12.zzi(r1, r0)
            monitor-enter(r12)
            boolean r1 = r12.zzbDf     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L1a
            int r1 = r12.zzbDg     // Catch: java.lang.Throwable -> L4b
            int r2 = r1 + 1
            r12.zzbDg = r2     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L22
            if (r0 != 0) goto L22
        L1a:
            boolean r0 = r12.zzbDf     // Catch: java.lang.Throwable -> L4b
            if (r0 != 0) goto L44
            int r0 = r12.zzbDh     // Catch: java.lang.Throwable -> L4b
            if (r0 != 0) goto L44
        L22:
            com.google.android.gms.common.stats.zze.zzrW()     // Catch: java.lang.Throwable -> L4b
            android.content.Context r0 = r12.mContext     // Catch: java.lang.Throwable -> L4b
            android.os.PowerManager$WakeLock r1 = r12.zzbDb     // Catch: java.lang.Throwable -> L4b
            java.lang.String r1 = com.google.android.gms.common.stats.zzc.zza(r1, r4)     // Catch: java.lang.Throwable -> L4b
            r2 = 7
            java.lang.String r3 = r12.zzaJr     // Catch: java.lang.Throwable -> L4b
            r5 = 0
            int r6 = r12.zzbDd     // Catch: java.lang.Throwable -> L4b
            android.os.WorkSource r7 = r12.zzbDc     // Catch: java.lang.Throwable -> L4b
            java.util.List r7 = com.google.android.gms.common.util.zzz.zzb(r7)     // Catch: java.lang.Throwable -> L4b
            r8 = 1000(0x3e8, double:4.94E-321)
            com.google.android.gms.common.stats.zze.zza(r0, r1, r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L4b
            int r0 = r12.zzbDh     // Catch: java.lang.Throwable -> L4b
            int r0 = r0 + 1
            r12.zzbDh = r0     // Catch: java.lang.Throwable -> L4b
        L44:
            monitor-exit(r12)     // Catch: java.lang.Throwable -> L4b
            android.os.PowerManager$WakeLock r0 = r12.zzbDb
            r0.acquire(r10)
            return
        L4b:
            r0 = move-exception
            monitor-exit(r12)     // Catch: java.lang.Throwable -> L4b
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcvl.acquire(long):void");
    }

    public final boolean isHeld() {
        return this.zzbDb.isHeld();
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0021 A[Catch: all -> 0x0049, TryCatch #0 {, blocks: (B:4:0x000a, B:6:0x000e, B:13:0x0021, B:14:0x0042, B:9:0x0018, B:11:0x001c), top: B:20:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0018 A[Catch: all -> 0x0049, TryCatch #0 {, blocks: (B:4:0x000a, B:6:0x000e, B:13:0x0021, B:14:0x0042, B:9:0x0018, B:11:0x001c), top: B:20:0x000a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void release() {
        /*
            r8 = this;
            r1 = 0
            boolean r0 = r8.zzeW(r1)
            java.lang.String r4 = r8.zzi(r1, r0)
            monitor-enter(r8)
            boolean r1 = r8.zzbDf     // Catch: java.lang.Throwable -> L49
            if (r1 == 0) goto L18
            int r1 = r8.zzbDg     // Catch: java.lang.Throwable -> L49
            int r1 = r1 + (-1)
            r8.zzbDg = r1     // Catch: java.lang.Throwable -> L49
            if (r1 == 0) goto L21
            if (r0 != 0) goto L21
        L18:
            boolean r0 = r8.zzbDf     // Catch: java.lang.Throwable -> L49
            if (r0 != 0) goto L42
            int r0 = r8.zzbDh     // Catch: java.lang.Throwable -> L49
            r1 = 1
            if (r0 != r1) goto L42
        L21:
            com.google.android.gms.common.stats.zze.zzrW()     // Catch: java.lang.Throwable -> L49
            android.content.Context r0 = r8.mContext     // Catch: java.lang.Throwable -> L49
            android.os.PowerManager$WakeLock r1 = r8.zzbDb     // Catch: java.lang.Throwable -> L49
            java.lang.String r1 = com.google.android.gms.common.stats.zzc.zza(r1, r4)     // Catch: java.lang.Throwable -> L49
            r2 = 8
            java.lang.String r3 = r8.zzaJr     // Catch: java.lang.Throwable -> L49
            r5 = 0
            int r6 = r8.zzbDd     // Catch: java.lang.Throwable -> L49
            android.os.WorkSource r7 = r8.zzbDc     // Catch: java.lang.Throwable -> L49
            java.util.List r7 = com.google.android.gms.common.util.zzz.zzb(r7)     // Catch: java.lang.Throwable -> L49
            com.google.android.gms.common.stats.zze.zza(r0, r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L49
            int r0 = r8.zzbDh     // Catch: java.lang.Throwable -> L49
            int r0 = r0 + (-1)
            r8.zzbDh = r0     // Catch: java.lang.Throwable -> L49
        L42:
            monitor-exit(r8)     // Catch: java.lang.Throwable -> L49
            android.os.PowerManager$WakeLock r0 = r8.zzbDb
            r0.release()
            return
        L49:
            r0 = move-exception
            monitor-exit(r8)     // Catch: java.lang.Throwable -> L49
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcvl.release():void");
    }

    public final void setReferenceCounted(boolean z) {
        this.zzbDb.setReferenceCounted(false);
        this.zzbDf = false;
    }
}
