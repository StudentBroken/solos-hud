package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.measurement.AppMeasurement;

/* JADX INFO: loaded from: classes36.dex */
final class zzchp implements Runnable {
    private /* synthetic */ zzchx zzbrQ;
    private /* synthetic */ long zzbrR;
    private /* synthetic */ Bundle zzbrS;
    private /* synthetic */ zzcgx zzbrT;
    private /* synthetic */ Context zztI;

    zzchp(zzcho zzchoVar, zzchx zzchxVar, long j, Bundle bundle, Context context, zzcgx zzcgxVar) {
        this.zzbrQ = zzchxVar;
        this.zzbrR = j;
        this.zzbrS = bundle;
        this.zztI = context;
        this.zzbrT = zzcgxVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzckw zzckwVarZzG = this.zzbrQ.zzwy().zzG(this.zzbrQ.zzwt().zzhk(), "_fot");
        long jLongValue = (zzckwVarZzG == null || !(zzckwVarZzG.mValue instanceof Long)) ? 0L : ((Long) zzckwVarZzG.mValue).longValue();
        long j = this.zzbrR;
        long j2 = (jLongValue <= 0 || (j < jLongValue && j > 0)) ? j : jLongValue - 1;
        if (j2 > 0) {
            this.zzbrS.putLong("click_timestamp", j2);
        }
        AppMeasurement.getInstance(this.zztI).logEventInternal("auto", "_cmp", this.zzbrS);
        this.zzbrT.zzyB().log("Install campaign recorded");
    }
}
