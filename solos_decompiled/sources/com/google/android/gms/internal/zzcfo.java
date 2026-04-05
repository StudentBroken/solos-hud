package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.measurement.AppMeasurement;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcfo extends zzciu {
    private final Map<String, Long> zzbou;
    private final Map<String, Integer> zzbov;
    private long zzbow;

    public zzcfo(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbov = new ArrayMap();
        this.zzbou = new ArrayMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzK(long j) {
        Iterator<String> it = this.zzbou.keySet().iterator();
        while (it.hasNext()) {
            this.zzbou.put(it.next(), Long.valueOf(j));
        }
        if (this.zzbou.isEmpty()) {
            return;
        }
        this.zzbow = j;
    }

    @WorkerThread
    private final void zza(long j, AppMeasurement.zzb zzbVar) {
        if (zzbVar == null) {
            super.zzwE().zzyB().log("Not logging ad exposure. No active activity");
            return;
        }
        if (j < 1000) {
            super.zzwE().zzyB().zzj("Not logging ad exposure. Less than 1000 ms. exposure", Long.valueOf(j));
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("_xt", j);
        zzcjl.zza(zzbVar, bundle);
        super.zzws().zzd("am", "_xa", bundle);
    }

    @WorkerThread
    private final void zza(String str, long j, AppMeasurement.zzb zzbVar) {
        if (zzbVar == null) {
            super.zzwE().zzyB().log("Not logging ad unit exposure. No active activity");
            return;
        }
        if (j < 1000) {
            super.zzwE().zzyB().zzj("Not logging ad unit exposure. Less than 1000 ms. exposure", Long.valueOf(j));
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("_ai", str);
        bundle.putLong("_xt", j);
        zzcjl.zza(zzbVar, bundle);
        super.zzws().zzd("am", "_xu", bundle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzd(String str, long j) {
        super.zzwo();
        super.zzjB();
        zzbr.zzcF(str);
        if (this.zzbov.isEmpty()) {
            this.zzbow = j;
        }
        Integer num = this.zzbov.get(str);
        if (num != null) {
            this.zzbov.put(str, Integer.valueOf(num.intValue() + 1));
        } else if (this.zzbov.size() >= 100) {
            super.zzwE().zzyx().log("Too many ads visible");
        } else {
            this.zzbov.put(str, 1);
            this.zzbou.put(str, Long.valueOf(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zze(String str, long j) {
        super.zzwo();
        super.zzjB();
        zzbr.zzcF(str);
        Integer num = this.zzbov.get(str);
        if (num == null) {
            super.zzwE().zzyv().zzj("Call to endAdUnitExposure for unknown ad unit id", str);
            return;
        }
        zzcjo zzcjoVarZzzf = super.zzww().zzzf();
        int iIntValue = num.intValue() - 1;
        if (iIntValue != 0) {
            this.zzbov.put(str, Integer.valueOf(iIntValue));
            return;
        }
        this.zzbov.remove(str);
        Long l = this.zzbou.get(str);
        if (l == null) {
            super.zzwE().zzyv().log("First ad unit exposure time was never set");
        } else {
            long jLongValue = j - l.longValue();
            this.zzbou.remove(str);
            zza(str, jLongValue, zzcjoVarZzzf);
        }
        if (this.zzbov.isEmpty()) {
            if (this.zzbow == 0) {
                super.zzwE().zzyv().log("First ad exposure time was never set");
            } else {
                zza(j - this.zzbow, zzcjoVarZzzf);
                this.zzbow = 0L;
            }
        }
    }

    public final void beginAdUnitExposure(String str) {
        if (str == null || str.length() == 0) {
            super.zzwE().zzyv().log("Ad unit id must be a non-empty string");
        } else {
            super.zzwD().zzj(new zzcfp(this, str, super.zzkp().elapsedRealtime()));
        }
    }

    public final void endAdUnitExposure(String str) {
        if (str == null || str.length() == 0) {
            super.zzwE().zzyv().log("Ad unit id must be a non-empty string");
        } else {
            super.zzwD().zzj(new zzcfq(this, str, super.zzkp().elapsedRealtime()));
        }
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @WorkerThread
    public final void zzJ(long j) {
        zzcjo zzcjoVarZzzf = super.zzww().zzzf();
        for (String str : this.zzbou.keySet()) {
            zza(str, j - this.zzbou.get(str).longValue(), zzcjoVarZzzf);
        }
        if (!this.zzbou.isEmpty()) {
            zza(j - this.zzbow, zzcjoVarZzzf);
        }
        zzK(j);
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
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

    public final void zzwm() {
        super.zzwD().zzj(new zzcfr(this, super.zzkp().elapsedRealtime()));
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
