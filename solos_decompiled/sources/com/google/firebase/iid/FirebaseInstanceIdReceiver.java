package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Base64;
import android.util.Log;

/* JADX INFO: loaded from: classes35.dex */
public final class FirebaseInstanceIdReceiver extends WakefulBroadcastReceiver {
    private static boolean zzbfF = false;

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        String str = null;
        int iZza = -1;
        intent.setComponent(null);
        intent.setPackage(context.getPackageName());
        if (Build.VERSION.SDK_INT <= 18) {
            intent.removeCategory(context.getPackageName());
        }
        String stringExtra = intent.getStringExtra("gcm.rawData64");
        if (stringExtra != null) {
            intent.putExtra("rawData", Base64.decode(stringExtra, 0));
            intent.removeExtra("gcm.rawData64");
        }
        String stringExtra2 = intent.getStringExtra("from");
        if ("google.com/iid".equals(stringExtra2) || "gcm.googleapis.com/refresh".equals(stringExtra2)) {
            str = "com.google.firebase.INSTANCE_ID_EVENT";
        } else if ("com.google.android.c2dm.intent.RECEIVE".equals(intent.getAction())) {
            str = "com.google.firebase.MESSAGING_EVENT";
        } else {
            Log.d("FirebaseInstanceId", "Unexpected intent");
        }
        if (str != null) {
            if (FirebaseInstanceIdInternalReceiver.zzbH(context)) {
                if (isOrderedBroadcast()) {
                    setResultCode(-1);
                }
                FirebaseInstanceIdInternalReceiver.zzI(context, str).zza(intent, goAsync());
            } else {
                iZza = zzq.zzKm().zza(context, str, intent);
            }
        }
        if (isOrderedBroadcast()) {
            setResultCode(iZza);
        }
    }
}
