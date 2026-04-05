package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.NodeApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzfo extends zzfc<NodeApi.GetLocalNodeResult> {
    public zzfo(zzbcl<NodeApi.GetLocalNodeResult> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzde zzdeVar) {
        zzR(new zzef(zzev.zzaY(zzdeVar.statusCode), zzdeVar.zzbSS));
    }
}
