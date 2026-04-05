package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.internal.zzca;
import java.util.Iterator;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbdm implements zzbei {
    private final zzbej zzaDb;
    private boolean zzaDc = false;

    public zzbdm(zzbej zzbejVar) {
        this.zzaDb = zzbejVar;
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void begin() {
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void connect() {
        if (this.zzaDc) {
            this.zzaDc = false;
            this.zzaDb.zza(new zzbdo(this, this));
        }
    }

    @Override // com.google.android.gms.internal.zzbei
    public final boolean disconnect() {
        if (this.zzaDc) {
            return false;
        }
        if (!this.zzaDb.zzaCn.zzqd()) {
            this.zzaDb.zzg(null);
            return true;
        }
        this.zzaDc = true;
        Iterator<zzbge> it = this.zzaDb.zzaCn.zzaDM.iterator();
        while (it.hasNext()) {
            it.next().zzqI();
        }
        return false;
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void onConnected(Bundle bundle) {
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void onConnectionSuspended(int i) {
        this.zzaDb.zzg(null);
        this.zzaDb.zzaEa.zze(i, this.zzaDc);
    }

    @Override // com.google.android.gms.internal.zzbei
    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
    }

    @Override // com.google.android.gms.internal.zzbei
    public final <A extends Api.zzb, R extends Result, T extends zzbck<R, A>> T zzd(T t) {
        return (T) zze(t);
    }

    @Override // com.google.android.gms.internal.zzbei
    public final <A extends Api.zzb, T extends zzbck<? extends Result, A>> T zze(T t) {
        try {
            this.zzaDb.zzaCn.zzaDN.zzb(t);
            zzbeb zzbebVar = this.zzaDb.zzaCn;
            Api.zze zzeVar = zzbebVar.zzaDH.get(t.zzpb());
            zzbr.zzb(zzeVar, "Appropriate Api was not requested.");
            if (zzeVar.isConnected() || !this.zzaDb.zzaDW.containsKey(t.zzpb())) {
                if (zzeVar instanceof zzca) {
                    zzeVar = null;
                }
                t.zzb(zzeVar);
            } else {
                t.zzr(new Status(17));
            }
        } catch (DeadObjectException e) {
            this.zzaDb.zza(new zzbdn(this, this));
        }
        return t;
    }

    final void zzpS() {
        if (this.zzaDc) {
            this.zzaDc = false;
            this.zzaDb.zzaCn.zzaDN.release();
            disconnect();
        }
    }
}
