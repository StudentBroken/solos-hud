package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.DataApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzfl extends zzfc<DataApi.DataItemResult> {
    public zzfl(zzbcl<DataApi.DataItemResult> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzda zzdaVar) {
        zzR(new zzbs(zzev.zzaY(zzdaVar.statusCode), zzdaVar.zzbSR));
    }
}
