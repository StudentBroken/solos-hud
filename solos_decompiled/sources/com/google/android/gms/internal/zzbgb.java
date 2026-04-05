package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes67.dex */
final class zzbgb implements Runnable {
    private /* synthetic */ String zzO;
    private /* synthetic */ zzbfe zzaEM;
    private /* synthetic */ zzbga zzaFb;

    zzbgb(zzbga zzbgaVar, zzbfe zzbfeVar, String str) {
        this.zzaFb = zzbgaVar;
        this.zzaEM = zzbfeVar;
        this.zzO = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.zzaFb.zzLj > 0) {
            this.zzaEM.onCreate(this.zzaFb.zzaEL != null ? this.zzaFb.zzaEL.getBundle(this.zzO) : null);
        }
        if (this.zzaFb.zzLj >= 2) {
            this.zzaEM.onStart();
        }
        if (this.zzaFb.zzLj >= 3) {
            this.zzaEM.onResume();
        }
        if (this.zzaFb.zzLj >= 4) {
            this.zzaEM.onStop();
        }
        if (this.zzaFb.zzLj >= 5) {
            this.zzaEM.onDestroy();
        }
    }
}
