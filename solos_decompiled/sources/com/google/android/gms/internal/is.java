package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes6.dex */
public final class is extends ahz<is> {
    private static volatile is[] zzbTM;
    public int type = 1;
    public it zzbTN = null;

    public is() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static is[] zzDY() {
        if (zzbTM == null) {
            synchronized (aid.zzcve) {
                if (zzbTM == null) {
                    zzbTM = new is[0];
                }
            }
        }
        return zzbTM;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof is)) {
            return false;
        }
        is isVar = (is) obj;
        if (this.type != isVar.type) {
            return false;
        }
        if (this.zzbTN == null) {
            if (isVar.zzbTN != null) {
                return false;
            }
        } else if (!this.zzbTN.equals(isVar.zzbTN)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? isVar.zzcuW == null || isVar.zzcuW.isEmpty() : this.zzcuW.equals(isVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbTN == null ? 0 : this.zzbTN.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + this.type) * 31)) * 31;
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
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                            this.type = iZzLV;
                            break;
                        default:
                            ahwVar.zzco(position);
                            zza(ahwVar, iZzLQ);
                            break;
                    }
                    break;
                case 18:
                    if (this.zzbTN == null) {
                        this.zzbTN = new it();
                    }
                    ahwVar.zzb(this.zzbTN);
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
        ahxVar.zzr(1, this.type);
        if (this.zzbTN != null) {
            ahxVar.zza(2, this.zzbTN);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn() + ahx.zzs(1, this.type);
        return this.zzbTN != null ? iZzn + ahx.zzb(2, this.zzbTN) : iZzn;
    }
}
