package com.ua.sdk.util;

import java.io.InputStream;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public final class Log {
    private static final String LOG_TAG = "sdk";
    private static boolean debugEnabled = true;

    private Log() {
    }

    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    public static void exception(String msg, String[] errors) {
        android.util.Log.e("sdk", msg, null);
        if (errors != null) {
            for (String error : errors) {
                android.util.Log.e("sdk", error, null);
            }
        }
    }

    public static void exception(String msg, List<String> errors) {
        android.util.Log.e("sdk", msg, null);
        if (errors != null) {
            for (String error : errors) {
                android.util.Log.e("sdk", error, null);
            }
        }
    }

    public static void exception(String msg, Throwable tr) {
        android.util.Log.e("sdk", msg, tr);
    }

    public static void exception(Throwable tr) {
        android.util.Log.e("sdk", "", tr);
    }

    public static void exception(String msg) {
        android.util.Log.e("sdk", msg);
    }

    public static void debug(String msg) {
        debug(msg, (Throwable) null);
    }

    public static void debug(Throwable tr) {
        debug((String) null, tr);
    }

    public static void debug(String msg, Throwable tr) {
        if (debugEnabled) {
            if (msg != null && msg.length() > 0) {
                android.util.Log.d("sdk", msg + "\n");
            }
            if (tr != null) {
                android.util.Log.d("sdk", tr.getMessage());
                StackTraceElement[] arr$ = tr.getStackTrace();
                for (StackTraceElement ste : arr$) {
                    android.util.Log.d("sdk", "\t" + ste.toString());
                }
            }
        }
    }

    public static InputStream debug(InputStream is) {
        return debug((String) null, is);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x003a A[Catch: IOException -> 0x0044, LOOP:1: B:28:0x0034->B:18:0x003a, LOOP_END, TRY_LEAVE, TryCatch #0 {IOException -> 0x0044, blocks: (B:16:0x0034, B:18:0x003a, B:26:0x0060), top: B:28:0x0034 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0060 A[EDGE_INSN: B:33:0x0060->B:26:0x0060 BREAK  A[LOOP:1: B:28:0x0034->B:18:0x003a], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.io.InputStream debug(java.lang.String r9, java.io.InputStream r10) {
        /*
            r8 = -1
            boolean r6 = com.ua.sdk.util.Log.debugEnabled
            if (r6 == 0) goto L7
            if (r10 != 0) goto L8
        L7:
            return r10
        L8:
            r0 = 0
            r6 = 2048(0x800, float:2.87E-42)
            byte[] r2 = new byte[r6]
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream
            r1.<init>()
        L12:
            int r0 = r10.read(r2)     // Catch: java.io.IOException -> L1d
            if (r8 == r0) goto L59
            r6 = 0
            r1.write(r2, r6, r0)     // Catch: java.io.IOException -> L1d
            goto L12
        L1d:
            r3 = move-exception
            exception(r3)
        L21:
            java.io.ByteArrayInputStream r4 = new java.io.ByteArrayInputStream
            byte[] r6 = r1.toByteArray()
            r4.<init>(r6)
            java.lang.StringBuffer r5 = new java.lang.StringBuffer
            r5.<init>()
            if (r9 == 0) goto L5d
        L31:
            r5.append(r9)
        L34:
            int r0 = r4.read(r2)     // Catch: java.io.IOException -> L44
            if (r0 == r8) goto L60
            java.lang.String r6 = new java.lang.String     // Catch: java.io.IOException -> L44
            r7 = 0
            r6.<init>(r2, r7, r0)     // Catch: java.io.IOException -> L44
            r5.append(r6)     // Catch: java.io.IOException -> L44
            goto L34
        L44:
            r3 = move-exception
            exception(r3)
        L48:
            java.lang.String r6 = r5.toString()
            debug(r6)
            java.io.ByteArrayInputStream r10 = new java.io.ByteArrayInputStream
            byte[] r6 = r1.toByteArray()
            r10.<init>(r6)
            goto L7
        L59:
            r10.close()     // Catch: java.io.IOException -> L1d
            goto L21
        L5d:
            java.lang.String r9 = ""
            goto L31
        L60:
            r4.close()     // Catch: java.io.IOException -> L44
            goto L48
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ua.sdk.util.Log.debug(java.lang.String, java.io.InputStream):java.io.InputStream");
    }
}
