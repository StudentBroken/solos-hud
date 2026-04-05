package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbfl;
import com.google.android.gms.wearable.CapabilityApi;

/* JADX INFO: loaded from: classes6.dex */
final class zzgg implements zzbfl<CapabilityApi.CapabilityListener> {
    private /* synthetic */ zzaa zzbTD;

    zzgg(zzaa zzaaVar) {
        this.zzbTD = zzaaVar;
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final void zzpR() {
    }

    @Override // com.google.android.gms.internal.zzbfl
    public final /* synthetic */ void zzq(CapabilityApi.CapabilityListener capabilityListener) {
        capabilityListener.onCapabilityChanged(this.zzbTD);
    }
}
