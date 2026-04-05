package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes3.dex */
final class zzbgi implements zzbgj {
    private /* synthetic */ zzbgh zzaFp;

    zzbgi(zzbgh zzbghVar) {
        this.zzaFp = zzbghVar;
    }

    @Override // com.google.android.gms.internal.zzbgj
    public final void zzc(zzbcq<?> zzbcqVar) {
        this.zzaFp.zzaFn.remove(zzbcqVar);
        zzbcqVar.zzpm();
    }
}
