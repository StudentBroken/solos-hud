package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes3.dex */
public final class ajb extends ahz<ajb> implements Cloneable {
    private byte[] zzcwA = aij.zzcvs;
    private String zzcwB = "";
    private byte[][] zzcwC = aij.zzcvr;
    private boolean zzcwD = false;

    public ajb() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMs, reason: merged with bridge method [inline-methods] */
    public ajb clone() {
        try {
            ajb ajbVar = (ajb) super.clone();
            if (this.zzcwC != null && this.zzcwC.length > 0) {
                ajbVar.zzcwC = (byte[][]) this.zzcwC.clone();
            }
            return ajbVar;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ajb)) {
            return false;
        }
        ajb ajbVar = (ajb) obj;
        if (!Arrays.equals(this.zzcwA, ajbVar.zzcwA)) {
            return false;
        }
        if (this.zzcwB == null) {
            if (ajbVar.zzcwB != null) {
                return false;
            }
        } else if (!this.zzcwB.equals(ajbVar.zzcwB)) {
            return false;
        }
        if (aid.zza(this.zzcwC, ajbVar.zzcwC) && this.zzcwD == ajbVar.zzcwD) {
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? ajbVar.zzcuW == null || ajbVar.zzcuW.isEmpty() : this.zzcuW.equals(ajbVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzcwD ? 1231 : 1237) + (((((this.zzcwB == null ? 0 : this.zzcwB.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + Arrays.hashCode(this.zzcwA)) * 31)) * 31) + aid.zzc(this.zzcwC)) * 31)) * 31;
        if (this.zzcuW != null && !this.zzcuW.isEmpty()) {
            iHashCode = this.zzcuW.hashCode();
        }
        return iHashCode2 + iHashCode;
    }

    @Override // com.google.android.gms.internal.ahz
    /* JADX INFO: renamed from: zzMd */
    public final /* synthetic */ ahz clone() throws CloneNotSupportedException {
        return (ajb) clone();
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMe */
    public final /* synthetic */ aif clone() throws CloneNotSupportedException {
        return (ajb) clone();
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 10:
                    this.zzcwA = ahwVar.readBytes();
                    break;
                case 18:
                    int iZzb = aij.zzb(ahwVar, 18);
                    int length = this.zzcwC == null ? 0 : this.zzcwC.length;
                    byte[][] bArr = new byte[iZzb + length][];
                    if (length != 0) {
                        System.arraycopy(this.zzcwC, 0, bArr, 0, length);
                    }
                    while (length < bArr.length - 1) {
                        bArr[length] = ahwVar.readBytes();
                        ahwVar.zzLQ();
                        length++;
                    }
                    bArr[length] = ahwVar.readBytes();
                    this.zzcwC = bArr;
                    break;
                case 24:
                    this.zzcwD = ahwVar.zzLT();
                    break;
                case 34:
                    this.zzcwB = ahwVar.readString();
                    break;
                default:
                    if (!super.zza(ahwVar, iZzLQ)) {
                    }
                    break;
            }
        }
        return this;
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    public final void zza(ahx ahxVar) throws IOException {
        if (!Arrays.equals(this.zzcwA, aij.zzcvs)) {
            ahxVar.zzb(1, this.zzcwA);
        }
        if (this.zzcwC != null && this.zzcwC.length > 0) {
            for (int i = 0; i < this.zzcwC.length; i++) {
                byte[] bArr = this.zzcwC[i];
                if (bArr != null) {
                    ahxVar.zzb(2, bArr);
                }
            }
        }
        if (this.zzcwD) {
            ahxVar.zzk(3, this.zzcwD);
        }
        if (this.zzcwB != null && !this.zzcwB.equals("")) {
            ahxVar.zzl(4, this.zzcwB);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (!Arrays.equals(this.zzcwA, aij.zzcvs)) {
            iZzn += ahx.zzc(1, this.zzcwA);
        }
        if (this.zzcwC != null && this.zzcwC.length > 0) {
            int iZzK = 0;
            int i = 0;
            for (int i2 = 0; i2 < this.zzcwC.length; i2++) {
                byte[] bArr = this.zzcwC[i2];
                if (bArr != null) {
                    i++;
                    iZzK += ahx.zzK(bArr);
                }
            }
            iZzn = iZzn + iZzK + (i * 1);
        }
        if (this.zzcwD) {
            iZzn += ahx.zzcs(3) + 1;
        }
        return (this.zzcwB == null || this.zzcwB.equals("")) ? iZzn : iZzn + ahx.zzm(4, this.zzcwB);
    }
}
