package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcla extends ahz<zzcla> {
    private static volatile zzcla[] zzbuV;
    public zzcld zzbuW = null;
    public zzclb zzbuX = null;
    public Boolean zzbuY = null;
    public String zzbuZ = null;

    public zzcla() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzcla[] zzzu() {
        if (zzbuV == null) {
            synchronized (aid.zzcve) {
                if (zzbuV == null) {
                    zzbuV = new zzcla[0];
                }
            }
        }
        return zzbuV;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcla)) {
            return false;
        }
        zzcla zzclaVar = (zzcla) obj;
        if (this.zzbuW == null) {
            if (zzclaVar.zzbuW != null) {
                return false;
            }
        } else if (!this.zzbuW.equals(zzclaVar.zzbuW)) {
            return false;
        }
        if (this.zzbuX == null) {
            if (zzclaVar.zzbuX != null) {
                return false;
            }
        } else if (!this.zzbuX.equals(zzclaVar.zzbuX)) {
            return false;
        }
        if (this.zzbuY == null) {
            if (zzclaVar.zzbuY != null) {
                return false;
            }
        } else if (!this.zzbuY.equals(zzclaVar.zzbuY)) {
            return false;
        }
        if (this.zzbuZ == null) {
            if (zzclaVar.zzbuZ != null) {
                return false;
            }
        } else if (!this.zzbuZ.equals(zzclaVar.zzbuZ)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzclaVar.zzcuW == null || zzclaVar.zzcuW.isEmpty() : this.zzcuW.equals(zzclaVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbuZ == null ? 0 : this.zzbuZ.hashCode()) + (((this.zzbuY == null ? 0 : this.zzbuY.hashCode()) + (((this.zzbuX == null ? 0 : this.zzbuX.hashCode()) + (((this.zzbuW == null ? 0 : this.zzbuW.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31;
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
                    if (this.zzbuW == null) {
                        this.zzbuW = new zzcld();
                    }
                    ahwVar.zzb(this.zzbuW);
                    break;
                case 18:
                    if (this.zzbuX == null) {
                        this.zzbuX = new zzclb();
                    }
                    ahwVar.zzb(this.zzbuX);
                    break;
                case 24:
                    this.zzbuY = Boolean.valueOf(ahwVar.zzLT());
                    break;
                case 34:
                    this.zzbuZ = ahwVar.readString();
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
        if (this.zzbuW != null) {
            ahxVar.zza(1, this.zzbuW);
        }
        if (this.zzbuX != null) {
            ahxVar.zza(2, this.zzbuX);
        }
        if (this.zzbuY != null) {
            ahxVar.zzk(3, this.zzbuY.booleanValue());
        }
        if (this.zzbuZ != null) {
            ahxVar.zzl(4, this.zzbuZ);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbuW != null) {
            iZzn += ahx.zzb(1, this.zzbuW);
        }
        if (this.zzbuX != null) {
            iZzn += ahx.zzb(2, this.zzbuX);
        }
        if (this.zzbuY != null) {
            this.zzbuY.booleanValue();
            iZzn += ahx.zzcs(3) + 1;
        }
        return this.zzbuZ != null ? iZzn + ahx.zzm(4, this.zzbuZ) : iZzn;
    }
}
