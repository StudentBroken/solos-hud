package com.google.android.gms.internal;

import android.os.Bundle;

/* JADX INFO: loaded from: classes3.dex */
final class zzbec implements com.google.android.gms.common.internal.zzae {
    private /* synthetic */ zzbeb zzaDP;

    zzbec(zzbeb zzbebVar) {
        this.zzaDP = zzbebVar;
    }

    @Override // com.google.android.gms.common.internal.zzae
    public final boolean isConnected() {
        return this.zzaDP.isConnected();
    }

    @Override // com.google.android.gms.common.internal.zzae
    public final Bundle zzoA() {
        return null;
    }
}
