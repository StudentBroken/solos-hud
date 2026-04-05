package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcli extends ahz<zzcli> {
    private static volatile zzcli[] zzbvz;
    public zzclj[] zzbvA = zzclj.zzzA();
    public String name = null;
    public Long zzbvB = null;
    public Long zzbvC = null;
    public Integer count = null;

    public zzcli() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzcli[] zzzz() {
        if (zzbvz == null) {
            synchronized (aid.zzcve) {
                if (zzbvz == null) {
                    zzbvz = new zzcli[0];
                }
            }
        }
        return zzbvz;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcli)) {
            return false;
        }
        zzcli zzcliVar = (zzcli) obj;
        if (!aid.equals(this.zzbvA, zzcliVar.zzbvA)) {
            return false;
        }
        if (this.name == null) {
            if (zzcliVar.name != null) {
                return false;
            }
        } else if (!this.name.equals(zzcliVar.name)) {
            return false;
        }
        if (this.zzbvB == null) {
            if (zzcliVar.zzbvB != null) {
                return false;
            }
        } else if (!this.zzbvB.equals(zzcliVar.zzbvB)) {
            return false;
        }
        if (this.zzbvC == null) {
            if (zzcliVar.zzbvC != null) {
                return false;
            }
        } else if (!this.zzbvC.equals(zzcliVar.zzbvC)) {
            return false;
        }
        if (this.count == null) {
            if (zzcliVar.count != null) {
                return false;
            }
        } else if (!this.count.equals(zzcliVar.count)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzcliVar.zzcuW == null || zzcliVar.zzcuW.isEmpty() : this.zzcuW.equals(zzcliVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.count == null ? 0 : this.count.hashCode()) + (((this.zzbvC == null ? 0 : this.zzbvC.hashCode()) + (((this.zzbvB == null ? 0 : this.zzbvB.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + aid.hashCode(this.zzbvA)) * 31)) * 31)) * 31)) * 31)) * 31;
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
                case 10:
                    int iZzb = aij.zzb(ahwVar, 10);
                    int length = this.zzbvA == null ? 0 : this.zzbvA.length;
                    zzclj[] zzcljVarArr = new zzclj[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbvA, 0, zzcljVarArr, 0, length);
                    }
                    while (length < zzcljVarArr.length - 1) {
                        zzcljVarArr[length] = new zzclj();
                        ahwVar.zzb(zzcljVarArr[length]);
                        ahwVar.zzLQ();
                        length++;
                    }
                    zzcljVarArr[length] = new zzclj();
                    ahwVar.zzb(zzcljVarArr[length]);
                    this.zzbvA = zzcljVarArr;
                    break;
                case 18:
                    this.name = ahwVar.readString();
                    break;
                case 24:
                    this.zzbvB = Long.valueOf(ahwVar.zzLW());
                    break;
                case 32:
                    this.zzbvC = Long.valueOf(ahwVar.zzLW());
                    break;
                case 40:
                    this.count = Integer.valueOf(ahwVar.zzLV());
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
        if (this.zzbvA != null && this.zzbvA.length > 0) {
            for (int i = 0; i < this.zzbvA.length; i++) {
                zzclj zzcljVar = this.zzbvA[i];
                if (zzcljVar != null) {
                    ahxVar.zza(1, zzcljVar);
                }
            }
        }
        if (this.name != null) {
            ahxVar.zzl(2, this.name);
        }
        if (this.zzbvB != null) {
            ahxVar.zzb(3, this.zzbvB.longValue());
        }
        if (this.zzbvC != null) {
            ahxVar.zzb(4, this.zzbvC.longValue());
        }
        if (this.count != null) {
            ahxVar.zzr(5, this.count.intValue());
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbvA != null && this.zzbvA.length > 0) {
            for (int i = 0; i < this.zzbvA.length; i++) {
                zzclj zzcljVar = this.zzbvA[i];
                if (zzcljVar != null) {
                    iZzn += ahx.zzb(1, zzcljVar);
                }
            }
        }
        if (this.name != null) {
            iZzn += ahx.zzm(2, this.name);
        }
        if (this.zzbvB != null) {
            iZzn += ahx.zze(3, this.zzbvB.longValue());
        }
        if (this.zzbvC != null) {
            iZzn += ahx.zze(4, this.zzbvC.longValue());
        }
        return this.count != null ? iZzn + ahx.zzs(5, this.count.intValue()) : iZzn;
    }
}
