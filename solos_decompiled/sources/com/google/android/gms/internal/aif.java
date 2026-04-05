package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes67.dex */
public abstract class aif {
    protected volatile int zzcvf = -1;

    public static final <T extends aif> T zza(T t, byte[] bArr) throws aie {
        return (T) zza(t, bArr, 0, bArr.length);
    }

    private static <T extends aif> T zza(T t, byte[] bArr, int i, int i2) throws aie {
        try {
            ahw ahwVarZzb = ahw.zzb(bArr, 0, i2);
            t.zza(ahwVarZzb);
            ahwVarZzb.zzck(0);
            return t;
        } catch (aie e) {
            throw e;
        } catch (IOException e2) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).");
        }
    }

    public static final byte[] zzd(aif aifVar) {
        byte[] bArr = new byte[aifVar.zzMl()];
        try {
            ahx ahxVarZzc = ahx.zzc(bArr, 0, bArr.length);
            aifVar.zza(ahxVarZzc);
            ahxVarZzc.zzMc();
            return bArr;
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public String toString() {
        return aig.zze(this);
    }

    @Override // 
    /* JADX INFO: renamed from: zzMe, reason: merged with bridge method [inline-methods] */
    public aif clone() throws CloneNotSupportedException {
        return (aif) super.clone();
    }

    public final int zzMk() {
        if (this.zzcvf < 0) {
            zzMl();
        }
        return this.zzcvf;
    }

    public final int zzMl() {
        int iZzn = zzn();
        this.zzcvf = iZzn;
        return iZzn;
    }

    public abstract aif zza(ahw ahwVar) throws IOException;

    public void zza(ahx ahxVar) throws IOException {
    }

    protected int zzn() {
        return 0;
    }
}
