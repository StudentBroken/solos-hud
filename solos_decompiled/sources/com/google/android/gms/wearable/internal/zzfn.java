package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.DataApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzfn extends zzfc<DataApi.GetFdForAssetResult> {
    public zzfn(zzbcl<DataApi.GetFdForAssetResult> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzdc zzdcVar) {
        zzR(new zzbu(zzev.zzaY(zzdcVar.statusCode), zzdcVar.zzbwA));
    }
}
