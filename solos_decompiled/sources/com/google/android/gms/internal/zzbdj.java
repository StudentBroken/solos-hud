package com.google.android.gms.internal;

import com.google.android.gms.common.data.DataHolder;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzbdj<L> implements zzbfl<L> {
    private final DataHolder zzaCZ;

    protected zzbdj(DataHolder dataHolder) {
        this.zzaCZ = dataHolder;
    }

    protected abstract void zza(L l, DataHolder dataHolder);

    @Override // com.google.android.gms.internal.zzbfl
    public final void zzpR() {
        if (this.zzaCZ != null) {
            this.zzaCZ.close();
        }
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final void zzq(L l) {
        zza(l, this.zzaCZ);
    }
}
