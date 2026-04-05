package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.MessageApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzfu extends zzfc<MessageApi.SendMessageResult> {
    public zzfu(zzbcl<MessageApi.SendMessageResult> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzes zzesVar) {
        zzR(new zzdw(zzev.zzaY(zzesVar.statusCode), zzesVar.zzbhh));
    }
}
