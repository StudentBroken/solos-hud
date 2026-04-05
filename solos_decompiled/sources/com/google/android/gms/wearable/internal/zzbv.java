package com.google.android.gms.wearable.internal;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataItem;

/* JADX INFO: loaded from: classes6.dex */
public final class zzbv implements DataEvent {
    private int zzamt;
    private DataItem zzbSD;

    public zzbv(DataEvent dataEvent) {
        this.zzamt = dataEvent.getType();
        this.zzbSD = dataEvent.getDataItem().freeze();
    }

    @Override // com.google.android.gms.common.data.Freezable
    public final /* bridge */ /* synthetic */ DataEvent freeze() {
        return this;
    }

    @Override // com.google.android.gms.wearable.DataEvent
    public final DataItem getDataItem() {
        return this.zzbSD;
    }

    @Override // com.google.android.gms.wearable.DataEvent
    public final int getType() {
        return this.zzamt;
    }

    @Override // com.google.android.gms.common.data.Freezable
    public final boolean isDataValid() {
        return true;
    }

    public final String toString() {
        String str = getType() == 1 ? "changed" : getType() == 2 ? "deleted" : "unknown";
        String strValueOf = String.valueOf(getDataItem());
        return new StringBuilder(String.valueOf(str).length() + 35 + String.valueOf(strValueOf).length()).append("DataEventEntity{ type=").append(str).append(", dataitem=").append(strValueOf).append(" }").toString();
    }
}
