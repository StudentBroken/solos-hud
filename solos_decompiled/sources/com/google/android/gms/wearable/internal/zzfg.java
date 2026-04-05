package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.CapabilityApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzfg extends zzfc<CapabilityApi.GetAllCapabilitiesResult> {
    public zzfg(zzbcl<CapabilityApi.GetAllCapabilitiesResult> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzcg zzcgVar) {
        zzR(new zzx(zzev.zzaY(zzcgVar.statusCode), zzfa.zzN(zzcgVar.zzbSI)));
    }
}
