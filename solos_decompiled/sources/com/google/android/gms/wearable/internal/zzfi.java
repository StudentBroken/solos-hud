package com.google.android.gms.wearable.internal;

import android.os.ParcelFileDescriptor;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.Channel;

/* JADX INFO: loaded from: classes6.dex */
final class zzfi extends zzfc<Channel.GetInputStreamResult> {
    private final zzbd zzbTh;

    public zzfi(zzbcl<Channel.GetInputStreamResult> zzbclVar, zzbd zzbdVar) {
        super(zzbclVar);
        this.zzbTh = (zzbd) com.google.android.gms.common.internal.zzbr.zzu(zzbdVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzck zzckVar) {
        zzav zzavVar = null;
        if (zzckVar.zzbSK != null) {
            zzavVar = new zzav(new ParcelFileDescriptor.AutoCloseInputStream(zzckVar.zzbSK));
            this.zzbTh.zza(new zzaw(zzavVar));
        }
        zzR(new zzas(new Status(zzckVar.statusCode), zzavVar));
    }
}
