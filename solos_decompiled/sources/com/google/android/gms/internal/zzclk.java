package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzclk extends ahz<zzclk> {
    public zzcll[] zzbvF = zzcll.zzzB();

    public zzclk() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclk)) {
            return false;
        }
        zzclk zzclkVar = (zzclk) obj;
        if (aid.equals(this.zzbvF, zzclkVar.zzbvF)) {
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzclkVar.zzcuW == null || zzclkVar.zzcuW.isEmpty() : this.zzcuW.equals(zzclkVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        return ((this.zzcuW == null || this.zzcuW.isEmpty()) ? 0 : this.zzcuW.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + aid.hashCode(this.zzbvF)) * 31);
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
                    int length = this.zzbvF == null ? 0 : this.zzbvF.length;
                    zzcll[] zzcllVarArr = new zzcll[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbvF, 0, zzcllVarArr, 0, length);
                    }
                    while (length < zzcllVarArr.length - 1) {
                        zzcllVarArr[length] = new zzcll();
                        ahwVar.zzb(zzcllVarArr[length]);
                        ahwVar.zzLQ();
                        length++;
                    }
                    zzcllVarArr[length] = new zzcll();
                    ahwVar.zzb(zzcllVarArr[length]);
                    this.zzbvF = zzcllVarArr;
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
        if (this.zzbvF != null && this.zzbvF.length > 0) {
            for (int i = 0; i < this.zzbvF.length; i++) {
                zzcll zzcllVar = this.zzbvF[i];
                if (zzcllVar != null) {
                    ahxVar.zza(1, zzcllVar);
                }
            }
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbvF != null && this.zzbvF.length > 0) {
            for (int i = 0; i < this.zzbvF.length; i++) {
                zzcll zzcllVar = this.zzbvF[i];
                if (zzcllVar != null) {
                    iZzn += ahx.zzb(1, zzcllVar);
                }
            }
        }
        return iZzn;
    }
}
