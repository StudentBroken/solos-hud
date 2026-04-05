package com.google.android.gms.internal;

import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;

/* JADX INFO: loaded from: classes3.dex */
public class zzbdk implements Releasable, Result {
    private Status mStatus;
    protected final DataHolder zzaCZ;

    protected zzbdk(DataHolder dataHolder, Status status) {
        this.mStatus = status;
        this.zzaCZ = dataHolder;
    }

    @Override // com.google.android.gms.common.api.Result
    public Status getStatus() {
        return this.mStatus;
    }

    @Override // com.google.android.gms.common.api.Releasable
    public void release() {
        if (this.zzaCZ != null) {
            this.zzaCZ.close();
        }
    }
}
