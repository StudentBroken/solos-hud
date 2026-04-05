package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes3.dex */
final class zzbcx implements Runnable {
    private /* synthetic */ zzbcw zzaCz;

    zzbcx(zzbcw zzbcwVar) {
        this.zzaCz = zzbcwVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzaCz.zzaCx.lock();
        try {
            this.zzaCz.zzpD();
        } finally {
            this.zzaCz.zzaCx.unlock();
        }
    }
}
