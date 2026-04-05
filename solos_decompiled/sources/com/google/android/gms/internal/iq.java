package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes6.dex */
public final class iq extends ahz<iq> {
    public ir[] zzbTJ = ir.zzDX();

    public iq() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static iq zzz(byte[] bArr) throws aie {
        return (iq) aif.zza(new iq(), bArr);
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof iq)) {
            return false;
        }
        iq iqVar = (iq) obj;
        if (aid.equals(this.zzbTJ, iqVar.zzbTJ)) {
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? iqVar.zzcuW == null || iqVar.zzcuW.isEmpty() : this.zzcuW.equals(iqVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        return ((this.zzcuW == null || this.zzcuW.isEmpty()) ? 0 : this.zzcuW.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + aid.hashCode(this.zzbTJ)) * 31);
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 10:
                    int iZzb = aij.zzb(ahwVar, 10);
                    int length = this.zzbTJ == null ? 0 : this.zzbTJ.length;
                    ir[] irVarArr = new ir[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbTJ, 0, irVarArr, 0, length);
                    }
                    while (length < irVarArr.length - 1) {
                        irVarArr[length] = new ir();
                        ahwVar.zzb(irVarArr[length]);
                        ahwVar.zzLQ();
                        length++;
                    }
                    irVarArr[length] = new ir();
                    ahwVar.zzb(irVarArr[length]);
                    this.zzbTJ = irVarArr;
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
        if (this.zzbTJ != null && this.zzbTJ.length > 0) {
            for (int i = 0; i < this.zzbTJ.length; i++) {
                ir irVar = this.zzbTJ[i];
                if (irVar != null) {
                    ahxVar.zza(1, irVar);
                }
            }
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbTJ != null && this.zzbTJ.length > 0) {
            for (int i = 0; i < this.zzbTJ.length; i++) {
                ir irVar = this.zzbTJ[i];
                if (irVar != null) {
                    iZzn += ahx.zzb(1, irVar);
                }
            }
        }
        return iZzn;
    }
}
