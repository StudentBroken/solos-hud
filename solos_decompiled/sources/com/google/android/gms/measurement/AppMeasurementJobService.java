package com.google.android.gms.measurement;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import com.google.android.gms.internal.zzcki;
import com.google.android.gms.internal.zzckl;

/* JADX INFO: loaded from: classes70.dex */
@TargetApi(24)
public final class AppMeasurementJobService extends JobService implements zzckl {
    private zzcki zzbot;

    private final zzcki zzwl() {
        if (this.zzbot == null) {
            this.zzbot = new zzcki(this);
        }
        return this.zzbot;
    }

    @Override // com.google.android.gms.internal.zzckl
    public final boolean callServiceStopSelfResult(int i) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.internal.zzckl
    public final Context getContext() {
        return this;
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

    @Override // android.app.job.JobService
    public final boolean onStartJob(JobParameters jobParameters) {
        return zzwl().onStartJob(jobParameters);
    }

    @Override // android.app.job.JobService
    public final boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override // android.app.Service
    @MainThread
    public final boolean onUnbind(Intent intent) {
        return zzwl().onUnbind(intent);
    }

    @Override // com.google.android.gms.internal.zzckl
    @TargetApi(24)
    public final void zza(JobParameters jobParameters, boolean z) {
        jobFinished(jobParameters, false);
    }
}
