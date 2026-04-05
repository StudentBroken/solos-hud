package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;

/* JADX INFO: loaded from: classes6.dex */
final class zzfv extends zzfc<Status> {
    public zzfv(zzbcl<Status> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzaz zzazVar) {
        zzR(new Status(zzazVar.statusCode));
    }
}
