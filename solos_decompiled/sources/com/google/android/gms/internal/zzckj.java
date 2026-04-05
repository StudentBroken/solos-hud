package com.google.android.gms.internal;

import android.app.job.JobParameters;

/* JADX INFO: loaded from: classes36.dex */
final class zzckj implements Runnable {
    private /* synthetic */ zzchx zzbrQ;
    final /* synthetic */ zzcgx zzbrT;
    final /* synthetic */ Integer zzbur;
    final /* synthetic */ JobParameters zzbus;
    final /* synthetic */ zzcki zzbut;

    zzckj(zzcki zzckiVar, zzchx zzchxVar, Integer num, zzcgx zzcgxVar, JobParameters jobParameters) {
        this.zzbut = zzckiVar;
        this.zzbrQ = zzchxVar;
        this.zzbur = num;
        this.zzbrT = zzcgxVar;
        this.zzbus = jobParameters;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzbrQ.zzzc();
        this.zzbrQ.zzl(new zzckk(this));
        this.zzbrQ.zzyY();
    }
}
