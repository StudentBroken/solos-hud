package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.wearable.DataItemAsset;

/* JADX INFO: loaded from: classes6.dex */
public final class zzbz extends com.google.android.gms.common.data.zzc implements DataItemAsset {
    public zzbz(DataHolder dataHolder, int i) {
        super(dataHolder, i);
    }

    @Override // com.google.android.gms.common.data.Freezable
    public final /* synthetic */ DataItemAsset freeze() {
        return new zzbx(this);
    }

    @Override // com.google.android.gms.wearable.DataItemAsset
    public final String getDataItemKey() {
        return getString("asset_key");
    }

    @Override // com.google.android.gms.wearable.DataItemAsset
    public final String getId() {
        return getString("asset_id");
    }
}
