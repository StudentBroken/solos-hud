package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes3.dex */
final class zzbfw implements Runnable {
    private /* synthetic */ zzcvj zzaDz;
    private /* synthetic */ zzbfv zzaFa;

    zzbfw(zzbfv zzbfvVar, zzcvj zzcvjVar) {
        this.zzaFa = zzbfvVar;
        this.zzaDz = zzcvjVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzaFa.zzc(this.zzaDz);
    }
}
