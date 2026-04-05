package com.google.android.gms.internal;

import android.os.Build;

/* JADX INFO: loaded from: classes36.dex */
final class zzckk implements Runnable {
    private /* synthetic */ zzckj zzbuu;

    zzckk(zzckj zzckjVar) {
        this.zzbuu = zzckjVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.zzbuu.zzbur != null) {
            if (this.zzbuu.zzbut.zzbuq.callServiceStopSelfResult(this.zzbuu.zzbur.intValue())) {
                zzcfy.zzxD();
                this.zzbuu.zzbrT.zzyB().zzj("Local AppMeasurementService processed last upload request. StartId", this.zzbuu.zzbur);
                return;
            }
            return;
        }
        zzcfy.zzxD();
        if (Build.VERSION.SDK_INT >= 24) {
            this.zzbuu.zzbrT.zzyB().log("AppMeasurementJobService processed last upload request.");
            this.zzbuu.zzbut.zzbuq.zza(this.zzbuu.zzbus, false);
        }
    }
}
