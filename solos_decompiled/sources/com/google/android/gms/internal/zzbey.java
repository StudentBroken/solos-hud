package com.google.android.gms.internal;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.google.android.gms.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbg;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.internal.zzcb;

/* JADX INFO: loaded from: classes67.dex */
@Deprecated
public final class zzbey {
    private static zzbey zzaED;
    private static final Object zzuI = new Object();
    private final String mAppId;
    private final Status zzaEE;
    private final boolean zzaEF;
    private final boolean zzaEG;

    private zzbey(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("google_app_measurement_enable", "integer", resources.getResourcePackageName(R.string.common_google_play_services_unknown_issue));
        if (identifier != 0) {
            boolean z = resources.getInteger(identifier) != 0;
            this.zzaEG = z ? false : true;
            z = z;
        } else {
            this.zzaEG = false;
        }
        this.zzaEF = z;
        String strZzaD = zzbg.zzaD(context);
        strZzaD = strZzaD == null ? new zzcb(context).getString("google_app_id") : strZzaD;
        if (TextUtils.isEmpty(strZzaD)) {
            this.zzaEE = new Status(10, "Missing google app id value from from string resources with name google_app_id.");
            this.mAppId = null;
        } else {
            this.mAppId = strZzaD;
            this.zzaEE = Status.zzaBo;
        }
    }

    public static Status zzaz(Context context) {
        Status status;
        zzbr.zzb(context, "Context must not be null.");
        synchronized (zzuI) {
            if (zzaED == null) {
                zzaED = new zzbey(context);
            }
            status = zzaED.zzaEE;
        }
        return status;
    }

    private static zzbey zzcu(String str) {
        zzbey zzbeyVar;
        synchronized (zzuI) {
            if (zzaED == null) {
                throw new IllegalStateException(new StringBuilder(String.valueOf(str).length() + 34).append("Initialize must be called before ").append(str).append(".").toString());
            }
            zzbeyVar = zzaED;
        }
        return zzbeyVar;
    }

    public static String zzqy() {
        return zzcu("getGoogleAppId").mAppId;
    }

    public static boolean zzqz() {
        return zzcu("isMeasurementExplicitlyDisabled").zzaEG;
    }
}
