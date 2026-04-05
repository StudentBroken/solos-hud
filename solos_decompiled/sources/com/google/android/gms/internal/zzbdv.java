package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.api.Api;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes3.dex */
final class zzbdv extends zzbdz {
    private /* synthetic */ zzbdp zzaDr;
    private final ArrayList<Api.zze> zzaDx;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzbdv(zzbdp zzbdpVar, ArrayList<Api.zze> arrayList) {
        super(zzbdpVar, null);
        this.zzaDr = zzbdpVar;
        this.zzaDx = arrayList;
    }

    @Override // com.google.android.gms.internal.zzbdz
    @WorkerThread
    public final void zzpT() {
        this.zzaDr.zzaDb.zzaCn.zzaDI = this.zzaDr.zzpZ();
        ArrayList<Api.zze> arrayList = this.zzaDx;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Api.zze zzeVar = arrayList.get(i);
            i++;
            zzeVar.zza(this.zzaDr.zzaDn, this.zzaDr.zzaDb.zzaCn.zzaDI);
        }
    }
}
