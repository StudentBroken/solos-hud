package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/* JADX INFO: loaded from: classes35.dex */
public final class FirebaseInstanceIdInternalReceiver extends WakefulBroadcastReceiver {
    private static boolean zzbfF = false;
    private static zzh zzcnt;
    private static zzh zzcnu;

    static synchronized zzh zzI(Context context, String str) {
        zzh zzhVar;
        if ("com.google.firebase.MESSAGING_EVENT".equals(str)) {
            if (zzcnu == null) {
                zzcnu = new zzh(context, str);
            }
            zzhVar = zzcnu;
        } else {
            if (zzcnt == null) {
                zzcnt = new zzh(context, str);
            }
            zzhVar = zzcnt;
        }
        return zzhVar;
    }

    static boolean zzbH(Context context) {
        return com.google.android.gms.common.util.zzs.isAtLeastO() && context.getApplicationInfo().targetSdkVersion > 25;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        Parcelable parcelableExtra = intent.getParcelableExtra("wrapped_intent");
        if (!(parcelableExtra instanceof Intent)) {
            Log.e("FirebaseInstanceId", "Missing or invalid wrapped intent");
            return;
        }
        Intent intent2 = (Intent) parcelableExtra;
        if (zzbH(context)) {
            zzI(context, intent.getAction()).zza(intent2, goAsync());
        } else {
            zzq.zzKm().zza(context, intent.getAction(), intent2);
        }
    }
}
