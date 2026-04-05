package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataItem;

/* JADX INFO: loaded from: classes6.dex */
public final class zzbw extends com.google.android.gms.common.data.zzc implements DataEvent {
    private final int zzbcT;

    public zzbw(DataHolder dataHolder, int i, int i2) {
        super(dataHolder, i);
        this.zzbcT = i2;
    }

    @Override // com.google.android.gms.common.data.Freezable
    public final /* synthetic */ DataEvent freeze() {
        return new zzbv(this);
    }

    @Override // com.google.android.gms.wearable.DataEvent
    public final DataItem getDataItem() {
        return new zzcd(this.zzaCZ, this.zzaFz, this.zzbcT);
    }

    @Override // com.google.android.gms.wearable.DataEvent
    public final int getType() {
        return getInteger("event_type");
    }

    public final String toString() {
        String str = getType() == 1 ? "changed" : getType() == 2 ? "deleted" : "unknown";
        String strValueOf = String.valueOf(getDataItem());
        return new StringBuilder(String.valueOf(str).length() + 32 + String.valueOf(strValueOf).length()).append("DataEventRef{ type=").append(str).append(", dataitem=").append(strValueOf).append(" }").toString();
    }
}
