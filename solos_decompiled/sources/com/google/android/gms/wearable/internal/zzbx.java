package com.google.android.gms.wearable.internal;

import com.google.android.gms.wearable.DataItemAsset;

/* JADX INFO: loaded from: classes6.dex */
public final class zzbx implements DataItemAsset {
    private final String zzBQ;
    private final String zzIl;

    public zzbx(DataItemAsset dataItemAsset) {
        this.zzIl = dataItemAsset.getId();
        this.zzBQ = dataItemAsset.getDataItemKey();
    }

    @Override // com.google.android.gms.common.data.Freezable
    public final /* bridge */ /* synthetic */ DataItemAsset freeze() {
        return this;
    }

    @Override // com.google.android.gms.wearable.DataItemAsset
    public final String getDataItemKey() {
        return this.zzBQ;
    }

    @Override // com.google.android.gms.wearable.DataItemAsset
    public final String getId() {
        return this.zzIl;
    }

    @Override // com.google.android.gms.common.data.Freezable
    public final boolean isDataValid() {
        return true;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DataItemAssetEntity[");
        sb.append("@");
        sb.append(Integer.toHexString(hashCode()));
        if (this.zzIl == null) {
            sb.append(",noid");
        } else {
            sb.append(",");
            sb.append(this.zzIl);
        }
        sb.append(", key=");
        sb.append(this.zzBQ);
        sb.append("]");
        return sb.toString();
    }
}
