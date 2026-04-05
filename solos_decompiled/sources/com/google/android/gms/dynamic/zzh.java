package com.google.android.gms.dynamic;

/* JADX INFO: loaded from: classes3.dex */
final class zzh implements zzi {
    private /* synthetic */ zza zzaSz;

    zzh(zza zzaVar) {
        this.zzaSz = zzaVar;
    }

    @Override // com.google.android.gms.dynamic.zzi
    public final int getState() {
        return 5;
    }

    @Override // com.google.android.gms.dynamic.zzi
    public final void zzb(LifecycleDelegate lifecycleDelegate) {
        this.zzaSz.zzaSv.onResume();
    }
}
