package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.DataApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzff extends zzfc<DataApi.DeleteDataItemsResult> {
    public zzff(zzbcl<DataApi.DeleteDataItemsResult> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzce zzceVar) {
        zzR(new zzbt(zzev.zzaY(zzceVar.statusCode), zzceVar.zzbSH));
    }
}
