package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzclm extends ahz<zzclm> {
    public long[] zzbwi = aij.zzcvn;
    public long[] zzbwj = aij.zzcvn;

    public zzclm() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclm)) {
            return false;
        }
        zzclm zzclmVar = (zzclm) obj;
        if (aid.equals(this.zzbwi, zzclmVar.zzbwi) && aid.equals(this.zzbwj, zzclmVar.zzbwj)) {
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzclmVar.zzcuW == null || zzclmVar.zzcuW.isEmpty() : this.zzcuW.equals(zzclmVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        return ((this.zzcuW == null || this.zzcuW.isEmpty()) ? 0 : this.zzcuW.hashCode()) + ((((((getClass().getName().hashCode() + 527) * 31) + aid.hashCode(this.zzbwi)) * 31) + aid.hashCode(this.zzbwj)) * 31);
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 8:
                    int iZzb = aij.zzb(ahwVar, 8);
                    int length = this.zzbwi == null ? 0 : this.zzbwi.length;
                    long[] jArr = new long[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbwi, 0, jArr, 0, length);
                    }
                    while (length < jArr.length - 1) {
                        jArr[length] = ahwVar.zzLW();
                        ahwVar.zzLQ();
                        length++;
                    }
                    jArr[length] = ahwVar.zzLW();
                    this.zzbwi = jArr;
                    break;
                case 10:
                    int iZzcm = ahwVar.zzcm(ahwVar.zzLV());
                    int position = ahwVar.getPosition();
                    int i = 0;
                    while (ahwVar.zzMa() > 0) {
                        ahwVar.zzLW();
                        i++;
                    }
                    ahwVar.zzco(position);
                    int length2 = this.zzbwi == null ? 0 : this.zzbwi.length;
                    long[] jArr2 = new long[i + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.zzbwi, 0, jArr2, 0, length2);
                    }
                    while (length2 < jArr2.length) {
                        jArr2[length2] = ahwVar.zzLW();
                        length2++;
                    }
                    this.zzbwi = jArr2;
                    ahwVar.zzcn(iZzcm);
                    break;
                case 16:
                    int iZzb2 = aij.zzb(ahwVar, 16);
                    int length3 = this.zzbwj == null ? 0 : this.zzbwj.length;
                    long[] jArr3 = new long[iZzb2 + length3];
                    if (length3 != 0) {
                        System.arraycopy(this.zzbwj, 0, jArr3, 0, length3);
                    }
                    while (length3 < jArr3.length - 1) {
                        jArr3[length3] = ahwVar.zzLW();
                        ahwVar.zzLQ();
                        length3++;
                    }
                    jArr3[length3] = ahwVar.zzLW();
                    this.zzbwj = jArr3;
                    break;
                case 18:
                    int iZzcm2 = ahwVar.zzcm(ahwVar.zzLV());
                    int position2 = ahwVar.getPosition();
                    int i2 = 0;
                    while (ahwVar.zzMa() > 0) {
                        ahwVar.zzLW();
                        i2++;
                    }
                    ahwVar.zzco(position2);
                    int length4 = this.zzbwj == null ? 0 : this.zzbwj.length;
                    long[] jArr4 = new long[i2 + length4];
                    if (length4 != 0) {
                        System.arraycopy(this.zzbwj, 0, jArr4, 0, length4);
                    }
                    while (length4 < jArr4.length) {
                        jArr4[length4] = ahwVar.zzLW();
                        length4++;
                    }
                    this.zzbwj = jArr4;
                    ahwVar.zzcn(iZzcm2);
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
        if (this.zzbwi != null && this.zzbwi.length > 0) {
            for (int i = 0; i < this.zzbwi.length; i++) {
                ahxVar.zza(1, this.zzbwi[i]);
            }
        }
        if (this.zzbwj != null && this.zzbwj.length > 0) {
            for (int i2 = 0; i2 < this.zzbwj.length; i2++) {
                ahxVar.zza(2, this.zzbwj[i2]);
            }
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int length;
        int iZzn = super.zzn();
        if (this.zzbwi == null || this.zzbwi.length <= 0) {
            length = iZzn;
        } else {
            int iZzaP = 0;
            for (int i = 0; i < this.zzbwi.length; i++) {
                iZzaP += ahx.zzaP(this.zzbwi[i]);
            }
            length = iZzn + iZzaP + (this.zzbwi.length * 1);
        }
        if (this.zzbwj == null || this.zzbwj.length <= 0) {
            return length;
        }
        int iZzaP2 = 0;
        for (int i2 = 0; i2 < this.zzbwj.length; i2++) {
            iZzaP2 += ahx.zzaP(this.zzbwj[i2]);
        }
        return length + iZzaP2 + (this.zzbwj.length * 1);
    }
}
