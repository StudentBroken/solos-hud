package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes35.dex */
public final class ajf extends aif {
    private static volatile ajf[] zzcxa;
    public String zzcxb = "";

    public ajf() {
        this.zzcvf = -1;
    }

    public static ajf[] zzMx() {
        if (zzcxa == null) {
            synchronized (aid.zzcve) {
                if (zzcxa == null) {
                    zzcxa = new ajf[0];
                }
            }
        }
        return zzcxa;
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 10:
                    this.zzcxb = ahwVar.readString();
                    break;
                default:
                    if (!ahwVar.zzcl(iZzLQ)) {
                    }
                    break;
            }
        }
        return this;
    }

    @Override // com.google.android.gms.internal.aif
    public final void zza(ahx ahxVar) throws IOException {
        if (this.zzcxb != null && !this.zzcxb.equals("")) {
            ahxVar.zzl(1, this.zzcxb);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        return (this.zzcxb == null || this.zzcxb.equals("")) ? iZzn : iZzn + ahx.zzm(1, this.zzcxb);
    }
}
