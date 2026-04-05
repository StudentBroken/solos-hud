package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;

/* JADX INFO: loaded from: classes6.dex */
final class zzfe extends zzfc<Status> {
    public zzfe(zzbcl<Status> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zzb(zzbf zzbfVar) {
        zzR(new Status(zzbfVar.statusCode));
    }
}
