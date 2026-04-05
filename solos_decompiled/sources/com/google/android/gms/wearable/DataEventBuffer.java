package com.google.android.gms.wearable;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.wearable.internal.zzbw;

/* JADX INFO: loaded from: classes6.dex */
public class DataEventBuffer extends com.google.android.gms.common.data.zzg<DataEvent> implements Result {
    private final Status mStatus;

    public DataEventBuffer(DataHolder dataHolder) {
        super(dataHolder);
        this.mStatus = new Status(dataHolder.getStatusCode());
    }

    @Override // com.google.android.gms.common.api.Result
    public Status getStatus() {
        return this.mStatus;
    }

    @Override // com.google.android.gms.common.data.zzg
    protected final /* synthetic */ DataEvent zzi(int i, int i2) {
        return new zzbw(this.zzaCZ, i, i2);
    }

    @Override // com.google.android.gms.common.data.zzg
    protected final String zzqQ() {
        return "path";
    }
}
