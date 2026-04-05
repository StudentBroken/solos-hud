package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.measurement.AppMeasurement;

/* JADX INFO: loaded from: classes36.dex */
final class zzcjm implements Runnable {
    private /* synthetic */ boolean zzbtR;
    private /* synthetic */ AppMeasurement.zzb zzbtS;
    private /* synthetic */ zzcjo zzbtT;
    private /* synthetic */ zzcjl zzbtU;

    zzcjm(zzcjl zzcjlVar, boolean z, AppMeasurement.zzb zzbVar, zzcjo zzcjoVar) {
        this.zzbtU = zzcjlVar;
        this.zzbtR = z;
        this.zzbtS = zzbVar;
        this.zzbtT = zzcjoVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.zzbtR && this.zzbtU.zzbtI != null) {
            this.zzbtU.zza(this.zzbtU.zzbtI);
        }
        if ((this.zzbtS != null && this.zzbtS.zzbop == this.zzbtT.zzbop && zzckx.zzR(this.zzbtS.zzboo, this.zzbtT.zzboo) && zzckx.zzR(this.zzbtS.zzbon, this.zzbtT.zzbon)) ? false : true) {
            Bundle bundle = new Bundle();
            zzcjl.zza(this.zzbtT, bundle);
            if (this.zzbtS != null) {
                if (this.zzbtS.zzbon != null) {
                    bundle.putString("_pn", this.zzbtS.zzbon);
                }
                bundle.putString("_pc", this.zzbtS.zzboo);
                bundle.putLong("_pi", this.zzbtS.zzbop);
            }
            this.zzbtU.zzws().zzd("auto", "_vs", bundle);
        }
        this.zzbtU.zzbtI = this.zzbtT;
        this.zzbtU.zzwv().zza(this.zzbtT);
    }
}
