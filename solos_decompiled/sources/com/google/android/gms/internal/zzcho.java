package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MainThread;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcho {
    private final zzchq zzbrP;

    public zzcho(zzchq zzchqVar) {
        zzbr.zzu(zzchqVar);
        this.zzbrP = zzchqVar;
    }

    public static boolean zzj(Context context, boolean z) {
        zzbr.zzu(context);
        return zzckx.zza(context, "com.google.android.gms.measurement.AppMeasurementReceiver", false);
    }

    @MainThread
    public final void onReceive(Context context, Intent intent) {
        zzchx zzchxVarZzbj = zzchx.zzbj(context);
        zzcgx zzcgxVarZzwE = zzchxVarZzbj.zzwE();
        if (intent == null) {
            zzcgxVarZzwE.zzyx().log("Receiver called with null intent");
            return;
        }
        zzcfy.zzxD();
        String action = intent.getAction();
        zzcgxVarZzwE.zzyB().zzj("Local receiver got", action);
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            zzcki.zzk(context, false);
            Intent className = new Intent().setClassName(context, "com.google.android.gms.measurement.AppMeasurementService");
            className.setAction("com.google.android.gms.measurement.UPLOAD");
            this.zzbrP.doStartService(context, className);
            return;
        }
        if ("com.android.vending.INSTALL_REFERRER".equals(action)) {
            String stringExtra = intent.getStringExtra("referrer");
            if (stringExtra == null) {
                zzcgxVarZzwE.zzyB().log("Install referrer extras are null");
                return;
            }
            zzcgxVarZzwE.zzyz().zzj("Install referrer extras are", stringExtra);
            if (!stringExtra.contains("?")) {
                String strValueOf = String.valueOf(stringExtra);
                stringExtra = strValueOf.length() != 0 ? "?".concat(strValueOf) : new String("?");
            }
            Bundle bundleZzq = zzchxVarZzbj.zzwA().zzq(Uri.parse(stringExtra));
            if (bundleZzq == null) {
                zzcgxVarZzwE.zzyB().log("No campaign defined in install referrer broadcast");
                return;
            }
            long longExtra = 1000 * intent.getLongExtra("referrer_timestamp_seconds", 0L);
            if (longExtra == 0) {
                zzcgxVarZzwE.zzyx().log("Install referrer is missing timestamp");
            }
            zzchxVarZzbj.zzwD().zzj(new zzchp(this, zzchxVarZzbj, longExtra, bundleZzq, context, zzcgxVarZzwE));
        }
    }
}
