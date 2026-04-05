package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;
import com.facebook.internal.NativeProtocol;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcks extends zzciv {
    private final AlarmManager zzahf;
    private Integer zzbuA;
    private final zzcgd zzbuz;

    protected zzcks(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzahf = (AlarmManager) super.getContext().getSystemService(NotificationCompat.CATEGORY_ALARM);
        this.zzbuz = new zzckt(this, zzchxVar);
    }

    private final int getJobId() {
        if (this.zzbuA == null) {
            String strValueOf = String.valueOf(super.getContext().getPackageName());
            this.zzbuA = Integer.valueOf((strValueOf.length() != 0 ? "measurement".concat(strValueOf) : new String("measurement")).hashCode());
        }
        return this.zzbuA.intValue();
    }

    private final PendingIntent zzlC() {
        Intent intent = new Intent();
        Context context = super.getContext();
        zzcfy.zzxD();
        Intent className = intent.setClassName(context, "com.google.android.gms.measurement.AppMeasurementReceiver");
        className.setAction("com.google.android.gms.measurement.UPLOAD");
        return PendingIntent.getBroadcast(super.getContext(), 0, className, 0);
    }

    @TargetApi(24)
    private final void zzzo() {
        JobScheduler jobScheduler = (JobScheduler) super.getContext().getSystemService("jobscheduler");
        super.zzwE().zzyB().zzj("Cancelling job. JobID", Integer.valueOf(getJobId()));
        jobScheduler.cancel(getJobId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzzp() {
        Intent intent = new Intent();
        Context context = super.getContext();
        zzcfy.zzxD();
        Intent className = intent.setClassName(context, "com.google.android.gms.measurement.AppMeasurementReceiver");
        className.setAction("com.google.android.gms.measurement.UPLOAD");
        super.getContext().sendBroadcast(className);
    }

    public final void cancel() {
        zzkC();
        this.zzahf.cancel(zzlC());
        this.zzbuz.cancel();
        if (Build.VERSION.SDK_INT >= 24) {
            zzzo();
        }
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
        this.zzahf.cancel(zzlC());
        if (Build.VERSION.SDK_INT >= 24) {
            zzzo();
        }
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
    }

    public final void zzs(long j) {
        zzkC();
        zzcfy.zzxD();
        if (!zzcho.zzj(super.getContext(), false)) {
            super.zzwE().zzyA().log("Receiver not registered/enabled");
        }
        zzcfy.zzxD();
        if (!zzcki.zzk(super.getContext(), false)) {
            super.zzwE().zzyA().log("Service not registered/enabled");
        }
        cancel();
        long jElapsedRealtime = super.zzkp().elapsedRealtime() + j;
        if (j < zzcfy.zzxU() && !this.zzbuz.zzbn()) {
            super.zzwE().zzyB().log("Scheduling upload with DelayedRunnable");
            this.zzbuz.zzs(j);
        }
        zzcfy.zzxD();
        if (Build.VERSION.SDK_INT < 24) {
            super.zzwE().zzyB().log("Scheduling upload with AlarmManager");
            this.zzahf.setInexactRepeating(2, jElapsedRealtime, Math.max(zzcfy.zzxV(), j), zzlC());
            return;
        }
        super.zzwE().zzyB().log("Scheduling upload with JobScheduler");
        ComponentName componentName = new ComponentName(super.getContext(), "com.google.android.gms.measurement.AppMeasurementJobService");
        JobScheduler jobScheduler = (JobScheduler) super.getContext().getSystemService("jobscheduler");
        JobInfo.Builder builder = new JobInfo.Builder(getJobId(), componentName);
        builder.setMinimumLatency(j);
        builder.setOverrideDeadline(j << 1);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString(NativeProtocol.WEB_DIALOG_ACTION, "com.google.android.gms.measurement.UPLOAD");
        builder.setExtras(persistableBundle);
        JobInfo jobInfoBuild = builder.build();
        super.zzwE().zzyB().zzj("Scheduling job. JobID", Integer.valueOf(getJobId()));
        jobScheduler.schedule(jobInfoBuild);
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckx zzwA() {
        return super.zzwA();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchr zzwB() {
        return super.zzwB();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckm zzwC() {
        return super.zzwC();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchs zzwD() {
        return super.zzwD();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgx zzwE() {
        return super.zzwE();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchi zzwF() {
        return super.zzwF();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfy zzwG() {
        return super.zzwG();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwn() {
        super.zzwn();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwo() {
        super.zzwo();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwp() {
        super.zzwp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfo zzwq() {
        return super.zzwq();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfv zzwr() {
        return super.zzwr();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcix zzws() {
        return super.zzws();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgs zzwt() {
        return super.zzwt();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgf zzwu() {
        return super.zzwu();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjp zzwv() {
        return super.zzwv();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjl zzww() {
        return super.zzww();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgt zzwx() {
        return super.zzwx();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfz zzwy() {
        return super.zzwy();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgv zzwz() {
        return super.zzwz();
    }
}
