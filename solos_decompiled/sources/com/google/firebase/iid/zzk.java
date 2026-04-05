package com.google.firebase.iid;

/* JADX INFO: loaded from: classes35.dex */
public final class zzk {
    private static final Object zzuI = new Object();
    private final zzr zzcnB;

    zzk(zzr zzrVar) {
        this.zzcnB = zzrVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0029 A[Catch: all -> 0x002b, DONT_GENERATE, TryCatch #0 {, blocks: (B:4:0x0005, B:6:0x0012, B:8:0x001b, B:10:0x0024, B:11:0x0027, B:13:0x0029), top: B:18:0x0005 }] */
    @android.support.annotation.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final java.lang.String zzKk() {
        /*
            r6 = this;
            r0 = 0
            r5 = 1
            java.lang.Object r1 = com.google.firebase.iid.zzk.zzuI
            monitor-enter(r1)
            com.google.firebase.iid.zzr r2 = r6.zzcnB     // Catch: java.lang.Throwable -> L2b
            android.content.SharedPreferences r2 = r2.zzbhs     // Catch: java.lang.Throwable -> L2b
            java.lang.String r3 = "topic_operaion_queue"
            r4 = 0
            java.lang.String r2 = r2.getString(r3, r4)     // Catch: java.lang.Throwable -> L2b
            if (r2 == 0) goto L29
            java.lang.String r3 = ","
            java.lang.String[] r2 = r2.split(r3)     // Catch: java.lang.Throwable -> L2b
            int r3 = r2.length     // Catch: java.lang.Throwable -> L2b
            if (r3 <= r5) goto L29
            r3 = 1
            r3 = r2[r3]     // Catch: java.lang.Throwable -> L2b
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch: java.lang.Throwable -> L2b
            if (r3 != 0) goto L29
            r0 = 1
            r0 = r2[r0]     // Catch: java.lang.Throwable -> L2b
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L2b
        L28:
            return r0
        L29:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L2b
            goto L28
        L2b:
            r0 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L2b
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzk.zzKk():java.lang.String");
    }

    final void zzhE(String str) {
        synchronized (zzuI) {
            String string = this.zzcnB.zzbhs.getString("topic_operaion_queue", "");
            String strValueOf = String.valueOf(",");
            this.zzcnB.zzbhs.edit().putString("topic_operaion_queue", new StringBuilder(String.valueOf(string).length() + String.valueOf(strValueOf).length() + String.valueOf(str).length()).append(string).append(strValueOf).append(str).toString()).apply();
        }
    }

    final boolean zzhI(String str) {
        boolean z;
        synchronized (zzuI) {
            String string = this.zzcnB.zzbhs.getString("topic_operaion_queue", "");
            String strValueOf = String.valueOf(",");
            String strValueOf2 = String.valueOf(str);
            if (string.startsWith(strValueOf2.length() != 0 ? strValueOf.concat(strValueOf2) : new String(strValueOf))) {
                String strValueOf3 = String.valueOf(",");
                String strValueOf4 = String.valueOf(str);
                this.zzcnB.zzbhs.edit().putString("topic_operaion_queue", string.substring((strValueOf4.length() != 0 ? strValueOf3.concat(strValueOf4) : new String(strValueOf3)).length())).apply();
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }
}
