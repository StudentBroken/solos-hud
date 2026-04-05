package com.google.android.gms.internal;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzclf extends ahz<zzclf> {
    public Long zzbvp = null;
    public String zzboU = null;
    private Integer zzbvq = null;
    public zzclg[] zzbvr = zzclg.zzzx();
    public zzcle[] zzbvs = zzcle.zzzw();
    public zzcky[] zzbvt = zzcky.zzzs();

    public zzclf() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclf)) {
            return false;
        }
        zzclf zzclfVar = (zzclf) obj;
        if (this.zzbvp == null) {
            if (zzclfVar.zzbvp != null) {
                return false;
            }
        } else if (!this.zzbvp.equals(zzclfVar.zzbvp)) {
            return false;
        }
        if (this.zzboU == null) {
            if (zzclfVar.zzboU != null) {
                return false;
            }
        } else if (!this.zzboU.equals(zzclfVar.zzboU)) {
            return false;
        }
        if (this.zzbvq == null) {
            if (zzclfVar.zzbvq != null) {
                return false;
            }
        } else if (!this.zzbvq.equals(zzclfVar.zzbvq)) {
            return false;
        }
        if (aid.equals(this.zzbvr, zzclfVar.zzbvr) && aid.equals(this.zzbvs, zzclfVar.zzbvs) && aid.equals(this.zzbvt, zzclfVar.zzbvt)) {
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzclfVar.zzcuW == null || zzclfVar.zzcuW.isEmpty() : this.zzcuW.equals(zzclfVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((((((((this.zzbvq == null ? 0 : this.zzbvq.hashCode()) + (((this.zzboU == null ? 0 : this.zzboU.hashCode()) + (((this.zzbvp == null ? 0 : this.zzbvp.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + aid.hashCode(this.zzbvr)) * 31) + aid.hashCode(this.zzbvs)) * 31) + aid.hashCode(this.zzbvt)) * 31;
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
                    this.zzbvp = Long.valueOf(ahwVar.zzLW());
                    break;
                case 18:
                    this.zzboU = ahwVar.readString();
                    break;
                case 24:
                    this.zzbvq = Integer.valueOf(ahwVar.zzLV());
                    break;
                case 34:
                    int iZzb = aij.zzb(ahwVar, 34);
                    int length = this.zzbvr == null ? 0 : this.zzbvr.length;
                    zzclg[] zzclgVarArr = new zzclg[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbvr, 0, zzclgVarArr, 0, length);
                    }
                    while (length < zzclgVarArr.length - 1) {
                        zzclgVarArr[length] = new zzclg();
                        ahwVar.zzb(zzclgVarArr[length]);
                        ahwVar.zzLQ();
                        length++;
                    }
                    zzclgVarArr[length] = new zzclg();
                    ahwVar.zzb(zzclgVarArr[length]);
                    this.zzbvr = zzclgVarArr;
                    break;
                case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                    int iZzb2 = aij.zzb(ahwVar, 42);
                    int length2 = this.zzbvs == null ? 0 : this.zzbvs.length;
                    zzcle[] zzcleVarArr = new zzcle[iZzb2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.zzbvs, 0, zzcleVarArr, 0, length2);
                    }
                    while (length2 < zzcleVarArr.length - 1) {
                        zzcleVarArr[length2] = new zzcle();
                        ahwVar.zzb(zzcleVarArr[length2]);
                        ahwVar.zzLQ();
                        length2++;
                    }
                    zzcleVarArr[length2] = new zzcle();
                    ahwVar.zzb(zzcleVarArr[length2]);
                    this.zzbvs = zzcleVarArr;
                    break;
                case 50:
                    int iZzb3 = aij.zzb(ahwVar, 50);
                    int length3 = this.zzbvt == null ? 0 : this.zzbvt.length;
                    zzcky[] zzckyVarArr = new zzcky[iZzb3 + length3];
                    if (length3 != 0) {
                        System.arraycopy(this.zzbvt, 0, zzckyVarArr, 0, length3);
                    }
                    while (length3 < zzckyVarArr.length - 1) {
                        zzckyVarArr[length3] = new zzcky();
                        ahwVar.zzb(zzckyVarArr[length3]);
                        ahwVar.zzLQ();
                        length3++;
                    }
                    zzckyVarArr[length3] = new zzcky();
                    ahwVar.zzb(zzckyVarArr[length3]);
                    this.zzbvt = zzckyVarArr;
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
        if (this.zzbvp != null) {
            ahxVar.zzb(1, this.zzbvp.longValue());
        }
        if (this.zzboU != null) {
            ahxVar.zzl(2, this.zzboU);
        }
        if (this.zzbvq != null) {
            ahxVar.zzr(3, this.zzbvq.intValue());
        }
        if (this.zzbvr != null && this.zzbvr.length > 0) {
            for (int i = 0; i < this.zzbvr.length; i++) {
                zzclg zzclgVar = this.zzbvr[i];
                if (zzclgVar != null) {
                    ahxVar.zza(4, zzclgVar);
                }
            }
        }
        if (this.zzbvs != null && this.zzbvs.length > 0) {
            for (int i2 = 0; i2 < this.zzbvs.length; i2++) {
                zzcle zzcleVar = this.zzbvs[i2];
                if (zzcleVar != null) {
                    ahxVar.zza(5, zzcleVar);
                }
            }
        }
        if (this.zzbvt != null && this.zzbvt.length > 0) {
            for (int i3 = 0; i3 < this.zzbvt.length; i3++) {
                zzcky zzckyVar = this.zzbvt[i3];
                if (zzckyVar != null) {
                    ahxVar.zza(6, zzckyVar);
                }
            }
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbvp != null) {
            iZzn += ahx.zze(1, this.zzbvp.longValue());
        }
        if (this.zzboU != null) {
            iZzn += ahx.zzm(2, this.zzboU);
        }
        if (this.zzbvq != null) {
            iZzn += ahx.zzs(3, this.zzbvq.intValue());
        }
        if (this.zzbvr != null && this.zzbvr.length > 0) {
            int iZzb = iZzn;
            for (int i = 0; i < this.zzbvr.length; i++) {
                zzclg zzclgVar = this.zzbvr[i];
                if (zzclgVar != null) {
                    iZzb += ahx.zzb(4, zzclgVar);
                }
            }
            iZzn = iZzb;
        }
        if (this.zzbvs != null && this.zzbvs.length > 0) {
            int iZzb2 = iZzn;
            for (int i2 = 0; i2 < this.zzbvs.length; i2++) {
                zzcle zzcleVar = this.zzbvs[i2];
                if (zzcleVar != null) {
                    iZzb2 += ahx.zzb(5, zzcleVar);
                }
            }
            iZzn = iZzb2;
        }
        if (this.zzbvt != null && this.zzbvt.length > 0) {
            for (int i3 = 0; i3 < this.zzbvt.length; i3++) {
                zzcky zzckyVar = this.zzbvt[i3];
                if (zzckyVar != null) {
                    iZzn += ahx.zzb(6, zzckyVar);
                }
            }
        }
        return iZzn;
    }
}
