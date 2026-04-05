package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.DataItemBuffer;

/* JADX INFO: loaded from: classes6.dex */
final class zzfm extends zzfc<DataItemBuffer> {
    public zzfm(zzbcl<DataItemBuffer> zzbclVar) {
        super(zzbclVar);
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zzT(DataHolder dataHolder) {
        zzR(new DataItemBuffer(dataHolder));
    }
}
