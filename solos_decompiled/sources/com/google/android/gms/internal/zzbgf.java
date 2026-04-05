package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;

/* JADX INFO: loaded from: classes3.dex */
final class zzbgf implements Runnable {
    private /* synthetic */ Result zzaFj;
    private /* synthetic */ zzbge zzaFk;

    zzbgf(zzbge zzbgeVar, Result result) {
        this.zzaFk = zzbgeVar;
        this.zzaFj = result;
    }

    @Override // java.lang.Runnable
    @WorkerThread
    public final void run() {
        try {
            try {
                zzbcq.zzaBX.set(true);
                this.zzaFk.zzaFh.sendMessage(this.zzaFk.zzaFh.obtainMessage(0, this.zzaFk.zzaFc.onSuccess(this.zzaFj)));
                zzbcq.zzaBX.set(false);
                zzbge zzbgeVar = this.zzaFk;
                zzbge.zzc(this.zzaFj);
                GoogleApiClient googleApiClient = (GoogleApiClient) this.zzaFk.zzaCa.get();
                if (googleApiClient != null) {
                    googleApiClient.zzb(this.zzaFk);
                }
            } catch (RuntimeException e) {
                this.zzaFk.zzaFh.sendMessage(this.zzaFk.zzaFh.obtainMessage(1, e));
                zzbcq.zzaBX.set(false);
                zzbge zzbgeVar2 = this.zzaFk;
                zzbge.zzc(this.zzaFj);
                GoogleApiClient googleApiClient2 = (GoogleApiClient) this.zzaFk.zzaCa.get();
                if (googleApiClient2 != null) {
                    googleApiClient2.zzb(this.zzaFk);
                }
            }
        } finally {
        }
    }
}
