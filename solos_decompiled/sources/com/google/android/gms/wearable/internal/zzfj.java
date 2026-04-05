package com.google.android.gms.wearable.internal;

import android.os.ParcelFileDescriptor;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.Channel;

/* JADX INFO: loaded from: classes6.dex */
final class zzfj extends zzfc<Channel.GetOutputStreamResult> {
    private final zzbd zzbTh;

    public zzfj(zzbcl<Channel.GetOutputStreamResult> zzbclVar, zzbd zzbdVar) {
        super(zzbclVar);
        this.zzbTh = (zzbd) com.google.android.gms.common.internal.zzbr.zzu(zzbdVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzcm zzcmVar) {
        zzax zzaxVar = null;
        if (zzcmVar.zzbSK != null) {
            zzaxVar = new zzax(new ParcelFileDescriptor.AutoCloseOutputStream(zzcmVar.zzbSK));
            this.zzbTh.zza(new zzay(zzaxVar));
        }
        zzR(new zzat(new Status(zzcmVar.statusCode), zzaxVar));
    }
}
