package com.google.android.gms.measurement;

import android.app.Service;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.MainThread;
import com.google.android.gms.internal.zzcki;
import com.google.android.gms.internal.zzckl;

/* JADX INFO: loaded from: classes70.dex */
public final class AppMeasurementService extends Service implements zzckl {
    private zzcki zzbot;

    private final zzcki zzwl() {
        if (this.zzbot == null) {
            this.zzbot = new zzcki(this);
        }
        return this.zzbot;
    }

    @Override // com.google.android.gms.internal.zzckl
    public final boolean callServiceStopSelfResult(int i) {
        return stopSelfResult(i);
    }

    @Override // com.google.android.gms.internal.zzckl
    public final Context getContext() {
        return this;
    }

    @Override // android.app.Service
    @MainThread
    public final IBinder onBind(Intent intent) {
        return zzwl().onBind(intent);
    }

    @Override // android.app.Service
    @MainThread
    public final void onCreate() {
        super.onCreate();
        zzwl().onCreate();
    }

    @Override // android.app.Service
    @MainThread
    public final void onDestroy() {
        zzwl().onDestroy();
        super.onDestroy();
    }

    @Override // android.app.Service
    @MainThread
    public final void onRebind(Intent intent) {
        zzwl().onRebind(intent);
    }

    @Override // android.app.Service
    @MainThread
    public final int onStartCommand(Intent intent, int i, int i2) {
        zzwl().onStartCommand(intent, i, i2);
        AppMeasurementReceiver.completeWakefulIntent(intent);
        return 2;
    }

    @Override // android.app.Service
    @MainThread
    public final boolean onUnbind(Intent intent) {
        return zzwl().onUnbind(intent);
    }

    @Override // com.google.android.gms.internal.zzckl
    public final void zza(JobParameters jobParameters, boolean z) {
        throw new UnsupportedOperationException();
    }
}
