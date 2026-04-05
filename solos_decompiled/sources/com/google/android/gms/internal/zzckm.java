package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.WorkerThread;

/* JADX INFO: loaded from: classes36.dex */
public final class zzckm extends zzciv {
    private Handler mHandler;
    private long zzbuv;
    private final zzcgd zzbuw;
    private final zzcgd zzbux;

    zzckm(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbuw = new zzckn(this, this.zzboi);
        this.zzbux = new zzcko(this, this.zzboi);
        this.zzbuv = super.zzkp().elapsedRealtime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzae(long j) {
        super.zzjB();
        zzzl();
        this.zzbuw.cancel();
        this.zzbux.cancel();
        super.zzwE().zzyB().zzj("Activity resumed, time", Long.valueOf(j));
        this.zzbuv = j;
        if (super.zzkp().currentTimeMillis() - super.zzwF().zzbrC.get() > super.zzwF().zzbrE.get()) {
            super.zzwF().zzbrD.set(true);
            super.zzwF().zzbrF.set(0L);
        }
        if (super.zzwF().zzbrD.get()) {
            this.zzbuw.zzs(Math.max(0L, super.zzwF().zzbrB.get() - super.zzwF().zzbrF.get()));
        } else {
            this.zzbux.zzs(Math.max(0L, 3600000 - super.zzwF().zzbrF.get()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzaf(long j) {
        super.zzjB();
        zzzl();
        this.zzbuw.cancel();
        this.zzbux.cancel();
        super.zzwE().zzyB().zzj("Activity paused, time", Long.valueOf(j));
        if (this.zzbuv != 0) {
            super.zzwF().zzbrF.set(super.zzwF().zzbrF.get() + (j - this.zzbuv));
        }
    }

    private final void zzzl() {
        synchronized (this) {
            if (this.mHandler == null) {
                this.mHandler = new Handler(Looper.getMainLooper());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzzn() {
        super.zzjB();
        zzap(false);
        super.zzwq().zzJ(super.zzkp().elapsedRealtime());
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @WorkerThread
    public final boolean zzap(boolean z) {
        super.zzjB();
        zzkC();
        long jElapsedRealtime = super.zzkp().elapsedRealtime();
        super.zzwF().zzbrE.set(super.zzkp().currentTimeMillis());
        long j = jElapsedRealtime - this.zzbuv;
        if (!z && j < 1000) {
            super.zzwE().zzyB().zzj("Screen exposed for less than 1000 ms. Event not sent. time", Long.valueOf(j));
            return false;
        }
        super.zzwF().zzbrF.set(j);
        super.zzwE().zzyB().zzj("Recording user engagement, ms", Long.valueOf(j));
        Bundle bundle = new Bundle();
        bundle.putLong("_et", j);
        zzcjl.zza(super.zzww().zzzf(), bundle);
        super.zzws().zzd("auto", "_e", bundle);
        this.zzbuv = jElapsedRealtime;
        this.zzbux.cancel();
        this.zzbux.zzs(Math.max(0L, 3600000 - super.zzwF().zzbrF.get()));
        return true;
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
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

    @WorkerThread
    protected final void zzzm() {
        super.zzjB();
        super.zzwE().zzyB().zzj("Session started, time", Long.valueOf(super.zzkp().elapsedRealtime()));
        super.zzwF().zzbrD.set(false);
        super.zzws().zzd("auto", "_s", new Bundle());
        super.zzwF().zzbrE.set(super.zzkp().currentTimeMillis());
    }
}
