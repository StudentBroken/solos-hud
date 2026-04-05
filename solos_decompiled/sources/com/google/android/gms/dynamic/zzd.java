package com.google.android.gms.dynamic;

import android.os.Bundle;

/* JADX INFO: loaded from: classes3.dex */
final class zzd implements zzi {
    private /* synthetic */ zza zzaSz;
    private /* synthetic */ Bundle zzxY;

    zzd(zza zzaVar, Bundle bundle) {
        this.zzaSz = zzaVar;
        this.zzxY = bundle;
    }

    @Override // com.google.android.gms.dynamic.zzi
    public final int getState() {
        return 1;
    }

    @Override // com.google.android.gms.dynamic.zzi
    public final void zzb(LifecycleDelegate lifecycleDelegate) {
        this.zzaSz.zzaSv.onCreate(this.zzxY);
    }
}
