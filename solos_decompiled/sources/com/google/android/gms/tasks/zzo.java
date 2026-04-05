package com.google.android.gms.tasks;

import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes56.dex */
final class zzo implements Runnable {
    private /* synthetic */ Callable zzZq;
    private /* synthetic */ zzn zzbMm;

    zzo(zzn zznVar, Callable callable) {
        this.zzbMm = zznVar;
        this.zzZq = callable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.zzbMm.setResult(this.zzZq.call());
        } catch (Exception e) {
            this.zzbMm.setException(e);
        }
    }
}
