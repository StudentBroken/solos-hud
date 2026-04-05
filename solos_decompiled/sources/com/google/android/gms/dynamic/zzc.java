package com.google.android.gms.dynamic;

import android.app.Activity;
import android.os.Bundle;

/* JADX INFO: loaded from: classes3.dex */
final class zzc implements zzi {
    private /* synthetic */ Activity val$activity;
    private /* synthetic */ Bundle zzaSA;
    private /* synthetic */ zza zzaSz;
    private /* synthetic */ Bundle zzxY;

    zzc(zza zzaVar, Activity activity, Bundle bundle, Bundle bundle2) {
        this.zzaSz = zzaVar;
        this.val$activity = activity;
        this.zzaSA = bundle;
        this.zzxY = bundle2;
    }

    @Override // com.google.android.gms.dynamic.zzi
    public final int getState() {
        return 0;
    }

    @Override // com.google.android.gms.dynamic.zzi
    public final void zzb(LifecycleDelegate lifecycleDelegate) {
        this.zzaSz.zzaSv.onInflate(this.val$activity, this.zzaSA, this.zzxY);
    }
}
