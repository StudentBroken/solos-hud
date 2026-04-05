package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.MainThread;
import com.facebook.internal.NativeProtocol;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcki {
    private final Context mContext;
    private final zzckl zzbuq;

    public zzcki(zzckl zzcklVar) {
        this.mContext = zzcklVar.getContext();
        zzbr.zzu(this.mContext);
        this.zzbuq = zzcklVar;
    }

    private final void zza(Integer num, JobParameters jobParameters) {
        zzchx zzchxVarZzbj = zzchx.zzbj(this.mContext);
        zzchxVarZzbj.zzwD().zzj(new zzckj(this, zzchxVarZzbj, num, zzchxVarZzbj.zzwE(), jobParameters));
    }

    public static boolean zzk(Context context, boolean z) {
        zzbr.zzu(context);
        return Build.VERSION.SDK_INT >= 24 ? zzckx.zzw(context, "com.google.android.gms.measurement.AppMeasurementJobService") : zzckx.zzw(context, "com.google.android.gms.measurement.AppMeasurementService");
    }

    private final zzcgx zzwE() {
        return zzchx.zzbj(this.mContext).zzwE();
    }

    @MainThread
    public final IBinder onBind(Intent intent) {
        if (intent == null) {
            zzwE().zzyv().log("onBind called with null intent");
            return null;
        }
        String action = intent.getAction();
        if ("com.google.android.gms.measurement.START".equals(action)) {
            return new zzcic(zzchx.zzbj(this.mContext));
        }
        zzwE().zzyx().zzj("onBind received unknown action", action);
        return null;
    }

    @MainThread
    public final void onCreate() {
        zzcgx zzcgxVarZzwE = zzchx.zzbj(this.mContext).zzwE();
        zzcfy.zzxD();
        zzcgxVarZzwE.zzyB().log("Local AppMeasurementService is starting up");
    }

    @MainThread
    public final void onDestroy() {
        zzcgx zzcgxVarZzwE = zzchx.zzbj(this.mContext).zzwE();
        zzcfy.zzxD();
        zzcgxVarZzwE.zzyB().log("Local AppMeasurementService is shutting down");
    }

    @MainThread
    public final void onRebind(Intent intent) {
        if (intent == null) {
            zzwE().zzyv().log("onRebind called with null intent");
        } else {
            zzwE().zzyB().zzj("onRebind called. action", intent.getAction());
        }
    }

    @MainThread
    public final int onStartCommand(Intent intent, int i, int i2) {
        zzcgx zzcgxVarZzwE = zzchx.zzbj(this.mContext).zzwE();
        if (intent == null) {
            zzcgxVarZzwE.zzyx().log("AppMeasurementService started with null intent");
        } else {
            String action = intent.getAction();
            zzcfy.zzxD();
            zzcgxVarZzwE.zzyB().zze("Local AppMeasurementService called. startId, action", Integer.valueOf(i2), action);
            if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
                zza(Integer.valueOf(i2), null);
            }
        }
        return 2;
    }

    @TargetApi(24)
    @MainThread
    public final boolean onStartJob(JobParameters jobParameters) {
        zzcgx zzcgxVarZzwE = zzchx.zzbj(this.mContext).zzwE();
        String string = jobParameters.getExtras().getString(NativeProtocol.WEB_DIALOG_ACTION);
        zzcfy.zzxD();
        zzcgxVarZzwE.zzyB().zzj("Local AppMeasurementJobService called. action", string);
        if (!"com.google.android.gms.measurement.UPLOAD".equals(string)) {
            return true;
        }
        zza(null, jobParameters);
        return true;
    }

    @MainThread
    public final boolean onUnbind(Intent intent) {
        if (intent == null) {
            zzwE().zzyv().log("onUnbind called with null intent");
        } else {
            zzwE().zzyB().zzj("onUnbind called for intent. action", intent.getAction());
        }
        return true;
    }
}
