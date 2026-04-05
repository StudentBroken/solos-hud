package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes3.dex */
final class zzbeo implements zzbcj {
    private /* synthetic */ zzben zzaEo;

    zzbeo(zzben zzbenVar) {
        this.zzaEo = zzbenVar;
    }

    @Override // com.google.android.gms.internal.zzbcj
    public final void zzac(boolean z) {
        this.zzaEo.mHandler.sendMessage(this.zzaEo.mHandler.obtainMessage(1, Boolean.valueOf(z)));
    }
}
