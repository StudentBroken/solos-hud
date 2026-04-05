package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcky extends ahz<zzcky> {
    private static volatile zzcky[] zzbuL;
    public Integer zzbuM = null;
    public zzclc[] zzbuN = zzclc.zzzv();
    public zzckz[] zzbuO = zzckz.zzzt();

    public zzcky() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzcky[] zzzs() {
        if (zzbuL == null) {
            synchronized (aid.zzcve) {
                if (zzbuL == null) {
                    zzbuL = new zzcky[0];
                }
            }
        }
        return zzbuL;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcky)) {
            return false;
        }
        zzcky zzckyVar = (zzcky) obj;
        if (this.zzbuM == null) {
            if (zzckyVar.zzbuM != null) {
                return false;
            }
        } else if (!this.zzbuM.equals(zzckyVar.zzbuM)) {
            return false;
        }
        if (aid.equals(this.zzbuN, zzckyVar.zzbuN) && aid.equals(this.zzbuO, zzckyVar.zzbuO)) {
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzckyVar.zzcuW == null || zzckyVar.zzcuW.isEmpty() : this.zzcuW.equals(zzckyVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((((((this.zzbuM == null ? 0 : this.zzbuM.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + aid.hashCode(this.zzbuN)) * 31) + aid.hashCode(this.zzbuO)) * 31;
        if (this.zzcuW != null && !this.zzcuW.isEmpty()) {
            iHashCode = this.zzcuW.hashCode();
        }
        return iHashCode2 + iHashCode;
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 8:
                    this.zzbuM = Integer.valueOf(ahwVar.zzLV());
                    break;
                case 18:
                    int iZzb = aij.zzb(ahwVar, 18);
                    int length = this.zzbuN == null ? 0 : this.zzbuN.length;
                    zzclc[] zzclcVarArr = new zzclc[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbuN, 0, zzclcVarArr, 0, length);
                    }
                    while (length < zzclcVarArr.length - 1) {
                        zzclcVarArr[length] = new zzclc();
                        ahwVar.zzb(zzclcVarArr[length]);
                        ahwVar.zzLQ();
                        length++;
                    }
                    zzclcVarArr[length] = new zzclc();
                    ahwVar.zzb(zzclcVarArr[length]);
                    this.zzbuN = zzclcVarArr;
                    break;
                case 26:
                    int iZzb2 = aij.zzb(ahwVar, 26);
                    int length2 = this.zzbuO == null ? 0 : this.zzbuO.length;
                    zzckz[] zzckzVarArr = new zzckz[iZzb2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.zzbuO, 0, zzckzVarArr, 0, length2);
                    }
                    while (length2 < zzckzVarArr.length - 1) {
                        zzckzVarArr[length2] = new zzckz();
                        ahwVar.zzb(zzckzVarArr[length2]);
                        ahwVar.zzLQ();
                        length2++;
                    }
                    zzckzVarArr[length2] = new zzckz();
                    ahwVar.zzb(zzckzVarArr[length2]);
                    this.zzbuO = zzckzVarArr;
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
        if (this.zzbuM != null) {
            ahxVar.zzr(1, this.zzbuM.intValue());
        }
        if (this.zzbuN != null && this.zzbuN.length > 0) {
            for (int i = 0; i < this.zzbuN.length; i++) {
                zzclc zzclcVar = this.zzbuN[i];
                if (zzclcVar != null) {
                    ahxVar.zza(2, zzclcVar);
                }
            }
        }
        if (this.zzbuO != null && this.zzbuO.length > 0) {
            for (int i2 = 0; i2 < this.zzbuO.length; i2++) {
                zzckz zzckzVar = this.zzbuO[i2];
                if (zzckzVar != null) {
                    ahxVar.zza(3, zzckzVar);
                }
            }
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbuM != null) {
            iZzn += ahx.zzs(1, this.zzbuM.intValue());
        }
        if (this.zzbuN != null && this.zzbuN.length > 0) {
            int iZzb = iZzn;
            for (int i = 0; i < this.zzbuN.length; i++) {
                zzclc zzclcVar = this.zzbuN[i];
                if (zzclcVar != null) {
                    iZzb += ahx.zzb(2, zzclcVar);
                }
            }
            iZzn = iZzb;
        }
        if (this.zzbuO != null && this.zzbuO.length > 0) {
            for (int i2 = 0; i2 < this.zzbuO.length; i2++) {
                zzckz zzckzVar = this.zzbuO[i2];
                if (zzckzVar != null) {
                    iZzn += ahx.zzb(3, zzckzVar);
                }
            }
        }
        return iZzn;
    }
}
