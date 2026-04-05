package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.zzbfl;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;

/* JADX INFO: loaded from: classes6.dex */
final class zzgb implements zzbfl<DataApi.DataListener> {
    private /* synthetic */ DataHolder zzbRB;

    zzgb(DataHolder dataHolder) {
        this.zzbRB = dataHolder;
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final void zzpR() {
        this.zzbRB.close();
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final /* synthetic */ void zzq(DataApi.DataListener dataListener) {
        try {
            dataListener.onDataChanged(new DataEventBuffer(this.zzbRB));
        } finally {
            this.zzbRB.close();
        }
    }
}
