package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.CapabilityApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzfh extends zzfc<CapabilityApi.GetCapabilityResult> {
    public zzfh(zzbcl<CapabilityApi.GetCapabilityResult> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzci zzciVar) {
        zzR(new zzy(zzev.zzaY(zzciVar.statusCode), new zzw(zzciVar.zzbSJ)));
    }
}
