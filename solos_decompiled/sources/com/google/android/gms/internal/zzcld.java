package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcld extends ahz<zzcld> {
    public Integer zzbvi = null;
    public String zzbvj = null;
    public Boolean zzbvk = null;
    public String[] zzbvl = aij.EMPTY_STRING_ARRAY;

    public zzcld() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcld)) {
            return false;
        }
        zzcld zzcldVar = (zzcld) obj;
        if (this.zzbvi == null) {
            if (zzcldVar.zzbvi != null) {
                return false;
            }
        } else if (!this.zzbvi.equals(zzcldVar.zzbvi)) {
            return false;
        }
        if (this.zzbvj == null) {
            if (zzcldVar.zzbvj != null) {
                return false;
            }
        } else if (!this.zzbvj.equals(zzcldVar.zzbvj)) {
            return false;
        }
        if (this.zzbvk == null) {
            if (zzcldVar.zzbvk != null) {
                return false;
            }
        } else if (!this.zzbvk.equals(zzcldVar.zzbvk)) {
            return false;
        }
        if (aid.equals(this.zzbvl, zzcldVar.zzbvl)) {
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzcldVar.zzcuW == null || zzcldVar.zzcuW.isEmpty() : this.zzcuW.equals(zzcldVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((((this.zzbvk == null ? 0 : this.zzbvk.hashCode()) + (((this.zzbvj == null ? 0 : this.zzbvj.hashCode()) + (((this.zzbvi == null ? 0 : this.zzbvi.intValue()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + aid.hashCode(this.zzbvl)) * 31;
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
                    int position = ahwVar.getPosition();
                    int iZzLV = ahwVar.zzLV();
                    switch (iZzLV) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            this.zzbvi = Integer.valueOf(iZzLV);
                            break;
                        default:
                            ahwVar.zzco(position);
                            zza(ahwVar, iZzLQ);
                            break;
                    }
                    break;
                case 18:
                    this.zzbvj = ahwVar.readString();
                    break;
                case 24:
                    this.zzbvk = Boolean.valueOf(ahwVar.zzLT());
                    break;
                case 34:
                    int iZzb = aij.zzb(ahwVar, 34);
                    int length = this.zzbvl == null ? 0 : this.zzbvl.length;
                    String[] strArr = new String[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbvl, 0, strArr, 0, length);
                    }
                    while (length < strArr.length - 1) {
                        strArr[length] = ahwVar.readString();
                        ahwVar.zzLQ();
                        length++;
                    }
                    strArr[length] = ahwVar.readString();
                    this.zzbvl = strArr;
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
        if (this.zzbvi != null) {
            ahxVar.zzr(1, this.zzbvi.intValue());
        }
        if (this.zzbvj != null) {
            ahxVar.zzl(2, this.zzbvj);
        }
        if (this.zzbvk != null) {
            ahxVar.zzk(3, this.zzbvk.booleanValue());
        }
        if (this.zzbvl != null && this.zzbvl.length > 0) {
            for (int i = 0; i < this.zzbvl.length; i++) {
                String str = this.zzbvl[i];
                if (str != null) {
                    ahxVar.zzl(4, str);
                }
            }
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbvi != null) {
            iZzn += ahx.zzs(1, this.zzbvi.intValue());
        }
        if (this.zzbvj != null) {
            iZzn += ahx.zzm(2, this.zzbvj);
        }
        if (this.zzbvk != null) {
            this.zzbvk.booleanValue();
            iZzn += ahx.zzcs(3) + 1;
        }
        if (this.zzbvl == null || this.zzbvl.length <= 0) {
            return iZzn;
        }
        int iZzip = 0;
        int i = 0;
        for (int i2 = 0; i2 < this.zzbvl.length; i2++) {
            String str = this.zzbvl[i2];
            if (str != null) {
                i++;
                iZzip += ahx.zzip(str);
            }
        }
        return iZzn + iZzip + (i * 1);
    }
}
