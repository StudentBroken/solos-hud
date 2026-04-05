package com.google.android.gms.internal;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes67.dex */
public class iu {
    private static HashMap<String, String> zzbUg;
    private static Object zzbUl;
    private static boolean zzbUm;
    private static Uri CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");
    private static Uri zzbUc = Uri.parse("content://com.google.android.gsf.gservices/prefix");
    private static Pattern zzbUd = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
    private static Pattern zzbUe = Pattern.compile("^(0|false|f|off|no|n)$", 2);
    private static final AtomicBoolean zzbUf = new AtomicBoolean();
    private static HashMap<String, Boolean> zzbUh = new HashMap<>();
    private static HashMap<String, Integer> zzbUi = new HashMap<>();
    private static HashMap<String, Long> zzbUj = new HashMap<>();
    private static HashMap<String, Float> zzbUk = new HashMap<>();
    private static String[] zzbUn = new String[0];

    public static long getLong(ContentResolver contentResolver, String str, long j) {
        Long lValueOf;
        long j2;
        Object objZzb = zzb(contentResolver);
        Long l = (Long) zza((HashMap<String, long>) zzbUj, str, 0L);
        if (l != null) {
            return l.longValue();
        }
        String strZza = zza(contentResolver, str, (String) null);
        if (strZza == null) {
            lValueOf = l;
            j2 = 0;
        } else {
            try {
                long j3 = Long.parseLong(strZza);
                lValueOf = Long.valueOf(j3);
                j2 = j3;
            } catch (NumberFormatException e) {
                lValueOf = l;
                j2 = 0;
            }
        }
        HashMap<String, Long> map = zzbUj;
        synchronized (iu.class) {
            if (objZzb == zzbUl) {
                map.put(str, lValueOf);
                zzbUg.remove(str);
            }
        }
        return j2;
    }

