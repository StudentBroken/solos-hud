package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
final class zzcgy implements Runnable {
    private /* synthetic */ String zzbqZ;
    private /* synthetic */ zzcgx zzbra;

    zzcgy(zzcgx zzcgxVar, String str) {
        this.zzbra = zzcgxVar;
        this.zzbqZ = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzchi zzchiVarZzwF = this.zzbra.zzboi.zzwF();
        if (zzchiVarZzwF.isInitialized()) {
            zzchiVarZzwF.zzbrn.zzf(this.zzbqZ, 1L);
        } else {
            this.zzbra.zzk(6, "Persisted config not initialized. Not logging error/warn");
        }
    }
}
