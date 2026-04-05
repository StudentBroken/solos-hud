package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.ChannelApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzfq extends zzfc<ChannelApi.OpenChannelResult> {
    public zzfq(zzbcl<ChannelApi.OpenChannelResult> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzei zzeiVar) {
        zzR(new zzaf(zzev.zzaY(zzeiVar.statusCode), zzeiVar.zzbSl));
    }
}
