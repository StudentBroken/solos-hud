package com.google.android.gms.common.internal;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.IInterface;
import android.support.annotation.BinderThread;
import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes67.dex */
abstract class zze extends zzi<Boolean> {
    private int statusCode;
    private Bundle zzaHf;
    private /* synthetic */ zzd zzaHg;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    @BinderThread
    protected zze(zzd zzdVar, int i, Bundle bundle) {
        super(zzdVar, true);
        this.zzaHg = zzdVar;
        this.statusCode = i;
        this.zzaHf = bundle;
    }

    protected abstract void zzj(ConnectionResult connectionResult);

    protected abstract boolean zzrh();

    @Override // com.google.android.gms.common.internal.zzi
    protected final /* synthetic */ void zzs(Boolean bool) {
        if (bool == null) {
            this.zzaHg.zza(1, (IInterface) null);
            return;
        }
        switch (this.statusCode) {
            case 0:
                if (zzrh()) {
                    return;
                }
                this.zzaHg.zza(1, (IInterface) null);
                zzj(new ConnectionResult(8, null));
                return;
            case 10:
                this.zzaHg.zza(1, (IInterface) null);
                throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
            default:
                this.zzaHg.zza(1, (IInterface) null);
                zzj(new ConnectionResult(this.statusCode, this.zzaHf != null ? (PendingIntent) this.zzaHf.getParcelable("pendingIntent") : null));
                return;
        }
    }
}