    private static <T> T zza(HashMap<String, T> map, String str, T t) {
        synchronized (iu.class) {
            if (!map.containsKey(str)) {
                return null;
            }
            T t2 = map.get(str);
            if (t2 == null) {
                t2 = t;
            }
            return t2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0057 A[Catch: all -> 0x0054, DONT_GENERATE, TRY_ENTER, TryCatch #0 {, blocks: (B:4:0x0006, B:6:0x0013, B:9:0x001e, B:11:0x0020, B:13:0x0026, B:15:0x002e, B:17:0x0032, B:29:0x0057, B:19:0x003a, B:21:0x0047, B:24:0x0052, B:31:0x0059, B:32:0x005c), top: B:55:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0072 A[Catch: all -> 0x0097, TRY_LEAVE, TryCatch #1 {all -> 0x0097, blocks: (B:35:0x006c, B:41:0x007d, B:43:0x0083, B:46:0x008b, B:37:0x0072), top: B:56:0x006c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String zza(android.content.ContentResolver r9, java.lang.String r10, java.lang.String r11) {
        /*
            r8 = 1
            r3 = 0
            r2 = 0
            java.lang.Class<com.google.android.gms.internal.iu> r1 = com.google.android.gms.internal.iu.class
            monitor-enter(r1)
            zza(r9)     // Catch: java.lang.Throwable -> L54
            java.lang.Object r6 = com.google.android.gms.internal.iu.zzbUl     // Catch: java.lang.Throwable -> L54
            java.util.HashMap<java.lang.String, java.lang.String> r0 = com.google.android.gms.internal.iu.zzbUg     // Catch: java.lang.Throwable -> L54
            boolean r0 = r0.containsKey(r10)     // Catch: java.lang.Throwable -> L54
            if (r0 == 0) goto L20
            java.util.HashMap<java.lang.String, java.lang.String> r0 = com.google.android.gms.internal.iu.zzbUg     // Catch: java.lang.Throwable -> L54
            java.lang.Object r0 = r0.get(r10)     // Catch: java.lang.Throwable -> L54
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Throwable -> L54
            if (r0 == 0) goto L1e
            r2 = r0
        L1e:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L54
        L1f:
            return r2
        L20:
            java.lang.String[] r4 = com.google.android.gms.internal.iu.zzbUn     // Catch: java.lang.Throwable -> L54
            int r5 = r4.length     // Catch: java.lang.Throwable -> L54
            r0 = r3
        L24:
            if (r0 >= r5) goto L5c
            r7 = r4[r0]     // Catch: java.lang.Throwable -> L54
            boolean r7 = r10.startsWith(r7)     // Catch: java.lang.Throwable -> L54
            if (r7 == 0) goto L59
            boolean r0 = com.google.android.gms.internal.iu.zzbUm     // Catch: java.lang.Throwable -> L54
            if (r0 == 0) goto L3a
            java.util.HashMap<java.lang.String, java.lang.String> r0 = com.google.android.gms.internal.iu.zzbUg     // Catch: java.lang.Throwable -> L54
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> L54
            if (r0 == 0) goto L57
        L3a:
            java.lang.String[] r0 = com.google.android.gms.internal.iu.zzbUn     // Catch: java.lang.Throwable -> L54
            zzc(r9, r0)     // Catch: java.lang.Throwable -> L54
            java.util.HashMap<java.lang.String, java.lang.String> r0 = com.google.android.gms.internal.iu.zzbUg     // Catch: java.lang.Throwable -> L54
            boolean r0 = r0.containsKey(r10)     // Catch: java.lang.Throwable -> L54
            if (r0 == 0) goto L57
            java.util.HashMap<java.lang.String, java.lang.String> r0 = com.google.android.gms.internal.iu.zzbUg     // Catch: java.lang.Throwable -> L54
            java.lang.Object r0 = r0.get(r10)     // Catch: java.lang.Throwable -> L54
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Throwable -> L54
            if (r0 == 0) goto L52
            r2 = r0
        L52:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L54
            goto L1f
        L54:
            r0 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L54
            throw r0
        L57:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L54
            goto L1f
        L59:
            int r0 = r0 + 1
            goto L24
        L5c:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L54
            android.net.Uri r1 = com.google.android.gms.internal.iu.CONTENT_URI
            java.lang.String[] r4 = new java.lang.String[r8]
            r4[r3] = r10
            r0 = r9
            r3 = r2
            r5 = r2
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5)
            if (r1 == 0) goto L72
            boolean r0 = r1.moveToFirst()     // Catch: java.lang.Throwable -> L97
            if (r0 != 0) goto L7c
        L72:
            r0 = 0
            zza(r6, r10, r0)     // Catch: java.lang.Throwable -> L97
            if (r1 == 0) goto L1f
            r1.close()
            goto L1f
        L7c:
            r0 = 1
            java.lang.String r0 = r1.getString(r0)     // Catch: java.lang.Throwable -> L97
            if (r0 == 0) goto L8b
            r3 = 0
            boolean r3 = r0.equals(r3)     // Catch: java.lang.Throwable -> L97
            if (r3 == 0) goto L8b
            r0 = r2
        L8b:
            zza(r6, r10, r0)     // Catch: java.lang.Throwable -> L97
            if (r0 == 0) goto L91
            r2 = r0
        L91:
            if (r1 == 0) goto L1f
            r1.close()
            goto L1f
        L97:
            r0 = move-exception
            if (r1 == 0) goto L9d
            r1.close()
        L9d:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.iu.zza(android.content.ContentResolver, java.lang.String, java.lang.String):java.lang.String");
    }

    private static Map<String, String> zza(ContentResolver contentResolver, String... strArr) {
        Cursor cursorQuery = contentResolver.query(zzbUc, null, null, strArr, null);
        TreeMap treeMap = new TreeMap();
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                try {
                    treeMap.put(cursorQuery.getString(0), cursorQuery.getString(1));
                } finally {
                    cursorQuery.close();
                }
            }
        }
        return treeMap;
    }

    private static void zza(ContentResolver contentResolver) {
        if (zzbUg == null) {
            zzbUf.set(false);
            zzbUg = new HashMap<>();
            zzbUl = new Object();
            zzbUm = false;
            contentResolver.registerContentObserver(CONTENT_URI, true, new iv(null));
            return;
        }
        if (zzbUf.getAndSet(false)) {
            zzbUg.clear();
            zzbUh.clear();
            zzbUi.clear();
            zzbUj.clear();
            zzbUk.clear();
            zzbUl = new Object();
            zzbUm = false;
        }
    }

    private static void zza(Object obj, String str, String str2) {
        synchronized (iu.class) {
            if (obj == zzbUl) {
                zzbUg.put(str, str2);
            }
        }
    }

    private static Object zzb(ContentResolver contentResolver) {
        Object obj;
        synchronized (iu.class) {
            zza(contentResolver);
            obj = zzbUl;
        }
        return obj;
    }

    public static void zzb(ContentResolver contentResolver, String... strArr) {
        String[] strArr2;
        if (strArr.length == 0) {
            return;
        }
        synchronized (iu.class) {
            zza(contentResolver);
            HashSet hashSet = new HashSet((((zzbUn.length + strArr.length) << 2) / 3) + 1);
            hashSet.addAll(Arrays.asList(zzbUn));
            ArrayList arrayList = new ArrayList();
            for (String str : strArr) {
                if (hashSet.add(str)) {
                    arrayList.add(str);
                }
            }
            if (arrayList.isEmpty()) {
                strArr2 = new String[0];
            } else {
                zzbUn = (String[]) hashSet.toArray(new String[hashSet.size()]);
                strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
            if (!zzbUm || zzbUg.isEmpty()) {
                zzc(contentResolver, zzbUn);
            } else if (strArr2.length != 0) {
                zzc(contentResolver, strArr2);
            }
        }
    }

    private static void zzc(ContentResolver contentResolver, String[] strArr) {
        zzbUg.putAll(zza(contentResolver, strArr));
        zzbUm = true;
    }
}
