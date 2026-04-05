package com.google.android.gms.internal;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
final class zzbet implements com.google.android.gms.common.internal.zzj, zzbfx {
    private final zzbcf<?> zzaAM;
    private final Api.zze zzaCA;
    final /* synthetic */ zzben zzaEo;
    private com.google.android.gms.common.internal.zzam zzaDn = null;
    private Set<Scope> zzamg = null;
    private boolean zzaEz = false;

    public zzbet(zzben zzbenVar, Api.zze zzeVar, zzbcf<?> zzbcfVar) {
        this.zzaEo = zzbenVar;
        this.zzaCA = zzeVar;
        this.zzaAM = zzbcfVar;
    }

    static /* synthetic */ boolean zza(zzbet zzbetVar, boolean z) {
        zzbetVar.zzaEz = true;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzqx() {
        if (!this.zzaEz || this.zzaDn == null) {
            return;
        }
        this.zzaCA.zza(this.zzaDn, this.zzamg);
    }

    @Override // com.google.android.gms.internal.zzbfx
    @WorkerThread
    public final void zzb(com.google.android.gms.common.internal.zzam zzamVar, Set<Scope> set) {
        if (zzamVar == null || set == null) {
            Log.wtf("GoogleApiManager", "Received null response from onSignInSuccess", new Exception());
            zzh(new ConnectionResult(4));
        } else {
            this.zzaDn = zzamVar;
            this.zzamg = set;
            zzqx();
        }
    }

    @Override // com.google.android.gms.common.internal.zzj
    public final void zzf(@NonNull ConnectionResult connectionResult) {
        this.zzaEo.mHandler.post(new zzbeu(this, connectionResult));
    }

    @Override // com.google.android.gms.internal.zzbfx
    @WorkerThread
    public final void zzh(ConnectionResult connectionResult) {
        ((zzbep) this.zzaEo.zzaCD.get(this.zzaAM)).zzh(connectionResult);
    }
}
