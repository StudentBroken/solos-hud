package com.google.firebase.iid;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import com.digits.sdk.vcard.VCardConfig;
import java.util.LinkedList;
import java.util.Queue;

/* JADX INFO: loaded from: classes35.dex */
public final class zzq {
    private static zzq zzcnG;
    private final SimpleArrayMap<String, String> zzcnH = new SimpleArrayMap<>();
    private Boolean zzcnI = null;

    @VisibleForTesting
    final Queue<Intent> zzcnJ = new LinkedList();

    @VisibleForTesting
    private Queue<Intent> zzcnK = new LinkedList();

    private zzq() {
    }

    public static synchronized zzq zzKm() {
        if (zzcnG == null) {
            zzcnG = new zzq();
        }
        return zzcnG;
    }

    public static PendingIntent zza(Context context, int i, Intent intent, int i2) {
        return zza(context, 0, "com.google.firebase.INSTANCE_ID_EVENT", intent, VCardConfig.FLAG_CONVERT_PHONETIC_NAME_STRINGS);
    }

    private static PendingIntent zza(Context context, int i, String str, Intent intent, int i2) {
        Intent intent2 = new Intent(context, (Class<?>) FirebaseInstanceIdInternalReceiver.class);
        intent2.setAction(str);
        intent2.putExtra("wrapped_intent", intent);
        return PendingIntent.getBroadcast(context, i, intent2, i2);
    }

    public static PendingIntent zzb(Context context, int i, Intent intent, int i2) {
        return zza(context, i, "com.google.firebase.MESSAGING_EVENT", intent, 1073741824);
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00e1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final int zzf(android.content.Context r7, android.content.Intent r8) {
        /*
            Method dump skipped, instruction units count: 344
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzq.zzf(android.content.Context, android.content.Intent):int");
    }

    public final Intent zzKn() {
        return this.zzcnK.poll();
    }

    public final int zza(Context context, String str, Intent intent) {
        switch (str) {
            case "com.google.firebase.INSTANCE_ID_EVENT":
                this.zzcnJ.offer(intent);
                break;
            case "com.google.firebase.MESSAGING_EVENT":
                this.zzcnK.offer(intent);
                break;
            default:
                String strValueOf = String.valueOf(str);
                Log.w("FirebaseInstanceId", strValueOf.length() != 0 ? "Unknown service action: ".concat(strValueOf) : new String("Unknown service action: "));
                return 500;
        }
        Intent intent2 = new Intent(str);
        intent2.setPackage(context.getPackageName());
        return zzf(context, intent2);
    }

    public final void zze(Context context, Intent intent) {
        zza(context, "com.google.firebase.INSTANCE_ID_EVENT", intent);
    }
}
