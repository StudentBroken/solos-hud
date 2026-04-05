package com.google.android.gms.internal;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzckz extends ahz<zzckz> {
    private static volatile zzckz[] zzbuP;
    public Integer zzbuQ = null;
    public String zzbuR = null;
    public zzcla[] zzbuS = zzcla.zzzu();
    private Boolean zzbuT = null;
    public zzclb zzbuU = null;

    public zzckz() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzckz[] zzzt() {
        if (zzbuP == null) {
            synchronized (aid.zzcve) {
                if (zzbuP == null) {
                    zzbuP = new zzckz[0];
                }
            }
        }
        return zzbuP;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzckz)) {
            return false;
        }
        zzckz zzckzVar = (zzckz) obj;
        if (this.zzbuQ == null) {
            if (zzckzVar.zzbuQ != null) {
                return false;
            }
        } else if (!this.zzbuQ.equals(zzckzVar.zzbuQ)) {
            return false;
        }
        if (this.zzbuR == null) {
            if (zzckzVar.zzbuR != null) {
                return false;
            }
        } else if (!this.zzbuR.equals(zzckzVar.zzbuR)) {
            return false;
        }
        if (!aid.equals(this.zzbuS, zzckzVar.zzbuS)) {
            return false;
        }
        if (this.zzbuT == null) {
            if (zzckzVar.zzbuT != null) {
                return false;
            }
        } else if (!this.zzbuT.equals(zzckzVar.zzbuT)) {
            return false;
        }
        if (this.zzbuU == null) {
            if (zzckzVar.zzbuU != null) {
                return false;
            }
        } else if (!this.zzbuU.equals(zzckzVar.zzbuU)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzckzVar.zzcuW == null || zzckzVar.zzcuW.isEmpty() : this.zzcuW.equals(zzckzVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbuU == null ? 0 : this.zzbuU.hashCode()) + (((this.zzbuT == null ? 0 : this.zzbuT.hashCode()) + (((((this.zzbuR == null ? 0 : this.zzbuR.hashCode()) + (((this.zzbuQ == null ? 0 : this.zzbuQ.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31) + aid.hashCode(this.zzbuS)) * 31)) * 31)) * 31;
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
                    this.zzbuQ = Integer.valueOf(ahwVar.zzLV());
                    break;
                case 18:
                    this.zzbuR = ahwVar.readString();
                    break;
                case 26:
                    int iZzb = aij.zzb(ahwVar, 26);
                    int length = this.zzbuS == null ? 0 : this.zzbuS.length;
                    zzcla[] zzclaVarArr = new zzcla[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbuS, 0, zzclaVarArr, 0, length);
                    }
                    while (length < zzclaVarArr.length - 1) {
                        zzclaVarArr[length] = new zzcla();
                        ahwVar.zzb(zzclaVarArr[length]);
                        ahwVar.zzLQ();
                        length++;
                    }
                    zzclaVarArr[length] = new zzcla();
                    ahwVar.zzb(zzclaVarArr[length]);
                    this.zzbuS = zzclaVarArr;
                    break;
                case 32:
                    this.zzbuT = Boolean.valueOf(ahwVar.zzLT());
                    break;
                case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                    if (this.zzbuU == null) {
                        this.zzbuU = new zzclb();
                    }
                    ahwVar.zzb(this.zzbuU);
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
        if (this.zzbuQ != null) {
            ahxVar.zzr(1, this.zzbuQ.intValue());
        }
        if (this.zzbuR != null) {
            ahxVar.zzl(2, this.zzbuR);
        }
        if (this.zzbuS != null && this.zzbuS.length > 0) {
            for (int i = 0; i < this.zzbuS.length; i++) {
                zzcla zzclaVar = this.zzbuS[i];
                if (zzclaVar != null) {
                    ahxVar.zza(3, zzclaVar);
                }
            }
        }
        if (this.zzbuT != null) {
            ahxVar.zzk(4, this.zzbuT.booleanValue());
        }
        if (this.zzbuU != null) {
            ahxVar.zza(5, this.zzbuU);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbuQ != null) {
            iZzn += ahx.zzs(1, this.zzbuQ.intValue());
        }
        if (this.zzbuR != null) {
            iZzn += ahx.zzm(2, this.zzbuR);
        }
        if (this.zzbuS != null && this.zzbuS.length > 0) {
            int iZzb = iZzn;
            for (int i = 0; i < this.zzbuS.length; i++) {
                zzcla zzclaVar = this.zzbuS[i];
                if (zzclaVar != null) {
                    iZzb += ahx.zzb(3, zzclaVar);
                }
            }
            iZzn = iZzb;
        }
        if (this.zzbuT != null) {
            this.zzbuT.booleanValue();
            iZzn += ahx.zzcs(4) + 1;
        }
        return this.zzbuU != null ? iZzn + ahx.zzb(5, this.zzbuU) : iZzn;
    }
}
