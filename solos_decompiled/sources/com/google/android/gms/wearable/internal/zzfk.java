package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.NodeApi;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes6.dex */
final class zzfk extends zzfc<NodeApi.GetConnectedNodesResult> {
    public zzfk(zzbcl<NodeApi.GetConnectedNodesResult> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzcy zzcyVar) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(zzcyVar.zzbSQ);
        zzR(new zzee(zzev.zzaY(zzcyVar.statusCode), arrayList));
    }
}
