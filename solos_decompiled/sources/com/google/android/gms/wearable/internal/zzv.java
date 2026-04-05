package com.google.android.gms.wearable.internal;

import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;

/* JADX INFO: loaded from: classes6.dex */
final class zzv implements CapabilityApi.CapabilityListener {
    private CapabilityApi.CapabilityListener zzbSa;
    private String zzbSb;

    zzv(CapabilityApi.CapabilityListener capabilityListener, String str) {
        this.zzbSa = capabilityListener;
        this.zzbSb = str;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        zzv zzvVar = (zzv) obj;
        if (this.zzbSa.equals(zzvVar.zzbSa)) {
            return this.zzbSb.equals(zzvVar.zzbSb);
        }
        return false;
    }

    public final int hashCode() {
        return (this.zzbSa.hashCode() * 31) + this.zzbSb.hashCode();
    }

    @Override // com.google.android.gms.wearable.CapabilityApi.CapabilityListener
    public final void onCapabilityChanged(CapabilityInfo capabilityInfo) {
        this.zzbSa.onCapabilityChanged(capabilityInfo);
    }
}
