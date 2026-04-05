package com.google.android.gms.dynamic;

/* JADX INFO: loaded from: classes3.dex */
final class zzg implements zzi {
    private /* synthetic */ zza zzaSz;

    zzg(zza zzaVar) {
        this.zzaSz = zzaVar;
    }

    @Override // com.google.android.gms.dynamic.zzi
    public final int getState() {
        return 4;
    }

    @Override // com.google.android.gms.dynamic.zzi
    public final void zzb(LifecycleDelegate lifecycleDelegate) {
        this.zzaSz.zzaSv.onStart();
    }
}
