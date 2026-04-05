package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
final class zzbds extends zzbdz {
    final /* synthetic */ zzbdp zzaDr;
    private final Map<Api.zze, zzbdr> zzaDt;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzbds(zzbdp zzbdpVar, Map<Api.zze, zzbdr> map) {
        super(zzbdpVar, null);
        this.zzaDr = zzbdpVar;
        this.zzaDt = map;
    }

    @Override // com.google.android.gms.internal.zzbdz
    @WorkerThread
    public final void zzpT() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = true;
        Iterator<Api.zze> it = this.zzaDt.keySet().iterator();
        boolean z5 = true;
        boolean z6 = false;
        while (true) {
            if (!it.hasNext()) {
                z4 = z6;
                z = false;
                break;
            }
            Api.zze next = it.next();
            if (!next.zzpc()) {
                z2 = false;
                z3 = z6;
            } else if (!this.zzaDt.get(next).zzaCl) {
                z = true;
                break;
            } else {
                z2 = z5;
                z3 = true;
            }
            z6 = z3;
            z5 = z2;
        }
        int iIsGooglePlayServicesAvailable = z4 ? this.zzaDr.zzaCH.isGooglePlayServicesAvailable(this.zzaDr.mContext) : 0;
        if (iIsGooglePlayServicesAvailable != 0 && (z || z5)) {
            this.zzaDr.zzaDb.zza(new zzbdt(this, this.zzaDr, new ConnectionResult(iIsGooglePlayServicesAvailable, null)));
            return;
        }
        if (this.zzaDr.zzaDl) {
            this.zzaDr.zzaDj.connect();
        }
        for (Api.zze zzeVar : this.zzaDt.keySet()) {
            zzbdr zzbdrVar = this.zzaDt.get(zzeVar);
            if (!zzeVar.zzpc() || iIsGooglePlayServicesAvailable == 0) {
                zzeVar.zza(zzbdrVar);
            } else {
                this.zzaDr.zzaDb.zza(new zzbdu(this, this.zzaDr, zzbdrVar));
            }
        }
    }
}
